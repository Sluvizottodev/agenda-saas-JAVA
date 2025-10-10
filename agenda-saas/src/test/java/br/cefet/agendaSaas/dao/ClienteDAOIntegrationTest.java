package br.cefet.agendaSaas.dao;

import br.cefet.agendaSaas.model.entidades.Cliente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClienteDAOIntegrationTest {

    private ClienteDAO clienteDAO;
    private Cliente created;

    @BeforeEach
    public void setup() {
        clienteDAO = new ClienteDAO();
    }

    @AfterEach
    public void teardown() {
        if (created != null && created.getId() != 0) {
            clienteDAO.remover(created.getId());
        }
    }

    @Test
    public void inserirEPesquisarPorId_deveGerarIdENcontrarRegistro() {
        Cliente c = new Cliente();
        c.setNome("Teste Integração");
        c.setEmail("teste.integ@example.com");
        c.setSenha("senha123");
        c.setCpf("000.000.000-00");

        boolean ok = clienteDAO.inserir(c);
        Assertions.assertTrue(ok, "Inserção deve retornar true");
        Assertions.assertTrue(c.getId() > 0, "ID deve ser maior que zero após inserção");

        created = c;

        Cliente fetched = clienteDAO.buscarPorId(c.getId());
        Assertions.assertNotNull(fetched, "Deve conseguir buscar o cliente inserido por ID");
        Assertions.assertEquals(c.getEmail(), fetched.getEmail());
    }
}
