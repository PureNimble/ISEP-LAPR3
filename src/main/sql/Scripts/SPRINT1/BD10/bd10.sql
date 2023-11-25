SELECT P.Designacao AS Parcela, COUNT(O.ID) AS NUMERO_DE_OPERACOES
FROM Parcela P
INNER JOIN Operacao O ON P.ID = O.ParcelaID				                            -- Obter a operação da parcela
INNER JOIN TipoOperacao TOp ON O.TipoOperacaoID = TOp.ID		                    -- Obter o tipo de operação
WHERE TOp.Designacao = 'Rega'
GROUP BY P.Designacao
HAVING  COUNT(O.ID) = (SELECT MAX(COUNT(O.ID)) FROM Parcela P
                        INNER JOIN Operacao O ON P.ID = O.ParcelaID
                        INNER JOIN TipoOperacao TOp ON O.TipoOperacaoID = TOp.ID
                        WHERE TOp.Designacao = 'Rega'
                        GROUP BY P.Designacao);