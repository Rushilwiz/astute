import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PhoneFormatterComponent } from './phone-formatter.component';

describe('PhoneFormatterComponent', () => {
  let component: PhoneFormatterComponent;
  let fixture: ComponentFixture<PhoneFormatterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PhoneFormatterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PhoneFormatterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
