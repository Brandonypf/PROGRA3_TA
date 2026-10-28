-- Ejecutar solo si se necesitan vender productos sin receta (p. ej. agua envasada).
-- No modifica registros previos ni cambia los procedimientos almacenados.
ALTER TABLE plato MODIFY COLUMN id_receta INT NULL;
