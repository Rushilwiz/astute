import { TestBed, inject } from '@angular/core/testing';

import { PrintInvoiceService } from './print-invoice.service';

describe('PrintInvoiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PrintInvoiceService]
    });
  });

  it('should be created', inject([PrintInvoiceService], (service: PrintInvoiceService) => {
    expect(service).toBeTruthy();
  }));
});
