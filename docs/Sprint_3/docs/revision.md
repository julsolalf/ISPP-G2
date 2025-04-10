---
title: Revision
groupNumber: 2
groupName: GastroStock
deliverable: Sprint 3
description: Casos de uso del sistema GastroStock
author: Writing by Ibai Pérez Fernández
---

# [Sprint 3] Revision - GastroStock (Group 2)

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