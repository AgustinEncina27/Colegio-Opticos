import { Component } from '@angular/core';

interface Documento {
  titulo: string;
  archivo: string;
}

interface Nota {
  titulo: string;
  archivo: string;
}

@Component({
  selector: 'app-requisitos-habilitacion',
  templateUrl: './requisitos-habilitacion.component.html',
  styleUrls: ['./requisitos-habilitacion.component.css']
})
export class RequisitosHabilitacionComponent {

  documentosRequisitos: Documento[] = [
    { titulo: 'REQUISITOS PARA CAMBIO DE PROPIETARIO', archivo: 'REQUISITOS PARA CAMBIO DE PROPIETARIO.pdf' },
    { titulo: 'REQUISITOS PARA CAMBIO DE RAZON SOCIAL', archivo: 'REQUISITOS PARA CAMBIO DE RAZON SOCIAL.pdf' },
    { titulo: 'REQUISITOS PARA CAMBIO DE REGENCIA', archivo: 'REQUISITOS PARA CAMBIO DE REGENCIA.pdf' },
    { titulo: 'REQUISITOS PARA HABILITACIÓN DE INSUMOS ÓPTICOS Y EXPENDIO DE CRISTALES OFTÁLMICOS', archivo: 'REQUISITOS PARA HABILITACIÓN DE INSUMOS ÓPTICOS Y EXPENDIO DE CRISTALES OFTÁLMICOS.pdf' },
    { titulo: 'REQUISITOS PARA HABILITACIÓN DE ÓPTICA PROPIEDAD DE PERSONAS JURÍDICAS', archivo: 'REQUISITOS PARA HABILITACIÓN DE ÓPTICA PROPIEDAD DE PERSONAS JURÍDICAS.pdf' },
    { titulo: 'REQUISITOS PARA HABILITACIÓN DE ÓPTICA Y CONTACTOLOGÍA', archivo: 'REQUISITOS PARA HABILITACIÓN DE ÓPTICA Y CONTACTOLOGÍA.pdf' },
    { titulo: 'REQUISITOS PARA LA SOLICITUD DE MATRÍCULA', archivo: 'REQUISITOS PARA LA SOLICITUD DE MATRÍCULA.pdf' },
    { titulo: 'REQUISITOS PARA TALLERES DE ARMADO', archivo: 'REQUISITOS PARA TALLERES DE ARMADO.pdf' },
    { titulo: 'REQUISITOS PARA TRASLADO DE ÓPTICA', archivo: 'REQUISITOS PARA TRASLADO DE ÓPTICA.pdf' },
    { titulo: 'REQUISITOS SUSPENSION DE MATRICULA', archivo: 'REQUISITOS SUSPENSION DE MATRICULA.pdf' }
  ];

  documentosNotas: Nota[] = [
    { titulo: 'NOTA ALTA DE MATRICULA', archivo: 'NOTA ALTA DE MATRICULA.docx' },
    { titulo: 'NOTA BAJA DE MATRICULA', archivo: 'NOTA BAJA DE MATRICULA.docx' },
    { titulo: 'NOTA BAJA DE REGENCIA', archivo: 'NOTA BAJA DE REGENCIA.doc' },
    { titulo: 'NOTA BAJA ESTABLECIMIENTO', archivo: 'NOTA BAJA ESTABLECIMIENTO.docx' },
    { titulo: 'NOTA CAMBIO DE PROPIETARIO', archivo: 'NOTA CAMBIO DE PROPIETARIO.doc' },
    { titulo: 'NOTA CAMBIO DE REGENCIA', archivo: 'NOTA CAMBIO DE REGENCIA.doc' },
    { titulo: 'NOTA HABILITACION DE GABINETE DE CONTACTOLOGIA', archivo: 'NOTA HABILITACION DE GABINETE DE CONTACTOLOGIA.doc' },
    { titulo: 'NOTA HABILITACION DE LABORATORIOS Y TALLERES', archivo: 'NOTA HABILITACION DE LABORATORIOS Y TALLERES.doc' },
    { titulo: 'NOTA HABILITACION DE OPTICA', archivo: 'NOTA HABILITACION DE OPTICA.doc' },
    { titulo: 'NOTA HABILITACION VENTA DE CRISTALES E INSUMOS', archivo: 'NOTA HABILITACION VENTA DE CRISTALES E INSUMOS.doc' },
    { titulo: 'NOTA SOLICITUD DE MATRICULA', archivo: 'NOTA SOLICITUD DE MATRICULA.docx' },
    { titulo: 'NOTA TRASLADO DE OPTICA', archivo: 'NOTA TRASLADO DE OPTICA.doc' }
  ];

}
