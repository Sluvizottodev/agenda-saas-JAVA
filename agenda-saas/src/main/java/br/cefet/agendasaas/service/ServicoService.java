package br.cefet.agendasaas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.cefet.agendasaas.model.entidades.Servico;
import br.cefet.agendasaas.repository.ServicoRepository;
import br.cefet.agendasaas.utils.ValidationException;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public Servico cadastrar(Servico servico) throws ValidationException {
        validarServico(servico);
        return servicoRepository.save(servico);
    }

    public Servico buscarPorId(int id) throws ValidationException {
        if (id <= 0) {
            throw new ValidationException("ID do serviço deve ser positivo.");
        }

        Optional<Servico> servico = servicoRepository.findById(id);
        if (!servico.isPresent()) {
            throw new ValidationException("Serviço não encontrado.");
        }
        return servico.get();
    }

    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    public Servico atualizar(Servico servico) throws ValidationException {
        if (servico.getId() == null || servico.getId() <= 0) {
            throw new ValidationException("ID do serviço é obrigatório para atualização.");
        }

        validarServico(servico);
        
        // Verifica se o serviço existe
        Optional<Servico> existente = servicoRepository.findById(servico.getId());
        if (!existente.isPresent()) {
            throw new ValidationException("Serviço não encontrado para atualização.");
        }

        return servicoRepository.save(servico);
    }

    public void remover(int id) throws ValidationException {
        if (id <= 0) {
            throw new ValidationException("ID do serviço deve ser positivo.");
        }

        Optional<Servico> servico = servicoRepository.findById(id);
        if (!servico.isPresent()) {
            throw new ValidationException("Serviço não encontrado para remoção.");
        }

        servicoRepository.deleteById(id);
    }

    public List<Servico> listarPorPrestador(int prestadorId) throws ValidationException {
        if (prestadorId <= 0) {
            throw new ValidationException("ID do prestador deve ser positivo.");
        }
        return servicoRepository.findByPrestadorId(prestadorId);
    }

    private void validarServico(Servico servico) throws ValidationException {
        if (servico == null) {
            throw new ValidationException("Serviço não pode ser nulo.");
        }
        if (servico.getNome() == null || servico.getNome().trim().isEmpty()) {
            throw new ValidationException("Nome do serviço é obrigatório.");
        }
        if (servico.getDescricao() == null || servico.getDescricao().trim().isEmpty()) {
            throw new ValidationException("Descrição do serviço é obrigatória.");
        }
        if (servico.getPreco() < 0) {
            throw new ValidationException("Preço do serviço não pode ser negativo.");
        }
        if (servico.getPrestadorId() == null || servico.getPrestadorId() <= 0) {
            throw new ValidationException("Prestador é obrigatório para o serviço.");
        }
    }
}
