import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { OpticaService } from 'src/app/services/optica.service';
import { LocalidadService } from 'src/app/services/localidad.service';
import { OpticaRequest } from 'src/app/models/opticaRequest';
import { LocalidadDTO } from 'src/app/models/localidadDTO';
import { MatriculadoDTO } from 'src/app/models/matriculadoDTO';
import { MatriculadoService } from 'src/app/services/matriculado.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-gestionar-optica',
  templateUrl: './gestionar-optica.component.html',
  styleUrls: ['./gestionar-optica.component.css']
})
export class GestionarOpticaComponent implements OnInit {

  formulario!: FormGroup;
  esEdicion = false;
  idOptica!: number;

  filtroLocalidad = '';
  localidades: LocalidadDTO[] = [];
  localidadesFiltradas: LocalidadDTO[] = [];

  matriculados: MatriculadoDTO[] = [];
  filtroMatriculado: string = '';

  constructor(
    private fb: FormBuilder,
    private opticaService: OpticaService,
    private localidadService: LocalidadService,
    private matriculadoService: MatriculadoService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.formulario = this.fb.group({
      nombre: ['', Validators.required],
      propietario: ['', Validators.required],
      direccion: ['', Validators.required],
      telefono: ['', Validators.required],
      habilitadaParaContactologia: [false],
      idMatriculado: [null, Validators.required],
      idLocalidad: [null, Validators.required],
      filtroLocalidad: ['']
    });
  
    // Detectar si es edición y guardar ID
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.esEdicion = true;
      this.idOptica = +id;
    }
  
    // Cargar localidades y luego la óptica
    this.cargarLocalidades(() => {
      if (this.esEdicion) {
        this.cargarOptica(this.idOptica);
      }
    });
  
    // Filtro en tiempo real
    this.formulario.get('filtroLocalidad')?.valueChanges.subscribe(valor => {
      const texto = (valor ?? '').toLowerCase();
      this.localidadesFiltradas = this.localidades.filter(l =>
        l.nombre.toLowerCase().includes(texto)
      );
    });
  }

  cargarLocalidades(callback?: () => void): void {
    this.localidadService.listar().subscribe({
      next: data => {
        this.localidades = data;
        this.localidadesFiltradas = data;
        if (callback) callback(); // ✅ Ejecuta callback si existe
      },
      error: err => console.error('Error al cargar localidades', err)
    });
  }

  // Método nuevo: buscar matriculados desde backend
  buscarMatriculados(): void {
    if (this.filtroMatriculado.trim().length === 0) {
      this.matriculados = [];
      return;
    }

    this.matriculadoService.buscarMatriculados(this.filtroMatriculado).subscribe({
      next: data => {
        this.matriculados = data;
      },
      error: err => console.error('Error al buscar matriculados', err)
    });
  }

  seleccionarMatriculado(id: number): void {
    this.formulario.patchValue({ idMatriculado: id });
  }
  
  filtrarLocalidades(): void {
    const filtro = this.filtroLocalidad.toLowerCase();
    this.localidadesFiltradas = this.localidades.filter(l =>
      l.nombre.toLowerCase().includes(filtro)
    );
  }

  seleccionarLocalidad(id: number): void {
    this.formulario.patchValue({ idLocalidad: id });
  }

  cargarOptica(id: number): void {
    this.opticaService.obtenerPorId(id).subscribe({
      next: optica => {
        this.formulario.patchValue({
          nombre: optica.nombre,
          propietario: optica.propietario,
          direccion: optica.direccion,
          telefono: optica.telefono,
          habilitadaParaContactologia: optica.habilitadaParaContactologia,
          idMatriculado: optica.idMatriculado,
          idLocalidad: optica.idLocalidad
        });
  
        // ✅ Mostrar localidad ya filtrada y seleccionada
        this.filtroLocalidad = optica.nombreLocalidad;
        this.localidadesFiltradas = this.localidades.filter(l =>
          l.nombre.toLowerCase().includes(optica.nombreLocalidad.toLowerCase())
        );
  
        // ✅ Mostrar el óptico técnico actual
        if (optica.nombreOpticoTecnico) {
          this.filtroMatriculado = `${optica.nombreOpticoTecnico} - Matrícula: ${optica.matriculaOpticoTecnico}`;
        }
      },
      error: err => console.error('Error al obtener óptica', err)
    });
  }

  guardar(): void {
    if (this.formulario.invalid) return;
  
    const datos: OpticaRequest = this.formulario.value;
  
    if (this.esEdicion) {
      this.opticaService.editar(this.idOptica, datos).subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: 'Éxito',
            text: 'Óptica editada correctamente',
            confirmButtonColor: '#759065'
          }).then(() => this.router.navigate(['/opticas']));
        },
        error: err => {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: err.error || 'Error al editar la óptica',
            confirmButtonColor: '#d33'
          });
          console.error('Error al editar', err);
        }
      });
    } else {
      this.opticaService.registrar(datos).subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: 'Éxito',
            text: 'Óptica registrada correctamente',
            confirmButtonColor: '#759065'
          }).then(() => this.router.navigate(['/opticas']));
        },
        error: err => {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: err.error || 'Error al registrar la óptica',
            confirmButtonColor: '#d33'
          });
          console.error('Error al registrar', err);
        }
      });
    }
  }

  cancelar(): void {
    this.router.navigate(['/opticas']);
  }
}
