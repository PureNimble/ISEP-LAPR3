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
CREATE OR REPLACE PROCEDURE verifyPlantacaoInfo(plantacaoID NUMBER, parcelaID NUMBER, dataOperacao DATE) IS
    plantacaoExists NUMBER;

BEGIN
    
    SELECT 1 INTO plantacaoExists 
    FROM PLANTACAO 
    WHERE ID = plantacaoID 
    AND ParcelaEspacoID = parcelaID 
    AND dataOperacao BETWEEN DataIniciaL AND NVL(DataFinal, TO_DATE('9999-12-31','YYYY-MM-DD'));

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
    quantidadePlantacao NUMBER;
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

        SELECT QUANTIDADE INTO quantidadePlantacao FROM PLANTACAO WHERE ID = plantacaoID;
        IF quantidade > quantidadePlantacao THEN 
            RAISE invalidQuantidade;
        END IF;
        
    END IF;

EXCEPTION
    WHEN invalidQuantidade THEN 
        RAISE_APPLICATION_ERROR(-20001,'Quantidade fornecida superior à disponivel.');
END verifyQuantityInfo;
/

--Check verifyAvailableQuantity
CREATE OR REPLACE PROCEDURE verifyAvailableAreaInfo(parcelaID NUMBER, quantidade NUMBER) IS
    areaParcela NUMBER;
    quantidadeParcela NUMBER;
    invalidQuantidade EXCEPTION;

BEGIN
        
    SELECT Area INTO areaParcela FROM Espaco WHERE ID = parcelaID;
    SELECT SUM(Quantidade) INTO quantidadeParcela FROM Plantacao WHERE ParcelaEspacoID = parcelaID AND DataFinal IS NULL;
        IF quantidade > (areaParcela - quantidadeParcela) THEN 
            RAISE invalidQuantidade;
        END IF;

EXCEPTION
    WHEN invalidQuantidade THEN 
        RAISE_APPLICATION_ERROR(-20001,'Area fornecida superior à disponivel.');
END verifyAvailableAreaInfo;
/

CREATE OR REPLACE PROCEDURE verifyFatorDeProducaoInfo(fatorProducaoID NUMBER) IS
    fatorProducaoExists NUMBER;

BEGIN
        
    SELECT 1 INTO fatorProducaoExists FROM FatorProducao WHERE ID = fatorProducaoID;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001,'Fator de Produção não existe.');
END verifyFatorDeProducaoInfo;
/

CREATE OR REPLACE PROCEDURE verifyModoFertilizacaoInfo(modoAplicacaoID NUMBER) IS
    modoAplicacaoExists NUMBER;

BEGIN
        
    SELECT 1 INTO modoAplicacaoExists FROM ModoFertilizacao WHERE ID = modoAplicacaoID;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001,'Modo de Aplicação não existe.');
END verifyModoFertilizacaoInfo;
/