export interface Usuario {
  id: number;
  nome: string;
  email: string;
  usuario: string;
  perfis: string[];
  status: 'ATIVO' | 'INATIVO';
  cargaHorariaMinima: number | null;
  diretorOuVice: boolean;
}