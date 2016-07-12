package com.Fibonacci;

// 
// A web service class to do some basic math functions to return Fibonacci series given user specified depth
//
// Also included are some management functions to set/return current base fibonacci series starting values
//
// Note that all business logic is contained in the FibonacciManagement class
//

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/FibonacciService")
public class FibonacciService {
	FibonacciManagement managementService = new FibonacciManagement();
	
	//
	// Core method - return a fibonacci series of given depth
	// 
	@GET
	@Path("/series/{depth}")
	@Produces(MediaType.APPLICATION_XML)
	public List<FibonacciValue> getFibonacciSeries(@PathParam("depth") int depth){
		System.out.println("Received GET call, depth: " + depth);
		FibonacciReturnCodes returnCode = managementService.isDepthValid(depth);
		if (returnCode == FibonacciReturnCodes.SUCCESS) {
			List<FibonacciValue> results = managementService.getFibonacciResults(depth);
			if (results == null) {
				// Unexpected null return list
				ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
				builder.entity("Unexpected error from management server");
			    Response response = builder.build();
			    throw new WebApplicationException(response);
			}
			return results;
		} else {
			ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		    builder.entity(managementService.getErrorReason(returnCode));
		    Response response = builder.build();
		    throw new WebApplicationException(response);
		}
	}
	
	// 
	// These methods get/set the starting value of a fibonacci series (0 or 1)
	// 
	@GET
	@Path("/base")
	@Produces(MediaType.APPLICATION_XML)
	public FibonacciValue getFibonacciBase(){
		return managementService.getFibonacciBase();
	}
	
	@POST
	@Path("/base")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setFibonacciBase(@FormParam("value") int value) {
		FibonacciValue newBase = new FibonacciValue(value);
		FibonacciReturnCodes returnCode = managementService.setFibonacciBase(newBase);
		if (returnCode == FibonacciReturnCodes.SUCCESS) {
			ResponseBuilder builder = Response.status(Response.Status.OK);
			Response response = builder.build();
			return response;
		} else {
			ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		    builder.entity(managementService.getErrorReason(returnCode));
		    Response response = builder.build();
		    throw new WebApplicationException(response);
		}
	}

}
