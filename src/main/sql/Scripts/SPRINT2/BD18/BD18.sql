CREATE OR REPLACE FUNCTION GetOperacaoList(parcelaID NUMBER,dataInicial DATE, dataFinal DATE) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
OPEN result_cursor FOR
SELECT OperacaoID, TipoDeOperacao, data, Cultura
FROM(
    SELECT OP.OperacaoID AS OperacaoID, TOP.Designacao AS TipoDeOperacao, O.DataOperacao as data, 'Sem cultura' as Cultura
    FROM OperacaoParcela OP, TipoOperacao TOP, Operacao O
    WHERE OP.OperacaoID = O.ID
      AND O.TIPOOPERACAOID = TOP.ID
      AND OP.ParcelaEspacoID = parcelaID
    UNION
    SELECT OP.OperacaoID AS OperacaoID, TOP.Designacao AS TipoDeOperacao, O.DataOperacao as data, NP.Designacao  ||' '|| CU.Variedade AS Cultura
    FROM OperacaoPlantacao OP, TipoOperacao TOP, Operacao O, Plantacao P, Cultura CU, Produto Po, NomeProduto NP
    WHERE OP.OperacaoID = O.ID
      AND O.TIPOOPERACAOID = TOP.ID
      AND OP.PlantacaoID = P.ID
      AND P.ParcelaEspacoID = parcelaID
      AND P.CulturaID = CU.ID
      AND P.CulturaID = PO.CulturaID
      AND PO.NomeProdutoID = NP.ID
    UNION
    SELECT OS.OperacaoID AS OperacaoID, TOP.Designacao AS TipoDeOperacao, O.DataOperacao as data, NE.NomeComum  ||' '|| CU.Variedade AS Cultura
    FROM OperacaoSetor OS, TipoOperacao TOP, Operacao O, Plantacao P, PlantacaoSetor PS, Setor S, Cultura CU, NomeEspecie NE
    WHERE OS.OperacaoID = O.ID
      AND O.TIPOOPERACAOID = TOP.ID
      AND OS.SetorID = S.ID
      AND P.PARCELAESPACOID = parcelaID
      AND P.ID = PS.PlantacaoID
      AND PS.SetorID = S.ID
      AND P.CulturaID = Cu.ID
      AND CU.NomeEspecieID = NE.ID
    )
WHERE data BETWEEN DATAINICIAL AND DATAFINAL
ORDER BY TipoDeOperacao, data;
RETURN result_cursor;
END;
/

DECLARE
result_cursor SYS_REFCURSOR;
    operacaoid number(10);
    tipo_de_operacao VARCHAR2(255);
    cultura VARCHAR2(255);
    data VARCHAR2(255);

    previous_tipoOperacao VARCHAR2(100) := NULL;

    data_found BOOLEAN := FALSE;
BEGIN
    result_cursor := GetOperacaoList(108, TO_DATE('2023-07-01', 'YYYY-MM-DD'), TO_DATE('2023-10-02', 'YYYY-MM-DD'));
    LOOP
FETCH result_cursor INTO operacaoid, tipo_de_operacao, data, cultura;
        EXIT WHEN result_cursor%NOTFOUND;
        data_found := TRUE;
        IF previous_tipoOperacao IS NULL OR previous_tipoOperacao <> tipo_de_operacao THEN
            DBMS_OUTPUT.PUT_LINE('---------------------------------');
            DBMS_OUTPUT.PUT_LINE('Tipo de Operação: ' || tipo_de_operacao);
            DBMS_OUTPUT.PUT_LINE('---------------------------------');
            previous_tipoOperacao := tipo_de_operacao;
END IF;
        DBMS_OUTPUT.PUT_LINE('-> Operação: ' || operacaoid || '-> Cultura: ' || cultura);
        DBMS_OUTPUT.PUT_LINE(CHR(9) || '-> Data: '|| data);
        DBMS_OUTPUT.PUT_LINE(' ');
END LOOP;
CLOSE result_cursor;
IF NOT data_found THEN
        DBMS_OUTPUT.PUT_LINE('Sem resultados!');
END IF;
END;
/