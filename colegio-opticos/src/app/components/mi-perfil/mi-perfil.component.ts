import { Component } from '@angular/core';
import { CuotaService } from 'src/app/services/cuota.service';
import { CuotaDTO } from '../../models/cuotaDTO'; // Ajustá el path si está en otra carpeta
import { MatriculadoDTO } from 'src/app/models/matriculadoDTO';
import { MatriculadoService } from 'src/app/services/matriculado.service';


@Component({
  selector: 'app-mi-perfil',
  templateUrl: './mi-perfil.component.html',
  styleUrls: ['./mi-perfil.component.css']
})
export class MiPerfilComponent {
  cuotas: CuotaDTO[] = [];
  totalPages: number = 0;
  currentPage: number = 0;
  matriculado?: MatriculadoDTO;
  visiblePages = 5;
  tieneCuotasPendientesOVencidas = false;
  tieneTresCuotasImpagas = false;

  constructor(private cuotaService: CuotaService,
    private matriculadoService: MatriculadoService
  ) {}

  ngOnInit(): void {
    this.loadCuotas();
    this.loadMatriculado();
  }

  loadCuotas(page: number = 0): void {
    this.cuotaService.getMisCuotas(page).subscribe(data => {
      this.cuotas = data.content;
      this.totalPages = data.totalPages;
      this.currentPage = data.number;
  
      // --- Advertencias ---
      const cuotasNoPagas = this.cuotas.filter(c =>
        c.estado === 'PENDIENTE' || c.estado === 'VENCIDA'
      );
  
      this.tieneCuotasPendientesOVencidas = cuotasNoPagas.length > 0;
      this.tieneTresCuotasImpagas = cuotasNoPagas.length >= 3;
    });
  }

  loadMatriculado(): void {
    this.matriculadoService.getPerfil().subscribe({
      next: (data) => this.matriculado = data,
      error: (err) => console.error('Error al obtener perfil', err)
    });
  }

  get paginationRange(): number[] {
    const start = Math.max(0, this.currentPage - Math.floor(this.visiblePages / 2));
    const end = Math.min(this.totalPages, start + this.visiblePages);
    return Array.from({ length: end - start }, (_, i) => start + i);
  }

  descargarComprobante(cuotaId: number): void {
    this.cuotaService.descargarComprobante(cuotaId).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `comprobante-cuota-${cuotaId}.pdf`;
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }
}
