import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NoticiaPayload } from 'src/app/models/noticiaPayload';
import { AuthService } from 'src/app/services/auth.service';
import { NoticiaService } from 'src/app/services/noticia.service';
import Swal from 'sweetalert2';
import { URL_BACKEND } from 'src/app/config/config';

@Component({
  selector: 'app-noticias-form',
  templateUrl: './noticias-form.component.html',
  styleUrls: ['./noticias-form.component.css']
})
export class NoticiasFormComponent {
  noticiaId?: number;
  titulo = '';
  descripcion = '';
  imagenFile?: File;
  imagenNombre = '';
  modoEditar = false;
  vistaPrevia: string | ArrayBuffer | null = null;
  URL_BACKEND = URL_BACKEND;

  constructor(
    private noticiaService: NoticiaService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const paramId = this.route.snapshot.paramMap.get('id');
    this.modoEditar = this.route.snapshot.url.some(seg => seg.path === 'editar');
    
    if (paramId) {
      this.noticiaId = +paramId;
      this.noticiaService.detalle(this.noticiaId).subscribe(noticia => {
        this.titulo = noticia.titulo;
        this.descripcion = noticia.descripcion;
        this.imagenNombre = noticia.imagenUrl;
        this.vistaPrevia = `${this.URL_BACKEND}/api/noticias/uploads/${noticia.imagenUrl}`;
      });
    }
  }

  seleccionarArchivo(event: any): void {
    const archivoInput = event.target as HTMLInputElement;
    const archivo = archivoInput.files?.[0];
  
    if (!archivo) return;
  
    const limpiarTodo = () => {
      archivoInput.value = '';             // Limpiar el input
      this.imagenFile = undefined;            // Resetear la variable
      this.vistaPrevia = null;             // Quitar la vista previa
      this.imagenNombre = '';
    };
  
    // Validar tipo MIME (solo imágenes)
    const tiposPermitidos = ['image/jpeg', 'image/png', 'image/webp'];
    if (!tiposPermitidos.includes(archivo.type)) {
      Swal.fire('Formato no permitido', 'Solo se permiten imágenes JPG, PNG o WEBP.', 'error');
      limpiarTodo();
      return;
    }
  
    // Validar tamaño máximo (5MB)
    if (archivo.size > 5 * 1024 * 1024) {
      Swal.fire('Archivo muy grande', 'La imagen no debe superar los 5MB.', 'warning');
      limpiarTodo();
      return;
    }
  
    // Validar nombre de archivo (permitir letras, números, guiones, puntos, espacios)
    const nombreValido = /^[a-zA-Z0-9 _.-]+$/.test(archivo.name);
    if (!nombreValido) {
      Swal.fire(
        'Nombre de archivo inválido',
        'Usá solo letras, números, espacios, guiones o puntos.',
        'warning'
      );
      limpiarTodo();
      return;
    }
  
    this.imagenFile = archivo;
  
    const reader = new FileReader();
    reader.onload = e => this.vistaPrevia = e.target?.result ?? null;
    reader.readAsDataURL(archivo);
  }

  guardar(): void {
    const subirYGuardar = () => {
      if (this.imagenFile) {
        this.noticiaService.subirImagen(this.imagenFile).subscribe({
          next: (nombreImagen) => { // directamente el string
            this.persistirNoticia(nombreImagen);
          },
          error: () => Swal.fire('Error', 'No se pudo subir la imagen', 'error')
        });
      } else {
        this.persistirNoticia(this.imagenNombre);
      }
    };

    subirYGuardar();
  }

  private persistirNoticia(nombreImagen: string): void {
    const payload: NoticiaPayload = {
      titulo: this.titulo,
      descripcion: this.descripcion,
      imagenUrl: nombreImagen
    };

    if (!this.modoEditar) {
      payload.autorId = this.authService.user.id;
    }
    
    const mensaje = this.modoEditar ? 'actualizada' : 'creada';

    const observable = this.modoEditar && this.noticiaId
      ? this.noticiaService.editar(this.noticiaId, payload)
      : this.noticiaService.crear(payload);

    observable.subscribe(() => {
      Swal.fire('Éxito', `La noticia fue ${mensaje} correctamente`, 'success');
      this.router.navigate(['/noticias']);
    });
  }

  cancelar(): void {
    this.router.navigate(['/noticias']);
  }

  onImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    img.src = URL_BACKEND + '/api/noticias/uploads/default.jpg';
  }
}
