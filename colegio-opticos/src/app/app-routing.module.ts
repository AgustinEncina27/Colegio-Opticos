import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/user/login.component';
import { InicioComponent } from './components/inicio/inicio.component';
import { NoticiasComponent } from './components/noticias/noticias.component';
import { InstitucionalComponent } from './components/institucional/institucional.component';
import { RequisitosHabilitacionComponent } from './components/requisitos-habilitacion/requisitos-habilitacion.component';
import { MiPerfilComponent } from './components/mi-perfil/mi-perfil.component';
import { AdministracionPagosComponent } from './components/administracion-pagos/administracion-pagos.component';
import { GestionMatriculadosComponent } from './components/gestion-matriculados/gestion-matriculados.component';
import { MatriculadoFormComponent } from './components/matriculado-form/matriculado-form.component';
import { NoticiasFormComponent } from './components/noticias-form/noticias-form.component';
import { NoticiasDetalleComponent } from './components/noticias-detalle/noticias-detalle.component';
import { ListarOpticasComponent } from './components/listar-opticas/listar-opticas.component';
import { GestionarOpticaComponent } from './components/gestionar-optica/gestionar-optica.component';


const routes: Routes = [
  { path: '', redirectTo: '/inicio', pathMatch: 'full' },
  { path: 'login', component: LoginComponent }, 
  { path: 'inicio', component: InicioComponent },
  { path: 'noticias', component: NoticiasComponent },
  { path: 'institucional', component: InstitucionalComponent },
  { path: 'requisitos', component: RequisitosHabilitacionComponent },
  { path: 'mi-perfil', component: MiPerfilComponent },
  { path: 'administrar-pagos', component: AdministracionPagosComponent },
  { path: 'gestion-matriculados', component: GestionMatriculadosComponent },
  { path: 'matriculados/nuevo', component: MatriculadoFormComponent },
  { path: 'matriculados/editar/:id', component: MatriculadoFormComponent },
  {path: 'noticias', component: NoticiasComponent},
  {path: 'noticias/nueva', component: NoticiasFormComponent},
  {path: 'noticias/editar/:id', component: NoticiasFormComponent},
  {path: 'noticias/:id', component: NoticiasDetalleComponent},
  { path: 'opticas', component: ListarOpticasComponent },
  { path: 'opticas/nueva', component: GestionarOpticaComponent },
  { path: 'opticas/editar/:id', component: GestionarOpticaComponent },
  { path: '**', redirectTo: '/inicio' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
