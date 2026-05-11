# TPO: Plataforma de E-Commerce "E-Market" 

## Descripción del Proyecto
Este proyecto consiste en el diseño y desarrollo de una aplicación de comercio electrónico robusta, desarrollada íntegramente en Java. 
El objetivo principal es aplicar conceptos avanzados de Análisis y Diseño Orientado a Objetos (ADOO), modelado mediante diagramas UML y la implementación de patrones de diseño específicos.  

##👥 Integrantes del Grupo
+ Antonio Wu
+ Ignacio Opatich
+ Delfina Francisco Frate
+ Lucas
+ -
+ -
+ -

## Requisitos Funcionales
La aplicación permite gestionar el ciclo de vida de una compra en línea, incluyendo:  
+ **Gestión de Usuarios:** Registro y login diferenciado para Clientes y Administradores.
+ **Proceso de Compra:** Selección de productos, validación de stock y confirmación de pedidos.
+ **Notificaciones:** Sistema multicanal (simulado) para avisar cambios de estado vía Email, SMS o Push.  

## Diseño y ArquitecturaPrincipios Aplicados
El código ha sido desarrollado bajo estrictos estándares de calidad, priorizando:  
+ **POO:** Encapsulamiento, herencia y polimorfismo.
+ **SOLID:** Responsabilidad única, abierto/cerrado, sustitución de Liskov, segregación de interfaces e inversión de dependencias.
+ **GRASP:** Alta cohesión y bajo acoplamiento.

## Patrones de Diseño Implementados
Se han integrado los siguientes patrones para resolver problemas específicos de diseño:
+ **Strategy:** Utilizado para desacoplar los diferentes métodos de pago (Tarjeta, Efectivo, etc.).
+ **Observer:** Implementado para el sistema de notificaciones automáticas ante cambios de estado en los pedidos.
+ **State:** Aplicado para manejar la lógica de negocio según el estado actual del pedido (Pendiente, Enviado, Cancelado).

## Entregables Incluidos
Según lo requerido por la cátedra, este repositorio contiene:  
+ Informe Técnico: Análisis funcional y casos de uso detallados. [Documento](https://docs.google.com/document/d/1McHojP1doA5cFzUtZAKn8rhIxiiaZ072LmG5VDuMhWM/edit?usp=sharing)
+ Diagramas UML:
    - Diagrama de clases principal.
    - Diagramas de secuencia para "Confirmar compra" y "Cambiar estado de pedido".
+ Código Fuente: Implementación completa en Java con comentarios para facilitar su comprensión. 

## Guía de Inicio Rápido para Desarrolladores

Para levantar este proyecto localmente, por favor sigue estos pasos cuidadosamente. Si intentas correr la aplicación de inmediato, se detendrá al no encontrar las credenciales de la base de datos.

### 1. Requisitos Previos (Instalaciones necesarias)
* **Java JDK 21:** El proyecto está configurado para trabajar con Java 21. Asegúrate de tener esta versión instalada y seleccionada como SDK en IntelliJ.
* **Motor MySQL:** Debes tener MySQL corriendo localmente en tu computadora (puedes usar XAMPP o Docker Desktop).
* *Nota sobre Maven:* **NO necesitas instalar Maven.** El proyecto incluye el archivo `mvnw` (Maven Wrapper), el cual se encargará de descargar y utilizar la versión correcta automáticamente de forma transparente.

### 2. Configuración Local (`application.properties`)
Una vez que abras el proyecto, IntelliJ comenzará a descargar las dependencias automáticamente. Mientras esto ocurre, debes configurar la conexión a tu base de datos local.

Navega hasta el archivo `src/main/resources/application.properties` y agrega la siguiente configuración, reemplazando el usuario y contraseña con tus credenciales locales de MySQL:

```properties
# Nombre de la aplicación
spring.application.name=emarket

# Puerto del servidor
server.port=8080

# Conexión a la base de datos MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/emarket_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración de Hibernate (Mapeo de objetos a tablas)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
