import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InDetQtyEditorComponent } from './in-det-qty-editor.component';

describe('InDetQtyEditorComponent', () => {
  let component: InDetQtyEditorComponent;
  let fixture: ComponentFixture<InDetQtyEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InDetQtyEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InDetQtyEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
