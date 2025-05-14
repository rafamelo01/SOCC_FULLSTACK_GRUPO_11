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
            Usuario diretor = new Usuario("João", "joao", "joao@escola.com");
            diretor.setPerfis(Arrays.asList(Perfil.DIRETOR));
            usuarioRepository.save(diretor);

            if (!usuarioRepository.existsByEmail("maria@escola.com")){
                // Cria um usuário professor/docente
                Usuario maria = new Usuario("Maria", "maria", "maria@escola.com");
                maria.setPerfis(Arrays.asList(Perfil.DOCENTE));
        
                // Persiste os usuários no banco
                usuarioRepository.save(maria);
                System.out.println(GREEN + "Status inicial da Maria: " + maria.getStatus() + RESET);
    
                // Diretor altera o status de Maria
                try {
                    maria.alterarStatus(Status.INATIVO, diretor);
                    usuarioRepository.save(maria);
                    System.out.println(GREEN + "Novo status da Maria: " + maria.getStatus() + RESET);
                } catch (Exception e) {
                    System.out.println(RED + "Erro ao alterar status: " + e.getMessage() + RESET);
                }
        
                // Tenta alterar o perfil de Maria com outro DOCENTE como solicitante
                Usuario jose = new Usuario("José", "jose", "jose@escola.com");
                jose.setPerfis(Arrays.asList(Perfil.DOCENTE));
                usuarioRepository.save(jose);
        
                try {
                    maria.alterarPerfil(Arrays.asList(Perfil.DOCENTE), jose);
                } catch (Exception e) {
                    System.out.println(RED + "Erro ao alterar perfil: " + e.getMessage() + RESET);
                }
            }
        }

    }
}
