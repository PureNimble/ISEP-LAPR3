CREATE OR REPLACE FUNCTION getFatorProducaoList(dataInicial DATE, dataFinal DATE) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT DATA, FATORPRODUCAO, CULTURA, APLICACAO, PARCELA
        FROM(
            SELECT O.DATAOPERACAO AS data, FP.Designacao AS FATORPRODUCAO, NE.NOMECOMUM ||' ' || CU.VARIEDADE AS Cultura,A.DESIGNACAO AS Aplicacao, E.Designacao as parcela 
            FROM OPERACAOFATOR OFA, Operacao O, FatorProducao FP, OperacaoPlantacao OP, Plantacao P, Espaco E, Cultura CU, NOMEESPECIE NE, AplicacaoProduto AP, Aplicacao A
            WHERE OFA.OPERACAOID = O.ID
            AND OFA.FATORPRODUCAOID = FP.ID
            AND AP.FATORPRODUCAOID = FP.ID
            AND AP.AplicacaoID = A.ID
            AND O.ID = OP.OPERACAOID
            AND OP.PLANTACAOID = P.ID
            AND P.PARCELAESPACOID = E.ID
            AND P.CULTURAID = CU.ID
            AND CU.NOMEESPECIEID = NE.ID
            UNION
            SELECT O.DATAOPERACAO AS data, FP.Designacao AS FATORPRODUCAO,'Sem cultura'AS Cultura, A.DESIGNACAO AS APLICACAO, E.DESIGNACAO AS PARCELA
            FROM OPERACAOFATOR OFA, Operacao O, FatorProducao FP, OperacaoParcela OP, Plantacao P, Espaco E, Aplicacao A, AplicacaoProduto AP
            WHERE OFA.OPERACAOID = O.ID
            AND OFA.FATORPRODUCAOID = FP.ID
            AND AP.FATORPRODUCAOID = FP.ID
            AND AP.AplicacaoID = A.ID
            AND O.ID = OP.OPERACAOID
            AND OP.PARCELAESPACOID = E.ID
        )
        WHERE data BETWEEN DATAINICIAL AND DATAFINAL
        ORDER BY PARCELA,APLICACAO,FATORPRODUCAO;
        
    RETURN result_cursor;
END;
/

DECLARE
    result_cursor SYS_REFCURSOR;
    parcela VARCHAR2(255);
    cultura VARCHAR2(255);
    fatorproducao VARCHAR2(255);
    aplicacao VARCHAR2(255);
    data DATE;
    previous_parcela VARCHAR2(255);
    previous_aplicacao VARCHAR2(255);


    data_found BOOLEAN := FALSE;
BEGIN
    result_cursor := getFatorProducaoList(TO_DATE('2019-01-01', 'YYYY-MM-DD'), TO_DATE('2023-07-06', 'YYYY-MM-DD'));
    LOOP
        FETCH result_cursor INTO data, fatorproducao, cultura, aplicacao, parcela;
        EXIT WHEN result_cursor%NOTFOUND;
        data_found := TRUE;
        
        IF previous_parcela IS NULL OR previous_parcela != parcela THEN
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('Parcela: ' || parcela);
            
            IF previous_aplicacao = aplicacao THEN
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE(CHR(9)||'Aplicacao: ' || aplicacao);
                DBMS_OUTPUT.PUT_LINE(' ');
            END IF;
            
            previous_parcela := parcela;
        END IF;
        IF previous_aplicacao IS NULL OR previous_aplicacao != aplicacao THEN
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE(CHR(9)||'Aplicacao: ' || aplicacao);
            DBMS_OUTPUT.PUT_LINE(' ');
            previous_aplicacao := aplicacao;
        END IF;
        DBMS_OUTPUT.PUT_LINE(CHR(9) || CHR(9) || data || ' - ' || fatorproducao || ' - ' || cultura);

    END LOOP;
    CLOSE result_cursor;
    IF NOT data_found THEN
        DBMS_OUTPUT.PUT_LINE('Sem resultados, para o intervalo de tempo selecionado!');
    END IF;
END;
/