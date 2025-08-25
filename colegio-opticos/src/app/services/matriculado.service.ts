import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { URL_BACKEND } from '../config/config';
import { MatriculadoDTO } from '../models/matriculadoDTO';
import { MatriculadoRegistroResponse } from '../models/matriculadoRegistroResponse';
import { Page } from '../models/page';


@Injectable({
  providedIn: 'root'
})
export class MatriculadoService {
  private baseUrl = `${URL_BACKEND}/api/matriculados`;

  constructor(private http: HttpClient) {}

  getPerfil(): Observable<MatriculadoDTO> {
    return this.http.get<MatriculadoDTO>(`${this.baseUrl}/mi-perfil`);
  }

  getById(id: number): Observable<MatriculadoDTO> {
    return this.http.get<MatriculadoDTO>(`${this.baseUrl}/${id}`);
  }

  buscar(query: string, pagina: number, tamanio: number) {
    return this.http.get<Page<MatriculadoDTO>>(
      `${this.baseUrl}/buscar?query=${query}&page=${pagina}&size=${tamanio}`
    );
  }  
  
  // Buscar matriculados según texto
  buscarMatriculados(filtro: string): Observable<MatriculadoDTO[]> {
    return this.http.get<MatriculadoDTO[]>(`${this.baseUrl}/buscarMatriculados?filtro=${filtro}`);
  }

  registrarMatriculado(data: any): Observable<MatriculadoRegistroResponse> {
    return this.http.post<MatriculadoRegistroResponse>(`${this.baseUrl}/registrar`, data);
  }

  regenerarPassword(id: number): Observable<MatriculadoRegistroResponse> {
    return this.http.put<MatriculadoRegistroResponse>(`${this.baseUrl}/${id}/regenerar-password`, {});
  }

  listarTodosPaginado(pagina: number, tamanio: number) {
    return this.http.get<Page<MatriculadoDTO>>(`${this.baseUrl}/listar?page=${pagina}&size=${tamanio}`);
  }

  editar(id: number, data: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, data);
  }

  darDeBaja(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  darDeAlta(id: number): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${id}/alta`, {});
  }

  eliminarDefinitivo(id: number) {
    return this.http.delete<void>(`${this.baseUrl}/${id}/eliminar-definitivo`);
  }


}
