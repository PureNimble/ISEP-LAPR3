CREATE OR REPLACE PROCEDURE getNumberOfFactorsByType(
    p_Parcela VARCHAR2,
    p_StartDate DATE,
    p_EndDate DATE
)
IS
    v_ParcelaID NUMBER;
    v_ParcelaName VARCHAR2(100);
BEGIN
    SELECT ID, Designacao
    INTO v_ParcelaID, v_ParcelaName
    FROM Parcela
    WHERE UPPER(Designacao) = UPPER(p_Parcela);

    DBMS_OUTPUT.PUT_LINE('ID da Parcela: ' || v_ParcelaID);
    DBMS_OUTPUT.PUT_LINE('Nome da Parcela: ' || v_ParcelaName);

    FOR rec IN (
        SELECT TP.Designacao AS Tipo, COUNT(F.OperacaoID) AS numeroOperacoes
        FROM Operacao OP
        INNER JOIN Fertilizacao F ON OP.ID = F.OperacaoID             -- obter Fertilizações
        INNER JOIN FatorDeProducao FP ON F.FatorDeProducaoID = FP.ID  -- obter Fatores de Produção
        INNER JOIN TipoProduto TP ON FP.TipoProdutoID = TP.ID         -- obter Tipo de Produto
        WHERE OP.ParcelaID = v_ParcelaID
        AND OP.DataOperacao BETWEEN p_StartDate AND p_EndDate
        GROUP BY TP.Designacao
        UNION ALL
        SELECT TP.Designacao AS Tipo, COUNT(AF.OperacaoID) AS numeroOperacoes
        FROM Operacao OP
        INNER JOIN AplicacaoFitofarmaco AF ON OP.ID = AF.OperacaoID   -- obter Aplicações Fitofarmacos
        INNER JOIN FatorDeProducao FP ON AF.FatorDeProducaoID = FP.ID -- obter Fatores de Produção
        INNER JOIN TipoProduto TP ON FP.TipoProdutoID = TP.ID         -- obter Tipo de Produto
        WHERE OP.ParcelaID = v_ParcelaID
        AND OP.DataOperacao BETWEEN p_StartDate AND p_EndDate
        GROUP BY TP.Designacao
    ) LOOP
        DBMS_OUTPUT.PUT_LINE('Tipo de Fator De Produção: ' || rec.Tipo || '; Número de Operações: ' || rec.numeroOperacoes);
    END LOOP;
END;
/

CREATE OR REPLACE PROCEDURE getNumberOfFactorsByTypeV2(
    p_Parcela VARCHAR2,
    p_StartDate DATE,
    p_EndDate DATE
)
IS
    v_ParcelaID NUMBER;
    v_ParcelaName VARCHAR2(100);
BEGIN
    -- Retrieve the Parcela ID and Name
    SELECT ID, Designacao
    INTO v_ParcelaID, v_ParcelaName
    FROM Parcela
    WHERE UPPER(Designacao) = UPPER(p_Parcela);

    DBMS_OUTPUT.PUT_LINE('ID da Parcela: ' || v_ParcelaID);
    DBMS_OUTPUT.PUT_LINE('Nome da Parcela: ' || v_ParcelaName);

    FOR rec IN (
        SELECT TP.Designacao AS Tipo, COUNT(DISTINCT FP.Designacao) AS numeroOperacoes
        FROM Operacao OP
        INNER JOIN Fertilizacao F ON OP.ID = F.OperacaoID              -- obter Fertilizações
        INNER JOIN FatorDeProducao FP ON F.FatorDeProducaoID = FP.ID   -- obter Fatores de Produção
        INNER JOIN TipoProduto TP ON FP.TipoProdutoID = TP.ID          -- obter Tipo de Produto
        WHERE OP.ParcelaID = v_ParcelaID
        AND OP.DataOperacao BETWEEN p_StartDate AND p_EndDate
        GROUP BY TP.Designacao
        UNION ALL
        SELECT TP.Designacao AS Tipo, COUNT(DISTINCT FP.Designacao) AS numeroOperacoes
        FROM Operacao OP
        INNER JOIN AplicacaoFitofarmaco AF ON OP.ID = AF.OperacaoID    -- obter Aplicações Fitofarmacos
        INNER JOIN FatorDeProducao FP ON AF.FatorDeProducaoID = FP.ID  -- obter Fatores de Produção
        INNER JOIN TipoProduto TP ON FP.TipoProdutoID = TP.ID          -- obter Tipo de Produto
        WHERE OP.ParcelaID = v_ParcelaID
        AND OP.DataOperacao BETWEEN p_StartDate AND p_EndDate
        GROUP BY TP.Designacao
    ) LOOP
        DBMS_OUTPUT.PUT_LINE('Tipo de Fator De Produção: ' || rec.Tipo || '; Número de Fatores de Produção: ' || rec.numeroOperacoes);
    END LOOP;
END;
/

BEGIN 
    getNumberOfFactorsByType('Lameiro da ponte', TO_DATE('2003-01-01', 'yyyy-mm-dd'), TO_DATE('2022-12-31', 'yyyy-mm-dd')); 
    getNumberOfFactorsByTypeV2('Lameiro da ponte', TO_DATE('2003-01-01', 'yyyy-mm-dd'), TO_DATE('2022-12-31', 'yyyy-mm-dd')); 
END; 
/