package com.shap.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.json.JSONObject;

public class ApiClient {
	public String restClientService(String[] requestParams) {

		// local variables
		LocalDateTime myDateObj = null;
		DateTimeFormatter myFormatObj = null;
	    String formattedDate = null;
		String arrPropertyKey[] = null;
		String arrPropertyValue[] = null;
		URL url = null;
		HttpURLConnection httpURLConnection = null;
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

			url = new URL(requestParams[0]);
			httpURLConnection = (HttpURLConnection) url.openConnection();   
			httpURLConnection.setRequestMethod(requestParams[1]);
			
			for (int i = 0; i < arrPropertyKey.length; i++) {
				if(arrPropertyKey[i].equalsIgnoreCase("Content-Type")) {
					httpURLConnection.setRequestProperty(arrPropertyKey[i], arrPropertyValue[i]);				
				}else if(arrPropertyKey[i].equalsIgnoreCase("Accept")) {
					httpURLConnection.setRequestProperty(arrPropertyKey[i], arrPropertyValue[i]);
				}else if(arrPropertyKey[i].equalsIgnoreCase("Api-Key")) {
					if(arrPropertyKey[i+1].equalsIgnoreCase("Token")) {
						httpURLConnection.setRequestProperty(arrPropertyValue[i], arrPropertyValue[i+1]);
					}
				}else if(arrPropertyKey[i].equalsIgnoreCase("Basic")) {
					encoded = Base64.getEncoder().encodeToString((requestParams[3]+":"+requestParams[4]).getBytes(StandardCharsets.UTF_8));
					httpURLConnection.setRequestProperty("Authorization", arrPropertyValue[i]+" "+encoded);
				}else if(arrPropertyKey[i].equalsIgnoreCase("Bearer")) {
					httpURLConnection.setRequestProperty("Authorization", arrPropertyKey[i]+" "+arrPropertyValue[i]);
				}else {
					httpURLConnection.setRequestProperty(arrPropertyKey[i], arrPropertyValue[i]);
				}
			}
			httpURLConnection.setDoOutput(true);

			if(requestParams[2] != null) {
				rbodyjsonObject = new JSONObject(requestParams[2]);
				outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
				outputStreamWriter.write(rbodyjsonObject.toString());
				outputStreamWriter.flush();
			}

		    myDateObj = LocalDateTime.now();
		    myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		    formattedDate = myDateObj.format(myFormatObj);
			System.out.println(formattedDate+" ### "+requestParams[0]+" Response code: " + httpURLConnection.getResponseCode());

			if (httpURLConnection.getResponseCode() == 200 || httpURLConnection.getResponseCode() == 201) {
				responseXML = getResponseXML(httpURLConnection);
			}else {
				responseXML = "{\"HttpCode\":\""+httpURLConnection.getResponseCode()+"\",\"Message\":\""+httpURLConnection.getResponseMessage()+"\"}";
			}
		}
		catch(Exception ex){
			responseXML = ex.getMessage();
		}finally {
			myDateObj = null;
			myFormatObj = null;
		    formattedDate = null;
			httpURLConnection.disconnect();
			arrPropertyKey = null;
			arrPropertyValue = null;
			url = null;
			httpURLConnection = null;
			rbodyjsonObject = null;
			outputStreamWriter = null;
			encoded = null;
		}
		return responseXML;
	}
	
	private static String getResponseXML(HttpURLConnection httpURLConnection) throws IOException{

		StringBuffer stringBuffer = null;
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		String readSingleLine = null;

		try{
			// read the response stream & buffer the result into a StringBuffer
			inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
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
			httpURLConnection.disconnect();
		}
		return stringBuffer.toString();
	}
}
