CREATE OR REPLACE FUNCTION getRegaMensal(dataInicial DATE, dataFinal DATE) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT PARCELA, EXTRACT(YEAR FROM DATA) AS YEAR, EXTRACT(MONTH FROM DATA) AS MONTH, SUM(DURACAO) AS DURACAO
        FROM(
            SELECT ES.DESIGNACAO AS PARCELA, OP.DataOperacao AS DATA, MAX(OP.QUANTIDADE) AS DURACAO
            FROM TIPOOPERACAO TP, OPERACAO OP, PARCELA PAR, OPERACAOSETOR OS, SETOR S, PLANTACAOSETOR PS, PLANTACAO P, ESPACO ES
            WHERE TP.ID = 2
            AND OP.TIPOOPERACAOID = TP.ID
            AND OS.OPERACAOID = OP.ID
            AND S.ID = OS.SETORID
            AND PS.SETORID = S.ID
            AND P.ID = PS.PLANTACAOID
            AND PAR.ESPACOID = P.PARCELAESPACOID
            AND ES.ID = PAR.ESPACOID
            GROUP BY ES.DESIGNACAO, OP.DataOperacao, OP.ID
        )
        WHERE DATA BETWEEN dataInicial AND dataFinal
        GROUP BY PARCELA, EXTRACT(YEAR FROM DATA), EXTRACT(MONTH FROM DATA)
        ORDER BY PARCELA, YEAR, MONTH;
    RETURN result_cursor;
END;
/

DECLARE
    result_cursor SYS_REFCURSOR;
    parcela VARCHAR2(100);
    data NUMBER;
    duracao NUMBER;
    year NUMBER;
    previous_parcela VARCHAR2(100) := NULL;
    data_found BOOLEAN := FALSE;
BEGIN
    result_cursor := getRegaMensal(TO_DATE('2023-06-01', 'YYYY-MM-DD'), TO_DATE('2023-11-06', 'YYYY-MM-DD'));
    LOOP
        FETCH result_cursor INTO parcela, year, data, duracao;
        EXIT WHEN result_cursor%NOTFOUND;
        data_found := TRUE;
        IF previous_parcela IS NULL OR previous_parcela != parcela  THEN
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('Parcela: '|| parcela);
            previous_parcela := parcela;
        END IF;
        DBMS_OUTPUT.PUT_LINE(CHR(9) || TRIM(TO_CHAR(TO_DATE(data, 'MM'), 'MONTH', 'NLS_DATE_LANGUAGE = PORTUGUESE')) || ' de ' || year || ' -> Duração: ' || duracao || 'min');    END LOOP;
    CLOSE result_cursor;
    IF NOT data_found THEN
        DBMS_OUTPUT.PUT_LINE('Sem resultados!');
    END IF;
END;
/