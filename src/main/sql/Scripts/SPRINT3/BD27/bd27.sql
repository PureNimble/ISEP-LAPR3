CREATE OR REPLACE TRIGGER PreventLogChanges
BEFORE UPDATE OR DELETE ON LogOperacao
FOR EACH ROW
BEGIN
    RAISE_APPLICATION_ERROR(-20001, 'Não possivel alterar/apagar as informações do Log');
END;
/