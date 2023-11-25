CREATE OR REPLACE FUNCTION getFatorProducaoList(dataInicial DATE, dataFinal DATE) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT PARCELA, CULTURA, FATORPRODUCAO, TIPOPRODUTO, DATA
        FROM(
            SELECT E.DESIGNACAO AS PARCELA, NE.NOMECOMUM || ' ' || CU.VARIEDADE AS CULTURA, FP.DESIGNACAO AS FATORPRODUCAO, TP.DESIGNACAO AS TIPOPRODUTO, O.DATAOPERACAO as data
            FROM OPERACAOFATOR OPF, FATORPRODUCAO FP, OPERACAOPLANTACAO OP, PLANTACAO P, CULTURA CU, NOMEESPECIE NE, ESPACO E, TIPOPRODUTO TP,Operacao O
            WHERE OPF.OPERACAOID = O.ID
            AND O.ID = OP.OPERACAOID
            AND OPF.FATORPRODUCAOID = FP.ID
            AND FP.TIPOPRODUTOID = TP.ID
            AND OP.PLANTACAOID = P.ID
            AND P.PARCELAESPACOID = E.ID
            AND P.CULTURAID = CU.ID
            AND CU.NOMEESPECIEID = NE.ID
            UNION 
            SELECT E.DESIGNACAO AS PARCELA, 'NO DATA' AS CULTURA , FP.DESIGNACAO AS FATORPRODUCAO, TP.DESIGNACAO AS TIPOPRODUTO, O.DATAOPERACAO as data
            FROM OPERACAOFATOR OPF, FATORPRODUCAO FP, OPERACAOPARCELA OP, ESPACO E, TIPOPRODUTO TP,Operacao O
            WHERE OPF.OPERACAOID = O.ID
            AND O.ID = OP.OPERACAOID
            AND OPF.FATORPRODUCAOID = FP.ID
            AND FP.TIPOPRODUTOID = TP.ID
            AND OP.PARCELAESPACOID = E.ID 
        )
        WHERE data BETWEEN DATAINICIAL AND DATAFINAL
        ORDER BY TIPOPRODUTO;
    RETURN result_cursor;
END;
/

DECLARE
    result_cursor SYS_REFCURSOR;
    parcela VARCHAR2(255);
    cultura VARCHAR2(255);
    fatorproducao VARCHAR2(255);
    tipoproduto VARCHAR2(255);
    data VARCHAR2(255);

    data_found BOOLEAN := FALSE;
BEGIN
    result_cursor := getFatorProducaoList(TO_DATE('2018-01-10', 'YYYY-MM-DD'), TO_DATE('2018-07-10', 'YYYY-MM-DD'));
    LOOP
        FETCH result_cursor INTO parcela, cultura, fatorproducao, tipoproduto, data;
        EXIT WHEN result_cursor%NOTFOUND;
        data_found := TRUE;
        DBMS_OUTPUT.PUT_LINE(result_cursor%ROWCOUNT||' º'  );
        DBMS_OUTPUT.PUT_LINE(' -> Parcela: '|| parcela);
        DBMS_OUTPUT.PUT_LINE(' -> Cultura:' || cultura );
        DBMS_OUTPUT.PUT_LINE(' -> FatorProducao:'|| fatorproducao);
        DBMS_OUTPUT.PUT_LINE(' -> TipoProduto:'|| tipoproduto);
        DBMS_OUTPUT.PUT_LINE(' -> Data:'|| data);
        DBMS_OUTPUT.PUT_LINE(' ');
        DBMS_OUTPUT.PUT_LINE('---------------------------------');

    END LOOP;
    CLOSE result_cursor;
    IF NOT data_found THEN
        DBMS_OUTPUT.PUT_LINE('Sem resultados, para o intervalo de tempo selecionado!');
    END IF;
END;
/