CREATE OR REPLACE FUNCTION getFatorProducaoYear(year Number) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT fatorProducao, EXTRACT(YEAR FROM data) as ano
        FROM(
            SELECT FP.DESIGNACAO as FatorProducao, O.DATAOPERACAO as data  
            FROM OperacaoFator OFA, fatorproducao FP, Operacao O
            WHERE OFA.FATORPRODUCAOID = FP.ID
            AND OFA.OPERACAOID = O.ID
            AND FP.ID NOT IN (
                SELECT FP2.ID
                FROM OperacaoFator OFA2, fatorproducao FP2, Operacao O2
                WHERE OFA2.FATORPRODUCAOID = FP2.ID
                AND OFA2.OPERACAOID = O2.ID
                AND EXTRACT(YEAR FROM O2.DATAOPERACAO) = year
            )
        )
        WHERE year != EXTRACT(YEAR FROM data)
        GROUP BY fatorProducao, EXTRACT(YEAR FROM data)
        ORDER BY fatorProducao, EXTRACT(YEAR FROM data);

    RETURN result_cursor;
END;
/

DECLARE
    result_cursor SYS_REFCURSOR;
    FATORPRODUCAO VARCHAR2(100);
    YEAR_APLICACAO NUMBER;
    PREVIOUS_FATORPRODUCAO VARCHAR2(100);
    DATA_FOUND BOOLEAN := FALSE;
BEGIN
    result_cursor := getFatorProducaoYear(2023);
    LOOP
        FETCH result_cursor INTO FATORPRODUCAO, YEAR_APLICACAO;
        EXIT WHEN result_cursor%NOTFOUND;
        DATA_FOUND := TRUE;
        IF PREVIOUS_FATORPRODUCAO IS NULL OR PREVIOUS_FATORPRODUCAO != FATORPRODUCAO THEN
            IF PREVIOUS_FATORPRODUCAO IS NOT NULL THEN
                DBMS_OUTPUT.PUT_LINE('');
            END IF;
            DBMS_OUTPUT.PUT(FATORPRODUCAO || ', ' || YEAR_APLICACAO);
            PREVIOUS_FATORPRODUCAO := FATORPRODUCAO;
        ELSE
            DBMS_OUTPUT.PUT(', ' || YEAR_APLICACAO);
        END IF;
    END LOOP;
    IF PREVIOUS_FATORPRODUCAO IS NOT NULL THEN
        DBMS_OUTPUT.PUT_LINE('');
    END IF;
    CLOSE result_cursor;
    IF NOT DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Sem resultados!');
    END IF;
END;
/