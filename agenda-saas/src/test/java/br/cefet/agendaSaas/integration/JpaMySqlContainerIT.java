package br.cefet.agendaSaas.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;

import br.cefet.agendasaas.model.entidades.Cliente;

class JpaMySqlContainerIT {

    @Test
    void testPersistClienteWithMySqlContainer() throws Exception {
        try (MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.33").withDatabaseName("agenda_test").withUsername("test").withPassword("test")) {
            mysql.start();

            Map<String, Object> props = new HashMap<>();
            props.put("jakarta.persistence.jdbc.url", mysql.getJdbcUrl());
            props.put("jakarta.persistence.jdbc.user", mysql.getUsername());
            props.put("jakarta.persistence.jdbc.password", mysql.getPassword());
            props.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
            props.put("hibernate.hbm2ddl.auto", "create-drop");
            props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            props.put("hibernate.show_sql", "true");

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("agendaPU", props);
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                Cliente c = new Cliente("Teste", "teste@example.com", "senha", "12345678900");
                em.persist(c);
                em.getTransaction().commit();

                assertNotNull(c.getId(), "ID should be generated after persist");

                Cliente found = em.find(Cliente.class, c.getId());
                assertEquals("teste@example.com", found.getEmail());

            } finally {
                if (em.isOpen()) em.close();
                if (emf.isOpen()) emf.close();
                mysql.stop();
            }
        }
    }
}
