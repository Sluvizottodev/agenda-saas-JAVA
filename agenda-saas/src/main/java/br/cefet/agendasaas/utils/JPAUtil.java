package br.cefet.agendasaas.utils;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static volatile EntityManagerFactory emf;

    private JPAUtil() {
    }

    private static EntityManagerFactory buildEMF() {
        try {
            String host = System.getenv().getOrDefault("DB_HOST", "localhost");
            String port = System.getenv().getOrDefault("DB_PORT", "3306");
            String db = System.getenv().getOrDefault("DB_NAME", "agenda_saas");
            String user = System.getenv().getOrDefault("DB_USER", "root");
            String pass = System.getenv().getOrDefault("DB_PASSWORD", "");
            if ((pass == null || pass.isEmpty()) && "root".equals(user)) {
                pass = "rootpass";
            }

            String url = String.format(
                    "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                    host, port, db);

            Map<String, String> props = new HashMap<>();
            props.put("jakarta.persistence.jdbc.url", url);
            props.put("jakarta.persistence.jdbc.user", user);
            props.put("jakarta.persistence.jdbc.password", pass);
            props.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");

            props.putIfAbsent("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            props.putIfAbsent("hibernate.hbm2ddl.auto", "update");
            props.putIfAbsent("hibernate.show_sql", "true");
            props.putIfAbsent("hibernate.format_sql", "true");

            System.out.println("[JPAUtil] Iniciando EMF com URL=" + url + " user=" + user);

            EntityManagerFactory factory = Persistence.createEntityManagerFactory("agendaPU", props);

            try {
                var metamodel = factory.getMetamodel();
                var entities = metamodel.getEntities();
                System.out.println("[JPAUtil] Registered entities in metamodel:");
                entities.stream().map(e -> e.getName()).sorted().forEach(n -> System.out.println(" - " + n));
            } catch (Exception e) {
                System.err.println("[JPAUtil] Erro ao inspecionar o metamodel: " + e.getMessage());
                e.printStackTrace(System.err);
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
