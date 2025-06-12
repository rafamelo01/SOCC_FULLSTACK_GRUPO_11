# Etapa 1: Build da aplicação Angular
FROM node:20-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build --prod

# Etapa 2: Servindo com Nginx
FROM nginx:alpine

# Copia o build do Angular para o diretório padrão do Nginx
COPY --from=build /app/dist/angular.app/browser /usr/share/nginx/html

# Copia configuração customizada do Nginx, se existir
# COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]