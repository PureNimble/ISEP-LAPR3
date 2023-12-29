CREATE OR REPLACE TRIGGER PreventUpdateAfterThreeDays
BEFORE UPDATE ON Operacao
FOR EACH ROW
DECLARE
    PRAGMA AUTONOMOUS_TRANSACTION;
    dataAtual DATE;
    plantId NUMBER;
    operationId NUMBER;
    operationCount NUMBER;
BEGIN
    SELECT COUNT(*) INTO operationCount FROM OperacaoPlantacao WHERE OperacaoID = :OLD.ID;
    COMMIT;
    IF :NEW.ESTADOID = 3 THEN
        SELECT CURRENT_DATE INTO dataAtual FROM DUAL;
        IF (dataAtual - :OLD.DataOperacao) > 3 THEN
            RAISE_APPLICATION_ERROR(-20001, 'Não é possível alterar uma operação com mais de 3 dias');
        END IF;
    END IF;
    IF operationCount > 0 THEN
        SELECT PlantacaoID INTO plantId FROM OperacaoPlantacao WHERE OperacaoID = :OLD.ID;
        COMMIT;
        SELECT COUNT(Op.OperacaoID) INTO operationId FROM OperacaoPlantacao Op, Operacao O WHERE Op.PlantacaoID = plantId AND Op.OperacaoID = O.ID AND O.DataOperacao > :OLD.DataOperacao;
        COMMIT;
        IF operationId IS NOT NULL THEN
            RAISE_APPLICATION_ERROR(-20001, 'Não é possível alterar uma operação que tenha operações posteriores');
        END IF;
    END IF;
END;
/