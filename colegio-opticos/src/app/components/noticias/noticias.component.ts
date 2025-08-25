import { Component } from '@angular/core';
import { NoticiaDTO } from 'src/app/models/noticiaDTO';
import { AuthService } from 'src/app/services/auth.service';
import { NoticiaService } from 'src/app/services/noticia.service';
import Swal from 'sweetalert2';
import { URL_BACKEND } from 'src/app/config/config';

@Component({
  selector: 'app-noticias',
  templateUrl: './noticias.component.html',
  styleUrls: ['./noticias.component.css']
})
export class NoticiasComponent {
  noticias: NoticiaDTO[] = [];
  pagina = 0;
  totalPages = 0;
  paginasVisibles: number[] = [];
  URL_BACKEND = URL_BACKEND;


  constructor(private noticiaService: NoticiaService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.cargarNoticias();
  }

  cargarNoticias(): void {
    this.noticiaService.listar(this.pagina).subscribe((data: any) => {
      this.noticias = data.content;
      this.totalPages = data.totalPages;
      this.generarPaginasVisibles();
    });
  }

  generarPaginasVisibles(): void {
    const total = this.totalPages;
    const current = this.pagina;
  
    const start = Math.max(0, current - 2);
    const end = Math.min(total, start + 5);
  
    this.paginasVisibles = [];
  
    for (let i = start; i < end; i++) {
      this.paginasVisibles.push(i);
    }
  }

  cambiarPagina(nueva: number): void {
    this.pagina = nueva;
    this.cargarNoticias();
  }

  eliminar(id: number): void {
    Swal.fire({
      title: '¿Eliminar noticia?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.noticiaService.eliminar(id).subscribe(() => this.cargarNoticias());
      }
    });
  }

  onImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    img.src = URL_BACKEND + '/api/noticias/uploads/default.jpg';
  }
}
