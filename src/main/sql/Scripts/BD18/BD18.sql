CREATE OR REPLACE PROCEDURE GetOperacaoList(
    p_StartDate DATE,
    p_EndDate DATE,
    parcelaID NUMBER(10)
)
IS
BEGIN
    SELECT OP.OperacaoID AS OperacaoID, TipoOperacao.Designacao AS TipoDeOperacao
    FROM OperacaoParcela OP
    WHERE OP.OperacaoID = Operacao.ID
    AND Operacao.ID = TipoOperacao.ID
    AND Operacao.DataOperacao BETWEEN p_StartDate AND p_EndDate
    AND OP.ParcelaEspacoID = parcelaID
    GROUP BY OP.Designacao
END;
/

