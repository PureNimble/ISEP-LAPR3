CREATE OR REPLACE TRIGGER InsertLogOperacao
AFTER INSERT OR UPDATE ON Operacao
FOR EACH ROW
DECLARE
     logOperacaoID NUMBER;
     tipoOperacaoDes VARCHAR2(100);
     estadoDes VARCHAR2(100);
     registoDes VARCHAR2(100);
BEGIN
    SELECT LogOperacaoNextID.NEXTVAL
    INTO logOperacaoID
    FROM dual;

    SELECT Designacao INTO tipoOperacaoDes FROM TipoOperacao WHERE ID = :NEW.TipoOperacaoID;

    SELECT Designacao INTO estadoDes FROM Estado WHERE ID = :NEW.EstadoID;

    IF INSERTING THEN
        registoDes := 'Criação Da Operação';
    ELSE 
        registoDes := 'Alteração da Operação';
    END IF;

    INSERT INTO LogOperacao (ID, DataOperacao, Quantidade, Unidade, TipoOperacao, Estado, Registo, TipoRegisto)
    VALUES (logOperacaoID, :NEW.dataOperacao, :NEW.quantidade, :NEW.Unidade, tipoOperacaoDes, estadoDes, SYSTIMESTAMP, registoDes);
END;
/