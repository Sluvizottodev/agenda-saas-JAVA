package br.cefet.agendasaas.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static volatile EntityManagerFactory emf;

    private JPAUtil() {
    }

    private static EntityManagerFactory buildEMF() {
        try {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("agendaPU");
            try {
                var metamodel = factory.getMetamodel();
                var entities = metamodel.getEntities();
                System.out.println("[JPAUtil] Registered entities in metamodel:");
                entities.stream().map(e -> e.getName()).sorted().forEach(n -> System.out.println(" - " + n));
            } catch (Throwable t) {
                System.err.println("[JPAUtil] Erro ao inspecionar o metamodel: " + t.getMessage());
                t.printStackTrace(System.err);
            }
            return factory;
        } catch (RuntimeException ex) {
            System.err.println("Falha ao criar EntityManagerFactory: " + ex.getMessage());
            ex.printStackTrace(System.err);
            throw ex;
        }
    }

    private static EntityManagerFactory getEmf() {
        if (emf == null) {
            synchronized (JPAUtil.class) {
                if (emf == null) {
                    emf = buildEMF();
                }
            }
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getEmf().createEntityManager();
    }

    public static void shutdown() {
        if (emf != null && emf.isOpen())
            emf.close();
    }
}
