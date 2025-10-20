package br.cefet.agendasaas.config;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Sets timezone and locale during container bootstrap so dates/strings respect pt-BR/UTC-3.
 */
@WebListener
public class ApplicationStartupListener implements ServletContextListener {

    private static final ZoneId SAO_PAULO = ZoneId.of("America/Sao_Paulo");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        TimeZone.setDefault(TimeZone.getTimeZone(SAO_PAULO));
    Locale.setDefault(Locale.forLanguageTag("pt-BR"));
        sce.getServletContext().setRequestCharacterEncoding(StandardCharsets.UTF_8.name());
        sce.getServletContext().setResponseCharacterEncoding(StandardCharsets.UTF_8.name());
    }
}
