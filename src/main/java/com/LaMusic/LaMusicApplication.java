package com.LaMusic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.LaMusic.util.EnvLoader;

@SpringBootApplication
public class LaMusicApplication {

	public static void main(String[] args) {
		//EnvLoader.loadEnv();
		SpringApplication.run(LaMusicApplication.class, args);
	}

}

//email": "maria@email.com",
//"senha": "123456"