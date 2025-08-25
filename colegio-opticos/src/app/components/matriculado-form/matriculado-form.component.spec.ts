import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatriculadoFormComponent } from './matriculado-form.component';

describe('MatriculadoFormComponent', () => {
  let component: MatriculadoFormComponent;
  let fixture: ComponentFixture<MatriculadoFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MatriculadoFormComponent]
    });
    fixture = TestBed.createComponent(MatriculadoFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
