import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmptyErrorEditorComponent } from './empty-error-editor.component';

describe('EmptyErrorEditorComponent', () => {
  let component: EmptyErrorEditorComponent;
  let fixture: ComponentFixture<EmptyErrorEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmptyErrorEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmptyErrorEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
