# Damas Chinas

<img align="right" width="200" src="https://user-images.githubusercontent.com/5889006/111317939-e1518600-8664-11eb-861f-1aafaf6f8495.png">

[![Java CI with Maven](https://github.com/UCM-FDI-DISIA/2021-is2-dg-scichat/actions/workflows/maven.yml/badge.svg)](https://github.com/UCM-FDI-DISIA/2021-is2-dg-scichat/actions/workflows/maven.yml)

Vamos a desarrollar un juego de [damas chinas](https://es.wikipedia.org/wiki/Damas_chinas) multijugador online como proyecto de Ingeniería de Software II.

El proyecto se va a desarrollar utilizando como lenguaje de programación Java 8. Para la parte de interfaz gráfica, se utilizará [Swing](https://es.wikipedia.org/wiki/Swing_(biblioteca_gr%C3%A1fica)). 

## Configurar el entorno de desarrollo

Para empezar a contribuir en este proyecto, primero debes hacer un clone de este repositorio en tu ordenador:

```bash
git clone https://github.com/UCM-FDI-DISIA/2021-is2-dg-scichat
```

Luego, **abrirlo en tu entorno de desarrollo**. En este caso vamos a hacer un ejemplo con Eclipse.

Antes de poder ejecutarlo, necesitas **descargar las dependencias de Maven**. Para ello:

![](https://user-images.githubusercontent.com/5889006/111280682-683e3880-863c-11eb-9f23-1440776748c5.png)

Primero haces click derecho sobre el proyecto -> Maven -> Update Project, y luego pulsa `download sources`.

Una vez hecho eso, dirígete al archivo `src/main/java/Main.java`, hacer click derecho sobre él, `Run As -> Run As Java Application`

## Normas de contribución

Antes de realizar un commit, **se debe garantizar que los cambios que se ha hecho no vulnera ningún test**. Una vez subido el commit al repositorio, GitHub Actions comprobará de forma automática los casos de prueba. Pero conviene haber revisado antes de subirlo.

En el caso de la creación de una nueva clase, conviene crear casos de prueba correspondiente utilizando [JUnit 5](https://junit.org/junit5/). Echar un vistazo a la [página de wiki](https://github.com/UCM-FDI-DISIA/2021-is2-dg-scichat/wiki/Integraci%C3%B3n-con-Maven) correspondeinte.

## Estilos de código

* Indent de 4 espacios
* Nombrar atributos y métodos en inglés, con camelcase

IDE recomendado para el proyecto: IntelliJ IDEA o Eclipse
