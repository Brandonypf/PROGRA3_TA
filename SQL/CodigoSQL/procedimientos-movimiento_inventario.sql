USE rincon_satipeno;

-- ============================================
-- ELIMINAR PROCEDIMIENTOS EXISTENTES
-- ============================================
DROP PROCEDURE IF EXISTS insertar_movimiento_inventario;
DROP PROCEDURE IF EXISTS modificar_movimiento_inventario;
DROP PROCEDURE IF EXISTS eliminar_movimiento_inventario;
DROP PROCEDURE IF EXISTS buscar_movimiento_inventario_por_id;
DROP PROCEDURE IF EXISTS listar_movimientos_inventario;

-- 1. Insertar Movimiento de Inventario (retorna el ID creado en p_id_movimiento)
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
END;

-- 2. Modificar Movimiento de Inventario
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
END;

-- 3. Eliminar Movimiento de Inventario
CREATE PROCEDURE eliminar_movimiento_inventario(
    IN p_id_movimiento INT
)
BEGIN
    DELETE FROM movimiento_inventario 
    WHERE id_movimiento = p_id_movimiento;
END;

-- 4. Buscar Movimiento de Inventario por ID
CREATE PROCEDURE buscar_movimiento_inventario_por_id(
    IN p_id_movimiento INT
)
BEGIN
    SELECT *
    FROM movimiento_inventario 
    WHERE id_movimiento = p_id_movimiento;
END;

-- 5. Listar todos los Movimientos de Inventario
CREATE PROCEDURE listar_movimientos_inventario()
BEGIN
    SELECT *
    FROM movimiento_inventario;
END;