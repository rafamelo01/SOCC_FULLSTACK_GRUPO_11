package run.socc_manterusuario;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import Models.Perfil;
import Models.Usuario;
import Models.Status;
import Repository.UsuarioRepository;

@SpringBootApplication
@EntityScan(basePackages = "Models")  // Adicione esta linha para garantir que o pacote Models será escaneado
@EnableJpaRepositories(basePackages = "Repository")  // Certifica-se de que o pacote Repository será escaneado
public class TesteUsuarios implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public static void main(String[] args) {
        SpringApplication.run(TesteUsuarios.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Cria um diretor (pode alterar status/perfis)
        Usuario diretor = new Usuario("João Diretor", "joao", "joao@escola.com");
        diretor.setPerfis(Arrays.asList(Perfil.DIRETOR));

        // Cria um usuário discente/aluno
        Usuario aluno = new Usuario("Maria Aluna", "maria", "maria@escola.com");
        aluno.setPerfis(Arrays.asList(Perfil.DISCENTE));

        // Persiste os usuários no banco
        usuarioRepository.save(diretor);
        usuarioRepository.save(aluno);
        
        System.out.println("Status inicial da Maria: " + aluno.getStatus());
        
        // Diretor altera o status de Maria
        try {
            aluno.alterarStatus(Status.INATIVO, diretor);
            usuarioRepository.save(aluno);  // Salva a alteração
            System.out.println("Novo status da Maria: " + aluno.getStatus());
        } catch (Exception e) {
            System.out.println("Erro ao alterar status: " + e.getMessage());
        }
        
        // Tenta alterar o perfil de Maria com outro aluno como solicitante
        Usuario jose = new Usuario("José Estudante", "jose", "jose@escola.com");
        jose.setPerfis(Arrays.asList(Perfil.DISCENTE));
        
        try {
            aluno.alterarPerfil(Arrays.asList(Perfil.DOCENTE), jose);
        } catch (Exception e) {
            System.out.println("Erro ao alterar perfil: " + e.getMessage());
        }
        
        // Tenta alterar carga horaria de um usuario que possui somente o perfil DISCENTE
        try {
            System.out.println("carga horária minima: " + aluno.getCargaHorariaMinima());
            aluno.alterarCargaHorariaMinima(32, diretor);
            usuarioRepository.save(aluno);  
            System.out.println("Nova carga horária minima: " + aluno.getCargaHorariaMinima());
        } catch (Exception e) {
            System.out.println("Erro ao alterar carga horaria minima: " + e.getMessage());
        }
        
        // Diretor altera o perfil da Maria
        try {
            aluno.alterarPerfil(Arrays.asList(Perfil.DOCENTE), diretor);
            aluno.setCargaHorariaMinima(64);
            usuarioRepository.save(aluno);  
            System.out.println("Perfis da Maria: " + aluno.getPerfis());
        } catch (Exception e) {
            System.out.println("Erro ao alterar perfil: " + e.getMessage());
        }
        
        // Altera a carga horária mínima do perfil DOCENTE sendo o DIRETOR um solicitante 
        try {
            System.out.println("carga horária minima: " + aluno.getCargaHorariaMinima());
            aluno.alterarCargaHorariaMinima(32, diretor);
            usuarioRepository.save(aluno);  
            System.out.println("Nova carga horária minima: " + aluno.getCargaHorariaMinima());
        } catch (Exception e) {
            System.out.println("Erro ao alterar carga horaria minima: " + e.getMessage());
        }
        
        // Tenta alterar a carga horária mínima usando um perfil de DISCENTE (não permitido) apenas diretores ou vice-diretores
        try {
            System.out.println("carga horária minima: " + aluno.getCargaHorariaMinima());
            aluno.alterarCargaHorariaMinima(32, jose);
            usuarioRepository.save(aluno);  
            System.out.println("Nova carga horária minima: " + aluno.getCargaHorariaMinima());
        } catch (Exception e) {
            System.out.println("Erro ao alterar carga horaria minima: " + e.getMessage());
        }
        
    }
}
