CREATE OR REPLACE PROCEDURE registerRega(quantidade NUMBER, setorID NUMBER, dataOperacao DATE, hora TIMESTAMP, estado NUMBER) IS

    TIPO_OPERACAO CONSTANT NUMBER := 2;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE CONSTANT VARCHAR2(10) := 'min';
    idOperacao NUMBER;

BEGIN
    verifySetorInfo(setorID);

    insertOperacao(dataOperacao, quantidade, UNIDADE, TIPO_OPERACAO, CADERNO_DE_CAMPO, estado, idOperacao);
    INSERT INTO OperacaoSetor(OperacaoID, HoraInicial, SetorID) VALUES (idOperacao, hora, setorID);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE registerFertirrega(quantidade NUMBER,setorID NUMBER,dataOperacao DATE,hora TIMESTAMP, receita NUMBER, estado NUMBER) IS

    TIPO_OPERACAO CONSTANT NUMBER := 11;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE CONSTANT VARCHAR2(10) := 'min';
    idOperacao NUMBER;

BEGIN
    verifySetorInfo(setorID);
    
    insertOperacao(dataOperacao, quantidade, UNIDADE, TIPO_OPERACAO, CADERNO_DE_CAMPO, estado, idOperacao);
    INSERT INTO OperacaoReceita(OperacaoID, ReceitaID) VALUES (idOperacao, receita);
    INSERT INTO OperacaoSetor(OperacaoID, HoraInicial, SetorID) VALUES (idOperacao, hora, setorID);
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/