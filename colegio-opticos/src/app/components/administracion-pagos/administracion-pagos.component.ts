import { Component } from '@angular/core';
import Swal from 'sweetalert2';
import { CuotaDTO } from 'src/app/models/cuotaDTO';
import { CuotaService } from 'src/app/services/cuota.service';
import { MatriculadoDTO } from 'src/app/models/matriculadoDTO';

declare var bootstrap: any;

@Component({
  selector: 'app-administracion-pagos',
  templateUrl: './administracion-pagos.component.html',
  styleUrls: ['./administracion-pagos.component.css']
})
export class AdministracionPagosComponent {
  cuotas: CuotaDTO[] = [];
  totalItems = 0;
  paginaActual = 0;
  tamanioPagina = 10;
  filtro = '';
  cuotasSeleccionadas: number[] = [];
  mostrarMorosos = false;

  morosos: MatriculadoDTO[] = [];
  paginaMorosos = 0;
  totalMorosos = 0;
  tamanoMorosos = 5;

  constructor(private cuotaService: CuotaService) {}

  ngOnInit(): void {
    this.cargarCuotas();
  }

  verMorosos(): void {
    this.paginaMorosos = 0;
    this.mostrarMorosos = true;
    this.cargarMorosos();
  }

  cargarMorosos(): void {
    this.cuotaService.obtenerMorosos(this.paginaMorosos, this.tamanoMorosos).subscribe({
      next: data => {
        this.morosos = data.content;
        this.totalMorosos = data.totalElements;
      },
      error: err => console.error(err)
    });
  }

  irPaginaMorosos(n: number): void {
    this.paginaMorosos = n;
    this.cargarMorosos();
  }
  
  get totalPaginasMorosos(): number {
    return Math.ceil(this.totalMorosos / this.tamanoMorosos);
  }

  cargarCuotas(): void {
    this.cuotaService.obtenerCuotasAdmin(this.filtro, this.paginaActual, this.tamanioPagina).subscribe({
      next: data => {
        this.cuotas = data.content;
        this.totalItems = data.totalElements;
      },
      error: err => console.error(err)
    });
  }

  confirmarPagosSeleccionados(): void {
    Swal.fire({
      title: '¿Confirmar pagos?',
      text: `Se marcarán cuota(s) como pagadas.`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, confirmar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.cuotaService.confirmarPagos(this.cuotasSeleccionadas).subscribe({
          next: () => {
            this.cargarCuotas();
            this.cuotasSeleccionadas = [];
            Swal.fire('Confirmado', 'Cuotas marcadas como pagadas.', 'success');
          },
          error: () => Swal.fire('Error', 'No se pudieron registrar los pagos.', 'error')
        });
      }
    });
  }

  marcarComoImpaga(id: number): void {
    Swal.fire({
      title: '¿Revertir pago?',
      text: 'Esto marcará la cuota como impaga.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, revertir',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.cuotaService.revertirPago(id).subscribe({
          next: () => {
            this.cargarCuotas();
            Swal.fire('Revertido', 'La cuota fue marcada como impaga.', 'success');
          },
          error: (e) => {
            Swal.fire('Error', 'No se pudo revertir el pago.', 'error')
          }
        });
      }
    });
  }

  toggleSeleccion(id: number, event: Event): void {
    const checked = (event.target as HTMLInputElement).checked;
    if (checked) {
      this.cuotasSeleccionadas.push(id);
    } else {
      this.cuotasSeleccionadas = this.cuotasSeleccionadas.filter(x => x !== id);
    }
  }

  filtrar(): void {
    this.paginaActual = 0;
    this.cargarCuotas();
  }
  
  irPagina(n: number): void {
    this.paginaActual = n;
    this.cargarCuotas();
  }

  get totalPaginas(): number {
    return Math.ceil(this.totalItems / this.tamanioPagina);
  }
  
  get paginasVisibles(): number[] {
    const total = this.totalPaginas;
    const actual = this.paginaActual;
    const maxVisibles = 5;
  
    let inicio = Math.max(0, actual - Math.floor(maxVisibles / 2));
    let fin = Math.min(total, inicio + maxVisibles);
  
    const paginas: number[] = [];
    for (let i = inicio; i < fin; i++) {
      paginas.push(i);
    }
  
    return paginas;
  }

  limpiarFiltros(): void {
    this.filtro = '';
    this.paginaActual = 0;
    this.cargarCuotas();
  }

  get paginasVisiblesMorosos(): number[] {
    const total = this.totalPaginasMorosos;
    const actual = this.paginaMorosos;
    const maxVisibles = 5;
  
    let inicio = Math.max(0, actual - Math.floor(maxVisibles / 2));
    let fin = Math.min(total, inicio + maxVisibles);
  
    if (fin - inicio < maxVisibles) {
      inicio = Math.max(0, fin - maxVisibles);
    }
  
    const paginas: number[] = [];
    for (let i = inicio; i < fin; i++) {
      paginas.push(i);
    }
  
    return paginas;
  }
} 
