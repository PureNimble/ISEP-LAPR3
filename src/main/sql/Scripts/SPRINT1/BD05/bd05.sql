
CREATE OR REPLACE PROCEDURE getCollectedProductsByParcela(
    p_Parcela VARCHAR2,
    p_StartDate DATE,
    p_EndDate DATE
)
IS
BEGIN
    FOR rec IN (
        SELECT NP.NOMECOMUM || ' ' || C.VARIEDADE AS PRODUTO, MAX(O.Unidade) AS Unidade, SUM(O.Quantidade) AS Quantidade
        FROM Parcela P
        INNER JOIN Plantacao PL ON P.ID = PL.ParcelaID
        INNER JOIN Cultura C ON PL.CulturaID = C.ID
        INNER JOIN NomeEspecie NP ON C.NomeEspecieID = NP.ID
        INNER JOIN Operacao O ON PL.ID = O.PlantacaoID
        INNER JOIN TipoOperacao TOp ON O.TipoOperacaoID = TOp.ID
        WHERE TOp.Designacao = 'Colheita'
        AND UPPER(P.Designacao) = UPPER(p_Parcela)
        AND O.DataOperacao BETWEEN p_StartDate AND p_EndDate
        GROUP BY NP.NOMECOMUM, C.VARIEDADE
    )
    LOOP
        DBMS_OUTPUT.PUT_LINE(' Produto: ' || rec.Produto || '; Quantidade: ' || rec.Quantidade || ' ' || rec.Unidade );
    END LOOP;
END;
/
-- Call Function above
BEGIN
    getCollectedProductsByParcela('Horta nova', TO_DATE('2003-01-01', 'yyyy-mm-dd'), TO_DATE('2024-12-31', 'yyyy-mm-dd'));
END;
/

--Parcelas = 'Lameiro da ponte', 'Horta nova','Campo do poço','Campo da bouça','Vinha'
