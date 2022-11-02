import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NumberFormatterComponent } from './number-formatter.component';

describe('NumberFormatterComponent', () => {
  let component: NumberFormatterComponent;
  let fixture: ComponentFixture<NumberFormatterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NumberFormatterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NumberFormatterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
