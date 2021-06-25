package com.shap.adapter;

import org.json.JSONObject;
import org.json.JSONTokener;

public class TestApiClient {

	public static void main(String[] args) {
		String requestURL = null;
		String requestMethod = null;
		String requestBody = null;
		String userAccount = null;
		String userPassword = null;
		String propertyKey = null;
		String propertyValue = null;
		String[] requestParams = null;
		ApiClientSSL apiClientSSL = null;
		JSONTokener jsonTokener = null;
		JSONObject jsonObject = null;
		String json_value = null;
		String text = null;
		try {
			requestURL = "https://nlehub.kemenkeu.go.id/V1/NLE/OfferDetailBooking?topic=nle-offer&key=NPWP";
			requestMethod = "POST";
			requestBody = "{\"idRequestBooking\":\"123\",\"idServiceOrder\":\"1\",\"hargaPenawaran\":500000000,\"waktuPenawaran\":\"2019-10-07 14:00:00\",\"timestamp\":\"2019-09-08 14:00:00\"}";
			userAccount = null;
			userPassword = null;
			propertyKey = "Content-Type,Api-Key,Token";
			propertyValue = "application/json,beacukai-api-key,971aaf04-64af-4c71-be7e-0740542890ca";

			requestParams = new String[]{requestURL, requestMethod, requestBody, userAccount, userPassword, propertyKey, propertyValue};

			apiClientSSL = new ApiClientSSL();
			text = apiClientSSL.restClientService(requestParams);
			try {
    			jsonTokener = new JSONTokener(text);
    			jsonObject = new JSONObject(jsonTokener);
    			json_value = jsonObject.toString();
        	}catch (Exception e) {json_value = null;}
			if(json_value == null) {
				try {
					text = "{\"note\":\""+apiClientSSL.restClientService(requestParams)+"\"}";
	    			jsonTokener = new JSONTokener(text);
	    			jsonObject = new JSONObject(jsonTokener);
	    			json_value = jsonObject.toString();
	        	}catch (Exception e) {json_value = null;}
			}
			//execution.setVariable("response", json_value);
			System.out.println("Response: " +json_value);
		}catch (Exception e) {}
		finally {
			requestURL = null;
			requestMethod = null;
			requestBody = null;
			userAccount = null;
			userPassword = null;
			propertyKey = null;
			propertyValue = null;
			requestParams = null;
			apiClientSSL = null;
			jsonTokener = null;
			jsonObject = null;
			json_value = null;
			text = null;
		}
	}

}
