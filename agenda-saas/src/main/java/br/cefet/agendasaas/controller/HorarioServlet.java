package br.cefet.agendasaas.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import br.cefet.agendasaas.model.entidades.HorarioDisponivel;
import br.cefet.agendasaas.model.entidades.Prestador;
import br.cefet.agendasaas.model.entidades.Usuario;
import br.cefet.agendasaas.service.HorarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({ "/horarios", "/horarios/cadastrar", "/horarios/editar", "/horarios/remover", "/horarios/gerar" })
public class HorarioServlet extends GenericServlet {

    private final HorarioService horarioService = new HorarioService();

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
            switch (action) {
                case "/horarios":
                    listarHorarios(request, response, usuario);
                    break;
                case "/horarios/cadastrar":
                    exibirFormularioCadastro(request, response, usuario);
                    break;
                case "/horarios/editar":
                    exibirFormularioEdicao(request, response, usuario);
                    break;
                case "/horarios/remover":
                    removerHorario(request, response, usuario);
                    break;
                case "/horarios/gerar":
                    exibirFormularioGerarHorarios(request, response, usuario);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
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

        String action = request.getServletPath();

        try {
            switch (action) {
                case "/horarios/cadastrar":
                    cadastrarHorario(request, response, usuario);
                    break;
                case "/horarios/editar":
                    editarHorario(request, response, usuario);
                    break;
                case "/horarios/gerar":
                    gerarHorariosAutomaticos(request, response, usuario);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao processar solicitação: " + e.getMessage());
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
        }
    }

    private void listarHorarios(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        if (usuario instanceof Prestador) {
            List<HorarioDisponivel> horarios = horarioService.listarHorariosPorPrestador(usuario.getId());
            request.setAttribute("horarios", horarios);
            request.getRequestDispatcher("/WEB-INF/views/horarios/listar.jsp").forward(request, response);
        } else {
                request.setAttribute("erro", "Apenas prestadores podem gerenciar horários.");
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
        }
    }

    private void exibirFormularioCadastro(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        if (!(usuario instanceof Prestador)) {
            request.setAttribute("erro", "Apenas prestadores podem cadastrar horários.");
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/horarios/cadastrar.jsp").forward(request, response);
    }

    private void exibirFormularioEdicao(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        if (!(usuario instanceof Prestador)) {
            request.setAttribute("erro", "Apenas prestadores podem editar horários.");
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
            return;
        }

        String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
            request.setAttribute("erro", "ID do horário é obrigatório.");
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            HorarioDisponivel horario = horarioService.buscarPorId(id);

            if (horario == null) {
                request.setAttribute("erro", "Horário não encontrado.");
                request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
                return;
            }

            if (horario.getPrestadorId() != usuario.getId()) {
                request.setAttribute("erro", "Você não tem permissão para editar este horário.");
                request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
                return;
            }

            request.setAttribute("horario", horario);
            request.getRequestDispatcher("/WEB-INF/views/horarios/editar.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("erro", "ID do horário inválido.");
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
        }
    }

    private void removerHorario(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        if (!(usuario instanceof Prestador)) {
            request.setAttribute("erro", "Apenas prestadores podem remover horários.");
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
            return;
        }

        String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
            request.setAttribute("erro", "ID do horário é obrigatório.");
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            HorarioDisponivel horario = horarioService.buscarPorId(id);

            if (horario == null) {
                request.setAttribute("erro", "Horário não encontrado.");
                request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
                return;
            }

            if (horario.getPrestadorId() != usuario.getId()) {
                request.setAttribute("erro", "Você não tem permissão para remover este horário.");
                request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
                return;
            }

            boolean sucesso = horarioService.removerHorario(id);

            if (sucesso) {
                request.setAttribute("mensagem", "Horário removido com sucesso!");
            } else {
                request.setAttribute("erro", "Erro ao remover horário.");
            }

            response.sendRedirect(request.getContextPath() + "/horarios");

        } catch (NumberFormatException e) {
            request.setAttribute("erro", "ID do horÃ¡rio invÃ¡lido.");
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
        }
    }

    private void exibirFormularioGerarHorarios(HttpServletRequest request, HttpServletResponse response,
            Usuario usuario)
            throws ServletException, IOException {

        if (!(usuario instanceof Prestador)) {
            request.setAttribute("erro", "Apenas prestadores podem gerar horários.");
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/horarios/gerar.jsp").forward(request, response);
    }

    private void cadastrarHorario(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        if (!(usuario instanceof Prestador)) {
            request.setAttribute("erro", "Apenas prestadores podem cadastrar horários.");
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
            return;
        }

        try {
            String dataParam = request.getParameter("data");
            String horaInicioParam = request.getParameter("horaInicio");
            String horaFimParam = request.getParameter("horaFim");

            if (dataParam == null || horaInicioParam == null || horaFimParam == null) {
                    throw new IllegalArgumentException("Todos os campos são obrigatórios.");
            }

            LocalDate data = LocalDate.parse(dataParam);
            LocalTime horaInicio = LocalTime.parse(horaInicioParam);
            LocalTime horaFim = LocalTime.parse(horaFimParam);

            HorarioDisponivel horario = new HorarioDisponivel(usuario.getId(), data, horaInicio, horaFim);

            boolean sucesso = horarioService.cadastrarHorario(horario);

            if (sucesso) {
                request.setAttribute("mensagem", "HorÃ¡rio cadastrado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/horarios");
            } else {
                request.setAttribute("erro", "Erro ao cadastrar horÃ¡rio.");
                request.getRequestDispatcher("/WEB-INF/views/horarios/cadastrar.jsp").forward(request, response);
            }

        } catch (DateTimeParseException e) {
            request.setAttribute("erro", "Data ou horÃ¡rio invÃ¡lido.");
            request.getRequestDispatcher("/WEB-INF/views/horarios/cadastrar.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/horarios/cadastrar.jsp").forward(request, response);
        }
    }

    private void editarHorario(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        if (!(usuario instanceof Prestador)) {
            request.setAttribute("erro", "Apenas prestadores podem editar horÃ¡rios.");
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
            return;
        }

        try {
            String idParam = request.getParameter("id");
            String dataParam = request.getParameter("data");
            String horaInicioParam = request.getParameter("horaInicio");
            String horaFimParam = request.getParameter("horaFim");
            String disponivelParam = request.getParameter("disponivel");

            if (idParam == null || dataParam == null || horaInicioParam == null || horaFimParam == null) {
                throw new IllegalArgumentException("Todos os campos sÃ£o obrigatÃ³rios.");
            }

            int id = Integer.parseInt(idParam);
            LocalDate data = LocalDate.parse(dataParam);
            LocalTime horaInicio = LocalTime.parse(horaInicioParam);
            LocalTime horaFim = LocalTime.parse(horaFimParam);
            boolean disponivel = "true".equals(disponivelParam);

            HorarioDisponivel horario = horarioService.buscarPorId(id);
            if (horario == null) {
                throw new IllegalArgumentException("HorÃ¡rio nÃ£o encontrado.");
            }

            if (horario.getPrestadorId() != usuario.getId()) {
                throw new IllegalArgumentException("VocÃª nÃ£o tem permissÃ£o para editar este horÃ¡rio.");
            }

            horario.setData(data);
            horario.setHoraInicio(horaInicio);
            horario.setHoraFim(horaFim);
            horario.setDisponivel(disponivel);

            boolean sucesso = horarioService.atualizarHorario(horario);

            if (sucesso) {
                request.setAttribute("mensagem", "HorÃ¡rio atualizado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/horarios");
            } else {
                request.setAttribute("erro", "Erro ao atualizar horÃ¡rio.");
                request.setAttribute("horario", horario);
                request.getRequestDispatcher("/WEB-INF/views/horarios/editar.jsp").forward(request, response);
            }

        } catch (NumberFormatException | DateTimeParseException e) {
            request.setAttribute("erro", "Dados invÃ¡lidos fornecidos.");
            request.getRequestDispatcher("/WEB-INF/views/horarios/editar.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/horarios/editar.jsp").forward(request, response);
        }
    }

    private void gerarHorariosAutomaticos(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        if (!(usuario instanceof Prestador)) {
            request.setAttribute("erro", "Apenas prestadores podem gerar horÃ¡rios.");
            request.getRequestDispatcher("/utils/erro.jsp").forward(request, response);
            return;
        }

        try {
            String dataInicioParam = request.getParameter("dataInicio");
            String dataFimParam = request.getParameter("dataFim");
            String horaInicioParam = request.getParameter("horaInicio");
            String horaFimParam = request.getParameter("horaFim");
            String intervalosParam = request.getParameter("intervalos");

            if (dataInicioParam == null || dataFimParam == null || horaInicioParam == null ||
                    horaFimParam == null || intervalosParam == null) {
                throw new IllegalArgumentException("Todos os campos sÃ£o obrigatÃ³rios.");
            }

            LocalDate dataInicio = LocalDate.parse(dataInicioParam);
            LocalDate dataFim = LocalDate.parse(dataFimParam);
            LocalTime horaInicio = LocalTime.parse(horaInicioParam);
            LocalTime horaFim = LocalTime.parse(horaFimParam);
            int intervalos = Integer.parseInt(intervalosParam);

            boolean sucesso = horarioService.gerarHorariosAutomaticos(
                    usuario.getId(), dataInicio, dataFim, horaInicio, horaFim, intervalos);

            if (sucesso) {
                request.setAttribute("mensagem", "HorÃ¡rios gerados com sucesso!");
                response.sendRedirect(request.getContextPath() + "/horarios");
            } else {
                request.setAttribute("erro", "Erro ao gerar horÃ¡rios automÃ¡ticos.");
                request.getRequestDispatcher("/WEB-INF/views/horarios/gerar.jsp").forward(request, response);
            }

        } catch (NumberFormatException | DateTimeParseException e) {
            request.setAttribute("erro", "Dados invÃ¡lidos fornecidos.");
            request.getRequestDispatcher("/WEB-INF/views/horarios/gerar.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/horarios/gerar.jsp").forward(request, response);
        }
    }
}

