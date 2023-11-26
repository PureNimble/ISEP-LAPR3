CREATE OR REPLACE FUNCTION getProdutosColhidosList(parcelaID NUMBER,dataInicial DATE, dataFinal DATE) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT PARCELA, ESPECIE, PRODUTO, DATA
        FROM(
        SELECT ES.DESIGNACAO AS PARCELA, NE.ESPECIE AS ESPECIE, NP.DESIGNACAO || ' ' || C.VARIEDADE AS PRODUTO, O.DATAOPERACAO as data
            FROM ESPACO ES, NOMEESPECIE NE, NOMEPRODUTO NP, CULTURA C, TIPOOPERACAO TP, OPERACAO O, PLANTACAO P, OPERACAOPLANTACAO OP, PRODUTO PR
            WHERE ES.ID = parcelaID
            AND P.PARCELAESPACOID = ES.ID
            AND OP.PLANTACAOID = P.ID
            AND O.ID = OP.OPERACAOID
            AND TP.ID = 7
            AND TP.ID = O.TIPOOPERACAOID
            AND P.CULTURAID = PR.CULTURAID
            AND C.ID = PR.CULTURAID
            AND C.NOMEESPECIEID = NE.ID
            AND PR.NOMEPRODUTOID = NP.ID
        )
        WHERE data BETWEEN DATAINICIAL AND DATAFINAL
        ORDER BY ESPECIE DESC, PRODUTO;
    RETURN result_cursor;
END;
/

DECLARE
    result_cursor SYS_REFCURSOR;
    parcela VARCHAR2(255);
    especie VARCHAR2(255);
    produto VARCHAR2(255);
    data VARCHAR2(255);
    previous_especie VARCHAR2(100) := NULL;
    previous_produto VARCHAR2(100) := NULL;

    data_found BOOLEAN := FALSE;
BEGIN
    result_cursor := getProdutosColhidosList(108, TO_DATE('2023-05-20', 'YYYY-MM-DD'), TO_DATE('2023-11-06', 'YYYY-MM-DD'));
    LOOP
        FETCH result_cursor INTO parcela, especie, produto, data;
        EXIT WHEN result_cursor%NOTFOUND;
        data_found := TRUE;
        IF previous_especie IS NULL OR previous_especie != especie  THEN
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE(' -> Parcela: '|| parcela);
            DBMS_OUTPUT.PUT_LINE(' -> Especíe: ' || especie);
            previous_especie := especie;
        END IF;
        IF previous_produto IS NULL OR previous_produto != produto THEN
            DBMS_OUTPUT.PUT_LINE(CHR(9) || '-> Produto: ' || produto);
            previous_produto := produto;
        END IF;
        
    END LOOP;
    CLOSE result_cursor;
    IF NOT data_found THEN
        DBMS_OUTPUT.PUT_LINE('Sem resultados!');
    END IF;
END;
/