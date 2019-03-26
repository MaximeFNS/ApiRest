package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class Application
{
    public static void main( String[] args )
    {
    	try {
    		SpringApplication.run(Application.class, args);
    		System.out.println("Appli démarée !");
    	} catch (Exception e){
    		System.out.println("Appli erreur !\n" + e);
    	}
    }
}
