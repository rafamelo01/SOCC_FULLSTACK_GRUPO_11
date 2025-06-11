import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Usuario } from '../../model/usuario.model';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  standalone: true,
  selector: 'app-lista-usuarios',
  imports: [CommonModule, FormsModule,],
  templateUrl: './lista-usuarios.html',
  styleUrl: './lista-usuarios.css'
})
export class ListaUsuarios implements OnInit {
  usuarios: Usuario[] = [];
  filtro: string = '';
  loading: boolean = false;
  error: string | null = null;
  usuarioEditando: Usuario | null = null;
  novaCargaHoraria: number | null = null;
  
  constructor(private usuarioService: UsuarioService) {}


  ngOnInit(): void {
    this.carregarUsuarios();
  }

  // Abre o modal
  abrirDialogoCargaHoraria(usuario: Usuario): void {
    this.usuarioEditando = usuario;
    this.novaCargaHoraria = usuario.cargaHorariaMinima ?? 0;
  }

  // Fecha sem salvar
  cancelarEdicao(): void {
    this.usuarioEditando = null;
    this.novaCargaHoraria = null;
  }

  // Salva e chama a API
  confirmarEdicao(): void {
    if (this.usuarioEditando && this.novaCargaHoraria !== null) {
      const solicitanteId = 1; // Substituir futuramente com ID do usuário autenticado 

      this.usuarioService.atualizarCargaHorariaMinima(
        this.usuarioEditando.id,
        this.novaCargaHoraria,
        solicitanteId
      ).subscribe({
        next: () => {
          this.usuarioEditando!.cargaHorariaMinima = this.novaCargaHoraria;
          this.usuarioEditando = null;
          this.novaCargaHoraria = null;
          alert('Carga horária atualizada com sucesso!');
        },
        error: () => {
          alert('Erro ao atualizar carga horária.');
        }
      });
    }
  }

  carregarUsuarios(): void {
    this.loading = true;
    this.error = null;

    this.usuarioService.getUsuarios().subscribe(
      (data) => {
        this.usuarios = data;
        this.loading = false;
      },
      (err) => {
        this.error = 'Erro ao carregar os usuários. Tente novamente mais tarde.';
        this.loading = false;
      }
    );
  }
  editarCargaHoraria(usuario: Usuario): void {
    const novaCargaInput = prompt(`Informe a nova carga horária mínima para ${usuario.nome}:`, usuario.cargaHorariaMinima?.toString() || '0');
  
    const novaCarga = novaCargaInput !== null ? parseInt(novaCargaInput, 10) : null;
  
    if (novaCarga !== null && !isNaN(novaCarga)) {
      const solicitanteId = 1; // TODO: Substitua isso pelo ID real do solicitante (ex: vindo do AuthService)
  
      this.usuarioService.atualizarCargaHorariaMinima(usuario.id, novaCarga, solicitanteId)
        .subscribe({
          next: () => {
            // Atualiza localmente a carga horária
            usuario.cargaHorariaMinima = novaCarga;
            alert(`Carga horária atualizada com sucesso para ${usuario.nome}.`);
          },
          error: () => {
            alert('Erro ao atualizar a carga horária. Tente novamente.');
          }
        });
    }
  }

  

  // Filtro dinâmico com base em nome ou usuário
  get usuariosFiltrados(): Usuario[] {
    const termo = this.filtro.toLowerCase().trim();
    return this.usuarios.filter(usuario =>
      usuario.nome.toLowerCase().includes(termo) ||
      usuario.usuario.toLowerCase().includes(termo)
    );
  }
}
