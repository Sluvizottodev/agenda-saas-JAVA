package br.cefet.agendaSaas.controller;

import br.cefet.agendaSaas.dao.AgendamentoDAO;
import br.cefet.agendaSaas.dao.ServicoDAO;
import br.cefet.agendaSaas.model.entidades.Cliente;
import br.cefet.agendaSaas.model.entidades.Servico;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import br.cefet.agendaSaas.model.entidades.Agendamento;

@WebServlet("/dashboard/cliente")
public class DashboardClienteServlet extends GenericServlet {

    private final ServicoDAO servicoDAO = new ServicoDAO();
    private final AgendamentoDAO agendamentoDAO = new AgendamentoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtém o cliente logado
        Cliente cliente = (Cliente) request.getSession().getAttribute("usuarioLogado");
        if (cliente == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        List<Agendamento> agendamentos = agendamentoDAO.listarPorCliente(cliente.getId());
        List<Servico> servicos = servicoDAO.listarTodos(); // Mostra todos os serviços disponíveis

        request.setAttribute("cliente", cliente);
        request.setAttribute("agendamentos", agendamentos);
        request.setAttribute("servicos", servicos);

        request.getRequestDispatcher("/WEB-INF/views/dashboardCliente.jsp").forward(request, response);
    }
}
