BEGIN
    --US005
    GETCOLLECTEDPRODUCTSBYPARCELA('Horta nova', TO_DATE('2003-01-01', 'yyyy-mm-dd'), TO_DATE('2024-12-31', 'yyyy-mm-dd'));
    --US006
    GETNUMBEROFFACTORSBYTYPE('Lameiro da ponte', TO_DATE('2003-01-01', 'yyyy-mm-dd'), TO_DATE('2022-12-31', 'yyyy-mm-dd'));
    GETNUMBEROFFACTORSBYTYPEV2('Lameiro da ponte', TO_DATE('2003-01-01', 'yyyy-mm-dd'), TO_DATE('2022-12-31', 'yyyy-mm-dd'));
    --US007
    GETNUMBEROFOPERATIONBYTYPE('Campo grande', TO_DATE('2003-01-01', 'yyyy-mm-dd'), TO_DATE('2022-12-31', 'yyyy-mm-dd'));
    --US008
    GETFACTORWITHMOSTAPLICATIONS(TO_DATE('2003-01-01', 'yyyy-mm-dd'), TO_DATE('2023-12-31', 'yyyy-mm-dd'));
    --US009
    GETNUMBEROFAPLICATIONSBYFARM('Quinta Do Ângelo', TO_DATE('2003-01-01', 'yyyy-mm-dd'), TO_DATE('2022-12-31', 'yyyy-mm-dd'));

END;
/