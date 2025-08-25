import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { URL_BACKEND } from '../config/config';
import { LocalidadDTO } from '../models/localidadDTO';

@Injectable({
    providedIn: 'root'
  })
  export class LocalidadService {
  
    private apiUrl = `${URL_BACKEND}/api/localidades`;

    constructor(private http: HttpClient) {}
  
    listar(): Observable<LocalidadDTO[]> {
      return this.http.get<LocalidadDTO[]>(this.apiUrl);
    }
  
  }