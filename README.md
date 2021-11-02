# Super Heroe API

Super Heroe API es una solucion que permite realizar operaciones basicas tales como consultar, escribir, actualizar y borrar datos relacionados a superheroes.

## Instalacion

Se puede realizar la instalacion de la aplicacion ya sea mediante el wrapper de maven o creando una imagen que luego puede ser ejecutada en containers:

- ##### Maven 
Se utiliza el wrapper de maven el cual esta versionado en los source del repositorio y se deberan seguir los siguentes pasos:
```sh 
./mvnw clean install
java -jar target/app.jar
```
- ##### Docker 
Se debe tener previamente Docker instalado en el host y ejecutar los siguientes comandos:
```sh 
docker build -t mindata-challenge .
docker run -p 8080:8080 --name superheroe-local mindata-challenge
```

## Seguridad
La API esta protegida por un token estatico el cual se envia hacia la api como un header; cabe destacar que es un nivel de seguridad basico, no obstante se debe aclarar que existen metodos de seguridad mas complejos.

> Default Token: 35115b34-9e13-4a8a-90a5-9a470607956f

Sin embargo es posible cambiar el valor del token estatico por uno de su preferencia, para hacerlo antes de iniciar el contenedor se debe agregar una variable de entorno: 

```sh 
docker run -p 8080:8080 -e API-KEY=su-valor --name superheroes-local mindata-challenge
```

## Documentacion
La api expone un swagger en la siguiente url:
[Swagger](http:localhost:8080/swagger-ui.html "Swagger")

Se detalle aca el listado de los endpoints disponibles:

> GET http://localhost:8080/superheroes

> GET http://localhost:8080/superheroes?name=man

> GET http://localhost:8080/superheroes/1

> POST http://localhost:8080/superheroes

> PUT http://localhost:8080/superheroes/1

> DELETE http://localhost:8080/superheroes/1