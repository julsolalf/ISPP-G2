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
DELETE FROM app_user;
DELETE FROM authorities;

-- Insertando autoridades
INSERT INTO authorities (id, authority) VALUES (1,'dueno');
INSERT INTO authorities (id, authority) VALUES (2,'empleado');

-- Insertando usuarios todos con password como contraseña
INSERT INTO app_user (id, username, password, authority_id) VALUES (1, 'admin', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'dueno'));
INSERT INTO app_user (id, username, password, authority_id) VALUES (2, 'admin2', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'dueno'));
INSERT INTO app_user (id, username, password, authority_id) VALUES (3, 'juan', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'empleado'));
INSERT INTO app_user (id, username, password, authority_id) VALUES (4, 'alejandro', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'empleado'));
INSERT INTO app_user (id, username, password, authority_id) VALUES (5, 'antonio', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'empleado'));
INSERT INTO app_user (id, username, password, authority_id) VALUES (6, 'paco', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'empleado'));
INSERT INTO app_user (id, username, password, authority_id) VALUES (7, 'fernando', '$2a$10$wPqDTEhcLj7vLpEVxvlreehCK1tZl0FtvaxXxTiQoJOIOJL2uXSQm', (SELECT id FROM authorities WHERE authority = 'empleado'));

-- Insertando duenos
INSERT INTO dueno (id, first_name, last_name, email, num_telefono, token_dueno, user_id)
VALUES (1, 'Carlos', 'Perez', 'carlos.perez@gmail.com', '123486789', 'gst-hoGkisz7nslugPSIbZ8mp0QW3JSYhM1', (SELECT id FROM app_user WHERE username = 'admin'));
INSERT INTO dueno (id, first_name, last_name, email, num_telefono, token_dueno, user_id)
VALUES (2, 'Pablo', 'Rivas', 'pablo.rivas@gmail.com', '123456789', 'gst-hoGkisz7nslugPSIbZ8m7Tcq3JSYhM2', (SELECT id FROM app_user WHERE username = 'admin2'));

-- Insertando negocios
INSERT INTO negocio (id, name, token_negocio, direccion, codigo_postal, ciudad, pais, dueno_id)
VALUES (1, 'Restaurante La Trattoria', 12345, 'Calle Falsa 123', '28001', 'Madrid', 'Espana', (SELECT id FROM dueno WHERE first_name = 'Carlos'));
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
VALUES (1, '2025-03-17 13:00:00', 15.00, (SELECT id FROM mesa WHERE name = 'Mesa 1'), (SELECT id FROM empleado WHERE first_name = 'Juan' AND last_name = 'Garcia'), (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));

-- Insertando líneas de pedido
INSERT INTO linea_de_pedido (id, cantidad, precio_linea, pedido_id, producto_id)
VALUES (1, 1, 12.50, (SELECT id FROM pedido WHERE precio_total = 15.00), (SELECT id FROM producto_venta WHERE name = 'Pizza Margherita'));
INSERT INTO linea_de_pedido (id, cantidad, precio_linea, pedido_id, producto_id)
VALUES (2, 1, 2.50, (SELECT id FROM pedido WHERE precio_total = 15.00), (SELECT id FROM producto_venta WHERE name = 'Coca Cola'));

-- Insertando productos en inventario
INSERT INTO producto_inventario (id, name, categoria_id, precio_compra, cantidad_deseada, cantidad_aviso)
VALUES (1, 'Harina', 5, 0.50, 100, 10);
INSERT INTO producto_inventario (id, name, categoria_id, precio_compra, cantidad_deseada, cantidad_aviso)
VALUES (2, 'Tomate', 6, 0.30, 100, 10);
INSERT INTO producto_inventario (id, name, categoria_id, precio_compra, cantidad_deseada, cantidad_aviso)
VALUES (3, 'Carne Buey', 7, 5.50, 50, 5);

-- Insertando ingredientes
INSERT INTO ingrediente (id, cantidad, producto_inventario_id, producto_venta_id)
VALUES (1, 5, (SELECT id FROM producto_inventario WHERE name = 'Harina'), (SELECT id FROM producto_venta WHERE name = 'Pizza Margherita'));
INSERT INTO ingrediente (id, cantidad, producto_inventario_id, producto_venta_id)
VALUES (2, 5, (SELECT id FROM producto_inventario WHERE name = 'Carne Buey'), (SELECT id FROM producto_venta WHERE name = 'Hamburguesa'));

-- Insertando proveedores
INSERT INTO proveedor (id, name, email, telefono, direccion, negocio_id)
VALUES (1, 'Proveedor A', 'proveedorA@mail.com', '987654321', 'Calle Proveedor A',(SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO proveedor (id, name, email, telefono, direccion, negocio_id)
VALUES (2, 'Proveedor B', 'proveedorB@mail.com', '987654322', 'Calle Proveedor B',(SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));

-- Insertando reabastecimientos
INSERT INTO reabastecimiento (id, precio_total, referencia, fecha, proveedor_id, negocio_id)
VALUES (1, 100, 'REF001', '2025-03-01', (SELECT id FROM proveedor WHERE name = 'Proveedor A'), (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO reabastecimiento (id, precio_total, referencia, fecha, proveedor_id, negocio_id)
VALUES (2, 200, 'REF002', '2025-03-02', (SELECT id FROM proveedor WHERE name = 'Proveedor B'), (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));

-- Insertando lotes
INSERT INTO lote (id, cantidad, fecha_caducidad, producto_id, reabastecimiento_id)
VALUES (1, 100, '2025-05-01', (SELECT id FROM producto_inventario WHERE name = 'Harina'), (SELECT id FROM reabastecimiento WHERE referencia = 'REF001'));

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

