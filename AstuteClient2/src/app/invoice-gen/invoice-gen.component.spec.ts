import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InvoiceGenComponent } from './invoice-gen.component';

describe('InvoiceGenComponent', () => {
  let component: InvoiceGenComponent;
  let fixture: ComponentFixture<InvoiceGenComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InvoiceGenComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InvoiceGenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
