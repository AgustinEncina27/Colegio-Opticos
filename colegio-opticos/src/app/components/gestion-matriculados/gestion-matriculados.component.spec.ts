import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionMatriculadosComponent } from './gestion-matriculados.component';

describe('GestionMatriculadosComponent', () => {
  let component: GestionMatriculadosComponent;
  let fixture: ComponentFixture<GestionMatriculadosComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GestionMatriculadosComponent]
    });
    fixture = TestBed.createComponent(GestionMatriculadosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
