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

## Ejecución
Para correr el proyecto localmente:
1. Clonar el repositorio.
2. Importar como proyecto Maven/Gradle o proyecto Java estándar en su IDE (IntelliJ, Eclipse, etc.).
3. Ejecutar la clase principal Main.java.
