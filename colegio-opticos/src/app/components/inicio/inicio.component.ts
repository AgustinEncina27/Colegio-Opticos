import { Component } from '@angular/core';

declare var bootstrap: any;


@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.css']
})
export class InicioComponent {


  // inicio.component.ts (opcional)
  ngAfterViewInit(): void {
    const carousel = document.querySelector('#patrocinadoresCarousel');
    if (carousel) {
      new bootstrap.Carousel(carousel, {
        interval: 3000,
        ride: 'carousel'
      });
    }
  }
}
