export interface MatriculadoDTO {
    id: number;
    nombre: string;
    username: string;
    matricula: number;
    dni: string;
    telefono: string;
    fechaRegistro: string;
    email: string;
    habilitado: boolean;
    cuotasImpagas: number;
    pagoAprobado: boolean;
  }