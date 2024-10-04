package com.example.accessingdatajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccessingDataJpaApplication {

	private static final Logger log = LoggerFactory.getLogger(AccessingDataJpaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AccessingDataJpaApplication.class);
	}
	@Bean
	public CommandLineRunner demo(CustomerRepository repository) {
		return (args) -> {
			// save a few customers
			repository.save(new Customer("Jack", "Bauer"));
			repository.save(new Customer("Chloe", "O'Brian"));
			repository.save(new Customer("Kim", "Bauer"));
			repository.save(new Customer("David", "Palmer"));
			repository.save(new Customer("Michelle", "Dessler"));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			repository.findAll().forEach(customer -> {
				log.info(customer.toString());
			});
			log.info("");

			// fetch an individual customer by ID
			Customer customer = repository.findById(1L);
			log.info("Customer found with findById(1L):");
			log.info("--------------------------------");
			log.info(customer.toString());
			log.info("");

			// fetch customers by last name
			log.info("Customer found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			repository.findByLastName("Bauer").forEach(bauer -> {
				log.info(bauer.toString());
			});
			log.info("");
		};
	}
	@Bean
	public CommandLineRunner demoCache(CustomerService customerService) {
		return (args) -> {
			// Guardar algunos clientes
			customerService.save(new Customer("Jack", "Bauer"));
			customerService.save(new Customer("Chloe", "O'Brian"));
			customerService.save(new Customer("Kim", "Bauer"));
			customerService.save(new Customer("David", "Palmer"));
			customerService.save(new Customer("Michelle", "Dessler"));

			// Fetch individual customer by ID (primero de la base de datos, luego del caché)
			log.info("Primera búsqueda de Customer con ID 1 - debería ir a la base de datos:");
			customerService.findById(1L);

			log.info("Segunda búsqueda de Customer con ID 1 - debería ir al caché:");
			customerService.findById(1L);

			// Fetch customers by last name (sin caché para mantenerlo simple)
			log.info("Búsqueda de Customer por apellido 'Bauer':");
			customerService.findByLastName("Bauer").forEach(bauer -> {
				log.info(bauer.toString());
			});
		};
	}
}

