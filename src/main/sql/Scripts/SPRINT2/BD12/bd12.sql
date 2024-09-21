CREATE OR REPLACE PROCEDURE registerMonda(plantacaoID NUMBER,parcelaID NUMBER,dataOperacao DATE,quantidade NUMBER) IS

    TIPO_OPERACAO CONSTANT NUMBER := 9;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE CONSTANT VARCHAR2(10) := 'ha';
    idOperacao NUMBER;

BEGIN
    verifyDateInfo(dataOperacao);
    verifyParcelaInfo(parcelaID);
    verifyPlantacaoInfo(plantacaoID,parcelaID,dataOperacao);
    verifyQuantityInfo(plantacaoID,parcelaID,quantidade, UNIDADE);
    --Obter o ID da operação
    SELECT NVL(MAX(ID),0) + 1 INTO idOperacao FROM Operacao;

    INSERT INTO Operacao(ID, DataOperacao, Quantidade, Unidade, TipoOperacaoID, CadernoCampoID) 
    VALUES (idOperacao, dataOperacao, quantidade, UNIDADE, TIPO_OPERACAO, CADERNO_DE_CAMPO);

    IF plantacaoID IS NULL THEN 
        INSERT INTO OperacaoParcela(OperacaoID, ParcelaEspacoID) VALUES (idOperacao, parcelaID);
    ELSE
        INSERT INTO OperacaoPlantacao(OperacaoID, PlantacaoID) VALUES (idOperacao, plantacaoID);
    END IF;
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Operação registada com sucesso!');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/