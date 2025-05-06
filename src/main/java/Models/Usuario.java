package Models;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String usuario;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String foto;

    @ElementCollection(targetClass = Perfil.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "usuario_perfil", joinColumns = @JoinColumn(name = "usuario_id"))
    private List<Perfil> perfis;
    

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "carga_horaria_minima")
    private Integer cargaHorariaMinima;

    // Construtor sem argumentos (necessário para JPA)
    public Usuario() {
    }
        
    public Usuario(String nome, String usuario, String email) {
        this.nome = nome;
        this.usuario = usuario;
        this.email = email;
        this.perfis = new ArrayList<>(); 
        this.status = Status.ATIVO; 
    }

    // Construtores, getters e setters

    public String getNome() {
        return this.nome;
    }

    public String getUsuario() {
        return this.usuario;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    public List<Perfil> getPerfis() {
        return new ArrayList<>(this.perfis); 
    }

    public Integer getCargaHorariaMinima() {
        return this.cargaHorariaMinima;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public void setPerfis(List<Perfil> perfis) {
        this.perfis = perfis;
    }

    public void setCargaHorariaMinima(Integer cargaHorariaMinima) {
        this.cargaHorariaMinima = cargaHorariaMinima;
    }

    public boolean isDiretorOuVice() {
        return perfis.contains(Perfil.DIRETOR) || perfis.contains(Perfil.VICE_DIRETOR);
    }


    public void alterarStatus(Status novoStatus, Usuario solicitante) {
        if (!solicitante.isDiretorOuVice()) {
            throw new SecurityException("Apenas Diretor ou Vice-diretor podem alterar o status.");
        }
        this.status = novoStatus;
    }


    public void alterarPerfil(List<Perfil> novosPerfis, Usuario solicitante) {
        if (!solicitante.isDiretorOuVice()) {
            throw new SecurityException("Apenas Diretor ou Vice-diretor podem alterar o perfil.");
        }
        this.perfis = novosPerfis;
    }

    public void alterarCargaHorariaMinima(Integer novaCargaHoraria, Usuario solicitante) {
        if (!this.perfis.contains(Perfil.DOCENTE)) {
            throw new UnsupportedOperationException("Carga horária mínima só se aplica a docentes.");
        }
    
        if (!solicitante.isDiretorOuVice()) {
            throw new SecurityException("Apenas Diretor ou Vice-diretor podem alterar a carga horária mínima de um docente.");
        }
    
        this.cargaHorariaMinima = novaCargaHoraria;
    }

    public void listarUsuarios(Usuario solicitante) {

        System.out.println("Listando usuários...");
    }

}
