package com.ajay.confidace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import practice.RestTemplateClient;
import practice.classwebclient;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;

import java.lang.reflect.Array;
import java.time.format.ResolverStyle;
import java.util.*;

@SpringBootApplication
public class ConfidaceApplication {

	public static void main(String[] args) {



		SpringApplication.run(ConfidaceApplication.class, args);

		//RestTemplateClient obj = new RestTemplateClient();

		ObjectMapper objmap = new ObjectMapper();


		RestTemplate obj = new RestTemplate();

		String response = obj.getForObject("https://jsonplaceholder.typicode.com/users/1",String.class);

		JsonNode nodeobj =objmap.readTree(response);

		nodeobj.get("phone").asString();

		System.out.println(nodeobj.get("phone").asString());

		nodeobj.get("address").get("street").asString();

		System.out.println(nodeobj.get("address").get("geo").get("lat").asString());


		System.out.println("checking the git changes ");

       classwebclient cienttewed  = new classwebclient();
		cienttewed.webclientapicalling();




	}


	}




