package com.shap.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ApiClientSSL {
	public String restClientService(String[] requestParams) {

		// local variables
		LocalDateTime myDateObj = null;
		DateTimeFormatter myFormatObj = null;
	    String formattedDate = null;
		String arrPropertyKey[] = null;
		String arrPropertyValue[] = null;
		URL url = null;
		HttpsURLConnection conn = null;
		JSONObject rbodyjsonObject = null;
		OutputStreamWriter outputStreamWriter = null;
		String responseXML = null;
		String encoded = null;

		try {
			try {
				arrPropertyKey = new String[] {requestParams[5]};
				arrPropertyKey = requestParams[5].split(",");
				
				arrPropertyValue = new String[] {requestParams[6]};
				arrPropertyValue = requestParams[6].split(",");
			}catch (Exception e) {
				arrPropertyKey = null;
				arrPropertyValue = null;
			}

	        SSLContext ctx = SSLContext.getInstance("TLS");
	        ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
	        SSLContext.setDefault(ctx);
			
			url = new URL(requestParams[0]);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod(requestParams[1]);
			
			for (int i = 0; i < arrPropertyKey.length; i++) {
				if(arrPropertyKey[i].equalsIgnoreCase("Content-Type")) {
					conn.setRequestProperty(arrPropertyKey[i], arrPropertyValue[i]);				
				}else if(arrPropertyKey[i].equalsIgnoreCase("Accept")) {
					conn.setRequestProperty(arrPropertyKey[i], arrPropertyValue[i]);
				}else if(arrPropertyKey[i].equalsIgnoreCase("Api-Key")) {
					if(arrPropertyKey[i+1].equalsIgnoreCase("Token")) {
						conn.setRequestProperty(arrPropertyValue[i], arrPropertyValue[i+1]);
					}
				}else if(arrPropertyKey[i].equalsIgnoreCase("Basic")) {
					encoded = Base64.getEncoder().encodeToString((requestParams[3]+":"+requestParams[4]).getBytes(StandardCharsets.UTF_8));
					conn.setRequestProperty("Authorization", arrPropertyValue[i]+" "+encoded);
				}else if(arrPropertyKey[i].equalsIgnoreCase("Bearer")) {
					conn.setRequestProperty("Authorization", arrPropertyKey[i]+" "+arrPropertyValue[i]);
				}else {
					conn.setRequestProperty(arrPropertyKey[i], arrPropertyValue[i]);
				}
			}
	        conn.setHostnameVerifier(new HostnameVerifier() {
	        	public boolean verify(String arg0, SSLSession arg1) {
	        		return true;
	            }
	        });

	        conn.setDoOutput(true);
	        
			if(requestParams[2] != null) {
				rbodyjsonObject = new JSONObject(requestParams[2]);
				outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
				outputStreamWriter.write(rbodyjsonObject.toString());
				outputStreamWriter.flush();
			}

		    myDateObj = LocalDateTime.now();
		    myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		    formattedDate = myDateObj.format(myFormatObj);
			System.out.println(formattedDate+" ### "+requestParams[0]+" Response code: " + conn.getResponseCode());

			if (conn.getResponseCode() == 200 || conn.getResponseCode() == 201) {
				responseXML = getResponseXML(conn);
			}else {
				responseXML = "{\"HttpCode\":\""+conn.getResponseCode()+"\",\"Message\":\""+conn.getResponseMessage()+"\"}";
			}
		}
		catch(Exception ex){
			responseXML = ex.getMessage();
		}finally {
			myDateObj = null;
			myFormatObj = null;
		    formattedDate = null;
			conn.disconnect();
			arrPropertyKey = null;
			arrPropertyValue = null;
			url = null;
			conn = null;
			rbodyjsonObject = null;
			outputStreamWriter = null;
			encoded = null;
		}
		return responseXML;
	}
	
	private static String getResponseXML(HttpsURLConnection httpsURLConnection) throws IOException{

		StringBuffer stringBuffer = null;
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		String readSingleLine = null;

		try{
			// read the response stream & buffer the result into a StringBuffer
			inputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);

			// reading the XML response content line by line
			stringBuffer = new StringBuffer();
			while ((readSingleLine = bufferedReader.readLine()) != null) {
				stringBuffer.append(readSingleLine);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
			// finally close all operations
			bufferedReader.close();
			httpsURLConnection.disconnect();
		}
		return stringBuffer.toString();
	}
    private static class DefaultTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
