package Service;

import Models.Entity.Usuario;
import Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public String exportarTodosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("Id,Nome,Usuário,Email,Status,Perfis,Carga Horária Mínima\n");

        for (Usuario u : usuarios) {
            String perfis = u.getPerfis() != null
                    ? String.join(";", u.getPerfis().stream().map(Enum::name).collect(Collectors.toList()))
                    : "";

            sb.append(escape(String.valueOf(u.getID()))).append(",");
            sb.append(escape(u.getNome())).append(",");
            sb.append(escape(u.getUsuario())).append(",");
            sb.append(escape(u.getEmail())).append(",");
            sb.append(u.getStatus() != null ? u.getStatus().name() : "").append(",");
            sb.append(escape(perfis)).append(",");
            sb.append(u.getCargaHorariaMinima() != null ? u.getCargaHorariaMinima() : "").append("\n");
        }

        return sb.toString();
    }

    public void alterarCargaHorariaMinima(Long usuarioId, Integer novaCarga, Long solicitanteId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Usuario solicitante = usuarioRepository.findById(solicitanteId)
                .orElseThrow(() -> new RuntimeException("Usuário solicitante não encontrado"));

        usuario.alterarCargaHorariaMinima(novaCarga, solicitante);
        usuarioRepository.save(usuario);
    }

    // Escape de vírgulas e aspas para compatibilidade CSV
    private String escape(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
