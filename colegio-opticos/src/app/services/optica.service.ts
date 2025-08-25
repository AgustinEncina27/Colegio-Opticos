import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { URL_BACKEND } from '../config/config';
import { OpticaDTO } from '../models/opticaDTO';
import { OpticaRequest } from '../models/opticaRequest';

@Injectable({
    providedIn: 'root'
  })
  export class OpticaService {
  
    private apiUrl = `${URL_BACKEND}/api/opticas`;
  
    constructor(private http: HttpClient) {}
  
    listar(page: number = 0, size: number = 15): Observable<any> {
      return this.http.get<any>(`${this.apiUrl}/listarPaginado?page=${page}&size=${size}`);
    }

    buscar(nombre: string, page: number = 0): Observable<any> {
      return this.http.get<any>(`${this.apiUrl}/buscar`, {
        params: { nombre, page }
      });
    }
  
    registrar(optica: OpticaRequest): Observable<OpticaDTO> {
      return this.http.post<OpticaDTO>(this.apiUrl, optica);
    }
  
    editar(id: number, optica: OpticaRequest): Observable<OpticaDTO> {
      return this.http.put<OpticaDTO>(`${this.apiUrl}/${id}`, optica);
    }
  
    cambiarEstadoContactologia(id: number, habilitada: boolean): Observable<void> {
      return this.http.patch<void>(`${this.apiUrl}/${id}/contactologia?habilitada=${habilitada}`, {});
    }

    obtenerPorId(id: number): Observable<OpticaDTO> {
      return this.http.get<OpticaDTO>(`${this.apiUrl}/${id}`);
    }

    eliminar(id: number): Observable<void> {
      return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
  }