/**
 * 
 */
package com.api.remitGuru.component.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.util.Date;
import java.util.Iterator;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignature;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureList;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentVerifierBuilderProvider;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.util.io.Streams;

/**
 * @author mansi.gandhi
 *
 */
public class SignatureUtil {


	@SuppressWarnings("unchecked")
	public static PGPPublicKey readPublicKey(InputStream in) throws IOException, PGPException {
		in = org.bouncycastle.openpgp.PGPUtil.getDecoderStream(in);

		PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(in);

		//
		// we just loop through the collection till we find a key suitable for encryption, in the real
		// world you would probably want to be a bit smarter about this.
		//
		PGPPublicKey key = null;

		//
		// iterate through the key rings.
		//
		Iterator<PGPPublicKeyRing> rIt = pgpPub.getKeyRings();

		while (key == null && rIt.hasNext()) {
			PGPPublicKeyRing kRing = rIt.next();
			Iterator<PGPPublicKey> kIt = kRing.getPublicKeys();
			while (key == null && kIt.hasNext()) {
				PGPPublicKey k = kIt.next();

				if (k.isEncryptionKey()) {
					key = k;
				}
			}
		}

		if (key == null) {
			throw new IllegalArgumentException("Can't find encryption key in key ring.");
		}

		return key;
	}


	public static PGPSecretKey readSecretKey(InputStream in) throws IOException, PGPException {

		PGPSecretKeyRingCollection keyRingCollection = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(in));

		//
		// We just loop through the collection till we find a key suitable for signing.
		// In the real world you would probably want to be a bit smarter about this.
		//
		PGPSecretKey secretKey = null;

		Iterator rIt = keyRingCollection.getKeyRings();
		while (secretKey == null && rIt.hasNext()) {
			PGPSecretKeyRing keyRing = (PGPSecretKeyRing) rIt.next();
			Iterator kIt = keyRing.getSecretKeys();
			while (secretKey == null && kIt.hasNext()) {
				PGPSecretKey key = (PGPSecretKey) kIt.next();
				if (key.isSigningKey()) {
					secretKey = key;
				}
			}
		}

		// Validate secret key
		if (secretKey == null) {
			throw new IllegalArgumentException("Can't find private key in the key ring.");
		}
		if (!secretKey.isSigningKey()) {
			throw new IllegalArgumentException("Private key does not allow signing.");
		}
		if (secretKey.getPublicKey().isRevoked()) {
			throw new IllegalArgumentException("Private key has been revoked.");
		}
		//if (!hasKeyFlags(secretKey.getPublicKey(), KeyFlags.SIGN_DATA)) {
		//   throw new IllegalArgumentException("Key cannot be used for signing.");
		//}

		return secretKey;
	}
	/**
	 * Load a secret key ring collection from keyIn and find the secret key corresponding to
	 * keyID if it exists.
	 *
	 * @param keyIn input stream representing a key ring collection.
	 * @param keyID keyID we want.
	 * @param pass passphrase to decrypt secret key with.
	 * @return
	 * @throws IOException
	 * @throws PGPException
	 * @throws NoSuchProviderException
	 */
	public static PGPPrivateKey findSecretKey(InputStream keyIn, long keyID, char[] pass)
			throws IOException, PGPException, NoSuchProviderException
			{
		PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
				org.bouncycastle.openpgp.PGPUtil.getDecoderStream(keyIn));

		PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);

		if (pgpSecKey == null) {
			return null;
		}

		return pgpSecKey.extractPrivateKey(pass, "BC");

			}

	
	@SuppressWarnings("deprecation")
	public static void signAndEncryptFile(
            OutputStream out,
            String fileName,
            PGPPublicKey publicKey,
            PGPSecretKey secretKey,
            String password,
            boolean armor,
            boolean withIntegrityCheck ) throws Exception {

        // Initialize Bouncy Castle security provider
        Provider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

        if (armor) {
            out = new ArmoredOutputStream(out);
        }

        // Initialize encrypted data generator
        @SuppressWarnings("deprecation")
		PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(
                SymmetricKeyAlgorithmTags.TRIPLE_DES,
                withIntegrityCheck,
                new SecureRandom(),
                provider );
        encryptedDataGenerator.addMethod(publicKey);
        OutputStream encryptedOut = encryptedDataGenerator.open(out, new byte[1<<16]);

        // Initialize compressed data generator
        PGPCompressedDataGenerator compressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
        OutputStream compressedOut = compressedDataGenerator.open(encryptedOut, new byte [1<<16]);

        // Initialize signature generator
        PGPPrivateKey privateKey = secretKey.extractPrivateKey(password.toCharArray(), provider);
        PGPSignatureGenerator signatureGenerator = new PGPSignatureGenerator(
                secretKey.getPublicKey().getAlgorithm(),
                HashAlgorithmTags.SHA1,
                provider );
        signatureGenerator.initSign(PGPSignature.BINARY_DOCUMENT, privateKey);

        boolean firstTime = true;
        Iterator it = secretKey.getPublicKey().getUserIDs();
        while (it.hasNext() && firstTime) {
            PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();
            spGen.setSignerUserID(false, (String)it.next());
            signatureGenerator.setHashedSubpackets(spGen.generate());
            // Exit the loop after the first iteration
            firstTime = false;
        }
        signatureGenerator.generateOnePassVersion(false).encode(compressedOut);

        // Initialize literal data generator
        PGPLiteralDataGenerator literalDataGenerator = new PGPLiteralDataGenerator();
        OutputStream literalOut = literalDataGenerator.open(
                compressedOut,
                PGPLiteralData.BINARY,
                fileName,
                new Date(),
                new byte [1<<16] );

        // Main loop - read the "in" stream, compress, encrypt and write to the "out" stream
        FileInputStream in = new FileInputStream(fileName);
        byte[] buf = new byte[1<<16];
        int len;
        while ((len = in.read(buf)) > 0) {
            literalOut.write(buf, 0, len);
            signatureGenerator.update(buf, 0, len);
        }

        in.close();
        literalDataGenerator.close();
        // Generate the signature, compress, encrypt and write to the "out" stream
        signatureGenerator.generate().encode(compressedOut);
        compressedDataGenerator.close();
        encryptedDataGenerator.close();
        if (armor) {
            out.close();
        }
    }
	
	public static void decrypFileAndVerifySign(InputStream in, InputStream keyIn, char[] passwd, OutputStream fOut, InputStream publicKeyIn)
					throws Exception
    {
		Security.addProvider(new BouncyCastleProvider());

        try {
			in = org.bouncycastle.openpgp.PGPUtil.getDecoderStream(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PGPObjectFactory pgpF = new PGPObjectFactory(in);
		PGPEncryptedDataList enc = null;
		
		Object o = null;
		try {
			o = pgpF.nextObject();
			
			// the first object might be a PGP marker packet.
			if (o instanceof PGPEncryptedDataList) {
			    enc = (PGPEncryptedDataList) o;
			} else {
			    
					enc = (PGPEncryptedDataList) pgpF.nextObject();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// find the secret key		
		Iterator<PGPPublicKeyEncryptedData> it = enc.getEncryptedDataObjects();
		PGPPrivateKey sKey = null;
		PGPPublicKeyEncryptedData pbe = null;
		//PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(keyIn));
		
		while (sKey == null && it.hasNext()) {
		    pbe = (PGPPublicKeyEncryptedData) it.next();
		    try {
				sKey = findSecretKey(keyIn, pbe.getKeyID(), passwd);
			} catch (NoSuchProviderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PGPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (sKey == null) {
		    throw new IllegalArgumentException("secret key for message not found.");
		}
		
		InputStream clear = null;
		try {
			clear = pbe.getDataStream(
			        new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BC").build(sKey));
		} catch (PGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PGPObjectFactory plainFact = new PGPObjectFactory(clear);
		
		Object message = null;
		
		PGPOnePassSignatureList onePassSignatureList = null;
		PGPSignatureList signatureList = null;
		PGPCompressedData compressedData = null;
		
		try {
			message = plainFact.nextObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
		
		while (message != null) {
		    System.out.println((message.toString()));
		    if (message instanceof PGPCompressedData) {
		        compressedData = (PGPCompressedData) message;
		        try {
					plainFact = new PGPObjectFactory(compressedData.getDataStream());
				} catch (PGPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        try {
					message = plainFact.nextObject();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		
		    if (message instanceof PGPLiteralData) {
		        // have to read it and keep it somewhere.
		        try {
					Streams.pipeAll(((PGPLiteralData) message).getInputStream(), actualOutput);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    } else if (message instanceof PGPOnePassSignatureList) {
		        onePassSignatureList = (PGPOnePassSignatureList) message;
		    } else if (message instanceof PGPSignatureList) {
		        signatureList = (PGPSignatureList) message;
		    } else {
		       
					throw new PGPException("message unknown message type.");
				
		    }
		    try {
				message = plainFact.nextObject();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			actualOutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PGPPublicKey publicKey = null;
		byte[] output = actualOutput.toByteArray();
		if (onePassSignatureList == null || signatureList == null) {
		    throw new PGPException("Poor PGP. Signatures not found.");
		} else {
		
		    for (int i = 0; i < onePassSignatureList.size(); i++) {
		        PGPOnePassSignature ops = onePassSignatureList.get(0);
		        System.out.println("verifier : " + ops.getKeyID());
		        PGPPublicKeyRingCollection pgpRing;
				try {
					pgpRing = new PGPPublicKeyRingCollection(
					        PGPUtil.getDecoderStream(publicKeyIn));
					publicKey = pgpRing.getPublicKey(ops.getKeyID());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (PGPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        if (publicKey != null) {
		            try {
						ops.init(new JcaPGPContentVerifierBuilderProvider().setProvider("BC"), publicKey);
					} catch (PGPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            try {
						ops.update(output);
						PGPSignature signature = signatureList.get(i);
			            if (ops.verify(signature)) {
			                Iterator<?> userIds = publicKey.getUserIDs();
			                while (userIds.hasNext()) {
			                    String userId = (String) userIds.next();
			                    System.out.println("Signed by {}" + userId);
			                }
			                System.out.println("Signature verified");
			            } else {
			                throw new SignatureException("Signature verification failed");
			            }
					} catch (SignatureException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (PGPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            
		        }
		    }
		
		}
		
		if (pbe.isIntegrityProtected() && !pbe.verify()) {
		    throw new PGPException("Data is integrity protected but integrity is lost.");
		} else if (publicKey == null) {
		    throw new SignatureException("Signature not found");
		} else {
		    try {
				fOut.write(output);
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		}
	}

}
