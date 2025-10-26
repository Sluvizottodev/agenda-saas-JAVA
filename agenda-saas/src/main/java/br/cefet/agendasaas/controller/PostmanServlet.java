/*package br.cefet.agendaSaas.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/postman")
public class PostmanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        System.out.println("Entrou no GET");

        PrintWriter out = res.getWriter();
        out.write("{\"mensagem\": \"Requisição GET recebida com sucesso!\"}");
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        System.out.println("Entrou no POST");

        PrintWriter out = res.getWriter();
        out.write("{\"mensagem\": \"Requisição POST recebida com sucesso!\"}");
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        System.out.println("Entrou no PUT");

        PrintWriter out = res.getWriter();
        out.write("{\"mensagem\": \"Requisição PUT recebida com sucesso!\"}");
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        System.out.println("Entrou no DELETE");

        PrintWriter out = res.getWriter();
        out.write("{\"mensagem\": \"Requisição DELETE recebida com sucesso!\"}");
        out.flush();
    }
}
*/