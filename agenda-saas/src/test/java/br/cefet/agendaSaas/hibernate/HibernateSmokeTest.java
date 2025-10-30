package br.cefet.agendaSaas.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

import br.cefet.agendasaas.utils.HibernateUtil;

public class HibernateSmokeTest {

    @Test
    public void openSessionAndRunCount() {
        assertDoesNotThrow(() -> {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Query<Long> q = session.createQuery("select count(c) from Cliente c", Long.class);
                Long count = q.uniqueResultOptional().orElse(0L);
                System.out.println("Cliente count: " + count);
            }
        });
    }
}
