import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { CustomerComponent } from './customer/customer.component';
import { AstuteClientService } from './services/astute-client-service';
import { AgGridModule } from 'ag-grid-angular';
import { ModalFormComponent } from './modal-form/modal-form.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { SalesOrderComponent } from './sales-order/sales-order.component';
import { InvoiceComponent } from './invoice/invoice.component';
import { HomepageComponent } from './homepage/homepage.component';
import { AppBoxComponent } from './app-box/app-box.component';
import { InvoiceGenComponent } from './invoice-gen/invoice-gen.component';
import { TextMaskModule } from 'angular2-text-mask';
import { LoginComponent } from './login/login.component';
import { InvoicePaymentComponent } from './invoice-payment/invoice-payment.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { SettingsComponent } from './settings/settings.component';
import { ToastsContainerComponent } from './services/toast-manager/toasts-container/toasts-container.component';
import { NumberFormatterComponent } from './ag-grid-components/number-formatter/number-formatter.component';
import { PhoneFormatterComponent } from './ag-grid-components/phone-formatter/phone-formatter.component';
import { PhoneEditorComponent } from './ag-grid-components/phone-editor/phone-editor.component';
import { EmptyErrorEditorComponent } from './ag-grid-components/empty-error-editor/empty-error-editor.component';
import { NumericEditorComponent } from './ag-grid-components/numeric-editor/numeric-editor.component';
import { SoQtyEditorComponent } from './ag-grid-components/so-qty-editor/so-qty-editor.component';
import { InDetQtyEditorComponent } from './ag-grid-components/in-det-qty-editor/in-det-qty-editor.component';
import { SoQtyFormatterComponent } from './ag-grid-components/so-qty-formatter/so-qty-formatter.component';
// import { ServiceTypeComponent } from './service-type/service-type.component';

@NgModule({
  declarations: [
    AppComponent,
    CustomerComponent,
    ModalFormComponent,
    NavBarComponent,
    SalesOrderComponent,
    InvoiceComponent,
    HomepageComponent,
    AppBoxComponent,
    InvoiceGenComponent,
    LoginComponent,
    InvoicePaymentComponent,
    SettingsComponent,
    ToastsContainerComponent,
    NumberFormatterComponent,
    PhoneFormatterComponent,
    PhoneEditorComponent,
    EmptyErrorEditorComponent,
    NumericEditorComponent,
    SoQtyEditorComponent,
    InDetQtyEditorComponent,
    SoQtyFormatterComponent// ,
    // ServiceTypeComponent
  ],
  imports: [
    BrowserModule,
    AgGridModule.withComponents([
        NumberFormatterComponent,
        PhoneFormatterComponent,
        PhoneEditorComponent,
        EmptyErrorEditorComponent,
        NumericEditorComponent,
        SoQtyEditorComponent,
        InDetQtyEditorComponent,
        SoQtyFormatterComponent
    ]),
    NgbModule,
    HttpClientModule,
    AppRoutingModule,
    TextMaskModule,
    CommonModule,
    FormsModule,
      ReactiveFormsModule
  ],
  providers: [AstuteClientService],
  bootstrap: [AppComponent]
})
export class AppModule { }
