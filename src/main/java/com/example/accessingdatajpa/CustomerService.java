package com.example.accessingdatajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final Map<Long, Customer> customerCache = new HashMap<>();  // Simulación de caché local

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Metodo para obtener todos los clientes (sin caché para este caso)
     */
    public List<Customer> findAll() {
        return (List<Customer>) customerRepository.findAll();
    }

    /**
     * Metodo para obtener un cliente por ID, primero busca en caché.
     */
    public Customer findById(Long id) {
        // Primero busca en el caché
        if (customerCache.containsKey(id)) {
            log.info("Customer encontrado en caché con id: " + id);
            return customerCache.get(id);
        }

        // Si no está en caché, consulta la base de datos y lo guarda en caché
        log.info("Customer no encontrado en caché, buscando en base de datos...");
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            log.info("Customer encontrado en base de datos con id: " + id);
            customerCache.put(id, customer.get());  // Almacena en caché
            return customer.get();
        } else {
            log.warn("Customer no encontrado con id: " + id);
            return null;
        }
    }

    /**
     * Metodo para obtener clientes por apellido (sin caché para mantenerlo simple).
     */
    public List<Customer> findByLastName(String lastName) {
        return customerRepository.findByLastName(lastName);
    }

    /**
     * Metodo para guardar un cliente, actualiza el caché.
     */
    public void save(Customer customer) {
        customerRepository.save(customer);
        customerCache.put(customer.getId(), customer);  // Actualiza el caché
        log.info("Customer guardado y almacenado en caché con id: " + customer.getId());
    }

    /**
     * Metodo para limpiar el caché si es necesario.
     */
    public void clearCache() {
        customerCache.clear();
        log.info("Caché limpiado.");
    }
}
