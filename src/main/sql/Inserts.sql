INSERT INTO Quinta (Designacao) VALUES ('Quinta Do Ângelo');
INSERT INTO CadernoDeCampo (QuintaID) VALUES (SELECT ID FROM Quinta WHERE UPPER(Designacao) LIKE UPPER('Quinta Do Ângelo'));