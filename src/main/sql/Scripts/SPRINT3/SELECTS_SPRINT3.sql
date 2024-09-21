--USBD24 & USBD25

SELECT O.ID AS ID_OPERACAO,O.DATAOPERACAO AS DATA,O.QUANTIDADE,O.UNIDADE,O.REGISTO, E.DESIGNACAO AS ESTADO
FROM OPERACAO O, ESTADO E, CADERNOCAMPO CC
WHERE O.ESTADOID = E.ID
ORDER BY O.ID DESC;

--USBD26

SELECT * FROM LOGOPERACAO ORDER BY ID DESC;

--USBD27

DELETE FROM LOGOPERACAO
WHERE ID = (SELECT MAX(ID) FROM LOGOPERACAO);

UPDATE LOGOPERACAO
SET TIPOREGISTO = 'Teste do Professor Ângelo'
WHERE ID = (SELECT MAX(ID) FROM LOGOPERACAO);

--USBD28

DELETE FROM OPERACAO
WHERE ID = (
    SELECT MAX(O.ID)
    FROM OPERACAO O, TIPOOPERACAO TOO, OPERACAOTIPOOPERACAO TOOP
    WHERE O.ID = TOOP.OPERACAOID
    AND TOOP.TIPOOPERACAOID = TOO.ID
    AND TOO.ID = 2
);

UPDATE OPERACAO set ESTADOID = 3 WHERE ID = 254;
--USBD31

SELECT R.ID AS RECEITA_ID, R.DESIGNACAO AS RECEITA_NOME, FP.DESIGNACAO AS FATOR_NOME, FP.FABRICANTE, RF.QUANTIDADE, RF.UNIDADE 
FROM RECEITA R, RECEITAFATOR RF, FATORPRODUCAO FP
WHERE R.ID = RF.RECEITAID AND 
RF.FATORPRODUCAOID = FP.ID
ORDER BY R.ID DESC;


--USBD32

SELECT O.ID AS OPERACAO_ID, O.DATAOPERACAO, O.REGISTO AS REGISTO_OPERACAO, S.DESIGNACAO AS SETOR, TOO.DESIGNACAO AS TIPOOPERACAO, O.QUANTIDADE AS TEMPO_OPERACAO, O.UNIDADE AS UNIDADE_OPERACAO, NULL AS QUANTIDADE_FATORPRODUCAO, NULL AS UNIDADE_FATORPRODUCAO, NULL AS NOME_FATORPRODUCAO, NULL AS FABRICANTE_FATORPRODUCAO
FROM OPERACAO O, OPERACAOSETOR OS, SETOR S, OPERACAOFATOR OFT, FATORPRODUCAO FP, TIPOOPERACAO TOO, OPERACAOTIPOOPERACAO TOOP
WHERE O.ID = OS.OPERACAOID
AND O.ID = TOOP.OPERACAOID
AND O.ID = OFT.OPERACAOID
AND TOOP.TIPOOPERACAOID = TOO.ID
AND OS.SETORID = S.ID
AND OFT.FATORPRODUCAOID = FP.ID
AND TOO.ID = 2
UNION
SELECT O.ID AS OPERACAO_ID, O.DATAOPERACAO, O.REGISTO AS REGISTO_OPERACAO, S.DESIGNACAO AS SETOR, TOO.DESIGNACAO AS TIPOOPERACAO, NULL AS TEMPO_OPERACAO, NULL AS UNIDADE_OPERACAO, OFT.QUANTIDADE AS QUANTIDADE_FATORPRODUCAO, OFT.UNIDADE AS UNIDADE_FATORPRODUCAO, FP.DESIGNACAO AS NOME_FATORPRODUCAO, FP.FABRICANTE AS FABRICANTE_FATORPRODUCAO
FROM OPERACAO O, OPERACAOSETOR OS, SETOR S, OPERACAOFATOR OFT, FATORPRODUCAO FP, TIPOOPERACAO TOO, OPERACAOTIPOOPERACAO TOOP
WHERE O.ID = OS.OPERACAOID
AND O.ID = TOOP.OPERACAOID
AND O.ID = OFT.OPERACAOID
AND TOOP.TIPOOPERACAOID = TOO.ID
AND OS.SETORID = S.ID
AND OFT.FATORPRODUCAOID = FP.ID
AND TOO.ID = 11
ORDER BY OPERACAO_ID DESC, TIPOOPERACAO DESC;