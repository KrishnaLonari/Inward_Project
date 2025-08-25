package com.api.remitGuru.component.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

public class TransactionDataObj implements java.io.Serializable {
	
	private Hashtable extraParam = new Hashtable();
	private String  txnErrCode = null; 
	private String  txnErrMsg = null;
	private HashMap txnErrParam = new HashMap();
	
	private String sendGroupId = null;
	private String sendUserId = null;
	private String sendRoleId = null;
	private String sendRoleName = null;
	private String sendRoleType = null;
	private String sendLoginId = null;
	private String sendEmailId = null;
	private String sendRegistrationDate = null;
	private String sendFirstName = null;
	private String sendMiddleName = null;
	private String sendLastName = null;
	private String sendGender = null;	
	
	private String sendCountryCode = null;
	private String sendCurrencyCode = null;
	private String recvCountryCode = null;
	private String recvCurrencyCode = null;
	private String sendModeCode = null;
	private String receiveModeCode = null;
	private String programCode = null;
	
	private String transactionPurpose = null;
	private String transactionPersonalMessage = null;
	
	private String transactionAmount = "0";
	private String transactionNetAmount = "0";
	private String transactionDestinationAmount = "0";
	private String transactionDestinationAmountBeforePromo = "0";
	private String netConversionRateBeforePromo = "0";
	private String transactionAmountBeforePromo = "0";
	
	private String outwardTransactionFlag = "N";
	
	private String recvGroupId = null;
	private String recvUserId = null;
	private String recvLoginId = null;    
	private String recvNickName = null;
	private String recvRoleId = null;
	private String recvRoleType = null;
		
	private String achAccId = null;
	private String achAccountHolder = null;
	private String achBankName = null;
	private String routingNo = null;
	private String achAccountNo = null;
	private String achAccountType = null;
	private String achRecordStatus = null;
	private String isAccountVerified = null;
	
	private String amount1 = null;
	private String amount2 = null;
	private String amount3 = null;
	
	private String wireBankName = null;
	private String wireBankBranch = null;

	private String cipBankName = null;
	private String cipAccountNo = null;
	private String cipBankBranch = null;
	
	private String ibtrBankName = null;
	private String ibtrAccountNo = null;
	private String ibtrBankBranch = null;
	private String traceNo = null;
	
	private String ssn = null;
	private String sin = null;
	
	private boolean isRiskLimitDefine = false;
	private boolean isGlobalRiskLimitApplicable = false;
	private boolean isRiskLimitApplicable= false;
	private boolean isAmountInRange = false;
	private boolean isSenderRiskLimitCompatible = false;
	private boolean isRiskLimitCompatible = false;

	private boolean isFeeDefine = false;
	private String groupTxnFee = "0";
	private String isGroupFeeApplicable = null;
	private String custTxnFee = "0";
	private String isCustFeeApplicable = null;
	private String totalFee = "0";
	private String feeFlatPercFlag = null;
	private String feeType = null;
	private String isOptionalFeeApplicable = "N";
	private String feeTax = "0";
	
	private boolean isFixFeeDefine = false;
	private String fixGroupTxnFee = "0";
	private String fixIsGroupFeeApplicable = null;
	private String fixCustTxnFee = "0";
	private String fixIsCustFeeApplicable = null;
	private String fixFlatPercFlag = null;
	private String fixFeeType = null;	
	
	private boolean isCurrencyRateDefine = false;
	private String conversionRate = "0";
	private String groupTxnSpread = "0";
	private String isGroupSpreadApplicable = null;
	private String rgTxnSpread = "0";
	private String isRGSpreadApplicable = null;
	private String merchTxnSpread = "0";
	private String isMerchSpreadApplicable = null;
	private String totalSpread = "0";
	private String currRateFlatPercFlag = null;
	private String netConversionRate = "0";
	private String groupConvSpread = "0";
	private String isGroupConvSpreadApplicable = null;
	private String rgConvSpread = "0";
	private String isRGConvSpreadApplicable = null;
	private String totalConvSpread = "0";
	
	private boolean isFixConversionRateDefine = false;
	private String fixConversionRate = "0";
	private String fixGroupTxnSpread = "0";
	private String fixIsGroupSpreadApplicable = null;
	private String fixRgTxnSpread = "0";
	private String fixIsRGSpreadApplicable = null;
	private String fixMerchTxnSpread = "0";
	private String fixIsMerchSpreadApplicable = null;
	private String fixTotalSpread = "0";
	private String fixCurrRateFlatPercFlag = null;
	private String fixOutwardFlag = "N";

	private boolean isServiceChargeDefine = false;
	private String groupServiceCharge = "0";
	private String isGroupSvcChargeApplicable = null;
	private String custServiceCharge = "0";
	private String isCustSvcChargeApplicable = null;
	private String serviceChargeFlatPercFlag = null;
	private String totalServiceCharge = "0";
	private String fircFee = "0";
	private String fircFlag = "N";
	private String merchServiceCharge = "0";
	private String isMerchSvcChargeApplicable  = null;
	private String merchServiceChargeFlatPercFlag  = null;
	
	private boolean isServiceTaxDefine = false;
	private String compoundType = null;
	private String defaultServTaxFlat = null;
	private String servTax = "0";
	private String servTaxFlatPercFlag = null;
	private String eduCessTax = "0";
	private String eduCessFlatPercFlag = null;
	private String highEduCessTax = "0";
	private String highEduCessFlatPercFlag = null;
	private String maxServTaxAllowed = "0";
	private String maxServTaxAllowedApplicable = null;
	
	private String rgtn = null;	
	private String txnRefNumber = null;
	private String groupTransactionId = "N.A.";
		
	private String webName = null;
	private String ipAddress = null;
	private String sessionId = null;
	private String txnType = null;
	
	private boolean isKycTimePresent = false;
	private String kycCallingDate = null;
	private String kycFromTime = null;
	private String kycToTime = null;
	private String kycFromTime1 = null;
	private String kycToTime1 = null;
	private String kycSenderCallingDate = null;
	private String kycSenderTimeFrom = null;
	private String kycSenderTimeFrom1 = null;
	private String kycSenderTimeZone = null;
	
	private String promoCode = null;
	private boolean isVerificationRequired = false;	
	
	private String loyaltyCategory = null;
	private String loyaltyRank = null;
	private String outwardCorridorFlag = null;
	private String outwardFlag = null;
	private String promoValue = null;
	private String promoText = null;
	private String promoType = null;
	private String expDeliveryDate = null;
	private String senderAccountHolder = null;
	private String recvBankDetails = null;
	private String recvSubPurpose = null;
	private String accountType = null;
	
	private String typeOfQuote = null;
	private String branchCode = null;	
	private String tellerLoginId = null;	
	private String channel = null;
	
	private String isFCNR = null;
	
	private String feeBeforePromo=null;
	
	public String getFeeBeforePromo() {
		return feeBeforePromo;
	}
	public void setFeeBeforePromo(String feeBeforePromo) {
		this.feeBeforePromo = feeBeforePromo;
	}
	
	private String recvBankId=null;
	
	public String getrecvBankId() {
		return recvBankId;
	}
	public void setrecvBankIdo(String recvBankId) {
		this.recvBankId = recvBankId;
	}

	public String getParam(String paramName) {
		String val = "";
		try
		{
			val = extraParam.get(paramName).toString();
		}catch(Throwable t){
			val = "";
		}
		return val;
	}
	
	public Hashtable returnParam() {
		return extraParam;
	}

	public void addParam(String paramName,String paramValue) {
		this.extraParam.put(paramName, paramValue);
	}
	
	// changes done for australia added two new fields
	private String swiftCode = null;
	private String bsbCode = null;
	
	//Ip data added
	private ArrayList userIPData = null;
	
	
	public ArrayList getUserIPData() {
		return userIPData;
	}
	public void setUserIPData(ArrayList userIPData) {
		this.userIPData = userIPData;
	}
	public String getSwiftCode() {
		return swiftCode;
	}
	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}
	public String getBsbCode() {
		return bsbCode;
	}
	public void setBsbCode(String bsbCode) {
		this.bsbCode = bsbCode;
	}
	
	
	/**
	 * @return the txnErrCode
	 */
	public String getTxnErrCode() {
		return txnErrCode;
	}
	/**
	 * @param txnErrCode the txnErrCode to set
	 */
	public void setTxnErrCode(String txnErrCode) {
		this.txnErrCode = txnErrCode;
	}
	/**
	 * @return the txnErrMsg
	 */
	public String getTxnErrMsg() {
		return txnErrMsg;
	}
	/**
	 * @param txnErrMsg the txnErrMsg to set
	 */
	public void setTxnErrMsg(String txnErrMsg) {
		this.txnErrMsg = txnErrMsg;
	}
	/**
	 * @return the txnErrParam
	 */
	public HashMap getTxnErrParam() {
		return txnErrParam;
	}
	/**
	 * @param txnErrParam the txnErrParam to set
	 */
	public void setTxnErrParam(HashMap txnErrParam) {
		this.txnErrParam = txnErrParam;
	}
	/**
	 * @return the sendGroupId
	 */
	public String getSendGroupId() {
		return sendGroupId;
	}
	/**
	 * @param sendGroupId the sendGroupId to set
	 */
	public void setSendGroupId(String sendGroupId) {
		this.sendGroupId = sendGroupId;
	}
	/**
	 * @return the sendUserId
	 */
	public String getSendUserId() {
		return sendUserId;
	}
	/**
	 * @param sendUserId the sendUserId to set
	 */
	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}
	/**
	 * @return the sendRoleId
	 */
	public String getSendRoleId() {
		return sendRoleId;
	}
	/**
	 * @param sendRoleId the sendRoleId to set
	 */
	public void setSendRoleId(String sendRoleId) {
		this.sendRoleId = sendRoleId;
	}
	/**
	 * @return the sendRoleName
	 */
	public String getSendRoleName() {
		return sendRoleName;
	}
	/**
	 * @param sendRoleName the sendRoleName to set
	 */
	public void setSendRoleName(String sendRoleName) {
		this.sendRoleName = sendRoleName;
	}
	/**
	 * @return the sendRoleType
	 */
	public String getSendRoleType() {
		return sendRoleType;
	}
	/**
	 * @param sendRoleType the sendRoleType to set
	 */
	public void setSendRoleType(String sendRoleType) {
		this.sendRoleType = sendRoleType;
	}
	/**
	 * @return the sendLoginId
	 */
	public String getSendLoginId() {
		return sendLoginId;
	}
	/**
	 * @param sendLoginId the sendLoginId to set
	 */
	public void setSendLoginId(String sendLoginId) {
		this.sendLoginId = sendLoginId;
	}
	/**
	 * @return the sendEmailId
	 */
	public String getSendEmailId() {
		return sendEmailId;
	}
	/**
	 * @param sendEmailId the sendEmailId to set
	 */
	public void setSendEmailId(String sendEmailId) {
		this.sendEmailId = sendEmailId;
	}
	/**
	 * @return the sendRegistrationDate
	 */
	public String getSendRegistrationDate() {
		return sendRegistrationDate;
	}
	/**
	 * @param sendRegistrationDate the sendRegistrationDate to set
	 */
	public void setSendRegistrationDate(String sendRegistrationDate) {
		this.sendRegistrationDate = sendRegistrationDate;
	}
	/**
	 * @return the sendFirstName
	 */
	public String getSendFirstName() {
		return sendFirstName;
	}
	/**
	 * @param sendFirstName the sendFirstName to set
	 */
	public void setSendFirstName(String sendFirstName) {
		this.sendFirstName = sendFirstName;
	}
	/**
	 * @return the sendMiddleName
	 */
	public String getSendMiddleName() {
		return sendMiddleName;
	}
	/**
	 * @param sendMiddleName the sendMiddleName to set
	 */
	public void setSendMiddleName(String sendMiddleName) {
		this.sendMiddleName = sendMiddleName;
	}
	/**
	 * @return the sendLastName
	 */
	public String getSendLastName() {
		return sendLastName;
	}
	/**
	 * @param sendLastName the sendLastName to set
	 */
	public void setSendLastName(String sendLastName) {
		this.sendLastName = sendLastName;
	}
	/**
	 * @return the sendGender
	 */
	public String getSendGender() {
		return sendGender;
	}
	/**
	 * @param sendGender the sendGender to set
	 */
	public void setSendGender(String sendGender) {
		this.sendGender = sendGender;
	}
	/**
	 * @return the sendCountryCode
	 */
	public String getSendCountryCode() {
		return sendCountryCode;
	}
	/**
	 * @param sendCountryCode the sendCountryCode to set
	 */
	public void setSendCountryCode(String sendCountryCode) {
		this.sendCountryCode = sendCountryCode;
	}
	/**
	 * @return the sendCurrencyCode
	 */
	public String getSendCurrencyCode() {
		return sendCurrencyCode;
	}
	/**
	 * @param sendCurrencyCode the sendCurrencyCode to set
	 */
	public void setSendCurrencyCode(String sendCurrencyCode) {
		this.sendCurrencyCode = sendCurrencyCode;
	}
	/**
	 * @return the recvCountryCode
	 */
	public String getRecvCountryCode() {
		return recvCountryCode;
	}
	/**
	 * @param recvCountryCode the recvCountryCode to set
	 */
	public void setRecvCountryCode(String recvCountryCode) {
		this.recvCountryCode = recvCountryCode;
	}
	/**
	 * @return the recvCurrencyCode
	 */
	public String getRecvCurrencyCode() {
		return recvCurrencyCode;
	}
	/**
	 * @param recvCurrencyCode the recvCurrencyCode to set
	 */
	public void setRecvCurrencyCode(String recvCurrencyCode) {
		this.recvCurrencyCode = recvCurrencyCode;
	}
	/**
	 * @return the sendModeCode
	 */
	public String getSendModeCode() {
		return sendModeCode;
	}
	/**
	 * @param sendModeCode the sendModeCode to set
	 */
	public void setSendModeCode(String sendModeCode) {
		this.sendModeCode = sendModeCode;
	}
	/**
	 * @return the receiveModeCode
	 */
	public String getReceiveModeCode() {
		return receiveModeCode;
	}
	/**
	 * @param receiveModeCode the receiveModeCode to set
	 */
	public void setReceiveModeCode(String receiveModeCode) {
		this.receiveModeCode = receiveModeCode;
	}
	/**
	 * @return the programCode
	 */
	public String getProgramCode() {
		return programCode;
	}
	/**
	 * @param programCode the programCode to set
	 */
	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}
	/**
	 * @return the transactionPurpose
	 */
	public String getTransactionPurpose() {
		return transactionPurpose;
	}
	/**
	 * @param transactionPurpose the transactionPurpose to set
	 */
	public void setTransactionPurpose(String transactionPurpose) {
		this.transactionPurpose = transactionPurpose;
	}
	/**
	 * @return the transactionPersonalMessage
	 */
	public String getTransactionPersonalMessage() {
		return transactionPersonalMessage;
	}
	/**
	 * @param transactionPersonalMessage the transactionPersonalMessage to set
	 */
	public void setTransactionPersonalMessage(String transactionPersonalMessage) {
		this.transactionPersonalMessage = transactionPersonalMessage;
	}
	/**
	 * @return the transactionAmount
	 */
	public String getTransactionAmount() {
		return transactionAmount;
	}
	/**
	 * @param transactionAmount the transactionAmount to set
	 */
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	/**
	 * @return the transactionNetAmount
	 */
	public String getTransactionNetAmount() {
		return transactionNetAmount;
	}
	/**
	 * @param transactionNetAmount the transactionNetAmount to set
	 */
	public void setTransactionNetAmount(String transactionNetAmount) {
		this.transactionNetAmount = transactionNetAmount;
	}
	/**
	 * @return the transactionDestinationAmount
	 */
	public String getTransactionDestinationAmount() {
		return transactionDestinationAmount;
	}
	/**
	 * @param transactionDestinationAmount the transactionDestinationAmount to set
	 */
	public void setTransactionDestinationAmount(String transactionDestinationAmount) {
		this.transactionDestinationAmount = transactionDestinationAmount;
	}
	/**
	 * @return the outwardTransactionFlag
	 */
	public String getOutwardTransactionFlag() {
		return outwardTransactionFlag;
	}
	/**
	 * @param outwardTransactionFlag the outwardTransactionFlag to set
	 */
	public void setOutwardTransactionFlag(String outwardTransactionFlag) {
		this.outwardTransactionFlag = outwardTransactionFlag;
	}
	/**
	 * @return the recvGroupId
	 */
	public String getRecvGroupId() {
		return recvGroupId;
	}
	/**
	 * @param recvGroupId the recvGroupId to set
	 */
	public void setRecvGroupId(String recvGroupId) {
		this.recvGroupId = recvGroupId;
	}
	/**
	 * @return the recvUserId
	 */
	public String getRecvUserId() {
		return recvUserId;
	}
	/**
	 * @param recvUserId the recvUserId to set
	 */
	public void setRecvUserId(String recvUserId) {
		this.recvUserId = recvUserId;
	}
	/**
	 * @return the recvLoginId
	 */
	public String getRecvLoginId() {
		return recvLoginId;
	}
	/**
	 * @param recvLoginId the recvLoginId to set
	 */
	public void setRecvLoginId(String recvLoginId) {
		this.recvLoginId = recvLoginId;
	}
	/**
	 * @return the recvNickName
	 */
	public String getRecvNickName() {
		return recvNickName;
	}
	/**
	 * @param recvNickName the recvNickName to set
	 */
	public void setRecvNickName(String recvNickName) {
		this.recvNickName = recvNickName;
	}
	/**
	 * @return the recvRoleId
	 */
	public String getRecvRoleId() {
		return recvRoleId;
	}
	/**
	 * @param recvRoleId the recvRoleId to set
	 */
	public void setRecvRoleId(String recvRoleId) {
		this.recvRoleId = recvRoleId;
	}
	/**
	 * @return the recvRoleType
	 */
	public String getRecvRoleType() {
		return recvRoleType;
	}
	/**
	 * @param recvRoleType the recvRoleType to set
	 */
	public void setRecvRoleType(String recvRoleType) {
		this.recvRoleType = recvRoleType;
	}
	/**
	 * @return the achAccId
	 */
	public String getAchAccId() {
		return achAccId;
	}
	/**
	 * @param achAccId the achAccId to set
	 */
	public void setAchAccId(String achAccId) {
		this.achAccId = achAccId;
	}
	/**
	 * @return the achAccountHolder
	 */
	public String getAchAccountHolder() {
		return achAccountHolder;
	}
	/**
	 * @param achAccountHolder the achAccountHolder to set
	 */
	public void setAchAccountHolder(String achAccountHolder) {
		this.achAccountHolder = achAccountHolder;
	}
	/**
	 * @return the achBankName
	 */
	public String getAchBankName() {
		return achBankName;
	}
	/**
	 * @param achBankName the achBankName to set
	 */
	public void setAchBankName(String achBankName) {
		this.achBankName = achBankName;
	}
	/**
	 * @return the routingNo
	 */
	public String getRoutingNo() {
		return routingNo;
	}
	/**
	 * @param routingNo the routingNo to set
	 */
	public void setRoutingNo(String routingNo) {
		this.routingNo = routingNo;
	}
	/**
	 * @return the achAccountNo
	 */
	public String getAchAccountNo() {
		return achAccountNo;
	}
	/**
	 * @param achAccountNo the achAccountNo to set
	 */
	public void setAchAccountNo(String achAccountNo) {
		this.achAccountNo = achAccountNo;
	}
	/**
	 * @return the achAccountType
	 */
	public String getAchAccountType() {
		return achAccountType;
	}
	/**
	 * @param achAccountType the achAccountType to set
	 */
	public void setAchAccountType(String achAccountType) {
		this.achAccountType = achAccountType;
	}
	/**
	 * @return the achRecordStatus
	 */
	public String getAchRecordStatus() {
		return achRecordStatus;
	}
	/**
	 * @param achRecordStatus the achRecordStatus to set
	 */
	public void setAchRecordStatus(String achRecordStatus) {
		this.achRecordStatus = achRecordStatus;
	}
	/**
	 * @return the isAccountVerified
	 */
	public String getIsAccountVerified() {
		return isAccountVerified;
	}
	/**
	 * @param isAccountVerified the isAccountVerified to set
	 */
	public void setIsAccountVerified(String isAccountVerified) {
		this.isAccountVerified = isAccountVerified;
	}
	/**
	 * @return the amount1
	 */
	public String getAmount1() {
		return amount1;
	}
	/**
	 * @param amount1 the amount1 to set
	 */
	public void setAmount1(String amount1) {
		this.amount1 = amount1;
	}
	/**
	 * @return the amount2
	 */
	public String getAmount2() {
		return amount2;
	}
	/**
	 * @param amount2 the amount2 to set
	 */
	public void setAmount2(String amount2) {
		this.amount2 = amount2;
	}
	/**
	 * @return the amount3
	 */
	public String getAmount3() {
		return amount3;
	}
	/**
	 * @param amount3 the amount3 to set
	 */
	public void setAmount3(String amount3) {
		this.amount3 = amount3;
	}
	/**
	 * @return the wireBankName
	 */
	public String getWireBankName() {
		return wireBankName;
	}
	/**
	 * @param wireBankName the wireBankName to set
	 */
	public void setWireBankName(String wireBankName) {
		this.wireBankName = wireBankName;
	}
	/**
	 * @return the wireBankBranch
	 */
	public String getWireBankBranch() {
		return wireBankBranch;
	}
	/**
	 * @param wireBankBranch the wireBankBranch to set
	 */
	public void setWireBankBranch(String wireBankBranch) {
		this.wireBankBranch = wireBankBranch;
	}
	/**
	 * @return the ibtrBankName
	 */
	public String getIbtrBankName() {
		return ibtrBankName;
	}
	/**
	 * @param ibtrBankName the ibtrBankName to set
	 */
	public void setIbtrBankName(String ibtrBankName) {
		this.ibtrBankName = ibtrBankName;
	}
	/**
	 * @return the ibtrAccountNo
	 */
	public String getIbtrAccountNo() {
		return ibtrAccountNo;
	}
	/**
	 * @param ibtrAccountNo the ibtrAccountNo to set
	 */
	public void setIbtrAccountNo(String ibtrAccountNo) {
		this.ibtrAccountNo = ibtrAccountNo;
	}
	/**
	 * @return the ibtrBankBranch
	 */
	public String getIbtrBankBranch() {
		return ibtrBankBranch;
	}
	/**
	 * @param ibtrBankBranch the ibtrBankBranch to set
	 */
	public void setIbtrBankBranch(String ibtrBankBranch) {
		this.ibtrBankBranch = ibtrBankBranch;
	}
	/**
	 * @return the traceNo
	 */
	public String getTraceNo() {
		return traceNo;
	}
	/**
	 * @param traceNo the traceNo to set
	 */
	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}
	/**
	 * @return the isRiskLimitDefine
	 */
	public boolean isRiskLimitDefine() {
		return isRiskLimitDefine;
	}
	/**
	 * @param isRiskLimitDefine the isRiskLimitDefine to set
	 */
	public void setRiskLimitDefine(boolean isRiskLimitDefine) {
		this.isRiskLimitDefine = isRiskLimitDefine;
	}
	/**
	 * @return the isGlobalRiskLimitApplicable
	 */
	public boolean isGlobalRiskLimitApplicable() {
		return isGlobalRiskLimitApplicable;
	}
	/**
	 * @param isGlobalRiskLimitApplicable the isGlobalRiskLimitApplicable to set
	 */
	public void setGlobalRiskLimitApplicable(boolean isGlobalRiskLimitApplicable) {
		this.isGlobalRiskLimitApplicable = isGlobalRiskLimitApplicable;
	}
	/**
	 * @return the isRiskLimitApplicable
	 */
	public boolean isRiskLimitApplicable() {
		return isRiskLimitApplicable;
	}
	/**
	 * @param isRiskLimitApplicable the isRiskLimitApplicable to set
	 */
	public void setRiskLimitApplicable(boolean isRiskLimitApplicable) {
		this.isRiskLimitApplicable = isRiskLimitApplicable;
	}
	/**
	 * @return the isAmountInRange
	 */
	public boolean isAmountInRange() {
		return isAmountInRange;
	}
	/**
	 * @param isAmountInRange the isAmountInRange to set
	 */
	public void setAmountInRange(boolean isAmountInRange) {
		this.isAmountInRange = isAmountInRange;
	}
	/**
	 * @return the isSenderRiskLimitCompatible
	 */
	public boolean isSenderRiskLimitCompatible() {
		return isSenderRiskLimitCompatible;
	}
	/**
	 * @param isSenderRiskLimitCompatible the isSenderRiskLimitCompatible to set
	 */
	public void setSenderRiskLimitCompatible(boolean isSenderRiskLimitCompatible) {
		this.isSenderRiskLimitCompatible = isSenderRiskLimitCompatible;
	}
	/**
	 * @return the isRiskLimitCompatible
	 */
	public boolean isRiskLimitCompatible() {
		return isRiskLimitCompatible;
	}
	/**
	 * @param isRiskLimitCompatible the isRiskLimitCompatible to set
	 */
	public void setRiskLimitCompatible(boolean isRiskLimitCompatible) {
		this.isRiskLimitCompatible = isRiskLimitCompatible;
	}
	/**
	 * @return the isFeeDefine
	 */
	public boolean isFeeDefine() {
		return isFeeDefine;
	}
	/**
	 * @param isFeeDefine the isFeeDefine to set
	 */
	public void setFeeDefine(boolean isFeeDefine) {
		this.isFeeDefine = isFeeDefine;
	}
	/**
	 * @return the groupTxnFee
	 */
	public String getGroupTxnFee() {
		return groupTxnFee;
	}
	/**
	 * @param groupTxnFee the groupTxnFee to set
	 */
	public void setGroupTxnFee(String groupTxnFee) {
		this.groupTxnFee = groupTxnFee;
	}
	/**
	 * @return the isGroupFeeApplicable
	 */
	public String getIsGroupFeeApplicable() {
		return isGroupFeeApplicable;
	}
	/**
	 * @param isGroupFeeApplicable the isGroupFeeApplicable to set
	 */
	public void setIsGroupFeeApplicable(String isGroupFeeApplicable) {
		this.isGroupFeeApplicable = isGroupFeeApplicable;
	}
	/**
	 * @return the custTxnFee
	 */
	public String getCustTxnFee() {
		return custTxnFee;
	}
	/**
	 * @param custTxnFee the custTxnFee to set
	 */
	public void setCustTxnFee(String custTxnFee) {
		this.custTxnFee = custTxnFee;
	}
	/**
	 * @return the isCustFeeApplicable
	 */
	public String getIsCustFeeApplicable() {
		return isCustFeeApplicable;
	}
	/**
	 * @param isCustFeeApplicable the isCustFeeApplicable to set
	 */
	public void setIsCustFeeApplicable(String isCustFeeApplicable) {
		this.isCustFeeApplicable = isCustFeeApplicable;
	}
	
	/**
	 * @return the isOptionalFeeApplicable
	 */
	public String getIsOptionalFeeApplicable() {
		return isOptionalFeeApplicable;
	}
	/**
	 * @param isOptionalFeeApplicable the isOptionalFeeApplicable to set
	 */
	public void setIsOptionalFeeApplicable(String isOptionalFeeApplicable) {
		this.isOptionalFeeApplicable = isOptionalFeeApplicable;
	}
	
	/**
	 * @return the totalFee
	 */
	public String getTotalFee() {
		return totalFee;
	}
	/**
	 * @param totalFee the totalFee to set
	 */
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	/**
	 * @return the feeFlatPercFlag
	 */
	public String getFeeFlatPercFlag() {
		return feeFlatPercFlag;
	}
	/**
	 * @param feeFlatPercFlag the feeFlatPercFlag to set
	 */
	public void setFeeFlatPercFlag(String feeFlatPercFlag) {
		this.feeFlatPercFlag = feeFlatPercFlag;
	}
	
	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	
	
	public boolean isFixFeeDefine() {
		return isFixFeeDefine;
	}

	public void setFixFeeDefine(boolean isFixFeeDefine) {
		this.isFixFeeDefine = isFixFeeDefine;
	}

	public String getFixGroupTxnFee() {
		return fixGroupTxnFee;
	}

	public void setFixGroupTxnFee(String fixGroupTxnFee) {
		this.fixGroupTxnFee = fixGroupTxnFee;
	}

	public String getFixIsGroupFeeApplicable() {
		return fixIsGroupFeeApplicable;
	}

	public void setFixIsGroupFeeApplicable(String fixIsGroupFeeApplicable) {
		this.fixIsGroupFeeApplicable = fixIsGroupFeeApplicable;
	}

	public String getFixCustTxnFee() {
		return fixCustTxnFee;
	}

	public void setFixCustTxnFee(String fixCustTxnFee) {
		this.fixCustTxnFee = fixCustTxnFee;
	}

	public String getFixIsCustFeeApplicable() {
		return fixIsCustFeeApplicable;
	}

	public void setFixIsCustFeeApplicable(String fixIsCustFeeApplicable) {
		this.fixIsCustFeeApplicable = fixIsCustFeeApplicable;
	}

	public String getFixFlatPercFlag() {
		return fixFlatPercFlag;
	}

	public void setFixFlatPercFlag(String fixFlatPercFlag) {
		this.fixFlatPercFlag = fixFlatPercFlag;
	}
	
	public String getFixFeeType() {
		return fixFeeType;
	}

	public void setFixFeeType(String fixFeeType) {
		this.fixFeeType = fixFeeType;
	}
	
	/**
	 * @return the isCurrencyRateDefine
	 */
	public boolean isCurrencyRateDefine() {
		return isCurrencyRateDefine;
	}
	/**
	 * @param isCurrencyRateDefine the isCurrencyRateDefine to set
	 */
	public void setCurrencyRateDefine(boolean isCurrencyRateDefine) {
		this.isCurrencyRateDefine = isCurrencyRateDefine;
	}
	/**
	 * @return the conversionRate
	 */
	public String getConversionRate() {
		return conversionRate;
	}
	/**
	 * @param conversionRate the conversionRate to set
	 */
	public void setConversionRate(String conversionRate) {
		this.conversionRate = conversionRate;
	}
	/**
	 * @return the groupTxnSpread
	 */
	public String getGroupTxnSpread() {
		return groupTxnSpread;
	}
	/**
	 * @param groupTxnSpread the groupTxnSpread to set
	 */
	public void setGroupTxnSpread(String groupTxnSpread) {
		this.groupTxnSpread = groupTxnSpread;
	}
	/**
	 * @return the isGroupSpreadApplicable
	 */
	public String getIsGroupSpreadApplicable() {
		return isGroupSpreadApplicable;
	}
	/**
	 * @param isGroupSpreadApplicable the isGroupSpreadApplicable to set
	 */
	public void setIsGroupSpreadApplicable(String isGroupSpreadApplicable) {
		this.isGroupSpreadApplicable = isGroupSpreadApplicable;
	}
	/**
	 * @return the rgTxnSpread
	 */
	public String getRgTxnSpread() {
		return rgTxnSpread;
	}
	/**
	 * @param rgTxnSpread the rgTxnSpread to set
	 */
	public void setRgTxnSpread(String rgTxnSpread) {
		this.rgTxnSpread = rgTxnSpread;
	}
	/**
	 * @return the isRGSpreadApplicable
	 */
	public String getIsRGSpreadApplicable() {
		return isRGSpreadApplicable;
	}
	/**
	 * @param isRGSpreadApplicable the isRGSpreadApplicable to set
	 */
	public void setIsRGSpreadApplicable(String isRGSpreadApplicable) {
		this.isRGSpreadApplicable = isRGSpreadApplicable;
	}
	/**
	 * @return the merchTxnSpread
	 */
	public String getMerchTxnSpread() {
		return merchTxnSpread;
	}
	/**
	 * @param merchTxnSpread the merchTxnSpread to set
	 */
	public void setMerchTxnSpread(String merchTxnSpread) {
		this.merchTxnSpread = merchTxnSpread;
	}
	/**
	 * @return the isMerchSpreadApplicable
	 */
	public String getIsMerchSpreadApplicable() {
		return isMerchSpreadApplicable;
	}
	/**
	 * @param isMerchSpreadApplicable the isMerchSpreadApplicable to set
	 */
	public void setIsMerchSpreadApplicable(String isMerchSpreadApplicable) {
		this.isMerchSpreadApplicable = isMerchSpreadApplicable;
	}
	/**
	 * @return the totalSpread
	 */
	public String getTotalSpread() {
		return totalSpread;
	}
	/**
	 * @param totalSpread the totalSpread to set
	 */
	public void setTotalSpread(String totalSpread) {
		this.totalSpread = totalSpread;
	}
	/**
	 * @return the currRateFlatPercFlag
	 */
	public String getCurrRateFlatPercFlag() {
		return currRateFlatPercFlag;
	}
	/**
	 * @param currRateFlatPercFlag the currRateFlatPercFlag to set
	 */
	public void setCurrRateFlatPercFlag(String currRateFlatPercFlag) {
		this.currRateFlatPercFlag = currRateFlatPercFlag;
	}
	/**
	 * @return the netConversionRate
	 */
	public String getNetConversionRate() {
		return netConversionRate;
	}
	/**
	 * @param netConversionRate the netConversionRate to set
	 */
	public void setNetConversionRate(String netConversionRate) {
		this.netConversionRate = netConversionRate;
	}
	/**
	 * @return the groupConvSpread
	 */
	public String getGroupConvSpread() {
		return groupConvSpread;
	}
	/**
	 * @param groupConvSpread the groupConvSpread to set
	 */
	public void setGroupConvSpread(String groupConvSpread) {
		this.groupConvSpread = groupConvSpread;
	}
	/**
	 * @return the isGroupConvSpreadApplicable
	 */
	public String getIsGroupConvSpreadApplicable() {
		return isGroupConvSpreadApplicable;
	}
	/**
	 * @param isGroupConvSpreadApplicable the isGroupConvSpreadApplicable to set
	 */
	public void setIsGroupConvSpreadApplicable(String isGroupConvSpreadApplicable) {
		this.isGroupConvSpreadApplicable = isGroupConvSpreadApplicable;
	}
	/**
	 * @return the rgConvSpread
	 */
	public String getRgConvSpread() {
		return rgConvSpread;
	}
	/**
	 * @param rgConvSpread the rgConvSpread to set
	 */
	public void setRgConvSpread(String rgConvSpread) {
		this.rgConvSpread = rgConvSpread;
	}
	/**
	 * @return the isRGConvSpreadApplicable
	 */
	public String getIsRGConvSpreadApplicable() {
		return isRGConvSpreadApplicable;
	}
	/**
	 * @param isRGConvSpreadApplicable the isRGConvSpreadApplicable to set
	 */
	public void setIsRGConvSpreadApplicable(String isRGConvSpreadApplicable) {
		this.isRGConvSpreadApplicable = isRGConvSpreadApplicable;
	}
	/**
	 * @return the totalConvSpread
	 */
	public String getTotalConvSpread() {
		return totalConvSpread;
	}
	/**
	 * @param totalConvSpread the totalConvSpread to set
	 */
	public void setTotalConvSpread(String totalConvSpread) {
		this.totalConvSpread = totalConvSpread;
	}
	/**
	 * @return the isServiceChargeDefine
	 */
	public boolean isServiceChargeDefine() {
		return isServiceChargeDefine;
	}
	/**
	 * @param isServiceChargeDefine the isServiceChargeDefine to set
	 */
	public void setServiceChargeDefine(boolean isServiceChargeDefine) {
		this.isServiceChargeDefine = isServiceChargeDefine;
	}
	/**
	 * @return the groupServiceCharge
	 */
	public String getGroupServiceCharge() {
		return groupServiceCharge;
	}
	/**
	 * @param groupServiceCharge the groupServiceCharge to set
	 */
	public void setGroupServiceCharge(String groupServiceCharge) {
		this.groupServiceCharge = groupServiceCharge;
	}
	/**
	 * @return the isGroupSvcChargeApplicable
	 */
	public String getIsGroupSvcChargeApplicable() {
		return isGroupSvcChargeApplicable;
	}
	/**
	 * @param isGroupSvcChargeApplicable the isGroupSvcChargeApplicable to set
	 */
	public void setIsGroupSvcChargeApplicable(String isGroupSvcChargeApplicable) {
		this.isGroupSvcChargeApplicable = isGroupSvcChargeApplicable;
	}
	/**
	 * @return the custServiceCharge
	 */
	public String getCustServiceCharge() {
		return custServiceCharge;
	}
	/**
	 * @param custServiceCharge the custServiceCharge to set
	 */
	public void setCustServiceCharge(String custServiceCharge) {
		this.custServiceCharge = custServiceCharge;
	}
	/**
	 * @return the isCustSvcChargeApplicable
	 */
	public String getIsCustSvcChargeApplicable() {
		return isCustSvcChargeApplicable;
	}
	/**
	 * @param isCustSvcChargeApplicable the isCustSvcChargeApplicable to set
	 */
	public void setIsCustSvcChargeApplicable(String isCustSvcChargeApplicable) {
		this.isCustSvcChargeApplicable = isCustSvcChargeApplicable;
	}
	/**
	 * @return the serviceChargeFlatPercFlag
	 */
	public String getServiceChargeFlatPercFlag() {
		return serviceChargeFlatPercFlag;
	}
	/**
	 * @param serviceChargeFlatPercFlag the serviceChargeFlatPercFlag to set
	 */
	public void setServiceChargeFlatPercFlag(String serviceChargeFlatPercFlag) {
		this.serviceChargeFlatPercFlag = serviceChargeFlatPercFlag;
	}
	/**
	 * @return the totalServiceCharge
	 */
	public String getTotalServiceCharge() {
		return totalServiceCharge;
	}
	/**
	 * @param totalServiceCharge the totalServiceCharge to set
	 */
	public void setTotalServiceCharge(String totalServiceCharge) {
		this.totalServiceCharge = totalServiceCharge;
	}
	/**
	 * @return the fircFee
	 */
	public String getFircFee() {
		return fircFee;
	}
	/**
	 * @param fircFee the fircFee to set
	 */
	public void setFircFee(String fircFee) {
		this.fircFee = fircFee;
	}
	/**
	 * @return the fircFlag
	 */
	public String getFircFlag() {
		return fircFlag;
	}
	/**
	 * @param fircFlag the fircFlag to set
	 */
	public void setFircFlag(String fircFlag) {
		this.fircFlag = fircFlag;
	}
	/**
	 * @return the merchServiceCharge
	 */
	public String getMerchServiceCharge() {
		return merchServiceCharge;
	}
	/**
	 * @param merchServiceCharge the merchServiceCharge to set
	 */
	public void setMerchServiceCharge(String merchServiceCharge) {
		this.merchServiceCharge = merchServiceCharge;
	}
	/**
	 * @return the isMerchSvcChargeApplicable
	 */
	public String getIsMerchSvcChargeApplicable() {
		return isMerchSvcChargeApplicable;
	}
	/**
	 * @param isMerchSvcChargeApplicable the isMerchSvcChargeApplicable to set
	 */
	public void setIsMerchSvcChargeApplicable(String isMerchSvcChargeApplicable) {
		this.isMerchSvcChargeApplicable = isMerchSvcChargeApplicable;
	}
	/**
	 * @return the merchServiceChargeFlatPercFlag
	 */
	public String getMerchServiceChargeFlatPercFlag() {
		return merchServiceChargeFlatPercFlag;
	}
	/**
	 * @param merchServiceChargeFlatPercFlag the merchServiceChargeFlatPercFlag to set
	 */
	public void setMerchServiceChargeFlatPercFlag(
			String merchServiceChargeFlatPercFlag) {
		this.merchServiceChargeFlatPercFlag = merchServiceChargeFlatPercFlag;
	}
	/**
	 * @return the isServiceTaxDefine
	 */
	public boolean isServiceTaxDefine() {
		return isServiceTaxDefine;
	}
	/**
	 * @param isServiceTaxDefine the isServiceTaxDefine to set
	 */
	public void setServiceTaxDefine(boolean isServiceTaxDefine) {
		this.isServiceTaxDefine = isServiceTaxDefine;
	}
	/**
	 * @return the compoundType
	 */
	public String getCompoundType() {
		return compoundType;
	}
	/**
	 * @param compoundType the compoundType to set
	 */
	public void setCompoundType(String compoundType) {
		this.compoundType = compoundType;
	}
	/**
	 * @return the defaultServTaxFlat
	 */
	public String getDefaultServTaxFlat() {
		return defaultServTaxFlat;
	}
	/**
	 * @param defaultServTaxFlat the defaultServTaxFlat to set
	 */
	public void setDefaultServTaxFlat(String defaultServTaxFlat) {
		this.defaultServTaxFlat = defaultServTaxFlat;
	}
	/**
	 * @return the servTax
	 */
	public String getServTax() {
		return servTax;
	}
	/**
	 * @param servTax the servTax to set
	 */
	public void setServTax(String servTax) {
		this.servTax = servTax;
	}
	/**
	 * @return the servTaxFlatPercFlag
	 */
	public String getServTaxFlatPercFlag() {
		return servTaxFlatPercFlag;
	}
	/**
	 * @param servTaxFlatPercFlag the servTaxFlatPercFlag to set
	 */
	public void setServTaxFlatPercFlag(String servTaxFlatPercFlag) {
		this.servTaxFlatPercFlag = servTaxFlatPercFlag;
	}
	/**
	 * @return the eduCessTax
	 */
	public String getEduCessTax() {
		return eduCessTax;
	}
	/**
	 * @param eduCessTax the eduCessTax to set
	 */
	public void setEduCessTax(String eduCessTax) {
		this.eduCessTax = eduCessTax;
	}
	/**
	 * @return the eduCessFlatPercFlag
	 */
	public String getEduCessFlatPercFlag() {
		return eduCessFlatPercFlag;
	}
	/**
	 * @param eduCessFlatPercFlag the eduCessFlatPercFlag to set
	 */
	public void setEduCessFlatPercFlag(String eduCessFlatPercFlag) {
		this.eduCessFlatPercFlag = eduCessFlatPercFlag;
	}
	/**
	 * @return the highEduCessTax
	 */
	public String getHighEduCessTax() {
		return highEduCessTax;
	}
	/**
	 * @param highEduCessTax the highEduCessTax to set
	 */
	public void setHighEduCessTax(String highEduCessTax) {
		this.highEduCessTax = highEduCessTax;
	}
	/**
	 * @return the highEduCessFlatPercFlag
	 */
	public String getHighEduCessFlatPercFlag() {
		return highEduCessFlatPercFlag;
	}
	/**
	 * @param highEduCessFlatPercFlag the highEduCessFlatPercFlag to set
	 */
	public void setHighEduCessFlatPercFlag(String highEduCessFlatPercFlag) {
		this.highEduCessFlatPercFlag = highEduCessFlatPercFlag;
	}
	/**
	 * @return the maxServTaxAllowed
	 */
	public String getMaxServTaxAllowed() {
		return maxServTaxAllowed;
	}
	/**
	 * @param maxServTaxAllowed the maxServTaxAllowed to set
	 */
	public void setMaxServTaxAllowed(String maxServTaxAllowed) {
		this.maxServTaxAllowed = maxServTaxAllowed;
	}
	/**
	 * @return the maxServTaxAllowedApplicable
	 */
	public String getMaxServTaxAllowedApplicable() {
		return maxServTaxAllowedApplicable;
	}
	/**
	 * @param maxServTaxAllowedApplicable the maxServTaxAllowedApplicable to set
	 */
	public void setMaxServTaxAllowedApplicable(String maxServTaxAllowedApplicable) {
		this.maxServTaxAllowedApplicable = maxServTaxAllowedApplicable;
	}
	/**
	 * @return the rgtn
	 */
	public String getRgtn() {
		return rgtn;
	}
	/**
	 * @param rgtn the rgtn to set
	 */
	public void setRgtn(String rgtn) {
		this.rgtn = rgtn;
	}
	/**
	 * @return the groupTransactionId
	 */
	public String getGroupTransactionId() {
		return groupTransactionId;
	}
	/**
	 * @param groupTransactionId the groupTransactionId to set
	 */
	public void setGroupTransactionId(String groupTransactionId) {
		this.groupTransactionId = groupTransactionId;
	}
	/**
	 * @return the webName
	 */
	public String getWebName() {
		return webName;
	}
	/**
	 * @param webName the webName to set
	 */
	public void setWebName(String webName) {
		this.webName = webName;
	}
	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/**
	 * @return the txnType
	 */
	public String getTxnType() {
		return txnType;
	}
	/**
	 * @param txnType the txnType to set
	 */
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	/**
	 * @return the isKycTimePresent
	 */
	public boolean isKycTimePresent() {
		return isKycTimePresent;
	}
	/**
	 * @param isKycTimePresent the isKycTimePresent to set
	 */
	public void setKycTimePresent(boolean isKycTimePresent) {
		this.isKycTimePresent = isKycTimePresent;
	}
	/**
	 * @return the kycCallingDate
	 */
	public String getKycCallingDate() {
		return kycCallingDate;
	}
	/**
	 * @param kycCallingDate the kycCallingDate to set
	 */
	public void setKycCallingDate(String kycCallingDate) {
		this.kycCallingDate = kycCallingDate;
	}
	/**
	 * @return the kycFromTime
	 */
	public String getKycFromTime() {
		return kycFromTime;
	}
	/**
	 * @param kycFromTime the kycFromTime to set
	 */
	public void setKycFromTime(String kycFromTime) {
		this.kycFromTime = kycFromTime;
	}
	/**
	 * @return the kycToTime
	 */
	public String getKycToTime() {
		return kycToTime;
	}
	/**
	 * @param kycToTime the kycToTime to set
	 */
	public void setKycToTime(String kycToTime) {
		this.kycToTime = kycToTime;
	}
	/**
	 * @return the kycFromTime1
	 */
	public String getKycFromTime1() {
		return kycFromTime1;
	}
	/**
	 * @param kycFromTime1 the kycFromTime1 to set
	 */
	public void setKycFromTime1(String kycFromTime1) {
		this.kycFromTime1 = kycFromTime1;
	}
	/**
	 * @return the kycToTime1
	 */
	public String getKycToTime1() {
		return kycToTime1;
	}
	/**
	 * @param kycToTime1 the kycToTime1 to set
	 */
	public void setKycToTime1(String kycToTime1) {
		this.kycToTime1 = kycToTime1;
	}
	/**
	 * @param ssn the ssn to set
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	/**
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}
	
	/**
	 * @param sin the sin to set
	 */
	public void setSin(String sin) {
		this.sin = sin;
	}
	/**
	 * @return the sin
	 */
	public String getSin() {
		return sin;
	}
	
	/**
	 * @param kycSenderCallingDate the kycSenderCallingDate to set
	 */
	public void setKycSenderCallingDate(String kycSenderCallingDate) {
		this.kycSenderCallingDate = kycSenderCallingDate;
	}
	/**
	 * @return the kycSenderCallingDate
	 */
	public String getKycSenderCallingDate() {
		return kycSenderCallingDate;
	}
	/**
	 * @param kycSenderTimeFrom the kycSenderTimeFrom to set
	 */
	public void setKycSenderTimeFrom(String kycSenderTimeFrom) {
		this.kycSenderTimeFrom = kycSenderTimeFrom;
	}
	/**
	 * @return the kycSenderTimeFrom
	 */
	public String getKycSenderTimeFrom() {
		return kycSenderTimeFrom;
	}
	/**
	 * @param kycSenderTimeFrom1 the kycSenderTimeFrom1 to set
	 */
	public void setKycSenderTimeFrom1(String kycSenderTimeFrom1) {
		this.kycSenderTimeFrom1 = kycSenderTimeFrom1;
	}
	/**
	 * @return the kycSenderTimeFrom1
	 */
	public String getKycSenderTimeFrom1() {
		return kycSenderTimeFrom1;
	}
	/**
	 * @param kycSenderTimeZone the kycSenderTimeZone to set
	 */
	public void setKycSenderTimeZone(String kycSenderTimeZone) {
		this.kycSenderTimeZone = kycSenderTimeZone;
	}
	/**
	 * @return the kycSenderTimeZone
	 */
	public String getKycSenderTimeZone() {
		return kycSenderTimeZone;
	}
	
	public String getTypeOfQuote() {
		return typeOfQuote;
	}

	public void setTypeOfQuote(String typeOfQuote) {
		this.typeOfQuote = typeOfQuote;
	}
	
	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getTellerLoginId() {
		return tellerLoginId;
	}

	public void setTellerLoginId(String tellerLoginId) {
		this.tellerLoginId = tellerLoginId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	public void resetTransactionDataObj()
	{
		sendGroupId = null;
		sendUserId = null;
		sendRoleId = null;
		sendRoleName = null;
		sendRoleType = null;
		sendLoginId = null;
		sendEmailId = null;
		sendRegistrationDate = null;
		sendFirstName = null;
		sendMiddleName = null;
		sendLastName = null;
		sendGender = null;	

		sendCountryCode = null;
		sendCurrencyCode = null;
		recvCountryCode = null;
		recvCurrencyCode = null;
		sendModeCode = null;
		receiveModeCode = null;
		programCode = null;

		transactionPurpose = null;
		transactionPersonalMessage = null;

		transactionAmount = "0";
		transactionNetAmount = "0";
		transactionDestinationAmount = "0";
		transactionDestinationAmountBeforePromo = "0";
		netConversionRateBeforePromo = "0";
		transactionAmountBeforePromo = "0";
		
		outwardTransactionFlag = "N";

		recvGroupId = null;
		recvUserId = null;
		recvLoginId = null;    
		recvNickName = null;
		recvRoleId = null;
		recvRoleType = null;

		achAccId = null;
		achAccountHolder = null;
		achBankName = null;
		routingNo = null;
		achAccountNo = null;
		achAccountType = null;
		achRecordStatus = null;
		isAccountVerified = null;
		
		amount1 = null;
		amount2 = null;
		amount3 = null;

		wireBankName = null;
		wireBankBranch = null;
		
		cipBankName = null;
		cipAccountNo = null;
		cipBankBranch = null;
		// added for australia
		swiftCode = null;
		bsbCode = null;
		

		ibtrBankName = null;
		ibtrAccountNo = null;
		ibtrBankBranch = null;
		traceNo = null;

		ssn = null;
		sin = null;

		isRiskLimitDefine = false;
		isGlobalRiskLimitApplicable = false;
		isRiskLimitApplicable= false;
		isAmountInRange = false;
		isSenderRiskLimitCompatible = false;
		isRiskLimitCompatible = false;

		isFeeDefine = false;
		groupTxnFee = "0";
		isGroupFeeApplicable = null;
		custTxnFee = "0";
		isCustFeeApplicable = null;
		totalFee = "0";
		feeFlatPercFlag = null;	
		feeType = null;
		isOptionalFeeApplicable = "N";
		setFeeTax("0");
		
		isFixFeeDefine = false;
		fixGroupTxnFee = "0";
		fixIsGroupFeeApplicable = null;
		fixCustTxnFee = "0";
		fixIsCustFeeApplicable = null;
		fixFlatPercFlag = null;
		fixFeeType = null;

		isCurrencyRateDefine = false;
		conversionRate = "0";
		groupTxnSpread = "0";
		isGroupSpreadApplicable = null;
		rgTxnSpread = "0";
		isRGSpreadApplicable = null;
		merchTxnSpread = "0";
		isMerchSpreadApplicable = null;
		totalSpread = "0";
		currRateFlatPercFlag = null;
		netConversionRate = "0";
		groupConvSpread = "0";
		isGroupConvSpreadApplicable = null;
		rgConvSpread = "0";
		isRGConvSpreadApplicable = null;
		totalConvSpread = "0";
		
		isFixConversionRateDefine = false;     
		fixConversionRate = "0";               
		fixGroupTxnSpread = "0";               
		fixIsGroupSpreadApplicable = null;     
		fixRgTxnSpread = "0";                  
		fixIsRGSpreadApplicable = null;        
		fixMerchTxnSpread = "0";               
		fixIsMerchSpreadApplicable = null;     
		fixTotalSpread = "0";                  
		fixCurrRateFlatPercFlag = null;        
		fixOutwardFlag = "N";
		
		isServiceChargeDefine = false;
		groupServiceCharge = "0";
		isGroupSvcChargeApplicable = null;
		custServiceCharge = "0";
		isCustSvcChargeApplicable = null;
		serviceChargeFlatPercFlag = null;
		totalServiceCharge = "0";
		fircFee = "0";
		fircFlag = "N";
		merchServiceCharge = "0";
		isMerchSvcChargeApplicable  = null;
		merchServiceChargeFlatPercFlag  = null;

		isServiceTaxDefine = false;
		compoundType = null;
		defaultServTaxFlat = null;
		servTax = "0";
		servTaxFlatPercFlag = null;
		eduCessTax = "0";
		eduCessFlatPercFlag = null;
		highEduCessTax = "0";
		highEduCessFlatPercFlag = null;
		maxServTaxAllowed = "0";
		maxServTaxAllowedApplicable = null;

		rgtn = null;
		txnRefNumber = null;
		groupTransactionId = "N.A.";

		webName = null;
		ipAddress = null;
		sessionId = null;
		txnType = null;
		promoCode = null;

		isKycTimePresent = false;
		kycCallingDate = null;
		kycFromTime = null;
		kycToTime = null;
		kycFromTime1 = null;
		kycToTime1 = null;
		isVerificationRequired = false;
		
		loyaltyCategory = null;
		loyaltyRank = null;
		outwardCorridorFlag = null;
		outwardFlag = null;
		promoValue = null;
		promoText = null;
		promoType = null;
		expDeliveryDate = null;
		senderAccountHolder = null;
		
		setKycSenderCallingDate(null);
		setKycSenderTimeFrom(null);
		setKycSenderTimeFrom1(null);
		setKycSenderTimeZone(null);
		
		userIPData = null;
		extraParam = new Hashtable();
		
		typeOfQuote = null;
		branchCode = null;
		tellerLoginId = null;
		channel = null;
		
		isFCNR = null;
	}	
	
	public String getIsFCNR() {
		return isFCNR;
	}

	public void setIsFCNR(String isFCNR) {
		this.isFCNR = isFCNR;
	}

	/**
	 * @param cipBankName the cipBankName to set
	 */
	public void setCipBankName(String cipBankName) {
		this.cipBankName = cipBankName;
	}
	/**
	 * @return the cipBankName
	 */
	public String getCipBankName() {
		return cipBankName;
	}
	/**
	 * @param cipAccountNo the cipAccountNo to set
	 */
	public void setCipAccountNo(String cipAccountNo) {
		this.cipAccountNo = cipAccountNo;
	}
	/**
	 * @return the cipAccountNo
	 */
	public String getCipAccountNo() {
		return cipAccountNo;
	}
	/**
	 * @param cipBankBranch the cipBankBranch to set
	 */
	public void setCipBankBranch(String cipBankBranch) {
		this.cipBankBranch = cipBankBranch;
	}
	/**
	 * @return the cipBankBranch
	 */
	public String getCipBankBranch() {
		return cipBankBranch;
	}
	/**
	 * @param promoCode the promoCode to set
	 */
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
	/**
	 * @return the promoCode
	 */
	public String getPromoCode() {
		return promoCode;
	}
	/**
	 * @return the isVerificationRequired
	 */
	public boolean isVerificationRequired() {
		return isVerificationRequired;
	}
	/**
	 * @param isVerificationRequired the isVerificationRequired to set
	 */
	public void setVerificationRequired(boolean isVerificationRequired) {
		this.isVerificationRequired = isVerificationRequired;
	}
	/**
	 * @return the txnRefNumber
	 */
	public String getTxnRefNumber() {
		return txnRefNumber;
	}
	/**
	 * @param txnRefNumber the txnRefNumber to set
	 */
	public void setTxnRefNumber(String txnRefNumber) {
		this.txnRefNumber = txnRefNumber;
	}
	
	public String getLoyaltyCategory() {
		return loyaltyCategory;
	}

	public void setLoyaltyCategory(String loyaltyCategory) {
		this.loyaltyCategory = loyaltyCategory;
	}

	public String getLoyaltyRank() {
		return loyaltyRank;
	}

	public void setLoyaltyRank(String loyaltyRank) {
		this.loyaltyRank = loyaltyRank;
	}

	public String getOutwardCorridorFlag() {
		return outwardCorridorFlag;
	}

	public void setOutwardCorridorFlag(String outwardCorridorFlag) {
		this.outwardCorridorFlag = outwardCorridorFlag;
	}

	public String getOutwardFlag() {
		return outwardFlag;
	}

	public void setOutwardFlag(String outwardFlag) {
		this.outwardFlag = outwardFlag;
	}

	public String getPromoValue() {
		return promoValue;
	}

	public void setPromoValue(String promoValue) {
		this.promoValue = promoValue;
	}

	public String getPromoText() {
		return promoText;
	}

	public void setPromoText(String promoText) {
		this.promoText = promoText;
	}

	public String getPromoType() {
		return promoType;
	}

	public void setPromoType(String promoType) {
		this.promoType = promoType;
	}

	public String getExpDeliveryDate() {
		return expDeliveryDate;
	}

	public void setExpDeliveryDate(String expDeliveryDate) {
		this.expDeliveryDate = expDeliveryDate;
	}

	public String getSenderAccountHolder() {
		return senderAccountHolder;
	}

	public void setSenderAccountHolder(String senderAccountHolder) {
		this.senderAccountHolder = senderAccountHolder;
	}
	
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public boolean isFixConversionRateDefine() {
		return isFixConversionRateDefine;
	}

	public void setFixConversionRateDefine(boolean isFixConversionRateDefine) {
		this.isFixConversionRateDefine = isFixConversionRateDefine;
	}

	public String getFixConversionRate() {
		return fixConversionRate;
	}

	public void setFixConversionRate(String fixConversionRate) {
		this.fixConversionRate = fixConversionRate;
	}

	public String getFixGroupTxnSpread() {
		return fixGroupTxnSpread;
	}

	public void setFixGroupTxnSpread(String fixGroupTxnSpread) {
		this.fixGroupTxnSpread = fixGroupTxnSpread;
	}

	public String getFixIsGroupSpreadApplicable() {
		return fixIsGroupSpreadApplicable;
	}

	public void setFixIsGroupSpreadApplicable(String fixIsGroupSpreadApplicable) {
		this.fixIsGroupSpreadApplicable = fixIsGroupSpreadApplicable;
	}

	public String getFixRgTxnSpread() {
		return fixRgTxnSpread;
	}

	public void setFixRgTxnSpread(String fixRgTxnSpread) {
		this.fixRgTxnSpread = fixRgTxnSpread;
	}

	public String getFixIsRGSpreadApplicable() {
		return fixIsRGSpreadApplicable;
	}

	public void setFixIsRGSpreadApplicable(String fixIsRGSpreadApplicable) {
		this.fixIsRGSpreadApplicable = fixIsRGSpreadApplicable;
	}

	public String getFixMerchTxnSpread() {
		return fixMerchTxnSpread;
	}

	public void setFixMerchTxnSpread(String fixMerchTxnSpread) {
		this.fixMerchTxnSpread = fixMerchTxnSpread;
	}

	public String getFixIsMerchSpreadApplicable() {
		return fixIsMerchSpreadApplicable;
	}

	public void setFixIsMerchSpreadApplicable(String fixIsMerchSpreadApplicable) {
		this.fixIsMerchSpreadApplicable = fixIsMerchSpreadApplicable;
	}

	public String getFixTotalSpread() {
		return fixTotalSpread;
	}

	public void setFixTotalSpread(String fixTotalSpread) {
		this.fixTotalSpread = fixTotalSpread;
	}

	public String getFixCurrRateFlatPercFlag() {
		return fixCurrRateFlatPercFlag;
	}

	public void setFixCurrRateFlatPercFlag(String fixCurrRateFlatPercFlag) {
		this.fixCurrRateFlatPercFlag = fixCurrRateFlatPercFlag;
	}
	
	public String getFixOutwardFlag() {
		return fixOutwardFlag;
	}

	public void setFixOutwardFlag(String fixOutwardFlag) {
		this.fixOutwardFlag = fixOutwardFlag;
	}
	
	public String getRecvSubPurpose() {
		return recvSubPurpose;
	}

	public void setRecvSubPurpose(String recvSubPurpose) {
		this.recvSubPurpose = recvSubPurpose;
	}
	
	public String getRecvBankDetails() {
		return recvBankDetails;
	}

	public void setRecvBankDetails(String recvBankDetails) {
		this.recvBankDetails = recvBankDetails;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		return "TransactionDataObj ["
				+ (txnErrCode != null ? "txnErrCode=" + txnErrCode + ", " : "")
				+ (txnErrMsg != null ? "txnErrMsg=" + txnErrMsg + ", " : "")
				+ (txnErrParam != null ? "txnErrParam="
						+ toString(txnErrParam.entrySet(), maxLen) + ", " : "")
				+ (sendGroupId != null ? "sendGroupId=" + sendGroupId + ", "
						: "")
				+ (sendUserId != null ? "sendUserId=" + sendUserId + ", " : "")
				+ (sendRoleId != null ? "sendRoleId=" + sendRoleId + ", " : "")
				+ (sendRoleName != null ? "sendRoleName=" + sendRoleName + ", "
						: "")
				+ (sendRoleType != null ? "sendRoleType=" + sendRoleType + ", "
						: "")
				+ (sendLoginId != null ? "sendLoginId=" + sendLoginId + ", "
						: "")
				+ (sendEmailId != null ? "sendEmailId=" + sendEmailId + ", "
						: "")
				+ (sendRegistrationDate != null ? "sendRegistrationDate="
						+ sendRegistrationDate + ", " : "")
				+ (sendFirstName != null ? "sendFirstName=" + sendFirstName
						+ ", " : "")
				+ (sendMiddleName != null ? "sendMiddleName=" + sendMiddleName
						+ ", " : "")
				+ (sendLastName != null ? "sendLastName=" + sendLastName + ", "
						: "")
				+ (sendGender != null ? "sendGender=" + sendGender + ", " : "")
				+ (sendCountryCode != null ? "sendCountryCode="
						+ sendCountryCode + ", " : "")
				+ (sendCurrencyCode != null ? "sendCurrencyCode="
						+ sendCurrencyCode + ", " : "")
				+ (recvCountryCode != null ? "recvCountryCode="
						+ recvCountryCode + ", " : "")
				+ (recvCurrencyCode != null ? "recvCurrencyCode="
						+ recvCurrencyCode + ", " : "")
				+ (sendModeCode != null ? "sendModeCode=" + sendModeCode + ", "
						: "")
				+ (receiveModeCode != null ? "receiveModeCode="
						+ receiveModeCode + ", " : "")
				+ (programCode != null ? "programCode=" + programCode + ", "
						: "")
				+ (transactionPurpose != null ? "transactionPurpose="
						+ transactionPurpose + ", " : "")
				+ (transactionPersonalMessage != null ? "transactionPersonalMessage="
						+ transactionPersonalMessage + ", "
						: "")
				+ (transactionAmount != null ? "transactionAmount="
						+ transactionAmount + ", " : "")
				+ (transactionNetAmount != null ? "transactionNetAmount="
						+ transactionNetAmount + ", " : "")
				+ (transactionDestinationAmount != null ? "transactionDestinationAmount="
						+ transactionDestinationAmount + ", "
						: "")
				+ (transactionDestinationAmountBeforePromo != null ? "transactionDestinationAmountBeforePromo="
						+ transactionDestinationAmountBeforePromo + ", "
						: "")
				+ (transactionAmountBeforePromo != null ? "transactionAmountBeforePromo="
						+ transactionAmountBeforePromo + ", "
						: "")
				+ (netConversionRateBeforePromo != null ? "NetConversionRateBeforePromo="
						+ netConversionRateBeforePromo + ", "
						: "")
				+ (outwardTransactionFlag != null ? "outwardTransactionFlag="
						+ outwardTransactionFlag + ", " : "")
				+ (recvGroupId != null ? "recvGroupId=" + recvGroupId + ", "
						: "")
				+ (recvUserId != null ? "recvUserId=" + recvUserId + ", " : "")
				+ (recvLoginId != null ? "recvLoginId=" + recvLoginId + ", "
						: "")
				+ (recvNickName != null ? "recvNickName=" + recvNickName + ", "
						: "")
				+ (recvRoleId != null ? "recvRoleId=" + recvRoleId + ", " : "")
				+ (recvRoleType != null ? "recvRoleType=" + recvRoleType + ", "
						: "")
				+ (achAccId != null ? "achAccId=" + achAccId + ", " : "")
				+ (achAccountHolder != null ? "achAccountHolder="
						+ achAccountHolder + ", " : "")
				+ (achBankName != null ? "achBankName=" + achBankName + ", "
						: "")
				+ (routingNo != null ? "routingNo=" + routingNo + ", " : "")
				+ (achAccountNo != null ? "achAccountNo=" + achAccountNo + ", "
						: "")
				+ (achAccountType != null ? "achAccountType=" + achAccountType
						+ ", " : "")
				+ (achRecordStatus != null ? "achRecordStatus="
						+ achRecordStatus + ", " : "")
				+ (isAccountVerified != null ? "isAccountVerified="
						+ isAccountVerified + ", " : "")
				+ (amount1 != null ? "amount1=" + amount1 + ", " : "")
				+ (amount2 != null ? "amount2=" + amount2 + ", " : "")
				+ (amount3 != null ? "amount3=" + amount3 + ", " : "")
				+ (wireBankName != null ? "wireBankName=" + wireBankName + ", "
						: "")
				+ (wireBankBranch != null ? "wireBankBranch=" + wireBankBranch
						+ ", " : "")
				+ (cipBankName != null ? "cipBankName=" + cipBankName + ", "
						: "")
				+ (cipAccountNo != null ? "cipAccountNo=" + cipAccountNo + ", "
						: "")
				+ (cipBankBranch != null ? "cipBankBranch=" + cipBankBranch
						+ ", " : "")
				+ (ibtrBankName != null ? "ibtrBankName=" + ibtrBankName + ", "
						: "")
				+ (ibtrAccountNo != null ? "ibtrAccountNo=" + ibtrAccountNo
						+ ", " : "")
				+ (ibtrBankBranch != null ? "ibtrBankBranch=" + ibtrBankBranch
						+ ", " : "")
				+ (swiftCode != null ? "swiftCode=" + swiftCode
						+ ", " : "")
				+ (bsbCode != null ? "bsbCode=" + bsbCode
						+ ", " : "")
				+ (traceNo != null ? "traceNo=" + traceNo + ", " : "")
				+ (ssn != null ? "ssn=" + ssn + ", " : "")
				+ (sin != null ? "sin=" + sin + ", " : "")
				+ "isRiskLimitDefine="
				+ isRiskLimitDefine
				+ ", isGlobalRiskLimitApplicable="
				+ isGlobalRiskLimitApplicable
				+ ", isRiskLimitApplicable="
				+ isRiskLimitApplicable
				+ ", isAmountInRange="
				+ isAmountInRange
				+ ", isSenderRiskLimitCompatible="
				+ isSenderRiskLimitCompatible
				+ ", isRiskLimitCompatible="
				+ isRiskLimitCompatible
				+ ", isFeeDefine="
				+ isFeeDefine
				+ ", "
				+ (groupTxnFee != null ? "groupTxnFee=" + groupTxnFee + ", "
						: "")
				+ (isGroupFeeApplicable != null ? "isGroupFeeApplicable="
						+ isGroupFeeApplicable + ", " : "")
				+ (custTxnFee != null ? "custTxnFee=" + custTxnFee + ", " : "")
				+ (isCustFeeApplicable != null ? "isCustFeeApplicable="
						+ isCustFeeApplicable + ", " : "")
				+ (isOptionalFeeApplicable != null ? "isOptionalFeeApplicable="
						+ isOptionalFeeApplicable + ", " : "")
				+ (totalFee != null ? "totalFee=" + totalFee + ", " : "")
				+ (feeFlatPercFlag != null ? "feeFlatPercFlag="
						+ feeFlatPercFlag + ", " : "")
				+ (feeType != null ? "feeType="
						+ feeType + ", " : "")
				+ "isCurrencyRateDefine="
				+ isCurrencyRateDefine
				+ ", "
				+ (conversionRate != null ? "conversionRate=" + conversionRate
						+ ", " : "")
				+ (groupTxnSpread != null ? "groupTxnSpread=" + groupTxnSpread
						+ ", " : "")
				+ (isGroupSpreadApplicable != null ? "isGroupSpreadApplicable="
						+ isGroupSpreadApplicable + ", " : "")
				+ (rgTxnSpread != null ? "rgTxnSpread=" + rgTxnSpread + ", "
						: "")
				+ (isRGSpreadApplicable != null ? "isRGSpreadApplicable="
						+ isRGSpreadApplicable + ", " : "")
				+ (merchTxnSpread != null ? "merchTxnSpread=" + merchTxnSpread
						+ ", " : "")
				+ (isMerchSpreadApplicable != null ? "isMerchSpreadApplicable="
						+ isMerchSpreadApplicable + ", " : "")
				+ (totalSpread != null ? "totalSpread=" + totalSpread + ", "
						: "")
				+ (currRateFlatPercFlag != null ? "currRateFlatPercFlag="
						+ currRateFlatPercFlag + ", " : "")
				+ (netConversionRate != null ? "netConversionRate="
						+ netConversionRate + ", " : "")
				+ (groupConvSpread != null ? "groupConvSpread="
						+ groupConvSpread + ", " : "")
				+ (isGroupConvSpreadApplicable != null ? "isGroupConvSpreadApplicable="
						+ isGroupConvSpreadApplicable + ", "
						: "")
				+ (rgConvSpread != null ? "rgConvSpread=" + rgConvSpread + ", "
						: "")
				+ (isRGConvSpreadApplicable != null ? "isRGConvSpreadApplicable="
						+ isRGConvSpreadApplicable + ", "
						: "")
				+ (totalConvSpread != null ? "totalConvSpread="
						+ totalConvSpread + ", " : "")
				+ "isServiceChargeDefine="
				+ isServiceChargeDefine
				+ ", "
				+ (groupServiceCharge != null ? "groupServiceCharge="
						+ groupServiceCharge + ", " : "")
				+ (isGroupSvcChargeApplicable != null ? "isGroupSvcChargeApplicable="
						+ isGroupSvcChargeApplicable + ", "
						: "")
				+ (custServiceCharge != null ? "custServiceCharge="
						+ custServiceCharge + ", " : "")
				+ (isCustSvcChargeApplicable != null ? "isCustSvcChargeApplicable="
						+ isCustSvcChargeApplicable + ", "
						: "")
				+ (serviceChargeFlatPercFlag != null ? "serviceChargeFlatPercFlag="
						+ serviceChargeFlatPercFlag + ", "
						: "")
				+ (totalServiceCharge != null ? "totalServiceCharge="
						+ totalServiceCharge + ", " : "")
				+ (fircFee != null ? "fircFee=" + fircFee + ", " : "")
				+ (fircFlag != null ? "fircFlag=" + fircFlag + ", " : "")
				+ (merchServiceCharge != null ? "merchServiceCharge="
						+ merchServiceCharge + ", " : "")
				+ (isMerchSvcChargeApplicable != null ? "isMerchSvcChargeApplicable="
						+ isMerchSvcChargeApplicable + ", "
						: "")
				+ (merchServiceChargeFlatPercFlag != null ? "merchServiceChargeFlatPercFlag="
						+ merchServiceChargeFlatPercFlag + ", "
						: "")
				+ "isServiceTaxDefine="
				+ isServiceTaxDefine
				+ ", "
				+ (compoundType != null ? "compoundType=" + compoundType + ", "
						: "")
				+ (defaultServTaxFlat != null ? "defaultServTaxFlat="
						+ defaultServTaxFlat + ", " : "")
				+ (servTax != null ? "servTax=" + servTax + ", " : "")
				+ (servTaxFlatPercFlag != null ? "servTaxFlatPercFlag="
						+ servTaxFlatPercFlag + ", " : "")
				+ (eduCessTax != null ? "eduCessTax=" + eduCessTax + ", " : "")
				+ (eduCessFlatPercFlag != null ? "eduCessFlatPercFlag="
						+ eduCessFlatPercFlag + ", " : "")
				+ (highEduCessTax != null ? "highEduCessTax=" + highEduCessTax
						+ ", " : "")
				+ (highEduCessFlatPercFlag != null ? "highEduCessFlatPercFlag="
						+ highEduCessFlatPercFlag + ", " : "")
				+ (maxServTaxAllowed != null ? "maxServTaxAllowed="
						+ maxServTaxAllowed + ", " : "")
				+ (maxServTaxAllowedApplicable != null ? "maxServTaxAllowedApplicable="
						+ maxServTaxAllowedApplicable + ", "
						: "")
				+ (rgtn != null ? "rgtn=" + rgtn + ", " : "")
				+ (groupTransactionId != null ? "groupTransactionId="
						+ groupTransactionId + ", " : "")
				+ (webName != null ? "webName=" + webName + ", " : "")
				+ (ipAddress != null ? "ipAddress=" + ipAddress + ", " : "")
				+ (sessionId != null ? "sessionId=" + sessionId + ", " : "")
				+ (txnType != null ? "txnType=" + txnType + ", " : "")
				+ "isKycTimePresent="
				+ isKycTimePresent
				+ ", "
				+ (kycCallingDate != null ? "kycCallingDate=" + kycCallingDate
						+ ", " : "")
				+ (kycFromTime != null ? "kycFromTime=" + kycFromTime + ", "
						: "")
				+ (kycToTime != null ? "kycToTime=" + kycToTime + ", " : "")
				+ (kycFromTime1 != null ? "kycFromTime1=" + kycFromTime1 + ", "
						: "")
				+ (kycToTime1 != null ? "kycToTime1=" + kycToTime1 + ", " : "")
				+ (kycSenderCallingDate != null ? "kycSenderCallingDate="
						+ kycSenderCallingDate + ", " : "")
				+ (kycSenderTimeFrom != null ? "kycSenderTimeFrom="
						+ kycSenderTimeFrom + ", " : "")
				+ (kycSenderTimeFrom1 != null ? "kycSenderTimeFrom1="
						+ kycSenderTimeFrom1 + ", " : "")
				+ (kycSenderTimeZone != null ? "kycSenderTimeZone="
						+ kycSenderTimeZone + ", " : "")
				+ (promoCode != null ? "promoCode=" + promoCode + ", " : "")
				+ (branchCode != null ? "branchCode=" + branchCode + ", " : "")
				+ (tellerLoginId != null ? "tellerLoginId=" + tellerLoginId + ", " : "")
				+ (channel != null ? "channel=" + channel + ", " : "")
				+ "isVerificationRequired=" + isVerificationRequired 
				+ ", loyaltyCategory=" + loyaltyCategory
				+ ", loyaltyRank=" + loyaltyRank
				+ ", outwardCorridorFlag=" + outwardCorridorFlag
				+ ", outwardFlag=" + outwardFlag
				+ ", promoValue=" + promoValue
				+ ", promoText=" + promoText
				+ ", promoType=" + promoType
				+ ", expDeliveryDate=" + expDeliveryDate
				+ ", senderAccountHolder=" + senderAccountHolder
				+ ", isFixConversionRateDefine=" + isFixConversionRateDefine				
				+ ", fixConversionRate=" + fixConversionRate
				+ ", fixGroupTxnSpread=" + fixGroupTxnSpread
				+ ", fixRgTxnSpread=" + fixRgTxnSpread
				+ ", fixTotalSpread=" + fixTotalSpread
				+ ", fixCurrRateFlatPercFlag=" + fixCurrRateFlatPercFlag
				+ ", fixOutwardFlag=" + fixOutwardFlag
				+ ", isFixFeeDefine=" + isFixFeeDefine
				+ ", fixGroupTxnFee=" + fixGroupTxnFee
				+ ", fixIsGroupFeeApplicable=" + fixIsGroupFeeApplicable
				+ ", fixCustTxnFee=" + fixCustTxnFee
				+ ", fixIsCustFeeApplicable=" + fixIsCustFeeApplicable
				+ ", fixFlatPercFlag=" + fixFlatPercFlag
				+ ", fixFeeType=" + fixFeeType
				+ ", userIPData="+userIPData
				+ ", recvBankDetails="+recvBankDetails
				+ ", recvSubPurpose="+recvSubPurpose
				+ ", accountType="+accountType 
				+ ", typeOfQuote="+typeOfQuote 
				+"]";
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	public String getFeeTax() {
		return feeTax;
	}

	public void setFeeTax(String feeTax) {
		this.feeTax = feeTax;
	}

	public String getTransactionDestinationAmountBeforePromo() {
		return transactionDestinationAmountBeforePromo;
	}

	public void setTransactionDestinationAmountBeforePromo(String transactionDestinationAmountBeforePromo) {
		this.transactionDestinationAmountBeforePromo = transactionDestinationAmountBeforePromo;
	}

	public String getNetConversionRateBeforePromo() {
		return netConversionRateBeforePromo;
	}

	public void setNetConversionRateBeforePromo(String netConversionRateBeforePromo) {
		this.netConversionRateBeforePromo = netConversionRateBeforePromo;
	}

	public String getTransactionAmountBeforePromo() {
		return transactionAmountBeforePromo;
	}

	public void setTransactionAmountBeforePromo(String transactionAmountBeforePromo) {
		this.transactionAmountBeforePromo = transactionAmountBeforePromo;
	}	
	
}
