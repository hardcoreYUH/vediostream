package com.yuhang.vediostream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.yuhang")
public class VediostreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(VediostreamApplication.class, args);
	}

}
