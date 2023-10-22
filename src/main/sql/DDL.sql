DROP TABLE AplicaoProduto CASCADE CONSTRAINTS;
DROP TABLE CadernoCampo CASCADE CONSTRAINTS;
DROP TABLE Cultura CASCADE CONSTRAINTS;
DROP TABLE Edificio CASCADE CONSTRAINTS;
DROP TABLE Elemento CASCADE CONSTRAINTS;
DROP TABLE ElementoFicha CASCADE CONSTRAINTS;
DROP TABLE EstacaoMeteorologica CASCADE CONSTRAINTS;
DROP TABLE FatorDeProducao CASCADE CONSTRAINTS;
DROP TABLE Fertilizacao CASCADE CONSTRAINTS;
DROP TABLE FichaTecnica CASCADE CONSTRAINTS;
DROP TABLE ModoFertilizicao CASCADE CONSTRAINTS;
DROP TABLE Operacao CASCADE CONSTRAINTS;
DROP TABLE Parcela CASCADE CONSTRAINTS;
DROP TABLE Plantacao CASCADE CONSTRAINTS;
DROP TABLE Quinta CASCADE CONSTRAINTS;
DROP TABLE RegistoSensor CASCADE CONSTRAINTS;
DROP TABLE Sensor CASCADE CONSTRAINTS;
DROP TABLE TipoCultura CASCADE CONSTRAINTS;
DROP TABLE TipoEdificio CASCADE CONSTRAINTS;
DROP TABLE TipoOperacao CASCADE CONSTRAINTS;
DROP TABLE TipoProduto CASCADE CONSTRAINTS;
DROP TABLE TipoSensor CASCADE CONSTRAINTS;

CREATE TABLE AplicaoProduto (
  ID                number(10) GENERATED AS IDENTITY, 
  Designacao        varchar2(255) NOT NULL, 
  FatorDeProducaoID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE CadernoCampo (
  ID number(10) GENERATED AS IDENTITY, 
  PRIMARY KEY (ID));
CREATE TABLE Cultura (
  ID            number(10) GENERATED AS IDENTITY, 
  Especie       varchar2(255) NOT NULL, 
  NomeComum     varchar2(255) NOT NULL, 
  Variedade     varchar2(255) NOT NULL, 
  Poda          varchar2(255) NOT NULL, 
  Floracao      varchar2(255) NOT NULL, 
  Colheita      varchar2(255) NOT NULL, 
  Sementeira    varchar2(255) NOT NULL, 
  TipoCulturaID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Edificio (
  ID             number(10) GENERATED AS IDENTITY, 
  Designacao     varchar2(255) NOT NULL, 
  Area           number(10) NOT NULL, 
  Unidade        varchar2(255) NOT NULL, 
  QuintaID       number(10) NOT NULL, 
  TipoEdificioID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Elemento (
  ID         number(10) GENERATED AS IDENTITY, 
  Designacao varchar2(255) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE ElementoFicha (
  FichaTecnicaID number(10) NOT NULL, 
  ElementoID     number(10) NOT NULL, 
  Quantidade     number(10) NOT NULL, 
  Unidade        varchar2(255) NOT NULL, 
  PRIMARY KEY (FichaTecnicaID, 
  ElementoID));
CREATE TABLE EstacaoMeteorologica (
  ID       number(10) GENERATED AS IDENTITY, 
  QuintaID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE FatorDeProducao (
  ID            number(10) GENERATED AS IDENTITY, 
  Designacao    varchar2(255) NOT NULL, 
  Fabricante    varchar2(255) NOT NULL, 
  Formato       varchar2(255) NOT NULL, 
  TipoProdutoID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Fertilizacao (
  OperacaoID         number(10) NOT NULL, 
  FatorDeProducaoID  number(10) NOT NULL, 
  ModoFertilizicaoID number(10) NOT NULL, 
  PRIMARY KEY (OperacaoID));
CREATE TABLE FichaTecnica (
  ID                number(10) GENERATED AS IDENTITY, 
  FatorDeProducaoID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE ModoFertilizicao (
  ID         number(10) GENERATED AS IDENTITY, 
  Designacao varchar2(255) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Operacao (
  ID             number(10) GENERATED AS IDENTITY, 
  Data           date NOT NULL, 
  Quantidade     number(10) NOT NULL, 
  Unidade        varchar2(255) NOT NULL, 
  PlantacaoID    number(10) NOT NULL, 
  TipoOperacaoID number(10) NOT NULL, 
  CadernoCampoID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Parcela (
  ID         number(10) GENERATED AS IDENTITY, 
  Designacao varchar2(255) NOT NULL, 
  Area       number(10) NOT NULL, 
  QuintaID   number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Plantacao (
  ID          number(10) GENERATED AS IDENTITY, 
  DataInicial date NOT NULL, 
  DataFinal   date NOT NULL, 
  Quantidade  number(10) NOT NULL, 
  Unidade     varchar2(255) NOT NULL, 
  ParcelaID   number(10) NOT NULL, 
  CulturaID   number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Quinta (
  ID         number(10) GENERATED AS IDENTITY, 
  Designacao varchar2(255) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE RegistoSensor (
  ID                     number(10) GENERATED AS IDENTITY, 
  Valor                  number(10) NOT NULL, 
  Data                   date NOT NULL, 
  EstacaoMeteorologicaID number(10) NOT NULL, 
  SensorID               number(10) NOT NULL, 
  CadernoCampoID         number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE Sensor (
  ID           number(10) GENERATED AS IDENTITY, 
  TipoSensorID number(10) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE TipoCultura (
  ID         number(10) GENERATED AS IDENTITY, 
  Designacao varchar2(255) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE TipoEdificio (
  ID         number(10) GENERATED AS IDENTITY, 
  Designacao varchar2(255) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE TipoOperacao (
  ID         number(10) GENERATED AS IDENTITY, 
  Designacao varchar2(255) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE TipoProduto (
  ID         number(10) GENERATED AS IDENTITY, 
  Designacao varchar2(255) NOT NULL, 
  PRIMARY KEY (ID));
CREATE TABLE TipoSensor (
  ID         number(10) GENERATED AS IDENTITY, 
  Designacao varchar2(255) NOT NULL, 
  Unidade    varchar2(255) NOT NULL, 
  PRIMARY KEY (ID));
  
ALTER TABLE Edificio ADD CONSTRAINT FKEdificio778486 FOREIGN KEY (TipoEdificioID) REFERENCES TipoEdificio (ID);
ALTER TABLE Edificio ADD CONSTRAINT FKEdificio681139 FOREIGN KEY (QuintaID) REFERENCES Quinta (ID);
ALTER TABLE Parcela ADD CONSTRAINT FKParcela842886 FOREIGN KEY (QuintaID) REFERENCES Quinta (ID);
ALTER TABLE Plantacao ADD CONSTRAINT FKPlantacao85856 FOREIGN KEY (ParcelaID) REFERENCES Parcela (ID);
ALTER TABLE Operacao ADD CONSTRAINT FKOperacao529665 FOREIGN KEY (PlantacaoID) REFERENCES Plantacao (ID);
ALTER TABLE AplicaoProduto ADD CONSTRAINT FKAplicaoPro761794 FOREIGN KEY (FatorDeProducaoID) REFERENCES FatorDeProducao (ID);
ALTER TABLE FichaTecnica ADD CONSTRAINT FKFichaTecni776683 FOREIGN KEY (FatorDeProducaoID) REFERENCES FatorDeProducao (ID);
ALTER TABLE ElementoFicha ADD CONSTRAINT FKElementoFi602363 FOREIGN KEY (FichaTecnicaID) REFERENCES FichaTecnica (ID);
ALTER TABLE RegistoSensor ADD CONSTRAINT FKRegistoSen577174 FOREIGN KEY (EstacaoMeteorologicaID) REFERENCES EstacaoMeteorologica (ID);
ALTER TABLE Operacao ADD CONSTRAINT FKOperacao623592 FOREIGN KEY (TipoOperacaoID) REFERENCES TipoOperacao (ID);
ALTER TABLE FatorDeProducao ADD CONSTRAINT FKFatorDePro879583 FOREIGN KEY (TipoProdutoID) REFERENCES TipoProduto (ID);
ALTER TABLE EstacaoMeteorologica ADD CONSTRAINT FKEstacaoMet933281 FOREIGN KEY (QuintaID) REFERENCES Quinta (ID);
ALTER TABLE RegistoSensor ADD CONSTRAINT FKRegistoSen307126 FOREIGN KEY (SensorID) REFERENCES Sensor (ID);
ALTER TABLE Sensor ADD CONSTRAINT FKSensor879684 FOREIGN KEY (TipoSensorID) REFERENCES TipoSensor (ID);
ALTER TABLE RegistoSensor ADD CONSTRAINT FKRegistoSen457242 FOREIGN KEY (CadernoCampoID) REFERENCES CadernoCampo (ID);
ALTER TABLE Operacao ADD CONSTRAINT FKOperacao517013 FOREIGN KEY (CadernoCampoID) REFERENCES CadernoCampo (ID);
ALTER TABLE Plantacao ADD CONSTRAINT FKPlantacao171838 FOREIGN KEY (CulturaID) REFERENCES Cultura (ID);
ALTER TABLE Cultura ADD CONSTRAINT FKCultura303785 FOREIGN KEY (TipoCulturaID) REFERENCES TipoCultura (ID);
ALTER TABLE Fertilizacao ADD CONSTRAINT FKFertilizac483896 FOREIGN KEY (OperacaoID) REFERENCES Operacao (ID);
ALTER TABLE Fertilizacao ADD CONSTRAINT FKFertilizac457773 FOREIGN KEY (ModoFertilizicaoID) REFERENCES ModoFertilizicao (ID);
ALTER TABLE Fertilizacao ADD CONSTRAINT FKFertilizac478523 FOREIGN KEY (FatorDeProducaoID) REFERENCES FatorDeProducao (ID);
ALTER TABLE ElementoFicha ADD CONSTRAINT FKElementoFi523793 FOREIGN KEY (ElementoID) REFERENCES Elemento (ID);
