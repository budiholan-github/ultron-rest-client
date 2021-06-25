package com.shap.adapter;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ApiClientSSLGeneralDelegate implements JavaDelegate {
	public void execute(DelegateExecution execution) {
		String requestURL = null;
		String requestMethod = null;
		String requestBody = null;
		String userAccount = null;
		String userPassword = null;
		String propertyKey = null;
		String propertyValue = null;
		String[] requestParams = null;
		ApiClientSSLGeneral apiClientSSL = null;
		JSONTokener jsonTokener = null;
		JSONObject jsonObject = null;
		String json_value = null;
		String text = null;
		try {
			try{requestURL = execution.getVariableInstance("RequestURL").getValue().toString();}catch (Exception e) {requestURL = null;}
			try{requestMethod = execution.getVariableInstance("RequestMethod").getValue().toString();}catch (Exception e) {requestMethod = null;}
			try{requestBody = execution.getVariableInstance("RequestBody").getValue().toString();}catch (Exception e) {requestBody = null;}
			try{userAccount = execution.getVariableInstance("UserAccount").getValue().toString();}catch (Exception e) {userAccount = null;}
			try{userPassword = execution.getVariableInstance("UserPassword").getValue().toString();}catch (Exception e) {userPassword = null;}
			try{propertyKey = execution.getVariableInstance("PropertyKey").getValue().toString();}catch (Exception e) {propertyKey = null;}
			try{propertyValue = execution.getVariableInstance("PropertyValue").getValue().toString();}catch (Exception e) {propertyValue = null;}
			
			requestParams = new String[]{requestURL, requestMethod, requestBody, userAccount, userPassword, propertyKey, propertyValue};
			
			apiClientSSL = new ApiClientSSLGeneral();
			text = apiClientSSL.restClientService(requestParams);
			try {
    			jsonTokener = new JSONTokener(text);
    			jsonObject = new JSONObject(jsonTokener);
    			json_value = jsonObject.toString();
        	}catch (Exception e) {json_value = null;}
			if(json_value == null) {
				try {
					text = "{\"note\":\""+text+"\"}";
	    			jsonTokener = new JSONTokener(text);
	    			jsonObject = new JSONObject(jsonTokener);
	    			json_value = jsonObject.toString();
	        	}catch (Exception e) {json_value = null;}
			}
			execution.setVariable("response", json_value);
		}catch (Exception e) {e.printStackTrace();}
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
