DROP TRIGGER CheckUniqueOperacaoIDSetor;
DROP TRIGGER CheckUniqueOperacaoIDParcela;
DROP TRIGGER ChechUniqueOperacaoIDPlantacao;
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
DROP TABLE FatorProducao CASCADE CONSTRAINTS;
DROP TABLE Fertilizacao CASCADE CONSTRAINTS;
DROP TABLE Garagem CASCADE CONSTRAINTS;
DROP TABLE Hora CASCADE CONSTRAINTS;
DROP TABLE ModoFertilizacao CASCADE CONSTRAINTS;
DROP TABLE NomeEspecie CASCADE CONSTRAINTS;
DROP TABLE Operacao CASCADE CONSTRAINTS;
DROP TABLE OperacaoFator CASCADE CONSTRAINTS;
DROP TABLE OperacaoParcela CASCADE CONSTRAINTS;
DROP TABLE OperacaoPlantacao CASCADE CONSTRAINTS;
DROP TABLE OperacaoSetor CASCADE CONSTRAINTS;
DROP TABLE Parcela CASCADE CONSTRAINTS;
DROP TABLE PlanoHora CASCADE CONSTRAINTS;
DROP TABLE PlanoPlantacao CASCADE CONSTRAINTS;
DROP TABLE PlanoRega CASCADE CONSTRAINTS;
DROP TABLE PlanoSetor CASCADE CONSTRAINTS;
DROP TABLE Plantacao CASCADE CONSTRAINTS;
DROP TABLE PlantacaoSetor CASCADE CONSTRAINTS;
DROP TABLE Produto CASCADE CONSTRAINTS;
DROP TABLE ProdutoArmazem CASCADE CONSTRAINTS;
DROP TABLE Quinta CASCADE CONSTRAINTS;
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
  CAPACIDADE NUMBER(10) DEFAULT 300 NOT NULL CHECK(CAPACIDADE > 0),
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
  QUANTIDADE DOUBLE PRECISION NOT NULL CHECK(QUANTIDADE > 0),
  PRIMARY KEY (FATORPRODUCAOID, ELEMENTOID)
);

CREATE TABLE ESPACO (
  ID NUMBER(10) NOT NULL,
  DESIGNACAO VARCHAR2(255) NOT NULL,
  AREA DOUBLE PRECISION NOT NULL CHECK(AREA > 0),
  UNIDADE VARCHAR2(255) NOT NULL,
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

CREATE TABLE FATORPRODUCAO (
  ID NUMBER(10) NOT NULL,
  DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE,
  FABRICANTE VARCHAR2(255) NOT NULL,
  FORMATO NUMBER(10) NOT NULL,
  PH DOUBLE PRECISION CHECK(PH >= 0 AND PH <= 14),
  TIPOPRODUTOID NUMBER(10) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE FERTILIZACAO (
  OPERACAOID NUMBER(10) NOT NULL,
  MODOFERTILIZACAOID NUMBER(10) NOT NULL,
  PRIMARY KEY (OPERACAOID)
);

CREATE TABLE GARAGEM (
  ESPACOID NUMBER(10) NOT NULL,
  PRIMARY KEY (ESPACOID)
);

CREATE TABLE HORA (
  ID NUMBER(10) NOT NULL,
  HORAINICIAL TIMESTAMP(7) NOT NULL UNIQUE,
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

CREATE TABLE OPERACAO (
  ID NUMBER(10) NOT NULL,
  DATAOPERACAO DATE NOT NULL,
  QUANTIDADE NUMBER(10) NOT NULL,
  UNIDADE VARCHAR2(255) NOT NULL,
  TIPOOPERACAOID NUMBER(10) NOT NULL,
  CADERNOCAMPOID NUMBER(10) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE OPERACAOFATOR (
  OPERACAOID NUMBER(10) NOT NULL,
  FATORPRODUCAOID NUMBER(10) NOT NULL,
  PRIMARY KEY (OPERACAOID)
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
  HORA TIMESTAMP(7) NOT NULL,
  SETORID NUMBER(10) NOT NULL,
  PRIMARY KEY (OPERACAOID)
);

CREATE TABLE PARCELA (
  ESPACOID NUMBER(10) NOT NULL,
  PRIMARY KEY (ESPACOID)
);

CREATE TABLE PLANOHORA (
  PLANOREGAANOINSERCAO NUMBER(10) NOT NULL,
  HORAID NUMBER(10) NOT NULL,
  PRIMARY KEY (PLANOREGAANOINSERCAO, HORAID)
);

CREATE TABLE PLANOPLANTACAO (
  PLANTACAOSETORPLANTACAOID NUMBER(10) NOT NULL,
  QUANTIDADE NUMBER(10),
  DATAINICIAL DATE NOT NULL,
  DATAFINAL DATE,
  PRIMARY KEY (PLANTACAOSETORPLANTACAOID),
  CONSTRAINT CHECKDATES CHECK (DATAINICIAL <= DATAFINAL)
);

CREATE TABLE PLANOREGA (
  ANOINSERCAO NUMBER(10) NOT NULL,
  SISTEMAREGAESPACOID NUMBER(10) NOT NULL,
  PRIMARY KEY (ANOINSERCAO)
);

CREATE TABLE PLANOSETOR (
  PLANOREGAANOINSERCAO NUMBER(10) NOT NULL,
  SETORID NUMBER(10) NOT NULL,
  DATAINICIAL DATE NOT NULL,
  DATAFINAL DATE,
  CAUDAL NUMBER(10) DEFAULT CAUDAL > 0 NOT NULL,
  DURACAO NUMBER(10) NOT NULL CHECK(DURACAO > 0),
  DISPERCAO VARCHAR2(255) NOT NULL,
  PERIODICIDADE NUMBER(10) NOT NULL CHECK(PERIODICIDADE IN ('P', 'I', 'T')),
  PRIMARY KEY (PLANOREGAANOINSERCAO, SETORID),
  CONSTRAINT CHECKDATES CHECK (DATAINICIAL<= DATAFINAL)
);

CREATE TABLE PLANTACAO (
  ID NUMBER(10) NOT NULL,
  DATAINICIAL DATE NOT NULL,
  DATAFINAL DATE,
  QUANTIDADE DOUBLE PRECISION NOT NULL CHECK(QUANTIDADE > 0),
  UNIDADE VARCHAR2(255) NOT NULL,
  ESTADOFENOLOGICO VARCHAR2(255) NOT NULL,
  CULTURAID NUMBER(10) NOT NULL,
  PARCELAESPACOID NUMBER(10) NOT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT CHECKDATES CHECK (DATAINICIAL <= DATAFINAL)
);

CREATE TABLE PLANTACAOSETOR (
  PLANTACAOID NUMBER(10) NOT NULL,
  SETORID NUMBER(10) NOT NULL,
  PRIMARY KEY (PLANTACAOID)
);

CREATE TABLE PRODUTO (
  CULTURAID NUMBER(10) NOT NULL,
  DESIGNACAO VARCHAR2(255) NOT NULL,
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
  ESPACOID NUMBER(10) NOT NULL,
  DEBITOMAXIMO NUMBER(10) NOT NULL CHECK(DEBITOMAXIMO > 0),
  PRIMARY KEY (ESPACOID)
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
ALTER TABLE SistemaRega ADD CONSTRAINT FKSistemaReg467386 FOREIGN KEY (EspacoID) REFERENCES Espaco (ID);
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
ALTER TABLE Operacao ADD CONSTRAINT FKOperacao623592 FOREIGN KEY (TipoOperacaoID) REFERENCES TipoOperacao (ID);
ALTER TABLE OperacaoFator ADD CONSTRAINT FKOperacaoFa121712 FOREIGN KEY (OperacaoID) REFERENCES Operacao (ID);
ALTER TABLE OperacaoFator ADD CONSTRAINT FKOperacaoFa436815 FOREIGN KEY (FatorProducaoID) REFERENCES FatorProducao (ID);
ALTER TABLE ProdutoArmazem ADD CONSTRAINT FKProdutoArm466455 FOREIGN KEY (ProdutoCulturaID) REFERENCES Produto (CulturaID);
ALTER TABLE PlanoSetor ADD CONSTRAINT FKPlanoSetor822837 FOREIGN KEY (SetorID) REFERENCES Setor (ID);
ALTER TABLE PlanoSetor ADD CONSTRAINT FKPlanoSetor30287 FOREIGN KEY (PlanoRegaAnoInsercao) REFERENCES PlanoRega (AnoInsercao);
ALTER TABLE PlanoRega ADD CONSTRAINT FKPlanoRega898248 FOREIGN KEY (SistemaRegaEspacoID) REFERENCES SistemaRega (EspacoID);
ALTER TABLE PlanoHora ADD CONSTRAINT FKPlanoHora58591 FOREIGN KEY (PlanoRegaAnoInsercao) REFERENCES PlanoRega (AnoInsercao);
ALTER TABLE OperacaoSetor ADD CONSTRAINT FKOperacaoSe277035 FOREIGN KEY (SetorID) REFERENCES Setor (ID);
ALTER TABLE OperacaoSetor ADD CONSTRAINT FKOperacaoSe246661 FOREIGN KEY (OperacaoID) REFERENCES Operacao (ID);
ALTER TABLE OperacaoPlantacao ADD CONSTRAINT FKOperacaoPl849173 FOREIGN KEY (OperacaoID) REFERENCES Operacao (ID);
ALTER TABLE OperacaoPlantacao ADD CONSTRAINT FKOperacaoPl732201 FOREIGN KEY (PlantacaoID) REFERENCES Plantacao (ID);
ALTER TABLE PlanoHora ADD CONSTRAINT FKPlanoHora563720 FOREIGN KEY (HoraID) REFERENCES Hora (ID);
ALTER TABLE OperacaoParcela ADD CONSTRAINT FKOperacaoPa455939 FOREIGN KEY (OperacaoID) REFERENCES Operacao (ID);
ALTER TABLE OperacaoParcela ADD CONSTRAINT FKOperacaoPa344730 FOREIGN KEY (ParcelaEspacoID) REFERENCES Parcela (EspacoID);
ALTER TABLE Produto ADD CONSTRAINT FKProduto49 FOREIGN KEY (CulturaID) REFERENCES Cultura (ID);
ALTER TABLE PlanoPlantacao ADD CONSTRAINT FKPlanoPlant813901 FOREIGN KEY (PlantacaoSetorPlantacaoID) REFERENCES PlantacaoSetor (PlantacaoID);

CREATE OR REPLACE TRIGGER CheckUniqueOperacaoIDSetor
BEFORE INSERT OR UPDATE ON OperacaoSetor
FOR EACH ROW
DECLARE
    existsParcela NUMBER;
    existsPlantacao NUMBER;
BEGIN
    SELECT 1
    INTO existsParcela
    FROM dual
    WHERE EXISTS (
        SELECT 1
        FROM OperacaoParcela OP
        WHERE OP.OperacaoID = :NEW.OperacaoID
    );

    SELECT 1
    INTO existsPlantacao
    FROM dual
    WHERE EXISTS (
        SELECT 1
        FROM OperacaoPlantacao OP
        WHERE OP.OperacaoID = :NEW.OperacaoID
    );

    IF existsParcela = 1 OR existsPlantacao = 1 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Esta operação já está relacionada com um(a) parcela/plantação.');
    END IF;
END;
/
;
CREATE OR REPLACE TRIGGER CheckUniqueOperacaoIDParcela
BEFORE INSERT OR UPDATE ON OperacaoParcela
FOR EACH ROW
DECLARE
    existsSetor NUMBER;
    existsPlantacao NUMBER;
BEGIN
    SELECT 1
    INTO existsSetor
    FROM dual
    WHERE EXISTS (
        SELECT 1
        FROM OperacaoSetor OS
        WHERE OS.OperacaoID = :NEW.OperacaoID
    );

    SELECT 1
    INTO existsPlantacao
    FROM dual
    WHERE EXISTS (
        SELECT 1
        FROM OperacaoPlantacao OP
        WHERE OP.OperacaoID = :NEW.OperacaoID
    );

    IF existsSetor = 1 OR existsPlantacao = 1 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Esta operação já está relacionada com um(a) setor/plantação.');
    END IF;
END;
/;
CREATE OR REPLACE TRIGGER ChechUniqueOperacaoIDPlantacao
BEFORE INSERT OR UPDATE ON OperacaoPlantacao
FOR EACH ROW
DECLARE
    existsSetor NUMBER;
    existsParcela NUMBER;
BEGIN
    SELECT 1
    INTO existsSetor
    FROM dual
    WHERE EXISTS (
        SELECT 1
        FROM OperacaoSetor OS
        WHERE OS.OperacaoID = :NEW.OperacaoID
    );

    SELECT 1
    INTO existsParcela
    FROM dual
    WHERE EXISTS (
        SELECT 1
        FROM OperacaoParcela OP
        WHERE OP.OperacaoID = :NEW.OperacaoID
    );

    IF existsSetor = 1 OR existsParcela = 1 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Esta operação já está relacionada com um(a) setor/parcela.');
    END IF;
END;
/;
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
      RAISE_APPLICATION_ERROR(-20001, 'Se o TipoCultura é Temporária, então Unidade têm de ser ha');
    ELSIF designacao != 'Temporária' AND :NEW.Unidade != 'un' THEN
      RAISE_APPLICATION_ERROR(-20001, 'Se o TipoCultura é Permanente, então Unidade têm de ser un');
    END IF;

  END IF;
END;
/;
CREATE OR REPLACE TRIGGER CheckCaudal
BEFORE INSERT OR UPDATE ON PlanoSetor
FOR EACH ROW
DECLARE
    debitoMaximo NUMBER;
BEGIN
    SELECT DebitoMaximo INTO debitoMaximo
    FROM SistemaRega
    WHERE EspacoID = (
        SELECT SistemaRegaEspacoID
        FROM PlanoRega
        WHERE AnoInsercao = :new.PlanoRegaAnoInsercao
    );

    IF :new.Caudal > debitoMaximo THEN
        RAISE_APPLICATION_ERROR(-20001, 'O valor de Caudal ultrapassa os limites do valor previsto para o Sistema de Rega');
    END IF;
END;
/;
CREATE OR REPLACE TRIGGER CheckUniqueTipoSensor
BEFORE INSERT OR UPDATE ON SensorEstacao
FOR EACH ROW
DECLARE
    exists NUMBER;
BEGIN
    IF INSERTING THEN
        SELECT 1
        INTO exists
        FROM dual
        WHERE EXISTS (
            SELECT 1
            FROM SensorEstacao SE
            JOIN Sensor S ON SE.SensorID = S.ID
            WHERE S.TipoSensorID = :NEW.TipoSensorID
            AND SE.EstacaoMeteorologicaID = :NEW.EstacaoMeteorologicaID
        );

        IF exists = 1 THEN
            RAISE_APPLICATION_ERROR(-20001, 'Já existe um sensor desse tipo associado a esta estação meteorológica.');
        END IF;
    ELSIF UPDATING THEN
        SELECT 1
        INTO exists
        FROM dual
        WHERE EXISTS (
            SELECT 1
            FROM SensorEstacao SE
            JOIN Sensor S ON SE.SensorID = S.ID
            WHERE S.TipoSensorID = :NEW.TipoSensorID
            AND SE.EstacaoMeteorologicaID = :NEW.EstacaoMeteorologicaID
            AND SE.SensorID != :NEW.SensorID
        );

        IF exists = 1 THEN
            RAISE_APPLICATION_ERROR(-20001, 'Já existe um sensor desse tipo associado a esta estação meteorológica.');
        END IF;
    END IF;
END;
/
;
CREATE OR REPLACE TRIGGER CheckUniqueSensorIDEstacao
BEFORE INSERT OR UPDATE ON SensorEstacao
FOR EACH ROW
DECLARE
    exists NUMBER;
BEGIN
    SELECT 1
    INTO exists
    FROM dual
    WHERE EXISTS (
        SELECT 1
        FROM SensorParcela SP
        WHERE SP.SensorID = :NEW.SensorID
    );

    IF exists = 1 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Este sensor já está a ser usado numa Parcela.');
    END IF;
END;
/
;
CREATE OR REPLACE TRIGGER CheckUniqueSensorIDParcela
BEFORE INSERT OR UPDATE ON SensorParcela
FOR EACH ROW
DECLARE
    exists NUMBER;
BEGIN
    SELECT 1
    INTO exists
    FROM dual
    WHERE EXISTS (
        SELECT 1
        FROM SensorEstacao SE
        WHERE SE.SensorID = :NEW.SensorID
    );

    IF exists = 1 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Este sensor já está a ser usado numa Estação Meteorológica.');
    END IF;
END;
/
;