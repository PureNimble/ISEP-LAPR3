CREATE OR REPLACE TRIGGER PreventUpdateAfterThreeDays
BEFORE UPDATE ON Operacao
FOR EACH ROW
DECLARE
    dataAtual DATE;
    plantId NUMBER;
    operationId NUMBER;
BEGIN
    SELECT PlantacaoID INTO plantId FROM OperacaoPlantacao WHERE OperacaoID = :OLD.ID;
    SELECT COUNT(Op.OperacaoID) INTO operationId FROM OperacaoPlantacao Op, Operacao O WHERE Op.PlantacaoID = plantId AND Op.OperacaoID = O.ID AND O.DataOperacao > :OLD.DataOperacao;
    IF operationId IS NOT NULL THEN
        RAISE_APPLICATION_ERROR(-20001, 'Não é possível alterar uma operação que tenha operações posteriores');
    END IF;
    IF :NEW.ESTADOID = 3 THEN
        SELECT CURRENT_DATE INTO dataAtual FROM DUAL;
        IF (dataAtual - :OLD.DataOperacao) > 3 THEN
            RAISE_APPLICATION_ERROR(-20001, 'Não é possível alterar uma operação com mais de 3 dias');
        END IF;
    END IF;
END;
/