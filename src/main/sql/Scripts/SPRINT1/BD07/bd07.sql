CREATE OR REPLACE PROCEDURE GetNumberOfOperationByType(
    p_Parcela VARCHAR2,
    p_StartDate DATE,
    p_EndDate DATE
)
IS
BEGIN
FOR rec IN (
        SELECT TOp.Designacao AS Tipo, COUNT(OP.ID) AS numeroOperacoes
        FROM Parcela P
        INNER JOIN Operacao OP ON P.ID = OP.ParcelaID                   -- Obter a operação da parcela
        INNER JOIN TipoOperacao TOp ON OP.TipoOperacaoID = TOp.ID       -- Obter o tipo de operação
        WHERE P.Designacao = p_Parcela                                  -- Selecionar a parcela passada como parâmetro
        AND OP.DataOperacao BETWEEN p_StartDate AND p_EndDate           -- Selecionar o intervalo de datas passado como parâmetro
        GROUP BY TOp.Designacao
    ) LOOP
        DBMS_OUTPUT.PUT_LINE('Tipo de Operação: ' || rec.Tipo || '; Número de Operações: ' || rec.numeroOperacoes);
END LOOP;
END;
/

BEGIN
    GetNumberOfOperationByType('Campo grande', TO_DATE('2003-01-01', 'yyyy-mm-dd'), TO_DATE('2022-12-31', 'yyyy-mm-dd'));
END;
/