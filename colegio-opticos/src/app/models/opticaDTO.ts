export interface OpticaDTO {
  id: number;
  nombre: string;
  propietario: string;
  direccion: string;
  telefono: string;
  habilitadaParaContactologia: boolean;

  idLocalidad: number;
  nombreLocalidad: string;
  codigoPostal: string;

  idMatriculado: number;
  nombreOpticoTecnico: string;
  matriculaOpticoTecnico: number;
}