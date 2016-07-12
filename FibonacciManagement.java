package com.Fibonacci;

import java.util.ArrayList;
import java.util.List;

//
// From wikipedia:
// 
// In mathematics, the Fibonacci numbers are the numbers in the following integer sequence, 
// called the Fibonacci sequence:
//
// 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, ,,,
//
// Often, especially in modern usage, the sequence is extended by one more initial term:
//
// 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, ,,,
//
// By definition, the first two numbers in the Fibonacci sequence are either 1 and 1, or 0 and 1, 
// depending on the chosen starting point of the sequence, and each subsequent number is the sum of the previous two.
//
// In mathematical terms, the sequence Fn of Fibonacci numbers is defined by the recurrence relation
//
// F[n]=F[n-1] + F[n-2]
//
// with seed values
//
// F[0]=1, F[1]=1
//
// or
//
// F[0]=0, F[1]=1
//
public class FibonacciManagement {
	
	// Start of fibonacci series, either 0 or 1 (can be changed, so make it static)
	private static FibonacciValue _fibBase = new FibonacciValue(1);
	
	// Maximum depth (arbitrary setting)
	private int _maximumDepth = 1024;
	
	// Get the Fibonacci series results to the desired depth
	public List<FibonacciValue> getFibonacciResults(int depth){
		List<FibonacciValue> results = new ArrayList<FibonacciValue>();
		// Caller should have checked, but let's verify that depth is valid
		if (isDepthValid(depth) != FibonacciReturnCodes.SUCCESS) {
			return null;
		}
		
		// Seed the list and check for special depths (0, 1 or 2)
		// By definition, the first two numbers in the Fibonacci sequence are either 1 and 1, or 0 and 1
		if (depth == 0) {
			return results;
		}
		// Seed 1st value
		results.add(new FibonacciValue(_fibBase.getValue()));
		if (depth == 1) {
			return results;
		}
		
		// Seed 2nd value
		results.add(_fibBase.getValue() == 0 ? 
				new FibonacciValue (_fibBase.getValue() + 1) : 
				new FibonacciValue (_fibBase.getValue()));
		if (depth == 2) {
			return results;
		}

		// OK - let's fill out the results
		// Could have been done recursively, but calculating the sequence is pretty straightforward
		for(int currentDepth = 2; currentDepth < depth; currentDepth++) {
			results.add(new FibonacciValue(results.get(currentDepth - 1).getValue() + 
					results.get(currentDepth - 2).getValue()));
		}
	
		return results;
	}

	//
	// Set the starting point (0 or 1) - check for bad input value
	//
	public FibonacciReturnCodes setFibonacciBase(FibonacciValue base) {
		if (base.getValue() != 0 && base.getValue() != 1) {
			return FibonacciReturnCodes.INVALID_SEED;
		}
		_fibBase = base;
		return FibonacciReturnCodes.SUCCESS;
	}
	
	//
	// Get current base value
	//
	public FibonacciValue getFibonacciBase() {
		return _fibBase;
	}
	
	//
	// Check that the requested depth is valid
	// 
	public FibonacciReturnCodes isDepthValid(int depth) {
		if (depth < 0) {
			return FibonacciReturnCodes.INVALID_DEPTH_NEGATIVE_VALUE;
		} else if (depth > _maximumDepth) {
			return FibonacciReturnCodes.INVALID_DEPTH_MAXIMUM_VALUE;
		}
		return FibonacciReturnCodes.SUCCESS;
	}
	
	//
	// Simple method to translate reason code to a text string
	//
	public String getErrorReason(FibonacciReturnCodes error) {
		switch (error) {
		case INVALID_SEED:
			return "The starting seed requested is invalid.  Seed must be 0 or 1";
		case INVALID_DEPTH_NEGATIVE_VALUE:
			return "Invalid depth value.  Cannot be negative";
		case INVALID_DEPTH_MAXIMUM_VALUE:
			return "Invalid depth value.  Requested depth exceeds maximum value of " + _maximumDepth;
		default:
			return "Unknown error";
		}
	}
}
