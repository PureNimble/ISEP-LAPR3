CREATE OR REPLACE PROCEDURE GetFactorWithMostAplications(
    p_StartDate DATE,
    p_EndDate DATE
)
IS
BEGIN
FOR rec IN (
    SELECT FatorDeProducao, TotalDeUsos
	FROM (
    	SELECT FatorDeProducao, TotalDeUsos
    	FROM (
        	SELECT FP.Designacao AS FatorDeProducao, count(AF.FatorDeProducaoID) AS TotalDeUsos
        	FROM FatorDeProducao FP
        	INNER JOIN AplicacaoFitofarmaco AF ON FP.ID = AF.FatorDeProducaoID
        	INNER JOIN Operacao OP ON AF.OperacaoID = OP.ID
        	WHERE OP.DataOperacao BETWEEN p_StartDate AND p_EndDate 
    		GROUP BY FP.Designacao
    		UNION ALL
       		SELECT FP.Designacao AS FatorDeProducao, count(F.FatorDeProducaoID) AS TotalDeUsos
      		FROM FatorDeProducao FP
  			INNER JOIN Fertilizacao F ON FP.ID = F.FatorDeProducaoID
       		INNER JOIN Operacao OP ON F.OperacaoID = OP.ID
       		WHERE OP.DataOperacao BETWEEN p_StartDate AND p_EndDate
       		GROUP BY fP.Designacao
   		) t
   		ORDER BY TotalDeUsos DESC
	)
	WHERE ROWNUM = 1
    ) LOOP
        DBMS_OUTPUT.PUT_LINE('Fator de produção: ' || rec.FatorDeProducao || '; Total de aplicações: ' || rec.TotalDeUsos);
END LOOP;
END;
/

BEGIN
    GetFactorWithMostAplications(TO_DATE('2003-01-01', 'yyyy-mm-dd'), TO_DATE('2023-12-31', 'yyyy-mm-dd'));
END;
