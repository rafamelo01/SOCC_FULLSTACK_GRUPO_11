package Controller;

import Models.Dto.AlteracaoCargaHorariaDTO;
import Models.Entity.Usuario;
import Service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/exportar")
    public void exportarUsuarios(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.csv");

        String csv = usuarioService.exportarTodosUsuarios();
        response.getWriter().write(csv);
    }

    @PutMapping("/carga_horaria")
    public ResponseEntity<String> alterarCargaHorariaMinima(@RequestBody AlteracaoCargaHorariaDTO dto) {
        try {
            usuarioService.alterarCargaHorariaMinima(dto.getUsuarioId(), dto.getNovaCargaHorariaMinima(), dto.getSolicitanteId());
            return ResponseEntity.ok("Carga Horária Mínima definida com sucesso");
        } catch (SecurityException | UnsupportedOperationException ex) {
            return ResponseEntity.status(403).body("Erro: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Erro: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    

}
