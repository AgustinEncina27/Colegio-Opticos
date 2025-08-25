import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NoticiasFormComponent } from './noticias-form.component';

describe('NoticiasFormComponent', () => {
  let component: NoticiasFormComponent;
  let fixture: ComponentFixture<NoticiasFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NoticiasFormComponent]
    });
    fixture = TestBed.createComponent(NoticiasFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
