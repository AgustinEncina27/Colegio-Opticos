import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public authService: AuthService,
    private router: Router
    ){}

  ngOnInit(): void {
  }

  longOut():void{
    Swal.fire('SE HA CERRADO SESIÓN',`Hola ${this.authService.user.nombre},has cerrado sesión con éxito!`,'success')
    this.authService.logOut();
    this.router.navigate(['/login']);
  }
}
