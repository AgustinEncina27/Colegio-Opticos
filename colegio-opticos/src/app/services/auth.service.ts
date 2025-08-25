import { HttpClient} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user'; 
import { URL_BACKEND } from '../config/config'; 

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private _user : User;
  private _token : string;

  constructor(private http: HttpClient) {
    this._user = new User();
    this._token = '';
  }

  public get user(): User {
    if (this._user != null) {
      return this._user;
    } else if (this._user == null && sessionStorage.getItem('user') != null) {
      const userJSON = sessionStorage.getItem('user');
      if (userJSON !== null) {
        this._user = JSON.parse(userJSON) as User;
        return this._user;
      }
    }
    return new User();
  }

  public get token(): any {
    if (this._token != null) {
      return this._token;
    } else if (this._token == null && sessionStorage.getItem('token') != null) {
      this._token = sessionStorage.getItem('token') ?? '';
      return this._token;
    }
    return null;
  }

  login(user: User): Observable<any> {
    return this.http.post(`${URL_BACKEND}/api/auth/login`, user);
  }

  saveUser(accessToken: string): void {
    const payload = this.getDataToken(accessToken);
    this._user = new User();
    this._user.id = payload.id;              
    this._user.username = payload.sub;
    this._user.nombre = payload.nombre;
    this._user.rol = payload.rol;
    sessionStorage.setItem('user', JSON.stringify(this._user));
  }

  saveToken(accessToken: string): void {
    this._token = accessToken;
    sessionStorage.setItem('token', accessToken);
  }

  getDataToken(accessToken: string): any {
    if (accessToken != null && accessToken.trim() !== '') {
      const payloadBase64 = accessToken.split(".")[1];
      const payloadJson = decodeURIComponent(
        Array.prototype.map.call(atob(payloadBase64), (c: string) => {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join('')
      );
      return JSON.parse(payloadJson);
    }
    return null;
  }

  isAuthenticated(): boolean {
    const payload = this.getDataToken(this.token);
    return payload != null && payload.nombre && payload.nombre.length > 0;
  }

  logOut(): void {
    this._token = '';
    this._user = new User();
    sessionStorage.clear();
  }

  hasRole(role: string): boolean {
    return this.user.rol === role;
  }

  isTokenExpirado(): boolean {
    const payload = this.getDataToken(this.token);
    const now = new Date().getTime() / 1000;
    return payload.exp < now;
  }
}
