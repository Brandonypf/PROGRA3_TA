-- ============================================
-- SCRIPT BASE DE DATOS: EL RINCÓN SATIPEÑO
-- Orden: 1. DROPS | 2. CREATES | 3. INSERTS
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 1. SENTENCIAS DROP TABLE
-- ============================================
DROP TABLE IF EXISTS detalle_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS cuenta_consumo;
DROP TABLE IF EXISTS reserva;
DROP TABLE IF EXISTS plato;
DROP TABLE IF EXISTS receta_insumo;
DROP TABLE IF EXISTS receta;
DROP TABLE IF EXISTS movimiento_inventario;
DROP TABLE IF EXISTS insumo;
DROP TABLE IF EXISTS mesa;
DROP TABLE IF EXISTS empleado;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 2. SENTENCIAS CREATE TABLE
-- ============================================

-- 1. Tabla: Empleado
CREATE TABLE empleado (
    id_empleado INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    contrasenia VARCHAR(255) NOT NULL,
    estado ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    turno ENUM('MANANA', 'TARDE', 'NOCHE') NOT NULL,
    fecha_contratacion DATE NOT NULL,
    tipo_empleado ENUM('ADMINISTRADOR', 'MOZO') NOT NULL,
    PRIMARY KEY (id_empleado),
    CONSTRAINT uk_empleado_email UNIQUE (email)
);

-- 2. Tabla: Mesa
CREATE TABLE mesa (
    id_mesa INT NOT NULL AUTO_INCREMENT,
    numero INT NOT NULL,
    capacidad INT NOT NULL,
    estado ENUM('LIBRE', 'OCUPADA', 'RESERVADA') NOT NULL DEFAULT 'LIBRE',
    PRIMARY KEY (id_mesa),
    CONSTRAINT UQ_mesa_numero UNIQUE (numero),
    CONSTRAINT CK_mesa_numero CHECK (numero > 0),
    CONSTRAINT CK_mesa_capacidad CHECK (capacidad > 0)
);

-- 3. Tabla: Insumo
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

-- 4. Tabla: MovimientoInventario
CREATE TABLE movimiento_inventario (
    id_movimiento INT NOT NULL AUTO_INCREMENT,
    id_insumo INT NOT NULL,
    tipo_movimiento ENUM('ENTRADA', 'SALIDA_CONSUMO', 'AJUSTE') NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_movimiento),
    CONSTRAINT FK_movimiento_insumo 
        FOREIGN KEY (id_insumo) REFERENCES insumo(id_insumo),
    CONSTRAINT CK_movimiento_cantidad CHECK (cantidad > 0)
);

-- 5. Tabla: Receta
CREATE TABLE receta (
    id_receta INT NOT NULL AUTO_INCREMENT,
    descripcion VARCHAR(255),
    PRIMARY KEY (id_receta)
);

-- 6. Tabla: RecetaInsumo
CREATE TABLE receta_insumo (
    id_receta INT NOT NULL,
    id_insumo INT NOT NULL,
    cantidad_requerida DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_receta, id_insumo),
    CONSTRAINT FK_recetainsumo_receta 
        FOREIGN KEY (id_receta) REFERENCES receta(id_receta),
    CONSTRAINT FK_recetainsumo_insumo 
        FOREIGN KEY (id_insumo) REFERENCES insumo(id_insumo),
    CONSTRAINT CK_receta_insumo_cantidad_req CHECK (cantidad_requerida > 0)
);

-- 7. Tabla: Plato (Categorías alineadas con la aplicación)
CREATE TABLE plato (
    id_plato INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    categoria ENUM('ENTRADA', 'PLATO_FONDO', 'PLATOS_FUERTES', 'POSTRE', 'BEBIDA') NOT NULL,
    descripcion VARCHAR(255),
    id_receta INT,
    PRIMARY KEY (id_plato),
    CONSTRAINT FK_plato_receta 
        FOREIGN KEY (id_receta) REFERENCES receta(id_receta),
    CONSTRAINT CK_plato_precio CHECK (precio >= 0)
);

-- 8. Tabla: Reserva
CREATE TABLE reserva (
    id_reserva INT NOT NULL AUTO_INCREMENT,
    fecha_reserva DATETIME NOT NULL,
    cantidad_personas INT NOT NULL,
    estado ENUM('CONFIRMADA', 'CANCELADA') NOT NULL DEFAULT 'CONFIRMADA',
    id_mesa INT NOT NULL,
    PRIMARY KEY (id_reserva),
    CONSTRAINT FK_reserva_mesa 
        FOREIGN KEY (id_mesa) REFERENCES mesa(id_mesa)
);

-- 9. Tabla: CuentaConsumo
CREATE TABLE cuenta_consumo (
    id_cuenta INT NOT NULL AUTO_INCREMENT,
    fecha_apertura DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre DATETIME,
    estado ENUM('ABIERTA', 'CERRADA', 'PAGADA') NOT NULL DEFAULT 'ABIERTA',
    monto_total_pagar DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    id_mesa INT NOT NULL,
    id_mozo INT NOT NULL,
    PRIMARY KEY (id_cuenta),
    CONSTRAINT FK_cuentaconsumo_mesa 
        FOREIGN KEY (id_mesa) REFERENCES mesa(id_mesa),
    CONSTRAINT FK_cuentaconsumo_mozo 
        FOREIGN KEY (id_mozo) REFERENCES empleado(id_empleado),
    CONSTRAINT CK_cuentaconsumo_monto CHECK (monto_total_pagar >= 0)
);

-- 10. Tabla: Pedido
CREATE TABLE pedido (
    id_pedido INT NOT NULL AUTO_INCREMENT,
    precio_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    id_mesa INT NOT NULL,
    id_cuenta_consumo INT NOT NULL,
    id_reserva INT NULL,
    PRIMARY KEY (id_pedido),
    CONSTRAINT FK_pedido_mesa 
        FOREIGN KEY (id_mesa) REFERENCES mesa(id_mesa),
    CONSTRAINT FK_pedido_cuentaconsumo 
        FOREIGN KEY (id_cuenta_consumo) REFERENCES cuenta_consumo(id_cuenta),
    CONSTRAINT FK_pedido_reserva 
        FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva),
    CONSTRAINT CK_pedido_preciototal CHECK (precio_total >= 0)
);

-- 11. Tabla: DetallePedido
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
    CONSTRAINT CK_detalle_subtotal CHECK (subtotal >= 0)
);

-- ============================================
-- 3. SENTENCIAS INSERT
-- ============================================

-- 1. Empleados
INSERT INTO empleado (nombre, email, telefono, contrasenia, estado, turno, fecha_contratacion, tipo_empleado) 
VALUES 
('Brandon Hidalgo', 'brandon.hidalgo@rinconsatipeno.pe', '987654321', 'hash_secure_pass_1', 'ACTIVO', 'MANANA', '2026-01-15', 'ADMINISTRADOR'),
('Leonardo Torres', 'leonardo.torres@rinconsatipeno.pe', '912345678', 'hash_secure_pass_2', 'ACTIVO', 'TARDE', '2026-02-01', 'ADMINISTRADOR'),
('Carlos Mendoza', 'carlos.mendoza@rinconsatipeno.pe', '955443322', 'hash_secure_pass_3', 'ACTIVO', 'MANANA', '2026-03-10', 'MOZO'),
('Ana Ramos', 'ana.ramos@rinconsatipeno.pe', '966778899', 'hash_secure_pass_4', 'ACTIVO', 'NOCHE', '2026-03-12', 'MOZO'),
('Jorge Flores', 'jorge.flores@rinconsatipeno.pe', '944332211', 'hash_secure_pass_5', 'INACTIVO', 'TARDE', '2026-04-01', 'MOZO');

-- 2. Mesas
INSERT INTO mesa (numero, capacidad, estado) VALUES 
(1, 4, 'LIBRE'),
(2, 2, 'OCUPADA'),
(3, 6, 'RESERVADA'),
(4, 4, 'OCUPADA');

-- 3. Insumos (Incluye Cecina para tus pruebas en Java)
INSERT INTO insumo (nombre, unidad_medida, stock_actual, stock_minimo) VALUES
('Cecina', 'Kg', 15.00, 3.00),
('Carne de Res', 'KG', 50.00, 10.00),
('Papas', 'KG', 100.00, 20.00),
('Arroz', 'KG', 80.00, 15.00);

-- 4. Movimientos de Inventario
INSERT INTO movimiento_inventario (id_insumo, tipo_movimiento, cantidad) VALUES
(1, 'ENTRADA', 15.00),
(2, 'ENTRADA', 50.00),
(3, 'ENTRADA', 100.00);

-- 5. Recetas
INSERT INTO receta (descripcion) VALUES 
('Receta para Tacacho con Cecina'),
('Receta para Lomo Saltado'),
('Receta para Papa a la Huancaína');

-- 6. RecetaInsumo
INSERT INTO receta_insumo (id_receta, id_insumo, cantidad_requerida) VALUES 
(1, 1, 0.25),
(2, 2, 0.25),
(2, 3, 0.30),
(3, 3, 0.20);

-- 7. Platos
INSERT INTO plato (nombre, precio, categoria, descripcion, id_receta) VALUES 
('Tacacho con Cecina', 32.00, 'PLATOS_FUERTES', 'Tacacho tradicional con cecina de la selva.', 1),
('Lomo Saltado', 45.50, 'PLATO_FONDO', 'Exquisito lomo saltado al jugo con papas fritas y arroz', 2),
('Papa a la Huancaína', 18.00, 'ENTRADA', 'Papas servidas con crema huancaína tradicional', 3);

-- 8. Reservas
INSERT INTO reserva (fecha_reserva, cantidad_personas, estado, id_mesa) VALUES 
('2026-09-16 13:00:00', 4, 'CONFIRMADA', 3),
('2026-09-16 20:00:00', 2, 'CONFIRMADA', 2);

-- 9. Cuentas de Consumo
INSERT INTO cuenta_consumo (fecha_apertura, fecha_cierre, estado, monto_total_pagar, id_mesa, id_mozo) VALUES 
('2026-09-15 12:30:00', NULL, 'ABIERTA', 32.00, 1, 3),
('2026-09-15 13:00:00', '2026-09-15 14:15:00', 'PAGADA', 120.00, 2, 4);

-- 10. Pedidos
INSERT INTO pedido (precio_total, id_mesa, id_cuenta_consumo, id_reserva) VALUES 
(32.00, 1, 1, NULL),
(120.00, 2, 2, 2);

-- 11. Detalles de Pedido
INSERT INTO detalle_pedido (cantidad_platos, precio_unitario, subtotal, id_plato, id_pedido) VALUES 
(1, 32.00, 32.00, 1, 1),
(2, 45.50, 91.00, 2, 2);