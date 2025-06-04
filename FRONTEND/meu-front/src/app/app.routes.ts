import { Routes } from '@angular/router';

import { ListaUsuariosComponent } from './lista-usuarios/lista-usuarios.component';

export const routes: Routes = [
  {
    path: '',  // Defina o caminho que você quer usar para acessar o componente
    component: ListaUsuariosComponent
  },
  // outras rotas podem ser adicionadas aqui
];
