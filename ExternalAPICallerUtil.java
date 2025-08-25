package com.api.remitGuru.component.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ExternalAPICallerUtil {
	final static String FILENAME = ExternalAPICallerUtil.class.getSimpleName()+".java";
	final static String LOGNAME = ExternalAPICallerUtil.class.getSimpleName();

	public String callPostApi(String urlString, ApiRequest request, Map<String, String> headers) throws IOException {
        long startTime = System.currentTimeMillis();
		Logger.log("------------- callPostApi() started at : "+ startTime+" -------------", FILENAME, LOGNAME, null, Logger.INFO);

		Logger.log("urlString : "+ urlString, FILENAME, LOGNAME, null, Logger.INFO);
		Logger.log("request : "+ request.toJson(), FILENAME, LOGNAME, null, Logger.INFO);
		Logger.log("headers : "+ headers.toString(), FILENAME, LOGNAME, null, Logger.INFO);

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);

            // Set headers
            for (Map.Entry<String, String> header : headers.entrySet()) {
                conn.setRequestProperty(header.getKey(), header.getValue());
            }

            // Write body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = request.toJson().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
    		Logger.log("responseCode : "+ responseCode, FILENAME, LOGNAME, null, Logger.INFO);

            InputStream is = (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) ?
                             conn.getInputStream() : conn.getErrorStream();

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

    		Logger.log("response : "+ response, FILENAME, LOGNAME, null, Logger.INFO);

            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("API call to " + urlString + " took " + elapsedTime + " ms");

    		Logger.log("elapsedTime : "+ elapsedTime+" ms", FILENAME, LOGNAME, null, Logger.INFO);

            Logger.log("------------- callPostApi() ended at : "+ System.currentTimeMillis() +" -------------", FILENAME, LOGNAME, null, Logger.INFO);

            return response.toString();
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
