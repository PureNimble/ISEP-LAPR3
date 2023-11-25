CREATE OR REPLACE FUNCTION GetOperacaoList(parcelaID NUMBER,dataInicial DATE, dataFinal DATE) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
    SELECT OperacaoID, TipoDeOperacao, data
    FROM(
        SELECT OP.OperacaoID AS OperacaoID, TOP.Designacao AS TipoDeOperacao, O.DataOperacao as data
        FROM OperacaoParcela OP, TipoOperacao TOP, Operacao O
        WHERE OP.OperacaoID = O.ID
          AND O.ID = TOP.ID
          AND OP.ParcelaEspacoID = parcelaID
    )
    WHERE data BETWEEN DATAINICIAL AND DATAFINAL
    ORDER BY OperacaoID;
RETURN result_cursor;
END;
/

DECLARE
result_cursor SYS_REFCURSOR;
    operacaoid number(10);
    tipo_de_operacao VARCHAR2(255);
    data VARCHAR2(255);

    data_found BOOLEAN := FALSE;
BEGIN
    result_cursor := GetOperacaoList(500, TO_DATE('2023-05-20', 'YYYY-MM-DD'), TO_DATE('2023-11-06', 'YYYY-MM-DD'));
    LOOP
        FETCH result_cursor INTO operacaoid, tipo_de_operacao, data;
        EXIT WHEN result_cursor%NOTFOUND;
        data_found := TRUE;
        DBMS_OUTPUT.PUT_LINE(result_cursor%ROWCOUNT||' º'  );
        DBMS_OUTPUT.PUT_LINE(' -> Operção: '|| operacaoid);
        DBMS_OUTPUT.PUT_LINE(' -> Tipo de Operação:' || cultura );
        DBMS_OUTPUT.PUT_LINE(' -> FatorProducao:'|| tipo_de_operacao);
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
