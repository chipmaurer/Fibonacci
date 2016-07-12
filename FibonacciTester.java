package com.Fibonacci.test;

// 
// Run through some basic test cases
// From IDE, do RunAs->JavaApplication
// Eventually, these would be upgraded to junit tests
//

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.Fibonacci.FibonacciValue;

public class FibonacciTester  {

	private Client client;
	private String SERIES_REST_SERVICE_URL = "http://localhost:8080/Fibonacci/rest/FibonacciService/series";
	private String BASE_REST_SERVICE_URL = "http://localhost:8080/Fibonacci/rest/FibonacciService/base";
	private static final String PASS = "pass";
	private static final String FAIL = "fail";

	private void init(){
		this.client = ClientBuilder.newClient();
	}

	public static void main(String[] args){
	   
		FibonacciTester tester = new FibonacciTester();
		//initialize the tester
		tester.init();
	
		//test get all fibonacci series Web Service Methods (good and bad)
		tester.testGetFibonacciSeries();
		
		//test set/set fibonacci base value Web Service Method (good and bad)
		tester.testSetGetFibonacciBase();
	}
 
	//Test: Get valid and invalid fibonacci series depths
	private void testGetFibonacciSeries(){

	GenericType<List<FibonacciValue>> list = new GenericType<List<FibonacciValue>>() {};
	List<FibonacciValue> series = client
	     .target(SERIES_REST_SERVICE_URL)
	     .path("/{value}")
	     .resolveTemplate("value", 3)
	     .request(MediaType.APPLICATION_XML)
	     .get(list);
	  String result = FAIL;
	  if (series.size() == 3) {
		  result = PASS;
	  }
	  System.out.println("Test case name: testGetFibonacciSeries (positive test1), Result: " + result );
	  
	  // Expect webexception for invalid input
	  result = FAIL;
	  try {
		  series = client
		     .target(SERIES_REST_SERVICE_URL)
		     .path("/{value}")
		     .resolveTemplate("value", -1)
		     .request(MediaType.APPLICATION_XML)
		     .get(list);
	  } catch (WebApplicationException ex) {
		  result = PASS;
	  }
	  System.out.println("Test case name: testGetFibonacciSeries (negative test1), Result: " + result );
	  
	  // Expect webexception for invalid input
	  result = FAIL;
	  try {
		  series = client
		     .target(SERIES_REST_SERVICE_URL)
		     .path("/{value}")
		     .resolveTemplate("value", 1025)
		     .request(MediaType.APPLICATION_XML)
		     .get(list);
	  } catch (WebApplicationException ex) {
		  result = PASS;
	  }
	  System.out.println("Test case name: testGetFibonacciSeries (negative test2), Result: " + result );
	}
   
	private void testSetGetFibonacciBase(){
		
		FibonacciValue base = client
				.target(BASE_REST_SERVICE_URL)
		        .request(MediaType.APPLICATION_XML)
				.get(FibonacciValue.class);
		String result = FAIL;
		if (base.getValue() == 0 || base.getValue() == 1) {
			result = PASS;
		}
		System.out.println("Test case name: testSetGetFibonacciBase (positive test1), Result: " + result );
		
		// Now set the base and verify it was correct
		Form form = new Form();
		form.param("value", "0");
	
		Response callResult = client
	         .target(BASE_REST_SERVICE_URL)
	         .request(MediaType.APPLICATION_XML)
	         .post(Entity.entity(form,
	            MediaType.APPLICATION_FORM_URLENCODED_TYPE),
	            Response.class);
		// Now, we expect the value returned to be 0
		base = client
				.target(BASE_REST_SERVICE_URL)
		        .request(MediaType.APPLICATION_XML)
				.get(FibonacciValue.class);
		result = FAIL;
		if (base.getValue() == 0) {
			result = PASS;
		}
		System.out.println("Test case name: testSetGetFibonacciBase (positive test2), Result: " + result );
		
		// Now set the base and verify it was correct
		Form newform = new Form();
		newform.param("value", "5");
	
		callResult = client
	         .target(BASE_REST_SERVICE_URL)
	         .request(MediaType.APPLICATION_XML)
	         .post(Entity.entity(newform,
	            MediaType.APPLICATION_FORM_URLENCODED_TYPE),
	            Response.class);
		result = FAIL;
		if (callResult.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
			result = PASS;
		}
		System.out.println("Test case name: testSetGetFibonacciBase (negative test1), Result: " + result );
	}
}