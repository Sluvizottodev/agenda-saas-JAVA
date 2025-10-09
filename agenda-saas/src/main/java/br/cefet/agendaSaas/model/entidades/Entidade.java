import agenda-saas\src\main\java\br\cefet\agendaSaas\model\entidades\Usuario.java

@MappedSuperclass
public class Entidade {
    @id
    @GenetatedValue (strategy = GenerationType.AUTO)
    private Integer id;

    private LocalDate dataCriacao;
    @Collumn (nullable = true
    private LocalDate dataUltimaAlteracao;
    @ManyToOne
    private Usuario criadoPor;
    @ManyToOne(optional = true)
    private Usuario alteradoPor;
}
