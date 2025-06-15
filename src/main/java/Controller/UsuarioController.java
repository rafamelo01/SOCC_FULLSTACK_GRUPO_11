package Controller;

import Models.Dto.AlteracaoCargaHorariaDTO;
import Models.Dto.ErrorResponse;
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
import org.springframework.http.MediaType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/exportar")
    public void exportarUsuarios(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.csv");

        // Pega OutputStream e escreve com OutputStreamWriter UTF-8
        try (OutputStream os = response.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(osw)) {

            // Escrever BOM UTF-8 (opcional, mas recomendado para Excel)
            writer.write('\uFEFF');

            // Gera o CSV
            String csv = usuarioService.exportarTodosUsuarios();

            // Se quiser trocar vírgula por ponto e vírgula para Excel PT-BR:
            csv = csv.replace(",", ";");

            // Escreve CSV
            writer.write(csv);
        }
    }


    @PutMapping(value = "/carga_horaria", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> alterarCargaHorariaMinima(@RequestBody AlteracaoCargaHorariaDTO dto) {
        try {
            usuarioService.alterarCargaHorariaMinima(dto.getUsuarioId(), dto.getNovaCargaHorariaMinima(), dto.getSolicitanteId());
            return ResponseEntity.ok().body(Map.of("mensagem", "Carga Horária Mínima definida com sucesso"));
        } catch (SecurityException | UnsupportedOperationException ex) {
            return ResponseEntity.status(403).body(new ErrorResponse("Erro: " + ex.getMessage()));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Erro: " + ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    

}
