CREATE OR REPLACE PROCEDURE registerPoda(quantidade NUMBER,parcelaID NUMBER,plantacaoID NUMBER,dataOperacao DATE) IS

    TIPO_OPERACAO CONSTANT NUMBER := 3;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE CONSTANT VARCHAR2(10) := 'un';
    idOperacao NUMBER;

BEGIN
    verifyParcelaInfo(parcelaID);
    verifyPlantacaoInfo(plantacaoID, parcelaID,dataOperacao);
    verifyQuantityInfo(plantacaoID, parcelaID, quantidade, UNIDADE);
    verifyDateInfo(dataOperacao);
    --Obter o ID da operação
    SELECT NVL(MAX(ID),0) + 1 INTO idOperacao FROM Operacao;

    INSERT INTO Operacao(ID, DataOperacao, Quantidade, Unidade, TipoOperacaoID, CadernoCampoID) 
    VALUES (idOperacao, dataOperacao, quantidade, UNIDADE, TIPO_OPERACAO, CADERNO_DE_CAMPO);

    INSERT INTO OperacaoPlantacao(OperacaoID, PlantacaoID) VALUES (idOperacao, plantacaoID);
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Operação registada com sucesso!');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/