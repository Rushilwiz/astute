import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {CustomerComponent} from './customer/customer.component';
import {SalesOrderComponent} from './sales-order/sales-order.component';
import {InvoiceComponent} from './invoice/invoice.component';
import {HomepageComponent} from './homepage/homepage.component';
import {InvoiceGenComponent} from './invoice-gen/invoice-gen.component';
import {InvoicePaymentComponent} from './invoice-payment/invoice-payment.component';
import {LoginComponent} from './login/login.component';
import {SettingsComponent} from './settings/settings.component';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full'},
  { path: 'home', redirectTo: 'homepage', pathMatch: 'full'},
  { path: 'homepage', component: HomepageComponent },
  { path: 'customer', component: CustomerComponent },
  { path: 'sales-order', component: SalesOrderComponent },
  { path: 'invoice', component: InvoiceComponent },
  { path: 'invoice-gen', component: InvoiceGenComponent },
  { path: 'invoice-payment', component: InvoicePaymentComponent },
  // { path: 'service-type', component: ServiceTypeComponent},
  { path: 'login', component: LoginComponent },
  { path: 'settings', component: SettingsComponent }
];

@NgModule({
  exports: [RouterModule],
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  declarations: []
})
export class AppRoutingModule { }
