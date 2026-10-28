-- DAO Java: SQL parametrizado (PreparedStatement). Rutinas conservadas por compatibilidad.

CREATE DATABASE IF NOT EXISTS RinconcitoSatipenno;
USE RinconcitoSatipenno;

SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 1. SENTENCIAS DROP TABLE
-- ============================================
DROP TABLE IF EXISTS detalle_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS cuenta_consumo;
DROP TABLE IF EXISTS reserva;
DROP TABLE IF EXISTS datos_contacto;
DROP TABLE IF EXISTS plato;
DROP TABLE IF EXISTS receta_insumo;
DROP TABLE IF EXISTS receta;
DROP TABLE IF EXISTS movimiento_inventario;
DROP TABLE IF EXISTS insumo;
DROP TABLE IF EXISTS mesa;
DROP TABLE IF EXISTS mozo;
DROP TABLE IF EXISTS administrador;
DROP TABLE IF EXISTS cuenta_acceso;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 2. SENTENCIAS CREATE TABLE
-- ============================================

-- 1. Tabla: CuentaAcceso
CREATE TABLE cuenta_acceso (
                               id_cuenta_acceso INT NOT NULL AUTO_INCREMENT,
                               email VARCHAR(100) NOT NULL,
                               contrasenia VARCHAR(255) NOT NULL,
                               estado ENUM('ACTIVO', 'INACTIVO', 'CON_LICENCIA') NOT NULL DEFAULT 'ACTIVO',
                               PRIMARY KEY (id_cuenta_acceso),
                               CONSTRAINT UQ_cuentaacceso_email UNIQUE (email)
);

-- 2. Tabla: Administrador
CREATE TABLE administrador (
                               id_administrador INT NOT NULL AUTO_INCREMENT,
                               nombre VARCHAR(100) NOT NULL,
                               telefono VARCHAR(15) NOT NULL,
                               fecha_contratacion DATE NOT NULL,
                               id_cuenta_acceso INT NOT NULL,
                               PRIMARY KEY (id_administrador),
                               CONSTRAINT UQ_administrador_cuentaacceso UNIQUE (id_cuenta_acceso),
                               CONSTRAINT FK_administrador_cuentaacceso
                                   FOREIGN KEY (id_cuenta_acceso) REFERENCES cuenta_acceso(id_cuenta_acceso)
);

-- 3. Tabla: Mozo
CREATE TABLE mozo (
                      id_mozo INT NOT NULL AUTO_INCREMENT,
                      nombre VARCHAR(100) NOT NULL,
                      telefono VARCHAR(15) NOT NULL,
                      fecha_contratacion DATE NOT NULL,
                      turno ENUM('MANANA', 'TARDE', 'NOCHE') NOT NULL,
                      zona ENUM('SALON_PRINCIPAL', 'TERRAZA') NOT NULL,
                      id_cuenta_acceso INT NOT NULL,
                      PRIMARY KEY (id_mozo),
                      CONSTRAINT UQ_mozo_cuentaacceso UNIQUE (id_cuenta_acceso),
                      CONSTRAINT FK_mozo_cuentaacceso
                          FOREIGN KEY (id_cuenta_acceso) REFERENCES cuenta_acceso(id_cuenta_acceso)
);

-- 4. Tabla: Mesa
CREATE TABLE mesa (
                      id_mesa INT NOT NULL AUTO_INCREMENT,
                      numero INT NOT NULL,
                      capacidad INT NOT NULL,
                      zona ENUM('SALON_PRINCIPAL', 'TERRAZA') NOT NULL,
                      estado ENUM('LIBRE', 'OCUPADA', 'RESERVADA') NOT NULL DEFAULT 'LIBRE',
                      PRIMARY KEY (id_mesa),
                      CONSTRAINT UQ_mesa_numero UNIQUE (numero),
                      CONSTRAINT CK_mesa_numero CHECK (numero > 0),
                      CONSTRAINT CK_mesa_capacidad CHECK (capacidad > 0)
);

-- 5. Tabla: Insumo
CREATE TABLE insumo (
                        id_insumo INT NOT NULL AUTO_INCREMENT,
                        nombre VARCHAR(100) NOT NULL,
                        unidad_medida VARCHAR(20) NOT NULL,
                        stock_actual DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                        stock_minimo DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                        PRIMARY KEY (id_insumo),
                        CONSTRAINT CK_insumo_stock_actual CHECK (stock_actual >= 0),
                        CONSTRAINT CK_insumo_stock_minimo CHECK (stock_minimo >= 0)
);

-- 6. Tabla: MovimientoInventario
CREATE TABLE movimiento_inventario (
                                       id_movimiento INT NOT NULL AUTO_INCREMENT,
                                       id_insumo INT NOT NULL,
                                       tipo_movimiento ENUM('ENTRADA', 'SALIDA') NOT NULL,
                                       cantidad DECIMAL(10,2) NOT NULL,
                                       fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       PRIMARY KEY (id_movimiento),
                                       CONSTRAINT FK_movimiento_insumo
                                           FOREIGN KEY (id_insumo) REFERENCES insumo(id_insumo),
                                       CONSTRAINT CK_movimiento_cantidad CHECK (cantidad > 0)
);

-- 7. Tabla: Receta
CREATE TABLE receta (
                        id_receta INT NOT NULL AUTO_INCREMENT,
                        descripcion VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id_receta)
);

-- 8. Tabla: RecetaInsumo
CREATE TABLE receta_insumo (
                               id_receta INT NOT NULL,
                               id_insumo INT NOT NULL,
                               cantidad_requerida DECIMAL(10,2) NOT NULL,
                               PRIMARY KEY (id_receta, id_insumo),
                               CONSTRAINT FK_recetainsumo_receta
                                   FOREIGN KEY (id_receta) REFERENCES receta(id_receta),
                               CONSTRAINT FK_recetainsumo_insumo
                                   FOREIGN KEY (id_insumo) REFERENCES insumo(id_insumo),
                               CONSTRAINT CK_recetainsumo_cantidad CHECK (cantidad_requerida > 0)
);

-- 9. Tabla: Plato
CREATE TABLE plato (
                       id_plato INT NOT NULL AUTO_INCREMENT,
                       nombre VARCHAR(100) NOT NULL,
                       precio DECIMAL(10,2) NOT NULL,
                       categoria ENUM('ENTRADA', 'PLATO_FONDO', 'POSTRE', 'BEBIDA') NOT NULL,
                       descripcion VARCHAR(255) NOT NULL,
                       id_receta INT NULL,
                       PRIMARY KEY (id_plato),
                       CONSTRAINT FK_plato_receta
                           FOREIGN KEY (id_receta) REFERENCES receta(id_receta),
                       CONSTRAINT CK_plato_precio CHECK (precio >= 0)
);

-- 10. Tabla: DatosContacto (Mapeo de la clase DatosContacto)
CREATE TABLE datos_contacto (
                                id_contacto INT NOT NULL AUTO_INCREMENT,
                                nombre VARCHAR(100) NOT NULL,
                                telefono VARCHAR(15) NOT NULL,
                                correo VARCHAR(100) NOT NULL,
                                PRIMARY KEY (id_contacto),
                                INDEX idx_contacto_correo (correo)
);

-- 11. Tabla: Reserva (Relacionada con DatosContacto mediante FK)
CREATE TABLE reserva (
                         id_reserva INT NOT NULL AUTO_INCREMENT,
                         fecha DATE NOT NULL,
                         hora_inicio TIME NOT NULL,
                         hora_fin TIME NOT NULL,
                         cantidad_personas INT NOT NULL,
                         estado ENUM('PENDIENTE', 'CONFIRMADA', 'CANCELADA', 'COMPLETADA') NOT NULL DEFAULT 'PENDIENTE',
                         codigo_acceso VARCHAR(64) NOT NULL,
                         id_mesa INT NOT NULL,
                         id_contacto INT NOT NULL,
                         PRIMARY KEY (id_reserva),
                         CONSTRAINT UQ_reserva_codigoacceso UNIQUE (codigo_acceso),
                         CONSTRAINT FK_reserva_mesa
                             FOREIGN KEY (id_mesa) REFERENCES mesa(id_mesa),
                         CONSTRAINT FK_reserva_datoscontacto
                             FOREIGN KEY (id_contacto) REFERENCES datos_contacto(id_contacto),
                         CONSTRAINT CK_reserva_personas CHECK (cantidad_personas > 0),
                         CONSTRAINT CK_reserva_horas CHECK (hora_fin > hora_inicio),
                         CONSTRAINT CK_reserva_maxduracion
                             CHECK (TIME_TO_SEC(hora_fin) - TIME_TO_SEC(hora_inicio) <= 7200),
                         INDEX idx_reserva_disponibilidad (id_mesa, fecha, estado, hora_inicio, hora_fin)
);

-- 12. Tabla: CuentaConsumo
CREATE TABLE cuenta_consumo (
                                id_cuenta INT NOT NULL AUTO_INCREMENT,
                                fecha_apertura DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                fecha_cierre DATETIME NULL,
                                fecha_pago DATETIME NULL,
                                estado ENUM('ABIERTA', 'PAGADA', 'CERRADA') NOT NULL DEFAULT 'ABIERTA',
                                monto_total_pagar DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                                enlace_pago CHAR(36) NULL,
                                id_mesa INT NOT NULL,
                                id_mozo INT NOT NULL,
                                id_reserva INT NULL,
                                PRIMARY KEY (id_cuenta),
                                CONSTRAINT UQ_cuentaconsumo_enlacepago UNIQUE (enlace_pago),
                                CONSTRAINT FK_cuentaconsumo_mesa
                                    FOREIGN KEY (id_mesa) REFERENCES mesa(id_mesa),
                                CONSTRAINT FK_cuentaconsumo_mozo
                                    FOREIGN KEY (id_mozo) REFERENCES mozo(id_mozo),
                                CONSTRAINT FK_cuentaconsumo_reserva
                                    FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva),
                                CONSTRAINT CK_cuentaconsumo_monto
                                    CHECK (monto_total_pagar >= 0),
                                CONSTRAINT CK_cuentaconsumo_pago
                                    CHECK (fecha_pago IS NULL OR fecha_pago >= fecha_apertura),
                                CONSTRAINT CK_cuentaconsumo_cierre
                                    CHECK (fecha_cierre IS NULL OR fecha_cierre >= fecha_apertura),
                                CONSTRAINT CK_cuentaconsumo_cierre_pago
                                    CHECK (
                                        fecha_cierre IS NULL
                                            OR fecha_pago IS NULL
                                            OR fecha_cierre >= fecha_pago
                                        ),
                                CONSTRAINT CK_cuentaconsumo_estado_fechas
                                    CHECK (
                                        (estado = 'ABIERTA'
                                            AND fecha_pago IS NULL
                                            AND fecha_cierre IS NULL)
                                            OR
                                        (estado = 'PAGADA'
                                            AND fecha_pago IS NOT NULL
                                            AND fecha_cierre IS NULL)
                                            OR
                                        (estado = 'CERRADA'
                                            AND fecha_pago IS NOT NULL
                                            AND fecha_cierre IS NOT NULL)
                                        )
);

-- 13. Tabla: Pedido
CREATE TABLE pedido (
                        id_pedido INT NOT NULL AUTO_INCREMENT,
                        fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        id_cuenta_consumo INT NOT NULL,
                        PRIMARY KEY (id_pedido),
                        CONSTRAINT FK_pedido_cuentaconsumo
                            FOREIGN KEY (id_cuenta_consumo) REFERENCES cuenta_consumo(id_cuenta)
);

-- 14. Tabla: DetallePedido
CREATE TABLE detalle_pedido (
                                id_detalle INT NOT NULL AUTO_INCREMENT,
                                cantidad_platos INT NOT NULL,
                                precio_unitario DECIMAL(10,2) NOT NULL,
                                subtotal DECIMAL(10,2) NOT NULL,
                                id_plato INT NOT NULL,
                                id_pedido INT NOT NULL,
                                PRIMARY KEY (id_detalle),
                                CONSTRAINT FK_detalle_plato
                                    FOREIGN KEY (id_plato) REFERENCES plato(id_plato),
                                CONSTRAINT FK_detalle_pedido
                                    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
                                CONSTRAINT CK_detalle_cantidad CHECK (cantidad_platos > 0),
                                CONSTRAINT CK_detalle_preciounitario CHECK (precio_unitario >= 0),
                                CONSTRAINT CK_detalle_subtotal CHECK (subtotal = cantidad_platos * precio_unitario)
);

-- ============================================
-- 3. SENTENCIAS INSERT
-- ============================================

-- 1. Cuentas de acceso
INSERT INTO cuenta_acceso (email, contrasenia, estado) VALUES
                                                           ('brandon.hidalgo@rinconsatipeno.pe', 'hash_secure_pass_1', 'ACTIVO'),
                                                           ('leonardo.torres@rinconsatipeno.pe', 'hash_secure_pass_2', 'ACTIVO'),
                                                           ('carlos.mendoza@rinconsatipeno.pe', 'hash_secure_pass_3', 'ACTIVO'),
                                                           ('ana.ramos@rinconsatipeno.pe', 'hash_secure_pass_4', 'ACTIVO'),
                                                           ('jorge.flores@rinconsatipeno.pe', 'hash_secure_pass_5', 'INACTIVO');

-- 2. Administradores
INSERT INTO administrador (nombre, telefono, fecha_contratacion, id_cuenta_acceso) VALUES
                                                                                       ('Brandon Hidalgo', '987654321', '2026-01-15', 1),
                                                                                       ('Leonardo Torres', '912345678', '2026-02-01', 2);

-- 3. Mozos
INSERT INTO mozo (nombre, telefono, fecha_contratacion, turno, zona, id_cuenta_acceso) VALUES
                                                                                           ('Carlos Mendoza', '955443322', '2026-03-10', 'MANANA', 'SALON_PRINCIPAL', 3),
                                                                                           ('Ana Ramos', '966778899', '2026-03-12', 'NOCHE', 'TERRAZA', 4),
                                                                                           ('Jorge Flores', '944332211', '2026-04-01', 'TARDE', 'SALON_PRINCIPAL', 5);

-- 4. Mesas
INSERT INTO mesa (numero, capacidad, zona, estado) VALUES
    (1, 4, 'SALON_PRINCIPAL', 'LIBRE'),
    (2, 2, 'SALON_PRINCIPAL', 'OCUPADA'),
    (3, 6, 'TERRAZA', 'LIBRE'),
    (4, 4, 'TERRAZA', 'OCUPADA'),
    (5, 2, 'SALON_PRINCIPAL', 'OCUPADA'),
    (6, 4, 'TERRAZA', 'LIBRE'),
    (7, 6, 'SALON_PRINCIPAL', 'RESERVADA'),
    (8, 4, 'SALON_PRINCIPAL', 'OCUPADA');

-- 5. Insumos
INSERT INTO insumo (nombre, unidad_medida, stock_actual, stock_minimo) VALUES
                                                                           ('Cecina', 'KG', 15.00, 3.00),
                                                                           ('Carne de Res', 'KG', 50.00, 10.00),
                                                                           ('Papas', 'KG', 99.40, 20.00),
                                                                           ('Arroz', 'KG', 80.00, 15.00);

-- 6. Movimientos de inventario
INSERT INTO movimiento_inventario (id_insumo, tipo_movimiento, cantidad) VALUES
                                                                             (1, 'ENTRADA', 15.00),
                                                                             (2, 'ENTRADA', 50.00),
                                                                             (3, 'ENTRADA', 100.00),
                                                                             (4, 'ENTRADA', 80.00),
                                                                             (3, 'SALIDA', 0.60);

-- 7. Recetas
INSERT INTO receta (descripcion) VALUES
                                     ('Receta para Tacacho con Cecina'),
                                     ('Receta para Lomo Saltado'),
                                     ('Receta para Papa a la Huancaína');

-- 8. RecetaInsumo
INSERT INTO receta_insumo (id_receta, id_insumo, cantidad_requerida) VALUES
                                                                         (1, 1, 0.25),
                                                                         (2, 2, 0.25),
                                                                         (2, 3, 0.30),
                                                                         (2, 4, 0.15),
                                                                         (3, 3, 0.20);

-- 9. Platos
-- Los IDs 1 a 16 corresponden a CartaRestaurante.cs del frontend.
-- Se conserva Tacacho con cecina como producto adicional (ID 17).
-- Las bebidas y otros productos sin receta usan id_receta = NULL.
INSERT INTO plato (id_plato, nombre, precio, categoria, descripcion, id_receta) VALUES
    (1, 'Tequeños de queso (6 unidades)', 18.00, 'ENTRADA', 'Seis tequeños dorados, rellenos de queso y acompañados de salsa de palta.', NULL),
    (2, 'Papa a la huancaína', 16.00, 'ENTRADA', 'Papas tiernas cubiertas con salsa huancaína, huevo y aceituna.', 3),
    (3, 'Ensalada fresca del huerto', 16.00, 'ENTRADA', 'Lechuga, tomate, pepino y vegetales frescos de temporada.', NULL),
    (4, 'Causa limeña de pollo', 20.00, 'ENTRADA', 'Capas de papa amarilla, pollo deshilachado y palta.', NULL),
    (5, 'Arroz con pollo', 25.00, 'PLATO_FONDO', 'Arroz verde con culantro, presa de pollo y salsa criolla.', NULL),
    (6, 'Lomo saltado — especialidad de la casa', 39.00, 'PLATO_FONDO', 'Lomo de res salteado al wok con cebolla, tomate, papas fritas y arroz.', 2),
    (7, 'Tallarines verdes con champiñones', 28.00, 'PLATO_FONDO', 'Pasta en salsa de albahaca y espinaca, con champiñones salteados.', NULL),
    (8, 'Juane de gallina', 28.00, 'PLATO_FONDO', 'Arroz amazónico especiado, gallina y huevo cocidos en hoja de bijao.', NULL),
    (9, 'Arroz chaufa de pollo', 27.00, 'PLATO_FONDO', 'Arroz salteado con pollo, huevo, cebolla china y sillao.', NULL),
    (10, 'Suspiro a la limeña — postre de la casa', 14.00, 'POSTRE', 'Crema suave de manjar con merengue y un toque de canela.', NULL),
    (11, 'Mazamorra morada', 12.00, 'POSTRE', 'Postre peruano de maíz morado y frutas con canela.', NULL),
    (12, 'Torta de chocolate', 15.00, 'POSTRE', 'Porción de bizcocho de chocolate húmedo con cobertura cremosa.', NULL),
    (13, 'Jugo de maracuyá', 10.00, 'BEBIDA', 'Jugo natural de maracuyá servido frío.', NULL),
    (14, 'Limonada con hierbabuena', 8.00, 'BEBIDA', 'Limones frescos, hierbabuena y hielo.', NULL),
    (15, 'Chicha morada', 9.00, 'BEBIDA', 'Bebida casera de maíz morado con frutas y especias.', NULL),
    (16, 'Batido de lúcuma', 13.00, 'BEBIDA', 'Batido cremoso de lúcuma preparado con leche.', NULL),
    (17, 'Tacacho con cecina', 32.00, 'PLATO_FONDO', 'Tacacho tradicional con cecina de la selva.', 1);

-- 10. DatosContacto
INSERT INTO datos_contacto (nombre, telefono, correo) VALUES
                                                          ('Ana Torres', '987654321', 'ana.torres@example.com'),
                                                          ('Luis Ramirez', '912345678', 'luis.ramirez@example.com'),
                                                          ('Maria Gutierrez', '998877665', 'maria.gutierrez@example.com'),
                                                          ('Jorge Salazar', '955443322', 'jorge.salazar@example.com');

-- 11. Reservas
INSERT INTO reserva (fecha, hora_inicio, hora_fin, cantidad_personas, estado, codigo_acceso, id_mesa, id_contacto) VALUES
                                                                                                                       ('2026-09-28', '13:00:00', '15:00:00', 4, 'CONFIRMADA', UUID(), 3, 1),
                                                                                                                       ('2026-09-27', '13:00:00', '14:30:00', 2, 'COMPLETADA', UUID(), 4, 2),
                                                                                                                       ('2026-09-29', '20:00:00', '21:30:00', 2, 'PENDIENTE', UUID(), 1, 3),
                                                                                                                       ('2026-09-30', '19:00:00', '21:00:00', 5, 'CANCELADA', UUID(), 3, 4);

-- 12. Cuentas de consumo
INSERT INTO cuenta_consumo (fecha_apertura, fecha_cierre, fecha_pago, estado, monto_total_pagar, enlace_pago, id_mesa, id_mozo, id_reserva) VALUES
                                                                                                                                                ('2026-09-27 12:30:00', NULL, NULL, 'ABIERTA', 32.00, NULL, 2, 1, NULL),
                                                                                                                                                ('2026-09-27 13:00:00', NULL, '2026-09-27 14:10:00', 'PAGADA', 78.00, UUID(), 4, 2, 2);

-- 13. Pedidos
INSERT INTO pedido (fecha_hora, id_cuenta_consumo) VALUES
                                                       ('2026-09-27 12:35:00', 1),
                                                       ('2026-09-27 13:05:00', 2);

-- 14. Detalles de pedido
INSERT INTO detalle_pedido (cantidad_platos, precio_unitario, subtotal, id_plato, id_pedido) VALUES
                                                                                                 (1, 32.00, 32.00, 17, 1);

INSERT INTO detalle_pedido (cantidad_platos, precio_unitario, subtotal, id_plato, id_pedido) VALUES
                                                                                                 (2, 39.00, 78.00, 6, 2);

-- ============================================================
-- SCRIPT CONSOLIDADO DE PROCEDIMIENTOS ALMACENADOS
-- Base de Datos: RinconcitoSatipenno
-- ============================================================

USE RinconcitoSatipenno;

DELIMITER $$

-- ============================================================
-- 1. PROCEDIMIENTOS: CUENTA_ACCESO
-- ============================================================
DROP PROCEDURE IF EXISTS INSERTAR_CUENTA_ACCESO$$
CREATE PROCEDURE INSERTAR_CUENTA_ACCESO(
    OUT p_id_cuenta_acceso INT,
    IN p_email VARCHAR(150),
    IN p_contrasenia VARCHAR(255),
    IN p_estado VARCHAR(20)
)
BEGIN
INSERT INTO cuenta_acceso (email, contrasenia, estado)
VALUES (p_email, p_contrasenia, p_estado);

SET p_id_cuenta_acceso = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS ACTUALIZAR_CUENTA_ACCESO$$
CREATE PROCEDURE ACTUALIZAR_CUENTA_ACCESO(
    IN p_id_cuenta_acceso INT,
    IN p_email VARCHAR(150),
    IN p_contrasenia VARCHAR(255),
    IN p_estado VARCHAR(20)
)
BEGIN
UPDATE cuenta_acceso
SET email = p_email,
    contrasenia = p_contrasenia,
    estado = p_estado
WHERE id_cuenta_acceso = p_id_cuenta_acceso;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_CUENTA_ACCESO$$
CREATE PROCEDURE ELIMINAR_CUENTA_ACCESO(
    IN p_id_cuenta_acceso INT
)
BEGIN
DELETE FROM cuenta_acceso
WHERE id_cuenta_acceso = p_id_cuenta_acceso;
END$$

DROP PROCEDURE IF EXISTS OBTENER_CUENTA_ACCESO_POR_ID$$
CREATE PROCEDURE OBTENER_CUENTA_ACCESO_POR_ID(
    IN p_id_cuenta_acceso INT
)
BEGIN
SELECT id_cuenta_acceso, email, contrasenia, estado
FROM cuenta_acceso
WHERE id_cuenta_acceso = p_id_cuenta_acceso;
END$$

DROP PROCEDURE IF EXISTS OBTENER_CUENTA_ACCESO_POR_EMAIL$$
CREATE PROCEDURE OBTENER_CUENTA_ACCESO_POR_EMAIL(
    IN p_email VARCHAR(150)
)
BEGIN
SELECT id_cuenta_acceso, email, contrasenia, estado
FROM cuenta_acceso
WHERE email = p_email;
END$$

DROP PROCEDURE IF EXISTS LISTAR_CUENTAS_ACCESO$$
CREATE PROCEDURE LISTAR_CUENTAS_ACCESO()
BEGIN
SELECT id_cuenta_acceso, email, contrasenia, estado
FROM cuenta_acceso;
END$$

-- ============================================================
-- 2. PROCEDIMIENTOS: ADMINISTRADOR
-- ============================================================
DROP PROCEDURE IF EXISTS INSERTAR_ADMINISTRADOR$$
CREATE PROCEDURE INSERTAR_ADMINISTRADOR(
    OUT p_id_administrador INT,
    IN p_nombre VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_fecha_contratacion DATE,
    IN p_id_cuenta_acceso INT
)
BEGIN
INSERT INTO administrador (nombre, telefono, fecha_contratacion, id_cuenta_acceso)
VALUES (p_nombre, p_telefono, p_fecha_contratacion, p_id_cuenta_acceso);

SET p_id_administrador = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS ACTUALIZAR_ADMINISTRADOR$$
CREATE PROCEDURE ACTUALIZAR_ADMINISTRADOR(
    IN p_id_administrador INT,
    IN p_nombre VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_fecha_contratacion DATE
)
BEGIN
UPDATE administrador
SET nombre = p_nombre,
    telefono = p_telefono,
    fecha_contratacion = p_fecha_contratacion
WHERE id_administrador = p_id_administrador;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_ADMINISTRADOR$$
CREATE PROCEDURE ELIMINAR_ADMINISTRADOR(
    IN p_id_administrador INT
)
BEGIN
DELETE FROM administrador
WHERE id_administrador = p_id_administrador;
END$$

DROP PROCEDURE IF EXISTS OBTENER_ADMINISTRADOR_POR_ID$$
CREATE PROCEDURE OBTENER_ADMINISTRADOR_POR_ID(
    IN p_id_administrador INT
)
BEGIN
SELECT id_administrador, nombre, telefono, fecha_contratacion, id_cuenta_acceso
FROM administrador
WHERE id_administrador = p_id_administrador;
END$$

DROP PROCEDURE IF EXISTS LISTAR_ADMINISTRADORES$$
CREATE PROCEDURE LISTAR_ADMINISTRADORES()
BEGIN
SELECT id_administrador, nombre, telefono, fecha_contratacion, id_cuenta_acceso
FROM administrador;
END$$

-- ============================================================
-- 3. PROCEDIMIENTOS: MOZO
-- ============================================================
DROP PROCEDURE IF EXISTS INSERTAR_MOZO$$
CREATE PROCEDURE INSERTAR_MOZO(
    OUT p_id_mozo INT,
    IN p_nombre VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_fecha_contratacion DATE,
    IN p_turno VARCHAR(20),
    IN p_zona VARCHAR(50),
    IN p_id_cuenta_acceso INT
)
BEGIN
INSERT INTO mozo (nombre, telefono, fecha_contratacion, turno, zona, id_cuenta_acceso)
VALUES (p_nombre, p_telefono, p_fecha_contratacion, p_turno, p_zona, p_id_cuenta_acceso);

SET p_id_mozo = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS ACTUALIZAR_MOZO$$
CREATE PROCEDURE ACTUALIZAR_MOZO(
    IN p_id_mozo INT,
    IN p_nombre VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_fecha_contratacion DATE,
    IN p_turno VARCHAR(20),
    IN p_zona VARCHAR(50)
)
BEGIN
UPDATE mozo
SET nombre = p_nombre,
    telefono = p_telefono,
    fecha_contratacion = p_fecha_contratacion,
    turno = p_turno,
    zona = p_zona
WHERE id_mozo = p_id_mozo;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_MOZO$$
CREATE PROCEDURE ELIMINAR_MOZO(
    IN p_id_mozo INT
)
BEGIN
DELETE FROM mozo
WHERE id_mozo = p_id_mozo;
END$$

DROP PROCEDURE IF EXISTS OBTENER_MOZO_POR_ID$$
CREATE PROCEDURE OBTENER_MOZO_POR_ID(
    IN p_id_mozo INT
)
BEGIN
SELECT id_mozo, nombre, telefono, fecha_contratacion, turno, zona, id_cuenta_acceso
FROM mozo
WHERE id_mozo = p_id_mozo;
END$$

DROP PROCEDURE IF EXISTS LISTAR_MOZOS$$
CREATE PROCEDURE LISTAR_MOZOS()
BEGIN
SELECT id_mozo, nombre, telefono, fecha_contratacion, turno, zona, id_cuenta_acceso
FROM mozo;
END$$

-- ============================================================
-- 4. PROCEDIMIENTOS: INSUMO
-- ============================================================
DROP PROCEDURE IF EXISTS insertar_insumo$$
CREATE PROCEDURE insertar_insumo(
    IN p_nombre VARCHAR(100),
    IN p_unidad_medida VARCHAR(20),
    IN p_stock_actual DECIMAL(10,2),
    IN p_stock_minimo DECIMAL(10,2),
    OUT p_id_insumo INT
)
BEGIN
INSERT INTO insumo(nombre, unidad_medida, stock_actual, stock_minimo)
VALUES(p_nombre, p_unidad_medida, p_stock_actual, p_stock_minimo);
SET p_id_insumo = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS modificar_insumo$$
CREATE PROCEDURE modificar_insumo(
    IN p_id_insumo INT,
    IN p_nombre VARCHAR(100),
    IN p_unidad_medida VARCHAR(20),
    IN p_stock_actual DECIMAL(10,2),
    IN p_stock_minimo DECIMAL(10,2)
)
BEGIN
UPDATE insumo
SET nombre = p_nombre,
    unidad_medida = p_unidad_medida,
    stock_actual = p_stock_actual,
    stock_minimo = p_stock_minimo
WHERE id_insumo = p_id_insumo;
END$$

DROP PROCEDURE IF EXISTS eliminar_insumo$$
CREATE PROCEDURE eliminar_insumo(IN p_id_insumo INT)
BEGIN
DELETE FROM insumo WHERE id_insumo = p_id_insumo;
END$$

DROP PROCEDURE IF EXISTS buscar_insumo_por_id$$
CREATE PROCEDURE buscar_insumo_por_id(IN p_id_insumo INT)
BEGIN
SELECT * FROM insumo WHERE id_insumo = p_id_insumo;
END$$

DROP PROCEDURE IF EXISTS listar_insumos$$
CREATE PROCEDURE listar_insumos()
BEGIN
SELECT * FROM insumo;
END$$

-- ============================================================
-- 5. PROCEDIMIENTOS: MOVIMIENTO_INVENTARIO
-- ============================================================
DROP PROCEDURE IF EXISTS insertar_movimiento_inventario$$
CREATE PROCEDURE insertar_movimiento_inventario(
    IN p_id_insumo INT,
    IN p_tipo_movimiento ENUM('ENTRADA', 'SALIDA'),
    IN p_cantidad DECIMAL(10,2),
    OUT p_id_movimiento INT
        )
BEGIN
INSERT INTO movimiento_inventario(id_insumo, tipo_movimiento, cantidad)
VALUES (p_id_insumo, p_tipo_movimiento, p_cantidad);
SET p_id_movimiento = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS modificar_movimiento_inventario$$
CREATE PROCEDURE modificar_movimiento_inventario(
    IN p_id_movimiento INT,
    IN p_id_insumo INT,
    IN p_tipo_movimiento ENUM('ENTRADA', 'SALIDA'),
    IN p_cantidad DECIMAL(10,2)
        )
BEGIN
UPDATE movimiento_inventario
SET id_insumo = p_id_insumo,
    tipo_movimiento = p_tipo_movimiento,
    cantidad = p_cantidad
WHERE id_movimiento = p_id_movimiento;
END$$

DROP PROCEDURE IF EXISTS eliminar_movimiento_inventario$$
CREATE PROCEDURE eliminar_movimiento_inventario(IN p_id_movimiento INT)
BEGIN
DELETE FROM movimiento_inventario WHERE id_movimiento = p_id_movimiento;
END$$

DROP PROCEDURE IF EXISTS buscar_movimiento_inventario_por_id$$
CREATE PROCEDURE buscar_movimiento_inventario_por_id(IN p_id_movimiento INT)
BEGIN
SELECT * FROM movimiento_inventario WHERE id_movimiento = p_id_movimiento;
END$$

DROP PROCEDURE IF EXISTS listar_movimientos_inventario$$
CREATE PROCEDURE listar_movimientos_inventario()
BEGIN
SELECT * FROM movimiento_inventario;
END$$

-- ============================================================
-- 6. PROCEDIMIENTOS: RECETA
-- ============================================================
DROP PROCEDURE IF EXISTS insertar_receta$$
CREATE PROCEDURE insertar_receta(
    IN p_descripcion VARCHAR(255),
    OUT p_id INT
)
BEGIN
INSERT INTO receta(descripcion) VALUES(p_descripcion);
SET p_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS modificar_receta$$
CREATE PROCEDURE modificar_receta(
    IN p_descripcion VARCHAR(255),
    IN p_id INT
)
BEGIN
UPDATE receta
SET descripcion = p_descripcion
WHERE id_receta = p_id;
END$$

DROP PROCEDURE IF EXISTS eliminar_receta$$
CREATE PROCEDURE eliminar_receta(IN p_id INT)
BEGIN
DELETE FROM receta WHERE id_receta = p_id;
END$$

DROP PROCEDURE IF EXISTS buscar_receta_por_id$$
CREATE PROCEDURE buscar_receta_por_id(IN p_id INT)
BEGIN
SELECT * FROM receta WHERE id_receta = p_id;
END$$

DROP PROCEDURE IF EXISTS listar_recetas$$
CREATE PROCEDURE listar_recetas()
BEGIN
SELECT * FROM receta;
END$$

-- ============================================================
-- 7. PROCEDIMIENTOS: PLATO
-- ============================================================
DROP PROCEDURE IF EXISTS insertar_plato$$
CREATE PROCEDURE insertar_plato(
    IN p_nombre VARCHAR(100),
    IN p_precio DECIMAL(10,2),
    IN p_categoria VARCHAR(20),
    IN p_descripcion VARCHAR(255),
    IN p_id_receta INT,
    OUT p_id INT
)
BEGIN
INSERT INTO plato(nombre, precio, categoria, descripcion, id_receta)
VALUES(p_nombre, p_precio, p_categoria, p_descripcion, p_id_receta);
SET p_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS modificar_plato$$
CREATE PROCEDURE modificar_plato(
    IN p_nombre VARCHAR(100),
    IN p_precio DECIMAL(10,2),
    IN p_categoria VARCHAR(20),
    IN p_descripcion VARCHAR(255),
    IN p_id_receta INT,
    IN p_id INT
)
BEGIN
UPDATE plato
SET nombre = p_nombre,
    precio = p_precio,
    categoria = p_categoria,
    descripcion = p_descripcion,
    id_receta = p_id_receta
WHERE id_plato = p_id;
END$$

DROP PROCEDURE IF EXISTS eliminar_plato$$
CREATE PROCEDURE eliminar_plato(IN p_id INT)
BEGIN
DELETE FROM plato WHERE id_plato = p_id;
END$$

DROP PROCEDURE IF EXISTS buscar_plato_por_id$$
CREATE PROCEDURE buscar_plato_por_id(IN p_id INT)
BEGIN
SELECT * FROM plato WHERE id_plato = p_id;
END$$

DROP PROCEDURE IF EXISTS listar_platos$$
CREATE PROCEDURE listar_platos()
BEGIN
SELECT * FROM plato;
END$$

-- ============================================================
-- 8. PROCEDIMIENTOS: RECETA_INSUMO
-- ============================================================
DROP PROCEDURE IF EXISTS insertar_receta_insumo$$
CREATE PROCEDURE insertar_receta_insumo(
    IN p_id_receta INT,
    IN p_id_insumo INT,
    IN p_cantidad_requerida DECIMAL(10,2)
)
BEGIN
INSERT INTO receta_insumo (id_receta, id_insumo, cantidad_requerida)
VALUES (p_id_receta, p_id_insumo, p_cantidad_requerida);
END$$

DROP PROCEDURE IF EXISTS modificar_receta_insumo$$
CREATE PROCEDURE modificar_receta_insumo(
    IN p_id_receta INT,
    IN p_id_insumo INT,
    IN p_cantidad_requerida DECIMAL(10,2)
)
BEGIN
UPDATE receta_insumo
SET cantidad_requerida = p_cantidad_requerida
WHERE id_receta = p_id_receta AND id_insumo = p_id_insumo;
END$$

DROP PROCEDURE IF EXISTS eliminar_receta_insumo$$
CREATE PROCEDURE eliminar_receta_insumo(
    IN p_id_receta INT,
    IN p_id_insumo INT
)
BEGIN
DELETE FROM receta_insumo
WHERE id_receta = p_id_receta AND id_insumo = p_id_insumo;
END$$

DROP PROCEDURE IF EXISTS buscar_receta_insumo_por_ids$$
CREATE PROCEDURE buscar_receta_insumo_por_ids(
    IN p_id_receta INT,
    IN p_id_insumo INT
)
BEGIN
SELECT id_receta, id_insumo, cantidad_requerida
FROM receta_insumo
WHERE id_receta = p_id_receta AND id_insumo = p_id_insumo;
END$$

DROP PROCEDURE IF EXISTS listar_recetas_insumos$$
CREATE PROCEDURE listar_recetas_insumos()
BEGIN
SELECT id_receta, id_insumo, cantidad_requerida
FROM receta_insumo
ORDER BY id_receta, id_insumo;
END$$

DROP PROCEDURE IF EXISTS listar_insumos_por_receta$$
CREATE PROCEDURE listar_insumos_por_receta(
    IN p_id_receta INT
)
BEGIN
SELECT id_receta, id_insumo, cantidad_requerida
FROM receta_insumo
WHERE id_receta = p_id_receta
ORDER BY id_insumo;
END$$

-- ============================================================
-- 9. PROCEDIMIENTOS: MESA
-- ============================================================
DROP PROCEDURE IF EXISTS insertar_mesa$$
CREATE PROCEDURE insertar_mesa(
    IN p_numero INT,
    IN p_capacidad INT,
    IN p_zona VARCHAR(20),
    IN p_estado VARCHAR(10),
    OUT p_id INT
)
BEGIN
INSERT INTO mesa(numero, capacidad, zona, estado)
VALUES(p_numero, p_capacidad, p_zona, p_estado);
SET p_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS modificar_mesa$$
CREATE PROCEDURE modificar_mesa(
    IN p_numero INT,
    IN p_capacidad INT,
    IN p_zona VARCHAR(20),
    IN p_estado VARCHAR(10),
    IN p_id INT
)
BEGIN
UPDATE mesa
SET numero = p_numero,
    capacidad = p_capacidad,
    zona = p_zona,
    estado = p_estado
WHERE id_mesa = p_id;
END$$

DROP PROCEDURE IF EXISTS eliminar_mesa$$
CREATE PROCEDURE eliminar_mesa(IN p_id INT)
BEGIN
DELETE FROM mesa WHERE id_mesa = p_id;
END$$

DROP PROCEDURE IF EXISTS buscar_mesa_por_id$$
CREATE PROCEDURE buscar_mesa_por_id(IN p_id INT)
BEGIN
SELECT * FROM mesa WHERE id_mesa = p_id;
END$$

DROP PROCEDURE IF EXISTS listar_mesas$$
CREATE PROCEDURE listar_mesas()
BEGIN
SELECT * FROM mesa;
END$$

-- ============================================================
-- 10. PROCEDIMIENTOS: DATOS_CONTACTO
-- ============================================================
DROP PROCEDURE IF EXISTS insertar_datos_contacto$$
CREATE PROCEDURE insertar_datos_contacto(
    IN p_nombre VARCHAR(100),
    IN p_telefono VARCHAR(15),
    IN p_correo VARCHAR(100),
    OUT p_id_contacto INT
)
BEGIN
INSERT INTO datos_contacto (nombre, telefono, correo)
VALUES (p_nombre, p_telefono, p_correo);
SET p_id_contacto = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS modificar_datos_contacto$$
CREATE PROCEDURE modificar_datos_contacto(
    IN p_id_contacto INT,
    IN p_nombre VARCHAR(100),
    IN p_telefono VARCHAR(15),
    IN p_correo VARCHAR(100)
)
BEGIN
UPDATE datos_contacto
SET nombre = p_nombre,
    telefono = p_telefono,
    correo = p_correo
WHERE id_contacto = p_id_contacto;
END$$

DROP PROCEDURE IF EXISTS eliminar_datos_contacto$$
CREATE PROCEDURE eliminar_datos_contacto(IN p_id_contacto INT)
BEGIN
DELETE FROM datos_contacto WHERE id_contacto = p_id_contacto;
END$$

DROP PROCEDURE IF EXISTS buscar_datos_contacto_por_id$$
CREATE PROCEDURE buscar_datos_contacto_por_id(IN p_id_contacto INT)
BEGIN
SELECT id_contacto, nombre, telefono, correo
FROM datos_contacto
WHERE id_contacto = p_id_contacto;
END$$

DROP PROCEDURE IF EXISTS listar_datos_contacto$$
CREATE PROCEDURE listar_datos_contacto()
BEGIN
SELECT id_contacto, nombre, telefono, correo
FROM datos_contacto
ORDER BY id_contacto;
END$$

-- ============================================================
-- 11. PROCEDIMIENTOS: RESERVA
-- ============================================================
DROP PROCEDURE IF EXISTS insertar_reserva$$
CREATE PROCEDURE insertar_reserva(
    IN p_fecha DATE,
    IN p_hora_inicio TIME,
    IN p_hora_fin TIME,
    IN p_cantidad_personas INT,
    IN p_estado VARCHAR(20),
    IN p_codigo_acceso VARCHAR(64),
    IN p_id_mesa INT,
    IN p_id_contacto INT,
    OUT p_id_reserva INT
)
BEGIN
INSERT INTO reserva (fecha, hora_inicio, hora_fin, cantidad_personas, estado, codigo_acceso, id_mesa, id_contacto)
VALUES (p_fecha, p_hora_inicio, p_hora_fin, p_cantidad_personas, p_estado, p_codigo_acceso, p_id_mesa, p_id_contacto);
SET p_id_reserva = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS modificar_reserva$$
CREATE PROCEDURE modificar_reserva(
    IN p_fecha DATE,
    IN p_hora_inicio TIME,
    IN p_hora_fin TIME,
    IN p_cantidad_personas INT,
    IN p_estado VARCHAR(20),
    IN p_codigo_acceso VARCHAR(64),
    IN p_id_mesa INT,
    IN p_id_contacto INT,
    IN p_id_reserva INT
)
BEGIN
UPDATE reserva
SET fecha = p_fecha,
    hora_inicio = p_hora_inicio,
    hora_fin = p_hora_fin,
    cantidad_personas = p_cantidad_personas,
    estado = p_estado,
    codigo_acceso = p_codigo_acceso,
    id_mesa = p_id_mesa,
    id_contacto = p_id_contacto
WHERE id_reserva = p_id_reserva;
END$$

DROP PROCEDURE IF EXISTS eliminar_reserva$$
CREATE PROCEDURE eliminar_reserva(IN p_id_reserva INT)
BEGIN
DELETE FROM reserva WHERE id_reserva = p_id_reserva;
END$$

DROP PROCEDURE IF EXISTS buscar_reserva_por_id$$
CREATE PROCEDURE buscar_reserva_por_id(IN p_id_reserva INT)
BEGIN
SELECT id_reserva, fecha, hora_inicio, hora_fin, cantidad_personas, estado, codigo_acceso, id_mesa, id_contacto
FROM reserva
WHERE id_reserva = p_id_reserva;
END$$

DROP PROCEDURE IF EXISTS listar_reservas$$
CREATE PROCEDURE listar_reservas()
BEGIN
SELECT id_reserva, fecha, hora_inicio, hora_fin, cantidad_personas, estado, codigo_acceso, id_mesa, id_contacto
FROM reserva
ORDER BY id_reserva;
END$$

-- ============================================================
-- 12. PROCEDIMIENTOS: CUENTA_CONSUMO
-- ============================================================
DROP PROCEDURE IF EXISTS insertar_cuenta_consumo$$
CREATE PROCEDURE insertar_cuenta_consumo(
    IN p_fecha_apertura DATETIME,
    IN p_fecha_cierre DATETIME,
    IN p_fecha_pago DATETIME,
    IN p_estado VARCHAR(10),
    IN p_monto_total_pagar DECIMAL(10,2),
    IN p_enlace_pago CHAR(36),
    IN p_id_mesa INT,
    IN p_id_mozo INT,
    IN p_id_reserva INT,
    OUT p_id INT
)
BEGIN
INSERT INTO cuenta_consumo(fecha_apertura, fecha_cierre, fecha_pago, estado, monto_total_pagar, enlace_pago, id_mesa, id_mozo, id_reserva)
VALUES(p_fecha_apertura, p_fecha_cierre, p_fecha_pago, p_estado, p_monto_total_pagar, p_enlace_pago, p_id_mesa, p_id_mozo, p_id_reserva);
SET p_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS modificar_cuenta_consumo$$
CREATE PROCEDURE modificar_cuenta_consumo(
    IN p_fecha_apertura DATETIME,
    IN p_fecha_cierre DATETIME,
    IN p_fecha_pago DATETIME,
    IN p_estado VARCHAR(10),
    IN p_monto_total_pagar DECIMAL(10,2),
    IN p_enlace_pago CHAR(36),
    IN p_id_mesa INT,
    IN p_id_mozo INT,
    IN p_id_reserva INT,
    IN p_id INT
)
BEGIN
UPDATE cuenta_consumo
SET fecha_apertura = p_fecha_apertura,
    fecha_cierre = p_fecha_cierre,
    fecha_pago = p_fecha_pago,
    estado = p_estado,
    monto_total_pagar = p_monto_total_pagar,
    enlace_pago = p_enlace_pago,
    id_mesa = p_id_mesa,
    id_mozo = p_id_mozo,
    id_reserva = p_id_reserva
WHERE id_cuenta = p_id;
END$$

DROP PROCEDURE IF EXISTS eliminar_cuenta_consumo$$
CREATE PROCEDURE eliminar_cuenta_consumo(IN p_id INT)
BEGIN
DELETE FROM cuenta_consumo WHERE id_cuenta = p_id;
END$$

DROP PROCEDURE IF EXISTS buscar_cuenta_consumo_por_id$$
CREATE PROCEDURE buscar_cuenta_consumo_por_id(IN p_id INT)
BEGIN
SELECT * FROM cuenta_consumo WHERE id_cuenta = p_id;
END$$

DROP PROCEDURE IF EXISTS listar_cuentas_consumo$$
CREATE PROCEDURE listar_cuentas_consumo()
BEGIN
SELECT * FROM cuenta_consumo;
END$$

-- ============================================================
-- 13. PROCEDIMIENTOS: PEDIDO Y DETALLE_PEDIDO
-- ============================================================
DROP PROCEDURE IF EXISTS insertar_pedido$$
CREATE PROCEDURE insertar_pedido(
    IN p_fecha_hora DATETIME,
    IN p_id_cuenta_consumo INT,
    OUT p_id INT
)
BEGIN
INSERT INTO pedido(fecha_hora, id_cuenta_consumo)
VALUES(p_fecha_hora, p_id_cuenta_consumo);
SET p_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS modificar_pedido$$
CREATE PROCEDURE modificar_pedido(
    IN p_fecha_hora DATETIME,
    IN p_id_cuenta_consumo INT,
    IN p_id INT
)
BEGIN
UPDATE pedido
SET fecha_hora = p_fecha_hora,
    id_cuenta_consumo = p_id_cuenta_consumo
WHERE id_pedido = p_id;
END$$

DROP PROCEDURE IF EXISTS eliminar_pedido$$
CREATE PROCEDURE eliminar_pedido(IN p_id INT)
BEGIN
DELETE FROM pedido WHERE id_pedido = p_id;
END$$

DROP PROCEDURE IF EXISTS buscar_pedido_por_id$$
CREATE PROCEDURE buscar_pedido_por_id(IN p_id INT)
BEGIN
SELECT * FROM pedido WHERE id_pedido = p_id;
END$$

DROP PROCEDURE IF EXISTS listar_pedidos$$
CREATE PROCEDURE listar_pedidos()
BEGIN
SELECT * FROM pedido;
END$$

DROP PROCEDURE IF EXISTS insertar_detalle_pedido$$
CREATE PROCEDURE insertar_detalle_pedido(
    IN p_cantidad_platos INT,
    IN p_precio_unitario DECIMAL(10,2),
    IN p_subtotal DECIMAL(10,2),
    IN p_id_plato INT,
    IN p_id_pedido INT,
    OUT p_id INT
)
BEGIN
INSERT INTO detalle_pedido(cantidad_platos, precio_unitario, subtotal, id_plato, id_pedido)
VALUES(p_cantidad_platos, p_precio_unitario, p_subtotal, p_id_plato, p_id_pedido);
SET p_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS modificar_detalle_pedido$$
CREATE PROCEDURE modificar_detalle_pedido(
    IN p_cantidad_platos INT,
    IN p_precio_unitario DECIMAL(10,2),
    IN p_subtotal DECIMAL(10,2),
    IN p_id_plato INT,
    IN p_id_pedido INT,
    IN p_id INT
)
BEGIN
UPDATE detalle_pedido
SET cantidad_platos = p_cantidad_platos,
    precio_unitario = p_precio_unitario,
    subtotal = p_subtotal,
    id_plato = p_id_plato,
    id_pedido = p_id_pedido
WHERE id_detalle = p_id;
END$$

DROP PROCEDURE IF EXISTS eliminar_detalle_pedido$$
CREATE PROCEDURE eliminar_detalle_pedido(IN p_id INT)
BEGIN
DELETE FROM detalle_pedido WHERE id_detalle = p_id;
END$$

DROP PROCEDURE IF EXISTS buscar_detalle_pedido_por_id$$
CREATE PROCEDURE buscar_detalle_pedido_por_id(IN p_id INT)
BEGIN
SELECT * FROM detalle_pedido WHERE id_detalle = p_id;
END$$

DROP PROCEDURE IF EXISTS listar_detalles_pedido$$
CREATE PROCEDURE listar_detalles_pedido()
BEGIN
SELECT * FROM detalle_pedido;
END$$

DROP PROCEDURE IF EXISTS listar_detalles_por_pedido$$
CREATE PROCEDURE listar_detalles_por_pedido(IN p_id_pedido INT)
BEGIN
SELECT * FROM detalle_pedido WHERE id_pedido = p_id_pedido;
END$$

DROP PROCEDURE IF EXISTS eliminar_detalles_por_pedido$$
CREATE PROCEDURE eliminar_detalles_por_pedido(IN p_id_pedido INT)
BEGIN
DELETE FROM detalle_pedido WHERE id_pedido = p_id_pedido;
END$$

-- Rutinas de compatibilidad con los nombres utilizados por los DAO Java.
DROP PROCEDURE IF EXISTS buscar_detalles_por_pedido_id$$
CREATE PROCEDURE buscar_detalles_por_pedido_id(IN p_id_pedido INT)
BEGIN
    SELECT * FROM detalle_pedido WHERE id_pedido = p_id_pedido;
END$$

DROP PROCEDURE IF EXISTS eliminar_insumos_por_receta$$
CREATE PROCEDURE eliminar_insumos_por_receta(IN p_id_receta INT)
BEGIN
    DELETE FROM receta_insumo WHERE id_receta = p_id_receta;
END$$

DELIMITER ;
