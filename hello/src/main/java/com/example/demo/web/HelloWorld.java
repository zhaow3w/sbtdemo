package com.example.demo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
	
	 @GetMapping("/hello")
	    public String hello(){
	        return "springboot hello world...";
	        //1111
	    }
}
