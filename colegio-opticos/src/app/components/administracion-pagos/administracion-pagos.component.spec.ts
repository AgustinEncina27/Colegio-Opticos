import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministracionPagosComponent } from './administracion-pagos.component';

describe('AdministracionPagosComponent', () => {
  let component: AdministracionPagosComponent;
  let fixture: ComponentFixture<AdministracionPagosComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdministracionPagosComponent]
    });
    fixture = TestBed.createComponent(AdministracionPagosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
