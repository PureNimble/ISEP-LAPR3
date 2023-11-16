INSERT INTO Aplicacao
  (ID, 
  Designacao) 
VALUES 
  (?, 
  ?);
INSERT INTO AplicacaoProduto
  (FatorProducaoID, 
  AplicacaoID) 
VALUES 
  (?, 
  ?);
INSERT INTO Armazem
  (EspacoID, 
  Capacidade) 
VALUES 
  (?, 
  ?);
INSERT INTO CadernoCampo
  (ID, 
  QuintaID) 
VALUES 
  (?, 
  ?);
INSERT INTO Cultura
  (ID, 
  Poda, 
  Floracao, 
  Colheita, 
  Sementeira, 
  NomeEspecieID, 
  TipoCulturaID, 
  VariedadeID, 
  ProdutoID) 
VALUES 
  (?, 
  ?, 
  ?, 
  ?, 
  ?, 
  ?, 
  ?, 
  ?, 
  ?);
INSERT INTO Elemento
  (ID, 
  Designacao) 
VALUES 
  (?, 
  ?);
INSERT INTO ElementoFicha
  (FatorProducaoID, 
  ElementoID, 
  Quantidade) 
VALUES 
  (?, 
  ?, 
  ?);
INSERT INTO Espaco
  (ID, 
  Designacao, 
  Area, 
  Unidade, 
  QuintaID) 
VALUES 
  (?, 
  ?, 
  ?, 
  ?, 
  ?);
INSERT INTO Estabulo
  (EspacoID) 
VALUES 
  (?);
INSERT INTO EstacaoMeteorologica
  (ID, 
  QuintaID) 
VALUES 
  (?, 
  ?);
INSERT INTO FatorProducao
  (ID, 
  Designacao, 
  Fabricante, 
  Formato, 
  TipoProdutoID) 
VALUES 
  (?, 
  ?, 
  ?, 
  ?, 
  ?);
INSERT INTO Fertilizacao
  (OperacaoID, 
  ModoFertilizacaoID) 
VALUES 
  (?, 
  ?);
INSERT INTO Garagem
  (EspacoID) 
VALUES 
  (?);
INSERT INTO Hora
  (ID, 
  HoraInicial) 
VALUES 
  (?, 
  ?);
INSERT INTO ModoFertilizacao
  (ID, 
  Designacao) 
VALUES 
  (?, 
  ?);
INSERT INTO NomeEspecie
  (ID, 
  NomeComum, 
  Especie) 
VALUES 
  (?, 
  ?, 
  ?);
INSERT INTO Operacao
  (ID, 
  DataOperacao, 
  Quantidade, 
  Unidade, 
  TipoOperacaoID, 
  CadernoCampoID) 
VALUES 
  (?, 
  ?, 
  ?, 
  ?, 
  ?, 
  ?);
INSERT INTO OperacaoFator
  (OperacaoID, 
  FatorProducaoID) 
VALUES 
  (?, 
  ?);
INSERT INTO OperacaoPlantacao
  (OperacaoID, 
  PlantacaoID) 
VALUES 
  (?, 
  ?);
INSERT INTO OperacaoSetor
  (OperacaoID, 
  Hora, 
  SetorID) 
VALUES 
  (?, 
  ?, 
  ?);
INSERT INTO Parcela
  (EspacoID) 
VALUES 
  (?);
INSERT INTO PlanoHora
  (PlanoRegaAnoInsercao, 
  HoraID) 
VALUES 
  (?, 
  ?);
INSERT INTO PlanoRega
  (AnoInsercao, 
  SistemaRegaEspacoID) 
VALUES 
  (?, 
  ?);
INSERT INTO PlanoSetor
  (PlanoRegaAnoInsercao, 
  SetorID, 
  Duracao, 
  Dispercao, 
  Periodicidade) 
VALUES 
  (?, 
  ?, 
  ?, 
  ?, 
  ?);
INSERT INTO Plantacao
  (ID, 
  DataInicial, 
  DataFinal, 
  Quantidade, 
  Unidade, 
  EstadoFenologico, 
  CulturaID, 
  ParcelaEspacoID) 
VALUES 
  (?, 
  ?, 
  ?, 
  ?, 
  ?, 
  ?, 
  ?, 
  ?);
INSERT INTO PlantacaoSetor
  (PlantacaoID, 
  SetorID) 
VALUES 
  (?, 
  ?);
INSERT INTO Produto
  (ID, 
  Designacao, 
  VariedadeID) 
VALUES 
  (?, 
  ?, 
  ?);
INSERT INTO ProdutoArmazem
  (ArmazemEspacoID, 
  ProdutoID, 
  Quantidade) 
VALUES 
  (?, 
  ?, 
  ?);
INSERT INTO Quinta
  (ID, 
  Designacao) 
VALUES 
  (?, 
  ?);
INSERT INTO RegistoSensor
  (ID, 
  Valor, 
  DataRegisto, 
  SensorID, 
  CadernoCampoID) 
VALUES 
  (?, 
  ?, 
  ?, 
  ?, 
  ?);
INSERT INTO Sensor
  (ID, 
  TipoSensorID) 
VALUES 
  (?, 
  ?);
INSERT INTO SensorEstacao
  (SensorID, 
  EstacaoMeteorologicaID) 
VALUES 
  (?, 
  ?);
INSERT INTO SensorParcela
  (SensorID, 
  ParcelaEspacoID) 
VALUES 
  (?, 
  ?);
INSERT INTO Setor
  (ID, 
  Designacao) 
VALUES 
  (?, 
  ?);
INSERT INTO SistemaRega
  (EspacoID, 
  DebitoMaximo) 
VALUES 
  (?, 
  ?);
INSERT INTO TipoCultura
  (ID, 
  Designacao) 
VALUES 
  (?, 
  ?);
INSERT INTO TipoOperacao
  (ID, 
  Designacao) 
VALUES 
  (?, 
  ?);
INSERT INTO TipoProduto
  (ID, 
  Designacao) 
VALUES 
  (?, 
  ?);
INSERT INTO TipoSensor
  (ID, 
  Designacao, 
  Unidade) 
VALUES 
  (?, 
  ?, 
  ?);
INSERT INTO Variedade
  (ID, 
  Variedade) 
VALUES 
  (?, 
  ?);