CREATE OR REPLACE PROCEDURE registerReceita(designacaoReceita VARCHAR2(255)) IS
    idReceita NUMBER;

BEGIN
    --Obter o ID da receita
    SELECT NVL(MAX(ID),0) + 1 INTO idReceita FROM Receita;

    INSERT INTO Receita(ID, Designacao)
    VALUES (idReceita, designacaoReceita);
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Receita instanciada com sucesso!');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE addFatorToReceita(receita NUMBER, fator NUMBER, quantidade NUMBER, unidade VARCHAR2(255)) IS


BEGIN
    verifyFatorDeProducaoInfo(fator);
    INSERT INTO ReceitaFator(ReceitaID, FatorProducaoID, Quantidade, Unidade) VALUES (receita, fator, quantidade, unidade);
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Fator adicionade com sucesso!');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/