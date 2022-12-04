DROP TRIGGER tr_ad_clientes;
DROP PACKAGE pq_api_clientes;
DROP SEQUENCE sq_ad_clientes;
DROP TABLE tb_ad_clientes purge;

CREATE TABLE tb_ad_clientes
(
  cliente_id          NUMBER not null,
  nombres             VARCHAR2(50) not null,
  apellidos           VARCHAR2(50) not null,
  activo              VARCHAR2(1) default 'S',
  usuario_creador     VARCHAR2(50) not null,
  fecha_creacion      DATE not null,
  usuario_modificador VARCHAR2(50),
  fecha_modificacion  DATE
);

ALTER TABLE tb_ad_clientes ADD CONSTRAINT pk_adclie_cliente_id primary key (CLIENTE_ID);
ALTER TABLE tb_ad_clientes ADD CONSTRAINT ck_adclie_activo CHECK (activo IN ('S','N'));

CREATE SEQUENCE SQ_AD_CLIENTES
MINVALUE 1
MAXVALUE 9999999999999999999999
START WITH 1
INCREMENT BY 1
NOCACHE
ORDER;

CREATE OR REPLACE PACKAGE pq_api_clientes AS

  PROCEDURE pr_crud_ad_clientes
  (
    p_evento     IN VARCHAR2
   ,p_cliente_id IN OUT NUMBER
   ,p_nombres    IN VARCHAR2
   ,p_apellidos  IN VARCHAR2
   ,p_activo     IN VARCHAR2
  );

  PROCEDURE pr_get_ad_clientes(c_cliente OUT SYS_REFCURSOR);

  PROCEDURE pr_get_ad_clientes_by_id
  (
    p_cliente_id IN NUMBER
   ,c_cliente    OUT SYS_REFCURSOR
  );

END;
/

CREATE OR REPLACE PROCEDURE pr_ad_reset_sequence(p_seq IN VARCHAR2) IS
  l_value NUMBER;
BEGIN
  EXECUTE IMMEDIATE 'SELECT ' || p_seq || '.NEXTVAL FROM DUAL'
    INTO l_value;

  -- RESET VALUE
  EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || p_seq || ' INCREMENT BY -' ||
                    l_value || ' MINVALUE 0';

  -- START SEQUENCE
  EXECUTE IMMEDIATE 'SELECT ' || p_seq || '.NEXTVAL FROM DUAL'
    INTO l_value;

  -- CONFIG SEQUENCE
  EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || p_seq ||
                    ' INCREMENT BY 1 MINVALUE 0';
END;
/

CREATE OR REPLACE PACKAGE BODY pq_api_clientes AS

  PROCEDURE pr_crud_ad_clientes
  (
    p_evento     IN VARCHAR2
   ,p_cliente_id IN OUT NUMBER
   ,p_nombres    IN VARCHAR2
   ,p_apellidos  IN VARCHAR2
   ,p_activo     IN VARCHAR2
  ) IS
  BEGIN
    IF p_evento = 'C' THEN
      INSERT INTO tb_ad_clientes
        (nombres
        ,apellidos
        ,activo)
      VALUES
        (p_nombres
        ,p_apellidos
        ,DECODE(UPPER(p_activo)
               ,'TRUE'
               ,'S'
               ,'FALSE'
               ,'N'))
      RETURNING cliente_id INTO p_cliente_id;
    
      COMMIT;
    ELSIF p_evento = 'U' THEN
      UPDATE tb_ad_clientes
         SET nombres   = p_nombres
            ,apellidos = p_apellidos
            ,activo    = DECODE(UPPER(p_activo)
                               ,'TRUE'
                               ,'S'
                               ,'FALSE'
                               ,'N')
       WHERE cliente_id = p_cliente_id;
    
    END IF;
  
    COMMIT;
  
  EXCEPTION
    WHEN OTHERS THEN
      dbms_output.put_line(SQLERRM);
    
  END pr_crud_ad_clientes;

  PROCEDURE pr_get_ad_clientes(c_cliente OUT SYS_REFCURSOR) IS
  BEGIN
    OPEN c_cliente FOR
      SELECT c.cliente_id
            ,c.nombres
            ,c.apellidos
            ,DECODE(UPPER(c.activo)
                   ,'S'
                   ,'TRUE'
                   ,'N'
                   ,'FALSE') activo
        FROM tb_ad_clientes c
       ORDER BY 1;
  
  EXCEPTION
    WHEN OTHERS THEN
      NULL;
    
  END pr_get_ad_clientes;

  PROCEDURE pr_get_ad_clientes_by_id
  (
    p_cliente_id IN NUMBER
   ,c_cliente    OUT SYS_REFCURSOR
  ) IS
  BEGIN
    OPEN c_cliente FOR
      SELECT c.cliente_id
            ,c.nombres
            ,c.apellidos
            ,DECODE(UPPER(c.activo)
                   ,'S'
                   ,'TRUE'
                   ,'N'
                   ,'FALSE') activo
        FROM tb_ad_clientes c
       WHERE c.cliente_id = p_cliente_id;
  
  EXCEPTION
    WHEN OTHERS THEN
      NULL;
    
  END pr_get_ad_clientes_by_id;

END;
/

CREATE OR REPLACE TRIGGER tr_ad_clientes
BEFORE INSERT OR UPDATE ON tb_ad_clientes
FOR EACH ROW
BEGIN
    IF inserting THEN
        IF :new.cliente_id IS NULL THEN
            SELECT sq_ad_clientes.nextval INTO :new.cliente_id FROM dual;
            :new.usuario_creador := USER;
            :new.fecha_creacion  := SYSDATE;
        END IF;
    ELSIF updating THEN
        :new.usuario_modificador := USER;
        :new.fecha_modificacion := SYSDATE;
    END IF;
END tr_ad_clientes;
/

