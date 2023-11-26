CREATE OR REPLACE FUNCTION getFatorProducaoElementosList(dataInicial DATE, dataFinal DATE, IDparcela NUMBER) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT fatorProducao, Quantidade, Elemento, data
        FROM(
            SELECT FP.DESIGNACAO as FatorProducao, EF.Quantidade as Quantidade, E.Designacao as Elemento, P.PARCELAESPACOID as parcelaID, O.DATAOPERACAO as data  
            FROM OperacaoFator OFA, fatorproducao FP, ElementoFicha EF, Elemento E, OPERACAOPLANTACAO OP, Plantacao P, Operacao O
            WHERE OFA.FATORPRODUCAOID = FP.ID
            AND OFA.OPERACAOID = O.ID
            AND OFA.OPERACAOID = OP.OPERACAOID
            AND OP.PlantacaoID = P.ID
            AND FP.ID = EF.FATORPRODUCAOID
            AND EF.ELEMENTOID = E.ID
            UNION 
            SELECT FP.DESIGNACAO as Designacao, EF.Quantidade as Quantidade, E.Designacao as Elemento, OP.PARCELAESPACOID as parcelaID, O.DATAOPERACAO as data 
            FROM OperacaoFator OFA, fatorproducao FP, ElementoFicha EF, Elemento E, OPERACAOPARCELA OP, Operacao O
            WHERE OFA.FATORPRODUCAOID = FP.ID
            AND OFA.OPERACAOID = O.ID
            AND OFA.OPERACAOID = OP.OPERACAOID
            AND FP.ID = EF.FATORPRODUCAOID
            AND EF.ELEMENTOID = E.ID
        )
        WHERE data BETWEEN DATAINICIAL AND DATAFINAL
        AND parcelaID = IDparcela
        ORDER BY fatorProducao, data, elemento;

    RETURN result_cursor;
END;
/

DECLARE
    result_cursor SYS_REFCURSOR;
    fatorproducao VARCHAR2(100);
    quantidade VARCHAR2(100);
    elemento VARCHAR2(100);
    data DATE;
    previous_fatorproducao VARCHAR2(100) := NULL;
    previous_data DATE := NULL;
    data_found BOOLEAN := FALSE;
BEGIN
    result_cursor := getFatorProducaoElementosList(TO_DATE('2019-01-01', 'YYYY-MM-DD'), TO_DATE('2023-07-06', 'YYYY-MM-DD'), 105);
    FETCH result_cursor INTO fatorproducao, quantidade, elemento, data;
    WHILE result_cursor%FOUND LOOP
        data_found := TRUE;
        IF previous_fatorproducao IS NULL OR previous_fatorproducao != fatorproducao  THEN
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('Fator de Producao: ' || fatorproducao);
            previous_fatorproducao := fatorproducao;
        END IF;
        IF previous_data IS NULL OR previous_data != data THEN
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('Data: ' || data);
            DBMS_OUTPUT.PUT_LINE(' ');
            previous_data := data;
        END IF;
        DBMS_OUTPUT.PUT_LINE(CHR(9) || '-> Quantidade: ' || quantidade || ', Elemento: ' || elemento);
        FETCH result_cursor INTO fatorproducao, quantidade, elemento, data;
    END LOOP;
    CLOSE result_cursor;
    IF NOT data_found THEN
        DBMS_OUTPUT.PUT_LINE('Sem resultados!');
    END IF;
END;
/