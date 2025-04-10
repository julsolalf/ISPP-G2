# **Plan de Pruebas GastroStock**

# **1\. Introducción**

* **Objetivo del documento:**  
   Este plan de pruebas tiene como objetivo definir el alcance, la estrategia y el procedimiento para la realización de las pruebas en la aplicación GastroStock, garantizando que se cumplan los requisitos funcionales y no funcionales establecidos.

* **Alcance:**  
   Se cubrirán las pruebas de todos los módulos principales del sistema, incluyendo la gestión de usuarios, la lógica de negocio, la comunicación con la base de datos y cualquier interfaz de usuario o API. Se dejarán fuera aquellos componentes o integraciones externas que, por su naturaleza, requieran pruebas especializadas (por ejemplo, sistemas de terceros).

# **2\. Objetivos de las Pruebas**

* **Validar el correcto funcionamiento de la aplicación:**  
   Verificar que todas las funcionalidades implementadas cumplen con las especificaciones y requisitos definidos en nuestro MVP.

* **Garantizar la calidad del software:**  
   Asegurar que el sistema es robusto, seguro, escalable y que la experiencia del usuario sea óptima.

* **Alcanzar un objetivo de cobertura de código:**  
   Se fijará como meta alcanzar una cobertura mínima del 80-90% en las pruebas unitarias y de integración, utilizando herramientas de análisis de cobertura.

# **3\. Estrategia de Pruebas**

* ## Pruebas Unitarias

  Descripción:  
  Las pruebas unitarias verificarán el funcionamiento correcto de cada componente por separado, asegurando que cada unidad de código cumple con su propósito específico.

  Enfoque y Metodología:  
  Framework: JUnit 5 y Mockito para simulación de dependencias  
  Cobertura objetivo: Mínimo 85% de cobertura de código  
  Alcance: Controladores, servicios, repositorios y modelos

  Tipos de Pruebas Unitarias:  
  Pruebas Positivas: Verificación del comportamiento esperado con datos válidos  
  Pruebas Negativas: Verificación del comportamiento con datos inválidos o erróneos  
  Pruebas de Casos Límite: Verificación del comportamiento en valores extremos

  Componentes a Probar:  
  \- Implementación de todos los métodos de cada controlador

  Servicios:  
  \- Implementación de todos los servicios correspondientes a cada controlador

  Repositorios:  
  \- Implementación de acceso a datos para cada entidad

  Estrategia de Automatización  
  \- Integración con el ciclo de CI/CD mediante GitHub Actions  
  \- Ejecución automática de pruebas unitarias en cada push/pull request

* ## Pruebas de Integración

  Descripción:  
  Las pruebas de integración validarán la interacción correcta entre los diferentes módulos del sistema y asegurarán que los flujos de trabajo completos funcionan como se espera.

  Enfoque y Metodología:  
  Framework: Spring Boot Test, TestContainers  
  Alcance: Flujos completos end-to-end, interacciones entre capas

  Flujos a Probar:  
  Gestión de Pedidos:  
  \- Creación de pedido  
  \- Adición de líneas de pedido  
  \- Actualización de inventario  
  \- Cierre de pedido

  Gestión de Reabastecimiento:  
  \- Creación de orden de reabastecimiento  
  \- Registro de lotes  
  \- Actualización de inventario

  Gestión de Empleados:  
  \- Registro de empleado  
  \- Asignación a negocio  
  \- Autenticación y autorización

  Gestión de Inventario:  
  \- Registro de productos  
  \- Gestión de ingredientes  
  \- Alertas de stock

  Estrategia de Ejecución:  
  \- Uso de base de datos en memoria para pruebas (H2)  
  \- Configuración específica de entorno de pruebas en application-test.properties  
  \- Verificación de transacciones completas

* ## Pruebas de Seguridad

  Descripción:  
  Las pruebas de seguridad evaluarán la robustez del sistema frente a vulnerabilidades y ataques comunes, asegurando la protección de los datos y el acceso controlado a las funcionalidades.

  Enfoque y Metodología:  
  Herramientas: SQLMap, SonarQube  
  Alcance: Autenticación, autorización, protección de datos, validación de entradas

  Tipos de Pruebas:  
  Pruebas de Inyección:  
  \- Inyección SQL en formularios y parámetros  
  \- Inyección NoSQL  
  \- Inyección de comandos

  Pruebas de Autenticación y Autorización:  
  \- Fuerza bruta de credenciales  
  \- Escalada de privilegios  
  \- Bypass de autenticación

  Pruebas de Protección de Datos:  
  \- Exposición de información sensible  
  \- Cifrado de datos en tránsito y en reposo

  Protección contra vulnerabilidades web:  
  \- Cross-Site Scripting (XSS)  
  \- Cross-Site Request Forgery (CSRF)  
  \- Clickjacking

  Estrategia de Ejecución:  
  \- Análisis estático de código con SonarQube  
  \- Escaneo de vulnerabilidades SQL con SQLMap  
  \- Revisión manual de configuraciones de seguridad

* ## Pruebas de Rendimiento y Carga

  Descripción:  
  Las pruebas de rendimiento evaluarán la capacidad del sistema para responder en tiempos adecuados bajo diferentes niveles de carga, identificando cuellos de botella y áreas de optimización.

  Enfoque y Metodología:  
  Herramienta: Gatling  
  Alcance: Tiempos de respuesta, capacidad de procesamiento, comportamiento bajo carga

  Tipos de Pruebas:  
  Pruebas de Carga:  
  \- Simulación de usuarios concurrentes (5, 10, 20, 50 usuarios)  
  \- Uso normal del sistema  
  \- Medición de tiempos de respuesta y throughput

  Pruebas de Estrés:  
  \- Incremento gradual de usuarios hasta punto de fallo  
  \- Determinación de límites del sistema  
  \- Evaluación de recuperación

  Escenarios de Prueba:  
  \- Flujo de creación de pedidos con múltiples líneas  
  \- Gestión simultánea de pedidos por múltiples empleados  
  \- Actualización de inventario en tiempo real

  Criterios de Aceptación:  
  \- Tiempos de respuesta \< 2 segundos para el 95% de las peticiones  
  \- Capacidad para manejar 20 usuarios concurrentes sin degradación  
  \- Recuperación completa tras periodos de estrés

* ## Pruebas de Interfaz de Usuario

  Descripción:  
  Las pruebas de interfaz verificarán la usabilidad, accesibilidad y consistencia de la interfaz de usuario, asegurando una experiencia de usuario óptima.

  Enfoque y Metodología  
  Herramientas: Selenium WebDriver, Katalon Recorder  
  Alcance: Interfaces web, comportamiento responsive, flujos de usuario

  Tipos de Pruebas:  
  Pruebas Funcionales de UI:  
  \- Verificación de todos los elementos de interfaz  
  \- Flujos completos de usuario  
  \- Validación de formularios

  Escenarios de Prueba:  
  \- Registro e inicio de sesión  
  \- Gestión de inventario  
  \- Creación y gestión de pedidos  
  \- Administración de empleados  
  \- Generación de reportes

  Estrategia de Automatización:  
  \- Desarrollo de scripts de Selenium para flujos críticos  
  \- Grabación y ejecución de casos de prueba con Katalon Recorder  
  \- Integración con pipeline de CI/CD para ejecución automática

# **4\. Herramientas y Entornos de Pruebas**

* ## Recursos Software

  Entornos:  
  \- Desarrollo  
  \- Pruebas  
  \- Pre-producción

  Software de Pruebas:  
  \- JUnit 5 y Mockito para pruebas unitarias  
  \- Testcontainers para pruebas de integración  
  \- Gatling para pruebas de rendimiento  
  \- SQLMap para pruebas de seguridad  
  \- Selenium WebDriver y Katalon Recorder para pruebas de UI

  Bases de Datos:  
  \- H2 para pruebas unitarias  
  \- PostgreSQL para pruebas de integración y rendimiento

* Configuración de Base de Datos:

  Configuración de Seguridad:  
  \- Credenciales de prueba específicas  
  \- Tokens JWT de prueba

  Datos de Prueba:  
  \- Conjunto de datos iniciales para cada entidad  
  \- Scripts de carga de datos para diferentes escenarios

# **5\. Criterios de Inicio y Finalización de las Pruebas**

* ## Criterios de Inicio:

 	– Disponibilidad de un build estable y versión mínima de cada módulo.

	– Configuración del entorno de pruebas completada.

– Disponibilidad de herramientas de pruebas y de integración continua configuradas.

* ## Criterios de Finalización:

  – Se alcanza el objetivo de cobertura de código definido (80-90%).

  – Todos los casos de prueba críticos han sido ejecutados y aprobados.

  – Se ha reducido el número de defectos a niveles aceptables para la liberación.

# **6\. Métricas**

– Cobertura de código: Porcentaje de líneas probadas.  
– Densidad de defectos: Defectos por 1000 líneas de código.  
– Tasa de resolución: Defectos resueltos vs. identificados.  
– Tiempo medio de resolución: Tiempo desde registro hasta cierre.  
– Efectividad de pruebas: Defectos encontrados vs. reportados en producción.

