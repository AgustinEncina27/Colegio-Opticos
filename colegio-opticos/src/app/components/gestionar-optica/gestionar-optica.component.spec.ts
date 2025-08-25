import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionarOpticaComponent } from './gestionar-optica.component';

describe('GestionarOpticaComponent', () => {
  let component: GestionarOpticaComponent;
  let fixture: ComponentFixture<GestionarOpticaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GestionarOpticaComponent]
    });
    fixture = TestBed.createComponent(GestionarOpticaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
