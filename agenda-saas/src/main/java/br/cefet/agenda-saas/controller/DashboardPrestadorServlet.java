package br.cefet.agendaSaas.controller;

import java.io.IOException;
import java.util.List;

import br.cefet.agendaSaas.dao.AgendamentoDAO;
import br.cefet.agendaSaas.dao.ServicoDAO;
import br.cefet.agendaSaas.model.entidades.Agendamento;
import br.cefet.agendaSaas.model.entidades.Prestador;
import br.cefet.agendaSaas.model.entidades.Servico;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/dashboard/prestador")
public class DashboardPrestadorServlet extends GenericServlet {

    private final ServicoDAO servicoDAO = new ServicoDAO();
    private final AgendamentoDAO agendamentoDAO = new AgendamentoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Aqui você deve obter o prestador logado. Exemplo:
        Prestador prestador = (Prestador) request.getSession().getAttribute("usuarioLogado");
        if (prestador == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        try {
            List<Servico> servicos = servicoDAO.listarPorPrestador(prestador.getId());
            List<Agendamento> agendamentos = agendamentoDAO.listarPorPrestador(prestador.getId());

            request.setAttribute("prestador", prestador);
            request.setAttribute("servicos", servicos);
            request.setAttribute("agendamentos", agendamentos);

            request.getRequestDispatcher("/WEB-INF/views/dashboardPrestador.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Erro ao carregar dados do dashboard", e);
        }
    }
}
