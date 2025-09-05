import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatriculadoDTO } from 'src/app/models/matriculadoDTO';
import { MatriculadoService } from 'src/app/services/matriculado.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-matriculado-form',
  templateUrl: './matriculado-form.component.html',
  styleUrls: ['./matriculado-form.component.css']
})
export class MatriculadoFormComponent {
  @Input() matriculado?: MatriculadoDTO;

  form!: FormGroup;
  esEdicion: boolean = false;
  id?: number;
  nuevaPassword: string = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private matriculadoService: MatriculadoService
  ) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.esEdicion = !!this.id;

    this.form = this.fb.group({
      nombre: ['', Validators.required],
      dni: [''],
      telefono: [''],
      email: ['', Validators.email],
      habilitado: [true],
      pagoAprobado: [false]
    });

    if (this.esEdicion && this.id) {
      this.matriculadoService.getById(this.id).subscribe({
        next: (matriculado) => {
          this.form.patchValue(matriculado);
        },
        error: () => {
          Swal.fire('Error', 'Matriculado no encontrado', 'error');
          this.router.navigate(['/matriculados']);
        }
      });
    } 
  }

  guardar(): void {
    if (this.form.invalid) return;
  
    const datos = this.form.value;
  
    if (this.esEdicion && this.id) {
      this.matriculadoService.editar(this.id, datos).subscribe({
        next: () => {
          Swal.fire({
            title: 'Matriculado editado',
            html: `El usuario fue editado con éxito`,
            icon: 'success'
          }).then(() => {
            this.router.navigate(['/gestion-matriculados']);
          });
        },
        error: (err) => {
          console.error('Error al editar', err);
          Swal.fire('Error', err.error || 'No se pudo editar el matriculado', 'error');
        }
      });
    } else {
      this.matriculadoService.registrarMatriculado(datos).subscribe({
        next: (response) => {
          Swal.fire({
            title: 'Matriculado creado',
            html: `El usuario es: <code>${response.matricula}</code><br>Contraseña generada: <code>${response.passwordPlano}</code>`,
            icon: 'success'
          }).then(() => {
            this.router.navigate(['/gestion-matriculados']);
          });
        },
        error: (err) => {
          console.error('Error al registrar', err);
          Swal.fire('Error', err.error || 'No se pudo registrar el matriculado', 'error');
        }
      });
    }
  }

  cancelar(): void {
    this.router.navigate(['/gestion-matriculados']);
  }

  regenerarPassword(): void {
    if (!this.id) return;
  
    this.matriculadoService.regenerarPassword(this.id).subscribe({
      next: (resp) => {
        this.nuevaPassword = resp.passwordPlano;
        Swal.fire({
          title: 'Nueva contraseña generada',
          html: `<code>${resp.passwordPlano}</code>`,
          icon: 'info'
        });
      },
      error: (e) => {
        console.error('Error al regenerar contraseña', e);
        Swal.fire('Error', 'No se pudo generar una nueva contraseña', 'error');
      }
    });
  }
  
  copiarAlPortapapeles(text: string): void {
    navigator.clipboard.writeText(text).then(() => {
      Swal.fire('Copiado', 'Contraseña copiada al portapapeles', 'success');
    });
  }
}
