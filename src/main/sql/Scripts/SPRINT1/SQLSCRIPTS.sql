REM   Script: LAPR3_SPRINT1
REM   Script correspondente ao Sprint1 de BDDAD

DROP TABLE Aplicacao CASCADE CONSTRAINTS;

DROP TABLE AplicacaoFitofarmaco CASCADE CONSTRAINTS;

DROP TABLE AplicacaoProduto CASCADE CONSTRAINTS;

DROP TABLE CadernoDeCampo CASCADE CONSTRAINTS;

DROP TABLE Cultura CASCADE CONSTRAINTS;

DROP TABLE Edificio CASCADE CONSTRAINTS;

DROP TABLE Elemento CASCADE CONSTRAINTS;

DROP TABLE ElementoFicha CASCADE CONSTRAINTS;

DROP TABLE EstacaoMeteorologica CASCADE CONSTRAINTS;

DROP TABLE FatorDeProducao CASCADE CONSTRAINTS;

DROP TABLE Fertilizacao CASCADE CONSTRAINTS;

DROP TABLE ModoFertilizacao CASCADE CONSTRAINTS;

DROP TABLE NomeEspecie CASCADE CONSTRAINTS;

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

CREATE TABLE APLICACAO ( 
  ID NUMBER(10) NOT NULL, 
  DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID) 
);

CREATE TABLE APLICACAOFITOFARMACO ( 
  OPERACAOID NUMBER(10) NOT NULL, 
  FATORDEPRODUCAOID NUMBER(10) NOT NULL, 
  PRIMARY KEY (OPERACAOID) 
);

CREATE TABLE APLICACAOPRODUTO ( 
  FATORDEPRODUCAOID NUMBER(10) NOT NULL, 
  APLICACAOID NUMBER(10) NOT NULL, 
  PRIMARY KEY (FATORDEPRODUCAOID, APLICACAOID) 
);

CREATE TABLE CADERNODECAMPO ( 
  ID NUMBER(10) NOT NULL, 
  QUINTAID NUMBER(10) NOT NULL, 
  PRIMARY KEY (ID) 
);

CREATE TABLE CULTURA ( 
  ID NUMBER(10) NOT NULL, 
  VARIEDADE VARCHAR2(255), 
  PODA VARCHAR2(255), 
  FLORACAO VARCHAR2(255), 
  COLHEITA VARCHAR2(255), 
  SEMENTEIRA VARCHAR2(255), 
  NOMEESPECIEID NUMBER(10) NOT NULL, 
  TIPOCULTURAID NUMBER(10) NOT NULL, 
  PRIMARY KEY (ID) 
);

CREATE TABLE EDIFICIO ( 
  ID NUMBER(10) NOT NULL, 
  DESIGNACAO VARCHAR2(255) NOT NULL, 
  AREA NUMBER(10), 
  UNIDADE VARCHAR2(255), 
  TIPOEDIFICIOID NUMBER(10) NOT NULL, 
  QUINTAID NUMBER(10) NOT NULL, 
  PRIMARY KEY (ID) 
);

CREATE TABLE ELEMENTO ( 
  ID NUMBER(10) NOT NULL, 
  DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID) 
);

CREATE TABLE ELEMENTOFICHA ( 
  FATORDEPRODUCAOID NUMBER(10) NOT NULL, 
  ELEMENTOID NUMBER(10) NOT NULL, 
  QUANTIDADE VARCHAR2(255) NOT NULL, 
  PRIMARY KEY (FATORDEPRODUCAOID, ELEMENTOID) 
);

CREATE TABLE ESTACAOMETEOROLOGICA ( 
  ID NUMBER(10) NOT NULL, 
  QUINTAID NUMBER(10) NOT NULL, 
  PRIMARY KEY (ID) 
);

CREATE TABLE FATORDEPRODUCAO ( 
  ID NUMBER(10) NOT NULL, 
  DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE, 
  FABRICANTE VARCHAR2(255) NOT NULL, 
  FORMATO VARCHAR2(255) NOT NULL, 
  TIPOPRODUTOID NUMBER(10) NOT NULL, 
  PRIMARY KEY (ID) 
);

CREATE TABLE FERTILIZACAO ( 
  OPERACAOID NUMBER(10) NOT NULL, 
  MODOFERTILIZACAOID NUMBER(10) NOT NULL, 
  FATORDEPRODUCAOID NUMBER(10) NOT NULL, 
  PRIMARY KEY (OPERACAOID) 
);

CREATE TABLE MODOFERTILIZACAO ( 
  ID NUMBER(10) NOT NULL, 
  DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID) 
);

CREATE TABLE NOMEESPECIE ( 
  ID NUMBER(10) NOT NULL, 
  NOMECOMUM VARCHAR2(255) NOT NULL, 
  ESPECIE VARCHAR2(255) NOT NULL UNIQUE, 
  PRIMARY KEY (ID) 
);

CREATE TABLE OPERACAO ( 
  ID NUMBER(10) NOT NULL, 
  DATAOPERACAO DATE NOT NULL, 
  QUANTIDADE NUMBER(10), 
  UNIDADE VARCHAR2(255), 
  PLANTACAOID NUMBER(10), 
  CADERNODECAMPOID NUMBER(10) NOT NULL, 
  TIPOOPERACAOID NUMBER(10) NOT NULL, 
  PARCELAID NUMBER(10) NOT NULL, 
  PRIMARY KEY (ID) 
);

CREATE TABLE PARCELA ( 
  ID NUMBER(10) NOT NULL, 
  DESIGNACAO VARCHAR2(255) NOT NULL, 
  AREA NUMBER(10) NOT NULL, 
  QUINTAID NUMBER(10) NOT NULL, 
  PRIMARY KEY (ID) 
);

CREATE TABLE PLANTACAO ( 
  ID NUMBER(10) NOT NULL, 
  DATAINICIAL DATE NOT NULL, 
  DATAFINAL DATE, 
  QUANTIDADE NUMBER(10) NOT NULL, 
  UNIDADE VARCHAR2(255) NOT NULL, 
  PARCELAID NUMBER(10) NOT NULL, 
  CULTURAID NUMBER(10) NOT NULL, 
  PRIMARY KEY (ID) 
);

CREATE TABLE QUINTA ( 
  ID NUMBER(10) NOT NULL, 
  DESIGNACAO VARCHAR2(255) NOT NULL, 
  PRIMARY KEY (ID) 
);

CREATE TABLE REGISTOSENSOR ( 
  ID NUMBER(10) NOT NULL, 
  VALOR NUMBER(10) NOT NULL, 
  DATAREGISTO DATE NOT NULL, 
  ESTACAOMETEOROLOGICAID NUMBER(10) NOT NULL, 
  SENSORID NUMBER(10) NOT NULL, 
  CADERNODECAMPOID NUMBER(10) NOT NULL, 
  PRIMARY KEY (ID) 
);

CREATE TABLE SENSOR ( 
  ID NUMBER(10) NOT NULL, 
  TIPOSENSORID NUMBER(10) NOT NULL, 
  PRIMARY KEY (ID) 
);

CREATE TABLE TIPOCULTURA ( 
  ID NUMBER(10) NOT NULL, 
  DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE CHECK(DESIGNACAO IN ('Permanente', 'Temporária')), 
  PRIMARY KEY (ID) 
);

CREATE TABLE TIPOEDIFICIO ( 
  ID NUMBER(10) NOT NULL, 
  DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE, 
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
  DESIGNACAO VARCHAR2(255) NOT NULL UNIQUE, 
  UNIDADE VARCHAR2(255) NOT NULL, 
  PRIMARY KEY (ID) 
);

ALTER TABLE Plantacao ADD CONSTRAINT FKPlantacao85856 FOREIGN KEY (ParcelaID) REFERENCES Parcela (ID);

ALTER TABLE Operacao ADD CONSTRAINT FKOperacao529665 FOREIGN KEY (PlantacaoID) REFERENCES Plantacao (ID);

ALTER TABLE RegistoSensor ADD CONSTRAINT FKRegistoSen577174 FOREIGN KEY (EstacaoMeteorologicaID) REFERENCES EstacaoMeteorologica (ID);

ALTER TABLE RegistoSensor ADD CONSTRAINT FKRegistoSen307126 FOREIGN KEY (SensorID) REFERENCES Sensor (ID);

ALTER TABLE Sensor ADD CONSTRAINT FKSensor879684 FOREIGN KEY (TipoSensorID) REFERENCES TipoSensor (ID);

ALTER TABLE RegistoSensor ADD CONSTRAINT FKRegistoSen387930 FOREIGN KEY (CadernoDeCampoID) REFERENCES CadernoDeCampo (ID);

ALTER TABLE Operacao ADD CONSTRAINT FKOperacao671840 FOREIGN KEY (CadernoDeCampoID) REFERENCES CadernoDeCampo (ID);

ALTER TABLE Plantacao ADD CONSTRAINT FKPlantacao171838 FOREIGN KEY (CulturaID) REFERENCES Cultura (ID);

ALTER TABLE Fertilizacao ADD CONSTRAINT FKFertilizac483896 FOREIGN KEY (OperacaoID) REFERENCES Operacao (ID);

ALTER TABLE Fertilizacao ADD CONSTRAINT FKFertilizac729538 FOREIGN KEY (ModoFertilizacaoID) REFERENCES ModoFertilizacao (ID);

ALTER TABLE ElementoFicha ADD CONSTRAINT FKElementoFi523793 FOREIGN KEY (ElementoID) REFERENCES Elemento (ID);

ALTER TABLE Edificio ADD CONSTRAINT FKEdificio681139 FOREIGN KEY (QuintaID) REFERENCES Quinta (ID);

ALTER TABLE Parcela ADD CONSTRAINT FKParcela842886 FOREIGN KEY (QuintaID) REFERENCES Quinta (ID);

ALTER TABLE EstacaoMeteorologica ADD CONSTRAINT FKEstacaoMet933281 FOREIGN KEY (QuintaID) REFERENCES Quinta (ID);

ALTER TABLE CadernoDeCampo ADD CONSTRAINT FKCadernoDeC804960 FOREIGN KEY (QuintaID) REFERENCES Quinta (ID);

ALTER TABLE AplicacaoProduto ADD CONSTRAINT FKAplicacaoP490933 FOREIGN KEY (FatorDeProducaoID) REFERENCES FatorDeProducao (ID);

ALTER TABLE Fertilizacao ADD CONSTRAINT FKFertilizac478523 FOREIGN KEY (FatorDeProducaoID) REFERENCES FatorDeProducao (ID);

ALTER TABLE Operacao ADD CONSTRAINT FKOperacao623592 FOREIGN KEY (TipoOperacaoID) REFERENCES TipoOperacao (ID);

ALTER TABLE Edificio ADD CONSTRAINT FKEdificio778486 FOREIGN KEY (TipoEdificioID) REFERENCES TipoEdificio (ID);

ALTER TABLE AplicacaoProduto ADD CONSTRAINT FKAplicacaoP648154 FOREIGN KEY (AplicacaoID) REFERENCES Aplicacao (ID);

ALTER TABLE ElementoFicha ADD CONSTRAINT FKElementoFi512400 FOREIGN KEY (FatorDeProducaoID) REFERENCES FatorDeProducao (ID);

ALTER TABLE FatorDeProducao ADD CONSTRAINT FKFatorDePro879583 FOREIGN KEY (TipoProdutoID) REFERENCES TipoProduto (ID);

ALTER TABLE Cultura ADD CONSTRAINT FKCultura303785 FOREIGN KEY (TipoCulturaID) REFERENCES TipoCultura (ID);

ALTER TABLE Cultura ADD CONSTRAINT FKCultura266968 FOREIGN KEY (NomeEspecieID) REFERENCES NomeEspecie (ID);

ALTER TABLE AplicacaoFitofarmaco ADD CONSTRAINT FKAplicacaoF291408 FOREIGN KEY (OperacaoID) REFERENCES Operacao (ID);

ALTER TABLE AplicacaoFitofarmaco ADD CONSTRAINT FKAplicacaoF296781 FOREIGN KEY (FatorDeProducaoID) REFERENCES FatorDeProducao (ID);

ALTER TABLE Operacao ADD CONSTRAINT FKOperacao806328 FOREIGN KEY (ParcelaID) REFERENCES Parcela (ID);

INSERT INTO Quinta (ID, Designacao) VALUES (0, 'Quinta Do Ângelo');

INSERT INTO CadernoDeCampo (ID, QuintaID) VALUES (0, 0);

INSERT INTO EstacaoMeteorologica (ID, QuintaID) VALUES(0, 0);

INSERT INTO TipoSensor (ID, Designacao, Unidade) VALUES (0, 'Pluviosidade', 'mm');

INSERT INTO Sensor (ID, TipoSensorID) VALUES (0, 0);

INSERT INTO RegistoSensor (ID, Valor, DataRegisto, EstacaoMeteorologicaID, SensorID, CadernoDeCampoID) VALUES (0, 3, TO_DATE('10/14/2020', 'MM/DD/YYYY'), 0, 0, 0);

INSERT INTO TipoSensor (ID, Designacao, Unidade) VALUES (1, 'Temperatura do Solo', '°C');

INSERT INTO Sensor (ID, TipoSensorID) VALUES (1, 1);

INSERT INTO RegistoSensor (ID, Valor, DataRegisto, EstacaoMeteorologicaID, SensorID, CadernoDeCampoID) VALUES (1, 23, TO_DATE('10/14/2020', 'MM/DD/YYYY'), 0, 1, 0);

INSERT INTO TipoSensor (ID, Designacao, Unidade) VALUES (2, 'Humidade do Solo', '%');

INSERT INTO Sensor (ID, TipoSensorID) VALUES (2, 2);

INSERT INTO RegistoSensor (ID, Valor, DataRegisto, EstacaoMeteorologicaID, SensorID, CadernoDeCampoID) VALUES (2, 34, TO_DATE('10/14/2020', 'MM/DD/YYYY'), 0, 2, 0);

INSERT INTO TipoSensor (ID, Designacao, Unidade) VALUES (3, 'Velocidade do Vento', 'k/h');

INSERT INTO Sensor (ID, TipoSensorID) VALUES (3, 3);

INSERT INTO RegistoSensor (ID, Valor, DataRegisto, EstacaoMeteorologicaID, SensorID, CadernoDeCampoID) VALUES (3, 25, TO_DATE('10/14/2020', 'MM/DD/YYYY'), 0, 3, 0);

INSERT INTO TipoSensor (ID, Designacao, Unidade) VALUES (4, 'Temperatura', '°C');

INSERT INTO Sensor (ID, TipoSensorID) VALUES (4, 4);

INSERT INTO RegistoSensor (ID, Valor, DataRegisto, EstacaoMeteorologicaID, SensorID, CadernoDeCampoID) VALUES (4, 22, TO_DATE('10/14/2020', 'MM/DD/YYYY'), 0, 4, 0);

INSERT INTO TipoSensor (ID, Designacao, Unidade) VALUES (5, 'Humidade do Ar', '%');

INSERT INTO Sensor (ID, TipoSensorID) VALUES (5, 5);

INSERT INTO RegistoSensor (ID, Valor, DataRegisto, EstacaoMeteorologicaID, SensorID, CadernoDeCampoID) VALUES (5, 60, TO_DATE('10/14/2020', 'MM/DD/YYYY'), 0, 5, 0);

INSERT INTO TipoSensor (ID, Designacao, Unidade) VALUES (6, 'Pressão Atmosférica', 'Pa');

INSERT INTO Sensor (ID, TipoSensorID) VALUES (6, 6);

INSERT INTO RegistoSensor (ID, Valor, DataRegisto, EstacaoMeteorologicaID, SensorID, CadernoDeCampoID) VALUES (6, 23, TO_DATE('10/14/2020', 'MM/DD/YYYY'), 0, 6, 0);

INSERT INTO TipoCultura (ID, Designacao) VALUES (0, 'Permanente');

INSERT INTO NomeEspecie (ID, NomeComum, Especie) VALUES (0, 'Ameixoeira', 'Prunus domestica');

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (0, 'RAINHA CLAUDIA CARANGUEJEIRA', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (1, 'PRESIDENT', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (2, 'STANLEY', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (3, 'ANGELENO', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (4, 'BLACK BEAUTY', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (5, 'BLACK STAR', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (6, 'BLACK GOLD', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (7, 'BLACK DIAMOND', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (8, 'BLACK AMBER', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (9, 'BLACK SPLENDOR', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (10, 'FORTUNA', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (11, 'FRIAR', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (12, 'EL DORADO', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (13, 'ELEPHANT HEART', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (14, 'GOLDEN JAPAN', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (15, 'HARRY PITCHON', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (16, 'LAETITIA', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (17, 'METLEY', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (18, 'MIRABELLE DE NANCY', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (19, 'QUEEN ROSE', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (20, 'RED BEAUT', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (21, 'SANTA ROSA', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (22, 'SHIRO', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (23, 'SUNGOLD', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (24, 'WILSON PERFECTION', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (25, 'AUTUMN GIANT', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO NomeEspecie (ID, NomeComum, Especie) VALUES (1, 'Damasqueiro', 'Prunus armeniaca');

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (26, 'BULIDA', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus armeniaca')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (27, 'CANINO', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus armeniaca')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (28, 'LIABAUD', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus armeniaca')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (29, 'MAILLOT JAUNE', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus armeniaca')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (30, 'POLONAIS', 'Novembro a dezembro', 'Fevereiro a março', 'Julho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Prunus armeniaca')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO NomeEspecie (ID, NomeComum, Especie) VALUES (2, 'Macieira', 'Malus domestica');

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (31, 'AKANE', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (32, 'BELGOLDEN', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (33, 'BRAVO DE ESMOLFE', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (34, 'CASA NOVA DE ALCOBAÇA', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (35, 'EROVAN', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (36, 'FUJI', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (37, 'GRANNY SMITH', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (38, 'GOLDEN DELICIOUS', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (39, 'HI-EARLY', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (40, 'JONAGORED', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (41, 'LYSGOLDEN', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (42, 'MUTSU', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (43, 'PORTA DA LOJA', 'Janeiro', 'Abril a maio', 'Novembro a dezembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (44, 'REINETTE OU CANADA', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (45, 'REINETTE OU GRAND FAY', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (46, 'RISCADINHA DE PALMELA', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (47, 'ROYAL GALA', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (48, 'REDCHIEF', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (49, 'STARKING', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (50, 'SUMMER RED', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (51, 'WELL''SPUR DELICIOUS', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (52, 'NOIVA', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (53, 'OLHO ABERTO', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (54, 'CAMOESA ROSA', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (55, 'MALÁPIO', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (56, 'GRONHO DOCE', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (57, 'PÉ DE BOI', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (58, 'PINOVA', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (59, 'PARDO LINDO', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (60, 'PIPO DE BASTO', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (61, 'PRIMA', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (62, 'QUERINA', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (63, 'VISTA BELLA', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (64, 'GOLDEN SMOOTHEE', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (65, 'GOLDEN SUPREMA', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (66, 'GLOSTER 69', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (67, 'FREEDOM', 'Novembro a dezembro', 'Março a abril', 'Agosto a setembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Malus domestica')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO NomeEspecie (ID, NomeComum, Especie) VALUES (3, 'Pera Nashi', 'Pyrus pyrifolia');

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (68, 'SNINSEIKI', NULL, NULL, NULL, NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Pyrus pyrifolia')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (69, 'KUMOI', NULL, NULL, NULL, NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Pyrus pyrifolia')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (70, 'HOSUI', NULL, NULL, NULL, NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Pyrus pyrifolia')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (71, 'NIJISSEIKI', NULL, NULL, NULL, NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Pyrus pyrifolia')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO TipoCultura (ID, Designacao) VALUES (1, 'Temporária');

INSERT INTO NomeEspecie (ID, NomeComum, Especie) VALUES (4, 'Cenoura', 'Daucus carota subsp. Sativus');

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (72, 'Carson Hybrid', NULL, NULL, '80 dias', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Daucus carota subsp. Sativus')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Temporária')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (73, 'Red Cored Chantenay', NULL, NULL, '80 dias', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Daucus carota subsp. Sativus')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Temporária')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (74, 'Danvers Half Long', NULL, NULL, '80 dias', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Daucus carota subsp. Sativus')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Temporária')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (75, 'Imperator 58', NULL, NULL, '80 dias', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Daucus carota subsp. Sativus')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Temporária')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (76, 'Sugarsnax Hybrid', NULL, NULL, '80 dias', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Daucus carota subsp. Sativus')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Temporária')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (77, 'Nelson Hybrid', NULL, NULL, '80 dias', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Daucus carota subsp. Sativus')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Temporária')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (78, 'Scarlet Nantes', NULL, NULL, '80 dias', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Daucus carota subsp. Sativus')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Temporária')));

INSERT INTO NomeEspecie (ID, NomeComum, Especie) VALUES (5, 'Tremoço', 'Lupinus luteus');

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (79, 'Amarelo', NULL, NULL, NULL, NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Lupinus luteus')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Temporária')));

INSERT INTO NomeEspecie (ID, NomeComum, Especie) VALUES (6, 'Tremoço', 'Lupinus albus');

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (80, 'Branco', NULL, NULL, NULL, NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Lupinus albus')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Temporária')));

INSERT INTO NomeEspecie (ID, NomeComum, Especie) VALUES (7, 'Milho', 'Zea mays');

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (81, 'MAS 24.C', NULL, NULL, 'Julho a setembro', 'Abril a junho', (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Zea mays')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Temporária')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (82, 'Doce Golden Bantam', NULL, NULL, 'Julho a setembro', 'Abril a junho', (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Zea mays')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Temporária')));

INSERT INTO NomeEspecie (ID, NomeComum, Especie) VALUES (8, 'Nabo greleiro', 'Brassica rapa');

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (83, 'Senhora Conceição', NULL, NULL, 'Junho a fevereiro', 'Março a setembro', (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Brassica rapa')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Temporária')));

INSERT INTO NomeEspecie (ID, NomeComum, Especie) VALUES (9, 'Oliveira', 'Olea europaea');

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (84, 'COBRANÇOSA', NULL, NULL, 'Outubro a novembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Olea europaea')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (85, 'ARBEQUINA', NULL, NULL, 'Outubro a novembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Olea europaea')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (86, 'HOJIBLANCA', NULL, NULL, 'Outubro a novembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Olea europaea')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (87, 'NEGRINHA DO FREIXO', NULL, NULL, 'Outubro a novembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Olea europaea')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (88, 'PICUAL', NULL, NULL, 'Outubro a novembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Olea europaea')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (89, 'MAÇANILHA', NULL, NULL, 'Outubro a novembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Olea europaea')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (90, 'CONSERVA DE ELVAS', NULL, NULL, 'Outubro a novembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Olea europaea')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (91, 'Galega', NULL, NULL, 'Outubro a novembro', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Olea europaea')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (92, 'S. Cosme', NULL, NULL, '90 dias', 'Fevereiro a abril, agosto a outubro', (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Brassica rapa')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Temporária')));

INSERT INTO NomeEspecie (ID, NomeComum, Especie) VALUES (10, 'Videira', 'Vitis vinifera');

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (93, 'Dona Maria', 'Dezembro a janeiro', 'Maio', 'Junho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Vitis vinifera')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (94, 'Cardinal', 'Dezembro a janeiro', 'Maio', 'Junho a agosto', NULL, (SELECT ID FROM NomeEspecie WHERE UPPER(Especie) = UPPER('Vitis vinifera')), (SELECT ID FROM TipoCultura WHERE UPPER(Designacao) = UPPER('Permanente')));

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (115, NULL, NULL, NULL, NULL, NULL, 2, 0);

INSERT INTO Cultura (ID, Variedade, Poda, Floracao, Colheita, Sementeira, NomeEspecieID, TipoCulturaID) VALUES (116, NULL, NULL, NULL, NULL, NULL, 10, 0);

INSERT INTO TipoProduto (ID, Designacao) VALUES (0, 'Fitofármaco');

INSERT INTO FatorDeProducao (ID, Designacao, Fabricante, Formato, TipoProdutoId) VALUES (0, 'Calda Bordalesa ASCENZA', 'ASCENZA', 'Pó molhável', (SELECT ID FROM TipoProduto WHERE UPPER(Designacao) = UPPER('Fitofármaco')));

INSERT INTO Aplicacao (ID, Designacao) VALUES (0, 'Fungicida');

INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Calda Bordalesa ASCENZA')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER('Fungicida')));

INSERT INTO Elemento (ID, Designacao) VALUES (0, 'CU');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Calda Bordalesa ASCENZA')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('CU')), '20.0%');

INSERT INTO FatorDeProducao (ID, Designacao, Fabricante, Formato, TipoProdutoId) VALUES (1, 'Enxofre Bayer 80 WG', 'Bayer', 'Pó molhável', (SELECT ID FROM TipoProduto WHERE UPPER(Designacao) = UPPER('Fitofármaco')));

INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Enxofre Bayer 80 WG')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER('Fungicida')));

INSERT INTO Elemento (ID, Designacao) VALUES (1, 'S');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Enxofre Bayer 80 WG')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('S')), '80.0%');

INSERT INTO TipoProduto (ID, Designacao) VALUES (1, 'Adubo');

INSERT INTO FatorDeProducao (ID, Designacao, Fabricante, Formato, TipoProdutoId) VALUES (2, 'Patentkali', 'K+S', 'Granulado', (SELECT ID FROM TipoProduto WHERE UPPER(Designacao) = UPPER('Adubo')));

INSERT INTO Aplicacao (ID, Designacao) VALUES (1, 'Adubo solo');

INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Patentkali')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER('Adubo solo')));

INSERT INTO Elemento (ID, Designacao) VALUES (2, 'K');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Patentkali')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('K')), '24.9%');

INSERT INTO Elemento (ID, Designacao) VALUES (3, 'Mg');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Patentkali')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('Mg')), '6.0%');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Patentkali')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('S')), '17.6%');

INSERT INTO FatorDeProducao (ID, Designacao, Fabricante, Formato, TipoProdutoId) VALUES (3, 'ESTA Kieserit', 'K+S', 'Granulado', (SELECT ID FROM TipoProduto WHERE UPPER(Designacao) = UPPER('Adubo')));

INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('ESTA Kieserit')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER('Adubo solo')));

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('ESTA Kieserit')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('Mg')), '15.1%');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('ESTA Kieserit')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('S')), '20.8%');

INSERT INTO FatorDeProducao (ID, Designacao, Fabricante, Formato, TipoProdutoId) VALUES (4, 'EPSO Microtop', 'K+S', 'Granulado', (SELECT ID FROM TipoProduto WHERE UPPER(Designacao) = UPPER('Adubo')));

INSERT INTO Aplicacao (ID, Designacao) VALUES (2, 'Adubo foliar');

INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('EPSO Microtop')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER('Adubo foliar')));

INSERT INTO Aplicacao (ID, Designacao) VALUES (3, 'Fertirrega');

INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('EPSO Microtop')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER('Fertirrega')));

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('EPSO Microtop')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('Mg')), '9.0%');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('EPSO Microtop')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('S')), '12.4%');

INSERT INTO Elemento (ID, Designacao) VALUES (4, 'B');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('EPSO Microtop')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('B')), '0.9%');

INSERT INTO Elemento (ID, Designacao) VALUES (5, 'Mn');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('EPSO Microtop')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('Mn')), '1.0%');

INSERT INTO FatorDeProducao (ID, Designacao, Fabricante, Formato, TipoProdutoId) VALUES (5, 'EPSO Top', 'K+S', 'Granulado', (SELECT ID FROM TipoProduto WHERE UPPER(Designacao) = UPPER('Adubo')));

INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('EPSO Top')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER('Adubo foliar')));

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('EPSO Top')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('Mg')), '9.6%');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('EPSO Top')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('S')), '13.0%');

INSERT INTO TipoProduto (ID, Designacao) VALUES (2, 'Corretor');

INSERT INTO FatorDeProducao (ID, Designacao, Fabricante, Formato, TipoProdutoId) VALUES (6, 'Biocal CaCo3', 'Biocal', 'Granulado', (SELECT ID FROM TipoProduto WHERE UPPER(Designacao) = UPPER('Corretor')));

INSERT INTO Aplicacao (ID, Designacao) VALUES (4, 'Correção solo');

INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Biocal CaCo3')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER('Correção solo')));

INSERT INTO Elemento (ID, Designacao) VALUES (6, 'CaCO3');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Biocal CaCo3')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('CaCO3')), '88.2%');

INSERT INTO Elemento (ID, Designacao) VALUES (7, 'MgCO3');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Biocal CaCo3')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('MgCO3')), '1.9%');

INSERT INTO FatorDeProducao (ID, Designacao, Fabricante, Formato, TipoProdutoId) VALUES (7, 'Biocal Composto', 'Biocal', 'Pó', (SELECT ID FROM TipoProduto WHERE UPPER(Designacao) = UPPER('Corretor')));

INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Biocal Composto')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER('Correção solo')));

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Biocal Composto')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('CaCO3')), '71.7%');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Biocal Composto')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('MgCO3')), '14.8%');

INSERT INTO Elemento (ID, Designacao) VALUES (8, 'MgO');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Biocal Composto')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('MgO')), '7.9%');

INSERT INTO FatorDeProducao (ID, Designacao, Fabricante, Formato, TipoProdutoId) VALUES (8, 'Sonata', 'Bayer', 'Líquido', (SELECT ID FROM TipoProduto WHERE UPPER(Designacao) = UPPER('Fitofármaco')));

INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Sonata')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER('Fungicida')));

INSERT INTO Elemento (ID, Designacao) VALUES (9, 'Bacillus pumilus');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Sonata')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('Bacillus pumilus')), '97.7%');

INSERT INTO FatorDeProducao (ID, Designacao, Fabricante, Formato, TipoProdutoId) VALUES (9, 'FLiPPER ', 'Bayer', 'Emulsão de óleo em água', (SELECT ID FROM TipoProduto WHERE UPPER(Designacao) = UPPER('Fitofármaco')));

INSERT INTO Aplicacao (ID, Designacao) VALUES (5, 'Insecticida');

INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('FLiPPER ')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER('Insecticida')));

INSERT INTO Elemento (ID, Designacao) VALUES (10, 'Ácidos gordos (na forma de sais de potássio)');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('FLiPPER ')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('Ácidos gordos (na forma de sais de potássio)')), '47.8%');

INSERT INTO FatorDeProducao (ID, Designacao, Fabricante, Formato, TipoProdutoId) VALUES (10, 'Requiem Prime', 'Bayer', 'Líquido', (SELECT ID FROM TipoProduto WHERE UPPER(Designacao) = UPPER('Fitofármaco')));

INSERT INTO AplicacaoProduto (FatorDeProducaoId, AplicacaoId) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Requiem Prime')), (SELECT ID FROM Aplicacao WHERE UPPER(Designacao) = UPPER('Insecticida')));

INSERT INTO Elemento (ID, Designacao) VALUES (11, 'Terpenóides');

INSERT INTO ElementoFicha (FatorDeProducaoId, ElementoId, Quantidade) VALUES ((SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Requiem Prime')), (SELECT ID FROM Elemento WHERE UPPER(Designacao) = UPPER('Terpenóides')), '14.4%');

INSERT INTO TipoEdificio (ID, Designacao) VALUES (0, 'Parcela');

INSERT INTO Parcela (ID, Designacao, Area, QuintaID) VALUES (101, 'Campo da bouça', 1.2, 0);

INSERT INTO Parcela (ID, Designacao, Area, QuintaID) VALUES (102, 'Campo grande', 3, 0);

INSERT INTO Parcela (ID, Designacao, Area, QuintaID) VALUES (103, 'Campo do poço', 1.5, 0);

INSERT INTO Parcela (ID, Designacao, Area, QuintaID) VALUES (104, 'Lameiro da ponte', 0.8, 0);

INSERT INTO Parcela (ID, Designacao, Area, QuintaID) VALUES (105, 'Lameiro do moinho', 1.1, 0);

INSERT INTO Parcela (ID, Designacao, Area, QuintaID) VALUES (106, 'Horta nova', 0.3, 0);

INSERT INTO Parcela (ID, Designacao, Area, QuintaID) VALUES (107, 'Vinha', 2, 0);

INSERT INTO TipoEdificio (ID, Designacao) VALUES (1, 'Armazém');

INSERT INTO Edificio (ID, Designacao, Area, Unidade, TipoEdificioID, QuintaID) VALUES (201, 'Espigueiro', 600, 'm2', (SELECT ID FROM TipoEdificio WHERE UPPER(Designacao) = UPPER('Armazém')), 0);

INSERT INTO Edificio (ID, Designacao, Area, Unidade, TipoEdificioID, QuintaID) VALUES (202, 'Armazém novo', 800, 'm2', (SELECT ID FROM TipoEdificio WHERE UPPER(Designacao) = UPPER('Armazém')), 0);

INSERT INTO TipoEdificio (ID, Designacao) VALUES (2, 'Garagem');

INSERT INTO Edificio (ID, Designacao, Area, Unidade, TipoEdificioID, QuintaID) VALUES (203, 'Armazém grande', 900, 'm2', (SELECT ID FROM TipoEdificio WHERE UPPER(Designacao) = UPPER('Garagem')), 0);

INSERT INTO TipoEdificio (ID, Designacao) VALUES (3, 'Moinho');

INSERT INTO Edificio (ID, Designacao, Area, Unidade, TipoEdificioID, QuintaID) VALUES (250, 'Moinho', NULL, NULL, (SELECT ID FROM TipoEdificio WHERE UPPER(Designacao) = UPPER('Moinho')), 0);

INSERT INTO TipoEdificio (ID, Designacao) VALUES (4, 'Rega');

INSERT INTO Edificio (ID, Designacao, Area, Unidade, TipoEdificioID, QuintaID) VALUES (301, 'Tanque do campo grande', 18, 'm3', (SELECT ID FROM TipoEdificio WHERE UPPER(Designacao) = UPPER('Rega')), 0);

INSERT INTO Edificio (ID, Designacao, Area, Unidade, TipoEdificioID, QuintaID) VALUES (302, 'Poço da bouça', 35, 'm3', (SELECT ID FROM TipoEdificio WHERE UPPER(Designacao) = UPPER('Rega')), 0);

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (0, TO_DATE('10/10/2020', 'MM/DD/YYYY'), TO_DATE('03/30/2021', 'MM/DD/YYYY'), 1.1, 'ha', 101, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (1, TO_DATE('04/10/2021', 'MM/DD/YYYY'), TO_DATE('08/12/2021', 'MM/DD/YYYY'), 0.9, 'ha', 101, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho Doce Golden Bantam')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (2, TO_DATE('10/03/2021', 'MM/DD/YYYY'), TO_DATE('04/05/2022', 'MM/DD/YYYY'), 1.1, 'ha', 101, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (3, TO_DATE('04/15/2022', 'MM/DD/YYYY'), TO_DATE('08/21/2022', 'MM/DD/YYYY'), 0.9, 'ha', 101, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho Doce Golden Bantam')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (4, TO_DATE('04/05/2020', 'MM/DD/YYYY'), TO_DATE('08/20/2020', 'MM/DD/YYYY'), 1.2, 'ha', 103, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (5, TO_DATE('10/12/2020', 'MM/DD/YYYY'), TO_DATE('03/15/2021', 'MM/DD/YYYY'), 1.3, 'ha', 103, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (6, TO_DATE('04/03/2021', 'MM/DD/YYYY'), TO_DATE('08/25/2021', 'MM/DD/YYYY'), 1.2, 'ha', 103, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (7, TO_DATE('10/06/2021', 'MM/DD/YYYY'), TO_DATE('03/19/2022', 'MM/DD/YYYY'), 1.3, 'ha', 103, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (8, TO_DATE('04/08/2022', 'MM/DD/YYYY'), TO_DATE('08/18/2022', 'MM/DD/YYYY'), 1.2, 'ha', 103, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (9, TO_DATE('10/12/2022', 'MM/DD/YYYY'), TO_DATE('03/20/2023', 'MM/DD/YYYY'), 1.3, 'ha', 103, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (10, TO_DATE('10/06/2016', 'MM/DD/YYYY'), NULL, 30, 'un', 102, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (11, TO_DATE('10/10/2016', 'MM/DD/YYYY'), NULL, 20, 'un', 102, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (12, TO_DATE('03/10/2020', 'MM/DD/YYYY'), TO_DATE('05/15/2020', 'MM/DD/YYYY'), 0.15, 'ha', 106, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Scarlet Nantes')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (13, TO_DATE('06/02/2020', 'MM/DD/YYYY'), TO_DATE('09/08/2020', 'MM/DD/YYYY'), 0.1, 'ha', 106, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (14, TO_DATE('09/20/2020', 'MM/DD/YYYY'), TO_DATE('01/10/2021', 'MM/DD/YYYY'), 0.2, 'ha', 106, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo S. Cosme')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (15, TO_DATE('03/10/2021', 'MM/DD/YYYY'), TO_DATE('05/15/2021', 'MM/DD/YYYY'), 0.15, 'ha', 106, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Sugarsnax Hybrid')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (16, TO_DATE('06/02/2021', 'MM/DD/YYYY'), TO_DATE('09/08/2021', 'MM/DD/YYYY'), 0.1, 'ha', 106, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Danvers Half Long')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (17, TO_DATE('09/20/2021', 'MM/DD/YYYY'), TO_DATE('01/10/2022', 'MM/DD/YYYY'), 0.2, 'ha', 106, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo S. Cosme')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (18, TO_DATE('03/06/2022', 'MM/DD/YYYY'), TO_DATE('05/16/2022', 'MM/DD/YYYY'), 0.15, 'ha', 106, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Sugarsnax Hybrid')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (19, TO_DATE('05/30/2022', 'MM/DD/YYYY'), TO_DATE('09/05/2022', 'MM/DD/YYYY'), 0.15, 'ha', 106, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (20, TO_DATE('09/20/2022', 'MM/DD/YYYY'), TO_DATE('01/14/2023', 'MM/DD/YYYY'), 0.25, 'ha', 106, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo greleiro Senhora Conceição')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (21, TO_DATE('01/07/2017', 'MM/DD/YYYY'), NULL, 90, 'un', 104, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (22, TO_DATE('01/08/2017', 'MM/DD/YYYY'), NULL, 60, 'un', 104, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Fuji')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (23, TO_DATE('01/08/2017', 'MM/DD/YYYY'), NULL, 40, 'un', 104, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (24, TO_DATE('12/10/2018', 'MM/DD/YYYY'), NULL, 30, 'un', 104, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (25, TO_DATE('01/10/2018', 'MM/DD/YYYY'), NULL, 500, 'un', 107, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (26, TO_DATE('01/11/2018', 'MM/DD/YYYY'), NULL, 700, 'un', 107, (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal')));

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (50, TO_DATE('10/28/2023', 'MM/DD/YYYY'), NULL, 1.1, 'ha', 104, 115);

INSERT INTO Plantacao (ID, DataInicial, DataFinal, Quantidade, Unidade, ParcelaID, CulturaID) VALUES (51, TO_DATE('10/28/2023', 'MM/DD/YYYY'), NULL, 1.1, 'ha', 107, 116);

INSERT INTO TipoOperacao (ID, Designacao) VALUES (0, 'Plantação');

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (0, TO_DATE('10/06/2016', 'MM/DD/YYYY'), 30, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND DataInicial = TO_DATE('10/06/2016', 'MM/DD/YYYY') AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Plantação')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (1, TO_DATE('10/10/2016', 'MM/DD/YYYY'), 20, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND DataInicial = TO_DATE('10/10/2016', 'MM/DD/YYYY') AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Plantação')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (2, TO_DATE('01/07/2017', 'MM/DD/YYYY'), 90, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND DataInicial = TO_DATE('01/07/2017', 'MM/DD/YYYY') AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Plantação')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (3, TO_DATE('01/08/2017', 'MM/DD/YYYY'), 60, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND DataInicial = TO_DATE('01/08/2017', 'MM/DD/YYYY') AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Fuji') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Plantação')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (4, TO_DATE('01/08/2017', 'MM/DD/YYYY'), 40, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND DataInicial = TO_DATE('01/08/2017', 'MM/DD/YYYY') AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Plantação')), 104);

INSERT INTO TipoOperacao (ID, Designacao) VALUES (1, 'Rega');

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (5, TO_DATE('07/03/2017', 'MM/DD/YYYY'), 0.4, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (6, TO_DATE('07/03/2017', 'MM/DD/YYYY'), 0.9, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (7, TO_DATE('07/10/2017', 'MM/DD/YYYY'), 3, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (8, TO_DATE('08/10/2017', 'MM/DD/YYYY'), 0.4, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (9, TO_DATE('08/10/2017', 'MM/DD/YYYY'), 0.9, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (10, TO_DATE('08/10/2017', 'MM/DD/YYYY'), 3.5, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (11, TO_DATE('09/10/2017', 'MM/DD/YYYY'), 3, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO TipoOperacao (ID, Designacao) VALUES (2, 'Poda');

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (12, TO_DATE('11/04/2017', 'MM/DD/YYYY'), 30, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (13, TO_DATE('11/04/2017', 'MM/DD/YYYY'), 20, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 102);

INSERT INTO TipoOperacao (ID, Designacao) VALUES (3, 'Fertilização');

INSERT INTO ModoFertilizacao (ID, Designacao) VALUES (0, 'Solo');

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (14, TO_DATE('12/10/2017', 'MM/DD/YYYY'), 15, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 102);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (14, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Solo')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Patentkali')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (15, TO_DATE('12/10/2017', 'MM/DD/YYYY'), 10, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 102);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (15, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Solo')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Patentkali')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (16, TO_DATE('01/07/2018', 'MM/DD/YYYY'), 90, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (17, TO_DATE('01/08/2018', 'MM/DD/YYYY'), 60, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Fuji') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (18, TO_DATE('01/08/2018', 'MM/DD/YYYY'), 40, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (19, TO_DATE('01/10/2018', 'MM/DD/YYYY'), 500, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND DataInicial = TO_DATE('01/10/2018', 'MM/DD/YYYY') AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Plantação')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (20, TO_DATE('01/11/2018', 'MM/DD/YYYY'), 700, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND DataInicial = TO_DATE('01/11/2018', 'MM/DD/YYYY') AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Plantação')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (21, TO_DATE('02/06/2018', 'MM/DD/YYYY'), 10, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 104);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (21, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Solo')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('ESTA Kieserit')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (22, TO_DATE('02/06/2018', 'MM/DD/YYYY'), 6, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Fuji') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 104);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (22, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Solo')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('ESTA Kieserit')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (23, TO_DATE('02/06/2018', 'MM/DD/YYYY'), 5, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 104);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (23, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Solo')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('ESTA Kieserit')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (24, TO_DATE('07/03/2018', 'MM/DD/YYYY'), 1, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (25, TO_DATE('07/03/2018', 'MM/DD/YYYY'), 1.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (26, TO_DATE('07/10/2018', 'MM/DD/YYYY'), 3.5, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (27, TO_DATE('07/10/2018', 'MM/DD/YYYY'), 6, 'm3', 51, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (28, TO_DATE('08/10/2018', 'MM/DD/YYYY'), 1, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (29, TO_DATE('08/10/2018', 'MM/DD/YYYY'), 1.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (30, TO_DATE('08/10/2018', 'MM/DD/YYYY'), 4, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (31, TO_DATE('08/11/2018', 'MM/DD/YYYY'), 7, 'm3', 51, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (32, TO_DATE('09/02/2018', 'MM/DD/YYYY'), 4, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (33, TO_DATE('09/10/2018', 'MM/DD/YYYY'), 4, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (34, TO_DATE('11/17/2018', 'MM/DD/YYYY'), 30, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (35, TO_DATE('11/17/2018', 'MM/DD/YYYY'), 20, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (36, TO_DATE('12/10/2018', 'MM/DD/YYYY'), 30, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND DataInicial = TO_DATE('12/10/2018', 'MM/DD/YYYY') AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Plantação')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (37, TO_DATE('12/16/2018', 'MM/DD/YYYY'), 500, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (38, TO_DATE('12/18/2018', 'MM/DD/YYYY'), 700, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (39, TO_DATE('01/07/2019', 'MM/DD/YYYY'), 90, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (40, TO_DATE('01/08/2019', 'MM/DD/YYYY'), 60, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Fuji') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (41, TO_DATE('01/08/2019', 'MM/DD/YYYY'), 40, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO TipoOperacao (ID, Designacao) VALUES (4, 'Aplicação fitofármaco');

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (42, TO_DATE('01/20/2019', 'MM/DD/YYYY'), 2, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Aplicação fitofármaco')), 107);

INSERT INTO AplicacaoFitofarmaco (OperacaoID, FatorDeProducaoID) VALUES (42, (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Calda Bordalesa ASCENZA')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (43, TO_DATE('01/20/2019', 'MM/DD/YYYY'), 2.5, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Aplicação fitofármaco')), 107);

INSERT INTO AplicacaoFitofarmaco (OperacaoID, FatorDeProducaoID) VALUES (43, (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Calda Bordalesa ASCENZA')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (44, TO_DATE('02/06/2019', 'MM/DD/YYYY'), 10, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 104);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (44, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Solo')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('ESTA Kieserit')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (45, TO_DATE('02/06/2019', 'MM/DD/YYYY'), 5, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Fuji') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 104);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (45, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Solo')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('ESTA Kieserit')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (46, TO_DATE('02/06/2019', 'MM/DD/YYYY'), 7, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 104);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (46, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Solo')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('ESTA Kieserit')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (47, TO_DATE('07/03/2019', 'MM/DD/YYYY'), 1, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (48, TO_DATE('07/03/2019', 'MM/DD/YYYY'), 1.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (49, TO_DATE('07/03/2019', 'MM/DD/YYYY'), 4, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (50, TO_DATE('07/10/2019', 'MM/DD/YYYY'), 6, 'm3', 51, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (51, TO_DATE('08/10/2019', 'MM/DD/YYYY'), 1, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (52, TO_DATE('08/10/2019', 'MM/DD/YYYY'), 1.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (53, TO_DATE('08/10/2019', 'MM/DD/YYYY'), 4.5, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (54, TO_DATE('08/11/2019', 'MM/DD/YYYY'), 7, 'm3', 51, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (55, TO_DATE('11/15/2019', 'MM/DD/YYYY'), 30, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (56, TO_DATE('11/15/2019', 'MM/DD/YYYY'), 20, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (57, TO_DATE('12/16/2019', 'MM/DD/YYYY'), 500, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (58, TO_DATE('12/18/2019', 'MM/DD/YYYY'), 700, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (59, TO_DATE('01/20/2020', 'MM/DD/YYYY'), 2, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Aplicação fitofármaco')), 107);

INSERT INTO AplicacaoFitofarmaco (OperacaoID, FatorDeProducaoID) VALUES (59, (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Calda Bordalesa ASCENZA')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (60, TO_DATE('01/20/2020', 'MM/DD/YYYY'), 2.5, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Aplicação fitofármaco')), 107);

INSERT INTO AplicacaoFitofarmaco (OperacaoID, FatorDeProducaoID) VALUES (60, (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Calda Bordalesa ASCENZA')));

INSERT INTO TipoOperacao (ID, Designacao) VALUES (5, 'Sementeira');

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (61, TO_DATE('03/12/2020', 'MM/DD/YYYY'), 0.9, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Scarlet Nantes') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (62, TO_DATE('03/30/2020', 'MM/DD/YYYY'), 600, 'kg', 51, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 103);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (62, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Solo')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Biocal Composto')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (63, TO_DATE('04/05/2020', 'MM/DD/YYYY'), 1.2, 'ha', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 103);

INSERT INTO TipoOperacao (ID, Designacao) VALUES (6, 'Colheita');

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (64, TO_DATE('05/05/2020', 'MM/DD/YYYY'), 2200, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Scarlet Nantes') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (65, TO_DATE('05/15/2020', 'MM/DD/YYYY'), 1400, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Scarlet Nantes') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (66, TO_DATE('06/02/2020', 'MM/DD/YYYY'), 0.6, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (67, TO_DATE('07/03/2020', 'MM/DD/YYYY'), 1, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (68, TO_DATE('07/03/2020', 'MM/DD/YYYY'), 1.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (69, TO_DATE('07/10/2020', 'MM/DD/YYYY'), 6, 'm3', 51, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (70, TO_DATE('07/12/2020', 'MM/DD/YYYY'), 15, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (71, TO_DATE('07/15/2020', 'MM/DD/YYYY'), 2.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (72, TO_DATE('07/28/2020', 'MM/DD/YYYY'), 15, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (73, TO_DATE('08/10/2020', 'MM/DD/YYYY'), 1, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (74, TO_DATE('08/10/2020', 'MM/DD/YYYY'), 1.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (75, TO_DATE('08/10/2020', 'MM/DD/YYYY'), 15, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (76, TO_DATE('08/11/2020', 'MM/DD/YYYY'), 7, 'm3', 51, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (77, TO_DATE('08/12/2020', 'MM/DD/YYYY'), 3.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (78, TO_DATE('08/20/2020', 'MM/DD/YYYY'), 3300, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (79, TO_DATE('08/28/2020', 'MM/DD/YYYY'), 600, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (80, TO_DATE('09/07/2020', 'MM/DD/YYYY'), 1800, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (81, TO_DATE('09/20/2020', 'MM/DD/YYYY'), 0.6, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo S. Cosme') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (82, TO_DATE('10/10/2020', 'MM/DD/YYYY'), 36, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 101 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 101);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (83, TO_DATE('10/12/2020', 'MM/DD/YYYY'), 1.3, 'ha', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (84, TO_DATE('11/10/2020', 'MM/DD/YYYY'), 30, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (85, TO_DATE('11/10/2020', 'MM/DD/YYYY'), 20, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (86, TO_DATE('11/15/2020', 'MM/DD/YYYY'), 600, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo S. Cosme') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (87, TO_DATE('12/05/2020', 'MM/DD/YYYY'), 70, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (88, TO_DATE('12/05/2020', 'MM/DD/YYYY'), 50, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (89, TO_DATE('12/10/2020', 'MM/DD/YYYY'), 10, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 102);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (89, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Solo')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Patentkali')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (90, TO_DATE('12/10/2020', 'MM/DD/YYYY'), 7, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 102);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (90, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Solo')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Patentkali')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (91, TO_DATE('12/15/2020', 'MM/DD/YYYY'), 40, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (92, TO_DATE('12/15/2020', 'MM/DD/YYYY'), 60, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Fuji') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (93, TO_DATE('12/16/2020', 'MM/DD/YYYY'), 500, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (94, TO_DATE('12/18/2020', 'MM/DD/YYYY'), 2500, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo S. Cosme') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (95, TO_DATE('12/18/2020', 'MM/DD/YYYY'), 700, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (96, TO_DATE('01/04/2021', 'MM/DD/YYYY'), 2900, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo S. Cosme') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (97, TO_DATE('01/20/2021', 'MM/DD/YYYY'), 2, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Aplicação fitofármaco')), 107);

INSERT INTO AplicacaoFitofarmaco (OperacaoID, FatorDeProducaoID) VALUES (97, (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Calda Bordalesa ASCENZA')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (98, TO_DATE('01/20/2021', 'MM/DD/YYYY'), 2.5, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Aplicação fitofármaco')), 107);

INSERT INTO AplicacaoFitofarmaco (OperacaoID, FatorDeProducaoID) VALUES (98, (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Calda Bordalesa ASCENZA')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (99, TO_DATE('03/10/2021', 'MM/DD/YYYY'), 0.9, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Sugarsnax Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 106);

INSERT INTO TipoOperacao (ID, Designacao) VALUES (7, 'Incorporação no solo');

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (100, TO_DATE('03/14/2021', 'MM/DD/YYYY'), 1.3, 'ha', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Incorporação no solo')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (101, TO_DATE('03/30/2021', 'MM/DD/YYYY'), 1.3, 'ha', (SELECT ID FROM Plantacao WHERE ParcelaID = 101 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Incorporação no solo')), 101);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (102, TO_DATE('04/03/2021', 'MM/DD/YYYY'), 1.2, 'ha', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (103, TO_DATE('04/15/2021', 'MM/DD/YYYY'), 30, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 101 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho Doce Golden Bantam') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 101);

INSERT INTO ModoFertilizacao (ID, Designacao) VALUES (1, 'Foliar');

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (104, TO_DATE('05/02/2021', 'MM/DD/YYYY'), 10, 'kg', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 104);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (104, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Foliar')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('EPSO Microtop')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (105, TO_DATE('05/05/2021', 'MM/DD/YYYY'), 2200, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Sugarsnax Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (106, TO_DATE('05/15/2021', 'MM/DD/YYYY'), 1400, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Sugarsnax Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (107, TO_DATE('06/02/2021', 'MM/DD/YYYY'), 0.6, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Danvers Half Long') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (108, TO_DATE('06/20/2021', 'MM/DD/YYYY'), 3, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Danvers Half Long') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (109, TO_DATE('07/03/2021', 'MM/DD/YYYY'), 0.8, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (110, TO_DATE('07/03/2021', 'MM/DD/YYYY'), 1.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (111, TO_DATE('07/05/2021', 'MM/DD/YYYY'), 5, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (112, TO_DATE('07/07/2021', 'MM/DD/YYYY'), 3, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Danvers Half Long') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (113, TO_DATE('07/10/2021', 'MM/DD/YYYY'), 7, 'm3', 51, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (114, TO_DATE('07/12/2021', 'MM/DD/YYYY'), 15, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (115, TO_DATE('07/15/2021', 'MM/DD/YYYY'), 300, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (116, TO_DATE('07/20/2021', 'MM/DD/YYYY'), 400, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (117, TO_DATE('07/24/2021', 'MM/DD/YYYY'), 15, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (118, TO_DATE('07/30/2021', 'MM/DD/YYYY'), 5.5, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (119, TO_DATE('07/30/2021', 'MM/DD/YYYY'), 3.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Danvers Half Long') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (120, TO_DATE('08/07/2021', 'MM/DD/YYYY'), 15, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (121, TO_DATE('08/10/2021', 'MM/DD/YYYY'), 0.8, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (122, TO_DATE('08/10/2021', 'MM/DD/YYYY'), 1.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (123, TO_DATE('08/12/2021', 'MM/DD/YYYY'), 3300, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 101 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho Doce Golden Bantam') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 101);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (124, TO_DATE('08/17/2021', 'MM/DD/YYYY'), 3, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Danvers Half Long') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (125, TO_DATE('08/24/2021', 'MM/DD/YYYY'), 900, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (126, TO_DATE('08/25/2021', 'MM/DD/YYYY'), 3300, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (127, TO_DATE('08/28/2021', 'MM/DD/YYYY'), 600, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Danvers Half Long') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (128, TO_DATE('09/05/2021', 'MM/DD/YYYY'), 800, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (129, TO_DATE('09/07/2021', 'MM/DD/YYYY'), 1800, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Danvers Half Long') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (130, TO_DATE('09/12/2021', 'MM/DD/YYYY'), 800, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (131, TO_DATE('09/20/2021', 'MM/DD/YYYY'), 0.6, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo S. Cosme') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (132, TO_DATE('09/23/2021', 'MM/DD/YYYY'), 1200, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (133, TO_DATE('10/03/2021', 'MM/DD/YYYY'), 36, NULL, (SELECT ID FROM Plantacao WHERE ParcelaID = 101 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 101);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (134, TO_DATE('10/06/2021', 'MM/DD/YYYY'), 1.3, 'ha', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (135, TO_DATE('10/12/2021', 'MM/DD/YYYY'), 950, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Fuji') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (136, TO_DATE('11/03/2021', 'MM/DD/YYYY'), 750, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Fuji') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (137, TO_DATE('11/10/2021', 'MM/DD/YYYY'), 210, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (138, TO_DATE('11/10/2021', 'MM/DD/YYYY'), 120, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (139, TO_DATE('11/15/2021', 'MM/DD/YYYY'), 600, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo S. Cosme') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (140, TO_DATE('11/17/2021', 'MM/DD/YYYY'), 30, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (141, TO_DATE('11/17/2021', 'MM/DD/YYYY'), 20, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (142, TO_DATE('11/28/2021', 'MM/DD/YYYY'), 70, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (143, TO_DATE('12/03/2021', 'MM/DD/YYYY'), 90, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (144, TO_DATE('12/16/2021', 'MM/DD/YYYY'), 500, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (145, TO_DATE('12/18/2021', 'MM/DD/YYYY'), 60, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Fuji') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (146, TO_DATE('12/18/2021', 'MM/DD/YYYY'), 2500, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo S. Cosme') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (147, TO_DATE('12/18/2021', 'MM/DD/YYYY'), 700, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (148, TO_DATE('01/04/2022', 'MM/DD/YYYY'), 2900, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo S. Cosme') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (149, TO_DATE('01/20/2022', 'MM/DD/YYYY'), 3, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Aplicação fitofármaco')), 107);

INSERT INTO AplicacaoFitofarmaco (OperacaoID, FatorDeProducaoID) VALUES (149, (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Calda Bordalesa ASCENZA')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (150, TO_DATE('01/20/2022', 'MM/DD/YYYY'), 3.5, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Aplicação fitofármaco')), 107);

INSERT INTO AplicacaoFitofarmaco (OperacaoID, FatorDeProducaoID) VALUES (150, (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Calda Bordalesa ASCENZA')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (151, TO_DATE('03/06/2022', 'MM/DD/YYYY'), 0.9, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Sugarsnax Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (152, TO_DATE('03/19/2022', 'MM/DD/YYYY'), 1.3, 'ha', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Incorporação no solo')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (153, TO_DATE('04/05/2022', 'MM/DD/YYYY'), 1.3, 'ha', (SELECT ID FROM Plantacao WHERE ParcelaID = 101 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Incorporação no solo')), 101);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (154, TO_DATE('04/08/2022', 'MM/DD/YYYY'), 1.2, 'ha', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (155, TO_DATE('04/15/2022', 'MM/DD/YYYY'), 30, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 101 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho Doce Golden Bantam') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 101);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (156, TO_DATE('05/05/2022', 'MM/DD/YYYY'), 2250, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Sugarsnax Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (157, TO_DATE('05/13/2022', 'MM/DD/YYYY'), 10, 'kg', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 104);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (157, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Foliar')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('EPSO Microtop')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (158, TO_DATE('05/15/2022', 'MM/DD/YYYY'), 1300, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Sugarsnax Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (159, TO_DATE('05/30/2022', 'MM/DD/YYYY'), 0.6, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (160, TO_DATE('06/05/2022', 'MM/DD/YYYY'), 3, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (161, TO_DATE('06/30/2022', 'MM/DD/YYYY'), 3, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (162, TO_DATE('07/02/2022', 'MM/DD/YYYY'), 5.5, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (163, TO_DATE('07/03/2022', 'MM/DD/YYYY'), 0.8, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (164, TO_DATE('07/03/2022', 'MM/DD/YYYY'), 1.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (165, TO_DATE('07/10/2022', 'MM/DD/YYYY'), 5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (166, TO_DATE('07/12/2022', 'MM/DD/YYYY'), 15, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (167, TO_DATE('07/15/2022', 'MM/DD/YYYY'), 3, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (168, TO_DATE('07/15/2022', 'MM/DD/YYYY'), 600, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (169, TO_DATE('07/20/2022', 'MM/DD/YYYY'), 500, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (170, TO_DATE('07/24/2022', 'MM/DD/YYYY'), 15, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (171, TO_DATE('07/30/2022', 'MM/DD/YYYY'), 5, 'm3', 50, 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (172, TO_DATE('07/30/2022', 'MM/DD/YYYY'), 2.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (173, TO_DATE('08/07/2022', 'MM/DD/YYYY'), 15, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (174, TO_DATE('08/10/2022', 'MM/DD/YYYY'), 0.8, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (175, TO_DATE('08/10/2022', 'MM/DD/YYYY'), 1.5, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (176, TO_DATE('08/12/2022', 'MM/DD/YYYY'), 1200, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (177, TO_DATE('08/12/2022', 'MM/DD/YYYY'), 600, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (178, TO_DATE('08/17/2022', 'MM/DD/YYYY'), 3500, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 101 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho Doce Golden Bantam') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 101);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (179, TO_DATE('08/17/2022', 'MM/DD/YYYY'), 3, 'm3', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Rega')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (180, TO_DATE('08/18/2022', 'MM/DD/YYYY'), 3300, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Milho MAS 24.C') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (181, TO_DATE('08/20/2022', 'MM/DD/YYYY'), 950, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (182, TO_DATE('08/24/2022', 'MM/DD/YYYY'), 650, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (183, TO_DATE('09/05/2022', 'MM/DD/YYYY'), 1900, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Cenoura Nelson Hybrid') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (184, TO_DATE('09/07/2022', 'MM/DD/YYYY'), 830, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (185, TO_DATE('09/11/2022', 'MM/DD/YYYY'), 750, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (186, TO_DATE('09/20/2022', 'MM/DD/YYYY'), 1150, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (187, TO_DATE('09/20/2022', 'MM/DD/YYYY'), 0.6, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo greleiro Senhora Conceição') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (188, TO_DATE('10/12/2022', 'MM/DD/YYYY'), 1.3, 'ha', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Sementeira')), 103);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (189, TO_DATE('10/17/2022', 'MM/DD/YYYY'), 850, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Fuji') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (190, TO_DATE('11/06/2022', 'MM/DD/YYYY'), 900, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Fuji') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (191, TO_DATE('11/10/2022', 'MM/DD/YYYY'), 30, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (192, TO_DATE('11/10/2022', 'MM/DD/YYYY'), 20, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (193, TO_DATE('11/12/2022', 'MM/DD/YYYY'), 300, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (194, TO_DATE('11/12/2022', 'MM/DD/YYYY'), 200, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 102);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (195, TO_DATE('11/15/2022', 'MM/DD/YYYY'), 50, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo greleiro Senhora Conceição') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (196, TO_DATE('12/04/2022', 'MM/DD/YYYY'), 70, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Royal Gala') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (197, TO_DATE('12/07/2022', 'MM/DD/YYYY'), 90, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Jonagored') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (198, TO_DATE('12/11/2022', 'MM/DD/YYYY'), 15, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Galega') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 102);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (198, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Solo')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Patentkali')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (199, TO_DATE('12/11/2022', 'MM/DD/YYYY'), 10, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 102 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Oliveira Picual') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Fertilização')), 102);

INSERT INTO Fertilizacao (OperacaoID, ModoFertilizacaoID, FatorDeProducaoID) VALUES (199, (SELECT ID FROM ModoFertilizacao WHERE UPPER(Designacao) = UPPER('Solo')), (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Patentkali')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (200, TO_DATE('12/16/2022', 'MM/DD/YYYY'), 500, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (201, TO_DATE('12/18/2022', 'MM/DD/YYYY'), 200, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo greleiro Senhora Conceição') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (202, TO_DATE('12/18/2022', 'MM/DD/YYYY'), 700, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 107);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (203, TO_DATE('01/12/2023', 'MM/DD/YYYY'), 60, 'un', (SELECT ID FROM Plantacao WHERE ParcelaID = 104 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Macieira Fuji') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Poda')), 104);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (204, TO_DATE('01/14/2023', 'MM/DD/YYYY'), 250, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 106 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Nabo greleiro Senhora Conceição') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Colheita')), 106);

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (205, TO_DATE('01/20/2023', 'MM/DD/YYYY'), 4, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Dona Maria') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Aplicação fitofármaco')), 107);

INSERT INTO AplicacaoFitofarmaco (OperacaoID, FatorDeProducaoID) VALUES (205, (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Calda Bordalesa ASCENZA')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (206, TO_DATE('01/20/2023', 'MM/DD/YYYY'), 5, 'kg', (SELECT ID FROM Plantacao WHERE ParcelaID = 107 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Videira Cardinal') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Aplicação fitofármaco')), 107);

INSERT INTO AplicacaoFitofarmaco (OperacaoID, FatorDeProducaoID) VALUES (206, (SELECT ID FROM FatorDeProducao WHERE UPPER(Designacao) = UPPER('Calda Bordalesa ASCENZA')));

INSERT INTO Operacao (ID, DataOperacao, Quantidade, Unidade, PlantacaoID, CadernoDeCampoID, TipoOperacaoID, ParcelaID) VALUES (207, TO_DATE('03/20/2023', 'MM/DD/YYYY'), 1.3, 'ha', (SELECT ID FROM Plantacao WHERE ParcelaID = 103 AND CulturaID = (SELECT C.ID FROM Cultura C INNER JOIN NomeEspecie N ON C.NomeEspecieID = N.ID WHERE UPPER(N.NomeComum || ' ' || C.Variedade) = UPPER('Tremoço Amarelo') AND C.ID = Plantacao.CulturaID) AND ROWNUM = 1), 0, (SELECT ID FROM TipoOperacao WHERE UPPER(Designacao) = UPPER('Incorporação no solo')), 103);

--US005
CREATE OR REPLACE PROCEDURE getCollectedProductsByParcela(
    p_Parcela VARCHAR2,
    p_StartDate DATE,
    p_EndDate DATE
)
IS
BEGIN
    FOR rec IN (
        SELECT NP.NOMECOMUM || ' ' || C.VARIEDADE AS PRODUTO, MAX(O.Unidade) AS Unidade, SUM(O.Quantidade) AS Quantidade
        FROM Parcela P
        INNER JOIN Plantacao PL ON P.ID = PL.ParcelaID                  -- obter Plantacoes
        INNER JOIN Cultura C ON PL.CulturaID = C.ID                     -- obter Culturas                   
        INNER JOIN NomeEspecie NP ON C.NomeEspecieID = NP.ID            -- obter NomeEspecie
        INNER JOIN Operacao O ON PL.ID = O.PlantacaoID                  -- obter Operacoes
        INNER JOIN TipoOperacao TOp ON O.TipoOperacaoID = TOp.ID        -- obter TipoOperacao
        WHERE TOp.Designacao = 'Colheita'                       -- obter Colheitas
        AND UPPER(P.Designacao) = UPPER(p_Parcela)              -- obter Parcela
        AND O.DataOperacao BETWEEN p_StartDate AND p_EndDate    -- obter Operacoes entre datas
        GROUP BY NP.NOMECOMUM, C.VARIEDADE                      -- agrupar por NomeEspecie e Variedade
    )
    LOOP
        DBMS_OUTPUT.PUT_LINE(' Produto: ' || rec.Produto || ';   Quantidade: ' || rec.Quantidade || ' ' || rec.Unidade);
    END LOOP;
END;
/
-- US006
CREATE OR REPLACE PROCEDURE getNumberOfFactorsByType(
    p_Parcela VARCHAR2,
    p_StartDate DATE,
    p_EndDate DATE
)
IS
    v_ParcelaID NUMBER;
    v_ParcelaName VARCHAR2(100);
BEGIN
    SELECT ID, Designacao
    INTO v_ParcelaID, v_ParcelaName
    FROM Parcela
    WHERE UPPER(Designacao) = UPPER(p_Parcela);

    DBMS_OUTPUT.PUT_LINE('ID da Parcela: ' || v_ParcelaID);
    DBMS_OUTPUT.PUT_LINE('Nome da Parcela: ' || v_ParcelaName);

    FOR rec IN (
        SELECT TP.Designacao AS Tipo, COUNT(F.OperacaoID) AS numeroOperacoes
        FROM Operacao OP
        INNER JOIN Fertilizacao F ON OP.ID = F.OperacaoID             -- obter Fertilizações
        INNER JOIN FatorDeProducao FP ON F.FatorDeProducaoID = FP.ID  -- obter Fatores de Produção
        INNER JOIN TipoProduto TP ON FP.TipoProdutoID = TP.ID         -- obter Tipo de Produto
        WHERE OP.ParcelaID = v_ParcelaID
        AND OP.DataOperacao BETWEEN p_StartDate AND p_EndDate
        GROUP BY TP.Designacao
        UNION ALL
        SELECT TP.Designacao AS Tipo, COUNT(AF.OperacaoID) AS numeroOperacoes
        FROM Operacao OP
        INNER JOIN AplicacaoFitofarmaco AF ON OP.ID = AF.OperacaoID   -- obter Aplicações Fitofarmacos
        INNER JOIN FatorDeProducao FP ON AF.FatorDeProducaoID = FP.ID -- obter Fatores de Produção
        INNER JOIN TipoProduto TP ON FP.TipoProdutoID = TP.ID         -- obter Tipo de Produto
        WHERE OP.ParcelaID = v_ParcelaID
        AND OP.DataOperacao BETWEEN p_StartDate AND p_EndDate
        GROUP BY TP.Designacao
    ) LOOP
        DBMS_OUTPUT.PUT_LINE('Tipo de Fator De Produção: ' || rec.Tipo || '; Número de Operações: ' || rec.numeroOperacoes);
    END LOOP;
END;
/
-- US006
CREATE OR REPLACE PROCEDURE getNumberOfFactorsByTypeV2(
    p_Parcela VARCHAR2,
    p_StartDate DATE,
    p_EndDate DATE
)
IS
    v_ParcelaID NUMBER;
    v_ParcelaName VARCHAR2(100);
BEGIN
    -- Retrieve the Parcela ID and Name
    SELECT ID, Designacao
    INTO v_ParcelaID, v_ParcelaName
    FROM Parcela
    WHERE UPPER(Designacao) = UPPER(p_Parcela);

    DBMS_OUTPUT.PUT_LINE('ID da Parcela: ' || v_ParcelaID);
    DBMS_OUTPUT.PUT_LINE('Nome da Parcela: ' || v_ParcelaName);

    FOR rec IN (
        SELECT TP.Designacao AS Tipo, COUNT(DISTINCT FP.Designacao) AS numeroOperacoes
        FROM Operacao OP
        INNER JOIN Fertilizacao F ON OP.ID = F.OperacaoID              -- obter Fertilizações
        INNER JOIN FatorDeProducao FP ON F.FatorDeProducaoID = FP.ID   -- obter Fatores de Produção
        INNER JOIN TipoProduto TP ON FP.TipoProdutoID = TP.ID          -- obter Tipo de Produto
        WHERE OP.ParcelaID = v_ParcelaID
        AND OP.DataOperacao BETWEEN p_StartDate AND p_EndDate
        GROUP BY TP.Designacao
        UNION ALL
        SELECT TP.Designacao AS Tipo, COUNT(DISTINCT FP.Designacao) AS numeroOperacoes
        FROM Operacao OP
        INNER JOIN AplicacaoFitofarmaco AF ON OP.ID = AF.OperacaoID    -- obter Aplicações Fitofarmacos
        INNER JOIN FatorDeProducao FP ON AF.FatorDeProducaoID = FP.ID  -- obter Fatores de Produção
        INNER JOIN TipoProduto TP ON FP.TipoProdutoID = TP.ID          -- obter Tipo de Produto
        WHERE OP.ParcelaID = v_ParcelaID
        AND OP.DataOperacao BETWEEN p_StartDate AND p_EndDate
        GROUP BY TP.Designacao
    ) LOOP
        DBMS_OUTPUT.PUT_LINE('Tipo de Fator De Produção: ' || rec.Tipo || '; Número de Fatores de Produção: ' || rec.numeroOperacoes);
    END LOOP;
END;
/

--US007
CREATE OR REPLACE PROCEDURE GetNumberOfOperationByType( 
    p_Parcela VARCHAR2, 
    p_StartDate DATE, 
    p_EndDate DATE 
) 
IS 
BEGIN 
    FOR rec IN ( 
        SELECT TOp.Designacao AS Tipo, COUNT(OP.ID) AS numeroOperacoes 
        FROM Parcela P 
        INNER JOIN Operacao OP ON P.ID = OP.ParcelaID                   -- Obter a operação da parcela 
        INNER JOIN TipoOperacao TOp ON OP.TipoOperacaoID = TOp.ID       -- Obter o tipo de operação 
        WHERE P.Designacao = p_Parcela                                    -- Selecionar a parcela passada como parâmetro 
        AND OP.DataOperacao BETWEEN p_StartDate AND p_EndDate            -- Selecionar o intervalo de datas passado como parâmetro 
        GROUP BY TOp.Designacao 
    ) LOOP 
        DBMS_OUTPUT.PUT_LINE('Tipo de Operação: ' || rec.Tipo || '; Número de Operações: ' || rec.numeroOperacoes); 
    END LOOP; 
END; 
/

-- US008
CREATE OR REPLACE PROCEDURE GetFactorWithMostAplications(
    p_StartDate DATE,
    p_EndDate DATE
)
IS
BEGIN
FOR rec IN (
    SELECT FatorDeProducao, TotalDeUsos
	FROM (
    	SELECT FatorDeProducao, TotalDeUsos
    	FROM (
        	SELECT FP.Designacao AS FatorDeProducao, count(AF.FatorDeProducaoID) AS TotalDeUsos
        	FROM FatorDeProducao FP
        	INNER JOIN AplicacaoFitofarmaco AF ON FP.ID = AF.FatorDeProducaoID
        	INNER JOIN Operacao OP ON AF.OperacaoID = OP.ID
        	WHERE OP.DataOperacao BETWEEN p_StartDate AND p_EndDate
    		GROUP BY FP.Designacao
    		UNION ALL
       		SELECT FP.Designacao AS FatorDeProducao, count(F.FatorDeProducaoID) AS TotalDeUsos
      		FROM FatorDeProducao FP
  			INNER JOIN Fertilizacao F ON FP.ID = F.FatorDeProducaoID
       		INNER JOIN Operacao OP ON F.OperacaoID = OP.ID
       		WHERE OP.DataOperacao BETWEEN p_StartDate AND p_EndDate
       		GROUP BY fP.Designacao
   		) t
   		ORDER BY TotalDeUsos DESC
	)
	WHERE ROWNUM = 1
    ) LOOP
        DBMS_OUTPUT.PUT_LINE('Fator de produção: ' || rec.FatorDeProducao || '; Total de aplicações: ' || rec.TotalDeUsos);
END LOOP;
END;
/

-- US009
CREATE OR REPLACE PROCEDURE getNumberOfAplicationsByFarm(
    p_FarmDesignation VARCHAR2,
    p_StartDate DATE,
    p_EndDate DATE
)
IS
    v_QuintaID NUMBER;
    v_QuintaDesignacao VARCHAR2(100);
BEGIN
    -- Retrieve the QuintaID and Designacao
    SELECT Q.ID, Q.Designacao
    INTO v_QuintaID, v_QuintaDesignacao
    FROM Quinta Q
    WHERE Q.Designacao = p_FarmDesignation;

    DBMS_OUTPUT.PUT_LINE('Quinta ID: ' || v_QuintaID);
    DBMS_OUTPUT.PUT_LINE('Nome da Quinta: ' || v_QuintaDesignacao);

    FOR rec IN (
        SELECT TP.Designacao AS Tipo, COUNT(DISTINCT AP.FATORDEPRODUCAOID) AS numeroOperacoes
        FROM Quinta Q
        INNER JOIN CadernoDeCampo CC ON Q.ID = CC.QuintaID         	          -- obter Caderno de campo
    	  INNER JOIN Operacao O ON CC.ID = O.CadernoDeCampoID		                -- obter Operações
        INNER JOIN FERTILIZACAO F ON O.ID = F.OPERACAOID                      -- obter Fertilizações
        INNER JOIN FATORDEPRODUCAO FDP ON F.FATORDEPRODUCAOID = FDP.ID        -- obter Fatores de Produção
        INNER JOIN TIPOPRODUTO TP ON FDP.TIPOPRODUTOID = TP.ID                -- obter Tipo de Produto
        INNER JOIN APLICACAOPRODUTO AP ON FDP.ID = AP.FATORDEPRODUCAOID       -- obter Aplicações de Produtos
        WHERE Q.Designacao = p_FarmDesignation                          -- Selecionar a quinta passada como parâmetro
        AND O.DataOperacao BETWEEN p_StartDate AND p_EndDate            -- Selecionar o intervalo de datas passado como parâmetro
        GROUP BY TP.Designacao                                          -- Agrupar por Tipo de Produto
        UNION ALL
        SELECT TP.Designacao  AS Tipo, COUNT(DISTINCT AP.FATORDEPRODUCAOID) AS numeroOperacoes
        FROM Quinta Q
    	  INNER JOIN CadernoDeCampo CC ON Q.ID = CC.QuintaID         	          -- obter Caderno de campo
    	  INNER JOIN Operacao O ON CC.ID = O.CadernoDeCampoID		                -- obter Operações
        INNER JOIN APLICACAOFITOFARMACO AF ON O.ID = AF.OPERACAOID            -- obter Aplicações Fitofarmacos
        INNER JOIN FATORDEPRODUCAO FDP ON AF.FATORDEPRODUCAOID = FDP.ID       -- obter Fatores de Produção
        INNER JOIN TIPOPRODUTO TP ON FDP.TIPOPRODUTOID = TP.ID                -- obter Tipo de Produto
        INNER JOIN APLICACAOPRODUTO AP ON FDP.ID = AP.FATORDEPRODUCAOID       -- obter Aplicações de Produtos
        WHERE Q.Designacao = p_FarmDesignation                          -- Selecionar a quinta passada como parâmetro
        AND O.DataOperacao BETWEEN p_StartDate AND p_EndDate            -- Selecionar o intervalo de datas passado como parâmetro
        GROUP BY TP.Designacao                                          -- Agrupar por Tipo de Produto
    ) LOOP
        DBMS_OUTPUT.PUT_LINE('Tipo de Fator De Produção: ' || rec.Tipo || '; Número de Operações: ' || rec.numeroOperacoes); 
    END LOOP;
END;
/

--US010
SELECT P.Designacao AS Parcela, COUNT(O.ID) AS NUMERO_DE_OPERACOES 
FROM Parcela P 
INNER JOIN Operacao O ON P.ID = O.ParcelaID				-- Obter a operação da parcela 
INNER JOIN TipoOperacao TOp ON O.TipoOperacaoID = TOp.ID		-- Obter o tipo de operação 
WHERE TOp.Designacao = 'Rega' 
GROUP BY P.Designacao 
HAVING  COUNT(O.ID) = (SELECT MAX(COUNT(O.ID)) FROM Parcela P 
                        INNER JOIN Operacao O ON P.ID = O.ParcelaID 
                        INNER JOIN TipoOperacao TOp ON O.TipoOperacaoID = TOp.ID 
                        WHERE TOp.Designacao = 'Rega' 
    					GROUP BY P.Designacao);