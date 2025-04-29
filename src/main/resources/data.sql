--Borrando todos los datos anteriores para partir siempre desde el mismo punto de inicio
DELETE FROM dia_reparto;
DELETE FROM lote;
DELETE FROM reabastecimiento;
DELETE FROM proveedor;
DELETE FROM ingrediente;
DELETE FROM producto_inventario;
DELETE FROM linea_de_pedido;
DELETE FROM pedido;
DELETE FROM producto_venta;
DELETE FROM mesa;
DELETE FROM empleado;
DELETE FROM negocio;
DELETE FROM dueno;
DELETE FROM subscripcion;
DELETE FROM app_user;
DELETE FROM authorities;

-- Insertando autoridades
INSERT INTO authorities (id, authority) VALUES (1,'dueno');
INSERT INTO authorities (id, authority) VALUES (2,'empleado');
INSERT INTO authorities (id, authority) VALUES (3, 'admin');

-- Insertar suscripciones gratuitas para todos los usuarios
INSERT INTO subscripcion (id, type, status, start_date) VALUES (1, 'FREE', 'ACTIVE', CURRENT_TIMESTAMP());
INSERT INTO subscripcion (id, type, status, start_date) VALUES (2, 'FREE', 'ACTIVE', CURRENT_TIMESTAMP());
INSERT INTO subscripcion (id, type, status, start_date) VALUES (3, 'FREE', 'ACTIVE', CURRENT_TIMESTAMP());
INSERT INTO subscripcion (id, type, status, start_date) VALUES (4, 'FREE', 'ACTIVE', CURRENT_TIMESTAMP());
INSERT INTO subscripcion (id, type, status, start_date) VALUES (5, 'FREE', 'ACTIVE', CURRENT_TIMESTAMP());
INSERT INTO subscripcion (id, type, status, start_date) VALUES (6, 'FREE', 'ACTIVE', CURRENT_TIMESTAMP());
INSERT INTO subscripcion (id, type, status, start_date) VALUES (7, 'FREE', 'ACTIVE', CURRENT_TIMESTAMP());
INSERT INTO subscripcion (id, type, status, start_date) VALUES (8, 'FREE', 'ACTIVE', CURRENT_TIMESTAMP());
INSERT INTO subscripcion (id, type, status, start_date) VALUES (9, 'FREE', 'ACTIVE', CURRENT_TIMESTAMP());
INSERT INTO subscripcion (id, type, status, start_date) VALUES (10, 'FREE', 'ACTIVE', CURRENT_TIMESTAMP());
UPDATE subscripcion SET stripe_customer_id = 'cus_test_123456' WHERE id = 1;
-- Actualizar usuarios para asociarlos con sus suscripciones
UPDATE app_user SET subscripcion_id = 1 WHERE id = 1; -- admin
UPDATE app_user SET subscripcion_id = 2 WHERE id = 2; -- admin2
UPDATE app_user SET subscripcion_id = 3 WHERE id = 3; -- juan
UPDATE app_user SET subscripcion_id = 4 WHERE id = 4; -- alejandro
UPDATE app_user SET subscripcion_id = 5 WHERE id = 5; -- antonio
UPDATE app_user SET subscripcion_id = 6 WHERE id = 6; -- paco
UPDATE app_user SET subscripcion_id = 7 WHERE id = 7; -- fernando
UPDATE app_user SET subscripcion_id = 8 WHERE id = 8; -- owner1 (temporal)
UPDATE app_user SET subscripcion_id = 9 WHERE id = 9; -- empleado (temporal)
UPDATE app_user SET subscripcion_id = 10 WHERE id = 10; -- gastroAdmin

-- Insertando usuarios todos con password como contraseña
INSERT INTO app_user (id, username, password, authority_id) VALUES (1, 'admin', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'dueno'));
INSERT INTO app_user (id, username, password, authority_id) VALUES (2, 'admin2', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'dueno'));
INSERT INTO app_user (id, username, password, authority_id) VALUES (3, 'juan', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'empleado'));
INSERT INTO app_user (id, username, password, authority_id) VALUES (4, 'alejandro', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'empleado'));
INSERT INTO app_user (id, username, password, authority_id) VALUES (5, 'antonio', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'empleado'));
INSERT INTO app_user (id, username, password, authority_id) VALUES (6, 'paco', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'empleado'));
INSERT INTO app_user (id, username, password, authority_id) VALUES (7, 'fernando', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'empleado'));
--Usuarios temporales mientras se conecta el frontend con el backend
INSERT INTO app_user (id, username, password, authority_id) VALUES (8, 'owner1', 'password', (SELECT id FROM authorities WHERE authority = 'dueno'));
INSERT INTO app_user (id, username, password, authority_id) VALUES (9, 'empleado', 'password', (SELECT id FROM authorities WHERE authority = 'empleado'));
-- Usuario admin, psswd = password
INSERT INTO app_user (id, username, password, authority_id) VALUES (10,'gastroAdmin','$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm',(SELECT id FROM authorities WHERE authority ='admin'));

-- Insertando duenos
INSERT INTO dueno (id, first_name, last_name, email, num_telefono, token_dueno, user_id)
VALUES (1, 'Carlos', 'Perez', 'carlos.perez@gmail.com', '623486789', 'gst-hoGkisz7nslugPSIbZ8mp0QW3JSYhM1', (SELECT id FROM app_user WHERE username = 'admin'));
INSERT INTO dueno (id, first_name, last_name, email, num_telefono, token_dueno, user_id)
VALUES (2, 'Pablo', 'Rivas', 'pablo.rivas@gmail.com', '623456789', 'gst-hoGkisz7nslugPSIbZ8m7Tcq3JSYhM2', (SELECT id FROM app_user WHERE username = 'admin2'));
--Dueño temporal mientras se conecta el frontend con el backend
INSERT INTO dueno (id, first_name, last_name, email, num_telefono, token_dueno, user_id)
VALUES (3, 'Owner', 'Temporal', 'owner@gmail.com', '623654789', 'gst-hoGkisz7etabePSIbZ8m7Tcq3JSYhM2', (SELECT id FROM app_user WHERE username = 'owner1'));

-- Insertando negocios
INSERT INTO negocio (id, name, token_negocio, direccion, codigo_postal, ciudad, pais, dueno_id)
VALUES (1, 'Restaurante La Trattoria', 12345, 'Calle Falsa 123', '28001', 'Madrid', 'Espana', (SELECT id FROM dueno WHERE first_name = 'Carlos')); --Cambiar de dueño cuando se borre el temporal
INSERT INTO negocio (id, name, token_negocio, direccion, codigo_postal, ciudad, pais, dueno_id)
VALUES (2, 'Restaurante Burguer', 09876, 'Calle Falsa 123', '28001', 'Madrid', 'Espana', (SELECT id FROM dueno WHERE first_name = 'Carlos'));

-- Insertando empleados
INSERT INTO empleado (id, first_name, last_name, email, num_telefono, token_empleado, descripcion, negocio_id, user_id)
VALUES (1,'Juan', 'Garcia', 'juan.garcia@gmail.com', '987654321', 'gst-hoGkisz7nspabPSIbZ8mp0QW3JSYhM3', 'Cocina', (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'), (SELECT id FROM app_user WHERE username = 'juan'));
INSERT INTO empleado (id, first_name, last_name, email, num_telefono, token_empleado, descripcion, negocio_id, user_id)
VALUES (2, 'Alejandro', 'Vargas', 'alejandro.vargas@gmail.com', '987654322', 'gst-hoGkisz7nslugPSpay8mp0QW3JSYhM4', 'Exterior', (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'), (SELECT id FROM app_user WHERE username = 'alejandro'));
INSERT INTO empleado (id, first_name, last_name, email, num_telefono, token_empleado, descripcion, negocio_id, user_id)
VALUES (3, 'Antonio', 'Fernández', 'antonio.fernandez@gmail.com', '987654323', 'gst-hoGkeby7nslugPSIbZ8mp0QW3JSYhM5', 'Barra', (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'), (SELECT id FROM app_user WHERE username = 'antonio'));
INSERT INTO empleado (id, first_name, last_name, email, num_telefono, token_empleado, descripcion, negocio_id, user_id)
VALUES (4, 'Paco', 'Hernández', 'paco.hernandez@gmail.com', '987654324', 'gst-hoGkisz7nslugPfQbZ8mp0QW3JSYhM6', 'Cocina', (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'), (SELECT id FROM app_user WHERE username = 'paco'));
INSERT INTO empleado (id, first_name, last_name, email, num_telefono, token_empleado, descripcion, negocio_id, user_id)
VALUES (5, 'Fernando', 'Pérez', 'fernando.perez@gmail.com', '987654325', 'gst-hoGkisz7metalPSIbZ8mp0QW3JSYhM7', null, (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'), (SELECT id FROM app_user WHERE username = 'fernando'));
--Empleado temporal mientras se conecta el frontend con el backend
INSERT INTO empleado (id, first_name, last_name, email, num_telefono, token_empleado, descripcion, negocio_id, user_id)
VALUES (6, 'Empleado', 'Temporal', 'empleado@gmail.com', '987456325', 'gst-hoGkisz7mgoldPSIbZ8mp0QW3JSYhM7', null, (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'), (SELECT id FROM app_user WHERE username = 'empleado'));

-- Insertando mesas
INSERT INTO mesa (id, name, numero_asientos, negocio_id)
VALUES (1, 'Mesa 1', 4, (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO mesa (id, name, numero_asientos, negocio_id)
VALUES (2, 'Mesa 2', 2, (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO mesa (id, name, numero_asientos, negocio_id)
VALUES (3, 'Mesa 3', 6, (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO mesa (id, name, numero_asientos, negocio_id)
VALUES (4, 'Mesa 4', 2, (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO mesa (id, name, numero_asientos, negocio_id)
VALUES (5, 'Mesa 5', 4, (SELECT id FROM negocio WHERE name = 'Restaurante Burguer'));

--Insertando Categoria
INSERT INTO categoria (id, name, pertenece, negocio_id)
VALUES (1, 'COMIDA',1, (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO categoria (id, name, pertenece, negocio_id)
VALUES (2, 'BEBIDAS',1,(SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO categoria (id, name,pertenece, negocio_id)
VALUES (3, 'COMIDA',1,(SELECT id FROM negocio WHERE name = 'Restaurante Burguer'));
INSERT INTO categoria (id, name, pertenece, negocio_id)
VALUES (4, 'BEBIDAS',1,(SELECT id FROM negocio WHERE name = 'Restaurante Burguer'));
INSERT INTO categoria (id, name, pertenece, negocio_id)
VALUES (5, 'HARINAS',0, (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO categoria (id, name, pertenece, negocio_id)
VALUES (6, 'VERDURAS',0,(SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO categoria (id, name, pertenece, negocio_id)
VALUES (7, 'CARNES',0,(SELECT id FROM negocio WHERE name = 'Restaurante Burguer'));

-- Insertando productos de venta
INSERT INTO producto_venta (id, name, categoria_id, precio_venta)
VALUES (1, 'Pizza Margherita', 1, 12.50);
INSERT INTO producto_venta (id, name, categoria_id, precio_venta)
VALUES (2, 'Coca Cola', 2, 2.50);
INSERT INTO producto_venta (id, name, categoria_id, precio_venta)
VALUES (3, 'Nestea', 2, 2.50);
INSERT INTO producto_venta (id, name, categoria_id, precio_venta)
VALUES (4, 'Fanta', 2, 2.50);
INSERT INTO producto_venta (id, name, categoria_id, precio_venta)
VALUES (5, 'Cerveza', 4, 2.50);
INSERT INTO producto_venta (id, name, categoria_id, precio_venta)
VALUES (6, 'Hamburguesa', 3, 12.50);
INSERT INTO producto_venta (id, name, categoria_id, precio_venta)
VALUES (7, 'Cocido', 3, 7.50);
INSERT INTO producto_venta (id, name, categoria_id, precio_venta)
VALUES (8, 'Pez espada', 3, 10.50);

-- Insertando pedidos
INSERT INTO pedido (id, fecha, precio_total, mesa_id, empleado_id, negocio_id)
VALUES (1, '2025-03-17 13:00:00', 15.00, (SELECT id FROM mesa WHERE name = 'Mesa 1'), (SELECT id FROM empleado WHERE first_name = 'Juan' AND last_name = 'Garcia'), 1);

-- Insertando líneas de pedido
INSERT INTO linea_de_pedido (id, cantidad, salio_de_cocina, precio_unitario, pedido_id, producto_id)
VALUES (1, 1, true, 12.50, (SELECT id FROM pedido WHERE precio_total = 15.00), (SELECT id FROM producto_venta WHERE name = 'Pizza Margherita'));
INSERT INTO linea_de_pedido (id, cantidad, salio_de_cocina, precio_unitario, pedido_id, producto_id)
VALUES (2, 1, true, 2.50, (SELECT id FROM pedido WHERE precio_total = 15.00), (SELECT id FROM producto_venta WHERE name = 'Coca Cola'));

-- Insertando proveedores
INSERT INTO proveedor (id, name, email, telefono, direccion, negocio_id)
VALUES (1, 'Proveedor A', 'proveedorA@mail.com', '987654321', 'Calle Proveedor A',(SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO proveedor (id, name, email, telefono, direccion, negocio_id)
VALUES (2, 'Proveedor B', 'proveedorB@mail.com', '987654322', 'Calle Proveedor B',(SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO proveedor (id, name, email, telefono, direccion, negocio_id)
VALUES (3, 'Proveedor La Burguer', 'proveedorLa@mail.com', '987654323', 'Calle Proveedor La Burguer',(SELECT id FROM negocio WHERE name = 'Restaurante Burguer'));

-- Insertando productos en inventario
INSERT INTO producto_inventario (id, name, categoria_id, precio_compra, cantidad_deseada, cantidad_aviso, proveedor_id)
VALUES (1, 'Harina', 5, 0.50, 100, 10, 1);
INSERT INTO producto_inventario (id, name, categoria_id, precio_compra, cantidad_deseada, cantidad_aviso, proveedor_id)
VALUES (2, 'Tomate', 6, 0.30, 100, 10,1);
INSERT INTO producto_inventario (id, name, categoria_id, precio_compra, cantidad_deseada, cantidad_aviso, proveedor_id)
VALUES (3, 'Carne Buey', 7, 5.50, 50, 5, 3);

-- Insertando ingredientes
INSERT INTO ingrediente (id, cantidad, producto_inventario_id, producto_venta_id)
VALUES (1, 5, (SELECT id FROM producto_inventario WHERE name = 'Harina'), (SELECT id FROM producto_venta WHERE name = 'Pizza Margherita'));
INSERT INTO ingrediente (id, cantidad, producto_inventario_id, producto_venta_id)
VALUES (2, 5, (SELECT id FROM producto_inventario WHERE name = 'Carne Buey'), (SELECT id FROM producto_venta WHERE name = 'Hamburguesa'));

-- Insertando reabastecimientos
INSERT INTO reabastecimiento (id, precio_total, referencia, fecha, proveedor_id, negocio_id)
VALUES (1, 100, 'REF001', '2025-06-01', (SELECT id FROM proveedor WHERE name = 'Proveedor A'), (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO reabastecimiento (id, precio_total, referencia, fecha, proveedor_id, negocio_id)
VALUES (2, 200, 'REF002', '2025-06-02', (SELECT id FROM proveedor WHERE name = 'Proveedor B'), (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));

-- Insertando lotes
INSERT INTO lote (id, cantidad, fecha_caducidad, producto_id, reabastecimiento_id)
VALUES (1, 100, '2025-08-01', (SELECT id FROM producto_inventario WHERE name = 'Harina'), (SELECT id FROM reabastecimiento WHERE referencia = 'REF001'));
INSERT INTO lote (id, cantidad, fecha_caducidad, producto_id, reabastecimiento_id)
VALUES (2, 100, '2025-08-02', (SELECT id FROM producto_inventario WHERE name = 'Harina'), (SELECT id FROM reabastecimiento WHERE referencia = 'REF002'));
INSERT INTO lote (id, cantidad, fecha_caducidad, producto_id, reabastecimiento_id)
VALUES (3, 100, '2025-07-02', (SELECT id FROM producto_inventario WHERE name = 'Carne Buey'), (SELECT id FROM reabastecimiento WHERE referencia = 'REF002'));

--Insertando diaReparto
INSERT INTO dia_reparto (id, descripcion, dia_semana, proveedor_id)
VALUES (1, '',5,(SELECT id FROM proveedor WHERE name = 'Proveedor A'));
INSERT INTO dia_reparto (id, descripcion, dia_semana, proveedor_id)
VALUES (2, 'Suele venir muy pronto',6,(SELECT id FROM proveedor WHERE name = 'Proveedor B'));

-- Insertando carritos
INSERT INTO carrito (id, precio_total, proveedor_id, dia_entrega)
VALUES (1, 160, (SELECT id FROM proveedor WHERE name = 'Proveedor A'), '2025-03-01');
INSERT INTO carrito (id, precio_total, proveedor_id, dia_entrega)
VALUES (2, 0, (SELECT id FROM proveedor WHERE name = 'Proveedor B'), '2025-03-02');

-- Insertando lineasDeCarrito
INSERT INTO linea_de_carrito (id, cantidad, precio_linea, producto_id, carrito_id)
VALUES (1, 200, 100, (SELECT id FROM producto_inventario WHERE name = 'Harina'), (SELECT id FROM carrito WHERE id = 1));
INSERT INTO linea_de_carrito (id, cantidad, precio_linea, producto_id, carrito_id)
VALUES (2, 200, 60,(SELECT id FROM producto_inventario WHERE name = 'Tomate'), (SELECT id FROM carrito WHERE id = 1));

