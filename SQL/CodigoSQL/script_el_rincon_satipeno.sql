-- ============================================
-- SCRIPT BASE DE DATOS: EL RINCÓN SATIPEÑO (v3.4 FINAL)
-- Ajuste final: datos_contacto normalizado e id_reserva opcional en cuenta_consumo
-- Orden: 0. BD | 1. DROPS | 2. CREATES | 3. INSERTS
-- Motor: MySQL 8.0.16+
-- ============================================

CREATE DATABASE IF NOT EXISTS rincon_satipeno;
USE rincon_satipeno;

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
                               estado ENUM('ACTIVO', 'INACTIVO', 'INACTIVO_TEMPORAL') NOT NULL DEFAULT 'ACTIVO',
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
                       id_receta INT NOT NULL,
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
                                PRIMARY KEY (id_contacto)
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
                             CHECK (TIME_TO_SEC(hora_fin) - TIME_TO_SEC(hora_inicio) <= 7200)
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
                                                       (4, 4, 'TERRAZA', 'OCUPADA');

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
INSERT INTO plato (nombre, precio, categoria, descripcion, id_receta) VALUES
                                                                          ('Tacacho con Cecina', 32.00, 'PLATO_FONDO', 'Tacacho tradicional con cecina de la selva.', 1),
                                                                          ('Lomo Saltado', 45.50, 'PLATO_FONDO', 'Exquisito lomo saltado al jugo con papas fritas y arroz', 2),
                                                                          ('Papa a la Huancaína', 18.00, 'ENTRADA', 'Papas servidas con crema huancaína tradicional', 3);

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
                                                                                                                                                ('2026-09-27 13:00:00', NULL, '2026-09-27 14:10:00', 'PAGADA', 91.00, UUID(), 4, 2, 2);

-- 13. Pedidos
INSERT INTO pedido (fecha_hora, id_cuenta_consumo) VALUES
                                                       ('2026-09-27 12:35:00', 1),
                                                       ('2026-09-27 13:05:00', 2);

-- 14. Detalles de pedido
INSERT INTO detalle_pedido (cantidad_platos, precio_unitario, subtotal, id_plato, id_pedido) VALUES
                                                                                                 (1, 32.00, 32.00, 1, 1);

INSERT INTO detalle_pedido (cantidad_platos, precio_unitario, subtotal, id_plato, id_pedido) VALUES
                                                                                                 (2, 45.50, 91.00, 2, 2);