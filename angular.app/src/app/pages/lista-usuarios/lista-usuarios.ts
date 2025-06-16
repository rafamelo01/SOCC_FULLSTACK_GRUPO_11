import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Usuario } from '../../model/usuario.model';
import { UsuarioService } from '../../services/usuario.service';
import { NotificationService } from '../../services/notification.service';

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
  solicitanteId: number | null = null; // ID do perfil selecionado
  showPerfilDialog = false; // controla modal seleção perfil
  isSidebarCollapsed = true; // controla o estado da sidebar

  constructor(private usuarioService: UsuarioService, private notificationService: NotificationService) {}


  ngOnInit(): void {
    this.showPerfilDialog = true; // abre modal de escolha

  }

  toggleSidebar() {
  this.isSidebarCollapsed = !this.isSidebarCollapsed;
  const mainContent = document.getElementById('mainContent');

  if (this.isSidebarCollapsed) {
    mainContent?.classList.remove('collapsed');
  } else {
    mainContent?.classList.add('collapsed');
  }
}

// Chamado quando o usuário escolhe o perfil
selecionarPerfil(id: number): void {
  this.solicitanteId = id;
  this.showPerfilDialog = false;

    // Agora pode carregar usuários, pois perfil selecionado
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
    if (this.usuarioEditando && this.novaCargaHoraria !== null && this.solicitanteId !== null) {
      this.usuarioService.atualizarCargaHorariaMinima(
        this.usuarioEditando.id,
        this.novaCargaHoraria,
        this.solicitanteId
      ).subscribe({
        next: () => {
          this.usuarioEditando!.cargaHorariaMinima = this.novaCargaHoraria;
          this.usuarioEditando = null;
          this.novaCargaHoraria = null;
          this.notificationService.show('Carga horária atualizada com sucesso!', 'success');
        },
        error: (err) => {
          console.error('Erro:', err);
          const mensagem = err?.message || 'Erro ao atualizar carga horária.';
          this.notificationService.show(mensagem, 'error');
          this.fecharDialogo();
        }
      });
    }
  }

  carregarUsuarios(): void {
    if (this.solicitanteId === null) return; // não carrega antes de escolher

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

    if (novaCarga !== null && !isNaN(novaCarga) && this.solicitanteId !== null) {
      this.usuarioService.atualizarCargaHorariaMinima(usuario.id, novaCarga, this.solicitanteId)
        .subscribe({
          next: () => {
            usuario.cargaHorariaMinima = novaCarga;
            this.notificationService.show(`Carga horária atualizada com sucesso para ${usuario.nome}.`, "success");
          },
          error: () => {
            this.notificationService.show("Erro ao atualizar a carga horária. Tente novamente.", 'error');
          }
        });
    }
  }


  exportarCSV(): void {
  this.usuarioService.exportarUsuariosCSV().subscribe({
    next: (blob) => {
      // Cria um link temporário para download
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'usuarios.csv';
      a.click();
      window.URL.revokeObjectURL(url);
    },
    error: (err) => {
      console.error('Erro ao exportar CSV:', err);
      this.notificationService.show('Erro ao exportar CSV. Tente novamente.', "error");
    }
  });
}


  // Filtro dinâmico com base em nome ou usuário
  get usuariosFiltrados(): Usuario[] {
    const termo = this.filtro.toLowerCase().trim();
    return this.usuarios.filter(usuario =>
      usuario.nome.toLowerCase().includes(termo) ||
      usuario.usuario.toLowerCase().includes(termo)
    );
  }
  // função utilitária para fechar o modal
private fecharDialogo(): void {
  this.usuarioEditando = null;
  this.novaCargaHoraria = null;
}

}
