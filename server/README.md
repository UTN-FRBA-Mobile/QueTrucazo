# Levantar el ambiente local
1. Instalar NodeJS 18
2. Instalar Docker
3. Crear la base de datos en docker con `docker run --name postgres-quetrucazo -d -p 5432:5432 -e POSTGRES_PASSWORD=password postgres`
5. Ejecutar `npm i`
6. Ejecutar `npm run dev`
7. Ejecutar `./ngrok http 8080`
8. Copiar la url `https` en la app mobile como la `baseUrl` en `ApiService.kt`. Esto se debe a que las aplicaciones móviles no pueden acceder a una url `http`.
