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
