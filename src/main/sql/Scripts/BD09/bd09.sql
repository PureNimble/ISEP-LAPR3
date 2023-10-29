CREATE OR REPLACE PROCEDURE getNumberOfAplicationsByFarm(
    p_FarmDesignation VARCHAR2,
    p_StartDate DATE,
    p_EndDate DATE
)
IS
    v_QuintaID NUMBER;
    v_QuintaDesignacao VARCHAR2(100);
BEGIN
    -- Retrieve the QuintaID and Designacao
    SELECT Q.ID, Q.Designacao
    INTO v_QuintaID, v_QuintaDesignacao
    FROM Quinta Q
    WHERE Q.Designacao = p_FarmDesignation;

    DBMS_OUTPUT.PUT_LINE('Quinta ID: ' || v_QuintaID);
    DBMS_OUTPUT.PUT_LINE('Nome da Quinta: ' || v_QuintaDesignacao);

    FOR rec IN (
        SELECT TP.Designacao AS Tipo, COUNT(DISTINCT AP.FATORDEPRODUCAOID) AS numeroOperacoes
        FROM Quinta Q
        INNER JOIN CadernoDeCampo CC ON Q.ID = CC.QuintaID         	 
    	INNER JOIN Operacao O ON CC.ID = O.CadernoDeCampoID		  
        INNER JOIN FERTILIZACAO F ON O.ID = F.OPERACAOID
        INNER JOIN FATORDEPRODUCAO FDP ON F.FATORDEPRODUCAOID = FDP.ID
        INNER JOIN TIPOPRODUTO TP ON FDP.TIPOPRODUTOID = TP.ID
        INNER JOIN APLICACAOPRODUTO AP ON FDP.ID = AP.FATORDEPRODUCAOID
        WHERE Q.Designacao = p_FarmDesignation
        AND O.DataOperacao BETWEEN p_StartDate AND p_EndDate
        GROUP BY TP.Designacao
        UNION ALL
        SELECT TP.Designacao  AS Tipo, COUNT(DISTINCT AP.FATORDEPRODUCAOID) AS numeroOperacoes
        FROM Quinta Q
    	INNER JOIN CadernoDeCampo CC ON Q.ID = CC.QuintaID         	 
    	INNER JOIN Operacao O ON CC.ID = O.CadernoDeCampoID		  
        INNER JOIN APLICACAOFITOFARMACO AF ON O.ID = AF.OPERACAOID
        INNER JOIN FATORDEPRODUCAO FDP ON AF.FATORDEPRODUCAOID = FDP.ID
        INNER JOIN TIPOPRODUTO TP ON FDP.TIPOPRODUTOID = TP.ID
        INNER JOIN APLICACAOPRODUTO AP ON FDP.ID = AP.FATORDEPRODUCAOID
        WHERE Q.Designacao = p_FarmDesignation
        AND O.DataOperacao BETWEEN p_StartDate AND p_EndDate
        GROUP BY TP.Designacao
    ) LOOP
        DBMS_OUTPUT.PUT_LINE('Tipo de Fator De Produção: ' || rec.Tipo || ';    Número de Operações: ' || rec.numeroOperacoes);
    END LOOP;
END;
/
BEGIN 
    getNumberOfAplicationsByFarm('Quinta Do Ângelo', TO_DATE('2003-01-01', 'yyyy-mm-dd'), TO_DATE('2022-12-31', 'yyyy-mm-dd')); 
END; 
/
