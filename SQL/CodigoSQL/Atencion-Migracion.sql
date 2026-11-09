CREATE TABLE mesa_mozo (
    id_mesa INT NOT NULL,
    id_mozo INT NOT NULL,
    PRIMARY KEY (id_mesa),
    CONSTRAINT FK_mesa_mozo_mesa FOREIGN KEY (id_mesa) REFERENCES mesa(id_mesa),
    CONSTRAINT FK_mesa_mozo_mozo FOREIGN KEY (id_mozo) REFERENCES mozo(id_mozo)
);

ALTER TABLE cuenta_consumo
    ADD COLUMN medio_pago VARCHAR(20) NULL,
    ADD COLUMN tipo_comprobante VARCHAR(20) NULL,
    ADD COLUMN ruc VARCHAR(11) NULL,
    ADD COLUMN razon_social VARCHAR(150) NULL,
    ADD COLUMN codigo_operacion VARCHAR(60) NULL;

INSERT INTO mesa_mozo (id_mesa, id_mozo)
SELECT cc.id_mesa, MAX(cc.id_mozo)
FROM cuenta_consumo cc
GROUP BY cc.id_mesa
ON DUPLICATE KEY UPDATE id_mozo = VALUES(id_mozo);
