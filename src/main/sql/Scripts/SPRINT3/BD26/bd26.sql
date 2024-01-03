CREATE OR REPLACE TRIGGER InsertLogOperacao
AFTER INSERT OR UPDATE ON Operacao
FOR EACH ROW
DECLARE
     logOperacaoID NUMBER;
     estadoDes VARCHAR2(100);
     registoDes VARCHAR2(100);
BEGIN
    SELECT LogOperacaoNextID.NEXTVAL
    INTO logOperacaoID
    FROM dual;

    SELECT Designacao INTO estadoDes FROM Estado WHERE ID = :NEW.EstadoID;

    IF INSERTING THEN
        registoDes := 'Criação Da Operação';
    ELSE 
        registoDes := 'Alteração da Operação';
    END IF;

    INSERT INTO LogOperacao (ID, OperacaoID, DataOperacao, Quantidade, Unidade, Estado, Registo, TipoRegisto)
    VALUES (logOperacaoID, :NEW.id, :NEW.dataOperacao, :NEW.quantidade, :NEW.Unidade, estadoDes, SYSTIMESTAMP, registoDes);
END;
/