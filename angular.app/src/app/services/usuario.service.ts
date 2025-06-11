import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
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
atualizarCargaHorariaMinima(usuarioId: number, novaCargaHorariaMinima: number, solicitanteId: number): Observable<any> {
  const url = `${this.apiUrl}/carga_horaria`;
  const body = {
    usuarioId: usuarioId,
    novaCargaHorariaMinima: novaCargaHorariaMinima,
    solicitanteID: solicitanteId
  };

  return this.http.put(url, body);
}


}
