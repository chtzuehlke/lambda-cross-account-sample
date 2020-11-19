package com.serverless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Handler implements RequestHandler<Handler.Request, Handler.Response> {
	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private static final STSAssumeRoleSessionCredentialsProvider assumeRoleCredentialsProvider = new STSAssumeRoleSessionCredentialsProvider.Builder(System.getenv("INVOKE_ROLE"), "JustASession").build();
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Response handleRequest(Request input, Context context) {
		try {
			return handleRequestInternal(input, context);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Response handleRequestInternal(Request input, Context context) throws Exception {
		LOG.info("received: {}", input);
		
		Response response;
		if (input.isRecursive()) {
			input.setRecursive(false);
			
			AWSLambda lambdaClient = AWSLambdaClientBuilder.standard()
					.withCredentials(assumeRoleCredentialsProvider)
					.build();;
			
			InvokeResult result = lambdaClient.invoke(new InvokeRequest()
					.withFunctionName(System.getenv("FUNCTION_NAME"))
					.withPayload(objectMapper.writeValueAsString(input)));
			
			response = objectMapper.readValue(result.getPayload().array(), Response.class);
			response.setMessage(response.getMessage() + " from " + context.getInvokedFunctionArn());
		}
		else {
			 response = new Response();
			 response.setMessage(input.getMessage() + " from " + context.getInvokedFunctionArn());
		}
			
		return response;
	}
	
	public static class Request {
		private String message;
		private boolean recursive = false;
		
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public boolean isRecursive() {
			return recursive;
		}
		public void setRecursive(boolean recursive) {
			this.recursive = recursive;
		}
	}
	
	public static class Response {
		private String message;
		
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}
}
