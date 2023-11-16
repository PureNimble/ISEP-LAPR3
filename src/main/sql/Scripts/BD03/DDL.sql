DROP TRIGGER CheckPlantacaoUnidade;
DROP TRIGGER CheckAnos;
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
DROP TABLE Parcela CASCADE CONSTRAINTS;
DROP TABLE PlanoHora CASCADE CONSTRAINTS;
DROP TABLE PlanoRega CASCADE CONSTRAINTS;
DROP TABLE PlanoRegaExecucao CASCADE CONSTRAINTS;
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
DROP TABLE Variedade CASCADE CONSTRAINTS;

CREATE TABLE Aplicacao (
  ID         number(10) NOT NULL, 
  Designacao varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID));
CREATE TABLE AplicacaoProduto (
  FatorProducaoID number(10) NOT NULL, 
  AplicacaoID     number(10) NOT NULL, 
  PRIMARY KEY (FatorProducaoID, 
  AplicacaoID));
CREATE TABLE Armazem (
  EspacoID   number(10) NOT NULL, 
  Capacidade number(10) DEFAULT 300 NOT NULL CHECK(Capacidade > 0), 
  PRIMARY KEY (EspacoID));
CREATE TABLE CadernoCampo (
  ID       number(10) NOT NULL, 
  QuintaID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Cultura (
  ID            number(10) NOT NULL, 
  Poda          varchar2(255), 
  Floracao      varchar2(255), 
  Colheita      varchar2(255), 
  Sementeira    varchar2(255), 
  NomeEspecieID number(10) NOT NULL, 
  TipoCulturaID number(10) NOT NULL, 
  VariedadeID   number(10) NOT NULL, 
  ProdutoID     number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Elemento (
  ID         number(10) NOT NULL, 
  Designacao varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID));
CREATE TABLE ElementoFicha (
  FatorProducaoID number(10) NOT NULL, 
  ElementoID      number(10) NOT NULL, 
  Quantidade      double precision NOT NULL CHECK(Quantidade > 0), 
  PRIMARY KEY (FatorProducaoID, 
  ElementoID));
CREATE TABLE Espaco (
  ID         number(10) NOT NULL, 
  Designacao varchar2(255) NOT NULL, 
  Area       double precision NOT NULL CHECK(Area > 0), 
  Unidade    varchar2(255) NOT NULL, 
  QuintaID   number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Estabulo (
  EspacoID number(10) NOT NULL, 
  PRIMARY KEY (EspacoID));
CREATE TABLE EstacaoMeteorologica (
  ID       number(10) NOT NULL, 
  QuintaID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE FatorProducao (
  ID            number(10) NOT NULL, 
  Designacao    varchar2(255) NOT NULL UNIQUE, 
  Fabricante    varchar2(255) NOT NULL, 
  Formato       number(10) NOT NULL, 
  TipoProdutoID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Fertilizacao (
  OperacaoID         number(10) NOT NULL, 
  ModoFertilizacaoID number(10) NOT NULL, 
  PRIMARY KEY (OperacaoID));
CREATE TABLE Garagem (
  EspacoID number(10) NOT NULL, 
  PRIMARY KEY (EspacoID));
CREATE TABLE Hora (
  HoraInicial timestamp(0) NOT NULL, 
  PRIMARY KEY (HoraInicial));
CREATE TABLE ModoFertilizacao (
  ID         number(10) NOT NULL, 
  Designacao varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID));
CREATE TABLE NomeEspecie (
  ID        number(10) NOT NULL, 
  NomeComum varchar2(255) NOT NULL, 
  Especie   varchar2(255) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Operacao (
  ID             number(10) NOT NULL, 
  DataOperacao   date NOT NULL, 
  Quantidade     number(10) NOT NULL, 
  Unidade        varchar2(255) NOT NULL, 
  TipoOperacaoID number(10) NOT NULL, 
  CadernoCampoID number(10) NOT NULL, 
  PlantacaoID    number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE OperacaoFator (
  OperacaoID      number(10) NOT NULL, 
  FatorProducaoID number(10) NOT NULL, 
  PRIMARY KEY (OperacaoID));
CREATE TABLE Parcela (
  EspacoID number(10) NOT NULL, 
  PRIMARY KEY (EspacoID));
CREATE TABLE PlanoHora (
  HoraHoraInicial timestamp(0) NOT NULL, 
  PlanoRegaID     number(10) NOT NULL, 
  PRIMARY KEY (HoraHoraInicial, 
  PlanoRegaID));
CREATE TABLE PlanoRega (
  AnoInsercao number(10) NOT NULL, 
  PRIMARY KEY (AnoInsercao));
CREATE TABLE PlanoRegaExecucao (
  DataExecucao         date NOT NULL, 
  PlanoRegaAnoInsercao number(10) NOT NULL, 
  Execucao             varchar2(255) NOT NULL CHECK(Execucao IN ('Sim', 'Não')), 
  SistemaRegaEspacoID  number(10) NOT NULL, 
  PRIMARY KEY (DataExecucao, 
  PlanoRegaAnoInsercao));
CREATE TABLE PlanoSetor (
  SetorID              number(10) NOT NULL, 
  PlanoRegaAnoInsercao number(10) NOT NULL, 
  Duracao              timestamp(7) NOT NULL, 
  Dispercao            varchar2(255) NOT NULL, 
  Periodicidade        number(10) NOT NULL, 
  PRIMARY KEY (SetorID, 
  PlanoRegaAnoInsercao));
CREATE TABLE Plantacao (
  ID               number(10) NOT NULL, 
  DataInicial      date NOT NULL, 
  DataFinal        date, 
  Quantidade       double precision NOT NULL CHECK(Quantidade > 0), 
  Unidade          varchar2(255) NOT NULL, 
  EstadoFenologico varchar2(255) NOT NULL, 
  CulturaID        number(10) NOT NULL, 
  ParcelaEspacoID  number(10) NOT NULL, 
  PRIMARY KEY (ID), 
  CONSTRAINT CheckDates 
    CHECK ((DataInicial <= DataFinal)));
CREATE TABLE PlantacaoSetor (
  PlantacaoID number(10) NOT NULL, 
  SetorID     number(10) NOT NULL, 
  PRIMARY KEY (PlantacaoID));
CREATE TABLE Produto (
  ID          number(10) NOT NULL, 
  Designacao  varchar2(255) NOT NULL UNIQUE, 
  VariedadeID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE ProdutoArmazem (
  ArmazemEspacoID number(10) NOT NULL, 
  ProdutoID       number(10) NOT NULL, 
  Quantidade      number(10) DEFAULT 0 NOT NULL CHECK(Quantidade >= 0), 
  PRIMARY KEY (ArmazemEspacoID, 
  ProdutoID));
CREATE TABLE Quinta (
  ID         number(10) NOT NULL, 
  Designacao varchar2(255) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE RegistoSensor (
  ID             number(10) NOT NULL, 
  Valor          double precision NOT NULL, 
  DataRegisto    date NOT NULL, 
  SensorID       number(10) NOT NULL, 
  CadernoCampoID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Sensor (
  ID           number(10) NOT NULL, 
  TipoSensorID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE SensorEstacao (
  SensorID               number(10) NOT NULL, 
  EstacaoMeteorologicaID number(10) NOT NULL, 
  PRIMARY KEY (SensorID));
CREATE TABLE SensorParcela (
  SensorID        number(10) NOT NULL, 
  ParcelaEspacoID number(10) NOT NULL, 
  PRIMARY KEY (SensorID));
CREATE TABLE Setor (
  ID         number(10) NOT NULL, 
  Designacao varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID));
CREATE TABLE SistemaRega (
  EspacoID     number(10) NOT NULL, 
  DebitoMaximo number(10) NOT NULL CHECK(DebitoMaximo > 0), 
  PRIMARY KEY (EspacoID));
CREATE TABLE TipoCultura (
  ID         number(10) NOT NULL, 
  Designacao varchar2(255) NOT NULL UNIQUE CHECK(Designacao IN ('Permanente', 'Temporária')), 
  PRIMARY KEY (ID));
CREATE TABLE TipoOperacao (
  ID         number(10) NOT NULL, 
  Designacao varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID));
CREATE TABLE TipoProduto (
  ID         number(10) NOT NULL, 
  Designacao varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID));
CREATE TABLE TipoSensor (
  ID         number(10) NOT NULL, 
  Designacao varchar2(255) NOT NULL, 
  Unidade    varchar2(255) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Variedade (
  ID        number(10) NOT NULL, 
  Variedade varchar2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID));

ALTER TABLE Sensor ADD CONSTRAINT FKSensor879684 FOREIGN KEY (TipoSensorID) REFERENCES TipoSensor (ID);
ALTER TABLE Operacao ADD CONSTRAINT FKOperacao529665 FOREIGN KEY (PlantacaoID) REFERENCES Plantacao (ID);
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
ALTER TABLE Cultura ADD CONSTRAINT FKCultura293427 FOREIGN KEY (VariedadeID) REFERENCES Variedade (ID);
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
ALTER TABLE ProdutoArmazem ADD CONSTRAINT FKProdutoArm115605 FOREIGN KEY (ProdutoID) REFERENCES Produto (ID);
ALTER TABLE Produto ADD CONSTRAINT FKProduto242363 FOREIGN KEY (VariedadeID) REFERENCES Variedade (ID);
ALTER TABLE Cultura ADD CONSTRAINT FKCultura981712 FOREIGN KEY (ProdutoID) REFERENCES Produto (ID);
ALTER TABLE PlanoRegaExecucao ADD CONSTRAINT FKPlanoRegaE472576 FOREIGN KEY (SistemaRegaEspacoID) REFERENCES SistemaRega (EspacoID);
ALTER TABLE PlanoHora ADD CONSTRAINT FKPlanoHora51099 FOREIGN KEY (HoraHoraInicial) REFERENCES Hora (HoraInicial);
ALTER TABLE PlanoHora ADD CONSTRAINT FKPlanoHora421262 FOREIGN KEY (PlanoRegaID) REFERENCES PlanoRega (AnoInsercao);
ALTER TABLE PlanoSetor ADD CONSTRAINT FKPlanoSetor822837 FOREIGN KEY (SetorID) REFERENCES Setor (ID);
ALTER TABLE PlanoRegaExecucao ADD CONSTRAINT FKPlanoRegaE920877 FOREIGN KEY (PlanoRegaAnoInsercao) REFERENCES PlanoRega (AnoInsercao);
ALTER TABLE PlanoSetor ADD CONSTRAINT FKPlanoSetor30287 FOREIGN KEY (PlanoRegaAnoInsercao) REFERENCES PlanoRega (AnoInsercao);

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
      RAISE_APPLICATION_ERROR(-20001, 'Constraint violation: Se o TipoCultura é Temporária, então Unidade têm de ser ha');
    ELSIF designacao != 'Temporária' AND :NEW.Unidade != 'un' THEN
      RAISE_APPLICATION_ERROR(-20001, 'Constraint violation: Se o TipoCultura é Permanente, então Unidade têm de ser un');
    END IF;
  END IF;
END;
/;
CREATE OR REPLACE TRIGGER CheckAnos
BEFORE INSERT OR UPDATE ON PlanoRegaExecucao
FOR EACH ROW
BEGIN
  IF EXTRACT(YEAR FROM :NEW.date) != :NEW.year THEN
    RAISE_APPLICATION_ERROR(-20001, 'Constraint violation: O ano em da data da execução e do plano de rega devem ser iguais');
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
