CREATE OR REPLACE PROCEDURE registerSemeadura(culturaID NUMBER,parcelaID NUMBER,dataOperacao DATE,quantidade NUMBER, area NUMBER) IS

    TIPO_OPERACAO CONSTANT NUMBER := 6;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE CONSTANT VARCHAR2(10) := 'kg';
    UNIDADE2 CONSTANT VARCHAR2(10) := 'ha';
    idOperacao NUMBER;
    idPlantacao NUMBER;

BEGIN
    verifyDateInfo(dataOperacao);
    verifyParcelaInfo(parcelaID);
    verifyQuantityInfo(idPlantacao, parcelaID, area, UNIDADE2);
    verifyAvailableAreaInfo(parcelaID, quantidade);
    
    --Obter o ID da operação
    SELECT NVL(MAX(ID),0) + 1 INTO idOperacao FROM Operacao;
    SELECT NVL(MAX(ID),0) + 1 INTO idPlantacao FROM Plantacao;

    INSERT INTO Operacao(ID, DataOperacao, Quantidade, Unidade, TipoOperacaoID, CadernoCampoID) 
    VALUES (idOperacao, dataOperacao, quantidade, UNIDADE, TIPO_OPERACAO, CADERNO_DE_CAMPO);
    INSERT INTO OperacaoParcela(OperacaoID, ParcelaEspacoID) 
    VALUES (idOperacao, parcelaID);
    INSERT INTO Plantacao(ID, DataInicial, DataFinal, Quantidade, Unidade, EstadoFenologico, CulturaID, ParcelaEspacoID) 
    VALUES (idPlantacao, dataOperacao, NULL, area, UNIDADE2, NULL, culturaID, parcelaID);
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Operação registada com sucesso!');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/