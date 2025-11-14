package br.cefet.agendaSaas.integration;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import br.cefet.agendasaas.model.entidades.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

class JpaMySqlContainerIT {

    @Test
    void testPersistClienteWithLocalDatabase() throws Exception {
        Map<String, Object> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.url", "jdbc:mysql://localhost:3306/agenda_test");
        props.put("jakarta.persistence.jdbc.user", "test");
        props.put("jakarta.persistence.jdbc.password", "test");
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
            if (em.isOpen())
                em.close();
            if (emf.isOpen())
                emf.close();
        }
    }
}
