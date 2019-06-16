package com.example.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		new Thread(new Runnable() {
			@Override
			public void run() {
				new ServerGui().main(args);
			}
		}).start();
	}

}
