# Gastrostock

## Requisitos previos
Antes de comenzar, asegúrate de tener instalados los siguientes componentes:

1. **JDK 21**: [Descargar JDK 21](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)
2. **MariaDB Server 11.7.2 Rolling**: [Descargar MariaDB](https://mariadb.org/download/)

## Configuración del entorno

### Instalación de JDK 21
1. Descarga e instala JDK 21 desde el enlace proporcionado.
2. Configura la variable de entorno `JAVA_HOME` apuntando al directorio de instalación de JDK 21.
3. Añade el directorio `bin` de JDK 21 a la variable de entorno `PATH`.

### Instalación de MariaDB
1. Descarga e instala MariaDB desde el enlace proporcionado.
2. Inicia HeidiSQL con  el usuario creado al principio:
3. Crea una base de datos llamada `gastrostockdb`:
4. Crea un usuario con nombre `root` y contraseña `root`:
5. Dale permisos para gestionar dica base de datos:
5. Crea una conexión con dicho usuario en el socket `localhost:3306` .

### Ejecución del entorno
1. Descarga el proyecto desde el repositorio de GitHub.
2. Abre el proyecto.
3. Para instalar las dependencias de Maven, ejecuta el comando `.\mvnw clean install`.
4. Para ejecutar la aplicación, ejecuta el comando `.\mvnw spring-boot:run`.
