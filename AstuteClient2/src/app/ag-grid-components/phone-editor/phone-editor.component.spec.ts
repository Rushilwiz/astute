import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PhoneEditorComponent } from './phone-editor.component';

describe('PhoneEditorComponent', () => {
  let component: PhoneEditorComponent;
  let fixture: ComponentFixture<PhoneEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PhoneEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PhoneEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
