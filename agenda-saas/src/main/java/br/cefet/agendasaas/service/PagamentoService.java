package br.cefet.agendasaas.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.cefet.agendasaas.dao.GenericDAO;
import br.cefet.agendasaas.model.entidades.Pagamento;
import br.cefet.agendasaas.utils.ValidationException;

@Service
public class PagamentoService {

    private final GenericDAO<Pagamento, Integer> dao = new GenericDAO<>(Pagamento.class);

    public PagamentoService() {
        super();
    }

    public void processarPagamento(Pagamento pagamento) throws ValidationException {
        validarPagamento(pagamento);

        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setStatus("PROCESSANDO");

        Pagamento saved = dao.save(pagamento);
        if (saved == null) {
            throw new ValidationException("Erro ao salvar pagamento");
        }

        boolean sucesso = simularProcessamentoPagamento();

        if (sucesso) {
            pagamento.setStatus("APROVADO");
        } else {
            pagamento.setStatus("REJEITADO");
        }

        dao.update(pagamento);
    }

    private boolean simularProcessamentoPagamento() {
        try {
            Thread.sleep(1000);
            return Math.random() > 0.05;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private void validarPagamento(Pagamento pagamento) throws ValidationException {
        if (pagamento == null) {
            throw new ValidationException("Pagamento não pode ser nulo");
        }
        if (pagamento.getAgendamentoId() == null || pagamento.getAgendamentoId() <= 0) {
            throw new ValidationException("ID do agendamento é obrigatório");
        }
        if (pagamento.getValor() == null || pagamento.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Valor deve ser maior que zero");
        }
    }

    public List<Pagamento> listarPorAgendamento(int agendamentoId) {
        return dao.findWithQuery("FROM Pagamento p WHERE p.agendamentoId = ? ORDER BY p.dataPagamento DESC",
                agendamentoId);
    }

    public List<Pagamento> listarPorStatus(String status) {
        return dao.findWithQuery("FROM Pagamento p WHERE p.status = ? ORDER BY p.dataPagamento DESC", status);
    }

    public BigDecimal calcularTotalPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Pagamento> pagamentos = dao.findWithQuery(
                "FROM Pagamento p WHERE p.status = 'APROVADO' AND p.dataPagamento BETWEEN ? AND ?",
                dataInicio, dataFim);

        return pagamentos.stream()
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void estornarPagamento(int pagamentoId) throws ValidationException {
        Pagamento pagamento = dao.findById(pagamentoId).orElse(null);
        if (pagamento == null) {
            throw new ValidationException("Pagamento não encontrado");
        }

        if (!"APROVADO".equals(pagamento.getStatus())) {
            throw new ValidationException("Apenas pagamentos aprovados podem ser estornados");
        }

        pagamento.setStatus("ESTORNADO");
        dao.update(pagamento);
    }

    public Pagamento buscarPorId(int id) {
        return dao.findById(id).orElse(null);
    }

    public List<Pagamento> listarTodos() {
        return dao.findAll();
    }

    public boolean remover(int id) {
        return dao.deleteById(id) > 0;
    }
}
