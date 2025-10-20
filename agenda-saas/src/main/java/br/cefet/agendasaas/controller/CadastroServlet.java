package br.cefet.agendasaas.controller;
import br.cefet.agendasaas.model.entidades.*;
import br.cefet.agendasaas.model.enums.TipoUsuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/auth/cadastro")
public class CadastroServlet extends GenericServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipoUsuario = request.getParameter("tipo"); // "cliente" ou "prestador"
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        if ("cliente".equalsIgnoreCase(tipoUsuario)) {
            String cpf = request.getParameter("cpf");

            Cliente cliente = new Cliente();
            cliente.setNome(nome);
            cliente.setEmail(email);
            cliente.setSenha(senha);
            cliente.setCpf(cpf);

            System.out.println("Cliente cadastrado: " + cliente.getNome());

        } else if ("prestador".equalsIgnoreCase(tipoUsuario)) {
            String telefone = request.getParameter("telefone");
            String especializacao = request.getParameter("especializacao");
            String cnpj = request.getParameter("cnpj");

            Prestador prestador = new Prestador();
            prestador.setNome(nome);
            prestador.setEmail(email);
            prestador.setSenha(senha);
            prestador.setTelefone(telefone);
            prestador.setEspecializacao(especializacao);
            prestador.setCnpj(cnpj);
            
            System.out.println("Prestador cadastrado: " + prestador.getNome());

        } else {
            request.setAttribute("erro", "Tipo de usuÃ¡rio invÃ¡lido.");
            request.getRequestDispatcher("cadastro.jsp").forward(request, response);
            return;
        }

        request.setAttribute("mensagem", "Cadastro realizado com sucesso!");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}

