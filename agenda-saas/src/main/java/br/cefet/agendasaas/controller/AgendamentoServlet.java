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
                request.getRequestDispatcher("/agendar.jsp").forward(request, response);

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

        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao processar solicitaÃ§Ã£o: " + e.getMessage());
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
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
            String servicoIdParam = request.getParameter("servicoId");
            String dataParam = request.getParameter("data");
            String horaParam = request.getParameter("hora");
            String observacoes = request.getParameter("observacoes");

            if (servicoIdParam == null || dataParam == null || horaParam == null) {
                throw new IllegalArgumentException("Todos os campos obrigatÃ³rios devem ser preenchidos.");
            }

            int servicoId = Integer.parseInt(servicoIdParam);
            LocalDate data = LocalDate.parse(dataParam);
            LocalTime hora = LocalTime.parse(horaParam);
            LocalDateTime dataHora = LocalDateTime.of(data, hora);

            if (dataHora.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("A data e horÃ¡rio devem ser futuros.");
            }

            Servico servico = servicoDAO.buscarPorId(servicoId);
            if (servico == null) {
                throw new IllegalArgumentException("ServiÃ§o nÃ£o encontrado.");
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
                        prestador = null;
                    }

                    Cliente clienteObj = (Cliente) usuario; // jÃ¡ Ã© cliente
                    EmailUtils.notifyAgendamentoAsync(agendamento, servico, prestador, clienteObj);
                } catch (Throwable t) {
                    System.err.println("Falha ao disparar notificaÃ§Ãµes por e-mail: " + t.getMessage());
                }

                request.setAttribute("mensagem",
                        "Agendamento realizado com sucesso! Seu agendamento estÃ¡ pendente de confirmaÃ§Ã£o.");
                request.setAttribute("agendamento", agendamento);
                request.getRequestDispatcher("/agendamento-sucesso.jsp").forward(request, response);
            } else {
                throw new RuntimeException("Erro ao salvar o agendamento no banco de dados.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("erro", "Dados invÃ¡lidos fornecidos.");
            carregarFormularioAgendamento(request, response);

        } catch (DateTimeParseException e) {
            request.setAttribute("erro", "Data ou horÃ¡rio invÃ¡lido.");
            carregarFormularioAgendamento(request, response);

        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            carregarFormularioAgendamento(request, response);

        } catch (Exception e) {
            request.setAttribute("erro", "Erro interno do servidor: " + e.getMessage());
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
        }
    }

    private void carregarFormularioAgendamento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Servico> servicosDisponiveis = servicoDAO.listarTodos();
            request.setAttribute("servicosDisponiveis", servicosDisponiveis);
            request.getRequestDispatcher("/agendar.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao carregar formulÃ¡rio: " + e.getMessage());
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
        }
    }
}

