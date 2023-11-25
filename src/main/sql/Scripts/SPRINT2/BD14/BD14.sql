CREATE OR REPLACE PROCEDURE registerFatorDeProducao(quantidade NUMBER,parcelaID NUMBER,dataOperacao DATE, area NUMBER, fatorProducaoID NUMBER, modoFertilizacaoID NUMBER) IS

    TIPO_OPERACAO CONSTANT NUMBER := 4;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE1 CONSTANT VARCHAR2(10) := 'ha';
    UNIDADE2 CONSTANT VARCHAR2(10) := 'kg';
    idOperacao NUMBER;

BEGIN
    verifyParcelaInfo(parcelaID);
    verifyDateInfo(dataOperacao);
    verifyFatorDeProducaoInfo(fatorProducaoID);
    verifyModoFertilizacaoInfo(modoFertilizacaoID);
    verifyQuantityInfo(NULL,parcelaID,area,UNIDADE1);
    --Obter o ID da operação
    SELECT NVL(MAX(ID),0) + 1 INTO idOperacao FROM Operacao;

    INSERT INTO Operacao(ID, DataOperacao, Quantidade, Unidade, TipoOperacaoID, CadernoCampoID) 
    VALUES (idOperacao, dataOperacao, quantidade, UNIDADE2, TIPO_OPERACAO, CADERNO_DE_CAMPO);
    INSERT INTO OperacaoFator(OperacaoID, FatorProducaoID) VALUES(idOperacao, fatorProducaoID);
    INSERT INTO Fertilizacao(OperacaoID, ModoFertilizacaoID) VALUES(idOperacao, modoFertilizacaoID);
    INSERT INTO OperacaoParcela(OperacaoID, ParcelaEspacoID) VALUES (idOperacao, parcelaID);

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Operação registada com sucesso!');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/