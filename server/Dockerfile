FROM node:20-alpine
WORKDIR /usr
COPY package.json ./
COPY tsconfig.json ./
COPY src ./src
RUN ls -a
RUN npm install
RUN npm run build-simple
## this is stage two , where the app actually runs
FROM node:20-alpine
WORKDIR /usr
COPY package.json ./
## next package.json is for version
COPY package.json ../
RUN npm install --only=production
COPY --from=0 /usr/build .
RUN npm install pm2 -g
EXPOSE 8080
CMD ["npm","run","docker:start"]