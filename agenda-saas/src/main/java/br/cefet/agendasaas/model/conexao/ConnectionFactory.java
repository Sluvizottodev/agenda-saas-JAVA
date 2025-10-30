package br.cefet.agendasaas.model.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static String getenvOrDefault(String name, String defaultValue) {
        String v = System.getenv(name);
        return (v == null || v.isEmpty()) ? defaultValue : v;
    }

    public static Connection getConnection() {
        String host = getenvOrDefault("DB_HOST", "localhost");
        String port = getenvOrDefault("DB_PORT", "3306");
        String db = getenvOrDefault("DB_NAME", "agenda_saas");
        String url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                host,
                port, db);
        String user = getenvOrDefault("DB_USER", "root");
        String password = getenvOrDefault("DB_PASSWORD", "");

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException firstEx) {
            try {
                System.out.println("ConnectionFactory: falha com credenciais iniciais, tentando 'agenda'@'rootpass'");
                return DriverManager.getConnection(url, "agenda", "rootpass");
            } catch (SQLException secondEx) {
                try {
                    System.out.println("ConnectionFactory: falha com 'agenda', tentando 'root'@'rootpass'");
                    return DriverManager.getConnection(url, "root", "rootpass");
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
