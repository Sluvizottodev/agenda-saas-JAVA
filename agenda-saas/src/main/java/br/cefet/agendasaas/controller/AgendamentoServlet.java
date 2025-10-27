package br.cefet.agendasaas.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import br.cefet.agendasaas.dao.AgendamentoDAO;
import br.cefet.agendasaas.dao.ServicoDAO;
import br.cefet.agendasaas.dao.UsuarioDAO;
import br.cefet.agendasaas.model.entidades.Agendamento;
import br.cefet.agendasaas.model.entidades.Cliente;
import br.cefet.agendasaas.model.entidades.Prestador;
import br.cefet.agendasaas.model.entidades.Servico;
import br.cefet.agendasaas.model.entidades.Usuario;
import br.cefet.agendasaas.utils.EmailUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({ "/agendar", "/agendamentos" })
public class AgendamentoServlet extends GenericServlet {

    private final AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        String action = request.getServletPath();

        try {
            if ("/agendar".equals(action)) {
                List<Servico> servicosDisponiveis = servicoDAO.listarTodos();
                request.setAttribute("servicosDisponiveis", servicosDisponiveis);
                request.getRequestDispatcher("/WEB-INF/views/agendar.jsp").forward(request, response);

            } else if ("/agendamentos".equals(action)) {
                List<Agendamento> agendamentos;

                if (usuario instanceof Cliente) {
                    agendamentos = agendamentoDAO.listarPorCliente(usuario.getId());
                } else {
                    agendamentos = agendamentoDAO.listarPorPrestador(usuario.getId());
                }

                request.setAttribute("agendamentos", agendamentos);
                request.getRequestDispatcher("/agendamentos.jsp").forward(request, response);
            }

        } catch (jakarta.servlet.ServletException | java.io.IOException e) {
            handleException(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        if (!(usuario instanceof Cliente)) {
            request.setAttribute("erro", "Apenas clientes podem fazer agendamentos.");
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
            return;
        }

        try {
            int servicoId = br.cefet.agendasaas.utils.InputValidator.parsePositiveInt(request.getParameter("servicoId"),
                    "Serviço");
            LocalDate data = java.time.LocalDate.parse(
                    br.cefet.agendasaas.utils.InputValidator.requireNonBlank(request.getParameter("data"), "Data"));
            LocalTime hora = java.time.LocalTime.parse(
                    br.cefet.agendasaas.utils.InputValidator.requireNonBlank(request.getParameter("hora"), "Hora"));
            String observacoes = request.getParameter("observacoes");

            LocalDateTime dataHora = LocalDateTime.of(data, hora);

            if (dataHora.isBefore(LocalDateTime.now())) {
                throw new br.cefet.agendasaas.utils.ValidationException("A data e horário devem ser futuros");
            }

            Servico servico = servicoDAO.buscarPorId(servicoId);
            if (servico == null) {
                throw new br.cefet.agendasaas.utils.ValidationException("Serviço não encontrado");
            }

            Agendamento agendamento = new Agendamento();
            agendamento.setClienteId(usuario.getId());
            agendamento.setPrestadorId(servico.getPrestadorId());
            agendamento.setServicoId(servicoId);
            agendamento.setDataHora(dataHora);
            agendamento.setStatus(br.cefet.agendasaas.model.enums.StatusAgendamento.PENDENTE.name());

            boolean sucesso = agendamentoDAO.inserir(agendamento);

            if (sucesso) {
                try {
                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    Prestador prestador = null;
                    try {
                        prestador = (Prestador) usuarioDAO.buscarPorId(agendamento.getPrestadorId());
                    } catch (Exception e) {
                        System.err.println("AgendamentoServlet: falha ao buscar prestador (ID="
                                + agendamento.getPrestadorId() + "): " + e.getMessage());
                        e.printStackTrace(System.err);
                        prestador = null;
                    }

                    Cliente clienteObj = (Cliente) usuario; // ja eh cliente
                    EmailUtils.notifyAgendamentoAsync(agendamento, servico, prestador, clienteObj);
                } catch (Exception t) {
                    System.err.println("AgendamentoServlet: erro ao enviar notificação: " + t.getMessage());
                    t.printStackTrace(System.err);
                }

                request.setAttribute("mensagem",
                        "Agendamento realizado com sucesso! Seu agendamento está pendente de confirmação.");
                request.setAttribute("agendamento", agendamento);
                request.getRequestDispatcher("/WEB-INF/views/agendamento-sucesso.jsp").forward(request, response);
            } else {
                throw new RuntimeException("Erro ao salvar o agendamento no banco de dados.");
            }

        } catch (br.cefet.agendasaas.utils.ValidationException ve) {
            request.setAttribute("erro", ve.getMessage());
            carregarFormularioAgendamento(request, response);
        } catch (DateTimeParseException e) {
            request.setAttribute("erro", "Data ou horário inválido.");
            carregarFormularioAgendamento(request, response);
        } catch (Exception e) {
            handleException(request, response, e);
        }
    }

    private void carregarFormularioAgendamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Servico> servicosDisponiveis = servicoDAO.listarTodos();
            request.setAttribute("servicosDisponiveis", servicosDisponiveis);
            request.getRequestDispatcher("/WEB-INF/views/agendar.jsp").forward(request, response);
        } catch (Exception e) {
            handleException(request, response, e);
        }
    }
}
