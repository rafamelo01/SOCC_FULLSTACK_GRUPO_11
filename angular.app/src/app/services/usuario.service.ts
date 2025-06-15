import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, catchError, map, throwError } from 'rxjs';
import { Usuario } from '../model/usuario.model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  private apiUrl = environment.apiUrl + '/usuarios';

  constructor(private http: HttpClient) {}

  getUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiUrl);
  }
  // Atualiza a carga horária mínima de um usuário
atualizarCargaHorariaMinima(usuarioId: number, novaCargaHorariaMinima: number, solicitanteId: number): Observable<string> {
  const url = `${this.apiUrl}/carga_horaria`;
  const body = { usuarioId, novaCargaHorariaMinima, solicitanteId };

  return this.http.put<{ mensagem: string }>(url, body).pipe(
    map(response => response.mensagem),
    catchError(err => {
      // Tenta extrair a mensagem específica da resposta JSON
      let msg = 'Erro desconhecido';
      if (err.error && typeof err.error === 'object' && err.error.mensagem) {
        msg = err.error.mensagem;
      } else if (err.message) {
        msg = err.message;
      }
      return throwError(() => new Error(msg));
    })
  );
}

// Retorna o CSV como blob (binário)
exportarUsuariosCSV(): Observable<Blob> {
  const url = `${this.apiUrl}/exportar`;
  return this.http.get(url, { responseType: 'blob' });
}



}
