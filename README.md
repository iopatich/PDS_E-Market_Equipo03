# TPO: Plataforma de E-Commerce "E-Market" 

## Descripción del Proyecto
Este proyecto consiste en el diseño y desarrollo de una aplicación de comercio electrónico robusta, con backend desarrollado en Java y una interfaz web en React.  
El objetivo principal es aplicar conceptos avanzados de Análisis y Diseño Orientado a Objetos (ADOO), modelado mediante diagramas UML y la implementación de patrones de diseño específicos.  

## 👥 Integrantes del Grupo
+ Antonio Wu | LU: 1162753
+ Ignacio Opatich | LU: 1161666
+ Delfina Francisco Frate | LU: 1149326
+ Lucas Puente | LU:1153931
+ Valentin Papa | LU: 1197189
+ Juan Manuel Mieres | LU: 1070703

## Requisitos Funcionales
La aplicación permite gestionar el ciclo de vida de una compra en línea, incluyendo:  
+ **Gestión de Usuarios:** Registro y login diferenciado para Clientes y Administradores.
+ **Proceso de Compra:** Selección de productos, validación de stock y confirmación de pedidos.
+ **Notificaciones:** Sistema multicanal (simulado) para avisar cambios de estado vía Email, SMS o Push.
+ **Catálogo de Productos:** Organización jerárquica de categorías y productos con variantes (color, stock y precio).
+ **Gestión de Stock:** Control de inventario por variante, con validación al confirmar una compra.
+ **Carrito de Compras:** Agregado, modificación y eliminación de ítems asociados al cliente autenticado.
+ **Panel de Administración:** ABM de productos, categorías, clientes, administradores y seguimiento de pedidos.
+ **Control de Acceso:** Sistema de permisos diferenciados según el rol del usuario (Cliente / Administrador).

## Diseño y Arquitectura

### Principios Aplicados
El código ha sido desarrollado bajo estrictos estándares de calidad, priorizando:  
+ **POO:** Encapsulamiento, herencia y polimorfismo.
+ **SOLID:** Responsabilidad única, abierto/cerrado, sustitución de Liskov, segregación de interfaces e inversión de dependencias.
+ **GRASP:** Alta cohesión y bajo acoplamiento.

### Arquitectura del Sistema
La solución sigue una arquitectura **cliente-servidor** con separación en capas:

+ **Capa de Presentación:** SPA en React que consume la API REST y gestiona el estado de sesión, carrito y permisos del usuario.
+ **Capa de Aplicación:** Controladores REST (`@RestController`) que exponen los endpoints bajo el prefijo `/api/`.
+ **Capa de Dominio / Negocio:** Servicios que encapsulan la lógica de negocio, validaciones y aplicación de patrones de diseño.
+ **Capa de Persistencia:** Repositorios JPA y entidades mapeadas con Hibernate sobre MySQL.

La comunicación entre frontend y backend se realiza mediante peticiones HTTP/JSON. El entorno completo (base de datos, API y frontend) se orquesta con **Docker Compose**.

## Patrones de Diseño Implementados
Se han integrado los siguientes patrones para resolver problemas específicos de diseño:
+ **Strategy:** Utilizado para desacoplar los diferentes métodos de pago (Tarjeta, Efectivo, etc.) y la estrategia de autenticación por credenciales.
+ **Observer:** Implementado para el sistema de notificaciones automáticas ante cambios de estado en los pedidos (mediante eventos de Spring).
+ **State:** Aplicado para manejar la lógica de negocio según el estado actual del pedido (Pendiente, Pagado, Enviado, Entregado).
+ **Composite:** Aplicado en el modelado del catálogo, permitiendo tratar categorías y productos de forma uniforme mediante `ComponenteCatalogo`.
+ **Factory (Simple Factory):** Centraliza la creación de instancias de `Cliente` y `Administrador` a través de `UsuarioFactory`.

## Stack Tecnológico

| Capa | Tecnologías |
|------|-------------|
| **Backend** | Java 21, Spring Boot, Spring Data JPA, Hibernate, Maven |
| **Base de Datos** | MySQL 8.0 |
| **Frontend** | React 18, TypeScript, Vite, Tailwind CSS, Axios |
| **Infraestructura** | Docker, Docker Compose, Nginx |
| **Herramientas** | Adminer, Postman, Git |

## Entregables Incluidos
Según lo requerido por la cátedra, este repositorio contiene:  
+ Informe Técnico: Análisis funcional y casos de uso detallados. [Documento](https://docs.google.com/document/d/1McHojP1doA5cFzUtZAKn8rhIxiiaZ072LmG5VDuMhWM/edit?usp=sharing)
+ Diagramas UML:
    - Diagrama de clases principal.
    - Diagramas de secuencia para "Confirmar compra" y "Cambiar estado de pedido".
+ Código Fuente: Implementación del backend en Java y frontend en React/TypeScript, con comentarios para facilitar su comprensión.

## 🚀 Guía de Inicio Rápido para Desarrolladores

El entorno de desarrollo de este proyecto está dockerizado para evitar conflictos de instalación local. Sigue estos pasos para clonar el repositorio y levantar la Base de Datos, el Frontend, el Gestor Web y la API de Spring Boot en minutos.

### 📋 1. Requisitos Previos (Instalaciones necesarias)
Asegúrate de tener instalados los siguientes programas en tu computadora antes de comenzar:

* **[Docker Desktop](https://www.docker.com/products/docker-desktop/)**: Fundamental para levantar los contenedores del proyecto sin necesidad de instalar MySQL localmente.
* **[Postman](https://www.postman.com/downloads/)**: Lo utilizaremos para realizar las peticiones HTTP y probar la API REST.
* **[Git](https://git-scm.com/downloads)**: Para clonar el repositorio.
* **[Intellij](https://www.jetbrains.com/es-es/idea/download/?section=windows)**: Si deseas codear o debugear el backend por fuera de Docker, necesitarás Java 21 y un entorno como IntelliJ IDEA.

### 📥 2. Clonar el Repositorio
Abre tu terminal en la carpeta donde quieras clonar y ejecuta:
```bash
git clone https://github.com/iopatich/PDS_E-Market_Equipo03.git
cd PDS_E-Market_Equipo03
```

### ⚙️ 3. Configuración de Variables de Entorno (`.env`)
Para facilitar el trabajo en equipo y agilizar el desarrollo de este TPO, el archivo `.env` ya se encuentra incluido en el repositorio con todas las credenciales locales preconfiguradas. No necesitas renombrar ni configurar nada; Docker y Spring Boot lo leerán automáticamente. **Asegurarse que este como ".env" y no como "env", lo mismo con ".dockerfile" y no "dockerfile"**

### 🐳 4. Levantar el Entorno con Docker
Abre la aplicación **Docker Desktop** y espera a que inicie correctamente (el ícono debe estar en verde o decir "Engine running"). Luego, en la terminal ubicada en la raíz del proyecto, ejecuta el siguiente comando:

`docker-compose up --build`

Este comando leerá el `Dockerfile` para construir la imagen de Spring Boot (descargando las dependencias y compilando el código) y levantará MySQL, Adminer y el frontend en paralelo. La primera vez puede tardar unos minutos. Cuando veas el logo de Spring y el mensaje indicando que la aplicación arrancó en la terminal, la API estará lista.*

### 🧪 5. Explorar y Probar el Sistema

Una vez que los contenedores estén corriendo en verde, tendrás acceso a las siguientes herramientas:

#### A. Ver la Base de Datos (Adminer)
No necesitas instalar DBeaver ni MySQL Workbench. El proyecto incluye un gestor web ligero para la base de datos. Para verificar cómo Hibernate generó tus tablas automáticamente:
1. Ingresa en tu navegador a: 👉 [http://localhost:8081](http://localhost:8081)
2. Inicia sesión con los siguientes datos:
   * **Sistema:** MySQL
   * **Servidor:** mysql
   * **Usuario:** root
   * **Contraseña:** rootsecret
   * **Base de datos:** emarket

#### B. Probar la API (Postman)
Abre Postman y apunta tus peticiones a la dirección base: `http://localhost:8080/api/`

**Ejemplos de flujo para probar el Catálogo Composite:**
1. **POST** `/categorias` (Crea la categoría padre, ej: "Living").
2. **POST** `/categorias` (Crea una subcategoría enviando el `idCategoriaPadre` obtenido en el paso 1).
3. **POST** `/productos` (Crea un producto asociado a la subcategoría enviando el `idCategoriaPadre`).
4. **POST** `/variantesproducto` (Crea una variante de color y stock para el producto).
5. **PUT** `/variantesproducto/reducirstock/{id}?cantidad=2` (Prueba la lógica de negocio simulando una compra que descuenta stock).
6. **DELETE** `/productos/{id}` (Comprueba la baja lógica; el producto ya no aparecerá en los `GET` pero si lo revisas en Adminer, verás que sigue en la base de datos con `activo = false`).

#### C. Acceder a la Interfaz Web (Frontend)
La aplicación web estará disponible en:

👉 [http://localhost:3000](http://localhost:3000)

Desde allí se puede navegar el catálogo, registrarse, iniciar sesión, gestionar el carrito, finalizar compras y —en el caso de administradores— acceder al panel de gestión en `/admin`.

**Credenciales del administrador inicial** (creado automáticamente al primer arranque):

| Campo | Valor |
|-------|-------|
| Usuario | `admin` |
| Contraseña | `admin123` |

Ingresá en [http://localhost:3000/ingresar](http://localhost:3000/ingresar) y luego accedé a **Administración → Administradores** para dar de alta otros administradores desde el panel.

#### D. Módulos de la API REST

| Módulo | Ruta base | Descripción |
|--------|-----------|-------------|
| Autenticación | `/api/auth` | Registro, login y gestión de sesión |
| Clientes | `/api/clientes` | ABM de clientes |
| Administradores | `/api/administradores` | ABM de administradores |
| Categorías | `/api/categorias` | Gestión del árbol de categorías (Composite) |
| Productos | `/api/productos` | CRUD con baja lógica |
| Variantes | `/api/variantesproducto` | Stock, color y precio por producto |
| Carrito | `/api/carrito` | Operaciones del carrito y confirmación de compra |
| Pedidos | `/api/pedidos` | Consulta y avance de estados del pedido |

> Las peticiones que requieren autenticación deben incluir el token de sesión en el header `Authorization`.

## Estructura del Proyecto

```
PDS_E-Market_Equipo03/
├── src/main/java/com/emarket/     # Backend Spring Boot
│   ├── controller/                # Endpoints REST
│   ├── service/                   # Lógica de negocio
│   ├── repository/                # Acceso a datos (JPA)
│   ├── entity/                    # Modelo de dominio / entidades
│   ├── dto/                       # Objetos de transferencia
│   ├── mapper/                    # Conversión Entity ↔ DTO
│   ├── pedido/                    # Patrón State
│   ├── pago/                      # Patrón Strategy (métodos de pago)
│   ├── notificacion/              # Patrón Observer (eventos de pedido)
│   ├── factory/                   # Patrón Factory (usuarios)
│   └── config/                    # Configuración y carga inicial (catálogo y admin)
├── frontend/                      # SPA React + TypeScript
│   └── src/
│       ├── pages/                 # Vistas (cliente y admin)
│       ├── components/            # Componentes reutilizables
│       ├── services/              # Consumo de la API
│       └── core/                  # Auth, carrito, patrones y utilidades
├── docker-compose.yml
├── Dockerfile
└── .env
```

## Carga Inicial del Catálogo

Al iniciar el backend por primera vez, se ejecuta automáticamente una carga de datos de ejemplo (categorías, productos y variantes) para facilitar las pruebas del sistema. Este comportamiento se controla mediante las variables de entorno:

+ `EMARKET_CATALOGO_CARGAR_INICIAL`: habilita o deshabilita la carga automática.
+ `EMARKET_CATALOGO_FORZAR_RECARGA`: fuerza la recarga del catálogo, eliminando los datos previos.

## Administrador Inicial

Si no existe ningún administrador activo en la base de datos, el backend crea uno automáticamente al arrancar. Las credenciales por defecto son:

+ **Usuario:** `admin`
+ **Contraseña:** `admin123`

Se pueden personalizar con estas variables (en `.env` o en el servicio `backend` de Docker Compose):

+ `EMARKET_ADMIN_CARGAR_INICIAL`: habilita o deshabilita la creación automática (default: `true`).
+ `EMARKET_ADMIN_USERNAME`: nombre de usuario del administrador inicial (default: `admin`).
+ `EMARKET_ADMIN_PASSWORD`: contraseña del administrador inicial (default: `admin123`).

Una vez dentro del panel de administración, en **Administradores** podés crear nuevos administradores con el botón **Nuevo administrador**. Solo un administrador autenticado puede registrar otros administradores cuando ya existe al menos uno en el sistema.

## Estados del Pedido

El ciclo de vida de un pedido se gestiona mediante el patrón **State**, contemplando las siguientes transiciones:

```
PENDIENTE → PAGADO → ENVIADO → ENTREGADO
```

Cada cambio de estado valida las reglas de negocio correspondientes y dispara notificaciones simuladas (Email, SMS, Push) hacia el cliente.

## 🌿 Flujo de Trabajo y Manejo de Ramas (Git Workflow)

Para mantener el código ordenado y evitar conflictos al trabajar en equipo, nos manejaremos bajo las siguientes reglas estrictas:

🚨 **REGLA DE ORO: NUNCA se debe trabajar ni hacer commits directamente sobre la rama `main`.** La rama `main` es sagrada y solo contendrá el código 100% estable para las entregas a los profesores.

Nuestra rama principal de integración diaria será **`develop`**.

### 🛠️ ¿Cómo trabajar en una nueva funcionalidad? (Paso a paso)
La mejor práctica para no pisarnos el código es que cada uno cree una rama nueva (feature branch) a partir de `develop` para hacer su tarea, y luego la fusione (merge) cuando esté terminada.

**1. Actualiza tu entorno local**
Antes de empezar a programar, asegúrate de estar en `develop` y tener la última versión:
```bash
git checkout develop
git pull origin develop
```
**2. Crea tu rama de trabajo**
Crea una rama con un nombre descriptivo tuyo para que la identifiques
```
git checkout -b branch/{nombre}
```

**3. Trabaja y haz tus commits**
Escribe tu código, prueba que funcione y guarda los cambios usando los Conventional Commits:
```
git add .
git commit -m "feat: agregar validación de login para clientes"
```

**4. Fusiona tu trabajo a la rama común (Merge)**
Una vez que tu tarea está terminada y probada, es hora de enviarla a la rama develop para compartirla con el equipo:
```
# Vuelve a la rama develop
git checkout develop

# Trae posibles cambios que hayan subido tus compañeros mientras programabas
git pull

# Fusiona tu rama hacia develop
git merge {nombre de tu rama}

# Sube todo el código actualizado a GitHub
git push origin develop

# Volver a tu rama
git checkout {nombre de tu rama}

```

**5. Traer cambios a tu rama**
En caso de que tus compañeros hayan subido cambios en la rama `develop` y los quieras traer a tu rama, hacer lo siguiente:
```
# Vuelve a la rama develop
git checkout develop

# Trae posibles cambios que hayan subido tus compañeros mientras programabas
git pull

# Volver a tu rama
git checkout {nombre de tu rama}

# Fusiona los cambios que hubieron en la rama develop
git pull origin develop
```
