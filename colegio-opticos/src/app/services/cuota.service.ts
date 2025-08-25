import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { URL_BACKEND } from '../config/config'; 
import { CuotaDTO } from '../models/cuotaDTO';
import { Page } from '../models/page';
import { MatriculadoDTO } from '../models/matriculadoDTO';


@Injectable({
  providedIn: 'root'
})
export class CuotaService {
  private baseUrl = `${URL_BACKEND}/api/cuotas`; // Ajustá si tu backend tiene prefijo

  constructor(private http: HttpClient) {}

  getMisCuotas(page: number = 0, size: number = 5): Observable<Page<CuotaDTO>> {
    return this.http.get<Page<CuotaDTO>>(`${this.baseUrl}/mis-cuotas?page=${page}&size=${size}`);
  }

  obtenerTodas(): Observable<Page<CuotaDTO>> {
    return this.http.get<Page<CuotaDTO>>(`${this.baseUrl}/mis-cuotas?page=0&size=1000`);
  }

  confirmarPagos(ids: number[]) {
    return this.http.patch(`${this.baseUrl}/pagar-multiple`, ids);
  }

  revertirPago(id: number) {
    return this.http.patch(`${this.baseUrl}/${id}/revertir-pago`,{});
  }

  obtenerMorosos(page: number = 0, size: number = 5): Observable<Page<MatriculadoDTO>> {
    return this.http.get<Page<MatriculadoDTO>>(`${this.baseUrl}/morosos?page=${page}&size=${size}`);
  }

  obtenerCuotasAdmin(filtro: string, page: number, size: number) {
    return this.http.get<Page<CuotaDTO>>(
      `${this.baseUrl}/pagos-filtrados?filtro=${filtro}&page=${page}&size=${size}`
    );
  }

  descargarComprobante(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${id}/comprobante`, {
      responseType: 'blob'
    });
  }
}