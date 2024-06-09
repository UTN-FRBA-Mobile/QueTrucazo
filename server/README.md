# Levantar el ambiente local
1. Instalar NodeJS 18
2. Instalar Docker
3. Crear la base de datos en docker con `docker run --name postgres-quetrucazo -d -p 5432:5432 -e POSTGRES_PASSWORD=password postgres`
5. Ejecutar `npm i`
6. Ejecutar `npm run dev`
7. Ejecutar `./ngrok http 8080`
8. Copiar la url `https` en la app mobile como la `baseUrl` en `ApiService.kt`. Esto se debe a que las aplicaciones móviles no pueden acceder a una url `http`.

## Buildear y subir docker image
1. `doctl registry login`
2. `docker build -t quetrucazo-api:0.0.6 .`
3. `docker tag quetrucazo-api:0.0.6 registry.digitalocean.com/miutn/quetrucazo-api:0.0.6`
4. `docker push registry.digitalocean.com/miutn/quetrucazo-api:0.0.6`

## Pullear y deployar en droplet
1. `docker --config /home/deve/.docker pull registry.digitalocean.com/miutn/quetrucazo-api:0.0.6`
2. `docker run --name quetrucazo-api -d --restart=on-failure:3 -p 32025:8080 --env-file=/home/deve/quetrucazo/api/env.list registry.digitalocean.com/miutn/quetrucazo-api:0.0.6`