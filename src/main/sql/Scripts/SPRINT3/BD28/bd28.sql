CREATE OR REPLACE TRIGGER PreventDeleteOperacao
BEFORE DELETE ON Operacao
FOR EACH ROW
BEGIN
    RAISE_APPLICATION_ERROR(-20001, 'Não é possivel apagar Operações.');
END;
/