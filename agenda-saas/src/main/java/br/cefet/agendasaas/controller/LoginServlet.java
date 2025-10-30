package br.cefet.agendasaas.controller;

import java.io.IOException;

import br.cefet.agendasaas.dao.UsuarioDAO;
import br.cefet.agendasaas.model.entidades.Usuario;
import br.cefet.agendasaas.utils.InputValidator;
import br.cefet.agendasaas.utils.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/auth/login")
public class LoginServlet extends GenericServlet {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String email = InputValidator.requireNonBlank(request.getParameter("email"), "E-mail");
            String senha = InputValidator.requireNonBlank(request.getParameter("senha"), "Senha");
            InputValidator.validateEmail(email);

            Usuario usuario = null;
            try {
                usuario = usuarioDAO.buscarPorEmailSenha(email, senha);
            } catch (Exception e) {
                System.err.println("LoginServlet: erro ao autenticar usuário: " + e.getMessage());
                e.printStackTrace(System.err);
                request.setAttribute("erro", "Erro interno ao processar login. Tente novamente mais tarde.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            if (usuario != null) {
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogado", usuario);

                String next = request.getParameter("next");
                String redirectTo = null;
                if (next != null && !next.isBlank()) {
                    try {
                        String decoded = java.net.URLDecoder.decode(next, java.nio.charset.StandardCharsets.UTF_8);
                        java.net.URI uri = new java.net.URI(decoded);
                        if (uri.getScheme() == null && uri.getHost() == null) {
                            String path = uri.getPath();
                            if (path != null) {
                                String ctx = request.getContextPath();
                                if (path.startsWith(ctx))
                                    redirectTo = decoded;
                                else if (path.startsWith("/"))
                                    redirectTo = ctx + decoded;
                            }
                        }
                    } catch (java.net.URISyntaxException | IllegalArgumentException ex) {
                        System.err
                                .println("LoginServlet: URI de redirecionamento inválida ou com formato não esperado: "
                                        + ex.getMessage());
                        ex.printStackTrace(System.err);
                    }
                }

                if (redirectTo != null) {
                    response.sendRedirect(redirectTo);
                    return;
                }
                switch (usuario.getTipo()) {
                    case CLIENTE:
                        response.sendRedirect(request.getContextPath() + "/dashboard/cliente");
                        break;
                    case PRESTADOR:
                        response.sendRedirect(request.getContextPath() + "/dashboard/prestador");
                        break;
                    default:
                        response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
                        break;
                }
            } else {
                request.setAttribute("erro", "E-mail ou senha inválidos!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (ValidationException ve) {
            request.setAttribute("erro", ve.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception t) {
            String logsDir = request.getServletContext().getRealPath("/WEB-INF/logs");
            if (logsDir == null) {
                String catalina = System.getProperty("catalina.base");
                if (catalina != null) {
                    logsDir = catalina + java.io.File.separator + "logs";
                } else {
                    logsDir = System.getProperty("java.io.tmpdir");
                }
            }
            java.io.File logFile = new java.io.File(logsDir, "app-error.log");
            try {
                logFile.getParentFile().mkdirs();
            } catch (Exception ex) {
                System.err.println("LoginServlet: falha ao criar pasta de logs (fallback): " + ex.getMessage());
                ex.printStackTrace(System.err);
            }
            try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(logFile, true))) {
                pw.println("--- Unhandled exception in LoginServlet at " + java.time.LocalDateTime.now() + " ---");
                t.printStackTrace(pw);
                pw.println();
            } catch (java.io.IOException ioe) {
                System.err.println("LoginServlet: falha ao escrever log de exceção não tratada: " + ioe.getMessage());
                ioe.printStackTrace(System.err);
            }
            throw new ServletException(t);
        }

    }
}
