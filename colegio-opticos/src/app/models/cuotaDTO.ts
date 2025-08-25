export interface CuotaDTO {
  id: number;
  monto: number;
  fechaVencimiento: string;
  fechaPago: string | null;
  periodo: string;
  estado: string;
  nombreMatriculado: string;
  matricula: number;
}