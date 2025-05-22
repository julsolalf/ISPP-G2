---
title: Revision
groupNumber: 2
groupName: GastroStock
deliverable: World Project Launch
description: Casos de uso del sistema GastroStock
author: Writing by Ibai Pérez Fernández
---

# [World Project Launch] Revision - GastroStock (Group 2)

In this file we will present an explicit mapping of each one of the main Use Cases of GastroStock.

## CU-000: Gestionar los empleados de un negocio

**Descripción**: Permite a un dueño gestionar los empleados de su negocio.

**Pasos**:
1. Iniciar sesión con usuario `"admin"` y contraseña `"password"`.
2. Seleccionar el negocio correspondiente.
3. Acceder a la pestaña **Empleados**.
4. Visualizar el listado de empleados.
5. Pulsar **Exportar** para obtener los datos de empleados.
6. Pulsar **Añadir** para registrar un nuevo empleado (requiere rellenar formulario).
7. Acceder a los detalles de un empleado pulsando su botón.
8. Eliminar un empleado pulsando el botón rojo desde sus detalles.

---

## CU-001: Gestionar el inventario de un negocio

**Descripción**: Permite la gestión de categorías y productos del inventario de un negocio.

**Pasos**:
1. Iniciar sesión con usuario `"admin"` y contraseña `"password"`.
2. Seleccionar el negocio correspondiente.
3. Acceder a la pestaña **Inventario**.
4. Visualizar las categorías de productos.
5. Pulsar **Exportar** para descargar los datos.
6. Pulsar **Alerta Stock** para ver alertas actuales.
7. Pulsar **Añadir** para registrar una nueva categoría.
8. Acceder a los productos de una categoría pulsando su botón.
9. Visualizar productos con cantidades actuales.
10. Pulsar **Exportar** para datos de productos.
11. Pulsar **Añadir** para registrar un nuevo producto.
12. Pulsar **Ver** para ver detalles de un producto.

---

## CU-002: Acceso a estadísticas de un negocio a través de un dashboard

**Descripción**: Visualización y exportación de estadísticas del negocio.

**Pasos**:
1. Iniciar sesión con usuario `"admin"` y contraseña `"password"`.
2. Seleccionar el negocio.
3. Acceder a la pestaña **Dashboard**.
4. Pulsar **Descargar PDF** para obtener las gráficas.

---

## CU-003: Gestionar pedidos de un negocio

**Descripción**: Gestión y exportación de pedidos del negocio.

**Pasos**:
1. Iniciar sesión con usuario `"admin"` y contraseña `"password"`.
2. Seleccionar el negocio.
3. Acceder a la pestaña **Ventas**.
4. Visualizar el listado de pedidos.
5. Pulsar **Exportar** para obtener los datos.
6. ⚠️ **Nota**: La parte de empleados relacionada aún no está implementada en el frontend.

---

## CU-004: Gestionar proveedores de un negocio

**Descripción**: Gestión básica de los proveedores de un negocio.

**Pasos**:
1. Iniciar sesión con usuario `"admin"` y contraseña `"password"`.
2. Seleccionar el negocio.
3. Acceder a la pestaña **Proveedores**.
4. Visualizar el listado de proveedores.
5. Pulsar **Exportar** para descargar los datos.
6. Pulsar **Añadir** para registrar un nuevo proveedor.
7. ⚠️ **Nota**: No está implementada la visualización de detalles ni reabastecimientos.


### Necessary Data

##### Landing Page

    https://jsalasm5.github.io/GastroStock_LandingPage

##### Credentials

- Dueño:
    - Usuario: admin
    - Contraseña: password
- Empleado:
    - Usuario: paco
    - Contraseña: password
- Administrador:
    - Usuario: gastroAdmin
    - Contraseña: password

##### Url of the deployed App

    https://ispp-2425-g2.ew.r.appspot.com

##### Url of GitHub Repository

    https://github.com/julsolalf/ISPP-G2.git

##### Url of Time Tracking Tool

    https://app.clockify.me/reports/detailed?start=2025-03-14T00:00:00.000Z&end=2025-04-11T23:59:59.999Z&filterValuesData=%7B%22projects%22:%5B%2267f009cf002a3d02a259a604%22,%2267e6c9ef007aaf14ddb07163%22%5D%7D&filterOptions=%7B%22projects%22:%7B%22status%22:%22ACTIVE%22%7D%7D&page=1&pageSize=50