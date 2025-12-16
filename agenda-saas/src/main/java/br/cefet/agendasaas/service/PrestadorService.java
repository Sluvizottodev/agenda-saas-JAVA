package br.cefet.agendasaas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.cefet.agendasaas.model.entidades.Prestador;
import br.cefet.agendasaas.repository.PrestadorRepository;
import br.cefet.agendasaas.utils.ValidationException;

@Service
public class PrestadorService {

    private final PrestadorRepository prestadorRepository;

    public PrestadorService(PrestadorRepository prestadorRepository) {
        this.prestadorRepository = prestadorRepository;
    }

    public Prestador criarPrestador(Prestador prestador) throws ValidationException {
        validarPrestador(prestador);
        return prestadorRepository.save(prestador);
    }

    private void validarPrestador(Prestador prestador) throws ValidationException {
        if (prestador == null) {
            throw new ValidationException("Prestador não pode ser nulo");
        }
        if (prestador.getNome() == null || prestador.getNome().trim().isEmpty()) {
            throw new ValidationException("Nome é obrigatório");
        }
        if (prestador.getEmail() == null || prestador.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email é obrigatório");
        }
        if (prestador.getTelefone() == null || prestador.getTelefone().trim().isEmpty()) {
            throw new ValidationException("Telefone é obrigatório");
        }
        if (prestador.getEspecializacao() == null || prestador.getEspecializacao().trim().isEmpty()) {
            throw new ValidationException("Especialização é obrigatória");
        }
    }

    public List<Prestador> buscarPorEspecializacao(String especializacao) {
        return prestadorRepository.findByEspecializacaoContaining(especializacao);
    }

    public Prestador buscarPorEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email é obrigatório.");
        }

        Optional<Prestador> prestador = prestadorRepository.findByEmail(email);
        if (!prestador.isPresent()) {
            throw new ValidationException("Prestador não encontrado com o email informado.");
        }
        return prestador.get();
    }

    public Prestador atualizarPrestador(Prestador prestador) throws ValidationException {
        if (prestador.getId() == null || prestador.getId() <= 0) {
            throw new ValidationException("ID do prestador é obrigatório para atualização");
        }

        validarPrestador(prestador);

        Optional<Prestador> existente = prestadorRepository.findById(prestador.getId());
        if (!existente.isPresent()) {
            throw new ValidationException("Prestador não encontrado para atualização.");
        }

        return prestadorRepository.save(prestador);
    }

    public void removerPrestador(int id) throws ValidationException {
        if (id <= 0) {
            throw new ValidationException("ID do prestador deve ser positivo");
        }

        Optional<Prestador> prestador = prestadorRepository.findById(id);
        if (!prestador.isPresent()) {
            throw new ValidationException("Prestador não encontrado para remoção.");
        }

        prestadorRepository.deleteById(id);
    }

    public Prestador buscarPorId(int id) throws ValidationException {
        if (id <= 0) {
            throw new ValidationException("ID do prestador deve ser positivo.");
        }

        Optional<Prestador> prestador = prestadorRepository.findById(id);
        if (!prestador.isPresent()) {
            throw new ValidationException("Prestador não encontrado.");
        }
        return prestador.get();
    }

    public List<Prestador> listarTodos() {
        return prestadorRepository.findAll();
    }
}
