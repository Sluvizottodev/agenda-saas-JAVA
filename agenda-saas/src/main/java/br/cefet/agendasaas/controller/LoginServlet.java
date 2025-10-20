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

        Usuario usuario = usuarioDAO.buscarPorEmailSenha(email, senha);

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
            request.setAttribute("erro", "E-mail ou senha invÃ¡lidos!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}

