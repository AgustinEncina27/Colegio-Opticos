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
  matriculados: MatriculadoDTO[] = [];              // lista paginada desde backend
  matriculadosFiltrados: MatriculadoDTO[] = [];     // resultados de /buscarMatriculados
  paginaActual: number = 0;
  tamanioPagina: number = 10;
  totalItems: number = 0;

  filtro = '';
  filtroAplicado: string | null = null;  // null => sin filtro aplicado
  // texto de búsqueda
  loading = false;

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
        this.totalItems = data.totalElements;
      },
      error: err => console.error('Error al cargar matriculados', err)
    });
  }

  // 👇 usar este getter en la tabla
  get listaRender(): MatriculadoDTO[] {
    return this.tieneFiltroActivo ? this.matriculadosFiltrados : this.matriculados;
  }

  get totalPaginas(): number {
    return Math.ceil(this.totalItems / this.tamanioPagina);
  }

  get tieneFiltroActivo(): boolean {
    return this.filtroAplicado !== null;
  }

  irPagina(nuevaPagina: number): void {
    if (this.tieneFiltroActivo) return; // con filtro, no paginamos
    this.paginaActual = nuevaPagina;
    this.cargarMatriculados();
  }

  /** 🔎 Buscar SOLO al hacer clic */
  buscarMatriculados(): void {
    const texto = this.filtro.trim();
    if (!texto) { this.limpiarFiltro(); return; }

    this.loading = true;
    this.matriculadoService.buscarMatriculados(texto).subscribe({
      next: (resp) => {
        this.matriculadosFiltrados = resp;
        this.filtroAplicado = texto;   // ← ahora el filtro queda “aplicado”
        this.loading = false;
      },
      error: (e) => {
        console.error(e);
        this.matriculadosFiltrados = [];
        this.filtroAplicado = texto;   // filtro aplicado, pero sin resultados
        this.loading = false;
      }
    });
  }

  /** 🧹 Limpiar filtro y volver a la lista paginada */
  limpiarFiltro(): void {
    this.filtro = '';
    this.filtroAplicado = null;
    this.matriculadosFiltrados = [];
    this.paginaActual = 0;
    this.cargarMatriculados();
  }

  // ---- acciones existentes ----
  nuevo(): void { this.router.navigate(['/matriculados/nuevo']); }

  editar(m: MatriculadoDTO): void { this.router.navigate(['/matriculados/editar', m.id]); }

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
          next: () => { this.cargarMatriculados(); Swal.fire('¡Usuario Deshabilitado!', 'El matriculado fue dado de baja.', 'success'); },
          error: err => { console.error('Error al dar de baja', err); Swal.fire('Error', 'No se pudo dar de baja al matriculado.', 'error'); }
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
          next: () => { this.cargarMatriculados(); Swal.fire('¡Habilitado!', 'El matriculado fue dado de alta.', 'success'); },
          error: err => { console.error('Error al dar de alta', err); Swal.fire('Error', 'No se pudo habilitar al matriculado.', 'error'); }
        });
      }
    });
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
          next: () => { this.cargarMatriculados(); Swal.fire('¡Eliminado!', 'El matriculado fue eliminado del sistema.', 'success'); },
          error: err => { console.error('Error al eliminar definitivamente', err); Swal.fire('Error', 'No se pudo eliminar al matriculado.', 'error'); }
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
    for (let i = inicio; i < fin; i++) paginas.push(i);
    return paginas;
  }
}
