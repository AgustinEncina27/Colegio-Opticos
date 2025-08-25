import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OpticaService } from 'src/app/services/optica.service';
import { OpticaDTO } from 'src/app/models/opticaDTO';
import { AuthService } from 'src/app/services/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listar-opticas',
  templateUrl: './listar-opticas.component.html',
  styleUrls: ['./listar-opticas.component.css']
})
export class ListarOpticasComponent implements OnInit {

  opticas: OpticaDTO[] = [];
  totalPages = 0;
  pagina = 0;
  paginasVisibles: number[] = [];
  filtroNombre: string = '';
  

  constructor(
    private opticaService: OpticaService,
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarOpticas();
  }

  cargarOpticas(page: number = 0): void {
    this.opticaService.listar(page).subscribe({
      next: data => {
        this.opticas = data.content;
        this.totalPages = data.totalPages;
        this.pagina = page;
        this.generarPaginasVisibles();
      },
      error: err => console.error('Error al cargar ópticas', err)
    });
  }

  buscarOpticas(page: number = 0): void {
    if (this.filtroNombre.trim().length > 0) {
      this.opticaService.buscar(this.filtroNombre, page).subscribe({
        next: data => {
          this.opticas = data.content;
          this.totalPages = data.totalPages;
          this.pagina = page;
          this.generarPaginasVisibles();
        },
        error: err => console.error('Error al buscar ópticas', err)
      });
    } else {
      this.cargarOpticas(page);
    }
  }
  
  limpiarBusqueda(): void {
    this.filtroNombre = '';
    this.cargarOpticas();
  }
  
  cambiarPagina(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.buscarOpticas(page); // ✅ ahora usa la búsqueda si hay filtro
    }
  }

  private generarPaginasVisibles(): void {
    const maxVisible = 5; // cantidad de botones visibles
    let start = Math.max(this.pagina - Math.floor(maxVisible / 2), 0);
    let end = start + maxVisible;

    if (end > this.totalPages) {
      end = this.totalPages;
      start = Math.max(end - maxVisible, 0);
    }

    this.paginasVisibles = Array.from({ length: end - start }, (_, i) => start + i);
  }

  registrar(): void {
    this.router.navigate(['/opticas/nueva']);
  }

  editar(id: number): void {
    this.router.navigate(['/opticas/editar', id]);
  }

  eliminar(id: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción eliminará la óptica seleccionada.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.opticaService.eliminar(id).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Eliminada',
              text: 'La óptica fue eliminada correctamente.',
              confirmButtonColor: '#759065'
            });
            this.cargarOpticas(); // Recargar lista
          },
          error: err => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: err.error || 'No se pudo eliminar la óptica.',
              confirmButtonColor: '#d33'
            });
            console.error('Error al eliminar óptica', err);
          }
        });
      }
    });
  }

  cambiarEstadoContactologia(optica: OpticaDTO): void {
    const nuevoEstado = !optica.habilitadaParaContactologia;
    this.opticaService.cambiarEstadoContactologia(optica.id!, nuevoEstado).subscribe({
      next: () => {
        optica.habilitadaParaContactologia = nuevoEstado;
      },
      error: err => console.error('Error al cambiar estado', err)
    });
  }
}
