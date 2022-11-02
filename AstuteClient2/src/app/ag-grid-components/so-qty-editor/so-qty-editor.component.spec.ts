import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SoQtyEditorComponent } from './so-qty-editor.component';

describe('SoQtyEditorComponent', () => {
  let component: SoQtyEditorComponent;
  let fixture: ComponentFixture<SoQtyEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SoQtyEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SoQtyEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
