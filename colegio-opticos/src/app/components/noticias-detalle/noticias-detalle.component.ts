import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NoticiaDTO } from 'src/app/models/noticiaDTO';
import { NoticiaService } from 'src/app/services/noticia.service';
import { URL_BACKEND } from 'src/app/config/config';

@Component({
  selector: 'app-noticias-detalle',
  templateUrl: './noticias-detalle.component.html',
  styleUrls: ['./noticias-detalle.component.css']
})
export class NoticiasDetalleComponent {
  noticia!: NoticiaDTO;
  URL_BACKEND = URL_BACKEND;

  constructor(private route: ActivatedRoute, private noticiaService: NoticiaService) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.noticiaService.detalle(id).subscribe(n => this.noticia = n);
  }

  onImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    img.src = URL_BACKEND + '/api/noticias/uploads/default.jpg';
  }
}
