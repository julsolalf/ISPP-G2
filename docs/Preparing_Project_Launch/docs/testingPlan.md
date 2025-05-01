---
title: testingPlan
groupNumber: 2
groupName: GastroStock
deliverable: Preparing Project Launch 
description: Testing plan for the GastroStock app.
author: Writing by Mario Zambrano Zapata
---

# [Preparing Project Launch] Testing Plan - GastroStock (Group 2)

# **1\. Introducción**

* **Objetivo del documento:**  
   Este plan de pruebas tiene como objetivo definir el alcance, la estrategia y el procedimiento para la realización de las pruebas en la aplicación GastroStock, garantizando que se cumplan los requisitos funcionales y no funcionales establecidos.

* **Alcance:**  
   Se cubrirán las pruebas de todos los módulos principales del sistema, incluyendo la gestión de usuarios, la lógica de negocio, la comunicación con la base de datos y cualquier interfaz de usuario o API. Se dejarán fuera aquellos componentes o integraciones externas que, por su naturaleza, requieran pruebas especializadas (por ejemplo, sistemas de terceros).

# **2\. Objetivos de las Pruebas**

* **Validación de funcionalidades:** Verificar que cada módulo y componente implementado cumpla con las especificaciones definidas.

* **Garantizar la calidad del software:** Asegurar que la aplicación es robusta, segura, escalable y que ofrece una experiencia de usuario óptima.

* **Cobertura de código:** Alcanzar una cobertura mínima del 80-90% en pruebas unitarias e integración, utilizando herramientas de análisis de cobertura.

* **Rendimiento y escalabilidad:** Evaluar la capacidad de respuesta y la estabilidad del sistema bajo condiciones de carga.

* **Seguridad:** Detectar y mitigar vulnerabilidades comunes, protegiendo los datos y asegurando el acceso controlado a la aplicación.

* **Usabilidad e interfaz:** Comprobar la consistencia y accesibilidad de la interfaz de usuario, garantizando flujos intuitivos.

# 

# 

# **3\. Estrategia de Pruebas**

La estrategia se divide en diversas categorías que aseguran la validación integral del sistema:

* ## **Pruebas Unitarias**

  **Descripción:**  
  Las pruebas unitarias verificarán el funcionamiento correcto de cada componente por separado, asegurando que cada unidad de código cumple con su propósito específico.

  **Enfoque y Metodología:**  
  Framework: JUnit 5 y Mockito para simulación de dependencias  
  Cobertura objetivo: Mínimo 85% de cobertura de código  
  Alcance: Controladores, servicios, repositorios y modelos

  **Tipos de Pruebas Unitarias:**  
  Pruebas Positivas: Verificación del comportamiento esperado con datos válidos  
  Pruebas Negativas: Verificación del comportamiento con datos inválidos o erróneos  
  Pruebas de Casos Límite: Verificación del comportamiento en valores extremos

  **Componentes a Probar:**  
  Métodos de los controladores.  
  Lógica de negocio en los servicios.  
  Acceso y manejo de datos en los repositorios.  
  **Estrategia de Automatización**  
  Integración con el ciclo de CI/CD mediante GitHub Actions  
  Ejecución automática de pruebas unitarias en cada push/pull request

* ## **Pruebas de Integración**

  **Descripción:**  
  Las pruebas de integración validarán la interacción correcta entre los diferentes módulos del sistema y asegurarán que los flujos de trabajo completos funcionan como se espera.

  **Enfoque y Metodología:**  
  Framework: Spring Boot Test, TestContainers  
  Alcance: Flujos completos end-to-end, interacciones entre capas

  **Flujos a Probar:**

  **Gestión de Pedidos:** Creación, adición de líneas, actualización de inventario y cierre.

  **Gestión de Reabastecimiento:** Creación de órdenes, registro de lotes y actualización de inventario.  
  **Gestión de Empleados:** Registro, asignación, autenticación y autorización.  
  **Gestión de Inventario:** Registro de productos, manejo de ingredientes y alertas de stock.

    
  **Estrategia de Ejecución:**  
  Uso de base de datos en memoria para pruebas (H2)  
  Configuración específica de entorno de pruebas en application-test.properties  
  Verificación de transacciones completas

* ## **Pruebas de Seguridad**

  **Descripción:**  
  Las pruebas de seguridad evaluarán la robustez del sistema frente a vulnerabilidades y ataques comunes, asegurando la protección de los datos y el acceso controlado a las funcionalidades.

  **Enfoque y Metodología:**  
  Herramientas: SQLMap, SonarQube  
  Alcance: Autenticación, autorización, protección de datos, validación de entradas

  **Tipos de Pruebas:**  
  **Pruebas de inyección** (SQL, NoSQL y comandos).  
  **Pruebas de autenticación y autorización** (fuerza bruta, escalada de privilegios, bypass).  
  **Evaluación de la protección de datos** (cifrado en tránsito y reposo).  
  **Protección contra vulnerabilidades web** (XSS, CSRF, clickjacking).

  **Estrategia de Ejecución:**  
  Análisis estático de código con SonarQube  
  Escaneo de vulnerabilidades SQL con SQLMap  
  Revisión manual de configuraciones de seguridad

* ## **Pruebas de Rendimiento y Carga**

  **Descripción:**  
  Las pruebas de rendimiento evaluarán la capacidad del sistema para responder en tiempos adecuados bajo diferentes niveles de carga, identificando cuellos de botella y áreas de optimización.

  **Enfoque y Metodología:**  
  Herramienta: Gatling  
  Alcance: Tiempos de respuesta, capacidad de procesamiento, comportamiento bajo carga

  **Tipos de Pruebas:**  
  **Pruebas de Carga:** Simulación de usuarios concurrentes (5, 10, 20, 50 usuarios) en escenarios de uso normal.  
  **Pruebas de Estrés:** Incremento gradual de usuarios hasta alcanzar el límite del sistema, evaluando la resiliencia y tiempos de recuperación.  
  **Escenarios de Prueba:**  
  Flujo de creación de pedidos con múltiples líneas  
  Gestión simultánea de pedidos por múltiples empleados  
  Actualización de inventario en tiempo real

  **Criterios de Aceptación:**  
  Tiempos de respuesta \< 2 segundos para el 95% de las peticiones  
  Capacidad para manejar 20 usuarios concurrentes sin degradación  
  Recuperación completa tras periodos de estrés

* ## **Pruebas de Interfaz de Usuario**

  **Descripción:**  
  Las pruebas de interfaz verificarán la usabilidad, accesibilidad y consistencia de la interfaz de usuario, asegurando una experiencia de usuario óptima.

  **Enfoque y Metodología**  
  Herramientas: Playwright
  Alcance: Interfaces web, comportamiento responsive, flujos de usuario

  **Tipos de Pruebas:**  
  **Pruebas Funcionales de UI:** Verificación de todos los elementos de interfaz, flujos completos de usuario y validación de formularios

    
  **Escenarios de Prueba:**  
  Registro e inicio de sesión  
  Gestión de inventario  
  Creación y gestión de pedidos  
  Administración de empleados  
  Generación de reportes

  **Estrategia de Automatización:**  
  Desarrollo de scripts de Selenium para flujos críticos  
  Grabación y ejecución de casos de prueba con Katalon Recorder  
  Integración con pipeline de CI/CD para ejecución automática

# **4\. Herramientas y Entornos de Pruebas**

**Entornos:**

* **Desarrollo:** Para pruebas iniciales y unitarias.

* **Pruebas:** Entorno controlado para integración, seguridad, rendimiento y UI.

* **Pre-producción:** (Si está disponible) para validación final en condiciones reales.

**Software y Herramientas:**

* **Pruebas Unitarias e Integración:** JUnit 5, Mockito, Spring Boot Test, TestContainers.

* **Pruebas de Rendimiento:** Gatling.

* **Pruebas de Seguridad:** SQLMap, SonarQube.

* **Pruebas UI:** Selenium WebDriver, Katalon Recorder.

**Bases de Datos:**

* **H2:** Para pruebas unitarias en entornos controlados.

* **PostgreSQL:** Para pruebas de integración y rendimiento.

**Otros Recursos:**

* Scripts para la carga de datos iniciales.

* Configuración de seguridad (credenciales de prueba, tokens JWT, etc.).

# **5\. Criterios de Inicio y Finalización de las Pruebas**

* ## Criterios de Inicio:

 	Disponibilidad de un build estable y versión mínima de cada módulo.

	Configuración del entorno de pruebas completada.

Disponibilidad de herramientas de pruebas y de integración continua configuradas.

* ## Criterios de Finalización:

  Se alcanza el objetivo de cobertura de código definido (80-90%).  
  Todos los casos de prueba críticos han sido ejecutados y aprobados.  
  Se ha reducido el número de defectos a niveles aceptables para la liberación.

# 

# **6\. Métricas**

Para evaluar la efectividad del proceso de pruebas se utilizarán las siguientes métricas:

* **Cobertura de Código:** Porcentaje de líneas y funcionalidades probadas.

* **Densidad de Defectos:** Número de defectos por cada 1000 líneas de código.

* **Tasa de Resolución:** Relación entre defectos corregidos y los identificados.

* **Tiempo Medio de Resolución:** Intervalo desde la identificación hasta la corrección de cada defecto.

* **Efectividad de Pruebas:** Comparación entre defectos encontrados en pruebas y en producción.

* **Tiempos de Respuesta:** Medición de latencia en escenarios de rendimiento, buscando cumplir la meta de \<2 segundos en el 95% de los casos.

# **7\. Resultados**

Se documentarán y presentarán los resultados obtenidos, incluyendo:

* Informes con gráficos y tablas de cobertura de pruebas.

* Registro detallado de incidencias y defectos, clasificados por severidad.

* Comparativas de tiempos de respuesta y análisis de comportamiento bajo carga.

* Resultados de las pruebas de seguridad (vulnerabilidades detectadas y medidas correctivas).

* Evaluación de la experiencia de usuario basada en pruebas de UI.

Author of the file: Mario Zambrano Zapata.
File reviewed by: Julio Solis Alfonso.