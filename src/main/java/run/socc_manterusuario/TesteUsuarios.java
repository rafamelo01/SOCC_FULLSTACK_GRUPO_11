package run.socc_manterusuario;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import Models.Entity.Perfil;
import Models.Entity.Usuario;
import Models.Entity.Status;
import Repository.UsuarioRepository;

@SpringBootApplication
@ComponentScan(basePackages = {"run.socc_manterusuario", "Controller", "Service"})
@EntityScan(basePackages = {"Models.Entity"})
@EnableJpaRepositories(basePackages = "Repository")  // Certifica-se de que o pacote Repository será escaneado
public class TesteUsuarios implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        SpringApplication.run(TesteUsuarios.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Cria um diretor (pode alterar status/perfis)
        if (!usuarioRepository.existsByEmail("joao@escola.com")){
            Usuario diretor = new Usuario("João Diretor", "joao", "joao@escola.com");
            diretor.setPerfis(Arrays.asList(Perfil.DIRETOR));
            usuarioRepository.save(diretor);

            if (!usuarioRepository.existsByEmail("maria@escola.com")){
                // Cria um usuário professor/docente
                Usuario professora = new Usuario("Maria Aluna", "maria", "maria@escola.com");
                professora.setPerfis(Arrays.asList(Perfil.DOCENTE));
        
                // Persiste os usuários no banco
                usuarioRepository.save(professora);
                System.out.println(GREEN + "Status inicial da Maria: " + professora.getStatus() + RESET);
    
                // Diretor altera o status de Maria
                try {
                    professora.alterarStatus(Status.INATIVO, diretor);
                    usuarioRepository.save(professora);
                    System.out.println(GREEN + "Novo status da Maria: " + professora.getStatus() + RESET);
                } catch (Exception e) {
                    System.out.println(RED + "Erro ao alterar status: " + e.getMessage() + RESET);
                }
        
                // Tenta alterar o perfil de Maria com outro aluno como solicitante
                Usuario jose = new Usuario("José Estudante", "jose", "jose@escola.com");
                jose.setPerfis(Arrays.asList(Perfil.DISCENTE));
                usuarioRepository.save(jose);
        
                try {
                    professora.alterarPerfil(Arrays.asList(Perfil.DISCENTE), jose);
                } catch (Exception e) {
                    System.out.println(RED + "Erro ao alterar perfil: " + e.getMessage() + RESET);
                }
            }
        }

        // // Tenta alterar carga horaria de um usuario que possui somente o perfil DISCENTE
        // try {
        //     System.out.println(GREEN + "Carga horária mínima: " + professora.getCargaHorariaMinima() + RESET);
        //     professora.alterarCargaHorariaMinima(32, diretor);
        //     usuarioRepository.save(professora);
        //     System.out.println(GREEN + "Nova carga horária mínima: " + professora.getCargaHorariaMinima() + RESET);
        // } catch (Exception e) {
        //     System.out.println(RED + "Erro ao alterar carga horária mínima: " + e.getMessage() + RESET);
        // }

        // // Diretor altera o perfil da Maria
        // try {
        //     aluno.alterarPerfil(Arrays.asList(Perfil.DOCENTE), diretor);
        //     aluno.setCargaHorariaMinima(64);
        //     usuarioRepository.save(aluno);
        //     System.out.println(GREEN + "Perfis da Maria: " + aluno.getPerfis() + RESET);
        // } catch (Exception e) {
        //     System.out.println(RED + "Erro ao alterar perfil: " + e.getMessage() + RESET);
        // }

        // // Altera a carga horária mínima do perfil DOCENTE sendo o DIRETOR um solicitante 
        // try {
        //     System.out.println(GREEN + "Carga horária mínima: " + aluno.getCargaHorariaMinima() + RESET);
        //     aluno.alterarCargaHorariaMinima(32, diretor);
        //     usuarioRepository.save(aluno);
        //     System.out.println(GREEN + "Nova carga horária mínima: " + aluno.getCargaHorariaMinima() + RESET);
        // } catch (Exception e) {
        //     System.out.println(RED + "Erro ao alterar carga horária mínima: " + e.getMessage() + RESET);
        // }

        // // Tenta alterar a carga horária mínima usando um perfil de DISCENTE (não permitido) apenas diretores ou vice-diretores
        // try {
        //     System.out.println(GREEN + "Carga horária mínima: " + aluno.getCargaHorariaMinima() + RESET);
        //     aluno.alterarCargaHorariaMinima(32, jose);
        //     usuarioRepository.save(aluno);
        //     System.out.println(GREEN + "Nova carga horária mínima: " + aluno.getCargaHorariaMinima() + RESET);
        // } catch (Exception e) {
        //     System.out.println(RED + "Erro ao alterar carga horária mínima: " + e.getMessage() + RESET);
        // }
    }
}
