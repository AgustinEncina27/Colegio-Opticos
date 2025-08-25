import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
import { InicioComponent } from './components/inicio/inicio.component';
import { NoticiasComponent } from './components/noticias/noticias.component';
import { InstitucionalComponent } from './components/institucional/institucional.component';
import { RequisitosHabilitacionComponent } from './components/requisitos-habilitacion/requisitos-habilitacion.component';
import { AdministracionPagosComponent } from './components/administracion-pagos/administracion-pagos.component';
import { GestionMatriculadosComponent } from './components/gestion-matriculados/gestion-matriculados.component';
import { MiPerfilComponent } from './components/mi-perfil/mi-perfil.component';
import { LoginComponent } from './components/user/login.component';
import { RouterModule } from '@angular/router';
import { MatriculadoFormComponent } from './components/matriculado-form/matriculado-form.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NoticiasDetalleComponent } from './components/noticias-detalle/noticias-detalle.component';
import { NoticiasFormComponent } from './components/noticias-form/noticias-form.component';
import { TokenInterceptor } from './services/interceptores/token.interceptor';
import { AuthToken } from './services/interceptores/auth.interceptor';
import { ListarOpticasComponent } from './components/listar-opticas/listar-opticas.component';
import { GestionarOpticaComponent } from './components/gestionar-optica/gestionar-optica.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    InicioComponent,
    NoticiasComponent,
    InstitucionalComponent,
    RequisitosHabilitacionComponent,
    AdministracionPagosComponent,
    GestionMatriculadosComponent,
    LoginComponent,
    MiPerfilComponent,
    MatriculadoFormComponent,
    NoticiasDetalleComponent,
    NoticiasFormComponent,
    ListarOpticasComponent,
    GestionarOpticaComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    SweetAlert2Module.forRoot(),
    AppRoutingModule,
    BrowserAnimationsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {
      provide:HTTP_INTERCEPTORS,
      useClass:AuthToken,
      multi:true
    }

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
