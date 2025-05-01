# Informe de Lecciones Aprendidas - Entrega S1

> **Grupo**: 2 
> **Entrega evaluada**: S1 
> **Fecha del informe**: 22/04/2025  
> **Versión del informe**: 1.0  

---

## 1. Resumen del fallo de la entrega

La entrega se considera fallida debido a dos factores principales. En primer lugar, no conseguimos desplegar la aplicación, lo que afectó directamente al cumplimiento de los objetivos del sprint. Este retraso en el despliegue fue consecuencia de una falta de organización en el equipo de FullStack, que no logró coordinarse adecuadamente para llevar a cabo las tareas necesarias dentro del tiempo establecido. Como resultado, no se pudo entregar ni una versión funcional de la aplicación ni en el entorno de producción ni en un entorno de pruebas. Además, hubo un cambio en la dirección del proyecto a pocos días de la entrega: originalmente, el proyecto consistía en una aplicación para gestionar el inventario de bares con integración en TPVs, pero se transformó en una aplicación TPV que también incluyera funciones de gestión de inventario. Este cambio de enfoque generó incertidumbre y desorganización en el equipo, lo que impidió cumplir con los objetivos marcados para este sprint. Estos factores combinados provocaron que no se pudiera entregar una versión funcional de la aplicación, lo que afectó la calidad final del entregable.

Además, uno de los miembros del equipo no asistió a la sesión de evaluación y no notificó su ausencia ni a los profesores ni al resto del equipo. Esta falta de comunicación y compromiso complicó aún más la situación, ya que, de haberse aprobado la entrega, la ausencia de este miembro habría resultado en un suspenso para todo el equipo debido a su falta de implicación.


---

## 2. Metodología y organización del equipo

| Miembro del equipo                      | Rol principal               | Responsabilidades en la iteración |
|----------------------------------------|-----------------------------|----------------------------------|
| Raúl Toro Romero                       | Project Manager             | Resolución de dudas a los distintos departamentos, organización de tareas por semana y realización de documentación |
| Alexis Molins López                    | Analista / RRPP / Despliegue| Realización de presentaciones y preparación de estas, y despliegue al final del sprint |
| Jesús Salas Muñoz                      | Analista / RRPP / Despliegue| Realización de presentaciones y preparación de estas, y despliegue al final del sprint |
| Alonso Portillo Sánchez                | Analista / RRPP / Despliegue| Realización de presentaciones y preparación de estas, y despliegue al final del sprint |
| Lidia Jiménez Soriano                  | Frontend Android            | Implementación del frontend en Android de todo lo relacionado con Usuarios, Productos y Ventas |
| Óscar Menéndez Márquez                 | Frontend Android            | Implementación del frontend en Android de todo lo relacionado con Usuarios, Productos y Ventas |
| Carlos Martín De Prado Barragán        | Frontend Android            | Implementación del frontend en Android de todo lo relacionado con Usuarios, Productos y Ventas |
| Álvaro Ruiz Gutiérrez                  | Frontend Android            | Implementación del frontend en Android de todo lo relacionado con Usuarios, Productos y Ventas |
| Ibai Pérez Fernández                   | FullStack                   | Implementación tanto de backend y frontend (página web) de todo lo relacionado con Usuarios, Productos y Ventas |
| Francisco Manuel Sabido González       | FullStack                   | Implementación tanto de backend y frontend (página web) de todo lo relacionado con Usuarios, Productos y Ventas |
| Alejandro Vargas Muñiz                 | FullStack                   | Implementación tanto de backend y frontend (página web) de todo lo relacionado con Usuarios, Productos y Ventas |
| Jose Miguel Iborra Conejo              | FullStack                   | Implementación tanto de backend y frontend (página web) de todo lo relacionado con Usuarios, Productos y Ventas |
| David Vicente Valderrama               | FullStack                   | Implementación tanto de backend y frontend (página web) de todo lo relacionado con Usuarios, Productos y Ventas |
| Pablo Rufián Jiménez                   | IA Developer                | Inicialización del entrenamiento de la IA y ayuda a FullStack |
| Rafael Molina García                   | IA Developer                | Inicialización del entrenamiento de la IA y ayuda a FullStack |
| Julio Solís Alfonso                    | QA & Tester                 | Realización de pruebas sobre lo creado por el equipo de FullStack |
| Mario Zambrano Zapata                  | QA & Tester                 | Realización de pruebas sobre lo creado por el equipo de FullStack |


- **Metodología usada**: SCRUM   
- **Herramientas empleadas**: Node.js, Lombok, React, MariaDB, GitHub (repositorio), Google Drive, WhatsApp y Discord.

---

## 3. Identificación de problemas

| ID   | Descripción breve del problema                                           | Identificado por         | Momento de identificación   |
|------|-------------------------------------------------------------------------|--------------------------|----------------------------|
| P-01 | No se desplegó la aplicación                               | Equipo                   | Después de la entrega      |
| P-02 | Falta de organización en el equipo de FullStack                         | Equipo                   | Durante el sprint          |
| P-03 | Cambio en la idea general del proyecto a pocos días de la entrega       | Equipo                   | Durante el sprint          |
| P-04 | Falta de comunicación en el equipo                                      | Equipo                   | Durante el sprint          |
| P-05 | No se implementaron todas las funcionalidades previstas para la entrega | Equipo                   | Después de la entrega      |
| P-06 | Falta de implicación y compromiso por parte de algunos miembros del equipo | Equipo                   | Durante el sprint          |
| P-07 | Ausencia no notificada de un miembro del equipo durante la sesión de evaluación | Equipo                   | Después de la entrega      |

---

## 4. Análisis detallado de los problemas

### Problema P-01 – No se desplegó la aplicación

| Aspecto                        | Detalle                                                                 |
|-------------------------------------|-----------------------------------------------------------------------------|
| **Origen técnico**                 | No se generaron los artefactos necesarios para el despliegue, lo que impidió que la aplicación se desplegara correctamente. |
| **Origen de proceso**              | La falta de planificación adecuada por parte del equipo de FullStack, sumado al cambio de la idea general del negocio, generó un desajuste en los tiempos y los entregables. |
| **Error introducido por**          | Departamento de RRPPs, Analistas y Despliegue (eran responsables del despliegue). |
| **Error permitido por**            | Equipo de FullStack (no realizaron el trabajo correctamente para entregar la aplicación lista para el despliegue). |
| **Acciones de mitigación técnicas**| Asegurar que se generen todos los artefactos necesarios para el despliegue con antelación y realizar pruebas de despliegue en entornos de preproducción. |
| **Acciones de mitigación de proceso**| Mejorar la planificación interna del equipo de FullStack y asegurar que todos los departamentos estén alineados antes de la entrega final. |
| **Estado**                         | Resuelto ✅                                                                  |
| **Justificación del estado**       | El despliegue de las siguientes entregas se ha realizado correctamente, aplicando las correcciones necesarias para garantizar que el proceso se lleve a cabo de forma adecuada. |


### Problema P-02 – Falta de organización en el equipo de FullStack

| Aspecto                        | Detalle                                                                 |
|-------------------------------------|-----------------------------------------------------------------------------|
| **Origen técnico**                 | No se estableció una estructura adecuada para la asignación de tareas y la coordinación dentro del equipo de FullStack. |
| **Origen de proceso**              | Falta de planificación y de organización en las tareas dentro del equipo de FullStack. La comunicación entre miembros fue insuficiente, lo que generó descoordinación. |
| **Error introducido por**          | Equipo de FullStack (falta de organización en la asignación de tareas y seguimiento). |
| **Error permitido por**            | Project Manager y el coordinador del departamento de FullStack (falta de supervisión y control sobre la organización interna del equipo). |
| **Acciones de mitigación técnicas**| Definir roles y responsabilidades claras dentro del equipo de FullStack y utilizar herramientas de gestión de tareas para mejorar la organización interna. |
| **Acciones de mitigación de proceso**| Asegurar que el Project Manager y los coordinadores de los departamentos supervisen y validen la planificación interna del equipo antes del inicio de cada Sprint. |
| **Estado**                         | Resuelto ✅                                                                  |
| **Justificación del estado**       | Se implementaron nuevas estrategias de organización interna y mejora de la planificación, las cuales se han aplicado en entregas posteriores. |


### Problema P-03 – Cambio en la idea general del proyecto a pocos días de la entrega

| Aspecto                        | Detalle                                                                 |
|-------------------------------|-------------------------------------------------------------------------|
| **Origen técnico**            | El cambio de dirección afectó tanto el diseño como la implementación, lo que llevó a retrasos y a una reestructuración parcial de las tareas. |
| **Origen de proceso**         | No se gestionó de forma adecuada el cambio en la dirección, lo que afectó la alineación del equipo con los objetivos iniciales. |
| **Error introducido por**     | Todo el equipo (no se anticiparon adecuadamente los impactos del cambio). |
| **Error permitido por**       | Todo el equipo (falta de planificación para mitigar el impacto de cambios en la dirección). |
| **Acciones de mitigación técnicas** | Mejorar la gestión de cambios dentro de los sprints, con una evaluación de impacto clara antes de cualquier modificación. |
| **Acciones de mitigación de proceso** | Implementar una fase de revisión exhaustiva de los objetivos y alcances antes de realizar cambios significativos. |
| **Estado**                    | En proceso ⏳                                                             |
| **Justificación del estado** | El equipo ha aprendido a gestionar mejor los cambios a medida que se avanzaba en el proyecto, pero el impacto de este cambio sigue siendo un área de mejora. |


### Problema P-04 – Falta de comunicación en el equipo

| Aspecto                        | Detalle                                                                 |
|-------------------------------------|-----------------------------------------------------------------------------|
| **Origen técnico**                 | La comunicación no fue suficiente para asegurar que todos los miembros estuvieran alineados con el progreso y los objetivos del sprint. |
| **Origen de proceso**              | La falta de reuniones de seguimiento y la escasa comunicación informal entre los miembros del equipo dificultaron la resolución de problemas de forma oportuna. |
| **Error introducido por**          | Todo el equipo (comunicación deficiente y poco frecuente)                   |
| **Error permitido por**            | Project Manager y Coordinadores de los departamentos (falta de seguimiento de la comunicación interna) |
| **Acciones de mitigación técnicas**| Establecer reuniones regulares de actualización para compartir avances, bloqueos y resolver problemas rápidamente. |
| **Acciones de mitigación de proceso**| Fomentar una cultura de comunicación constante y abierta. |
| **Estado**                         | Resuelto ✅                                                                  |
| **Justificación del estado**       | La comunicación ha mejorado tras las lecciones aprendidas, y se han implementado nuevas prácticas de comunicación que han dado mejores resultados en entregas posteriores. |


### Problema P-05 – No se implementaron todas las funcionalidades previstas para la entrega

| Aspecto                        | Detalle                                                                 |
|-------------------------------------|-----------------------------------------------------------------------------|
| **Origen técnico**                 | La falta de planificación adecuada y el cambio en la dirección del proyecto hicieron que muchas funcionalidades no se realizaran en el sprint. |
| **Origen de proceso**              | La mala gestión de prioridades, la falta de planificación y el cambio en los objetivos dificultaron la implementación completa de las funcionalidades. |
| **Error introducido por**          | Equipo de FullStack.                                                        |
| **Error permitido por**            | Project Manager y Coordinador del departamento de FullStack (falta de seguimiento continuo y priorización efectiva). |
| **Acciones de mitigación técnicas** | Establecer una lista clara de funcionalidades clave y prioridades.          |
| **Acciones de mitigación de proceso** | Realizar un seguimiento más cercano de las tareas. Además, alinear mejor las prioridades con los objetivos del sprint. |
| **Estado**                         | Resuelto ✅                                                                  |
| **Justificación del estado**       | Las funcionalidades se han ido completando en entregas posteriores, corrigiendo el retraso producido en este sprint. Se implementó un proceso de priorización más estricto y planificación de tareas para las siguientes entregas. |


### Problema P-06 – Falta de implicación y compromiso por parte de algunos miembros del equipo

| Aspecto                        | Detalle                                                                |
|-------------------------------------|-----------------------------------------------------------------------------|
| **Origen técnico**                 | No se identificó un problema técnico específico relacionado con la falta de implicación. |
| **Origen de proceso**              | La falta de responsabilidad individual y seguimiento adecuado dentro del equipo permitió que algunos miembros no cumplieran con sus tareas asignadas. |
| **Error introducido por**          | Miembros del equipo con bajo nivel de compromiso e implicación en sus tareas. |
| **Error permitido por**            | Project Manager y Coordinadores de departamentos (falta de control sobre el rendimiento y la implicación de los miembros del equipo). |
| **Acciones de mitigación técnicas** | No aplicable.                                                               |
| **Acciones de mitigación de proceso** | Establecimos un acuerdo de "stakes", donde si un miembro acumulaba 3 stakes debido a su falta de implicación o incumplimiento de tareas, sería expulsado del proyecto. Esto ayuda a mejorar el compromiso y la responsabilidad individual dentro del equipo. |
| **Estado**                         | En proceso ⏳                                                                |
| **Justificación del estado**       | Aunque se ha identificado el problema, es necesario continuar con acciones de seguimiento para mejorar la implicación del equipo en entregas futuras. Se están tomando medidas para asegurar la implicación de todos los miembros. |

### Problema P-07 – Ausencia no notificada de un miembro del equipo durante la sesión de evaluación

| Aspecto                        | Detalle                                                                 |
|-------------------------------|-------------------------------------------------------------------------|
| **Origen técnico**            | No aplicable.                                                           |
| **Origen de proceso**         | El miembro del equipo no comunicó su ausencia. |
| **Error introducido por**     | Miembro del equipo que no notificó su ausencia.                         |
| **Error permitido por**       | Todo el equipo (falta de seguimiento y gestión de la asistencia a las sesiones). |
| **Acciones de mitigación técnicas** | No aplicable.                                                           |
| **Acciones de mitigación de proceso** | Se preguntará al comienzo de cada sprint y al inicio de la semana de evaluación si algún miembro no podrá asistir a la sesión de evaluación. Esto permitirá realizar las acciones adecuadas con antelación. |
| **Estado**                    | Resuelto ✅                                                              |
| **Justificación del estado** | Se ha reforzado la comunicación interna y la gestión de la asistencia en entregas posteriores. |


## 5. Conclusiones generales

La entrega S1 representó un punto de inflexión para el equipo, ya que permitió visibilizar de forma clara las debilidades organizativas, de comunicación y de gestión que estaban afectando el desarrollo del proyecto. A pesar de no cumplir con los objetivos establecidos para el sprint, este fallo se convirtió en una oportunidad valiosa para identificar errores críticos y trabajar en su resolución.

Entre los aprendizajes más significativos se destacan:

- **La importancia de una buena planificación y coordinación interna**, especialmente en equipos grandes y multidisciplinares.
- **El impacto que tiene la comunicación constante y efectiva** en el alineamiento de tareas y en la resolución temprana de problemas.
- **La necesidad de gestionar adecuadamente los cambios en el enfoque del proyecto**, evaluando sus implicaciones y asegurando que todo el equipo esté al tanto.
- **El valor de establecer mecanismos de seguimiento al compromiso individual**, lo que permite actuar con anticipación ante posibles riesgos.

Como resultado del análisis y las acciones correctivas implementadas, se han logrado mejoras en entregas posteriores, tanto en el proceso de despliegue como en la ejecución de tareas, demostrando que el equipo ha sabido adaptarse y evolucionar a partir de sus errores.

De cara a las próximas iteraciones, el equipo se compromete a seguir aplicando estas lecciones aprendidas, reforzando los procesos de comunicación, planificación y seguimiento, con el objetivo de garantizar entregas más exitosas y colaborativas.




