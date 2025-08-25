import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequisitosHabilitacionComponent } from './requisitos-habilitacion.component';

describe('RequisitosHabilitacionComponent', () => {
  let component: RequisitosHabilitacionComponent;
  let fixture: ComponentFixture<RequisitosHabilitacionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RequisitosHabilitacionComponent]
    });
    fixture = TestBed.createComponent(RequisitosHabilitacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
