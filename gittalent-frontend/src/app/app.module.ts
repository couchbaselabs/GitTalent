import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from "@angular/router";
import { PopoverModule } from "ngx-popover";
import { ChartsModule } from 'ng2-charts/ng2-charts';
import { InfiniteScrollModule } from 'angular2-infinite-scroll';
import { appRoutes, appComponents } from "./app.routing";
import { PhonePipe } from './pipes/phonenumber';
import { UnknownPipe } from "./pipes/unknown";
import { KeysPipe } from "./pipes/keys";
import { ToCollectionPipe } from "./pipes/tocollection";
import { Utility } from './utility';
import { AppComponent } from './app.component';
import {SpinnerComponent} from './components/spinner/spinner';

@NgModule({
  declarations: [
    AppComponent,
    PhonePipe,
    UnknownPipe,
    KeysPipe,
    ToCollectionPipe,
    SpinnerComponent,
    ...appComponents
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    PopoverModule,
    ChartsModule,
    RouterModule.forRoot(appRoutes),
    InfiniteScrollModule
  ],
  providers: [Utility],
  bootstrap: [AppComponent]
})
export class AppModule { }
