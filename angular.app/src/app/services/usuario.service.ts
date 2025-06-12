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
atualizarCargaHorariaMinima(usuarioId: number, novaCargaHorariaMinima: number, solicitanteId: number): Observable<any> {
  const url = `${this.apiUrl}/carga_horaria`;
  const body = {
    usuarioId: usuarioId,
    novaCargaHorariaMinima: novaCargaHorariaMinima,
    solicitanteId: solicitanteId
  };

  return this.http.put<any>(url, body, { observe: 'response' }).pipe(
    map((response: HttpResponse<any>) => {
      if (response.status !== 200) {
        throw new Error('Erro ao atualizar carga horária');
      }
      return response.body;
    }),
    catchError((err) => {
      return throwError(() => err);
    })
  );
}


}
