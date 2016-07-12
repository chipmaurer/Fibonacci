package com.Fibonacci;

// 
// Serializable class that represents an individual Fibonacci valu
//

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "element")
public class FibonacciValue implements Serializable {

   private static final long serialVersionUID = 1L;
   private int value;

   public FibonacciValue(){}
   
   public FibonacciValue(int value){
      this.value = value;
   }

   public int getValue() {
      return value;
   }

   @XmlElement
   public void setValue(int value) {
      this.value = value;
   }
 
}