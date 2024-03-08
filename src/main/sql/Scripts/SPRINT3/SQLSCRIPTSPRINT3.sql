DROP TRIGGER CapacidadeTrigger;
DROP TRIGGER CheckUniqueEspacoIDRega;
DROP TRIGGER CheckUniqueEspacoIDEstabulo;
DROP TRIGGER CheckUniqueEspacoIDGagarem;
DROP TRIGGER CheckUniqueEspacoIDArmazem;
DROP TRIGGER CheckUniqueEspacoIDParcela;
DROP TRIGGER PreventLogChanges;
DROP TRIGGER CheckUniqueOperacaoIDSetor;
DROP TRIGGER CheckUniqueOperacaoIDParcela;
DROP TRIGGER ChechUniqueOperacaoIDPlantacao;
DROP TRIGGER OperacaoID;
DROP TRIGGER InsertLogOperacao;
DROP TRIGGER PreventDeleteOperacao;
DROP TRIGGER PreventUpdateAfterThreeDays;
DROP TRIGGER CheckPlantacaoUnidade;
DROP TRIGGER CheckCaudal;
DROP TRIGGER CheckUniqueTipoSensor;
DROP TRIGGER CheckUniqueSensorIDEstacao;
DROP TRIGGER CheckUniqueSensorIDParcela;
DROP TABLE Aplicacao CASCADE CONSTRAINTS;
DROP TABLE AplicacaoProduto CASCADE CONSTRAINTS;
DROP TABLE Armazem CASCADE CONSTRAINTS;
DROP TABLE CadernoCampo CASCADE CONSTRAINTS;
DROP TABLE Cultura CASCADE CONSTRAINTS;
DROP TABLE Elemento CASCADE CONSTRAINTS;
DROP TABLE ElementoFicha CASCADE CONSTRAINTS;
DROP TABLE Espaco CASCADE CONSTRAINTS;
DROP TABLE Estabulo CASCADE CONSTRAINTS;
DROP TABLE EstacaoMeteorologica CASCADE CONSTRAINTS;
DROP TABLE Estado CASCADE CONSTRAINTS;
DROP TABLE FatorProducao CASCADE CONSTRAINTS;
DROP TABLE Fertilizacao CASCADE CONSTRAINTS;
DROP TABLE FormatoProduto CASCADE CONSTRAINTS;
DROP TABLE Garagem CASCADE CONSTRAINTS;
DROP TABLE LogOperacao CASCADE CONSTRAINTS;
DROP TABLE ModoFertilizacao CASCADE CONSTRAINTS;
DROP TABLE NomeEspecie CASCADE CONSTRAINTS;
DROP TABLE NomeProduto CASCADE CONSTRAINTS;
DROP TABLE Operacao CASCADE CONSTRAINTS;
DROP TABLE OperacaoFator CASCADE CONSTRAINTS;
DROP TABLE OperacaoParcela CASCADE CONSTRAINTS;
DROP TABLE OperacaoPlantacao CASCADE CONSTRAINTS;
DROP TABLE OperacaoSetor CASCADE CONSTRAINTS;
DROP TABLE OperacaoTipoOperacao CASCADE CONSTRAINTS;
DROP TABLE Parcela CASCADE CONSTRAINTS;
DROP TABLE PlanoPlantacao CASCADE CONSTRAINTS;
DROP TABLE PlanoSetor CASCADE CONSTRAINTS;
DROP TABLE Plantacao CASCADE CONSTRAINTS;
DROP TABLE PlantacaoPermanente CASCADE CONSTRAINTS;
DROP TABLE PlantacaoSetor CASCADE CONSTRAINTS;
DROP TABLE Produto CASCADE CONSTRAINTS;
DROP TABLE ProdutoArmazem CASCADE CONSTRAINTS;
DROP TABLE Quinta CASCADE CONSTRAINTS;
DROP TABLE Receita CASCADE CONSTRAINTS;
DROP TABLE ReceitaFator CASCADE CONSTRAINTS;
DROP TABLE Rega CASCADE CONSTRAINTS;
DROP TABLE RegistoSensor CASCADE CONSTRAINTS;
DROP TABLE Sensor CASCADE CONSTRAINTS;
DROP TABLE SensorEstacao CASCADE CONSTRAINTS;
DROP TABLE SensorParcela CASCADE CONSTRAINTS;
DROP TABLE Setor CASCADE CONSTRAINTS;
DROP TABLE SistemaRega CASCADE CONSTRAINTS;
DROP TABLE TipoCultura CASCADE CONSTRAINTS;
DROP TABLE TipoOperacao CASCADE CONSTRAINTS;
DROP TABLE TipoProduto CASCADE CONSTRAINTS;
DROP TABLE TipoSensor CASCADE CONSTRAINTS;
DROP SEQUENCE LogOperacaoNextID;
DROP SEQUENCE OperacaoNextID;

CREATE SEQUENCE LogOperacaoNextID START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE OperacaoNextID START WITH 1 INCREMENT BY 1;
CREATE TABLE APLICACAO (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE,
    PRIMARY KEY (ID)
);

CREATE TABLE APLICACAOPRODUTO (
    FATORPRODUCAOID NUMBER(10) NOT NULL,
    APLICACAOID NUMBER(10) NOT NULL,
    PRIMARY KEY (FATORPRODUCAOID, APLICACAOID)
);

CREATE TABLE ARMAZEM (
    ESPACOID NUMBER(10) NOT NULL,
    CAPACIDADE NUMBER(10) DEFAULT 3000 NOT NULL CHECK(CAPACIDADE > 0),
    PRIMARY KEY (ESPACOID)
);

CREATE TABLE CADERNOCAMPO (
    ID NUMBER(10) NOT NULL,
    QUINTAID NUMBER(10) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE CULTURA (
    ID NUMBER(10) NOT NULL,
    VARIEDADE VARCHAR2(255) NOT NULL,
    PODA VARCHAR2(255),
    FLORACAO VARCHAR2(255),
    COLHEITA VARCHAR2(255),
    SEMENTEIRA VARCHAR2(255),
    NOMEESPECIEID NUMBER(10) NOT NULL,
    TIPOCULTURAID NUMBER(10) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE ELEMENTO (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE,
    PRIMARY KEY (ID)
);

CREATE TABLE ELEMENTOFICHA (
    FATORPRODUCAOID NUMBER(10) NOT NULL,
    ELEMENTOID NUMBER(10) NOT NULL,
    QUANTIDADE DOUBLE PRECISION NOT NULL CHECK(QUANTIDADE > 0 AND QUANTIDADE <= 100),
    PRIMARY KEY (FATORPRODUCAOID, ELEMENTOID)
);

CREATE TABLE ESPACO (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE,
    AREA DOUBLE PRECISION NOT NULL CHECK(AREA > 0),
    UNIDADE VARCHAR2(255) NOT NULL,
    DATACRIACAO DATE,
    QUINTAID NUMBER(10) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE ESTABULO (
    ESPACOID NUMBER(10) NOT NULL,
    PRIMARY KEY (ESPACOID)
);

CREATE TABLE ESTACAOMETEOROLOGICA (
    ID NUMBER(10) NOT NULL,
    QUINTAID NUMBER(10) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE ESTADO (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE CHECK(DESIGNACAO IN ('Pendente', 'Executada', 'Anulada')),
    PRIMARY KEY (ID)
);

CREATE TABLE FATORPRODUCAO (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE,
    FABRICANTE VARCHAR2(255) NOT NULL,
    MATERIAORGANICA DOUBLE PRECISION CHECK(MATERIAORGANICA >= 0 AND MATERIAORGANICA <= 100),
    PH DOUBLE PRECISION CHECK(PH >= 0 AND PH <= 14),
    FORMATOPRODUTOID NUMBER(10) NOT NULL,
    TIPOPRODUTOID NUMBER(10) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE FERTILIZACAO (
    OPERACAOID NUMBER(10) NOT NULL,
    MODOFERTILIZACAOID NUMBER(10) NOT NULL,
    PRIMARY KEY (OPERACAOID)
);

CREATE TABLE FORMATOPRODUTO (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE,
    PRIMARY KEY (ID)
);

CREATE TABLE GARAGEM (
    ESPACOID NUMBER(10) NOT NULL,
    PRIMARY KEY (ESPACOID)
);

CREATE TABLE LOGOPERACAO (
    ID NUMBER(10) NOT NULL,
    OPERACAOID NUMBER(10) NOT NULL,
    DATAOPERACAO DATE NOT NULL,
    QUANTIDADE DOUBLE PRECISION NOT NULL,
    UNIDADE VARCHAR2(255) NOT NULL,
    ESTADO VARCHAR2(255) NOT NULL,
    REGISTO TIMESTAMP(0) NOT NULL,
    TIPOREGISTO VARCHAR2(255) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE MODOFERTILIZACAO (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE,
    PRIMARY KEY (ID)
);

CREATE TABLE NOMEESPECIE (
    ID NUMBER(10) NOT NULL,
    NOMECOMUM VARCHAR2(255) NOT NULL,
    ESPECIE VARCHAR2(255) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE NOMEPRODUTO (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE,
    PRIMARY KEY (ID)
);

CREATE TABLE OPERACAO (
    ID NUMBER(10) NOT NULL,
    DATAOPERACAO DATE NOT NULL,
    QUANTIDADE DOUBLE PRECISION NOT NULL CHECK(QUANTIDADE > 0),
    UNIDADE VARCHAR2(255) NOT NULL,
    REGISTO TIMESTAMP(0) DEFAULT SYSTIMESTAMP NOT NULL,
    CADERNOCAMPOID NUMBER(10) NOT NULL,
    ESTADOID NUMBER(10) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE OPERACAOFATOR (
    OPERACAOID NUMBER(10) NOT NULL,
    FATORPRODUCAOID NUMBER(10) NOT NULL,
    QUANTIDADE DOUBLE PRECISION NOT NULL,
    UNIDADE VARCHAR2(255) NOT NULL,
    PRIMARY KEY (OPERACAOID, FATORPRODUCAOID)
);

CREATE TABLE OPERACAOPARCELA (
    OPERACAOID NUMBER(10) NOT NULL,
    PARCELAESPACOID NUMBER(10) NOT NULL,
    PRIMARY KEY (OPERACAOID)
);

CREATE TABLE OPERACAOPLANTACAO (
    OPERACAOID NUMBER(10) NOT NULL,
    PLANTACAOID NUMBER(10) NOT NULL,
    PRIMARY KEY (OPERACAOID)
);

CREATE TABLE OPERACAOSETOR (
    OPERACAOID NUMBER(10) NOT NULL,
    HORAINICIAL TIMESTAMP(0) NOT NULL,
    SETORID NUMBER(10) NOT NULL,
    PRIMARY KEY (OPERACAOID)
);

CREATE TABLE OPERACAOTIPOOPERACAO (
    OPERACAOID NUMBER(10) NOT NULL,
    TIPOOPERACAOID NUMBER(10) NOT NULL,
    PRIMARY KEY (OPERACAOID, TIPOOPERACAOID)
);

CREATE TABLE PARCELA (
    ESPACOID NUMBER(10) NOT NULL,
    PRIMARY KEY (ESPACOID)
);

CREATE TABLE PLANOPLANTACAO (
    PLANTACAOSETORPLANTACAOID NUMBER(10) NOT NULL,
    DATAINICIAL DATE NOT NULL,
    DATAFINAL DATE,
    PRIMARY KEY (PLANTACAOSETORPLANTACAOID),
    CONSTRAINT CHECKDATESPLANOPLANTACAO CHECK (DATAINICIAL <= DATAFINAL)
);

CREATE TABLE PLANOSETOR (
    SETORID NUMBER(10) NOT NULL,
    DATAINICIAL DATE NOT NULL,
    DATAFINAL DATE,
    CAUDAL NUMBER(10) NOT NULL CHECK(CAUDAL > 0),
    SISTEMAREGAQUINTAID NUMBER(10) NOT NULL,
    PRIMARY KEY (SETORID),
    CONSTRAINT CHECKDATESPLANOSETOR CHECK (DATAINICIAL<= DATAFINAL)
);

CREATE TABLE PLANTACAO (
    ID NUMBER(10) NOT NULL,
    DATAINICIAL DATE NOT NULL,
    DATAFINAL DATE,
    QUANTIDADE DOUBLE PRECISION NOT NULL CHECK(QUANTIDADE > 0),
    UNIDADE VARCHAR2(255) NOT NULL,
    ESTADOFENOLOGICO VARCHAR2(255),
    CULTURAID NUMBER(10) NOT NULL,
    PARCELAESPACOID NUMBER(10) NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT CHECKDATESPLANTACAO CHECK (DATAINICIAL <= DATAFINAL)
);

CREATE TABLE PLANTACAOPERMANENTE (
    PLANTACAOID NUMBER(10) NOT NULL,
    COMPASSO DOUBLE PRECISION NOT NULL,
    DISTANCIAFILAS DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (PLANTACAOID)
);

CREATE TABLE PLANTACAOSETOR (
    PLANTACAOID NUMBER(10) NOT NULL,
    SETORID NUMBER(10) NOT NULL,
    PRIMARY KEY (PLANTACAOID)
);

CREATE TABLE PRODUTO (
    CULTURAID NUMBER(10) NOT NULL,
    NOMEPRODUTOID NUMBER(10) NOT NULL,
    PRIMARY KEY (CULTURAID)
);

CREATE TABLE PRODUTOARMAZEM (
    PRODUTOCULTURAID NUMBER(10) NOT NULL,
    ARMAZEMESPACOID NUMBER(10) NOT NULL,
    QUANTIDADE NUMBER(10) DEFAULT 0 NOT NULL CHECK(QUANTIDADE >= 0),
    PRIMARY KEY (PRODUTOCULTURAID, ARMAZEMESPACOID)
);

CREATE TABLE QUINTA (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE RECEITA (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE,
    PRIMARY KEY (ID)
);

CREATE TABLE RECEITAFATOR (
    RECEITAID NUMBER(10) NOT NULL,
    FATORPRODUCAOID NUMBER(10) NOT NULL,
    QUANTIDADE DOUBLE PRECISION NOT NULL CHECK(QUANTIDADE > 0),
    UNIDADE VARCHAR2(255) NOT NULL,
    PRIMARY KEY (RECEITAID, FATORPRODUCAOID)
);

CREATE TABLE REGA (
    ESPACOID NUMBER(10) NOT NULL,
    PRIMARY KEY (ESPACOID)
);

CREATE TABLE REGISTOSENSOR (
    ID NUMBER(10) NOT NULL,
    VALOR DOUBLE PRECISION NOT NULL,
    DATAREGISTO DATE NOT NULL,
    SENSORID NUMBER(10) NOT NULL,
    CADERNOCAMPOID NUMBER(10) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE SENSOR (
    ID NUMBER(10) NOT NULL,
    TIPOSENSORID NUMBER(10) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE SENSORESTACAO (
    SENSORID NUMBER(10) NOT NULL,
    ESTACAOMETEOROLOGICAID NUMBER(10) NOT NULL,
    PRIMARY KEY (SENSORID)
);

CREATE TABLE SENSORPARCELA (
    SENSORID NUMBER(10) NOT NULL,
    PARCELAESPACOID NUMBER(10) NOT NULL,
    PRIMARY KEY (SENSORID)
);

CREATE TABLE SETOR (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE,
    PRIMARY KEY (ID)
);

CREATE TABLE SISTEMAREGA (
    QUINTAID NUMBER(10) NOT NULL,
    DEBITOMAXIMO NUMBER(10) NOT NULL CHECK(DEBITOMAXIMO > 0),
    PRIMARY KEY (QUINTAID)
);

CREATE TABLE TIPOCULTURA (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE CHECK(DESIGNACAO IN ('Permanente', 'Temporária')),
    PRIMARY KEY (ID)
);

CREATE TABLE TIPOOPERACAO (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE,
    PRIMARY KEY (ID)
);

CREATE TABLE TIPOPRODUTO (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE,
    PRIMARY KEY (ID)
);

CREATE TABLE TIPOSENSOR (
    ID NUMBER(10) NOT NULL,
    DESIGNACAO VARCHAR2(255) NOT NULL,
    UNIDADE VARCHAR2(255) NOT NULL,
    PRIMARY KEY (ID)
);

ALTER TABLE Sensor ADD CONSTRAINT FKSensor879684 FOREIGN KEY (TipoSensorID) REFERENCES TipoSensor (ID);
ALTER TABLE Plantacao ADD CONSTRAINT FKPlantacao171838 FOREIGN KEY (CulturaID) REFERENCES Cultura (ID);
ALTER TABLE Espaco ADD CONSTRAINT FKEspaco1654 FOREIGN KEY (QuintaID) REFERENCES Quinta (ID);
ALTER TABLE AplicacaoProduto ADD CONSTRAINT FKAplicacaoP927032 FOREIGN KEY (FatorProducaoID) REFERENCES FatorProducao (ID);
ALTER TABLE Plantacao ADD CONSTRAINT FKPlantacao969155 FOREIGN KEY (ParcelaEspacoID) REFERENCES Parcela (EspacoID);
ALTER TABLE EstacaoMeteorologica ADD CONSTRAINT FKEstacaoMet933281 FOREIGN KEY (QuintaID) REFERENCES Quinta (ID);
ALTER TABLE ElementoFicha ADD CONSTRAINT FKElementoFi523793 FOREIGN KEY (ElementoID) REFERENCES Elemento (ID);
ALTER TABLE RegistoSensor ADD CONSTRAINT FKRegistoSen307126 FOREIGN KEY (SensorID) REFERENCES Sensor (ID);
ALTER TABLE RegistoSensor ADD CONSTRAINT FKRegistoSen457242 FOREIGN KEY (CadernoCampoID) REFERENCES CadernoCampo (ID);
ALTER TABLE Operacao ADD CONSTRAINT FKOperacao517013 FOREIGN KEY (CadernoCampoID) REFERENCES CadernoCampo (ID);
ALTER TABLE AplicacaoProduto ADD CONSTRAINT FKAplicacaoP648154 FOREIGN KEY (AplicacaoID) REFERENCES Aplicacao (ID);
ALTER TABLE Cultura ADD CONSTRAINT FKCultura266968 FOREIGN KEY (NomeEspecieID) REFERENCES NomeEspecie (ID);
ALTER TABLE FatorProducao ADD CONSTRAINT FKFatorProdu308270 FOREIGN KEY (TipoProdutoID) REFERENCES TipoProduto (ID);
ALTER TABLE ElementoFicha ADD CONSTRAINT FKElementoFi948499 FOREIGN KEY (FatorProducaoID) REFERENCES FatorProducao (ID);
ALTER TABLE Cultura ADD CONSTRAINT FKCultura303785 FOREIGN KEY (TipoCulturaID) REFERENCES TipoCultura (ID);
ALTER TABLE ProdutoArmazem ADD CONSTRAINT FKProdutoArm383884 FOREIGN KEY (ArmazemEspacoID) REFERENCES Armazem (EspacoID);
ALTER TABLE Armazem ADD CONSTRAINT FKArmazem321482 FOREIGN KEY (EspacoID) REFERENCES Espaco (ID);
ALTER TABLE Parcela ADD CONSTRAINT FKParcela936398 FOREIGN KEY (EspacoID) REFERENCES Espaco (ID);
ALTER TABLE PlantacaoSetor ADD CONSTRAINT FKPlantacaoS872782 FOREIGN KEY (PlantacaoID) REFERENCES Plantacao (ID);
ALTER TABLE PlantacaoSetor ADD CONSTRAINT FKPlantacaoS6264 FOREIGN KEY (SetorID) REFERENCES Setor (ID);
ALTER TABLE Estabulo ADD CONSTRAINT FKEstabulo107470 FOREIGN KEY (EspacoID) REFERENCES Espaco (ID);
ALTER TABLE Garagem ADD CONSTRAINT FKGaragem280599 FOREIGN KEY (EspacoID) REFERENCES Espaco (ID);
ALTER TABLE CadernoCampo ADD CONSTRAINT FKCadernoCam897586 FOREIGN KEY (QuintaID) REFERENCES Quinta (ID);
ALTER TABLE SensorParcela ADD CONSTRAINT FKSensorParc805893 FOREIGN KEY (ParcelaEspacoID) REFERENCES Parcela (EspacoID);
ALTER TABLE SensorParcela ADD CONSTRAINT FKSensorParc742877 FOREIGN KEY (SensorID) REFERENCES Sensor (ID);
ALTER TABLE SensorEstacao ADD CONSTRAINT FKSensorEsta509551 FOREIGN KEY (EstacaoMeteorologicaID) REFERENCES EstacaoMeteorologica (ID);
ALTER TABLE SensorEstacao ADD CONSTRAINT FKSensorEsta239503 FOREIGN KEY (SensorID) REFERENCES Sensor (ID);
ALTER TABLE Fertilizacao ADD CONSTRAINT FKFertilizac483896 FOREIGN KEY (OperacaoID) REFERENCES Operacao (ID);
ALTER TABLE Fertilizacao ADD CONSTRAINT FKFertilizac729538 FOREIGN KEY (ModoFertilizacaoID) REFERENCES ModoFertilizacao (ID);
ALTER TABLE OperacaoFator ADD CONSTRAINT FKOperacaoFa121712 FOREIGN KEY (OperacaoID) REFERENCES Operacao (ID);
ALTER TABLE OperacaoFator ADD CONSTRAINT FKOperacaoFa436815 FOREIGN KEY (FatorProducaoID) REFERENCES FatorProducao (ID);
ALTER TABLE ProdutoArmazem ADD CONSTRAINT FKProdutoArm466455 FOREIGN KEY (ProdutoCulturaID) REFERENCES Produto (CulturaID);
ALTER TABLE PlanoSetor ADD CONSTRAINT FKPlanoSetor822837 FOREIGN KEY (SetorID) REFERENCES Setor (ID);
ALTER TABLE OperacaoPlantacao ADD CONSTRAINT FKOperacaoPl849173 FOREIGN KEY (OperacaoID) REFERENCES Operacao (ID);
ALTER TABLE OperacaoPlantacao ADD CONSTRAINT FKOperacaoPl732201 FOREIGN KEY (PlantacaoID) REFERENCES Plantacao (ID);
ALTER TABLE OperacaoParcela ADD CONSTRAINT FKOperacaoPa455939 FOREIGN KEY (OperacaoID) REFERENCES Operacao (ID);
ALTER TABLE OperacaoParcela ADD CONSTRAINT FKOperacaoPa344730 FOREIGN KEY (ParcelaEspacoID) REFERENCES Parcela (EspacoID);
ALTER TABLE PlanoPlantacao ADD CONSTRAINT FKPlanoPlant813901 FOREIGN KEY (PlantacaoSetorPlantacaoID) REFERENCES PlantacaoSetor (PlantacaoID);
ALTER TABLE Rega ADD CONSTRAINT FKRega269439 FOREIGN KEY (EspacoID) REFERENCES Espaco (ID);
ALTER TABLE SistemaRega ADD CONSTRAINT FKSistemaReg724918 FOREIGN KEY (QuintaID) REFERENCES Quinta (ID);
ALTER TABLE Produto ADD CONSTRAINT FKProduto214120 FOREIGN KEY (NomeProdutoID) REFERENCES NomeProduto (ID);
ALTER TABLE Produto ADD CONSTRAINT FKProduto49 FOREIGN KEY (CulturaID) REFERENCES Cultura (ID);
ALTER TABLE FatorProducao ADD CONSTRAINT FKFatorProdu289783 FOREIGN KEY (FormatoProdutoID) REFERENCES FormatoProduto (ID);
ALTER TABLE OperacaoSetor ADD CONSTRAINT FKOperacaoSe277035 FOREIGN KEY (SetorID) REFERENCES Setor (ID);
ALTER TABLE OperacaoSetor ADD CONSTRAINT FKOperacaoSe246661 FOREIGN KEY (OperacaoID) REFERENCES Operacao (ID);
ALTER TABLE PlanoSetor ADD CONSTRAINT FKPlanoSetor502685 FOREIGN KEY (SistemaRegaQuintaID) REFERENCES SistemaRega (QuintaID);
ALTER TABLE Operacao ADD CONSTRAINT FKOperacao534717 FOREIGN KEY (EstadoID) REFERENCES Estado (ID);
ALTER TABLE ReceitaFator ADD CONSTRAINT FKReceitaFat104034 FOREIGN KEY (ReceitaID) REFERENCES Receita (ID);
ALTER TABLE ReceitaFator ADD CONSTRAINT FKReceitaFat725349 FOREIGN KEY (FatorProducaoID) REFERENCES FatorProducao (ID);
ALTER TABLE PlantacaoPermanente ADD CONSTRAINT FKPlantacaoP192597 FOREIGN KEY (PlantacaoID) REFERENCES Plantacao (ID);
ALTER TABLE OperacaoTipoOperacao ADD CONSTRAINT FKOperacaoTi419278 FOREIGN KEY (OperacaoID) REFERENCES Operacao (ID);
ALTER TABLE OperacaoTipoOperacao ADD CONSTRAINT FKOperacaoTi64864 FOREIGN KEY (TipoOperacaoID) REFERENCES TipoOperacao (ID);

CREATE OR REPLACE TRIGGER CapacidadeTrigger
BEFORE INSERT OR UPDATE ON ProdutoArmazem
FOR EACH ROW
DECLARE
    quantidadeTotal NUMBER;
    capacidade NUMBER;
BEGIN
    SELECT SUM(Quantidade) into quantidadeTotal  FROM ProdutoArmazem
    WHERE ArmazemEspacoID = :NEW.ArmazemEspacoID;
    
    quantidadeTotal := quantidadeTotal + :NEW.Quantidade;

    SELECT Capacidade into capacidade From Armazem
    WHERE EspacoID = :NEW.ArmazemEspacoID;

    IF quantidadeTotal > capacidade THEN
        RAISE_APPLICATION_ERROR(-20001, 'Esse Armazém encontra-se cheio.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER CheckUniqueEspacoIDRega
BEFORE INSERT OR UPDATE ON Rega
FOR EACH ROW
DECLARE
    existsEstabulo NUMBER;
    existsGaragem NUMBER;
    existsArmazem NUMBER;
    existsParcela NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO existsEstabulo
    FROM Estabulo E
    WHERE E.EspacoID = :NEW.EspacoID;

    SELECT COUNT(*)
    INTO existsGaragem
    FROM Garagem G
    WHERE G.EspacoID = :NEW.EspacoID;

    SELECT COUNT(*)
    INTO existsArmazem
    FROM Armazem A
    WHERE A.EspacoID = :NEW.EspacoID;
	
    SELECT COUNT(*)
    INTO existsParcela
    FROM Parcela P
    WHERE P.EspacoID = :NEW.EspacoID;

    IF existsEstabulo > 0 OR existsGaragem > 0 OR existsArmazem > 0 OR existsParcela > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Este espaco já está a ser usado.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER CheckUniqueEspacoIDEstabulo
BEFORE INSERT OR UPDATE ON Estabulo
FOR EACH ROW
DECLARE
    existsRega NUMBER;
    existsGaragem NUMBER;
    existsArmazem NUMBER;
    existsParcela NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO existsRega
    FROM Rega R
    WHERE R.EspacoID = :NEW.EspacoID;

    SELECT COUNT(*)
    INTO existsGaragem
    FROM Garagem G
    WHERE G.EspacoID = :NEW.EspacoID;

    SELECT COUNT(*)
    INTO existsArmazem
    FROM Armazem A
    WHERE A.EspacoID = :NEW.EspacoID;
	
    SELECT COUNT(*)
    INTO existsParcela
    FROM Parcela P
    WHERE P.EspacoID = :NEW.EspacoID;

    IF existsRega > 0 OR existsGaragem > 0 OR existsArmazem > 0 OR existsParcela > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Este espaco já está a ser usado.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER CheckUniqueEspacoIDGagarem
BEFORE INSERT OR UPDATE ON Garagem
FOR EACH ROW
DECLARE
    existsEstabulo NUMBER;
    existsRega NUMBER;
    existsArmazem NUMBER;
    existsParcela NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO existsEstabulo
    FROM Estabulo E
    WHERE E.EspacoID = :NEW.EspacoID;

    SELECT COUNT(*)
    INTO existsRega
    FROM Rega R
    WHERE R.EspacoID = :NEW.EspacoID;

    SELECT COUNT(*)
    INTO existsArmazem
    FROM Armazem A
    WHERE A.EspacoID = :NEW.EspacoID;
	
    SELECT COUNT(*)
    INTO existsParcela
    FROM Parcela P
    WHERE P.EspacoID = :NEW.EspacoID;

    IF existsEstabulo > 0 OR existsRega > 0 OR existsArmazem > 0 OR existsParcela > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Este espaco já está a ser usado.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER CheckUniqueEspacoIDArmazem
BEFORE INSERT OR UPDATE ON Armazem
FOR EACH ROW
DECLARE
    existsEstabulo NUMBER;
    existsGaragem NUMBER;
    existsRega NUMBER;
    existsParcela NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO existsEstabulo
    FROM Estabulo E
    WHERE E.EspacoID = :NEW.EspacoID;

    SELECT COUNT(*)
    INTO existsGaragem
    FROM Garagem G
    WHERE G.EspacoID = :NEW.EspacoID;

    SELECT COUNT(*)
    INTO existsRega
    FROM Rega R
    WHERE R.EspacoID = :NEW.EspacoID;
	
    SELECT COUNT(*)
    INTO existsParcela
    FROM Parcela P
    WHERE P.EspacoID = :NEW.EspacoID;

    IF existsEstabulo > 0 OR existsGaragem > 0 OR existsRega > 0 OR existsParcela > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Este espaco já está a ser usado.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER CheckUniqueEspacoIDParcela
BEFORE INSERT OR UPDATE ON Parcela
FOR EACH ROW
DECLARE
    existsEstabulo NUMBER;
    existsGaragem NUMBER;
    existsArmazem NUMBER;
    existsRega NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO existsEstabulo
    FROM Estabulo E
    WHERE E.EspacoID = :NEW.EspacoID;

    SELECT COUNT(*)
    INTO existsGaragem
    FROM Garagem G
    WHERE G.EspacoID = :NEW.EspacoID;

    SELECT COUNT(*)
    INTO existsArmazem
    FROM Armazem A
    WHERE A.EspacoID = :NEW.EspacoID;
	
    SELECT COUNT(*)
    INTO existsRega
    FROM Rega R
    WHERE R.EspacoID = :NEW.EspacoID;

    IF existsEstabulo > 0 OR existsGaragem > 0 OR existsArmazem > 0 OR existsRega > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Este espaco já está a ser usado.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER PreventLogChanges
BEFORE UPDATE OR DELETE ON LogOperacao
FOR EACH ROW
BEGIN
    RAISE_APPLICATION_ERROR(-20001, 'Não possivel alterar/apagar as informações do Log');
END;
/

CREATE OR REPLACE TRIGGER CheckUniqueOperacaoIDSetor
BEFORE INSERT OR UPDATE ON OperacaoSetor
FOR EACH ROW
DECLARE
    existsParcela NUMBER;
    existsPlantacao NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO existsParcela
    FROM OperacaoParcela OP
    WHERE OP.OperacaoID = :NEW.OperacaoID;

    SELECT COUNT(*)
    INTO existsPlantacao
    FROM OperacaoPlantacao OP
    WHERE OP.OperacaoID = :NEW.OperacaoID;

    IF existsParcela > 0 OR existsPlantacao > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Esta operação já está relacionada com um(a) parcela/plantação.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER CheckUniqueOperacaoIDParcela
BEFORE INSERT OR UPDATE ON OperacaoParcela
FOR EACH ROW
DECLARE
    existsSetor NUMBER;
    existsPlantacao NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO existsSetor
    FROM OperacaoSetor OS
    WHERE OS.OperacaoID = :NEW.OperacaoID;

    SELECT COUNT(*)
    INTO existsPlantacao
    FROM OperacaoPlantacao OP
    WHERE OP.OperacaoID = :NEW.OperacaoID;

    IF existsSetor > 0 OR existsPlantacao > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Esta operação já está relacionada com um(a) setor/plantação.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER ChechUniqueOperacaoIDPlantacao
BEFORE INSERT OR UPDATE ON OperacaoPlantacao
FOR EACH ROW
DECLARE
    existsSetor NUMBER;
    existsParcela NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO existsSetor
    FROM OperacaoSetor OS
    WHERE OS.OperacaoID = :NEW.OperacaoID;

    SELECT COUNT(*)
    INTO existsParcela
    FROM OperacaoParcela OP
    WHERE OP.OperacaoID = :NEW.OperacaoID;

    IF existsSetor > 0 OR existsParcela > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Esta operação já está relacionada com um(a) setor/parcela.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER OperacaoID
BEFORE INSERT ON Operacao
FOR EACH ROW
BEGIN
    SELECT OperacaoNextID.NEXTVAL
    INTO :NEW.id
    FROM dual;
END;
/

CREATE OR REPLACE TRIGGER InsertLogOperacao
AFTER INSERT OR UPDATE ON Operacao
FOR EACH ROW
DECLARE
     logOperacaoID NUMBER;
     estadoDes VARCHAR2(100);
     registoDes VARCHAR2(100);
BEGIN
    SELECT LogOperacaoNextID.NEXTVAL
    INTO logOperacaoID
    FROM dual;

    SELECT Designacao INTO estadoDes FROM Estado WHERE ID = :NEW.EstadoID;

    IF INSERTING THEN
        registoDes := 'Criação Da Operação';
    ELSE 
        registoDes := 'Alteração da Operação';
    END IF;

    INSERT INTO LogOperacao (ID, OperacaoID, DataOperacao, Quantidade, Unidade, Estado, Registo, TipoRegisto)
    VALUES (logOperacaoID, :NEW.id, :NEW.dataOperacao, :NEW.quantidade, :NEW.Unidade, estadoDes, SYSTIMESTAMP, registoDes);
END;
/

CREATE OR REPLACE TRIGGER PreventDeleteOperacao
BEFORE DELETE ON Operacao
FOR EACH ROW
BEGIN
    RAISE_APPLICATION_ERROR(-20001, 'Não é possivel apagar Operações.');
END;
/

CREATE OR REPLACE TRIGGER PreventUpdateAfterThreeDays
BEFORE UPDATE ON Operacao
FOR EACH ROW
DECLARE
    PRAGMA AUTONOMOUS_TRANSACTION;
    dataAtual DATE;
    plantId NUMBER;
    operationId NUMBER;
    operationCount NUMBER;
BEGIN
    SELECT COUNT(*) INTO operationCount FROM OperacaoPlantacao WHERE OperacaoID = :OLD.ID;
    COMMIT;
    IF :NEW.ESTADOID = 3 THEN
        SELECT CURRENT_DATE INTO dataAtual FROM DUAL;
        IF (dataAtual - :OLD.DataOperacao) > 3 THEN
            RAISE_APPLICATION_ERROR(-20001, 'Não é possível alterar uma operação com mais de 3 dias');
        END IF;
    END IF;
    IF operationCount > 0 THEN
        SELECT PlantacaoID INTO plantId FROM OperacaoPlantacao WHERE OperacaoID = :OLD.ID;
        COMMIT;
        SELECT COUNT(Op.OperacaoID) INTO operationId FROM OperacaoPlantacao Op, Operacao O WHERE Op.PlantacaoID = plantId AND Op.OperacaoID = O.ID AND O.DataOperacao > :OLD.DataOperacao;
        COMMIT;
        IF operationId IS NOT NULL THEN
            RAISE_APPLICATION_ERROR(-20001, 'Não é possível alterar uma operação que tenha operações posteriores');
        END IF;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER CheckPlantacaoUnidade
BEFORE INSERT OR UPDATE ON Plantacao
FOR EACH ROW
DECLARE 
  designacao VARCHAR2(255);
BEGIN
  SELECT Designacao INTO designacao 
  FROM TipoCultura
  WHERE ID = (SELECT TipoCulturaID FROM Cultura WHERE ID = :NEW.CulturaID);

  IF INSERTING THEN
    IF designacao = 'Temporária' THEN
      :NEW.Unidade := 'ha';
    ELSE
      :NEW.Unidade := 'un';
    END IF;
  ELSIF UPDATING THEN
    IF designacao = 'Temporária' AND :NEW.Unidade != 'ha' THEN
      RAISE_APPLICATION_ERROR(-20001, 'Se o TipoCultura é Temporária, então Unidade deve ser ha');
    ELSIF designacao != 'Temporária' AND :NEW.Unidade != 'un' THEN
      RAISE_APPLICATION_ERROR(-20001, 'Se o TipoCultura é Permanente, então Unidade deve ser un');
    END IF;
  END IF;
END;
/

CREATE OR REPLACE TRIGGER CheckCaudal
BEFORE INSERT OR UPDATE ON PlanoSetor
FOR EACH ROW
DECLARE
    debitoMaximo NUMBER;
BEGIN
    SELECT DebitoMaximo INTO debitoMaximo
    FROM SistemaRega
    WHERE QuintaID = :new.SistemaRegaQuintaID;
    IF :new.Caudal > debitoMaximo THEN
        RAISE_APPLICATION_ERROR(-20001, 'O valor de Caudal ultrapassa os limites do valor previsto para o Sistema de Rega');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER CheckUniqueTipoSensor
BEFORE INSERT OR UPDATE ON SensorEstacao
FOR EACH ROW
DECLARE
    existsNum NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO existsNum
    FROM SensorEstacao SE
    JOIN Sensor S ON SE.SensorID = S.ID
    WHERE S.TipoSensorID = (
        SELECT TipoSensorID FROM Sensor WHERE ID = :NEW.SensorID
    )
    AND SE.EstacaoMeteorologicaID = :NEW.EstacaoMeteorologicaID;

    IF existsNum > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Já existe um sensor desse tipo associado a esta estação meteorológica.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER CheckUniqueSensorIDEstacao
BEFORE INSERT OR UPDATE ON SensorEstacao
FOR EACH ROW
DECLARE
    existsCheck NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO existsCheck
    FROM SensorParcela SP
    WHERE SP.SensorID = :NEW.SensorID;

    IF existsCheck > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Este sensor já está a ser usado numa Parcela.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER CheckUniqueSensorIDParcela
BEFORE INSERT OR UPDATE ON SensorParcela
FOR EACH ROW
DECLARE
    existsCheck NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO existsCheck
    FROM SensorEstacao SE
    WHERE SE.SensorID = :NEW.SensorID;

    IF existsCheck > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Este sensor já está a ser usado numa Estação Meteorológica.');
    END IF;
END;
/

CREATE OR REPLACE PROCEDURE verifyDateInfo(dataOperacao DATE, estado NUMBER) IS
    dataAtual DATE;
    invalidFutureDate EXCEPTION;
    invalidPastDate EXCEPTION;
BEGIN

    SELECT CURRENT_DATE INTO dataAtual FROM DUAL;
    IF estado = 1 THEN
        IF dataOperacao < dataAtual THEN
            RAISE invalidPastDate;
        END IF;
    ELSE
        IF dataOperacao > dataAtual THEN
            RAISE invalidFutureDate;
        END IF;
    END IF;

EXCEPTION
    
    WHEN invalidFutureDate THEN
        RAISE_APPLICATION_ERROR(-20001,'Não é possível registar operações executadas no futuro!');
    WHEN invalidPastDate THEN
        RAISE_APPLICATION_ERROR(-20001,'Não é possível registar operações pendentes no passado!');
END verifyDateInfo;
/

CREATE OR REPLACE PROCEDURE verifyParcelaInfo(parcelaID NUMBER) IS
    parcelaExists NUMBER;

BEGIN

    SELECT 1 INTO parcelaExists FROM PARCELA WHERE EspacoID = parcelaID;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001,'Parcela não existe.');
END verifyParcelaInfo;
/

CREATE OR REPLACE PROCEDURE verifyPlantacaoInfo(plantacaoID NUMBER, parcelaID NUMBER, dataOperacao DATE) IS
    plantacaoExists NUMBER;

BEGIN
    
    SELECT 1 INTO plantacaoExists 
    FROM PLANTACAO 
    WHERE ID = plantacaoID 
    AND ParcelaEspacoID = parcelaID 
    AND dataOperacao BETWEEN DataIniciaL AND NVL(DataFinal, TO_DATE('9999-12-31','YYYY-MM-DD'));
       
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001,'Plantação não existe.');
END verifyPlantacaoInfo;
/

CREATE OR REPLACE PROCEDURE verifySetorInfo(setorID NUMBER) IS
    setorExists NUMBER;

BEGIN

    SELECT 1 INTO setorExists FROM Setor WHERE ID = setorID;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001,'Setor não existe.');
END verifySetorInfo;
/

CREATE OR REPLACE PROCEDURE verifyQuantityInfo(plantacaoID NUMBER, parcelaID NUMBER, quantidade NUMBER, unidade VARCHAR2) IS
    areaParcela NUMBER;
    quantidadePlantacao NUMBER;
    quantidadeParcela NUMBER;
    invalidQuantidade EXCEPTION;
    culturaID NUMBER;

BEGIN
    IF unidade = 'ha' THEN
        
        SELECT Area INTO areaParcela FROM Espaco WHERE ID = parcelaID;
        IF quantidade > areaParcela THEN 
            RAISE invalidQuantidade;
        END IF;

    ELSE

        SELECT QUANTIDADE INTO quantidadePlantacao FROM PLANTACAO WHERE ID = plantacaoID;
        IF quantidade > quantidadePlantacao THEN 
            RAISE invalidQuantidade;
        END IF;
        
    END IF;

EXCEPTION
    WHEN invalidQuantidade THEN 
        RAISE_APPLICATION_ERROR(-20001,'Quantidade fornecida superior à disponivel.');
END verifyQuantityInfo;
/

CREATE OR REPLACE PROCEDURE verifyAvailableAreaInfo(parcelaID NUMBER, quantidade NUMBER) IS
    areaParcela NUMBER;
    quantidadeParcela NUMBER;
    invalidQuantidade EXCEPTION;

BEGIN
        
    SELECT Area INTO areaParcela FROM Espaco WHERE ID = parcelaID;
    SELECT SUM(Quantidade) INTO quantidadeParcela FROM Plantacao WHERE ParcelaEspacoID = parcelaID AND DataFinal IS NULL;
        IF quantidade > (areaParcela - quantidadeParcela) THEN 
            RAISE invalidQuantidade;
        END IF;

EXCEPTION
    WHEN invalidQuantidade THEN 
        RAISE_APPLICATION_ERROR(-20001,'Area fornecida superior à disponivel.');
END verifyAvailableAreaInfo;
/

CREATE OR REPLACE PROCEDURE verifyFatorDeProducaoInfo(fatorProducaoID NUMBER) IS
    fatorProducaoExists NUMBER;

BEGIN
        
    SELECT 1 INTO fatorProducaoExists FROM FatorProducao WHERE ID = fatorProducaoID;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001,'Fator de Produção não existe.');
END verifyFatorDeProducaoInfo;
/

CREATE OR REPLACE PROCEDURE verifyModoFertilizacaoInfo(modoAplicacaoID NUMBER) IS
    modoAplicacaoExists NUMBER;

BEGIN
        
    SELECT 1 INTO modoAplicacaoExists FROM ModoFertilizacao WHERE ID = modoAplicacaoID;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20001,'Modo de Aplicação não existe.');
END verifyModoFertilizacaoInfo;
/

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

CREATE OR REPLACE FUNCTION getFatorReceita(Receita NUMBER) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
    row_count NUMBER;
    no_data_found EXCEPTION;
BEGIN
    SELECT COUNT(*)
    INTO row_count
    FROM RECEITAFATOR
    WHERE RECEITAID = Receita;

    IF row_count = 0 THEN
        RAISE no_data_found;
    END IF;

    OPEN result_cursor FOR
        SELECT FATORPRODUCAOID as fatorproducaoID, QUANTIDADE as quantidade, UNIDADE as unidade
        FROM RECEITAFATOR
        WHERE RECEITAID = Receita;
    RETURN result_cursor;
EXCEPTION
    WHEN no_data_found THEN
        RAISE_APPLICATION_ERROR(-20001, 'Essa receita não existe ou não tem fatores de produção associados.');
END;
/

CREATE OR REPLACE PROCEDURE registerPoda(quantidade NUMBER,parcelaID NUMBER,plantacaoID NUMBER,dataOperacao DATE) IS

    TIPO_OPERACAO CONSTANT NUMBER := 3;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE CONSTANT VARCHAR2(10) := 'un';
    ESTADO CONSTANT NUMBER := 2;
    idOperacao NUMBER;

BEGIN
    verifyParcelaInfo(parcelaID);
    verifyPlantacaoInfo(plantacaoID, parcelaID,dataOperacao);
    verifyQuantityInfo(plantacaoID, parcelaID, quantidade, UNIDADE);
    verifyDateInfo(dataOperacao, ESTADO);

    insertOperacao(dataOperacao, quantidade, UNIDADE, CADERNO_DE_CAMPO, estado, idOperacao);
    INSERT INTO OperacaoPlantacao(OperacaoID, PlantacaoID) VALUES (idOperacao, plantacaoID);
    INSERT INTO OperacaoTipoOperacao(OperacaoID, TipoOperacaoID) VALUES (idOperacao, TIPO_OPERACAO);
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Operação registada com sucesso!');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE registerSemeadura(culturaID NUMBER,parcelaID NUMBER,dataOperacao DATE,quantidade NUMBER, area NUMBER) IS

    TIPO_OPERACAO CONSTANT NUMBER := 6;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE CONSTANT VARCHAR2(10) := 'kg';
    UNIDADE2 CONSTANT VARCHAR2(10) := 'ha';
    ESTADO CONSTANT NUMBER := 2;
    idOperacao NUMBER;
    idPlantacao NUMBER;

BEGIN
    verifyDateInfo(dataOperacao, ESTADO);
    verifyParcelaInfo(parcelaID);
    verifyQuantityInfo(idPlantacao, parcelaID, area, UNIDADE2);
    verifyAvailableAreaInfo(parcelaID, quantidade);
    
    --Obter o ID da operação
    SELECT NVL(MAX(ID),0) + 1 INTO idPlantacao FROM Plantacao;

    insertOperacao(dataOperacao, quantidade, UNIDADE, CADERNO_DE_CAMPO, estado, idOperacao);
    INSERT INTO OperacaoParcela(OperacaoID, ParcelaEspacoID) 
    VALUES (idOperacao, parcelaID);
    INSERT INTO Plantacao(ID, DataInicial, DataFinal, Quantidade, Unidade, EstadoFenologico, CulturaID, ParcelaEspacoID) 
    VALUES (idPlantacao, dataOperacao, NULL, area, UNIDADE2, NULL, culturaID, parcelaID);
    INSERT INTO OperacaoTipoOperacao(OperacaoID, TipoOperacaoID) 
    VALUES (idOperacao, TIPO_OPERACAO);

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Operação registada com sucesso!');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE registerMonda(plantacaoID NUMBER,parcelaID NUMBER,dataOperacao DATE,quantidade NUMBER) IS

    TIPO_OPERACAO CONSTANT NUMBER := 9;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE CONSTANT VARCHAR2(10) := 'ha';
    idOperacao NUMBER;
    ESTADO CONSTANT NUMBER := 2;

BEGIN
    verifyDateInfo(dataOperacao, ESTADO);
    verifyParcelaInfo(parcelaID);
    verifyPlantacaoInfo(plantacaoID,parcelaID,dataOperacao);
    verifyQuantityInfo(plantacaoID,parcelaID,quantidade, UNIDADE);
    --Obter o ID da operação

    insertOperacao(dataOperacao, quantidade, UNIDADE, CADERNO_DE_CAMPO, estado, idOperacao);
    INSERT INTO OperacaoTipoOperacao(OperacaoID, TipoOperacaoID) 
    VALUES (idOperacao, TIPO_OPERACAO);
    IF plantacaoID IS NULL THEN 
        INSERT INTO OperacaoParcela(OperacaoID, ParcelaEspacoID) VALUES (idOperacao, parcelaID);
    ELSE
        INSERT INTO OperacaoPlantacao(OperacaoID, PlantacaoID) VALUES (idOperacao, plantacaoID);
    END IF;
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Operação registada com sucesso!');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE registerColheita(plantacaoID NUMBER,parcelaID NUMBER,dataOperacao DATE,quantidade NUMBER) IS

    TIPO_OPERACAO CONSTANT NUMBER := 7;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE CONSTANT VARCHAR2(10) := 'kg';
    idOperacao NUMBER;
    ESTADO CONSTANT NUMBER := 2;

BEGIN
    verifyParcelaInfo(parcelaID);
    verifyPlantacaoInfo(plantacaoID, parcelaID, dataOperacao);
    verifyDateInfo(dataOperacao, ESTADO);
    --Obter o ID da operação
    SELECT NVL(MAX(ID),0) + 1 INTO idOperacao FROM Operacao;

    insertOperacao(dataOperacao, quantidade, UNIDADE, CADERNO_DE_CAMPO, estado, idOperacao);
    INSERT INTO OperacaoTipoOperacao(OperacaoID, TipoOperacaoID) VALUES (idOperacao, TIPO_OPERACAO);
    INSERT INTO OperacaoPlantacao(OperacaoID, PlantacaoID) VALUES (idOperacao, plantacaoID);
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Operação registada com sucesso!');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE registerFatorDeProducao(quantidade NUMBER,parcelaID NUMBER,dataOperacao DATE, area NUMBER, fatorProducaoID NUMBER, modoFertilizacaoID NUMBER) IS

    TIPO_OPERACAO CONSTANT NUMBER := 11;
    CADERNO_DE_CAMPO CONSTANT NUMBER := 1;
    UNIDADE1 CONSTANT VARCHAR2(10) := 'ha';
    UNIDADE2 CONSTANT VARCHAR2(10) := 'kg';
    idOperacao NUMBER;
    ESTADO CONSTANT NUMBER := 2;

BEGIN
    verifyParcelaInfo(parcelaID);
    verifyDateInfo(dataOperacao, ESTADO);
    verifyFatorDeProducaoInfo(fatorProducaoID);
    verifyModoFertilizacaoInfo(modoFertilizacaoID);
    verifyQuantityInfo(NULL,parcelaID,area,UNIDADE1);

    insertOperacao(dataOperacao, quantidade, UNIDADE2, CADERNO_DE_CAMPO, estado, idOperacao);
    INSERT INTO OperacaoTipoOperacao(OperacaoID, TipoOperacaoID) VALUES (idOperacao, TIPO_OPERACAO);
    INSERT INTO OperacaoFator(OperacaoID, FatorProducaoID, Quantidade, Unidade) VALUES(idOperacao, fatorProducaoID, quantidade, Unidade2);
    INSERT INTO Fertilizacao(OperacaoID, ModoFertilizacaoID) VALUES(idOperacao, modoFertilizacaoID);
    INSERT INTO OperacaoParcela(OperacaoID, ParcelaEspacoID) VALUES (idOperacao, parcelaID);

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Operação registada com sucesso!');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

CREATE OR REPLACE FUNCTION getProdutosColhidosList(parcelaID NUMBER,dataInicial DATE, dataFinal DATE) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT PARCELA, ESPECIE, PRODUTO, DATA
        FROM(
        SELECT ES.DESIGNACAO AS PARCELA, NE.ESPECIE AS ESPECIE, NP.DESIGNACAO || ' ' || C.VARIEDADE AS PRODUTO, O.DATAOPERACAO as data
            FROM ESPACO ES, NOMEESPECIE NE, NOMEPRODUTO NP, CULTURA C, TIPOOPERACAO TP, OPERACAO O, PLANTACAO P, OPERACAOPLANTACAO OP, PRODUTO PR, OPERACAOTIPOOPERACAO OT
            WHERE ES.ID = parcelaID
            AND P.PARCELAESPACOID = ES.ID
            AND OP.PLANTACAOID = P.ID
            AND O.ID = OP.OPERACAOID
            AND TP.ID = 7
            AND TP.ID = OT.TIPOOPERACAOID
            AND OT.OPERACAOID = O.ID
            AND P.CULTURAID = PR.CULTURAID
            AND C.ID = PR.CULTURAID
            AND C.NOMEESPECIEID = NE.ID
            AND PR.NOMEPRODUTOID = NP.ID
        )
        WHERE data BETWEEN DATAINICIAL AND DATAFINAL
        ORDER BY ESPECIE DESC, PRODUTO;
    RETURN result_cursor;
END;
/

CREATE OR REPLACE FUNCTION getFatorProducaoList(dataInicial DATE, dataFinal DATE) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT DATA, FATORPRODUCAO, CULTURA, APLICACAO, PARCELA
        FROM(
            SELECT O.DATAOPERACAO AS data, FP.Designacao AS FATORPRODUCAO, NE.NOMECOMUM ||' ' || CU.VARIEDADE AS Cultura,A.DESIGNACAO AS Aplicacao, E.Designacao as parcela 
            FROM OPERACAOFATOR OFA, Operacao O, FatorProducao FP, OperacaoPlantacao OP, Plantacao P, Espaco E, Cultura CU, NOMEESPECIE NE, AplicacaoProduto AP, Aplicacao A
            WHERE OFA.OPERACAOID = O.ID
            AND OFA.FATORPRODUCAOID = FP.ID
            AND AP.FATORPRODUCAOID = FP.ID
            AND AP.AplicacaoID = A.ID
            AND O.ID = OP.OPERACAOID
            AND OP.PLANTACAOID = P.ID
            AND P.PARCELAESPACOID = E.ID
            AND P.CULTURAID = CU.ID
            AND CU.NOMEESPECIEID = NE.ID
            UNION
            SELECT O.DATAOPERACAO AS data, FP.Designacao AS FATORPRODUCAO,'Sem cultura'AS Cultura, A.DESIGNACAO AS APLICACAO, E.DESIGNACAO AS PARCELA
            FROM OPERACAOFATOR OFA, Operacao O, FatorProducao FP, OperacaoParcela OP, Plantacao P, Espaco E, Aplicacao A, AplicacaoProduto AP
            WHERE OFA.OPERACAOID = O.ID
            AND OFA.FATORPRODUCAOID = FP.ID
            AND AP.FATORPRODUCAOID = FP.ID
            AND AP.AplicacaoID = A.ID
            AND O.ID = OP.OPERACAOID
            AND OP.PARCELAESPACOID = E.ID
        )
        WHERE data BETWEEN DATAINICIAL AND DATAFINAL
        ORDER BY PARCELA,APLICACAO,FATORPRODUCAO;
        
    RETURN result_cursor;
END;
/

CREATE OR REPLACE FUNCTION getFatorProducaoElementosList(dataInicial DATE, dataFinal DATE, IDparcela NUMBER) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT fatorProducao, Quantidade, Elemento, data
        FROM(
            SELECT FP.DESIGNACAO as FatorProducao, EF.Quantidade as Quantidade, E.Designacao as Elemento, P.PARCELAESPACOID as parcelaID, O.DATAOPERACAO as data  
            FROM OperacaoFator OFA, fatorproducao FP, ElementoFicha EF, Elemento E, OPERACAOPLANTACAO OP, Plantacao P, Operacao O
            WHERE OFA.FATORPRODUCAOID = FP.ID
            AND OFA.OPERACAOID = O.ID
            AND OFA.OPERACAOID = OP.OPERACAOID
            AND OP.PlantacaoID = P.ID
            AND FP.ID = EF.FATORPRODUCAOID
            AND EF.ELEMENTOID = E.ID
            UNION 
            SELECT FP.DESIGNACAO as Designacao, EF.Quantidade as Quantidade, E.Designacao as Elemento, OP.PARCELAESPACOID as parcelaID, O.DATAOPERACAO as data 
            FROM OperacaoFator OFA, fatorproducao FP, ElementoFicha EF, Elemento E, OPERACAOPARCELA OP, Operacao O
            WHERE OFA.FATORPRODUCAOID = FP.ID
            AND OFA.OPERACAOID = O.ID
            AND OFA.OPERACAOID = OP.OPERACAOID
            AND FP.ID = EF.FATORPRODUCAOID
            AND EF.ELEMENTOID = E.ID
        )
        WHERE data BETWEEN DATAINICIAL AND DATAFINAL
        AND parcelaID = IDparcela
        ORDER BY fatorProducao, data, elemento;

    RETURN result_cursor;
END;
/

CREATE OR REPLACE FUNCTION getRegaMensal(dataInicial DATE, dataFinal DATE) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT PARCELA, EXTRACT(YEAR FROM DATA) AS YEAR, EXTRACT(MONTH FROM DATA) AS MONTH, SUM(DURACAO) AS DURACAO
        FROM(
            SELECT ES.DESIGNACAO AS PARCELA, OP.DataOperacao AS DATA, MAX(OP.QUANTIDADE) AS DURACAO
            FROM TIPOOPERACAO TP, OPERACAO OP, PARCELA PAR, OPERACAOSETOR OS, SETOR S, PLANTACAOSETOR PS, PLANTACAO P, ESPACO ES, OPERACAOTIPOOPERACAO OT
            WHERE TP.ID = 2
            AND OT.TIPOOPERACAOID = TP.ID
            AND OT.OPERACAOID = OP.ID
            AND OS.OPERACAOID = OP.ID
            AND S.ID = OS.SETORID
            AND PS.SETORID = S.ID
            AND P.ID = PS.PLANTACAOID
            AND PAR.ESPACOID = P.PARCELAESPACOID
            AND ES.ID = PAR.ESPACOID
            GROUP BY ES.DESIGNACAO, OP.DataOperacao, OP.ID
        )
        WHERE DATA BETWEEN dataInicial AND dataFinal
        GROUP BY PARCELA, EXTRACT(YEAR FROM DATA), EXTRACT(MONTH FROM DATA)
        ORDER BY PARCELA, YEAR, MONTH;
    RETURN result_cursor;
END;
/

CREATE OR REPLACE FUNCTION GetOperacaoList(parcelaID NUMBER,dataInicial DATE, dataFinal DATE) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
OPEN result_cursor FOR
SELECT OperacaoID, TipoDeOperacao, data, Cultura
FROM(
    SELECT OP.OperacaoID AS OperacaoID, TOP.Designacao AS TipoDeOperacao, O.DataOperacao as data, 'Sem cultura' as Cultura
    FROM OperacaoParcela OP, TipoOperacao TOP, Operacao O, OPERACAOTIPOOPERACAO OT
    WHERE OP.OperacaoID = O.ID
        AND OT.TIPOOPERACAOID = TOP.ID
        AND OT.OPERACAOID = O.ID
        AND OP.ParcelaEspacoID = parcelaID
    UNION
    SELECT OP.OperacaoID AS OperacaoID, TOP.Designacao AS TipoDeOperacao, O.DataOperacao as data, NP.Designacao  ||' '|| CU.Variedade AS Cultura
    FROM OperacaoPlantacao OP, TipoOperacao TOP, Operacao O, Plantacao P, Cultura CU, Produto Po, NomeProduto NP, OPERACAOTIPOOPERACAO OT
    WHERE OP.OperacaoID = O.ID
        AND OT.TIPOOPERACAOID = TOP.ID
        AND OT.OPERACAOID = O.ID
        AND OP.PlantacaoID = P.ID
        AND P.ParcelaEspacoID = parcelaID
        AND P.CulturaID = CU.ID
        AND P.CulturaID = PO.CulturaID
        AND PO.NomeProdutoID = NP.ID
    UNION
    SELECT OS.OperacaoID AS OperacaoID, TOP.Designacao AS TipoDeOperacao, O.DataOperacao as data, NE.NomeComum  ||' '|| CU.Variedade AS Cultura
    FROM OperacaoSetor OS, TipoOperacao TOP, Operacao O, Plantacao P, PlantacaoSetor PS, Setor S, Cultura CU, NomeEspecie NE, OPERACAOTIPOOPERACAO OT
    WHERE OS.OperacaoID = O.ID
        AND OT.TIPOOPERACAOID = TOP.ID
        AND OT.OPERACAOID = O.ID
        AND OS.SetorID = S.ID
        AND P.PARCELAESPACOID = parcelaID
        AND P.ID = PS.PlantacaoID
        AND PS.SetorID = S.ID
        AND P.CulturaID = Cu.ID
        AND CU.NomeEspecieID = NE.ID
    )
WHERE data BETWEEN DATAINICIAL AND DATAFINAL
ORDER BY TipoDeOperacao, data;
RETURN result_cursor;
END;
/

CREATE OR REPLACE PROCEDURE insertOperacao(dataoperacao DATE, quantidade NUMBER, unidade VARCHAR2, cadernocampoid NUMBER, estadoid NUMBER, id OUT NUMBER) AS
BEGIN
    verifyDateInfo(dataOperacao, estadoid);

    INSERT INTO Operacao(DATAOPERACAO, QUANTIDADE, UNIDADE, CADERNOCAMPOID, ESTADOID)
    VALUES (dataoperacao, quantidade, unidade, cadernocampoid, estadoid)
    RETURNING id INTO id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20001, 'Ocorreu um erro' || SQLCODE || ' - ' || SQLERRM);
END;
/

CREATE OR REPLACE FUNCTION getConsumoRega(year Number) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT CULTURA, SUM(Minutos) AS CONSUMO_MINUTOS
        FROM(
            SELECT NE.NOMECOMUM || ' ' || CU.VARIEDADE AS CULTURA, O.QUANTIDADE AS Minutos, O.UNIDADE AS UNIDADE
            FROM OPERACAOSETOR OS, PLANTACAOSETOR PS, PLANTACAO P, CULTURA CU, NOMEESPECIE NE, OPERACAO O, OPERACAOTIPOOPERACAO OT, TIPOOPERACAO TOO
            WHERE OS.SETORID = PS.SETORID 
            AND PS.PLANTACAOID = P.ID
            AND P.CULTURAID = CU.ID
            AND CU.NOMEESPECIEID = NE.ID
            AND OS.OPERACAOID = O.ID
            AND O.ID = OT.OPERACAOID
            AND OT.TIPOOPERACAOID = TOO.ID
            AND TOO.ID = 2
            AND EXTRACT(YEAR FROM O.DATAOPERACAO) = year
        )
        GROUP BY CULTURA, UNIDADE
        ORDER BY CONSUMO_MINUTOS DESC;
    RETURN result_cursor;
END;
/

CREATE OR REPLACE FUNCTION getFatorProducaoYear(year Number) RETURN SYS_REFCURSOR AS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT fatorProducao, EXTRACT(YEAR FROM data) as ano
        FROM(
            SELECT FP.DESIGNACAO as FatorProducao, O.DATAOPERACAO as data  
            FROM OperacaoFator OFA, fatorproducao FP, Operacao O
            WHERE OFA.FATORPRODUCAOID = FP.ID
            AND OFA.OPERACAOID = O.ID
            AND FP.ID NOT IN (
                SELECT FP2.ID
                FROM OperacaoFator OFA2, fatorproducao FP2, Operacao O2
                WHERE OFA2.FATORPRODUCAOID = FP2.ID
                AND OFA2.OPERACAOID = O2.ID
                AND EXTRACT(YEAR FROM O2.DATAOPERACAO) = year
            )
        )
        WHERE year != EXTRACT(YEAR FROM data)
        GROUP BY fatorProducao, EXTRACT(YEAR FROM data)
        ORDER BY fatorProducao, EXTRACT(YEAR FROM data);

    RETURN result_cursor;
END;
/

CREATE OR REPLACE PROCEDURE registerReceita(designacaoReceita IN VARCHAR2, idReceita OUT NUMBER) IS
BEGIN
    --Obter o ID da receita
    SELECT NVL(MAX(ID),0) + 1 INTO idReceita FROM Receita;

    INSERT INTO Receita(ID, Designacao)
    VALUES (idReceita, designacaoReceita);
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Receita criada com sucesso!');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

CREATE OR REPLACE PROCEDURE addFatorToReceita(receita NUMBER, fator NUMBER, quantidade NUMBER, unidade VARCHAR2) IS

BEGIN
    verifyFatorDeProducaoInfo(fator);
    INSERT INTO ReceitaFator(ReceitaID, FatorProducaoID, Quantidade, Unidade) VALUES (receita, fator, quantidade, unidade);
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Fator adicionado com sucesso!');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/