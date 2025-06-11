# Usa uma imagem base leve com Java 17
FROM openjdk:17-jdk-slim

# Diretório de trabalho no container
WORKDIR /app

# Copia o jar gerado para dentro do container
COPY target/*.jar app.jar

# Define o comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
