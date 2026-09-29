USE rincon_satipeno;

-- ============================================
-- ELIMINAR PROCEDIMIENTOS EXISTENTES
-- ============================================
DROP PROCEDURE IF EXISTS insertar_insumo;
DROP PROCEDURE IF EXISTS modificar_insumo;
DROP PROCEDURE IF EXISTS eliminar_insumo;
DROP PROCEDURE IF EXISTS buscar_insumo_por_id;
DROP PROCEDURE IF EXISTS listar_insumos;

DELIMITER //

-- 1. Insertar Insumo (retorna el ID creado en p_id_insumo)
CREATE PROCEDURE insertar_insumo(
    IN p_nombre VARCHAR(100),
    IN p_unidad_medida VARCHAR(20),
    IN p_stock_actual DECIMAL(10,2),
    IN p_stock_minimo DECIMAL(10,2),
    OUT p_id_insumo INT
)
BEGIN
    INSERT INTO insumo(nombre, unidad_medida, stock_actual, stock_minimo)
    VALUES (p_nombre, p_unidad_medida, p_stock_actual, p_stock_minimo);
    
    SET p_id_insumo = LAST_INSERT_ID();
END //

-- 2. Modificar Insumo
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
END //

-- 3. Eliminar Insumo
CREATE PROCEDURE eliminar_insumo(
    IN p_id_insumo INT
)
BEGIN
    DELETE FROM insumo 
    WHERE id_insumo = p_id_insumo;
END //

-- 4. Buscar Insumo por ID
CREATE PROCEDURE buscar_insumo_por_id(
    IN p_id_insumo INT
)
BEGIN
    SELECT *
    FROM insumo 
    WHERE id_insumo = p_id_insumo;
END //

-- 5. Listar todos los Insumos
CREATE PROCEDURE listar_insumos()
BEGIN
    SELECT *
    FROM insumo;
END //

DELIMITER ;