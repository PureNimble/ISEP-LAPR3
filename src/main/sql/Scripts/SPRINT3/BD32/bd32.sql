CREATE OR REPLACE PROCEDURE registerRega(quantidade NUMBER, setorID NUMBER, dataOperacao DATE, hora TIMESTAMP, estado NUMBER) IS

    TIPO_OPERACAO CONSTANT NUMBER := 2;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE CONSTANT VARCHAR2(10) := 'min';
    idOperacao NUMBER;

BEGIN
    verifySetorInfo(setorID);

    insertOperacao(dataOperacao, quantidade, UNIDADE, CADERNO_DE_CAMPO, estado, idOperacao);
    INSERT INTO OperacaoSetor(OperacaoID, HoraInicial, SetorID) VALUES (idOperacao, hora, setorID);
    INSERT INTO OperacaoTipoOperacao(OperacaoID, TipoOperacaoID) VALUES (idOperacao, TIPO_OPERACAO);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE registerFertirrega(quantidade NUMBER,setorID NUMBER,dataOperacao DATE,hora TIMESTAMP, receita NUMBER, estado NUMBER) IS

    TIPO_OPERACAO_REGA CONSTANT NUMBER := 2;
    TIPO_OPERACAO_FATOR CONSTANT NUMBER := 11;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE CONSTANT VARCHAR2(10) := 'min';
    idOperacao NUMBER;
    result_cursor SYS_REFCURSOR;
    FATORPRODUCAO NUMBER;
    QUANTIDADE_CURSOR NUMBER;
    UNIDADE_CURSOR VARCHAR2(10);

BEGIN
    verifySetorInfo(setorID);
    
    insertOperacao(dataOperacao, quantidade, UNIDADE, CADERNO_DE_CAMPO, estado, idOperacao);
    INSERT INTO OperacaoSetor(OperacaoID, HoraInicial, SetorID) VALUES (idOperacao, hora, setorID);
    INSERT INTO OperacaoTipoOperacao(OperacaoID, TipoOperacaoID) VALUES (idOperacao, TIPO_OPERACAO_REGA);
    INSERT INTO OperacaoTipoOperacao(OperacaoID, TipoOperacaoID) VALUES (idOperacao, TIPO_OPERACAO_FATOR);

    result_cursor := getFatorReceita(receita);
    LOOP
        FETCH result_cursor INTO FATORPRODUCAO, QUANTIDADE_CURSOR, UNIDADE_CURSOR;
        EXIT WHEN result_cursor%NOTFOUND;
        INSERT INTO OperacaoFator(OperacaoID, FatorProducaoID, Quantidade, Unidade)
        VALUES (idOperacao, FATORPRODUCAO, QUANTIDADE_CURSOR, UNIDADE_CURSOR);
    END LOOP;
    CLOSE result_cursor;
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/