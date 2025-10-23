package br.cefet.agendasaas.controller;

import java.io.IOException;

import br.cefet.agendasaas.dao.UsuarioDAO;
import br.cefet.agendasaas.model.entidades.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/auth/login")
public class LoginServlet extends GenericServlet {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        Usuario usuario = null;
        try {
            usuario = usuarioDAO.buscarPorEmailSenha(email, senha);
        } catch (Exception e) {
            java.io.File logFile = new java.io.File("" + System.getProperty("user.dir")
                    + "\\tomcat-server\\logs\\app-error.log");
            try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(logFile, true))) {
                pw.println("--- Exception in LoginServlet at " + java.time.LocalDateTime.now() + " ---");
                e.printStackTrace(pw);
                pw.println();
            } catch (java.io.IOException ioe) {
                // swallow
            }
            request.setAttribute("erro", "Erro interno, tente novamente mais tarde.");
            try {
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } catch (ServletException | IOException se) {
                throw new ServletException(se);
            }
            return;
        }

        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", usuario);

            switch (usuario.getTipo()) {
                case CLIENTE:
                    response.sendRedirect(request.getContextPath() + "/cliente/dashboard.jsp");
                    break;
                case PRESTADOR:
                    response.sendRedirect(request.getContextPath() + "/prestador/dashboard.jsp");
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                    break;
            }
        } else {
            request.setAttribute("erro", "E-mail ou senha inválidos!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
        
    }
}

