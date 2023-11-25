CREATE OR REPLACE PROCEDURE registerRega(quantidade NUMBER,setorID NUMBER,dataOperacao DATE,hora TIMESTAMP) IS

    TIPO_OPERACAO CONSTANT NUMBER := 2;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE CONSTANT VARCHAR2(10) := 'min';
    idOperacao NUMBER;

BEGIN
    verifySetorInfo(setorID);
    verifyDateInfo(dataOperacao);
    --Obter o ID da operação
    SELECT NVL(MAX(ID),0) + 1 INTO idOperacao FROM Operacao;

    INSERT INTO Operacao(ID, DataOperacao, Quantidade, Unidade, TipoOperacaoID, CadernoCampoID) 
    VALUES (idOperacao, dataOperacao, quantidade, UNIDADE, TIPO_OPERACAO, CADERNO_DE_CAMPO);

    INSERT INTO OperacaoSetor(OperacaoID, HoraInicial, SetorID) VALUES (idOperacao, hora, setorID);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/