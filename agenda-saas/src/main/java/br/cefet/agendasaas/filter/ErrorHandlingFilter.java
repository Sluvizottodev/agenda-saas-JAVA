package br.cefet.agendasaas.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ErrorHandlingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        try {
            chain.doFilter(request, response);
        } catch (Exception t) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            String stack = sw.toString();
            System.err.println(
                    "[ErrorHandlingFilter] Exceção capturada em '" + req.getRequestURI() + "': " + t.getMessage());
            System.err.println(stack);
            try {
                String logsDir = req.getServletContext().getRealPath("/WEB-INF/logs");
                if (logsDir == null) {
                    String catalina = System.getProperty("catalina.base");
                    if (catalina != null) {
                        logsDir = catalina + java.io.File.separator + "logs";
                    } else {
                        logsDir = System.getProperty("java.io.tmpdir");
                    }
                }
                java.io.File out = new java.io.File(logsDir, "agenda-error.log");
                out.getParentFile().mkdirs();
                try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(out, true))) {
                    pw.println("--- ErrorHandlingFilter captured exception at " + java.time.LocalDateTime.now()
                            + " for " + req.getRequestURI() + " ---");
                    pw.println(stack);
                    pw.println();
                }
            } catch (java.io.IOException ioe) {
                System.err.println("ErrorHandlingFilter: falha ao escrever log secundário: " + ioe.getMessage());
                ioe.printStackTrace(System.err);
            }

            boolean ajax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
            String accept = req.getHeader("Accept");
            if (!ajax && accept != null && accept.contains("application/json")) {
                ajax = true;
            }

            if (ajax) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.setContentType("application/json;charset=UTF-8");
                String safeMessage = t.getMessage() == null ? "Erro interno no servidor" : t.getMessage();
                String json = "{\"error\": \"" + escapeJson(safeMessage) + "\"}";
                resp.getWriter().write(json);
            } else {

                request.setAttribute("erro", "Ocorreu um erro interno: " + Objects.toString(t.getMessage(), ""));
                request.setAttribute("detalhes", stack);
                try {
                    req.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
                } catch (jakarta.servlet.ServletException | java.io.IOException e) {
                    resp.setContentType("text/plain;charset=UTF-8");
                    resp.getWriter().write("Ocorreu um erro interno e a página de erro não pôde ser renderizada.");
                }
            }
        }
    }

    @Override
    public void destroy() {

    }

    private String escapeJson(String s) {
        if (s == null)
            return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
