CREATE OR REPLACE TRIGGER PreventUpdateAfterThreeDays
BEFORE UPDATE ON Operacao
FOR EACH ROW
DECLARE
    dataAtual DATE;
BEGIN
    IF :NEW.ESTADOID = 3 THEN
        SELECT CURRENT_DATE INTO dataAtual FROM DUAL;
        IF (dataAtual - :OLD.DataOperacao) > 3 THEN
            RAISE_APPLICATION_ERROR(-20001, 'Não é possível alterar uma operação com mais de 3 dias');
        END IF;
    END IF;
END;
/