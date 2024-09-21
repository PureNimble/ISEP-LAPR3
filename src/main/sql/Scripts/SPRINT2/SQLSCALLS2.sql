--US11
BEGIN
    registerSemeadura(84,106, TO_DATE('2023-09-20', 'yyyy-mm-dd'),1.2, 0.5);
END;
/
BEGIN
    registerSemeadura(84,108, TO_DATE('2023-09-19', 'yyyy-mm-dd'),1.8, 0.75);
END;
/
--US12
BEGIN
    registerMonda(36,108, TO_DATE('2023-09-08', 'yyyy-mm-dd'), 0.5);
END;
/
BEGIN
    registerMonda(36,108, TO_DATE('2023-10-08', 'yyyy-mm-dd'), 0.5);
    END;
/
--US13
BEGIN
    registerColheita(11, 102, TO_DATE('2023-11-05', 'yyyy-mm-dd'), 100);
END;
/
BEGIN
    registerColheita(?, 102, TO_DATE('2023-10-05', 'yyyy-mm-dd'), 800);
END;
/ 
--US14
BEGIN
    registerFatorDeProducao(4000, 108, TO_DATE('2023-10-06', 'yyyy-mm-dd'), 1.1, 12, 1);
END;
/
BEGIN
    registerFatorDeProducao(8000, 108, TO_DATE('2023-10-08', 'yyyy-mm-dd'), 2.1, 12, 1);
END;
/
--US15
BEGIN
    registerPoda(20,102,11,TO_DATE('2023-11-06', 'yyyy-mm-dd'));
END;
/
BEGIN
    registerPoda(60,102,11,TO_DATE('2023-11-06', 'yyyy-mm-dd'));    
END;
/
--US16
DECLARE
    result_cursor SYS_REFCURSOR;
    parcela VARCHAR2(255);
    especie VARCHAR2(255);
    produto VARCHAR2(255);
    data VARCHAR2(255);
    previous_especie VARCHAR2(100) := NULL;
    previous_produto VARCHAR2(100) := NULL;

    data_found BOOLEAN := FALSE;
BEGIN
    result_cursor := getProdutosColhidosList(108, TO_DATE('2023-05-20', 'YYYY-MM-DD'), TO_DATE('2023-11-06', 'YYYY-MM-DD'));
    LOOP
        FETCH result_cursor INTO parcela, especie, produto, data;
        EXIT WHEN result_cursor%NOTFOUND;
        data_found := TRUE;
        IF previous_especie IS NULL OR previous_especie != especie  THEN
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE(' -> Parcela: '|| parcela);
            DBMS_OUTPUT.PUT_LINE(' -> Especíe: ' || especie);
            previous_especie := especie;
        END IF;
        IF previous_produto IS NULL OR previous_produto != produto THEN
            DBMS_OUTPUT.PUT_LINE(CHR(9) || '-> Produto: ' || produto);
            previous_produto := produto;
        END IF;
        
    END LOOP;
    CLOSE result_cursor;
    IF NOT data_found THEN
        DBMS_OUTPUT.PUT_LINE('Sem resultados!');
    END IF;
END;
/
--US17
DECLARE
    result_cursor SYS_REFCURSOR;
    fatorproducao VARCHAR2(100);
    quantidade VARCHAR2(100);
    elemento VARCHAR2(100);
    data DATE;
    previous_fatorproducao VARCHAR2(100) := NULL;
    previous_data DATE := NULL;
    data_found BOOLEAN := FALSE;
BEGIN
    result_cursor := getFatorProducaoElementosList(TO_DATE('2019-01-01', 'YYYY-MM-DD'), TO_DATE('2023-07-06', 'YYYY-MM-DD'), 105);
    FETCH result_cursor INTO fatorproducao, quantidade, elemento, data;
    WHILE result_cursor%FOUND LOOP
        data_found := TRUE;
        IF previous_fatorproducao IS NULL OR previous_fatorproducao != fatorproducao  THEN
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('Fator de Producao: ' || fatorproducao);
            previous_fatorproducao := fatorproducao;
        END IF;
        IF previous_data IS NULL OR previous_data != data THEN
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('Data: ' || data);
            DBMS_OUTPUT.PUT_LINE(' ');
            previous_data := data;
        END IF;
        DBMS_OUTPUT.PUT_LINE(CHR(9) || '-> Quantidade: ' || quantidade || ', Elemento: ' || elemento);
        FETCH result_cursor INTO fatorproducao, quantidade, elemento, data;
    END LOOP;
    CLOSE result_cursor;
    IF NOT data_found THEN
        DBMS_OUTPUT.PUT_LINE('Sem resultados!');
    END IF;
END;
/
--US18
DECLARE
result_cursor SYS_REFCURSOR;
    operacaoid number(10);
    tipo_de_operacao VARCHAR2(255);
    cultura VARCHAR2(255);
    data VARCHAR2(255);

    previous_tipoOperacao VARCHAR2(100) := NULL;

    data_found BOOLEAN := FALSE;
BEGIN
    result_cursor := GetOperacaoList(108, TO_DATE('2023-07-01', 'YYYY-MM-DD'), TO_DATE('2023-10-02', 'YYYY-MM-DD'));
    LOOP
FETCH result_cursor INTO operacaoid, tipo_de_operacao, data, cultura;
        EXIT WHEN result_cursor%NOTFOUND;
        data_found := TRUE;
        IF previous_tipoOperacao IS NULL OR previous_tipoOperacao <> tipo_de_operacao THEN
            DBMS_OUTPUT.PUT_LINE('---------------------------------');
            DBMS_OUTPUT.PUT_LINE('Tipo de Operação: ' || tipo_de_operacao);
            DBMS_OUTPUT.PUT_LINE('---------------------------------');
            previous_tipoOperacao := tipo_de_operacao;
END IF;
        DBMS_OUTPUT.PUT_LINE('-> Operação: ' || operacaoid || '-> Cultura: ' || cultura);
        DBMS_OUTPUT.PUT_LINE(CHR(9) || '-> Data: '|| data);
        DBMS_OUTPUT.PUT_LINE(' ');
END LOOP;
CLOSE result_cursor;
IF NOT data_found THEN
        DBMS_OUTPUT.PUT_LINE('Sem resultados!');
END IF;
END;
/
--US19
DECLARE
    result_cursor SYS_REFCURSOR;
    parcela VARCHAR2(255);
    cultura VARCHAR2(255);
    fatorproducao VARCHAR2(255);
    aplicacao VARCHAR2(255);
    data DATE;
    previous_parcela VARCHAR2(255);
    previous_aplicacao VARCHAR2(255);


    data_found BOOLEAN := FALSE;
BEGIN
    result_cursor := getFatorProducaoList(TO_DATE('2019-01-01', 'YYYY-MM-DD'), TO_DATE('2023-07-06', 'YYYY-MM-DD'));
    LOOP
        FETCH result_cursor INTO data, fatorproducao, cultura, aplicacao, parcela;
        EXIT WHEN result_cursor%NOTFOUND;
        data_found := TRUE;
        
        IF previous_parcela IS NULL OR previous_parcela != parcela THEN
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('Parcela: ' || parcela);
            
            IF previous_aplicacao = aplicacao THEN
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE(CHR(9)||'Aplicacao: ' || aplicacao);
                DBMS_OUTPUT.PUT_LINE(' ');
            END IF;
            
            previous_parcela := parcela;
        END IF;
        IF previous_aplicacao IS NULL OR previous_aplicacao != aplicacao THEN
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE(CHR(9)||'Aplicacao: ' || aplicacao);
            DBMS_OUTPUT.PUT_LINE(' ');
            previous_aplicacao := aplicacao;
        END IF;
        DBMS_OUTPUT.PUT_LINE(CHR(9) || CHR(9) || data || ' - ' || fatorproducao || ' - ' || cultura);

    END LOOP;
    CLOSE result_cursor;
    IF NOT data_found THEN
        DBMS_OUTPUT.PUT_LINE('Sem resultados, para o intervalo de tempo selecionado!');
    END IF;
END;
/
--US20
DECLARE
    result_cursor SYS_REFCURSOR;
    parcela VARCHAR2(100);
    data NUMBER;
    duracao NUMBER;
    year NUMBER;
    previous_parcela VARCHAR2(100) := NULL;
    data_found BOOLEAN := FALSE;
BEGIN
    result_cursor := getRegaMensal(TO_DATE('2023-06-01', 'YYYY-MM-DD'), TO_DATE('2023-11-06', 'YYYY-MM-DD'));
    LOOP
        FETCH result_cursor INTO parcela, year, data, duracao;
        EXIT WHEN result_cursor%NOTFOUND;
        data_found := TRUE;
        IF previous_parcela IS NULL OR previous_parcela != parcela  THEN
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('Parcela: '|| parcela);
            previous_parcela := parcela;
        END IF;
        DBMS_OUTPUT.PUT_LINE(CHR(9) || TRIM(TO_CHAR(TO_DATE(data, 'MM'), 'MONTH', 'NLS_DATE_LANGUAGE = PORTUGUESE')) || ' de ' || year || ' -> Duração: ' || duracao || 'min');    END LOOP;
    CLOSE result_cursor;
    IF NOT data_found THEN
        DBMS_OUTPUT.PUT_LINE('Sem resultados!');
    END IF;
END;
/