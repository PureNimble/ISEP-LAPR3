--Check if date is in the future.
CREATE OR REPLACE PROCEDURE verifyDateInfo(dataOperacao DATE) IS
    dataAtual DATE;
    invalidDate EXCEPTION;

BEGIN

    SELECT CURRENT_DATE INTO dataAtual FROM DUAL;
    IF dataOperacao > dataAtual THEN
        RAISE invalidDate;
    END IF;

EXCEPTION
    
    WHEN invalidDate THEN
        RAISE_APPLICATION_ERROR(-20001,'Não é possível registar operações no futuro!');
END verifyDateInfo;
/

--Check if parcela exists
CREATE OR REPLACE PROCEDURE verifyParcelaInfo(parcelaID NUMBER) IS
    parcelaExists NUMBER;

BEGIN

    SELECT 1 INTO parcelaExists FROM PARCELA WHERE EspacoID = parcelaID;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001,'Parcela não existe.');
END verifyParcelaInfo;
/

--Check if plantação exists
CREATE OR REPLACE PROCEDURE verifyPlantacaoInfo(plantacaoID NUMBER, parcelaID NUMBER) IS
    plantacaoExists NUMBER;

BEGIN
    
    SELECT 1 INTO plantacaoExists FROM PLANTACAO WHERE ID = plantacaoID AND ParcelaEspacoID = parcelaID;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001,'Plantação não existe.');
END verifyPlantacaoInfo;
/

--Check if setor exists
CREATE OR REPLACE PROCEDURE verifySetorInfo(setorID NUMBER) IS
    setorExists NUMBER;

BEGIN

    SELECT 1 INTO setorExists FROM Setor WHERE ID = setorID;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001,'Setor não existe.');
END verifySetorInfo;
/

--Check quantity
CREATE OR REPLACE PROCEDURE verifyQuantityInfo(plantacaoID NUMBER, parcelaID NUMBER, quantidade NUMBER, unidade VARCHAR2) IS
    areaParcela NUMBER;
    quantidadeParcela NUMBER;
    invalidQuantidade EXCEPTION;
    culturaID NUMBER;

BEGIN
    IF unidade = 'ha' THEN
        
        SELECT Area INTO areaParcela FROM Espaco WHERE ID = parcelaID;
        IF quantidade > areaParcela THEN 
            RAISE invalidQuantidade;
        END IF;
    ELSE
        BEGIN
            SELECT CulturaID INTO culturaID FROM PLANTACAO WHERE ID = plantacaoID;
            SELECT SUM(QUANTIDADE)
            INTO quantidadeParcela
            FROM PLANTACAO
            WHERE CulturaID = culturaID AND ParcelaEspacoID = parcelaID;
        END;
        
    END IF;

EXCEPTION
    WHEN invalidQuantidade THEN 
        RAISE_APPLICATION_ERROR(-20001,'Quantidade fornecida superior à existente.');
END verifyQuantityInfo;
/