CREATE OR REPLACE PROCEDURE verifyOperacaoInfo(plantacaoID NUMBER,parcelaID NUMBER,dataOperacao DATE,quantidade NUMBER,unidade NUMBER) IS
    dataAtual DATE;
    areaParcela NUMBER;
    parcelaExists NUMBER;
    quantidadeParcela NUMBER;
    plantacaoExists NUMBER;
    invalidParcela EXCEPTION;
    plantacaoNull EXCEPTION;
    invalidDate EXCEPTION;
    invalidQuantidade EXCEPTION;

BEGIN
    SELECT CURRENT_DATE INTO dataAtual FROM DUAL;
    -- Critérios de aceitação das USBD11 a USBD15
    --1 Conforme aplicável, a parcela, operação, planta, fruto, fator de produção, etc., têm de existir.
    -- Check if plantacao exists
    DECLARE
        compare BOOLEAN;
    BEGIN
        SELECT TRUE INTO compare FROM PLANTACAO WHERE ID = plantacaoID;
    EXCEPTION WHEN NO_DATA_FOUND THEN
        RAISE invalidPlantacao;
    END;
    -- Check if parcela exists and get its area
    DECLARE
        compare BOOLEAN;
    BEGIN
        SELECT E.Area INTO areaParcela
        FROM ESPACO E JOIN PARCELA P ON E.ID = P.ESPACOID
        WHERE P.ID = parcelaID;
    EXCEPTION WHEN NO_DATA_FOUND THEN
        RAISE invalidParcela;
    END;
    --2 Não é possível registar operações no futuro.
    BEGIN 
        SELECT CURRENT_DATE INTO dataAtual FROM DUAL;
    EXCEPTION WHEN dataOperacao > dataAtual THEN
        RAISE invalidDate;
    END; 
    --3 Não é possível registar operações que envolvam área superior à de uma dada parcela.
    IF unidade = "ha" AND quantidade > areaParcela THEN 
        RAISE invalidQuantidade
    END IF;

EXCEPTION
    
    WHEN invalidParcela THEN
        RAISE_APPLICATION_ERROR(-20001,'Parcela não existe.');       
    WHEN plantacaoNull THEN
        RAISE_APPLICATION_ERROR(-20001,'ID da plantação é inválido.');
    WHEN invalidDate THEN
        RAISE_APPLICATION_ERROR(-20001,'Data da operação é inválida.');
    WHEN invalidQuantidade THEN 
        RAISE_APPLICATION_ERROR(-20001,'Quantidade fornecida superior á sua parcela.');
END verifyOperacaoInfo;