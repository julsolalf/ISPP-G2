-- Insertando autoridades
INSERT INTO authorities (authority) VALUES ('ROLE_ADMIN');
INSERT INTO authorities (authority) VALUES ('ROLE_USER');

-- Insertando usuarios
INSERT INTO app_user (username, password, authority_id) VALUES ('admin', 'admin123', (SELECT id FROM authorities WHERE authority = 'ROLE_ADMIN'));
INSERT INTO app_user (username, password, authority_id) VALUES ('admin2', 'admin123', (SELECT id FROM authorities WHERE authority = 'ROLE_ADMIN'));
INSERT INTO app_user (username, password, authority_id) VALUES ('juan', 'password123', (SELECT id FROM authorities WHERE authority = 'ROLE_USER'));
INSERT INTO app_user (username, password, authority_id) VALUES ('alejandro', 'password123', (SELECT id FROM authorities WHERE authority = 'ROLE_USER'));
INSERT INTO app_user (username, password, authority_id) VALUES ('antonio', 'password123', (SELECT id FROM authorities WHERE authority = 'ROLE_USER'));
INSERT INTO app_user (username, password, authority_id) VALUES ('paco', 'password123', (SELECT id FROM authorities WHERE authority = 'ROLE_USER'));
INSERT INTO app_user (username, password, authority_id) VALUES ('fernando', 'password123', (SELECT id FROM authorities WHERE authority = 'ROLE_USER'));

-- Insertando dueños
INSERT INTO dueño (first_name, last_name, email, num_telefono, token_dueño, user_id) 
VALUES ('Carlos', 'Perez', 'carlos.perez@gmail.com', '123456789', 'token123', (SELECT id FROM app_user WHERE username = 'admin'));
INSERT INTO dueño (first_name, last_name, email, num_telefono, token_dueño, user_id) 
VALUES ('Pablo', 'Rivas', 'pablo.rivas@gmail.com', '123456789', 'token123', (SELECT id FROM app_user WHERE username = 'admin2'));

-- Insertando negocios
INSERT INTO negocio (name, token_negocio, direccion, codigo_postal, ciudad, pais, dueño_id) 
VALUES ('Restaurante La Trattoria', 12345, 'Calle Falsa 123', '28001', 'Madrid', 'España', (SELECT id FROM dueño WHERE first_name = 'Carlos'));
INSERT INTO negocio (name, token_negocio, direccion, codigo_postal, ciudad, pais, dueño_id) 
VALUES ('Restaurante Burguer', 12345, 'Calle Falsa 123', '28001', 'Madrid', 'España', (SELECT id FROM dueño WHERE first_name = 'Carlos'));

-- Insertando empleados
INSERT INTO empleado (name, token_empleado, rol, negocio_id, user_id) 
VALUES ('Juan Garcia', 'tokenEmp123', 'COCINA', (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'), (SELECT id FROM app_user WHERE username = 'juan'));
INSERT INTO empleado (name, token_empleado, rol, negocio_id, user_id) 
VALUES ('Alejandro Vargas', 'tokenEmp123', 'EXTERIOR', (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'), (SELECT id FROM app_user WHERE username = 'alejandro'));
INSERT INTO empleado (name, token_empleado, rol, negocio_id, user_id) 
VALUES ('Antonio Fernández', 'tokenEmp123', 'BARRA', (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'), (SELECT id FROM app_user WHERE username = 'antonio'));
INSERT INTO empleado (name, token_empleado, rol, negocio_id, user_id) 
VALUES ('Paco Hernández', 'tokenEmp123', 'COCINA', (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'), (SELECT id FROM app_user WHERE username = 'paco'));
INSERT INTO empleado (name, token_empleado, rol, negocio_id, user_id) 
VALUES ('Fernando Pérez', 'tokenEmp123', 'EXTERIOR', (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'), (SELECT id FROM app_user WHERE username = 'fernando'));

-- Insertando mesas
INSERT INTO mesa (name, numero_asientos, negocio_id) 
VALUES ('Mesa 1', 4, (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO mesa (name, numero_asientos, negocio_id) 
VALUES ('Mesa 2', 2, (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO mesa (name, numero_asientos, negocio_id) 
VALUES ('Mesa 3', 6, (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO mesa (name, numero_asientos, negocio_id) 
VALUES ('Mesa 4', 2, (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));
INSERT INTO mesa (name, numero_asientos, negocio_id) 
VALUES ('Mesa 5', 4, (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));

-- Insertando productos de venta
INSERT INTO producto_venta (name, categoria_venta, precio_venta) 
VALUES ('Pizza Margherita', 'PLATOS', 12.50);
INSERT INTO producto_venta (name, categoria_venta, precio_venta) 
VALUES ('Coca Cola', 'BEBIDAS', 2.50);
INSERT INTO producto_venta (name, categoria_venta, precio_venta) 
VALUES ('Nestea', 'BEBIDAS', 2.50);
INSERT INTO producto_venta (name, categoria_venta, precio_venta) 
VALUES ('Fanta', 'BEBIDAS', 2.50);
INSERT INTO producto_venta (name, categoria_venta, precio_venta) 
VALUES ('Cerveza', 'BEBIDAS', 2.50);
INSERT INTO producto_venta (name, categoria_venta, precio_venta) 
VALUES ('Hamburguesa', 'PLATOS', 12.50);
INSERT INTO producto_venta (name, categoria_venta, precio_venta) 
VALUES ('Cocido', 'PLATOS', 7.50);
INSERT INTO producto_venta (name, categoria_venta, precio_venta) 
VALUES ('Pez espada', 'PLATOS', 10.50);

-- Insertando pedidos
INSERT INTO pedido (fecha, precio_total, mesa_id, empleado_id, negocio_id) 
VALUES ('2025-03-17 13:00:00', 15.00, (SELECT id FROM mesa WHERE name = 'Mesa 1'), (SELECT id FROM empleado WHERE name = 'Juan Garcia'), (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));


-- Insertando líneas de pedido
INSERT INTO linea_de_pedido (cantidad, precio_linea, pedido_id, producto_id) 
VALUES (1, 12.50, (SELECT id FROM pedido WHERE precio_total = 15.00), (SELECT id FROM producto_venta WHERE name = 'Pizza Margherita'));
INSERT INTO linea_de_pedido (cantidad, precio_linea, pedido_id, producto_id) 
VALUES (1, 2.50, (SELECT id FROM pedido WHERE precio_total = 15.00), (SELECT id FROM producto_venta WHERE name = 'Coca Cola'));

-- Insertando ingredientes
INSERT INTO ingrediente (cantidad, producto_inventario_id, producto_venta_id) 
VALUES (5, (SELECT id FROM producto_inventario WHERE nombre = 'Harina'), (SELECT id FROM producto_venta WHERE name = 'Pizza Margherita'));
INSERT INTO ingrediente (cantidad, producto_inventario_id, producto_venta_id) 
VALUES (5, (SELECT id FROM producto_inventario WHERE nombre = 'Carne Buey'), (SELECT id FROM producto_venta WHERE name = 'Hamburguesa'));

-- Insertando productos en inventario
INSERT INTO producto_inventario (nombre, categoria_inventario, precio_compra) 
VALUES ('Harina', 'COMIDA', 0.50);
INSERT INTO producto_inventario (nombre, categoria_inventario, precio_compra) 
VALUES ('Tomate', 'COMIDA', 0.30);
INSERT INTO producto_inventario (nombre, categoria_inventario, precio_compra) 
VALUES ('Carne Buey', 'COMIDA', 5.50);

-- Insertando lotes
INSERT INTO lote (cantidad, fecha_caducidad, producto_id, reabastecimiento_id) 
VALUES (100, '2025-05-01', (SELECT id FROM producto_inventario WHERE nombre = 'Harina'), (SELECT id FROM reabastecimiento WHERE referencia = 'REF001'));

-- Insertando proveedores
INSERT INTO proveedor (name, email, telefono, direccion, negocio_id) 
VALUES ('Proveedor A', 'proveedorA@mail.com', '987654321', 'Calle Proveedor 1', (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));

-- Insertando reabastecimientos
INSERT INTO reabastecimiento (fecha, precio_total, referencia, proveedor_id, negocio_id) 
VALUES ('2025-03-16', 50.00, 'REF001', (SELECT id FROM proveedor WHERE name = 'Proveedor A'), (SELECT id FROM negocio WHERE name = 'Restaurante La Trattoria'));

-- Insertando roles
INSERT INTO rol (rol) VALUES ('BARRA');
INSERT INTO rol (rol) VALUES ('COCINA');
INSERT INTO rol (rol) VALUES ('EXTERIOR');


