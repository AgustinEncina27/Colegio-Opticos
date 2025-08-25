import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  titulo: String='Iniciar Sesión';
  user: User ;

  constructor(private authService: AuthService,
    private router:Router ){
    this.user= new User();
  }
  
  ngOnInit(){
    if(this.authService.isAuthenticated()){
      Swal.fire('LOGIN', `Hola ${this.authService.user.nombre}, Ya estas autenticado`,'info');
      this.router.navigate(['/inicio']);
    }
  }

  login(){
    if(this.user.username==null||this.user.password==null){
      Swal.fire('EERROR LOGIN','El usuario y la contraseña estan vacios!','error');
      return;
    }

    this.authService.login(this.user).subscribe(
      response=>{
        this.authService.saveToken(response.token);
        this.authService.saveUser(response.token);   

        let user= this.authService.user;
        this.router.navigate(['/inicio']);
        Swal.fire('LOGIN',`Hola ${user.nombre},has iniciado sesión correctamente`,'success');
      },error=>{
        console.log('ERROR', error);  // 👈 esto también
        if(error.status==400 || error.status==401){
          Swal.fire('ERROR LOGIN','el usuario y contraseña son incorrectos','error');
        }
      }
    )
  }
}
