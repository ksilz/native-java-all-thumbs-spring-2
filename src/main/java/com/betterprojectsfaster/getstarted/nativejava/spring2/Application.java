package com.betterprojectsfaster.getstarted.nativejava.spring2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public void run(String... args) throws Exception {
		System.out.println();
		System.out.println("****************************************");
		System.out.println("*   ALL THUMBS SPRING BOOT 2.x 1.1.1   *");
		System.out.println("****************************************");
		System.out.println();

		var creator = new PdfCreator();

		creator.createPdfs();
	}

}
