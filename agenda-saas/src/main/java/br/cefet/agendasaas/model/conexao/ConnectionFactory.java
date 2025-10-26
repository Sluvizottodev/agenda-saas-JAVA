package br.cefet.agendasaas.model.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = createUrl();
    private static final String USER = getenvOrDefault("DB_USER", "root");
    private static final String PASSWORD = getenvOrDefault("DB_PASSWORD", "");

    private static String createUrl() {
        String host = getenvOrDefault("DB_HOST", "localhost");
        String port = getenvOrDefault("DB_PORT", "3306");
        String db = getenvOrDefault("DB_NAME", "agenda_saas");
        return String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", host,
                port, db);
    }

    private static String getenvOrDefault(String name, String defaultValue) {
        String v = System.getenv(name);
        return (v == null || v.isEmpty()) ? defaultValue : v;
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException firstEx) {
            try {
                System.out.println("ConnectionFactory: falha com credenciais iniciais, tentando 'agenda'@'rootpass'");
                return DriverManager.getConnection(URL, "agenda", "rootpass");
            } catch (SQLException secondEx) {
                try {
                    System.out.println("ConnectionFactory: falha com 'agenda', tentando 'root'@'rootpass'");
                    return DriverManager.getConnection(URL, "root", "rootpass");
                } catch (SQLException thirdEx) {
                    RuntimeException re = new RuntimeException("Erro ao conectar ao banco", firstEx);
                    re.addSuppressed(secondEx);
                    re.addSuppressed(thirdEx);
                    throw re;
                }
            }
        }
    }
}
