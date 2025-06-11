import { Routes } from '@angular/router';
import { ListaUsuarios } from './pages/lista-usuarios/lista-usuarios';

export const routes: Routes = [
  {
    path: '',  // Defina o caminho que você quer usar para acessar o componente
    component: ListaUsuarios
  },
  // outras rotas podem ser adicionadas aqui
];
