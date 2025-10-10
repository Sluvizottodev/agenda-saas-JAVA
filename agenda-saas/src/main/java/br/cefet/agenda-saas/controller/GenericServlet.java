package br.cefet.agendaSaas.controller;

import java.io.IOException;

import br.cefet.agendaSaas.model.entidades.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// servlet base com utilitários

public abstract class GenericServlet extends HttpServlet {

    protected Usuario getUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (Usuario) session.getAttribute("usuarioLogado");
    }

    protected boolean requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario u = getUsuarioLogado(request);
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return false;
        }
        return true;
    }

    protected void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher(path);
        rd.forward(request, response);
    }

    protected void redirect(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    protected void jsonResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }

    protected void handleException(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        request.setAttribute("erro", "Erro ao processar solicitação: " + e.getMessage());
        forward(request, response, "/utils/erro.jsp");
    }

}
