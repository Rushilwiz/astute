import { Injectable } from '@angular/core';
import {AstuteClientService} from '../services/astute-client-service';
import {formatCurrency} from '@angular/common';

declare var html2pdf: any;
declare var html2canvas: any;
declare var jsPDF: any;
declare var $: any;

@Injectable({
  providedIn: 'root'
})
export class PrintInvoiceService {

    gridX = []; // these are the layout grid STARTING
    gridY = []; // from the 1 inch border (x: 25; y:23)

    name: string;
    email: string;
    address: string;

    poNum;
    coNum;
    inNum;
    inDate;

    inDetails; // :[{lineNum:number, desc:string, rate:string, hrs:number, amount:number}];

    ogCoAmt;
    netChanges = 0;
    totCoAmt = 0;
    prevBill;
    inAmt;
    balToBeBill;
    subTotal; // =inAmt
    milage = 0;
    otherExp = 0;
    outOf = 0;
    finTot; // = inAmt;

    notes;
    cert;

  constructor(protected astuteClientService: AstuteClientService) {
      let x = 28.5;
      let y = 25;
      for (let i = 0; i < 17; i++) { //
          this.gridX[i] = x;
          x += 10;
      }
      for (let j = 0; j < 24; j++) {
          this.gridY[j] = y;
          y += 10;
      }
  }

    printPDF(invoice: string) {
        this.astuteClientService.getInvoiceGen(invoice).then((data) => {
            this.name = data.customer.customerName;
            this.email = data.customer.email;
            this.address = data.customer.add1 + ' ' + data.customer.add2 + ' ' +
                data.customer.city + ', ' + data.customer.state.toUpperCase() + ', ' +
                data.customer.zip + '-' + data.customer.ziplast4;

            this.poNum = data.po.ponum;
            this.coNum = data.po.contractNum;
            this.inNum = data.invoice.invoiceNumber;
            this.inDate = data.invoice.invoiceDate;

            this.inDetails = data.invoiceDetail;

            this.ogCoAmt = data.po.contractAmt;
            this.netChanges = 0;
            this.totCoAmt = data.po.contractAmt;
            this.prevBill = data.po.previouslyBilledAmount;
            this.balToBeBill = data.balanceToBeBilled;
            this.finTot = data.invoice.billAmt;

            this.notes = data.invoice.specialNotes;
            this.cert = data.invoice.certification;

            this.testjsPDF(invoice);
        });
    }

    private testjsPDF(invoice: string) {
        const doc = jsPDF('p', 'mm', 'letter');
        console.log(doc.getFontList());
        // this.printGrid(doc);
        this.printHeader(doc);
        let len = this.inDetails.length;
        if (len <= 6) {
            this.printTable(doc, 7, 13, this.inDetails);
        } else if (len > 6 && len <= 28) {
            this.printTable(doc, 7, 22, this.inDetails.slice(0, 15));
            doc.addPage('letter', 'p');
            this.printTable(doc, 0, 13, this.inDetails.slice(15, 28));
        } else {
            this.printTable(doc, 7, 22, this.inDetails.slice(0, 15));
            len -= 15;
            let i = 0;
            while (len > 13) {
                doc.addPage('letter', 'p');
                this.printTable(doc, 0, 22, this.inDetails.slice(15 + (22 * i), 37 + (22 * i)));
                i++;
            }
            doc.addPage('letter', 'p');
            this.printTable(doc, 0, 13, this.inDetails.slice(37 + (22 * (i - 1))));
        }
        this.printFooter(doc);
        doc.save(invoice + '.pdf');
    }

    private printHeader (doc) {
        if (!doc) {
            doc = jsPDF('p', 'mm', 'letter');
        }
        ///////////////////// Header //////////
        // Logo and INVOICE
        doc.setFontSize(35);
        doc.text('INVOICE', this.gridX[10] + 5, this.gridY[2] - 5);
        doc.addImage('assets/img/AstuteLogo.png', 'png', this.gridX[0], this.gridY[0], 50, 22.04);

        // To, customer
        doc.setFontSize(12);
        doc.text('To,', this.gridX[0], this.gridY[3]);
        const nameSplit = doc.splitTextToSize(this.name, 65);
        nameSplit.forEach((text, i) => {
            doc.text(text, this.gridX[0] + 5, this.gridY[3] + (5 * (i + 1)));
        });
        doc.setTextColor(66, 144, 255);
        const emailSplit = doc.splitTextToSize(this.email, 65);
        emailSplit.forEach((text, i) => {
            doc.text(text, this.gridX[0] + 5, this.gridY[4] + (5 * (i + 1)));
        });
        doc.setTextColor(0);
        const addressSplit = doc.splitTextToSize(this.address, 65);
        addressSplit.forEach((text, i) => {
            doc.text(text, this.gridX[0] + 5, this.gridY[5] + (5 * (i + 1)));
        });

        // Invoice Information
        doc.text('Purchase Order #:', this.gridX[11], this.gridY[3], {'align': 'right'});
        const poNumSplit = doc.splitTextToSize(' ' + this.poNum, 50);
        poNumSplit.forEach((text, i) => {
            doc.text(text, this.gridX[11], this.gridY[3] + (5 * i));
        });

        doc.text('Contract #:', this.gridX[11], this.gridY[4], {'align': 'right'});
        const coNumSplit = doc.splitTextToSize(' ' + this.coNum, 50);
        coNumSplit.forEach((text, i) => {
            doc.text(text, this.gridX[11], this.gridY[4] + (5 * i));
        });

        doc.text('Invoice #:', this.gridX[11], this.gridY[5], {'align': 'right'});
        const inNumSplit = doc.splitTextToSize(' ' + this.inNum, 50);
        inNumSplit.forEach((text, i) => {
            doc.text(text, this.gridX[11], this.gridY[5] + (5 * i));
        });

        doc.text('Invoice Date:', this.gridX[11], this.gridY[6], {'align': 'right'});
        const inDateSplit = doc.splitTextToSize(' ' + this.inDate, 50);
        inDateSplit.forEach((text, i) => {
            doc.text(text, this.gridX[11], this.gridY[6] + (5 * i));
        });
        ///////////////////////////////////////
        if (!doc) {
            doc.autoPrint({variant: 'non-conform'});
            doc.save('grid.pdf');
        }
    }

    private printTable (doc, start, end, details) {
        if (!doc) {
            doc = jsPDF('p', 'mm', 'letter');
        }
        ///////////////////// Table ///////////
        doc.setLineWidth(.25);
        doc.setDrawColor(170);

        // Number Column (header and rows)
        doc.setFillColor(225);
        doc.rect(this.gridX[0], this.gridY[start], 10, 10, 'FD');
        doc.text('#', this.gridX[0] + 5, this.gridY[start + 1] - 3.5, {'align': 'center'});
        for (let i = start + 1; i <= end; i++) {
            doc.rect(this.gridX[0], this.gridY[i], 10, 10);
        }

        // Description Column (header and rows)
        doc.setFillColor(225);
        doc.rect(this.gridX[1], this.gridY[start], 70, 10, 'FD');
        doc.text('Description', this.gridX[4] + 5, this.gridY[start + 1] - 3.5, {'align': 'center'});
        for (let i = start + 1; i <= end; i++) {
            doc.rect(this.gridX[1], this.gridY[i], 70, 10);
        }

        // Fee Column (header and rows)
        doc.setFillColor(225);
        doc.rect(this.gridX[8], this.gridY[start], 30, 10, 'FD');
        doc.text('Fee', this.gridX[9] + 5, this.gridY[start + 1] - 3.5, {'align': 'center'});
        for (let i = start + 1; i <= end; i++) {
            doc.rect(this.gridX[8], this.gridY[i], 30, 10);
        }

        // Quantity Column (header and rows)
        doc.setFillColor(225);
        doc.rect(this.gridX[11], this.gridY[start], 20, 10, 'FD');
        doc.text('Qty.', this.gridX[12], this.gridY[start + 1] - 3.5, {'align': 'center'});
        for (let i = start + 1; i <= end; i++) {
            doc.rect(this.gridX[11], this.gridY[i], 20, 10);
        }

        // Amount Column (header and rows)
        doc.setFillColor(225);
        doc.rect(this.gridX[13], this.gridY[start], 30, 10, 'FD');
        doc.text('Amount', this.gridX[14] + 5, this.gridY[start + 1] - 3.5, {'align': 'center'});
        for (let i = start + 1; i <= end; i++) {
            doc.rect(this.gridX[13], this.gridY[i], 30, 10);
        }

        // Filling Data
        doc.setFontSize(12);
        details.forEach((inDet, i) => {
            doc.text(inDet.lineItemNum.toString(), this.gridX[0] + 5, this.gridY[start + 2 + i] - 3.5, {'align': 'center'});

            doc.setFontSize(11);
            const descSplit = doc.splitTextToSize(inDet.desc, 66);
            descSplit.forEach((text, j) => {
                doc.text(text, this.gridX[1] + 2, this.gridY[start + 1 + i] + (4 * (j + 1)));
            });

            doc.setFontSize(12);
            const fee = formatCurrency(inDet.fee, 'en-US', '$', 'USD').split('.')[0] + ((inDet.feeTypeId === 2) ? '/hr' : '');
            doc.text(fee, this.gridX[11] - 3.5, this.gridY[start + 2 + i] - 3.5, {'align': 'right'});

            doc.text(inDet.qty.toString(), this.gridX[13] - 3.5, this.gridY[start + 2 + i] - 3.5, {'align': 'right'});

            const amt = formatCurrency(inDet.fee * inDet.qty, 'en-US', '$', 'USD');
            doc.text(amt, this.gridX[16] - 3.5, this.gridY[start + 2 + i] - 3.5, {'align': 'right'});
        });
        ///////////////////////////////////////
        if (!doc) {
            doc.autoPrint({variant: 'non-conform'});
            doc.save('table.pdf');
        }
    }

    private printFooter (doc) {
        if (!doc) {
            doc = jsPDF('p', 'mm', 'letter');
        }
        ///////////////////// Footer //////////
        doc.setFontSize(12);

        // Invoice Bill Info Section
        doc.setLineWidth(.25);
        doc.setDrawColor(170);
        doc.rect(this.gridX[0], this.gridY[15], 160, 50);
        doc.text('Original Contract Amt.', this.gridX[5] - 3.5, this.gridY[16] - 3.5, {align: 'right'});
        doc.text('Net Changes by CO\'s', this.gridX[5] - 3.5, this.gridY[17] - 3.5, {align: 'right'});
        doc.text('Total Contract Amt.', this.gridX[5] - 3.5, this.gridY[18] - 3.5, {align: 'right'});
        doc.text('Previously Billed', this.gridX[5] - 3.5, this.gridY[19] - 3.5, {align: 'right'});
        doc.text('Balance to be Billed', this.gridX[5] - 3.5, this.gridY[20] - 3.5, {align: 'right'});
        for (let i = 15; i <= 19; i++) {
            doc.setFillColor(225);
            doc.rect(this.gridX[5], this.gridY[i], 30, 10, 'FD');
        }
        doc.setFont('helvetica', 'bold');
        doc.text('Total Due this Invoice', this.gridX[13] - 3.5, this.gridY[16] - 3.5, {align: 'right'});
        doc.setFont('helvetica', 'normal');
        doc.setFillColor(225);
        doc.rect(this.gridX[13], this.gridY[15], 30, 10, 'FD');

        // Fill Data
        const ogCoAmt = formatCurrency(this.ogCoAmt, 'en-US', '$', 'USD');
        doc.text(ogCoAmt, this.gridX[8] - 3.5, this.gridY[16] - 3.5, {align: 'right'});

        const netChanges = formatCurrency(this.netChanges, 'en-US', '$', 'USD');
        doc.text(netChanges, this.gridX[8] - 3.5, this.gridY[17] - 3.5, {align: 'right'});

        const totCoAmt = formatCurrency(this.totCoAmt, 'en-US', '$', 'USD');
        doc.text(totCoAmt, this.gridX[8] - 3.5, this.gridY[18] - 3.5, {align: 'right'});

        const prevBill = formatCurrency(this.prevBill, 'en-US', '$', 'USD');
        doc.text(prevBill, this.gridX[8] - 3.5, this.gridY[19] - 3.5, {align: 'right'});

        const toBeBilled = formatCurrency(this.totCoAmt - this.prevBill - this.finTot, 'en-US', '$', 'USD');
        doc.text(toBeBilled, this.gridX[8] - 3.5, this.gridY[20] - 3.5, {align: 'right'});

        const finTot = formatCurrency(this.finTot, 'en-US', '$', 'USD');
        doc.setFont('helvetica', 'bold');
        doc.text(finTot, this.gridX[16] - 3.5, this.gridY[16] - 3.5, {align: 'right'});
        doc.setFont('helvetica', 'normal');

        // Notes and Certification
        doc.text('Notes:', this.gridX[0], this.gridY[20] + 5);
        const noteSplit = doc.splitTextToSize(this.notes, 145);
        noteSplit.forEach((text, i) => {
            doc.text(text, this.gridX[1] + 5, this.gridY[20] + (5 * (i + 1)));
        });
        const certSplit = doc.splitTextToSize(this.cert, 160);
        certSplit.forEach((text, i) => {
            doc.text(text, this.gridX[0], this.gridY[22] + (5 * i));
        });
        doc.line(this.gridX[11], this.gridY[23], this.gridX[16], this.gridY[23]);
        ///////////////////////////////////////
        if (!doc) {
            doc.autoPrint({variant: 'non-conform'});
            doc.save('footer.pdf');
        }
    }

    private printGrid (doc) {
        if (!doc) {
            doc = jsPDF('p', 'mm', 'letter');
        } else {
            doc.setDrawColor(225);
        }
        this.gridX.forEach((x) => {
            doc.line(x, this.gridY[0], x, this.gridY[this.gridY.length - 1]);
            // doc.text(x.toString(), x, this.gridY[0]);
        });
        this.gridY.forEach((y) => {
            doc.line(this.gridX[0], y, this.gridX[this.gridX.length - 1], y);
            // doc.text(y.toString(), this.gridX[0], y);
        });
        doc.setDrawColor(0);
        if (!doc) {
            doc.autoPrint({variant: 'non-conform'});
            doc.save('grid.pdf');
        }
    }
}
