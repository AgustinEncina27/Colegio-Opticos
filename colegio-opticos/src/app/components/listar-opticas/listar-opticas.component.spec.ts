import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarOpticasComponent } from './listar-opticas.component';

describe('ListarOpticasComponent', () => {
  let component: ListarOpticasComponent;
  let fixture: ComponentFixture<ListarOpticasComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListarOpticasComponent]
    });
    fixture = TestBed.createComponent(ListarOpticasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
