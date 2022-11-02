import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SoQtyFormatterComponent } from './so-qty-formatter.component';

describe('SoQtyFormatterComponent', () => {
  let component: SoQtyFormatterComponent;
  let fixture: ComponentFixture<SoQtyFormatterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SoQtyFormatterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SoQtyFormatterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
