import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatriculadoDTO } from 'src/app/models/matriculadoDTO';
import { MatriculadoService } from 'src/app/services/matriculado.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-gestion-matriculados',
  templateUrl: './gestion-matriculados.component.html',
  styleUrls: ['./gestion-matriculados.component.css']
})
export class GestionMatriculadosComponent {

  matriculados: MatriculadoDTO[] = [];
  paginaActual: number = 0;
  tamanioPagina: number = 10;
  totalItems: number = 0;
  filtro: string = '';
  mostrarForm: boolean = false;
  matriculadoSeleccionado?: MatriculadoDTO;
  matriculadosFiltrados: MatriculadoDTO[] = []; // Lista filtrada



  constructor(
    private matriculadoService: MatriculadoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarMatriculados();
  }

  cargarMatriculados(): void {
    this.matriculadoService.listarTodosPaginado(this.paginaActual, this.tamanioPagina).subscribe({
      next: data => {
        this.matriculados = data.content;
        this.matriculadosFiltrados = data.content; 
        this.totalItems = data.totalElements;
      },
      error: err => console.error('Error al cargar matriculados', err)
    });
  }

  get matriculadosPaginados(): MatriculadoDTO[] {
    return this.matriculados;
  }

  get totalPaginas(): number {
    return Math.ceil(this.totalItems / this.tamanioPagina);
  }

  get matriculadosFiltrados2(): MatriculadoDTO[] {
    return this.matriculados.filter(m =>
      m.nombre.toLowerCase().includes(this.filtro.toLowerCase()) ||
      m.dni.includes(this.filtro) ||
      m.matricula.toString().includes(this.filtro)
    );
  }

  irPagina(nuevaPagina: number): void {
    this.paginaActual = nuevaPagina;
    this.filtro.trim().length > 0 ? this.buscarMatriculados() : this.cargarMatriculados();
  }

  // ✅ Filtrar localmente sin ir al backend
  buscarMatriculados(): void {
    const texto = this.filtro.trim().toLowerCase();

    if (texto.length === 0) {
      this.matriculadosFiltrados = this.matriculados;
      return;
    }

    this.matriculadosFiltrados = this.matriculados.filter(m =>
      m.nombre.toLowerCase().includes(texto) ||
      m.dni.includes(texto) ||
      m.matricula.toString().includes(texto)
    );
  }

  darDeBaja(id: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esto dará de baja al matriculado seleccionado.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#759065',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, confirmar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.matriculadoService.darDeBaja(id).subscribe({
          next: () => {
            this.cargarMatriculados(); // ✅ Recargar lista después de baja
            Swal.fire('¡Usuario Deshabilitado!', 'El matriculado fue dado de baja.', 'success');
          },
          error: err => {
            console.error('Error al dar de baja', err);
            Swal.fire('Error', 'No se pudo dar de baja al matriculado.', 'error');
          }
        });
      }
    });
  }

  darDeAlta(id: number): void {
    Swal.fire({
      title: '¿Deseás habilitar a este matriculado?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#759065',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, habilitar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.matriculadoService.darDeAlta(id).subscribe({
          next: () => {
            this.cargarMatriculados(); // ✅ Recargar lista
            Swal.fire('¡Habilitado!', 'El matriculado fue dado de alta.', 'success');
          },
          error: err => {
            console.error('Error al dar de alta', err);
            Swal.fire('Error', 'No se pudo habilitar al matriculado.', 'error');
          }
        });
      }
    });
  }

  nuevo(): void {
    this.router.navigate(['/matriculados/nuevo']);
  }

  editar(m: MatriculadoDTO): void {
    this.router.navigate(['/matriculados/editar', m.id]);
  }

  eliminarDefinitivo(id: number): void {
    Swal.fire({
      title: '¿Eliminar definitivamente?',
      text: 'Esta acción no se puede deshacer.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.matriculadoService.eliminarDefinitivo(id).subscribe({
          next: () => {
            this.cargarMatriculados(); // ✅ Recargar lista
            Swal.fire('¡Eliminado!', 'El matriculado fue eliminado del sistema.', 'success');
          },
          error: err => {
            console.error('Error al eliminar definitivamente', err);
            Swal.fire('Error', 'No se pudo eliminar al matriculado.', 'error');
          }
        });
      }
    });
  }

  get paginasVisibles(): number[] {
    const total = this.totalPaginas;
    const actual = this.paginaActual;
    const maxVisibles = 5;
  
    let inicio = Math.max(0, actual - Math.floor(maxVisibles / 2));
    let fin = inicio + maxVisibles;
  
    if (fin > total) {
      fin = total;
      inicio = Math.max(0, fin - maxVisibles);
    }
  
    const paginas: number[] = [];
    for (let i = inicio; i < fin; i++) {
      paginas.push(i);
    }
  
    return paginas;
  }

}
