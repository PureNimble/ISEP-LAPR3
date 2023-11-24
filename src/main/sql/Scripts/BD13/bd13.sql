CREATE OR REPLACE PROCEDURE registerColheita(plantacaoID NUMBER,parcelaID NUMBER,dataOperacao DATE,quantidade NUMBER) IS

    TIPO_OPERACAO CONSTANT NUMBER := 7;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE CONSTANT VARCHAR2(10) := 'kg';
    idOperacao NUMBER;

BEGIN
    verifyColheitaInfo(plantacaoID,parcelaID,dataOperacao,quantidade,UNIDADE);
    --Obter o ID da operação
    SELECT NVL(MAX(ID),0) + 1 INTO idOperacao FROM Operacao;

    INSERT INTO Operacao(ID, DataOperacao, Quantidade, Unidade, TipoOperacaoID, CadernoCampoID) 
    VALUES (idOperacao, dataOperacao, quantidade, UNIDADE, TIPO_OPERACAO, CADERNO_DE_CAMPO);

    IF plantacaoID IS NULL THEN 
        INSERT INTO OperacaoParcela(OperacaoID, ParcelaID) VALUES (idOperacao, parcelaID);
    ELSE
        INSERT INTO OperacaoPlantacao(OperacaoID, PlantacaoID) VALUES (idOperacao, plantacaoID);
    END IF;
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/