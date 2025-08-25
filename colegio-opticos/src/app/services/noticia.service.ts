import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NoticiaDTO } from '../models/noticiaDTO';
import { map, Observable } from 'rxjs';
import { URL_BACKEND } from '../config/config';
import { NoticiaPayload } from '../models/noticiaPayload';


@Injectable({ providedIn: 'root' })
export class NoticiaService {
    private API_URL = `${URL_BACKEND}/api/noticias`;

  constructor(private http: HttpClient) {}

  listar(page: number = 0, size: number = 6): Observable<any> {
    return this.http.get(`${this.API_URL}?page=${page}&size=${size}`);
  }

  detalle(id: number): Observable<NoticiaDTO> {
    return this.http.get<NoticiaDTO>(`${this.API_URL}/${id}`);
  }

  crear(payload: NoticiaPayload): Observable<any> {
    return this.http.post(`${this.API_URL}`, payload);
  }

  editar(id: number, payload: NoticiaPayload): Observable<any> {
    return this.http.put(`${this.API_URL}/${id}`, payload);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  subirImagen(archivo: File): Observable<string> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    return this.http.post<{ url: string }>(`${this.API_URL}/upload`, formData).pipe(
      map(response => response.url)
    );
  }
}
