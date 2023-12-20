INSERT INTO APLICACAO (
    ID,
    DESIGNACAO
) VALUES (
    ?,
    ?
);

INSERT INTO APLICACAOPRODUTO (
    FATORPRODUCAOID,
    APLICACAOID
) VALUES (
    ?,
    ?
);

INSERT INTO ARMAZEM (
    ESPACOID,
    CAPACIDADE
) VALUES (
    ?,
    ?
);

INSERT INTO CADERNOCAMPO (
    ID,
    QUINTAID
) VALUES (
    ?,
    ?
);

INSERT INTO CULTURA (
    ID,
    VARIEDADE,
    PODA,
    FLORACAO,
    COLHEITA,
    SEMENTEIRA,
    NOMEESPECIEID,
    TIPOCULTURAID
) VALUES (
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?
);

INSERT INTO ELEMENTO (
    ID,
    DESIGNACAO
) VALUES (
    ?,
    ?
);

INSERT INTO ELEMENTOFICHA (
    FATORPRODUCAOID,
    ELEMENTOID,
    QUANTIDADE
) VALUES (
    ?,
    ?,
    ?
);

INSERT INTO ESPACO (
    ID,
    DESIGNACAO,
    AREA,
    UNIDADE,
    DATACRIACAO,
    QUINTAID
) VALUES (
    ?,
    ?,
    ?,
    ?,
    ?,
    ?
);

INSERT INTO ESTABULO (
    ESPACOID
) VALUES (
    ?
);

INSERT INTO ESTACAOMETEOROLOGICA (
    ID,
    QUINTAID
) VALUES (
    ?,
    ?
);

INSERT INTO ESTADO (
    ID,
    DESIGNACAO
) VALUES (
    ?,
    ?
);

INSERT INTO FATORPRODUCAO (
    ID,
    DESIGNACAO,
    FABRICANTE,
    MATERIAORGANICA,
    PH,
    FORMATOPRODUTOID,
    TIPOPRODUTOID
) VALUES (
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?
);

INSERT INTO FERTILIZACAO (
    OPERACAOID,
    MODOFERTILIZACAOID
) VALUES (
    ?,
    ?
);

INSERT INTO FORMATOPRODUTO (
    ID,
    DESIGNACAO
) VALUES (
    ?,
    ?
);

INSERT INTO GARAGEM (
    ESPACOID
) VALUES (
    ?
);

INSERT INTO LOGOPERACAO (
    ID,
    DATAOPERACAO,
    QUANTIDADE,
    UNIDADE,
    TIPOOPERACAO,
    ESTADO,
    REGISTO,
    TIPOREGISTO
) VALUES (
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?
);

INSERT INTO MODOFERTILIZACAO (
    ID,
    DESIGNACAO
) VALUES (
    ?,
    ?
);

INSERT INTO NOMEESPECIE (
    ID,
    NOMECOMUM,
    ESPECIE
) VALUES (
    ?,
    ?,
    ?
);

INSERT INTO NOMEPRODUTO (
    ID,
    DESIGNACAO
) VALUES (
    ?,
    ?
);

INSERT INTO OPERACAO (
    ID,
    DATAOPERACAO,
    QUANTIDADE,
    UNIDADE,
    REGISTO,
    TIPOOPERACAOID,
    CADERNOCAMPOID,
    ESTADOID
) VALUES (
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?
);

INSERT INTO OPERACAOFATOR (
    OPERACAOID,
    FATORPRODUCAOID
) VALUES (
    ?,
    ?
);

INSERT INTO OPERACAOPARCELA (
    OPERACAOID,
    PARCELAESPACOID
) VALUES (
    ?,
    ?
);

INSERT INTO OPERACAOPLANTACAO (
    OPERACAOID,
    PLANTACAOID
) VALUES (
    ?,
    ?
);

INSERT INTO OPERACAORECEITA (
    OPERACAOID,
    RECEITAID
) VALUES (
    ?,
    ?
);

INSERT INTO OPERACAOSETOR (
    OPERACAOID,
    HORAINICIAL,
    SETORID
) VALUES (
    ?,
    ?,
    ?
);

INSERT INTO PARCELA (
    ESPACOID
) VALUES (
    ?
);

INSERT INTO PLANOPLANTACAO (
    PLANTACAOSETORPLANTACAOID,
    QUANTIDADE,
    DATAINICIAL,
    DATAFINAL
) VALUES (
    ?,
    ?,
    ?,
    ?
);

INSERT INTO PLANOSETOR (
    SETORID,
    DATAINICIAL,
    DATAFINAL,
    CAUDAL,
    SISTEMAREGAQUINTAID
) VALUES (
    ?,
    ?,
    ?,
    ?,
    ?
);

INSERT INTO PLANTACAO (
    ID,
    DATAINICIAL,
    DATAFINAL,
    QUANTIDADE,
    UNIDADE,
    ESTADOFENOLOGICO,
    CULTURAID,
    PARCELAESPACOID
) VALUES (
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?,
    ?
);

INSERT INTO PLANTACAOSETOR (
    PLANTACAOID,
    SETORID
) VALUES (
    ?,
    ?
);

INSERT INTO PRODUTO (
    CULTURAID,
    NOMEPRODUTOID
) VALUES (
    ?,
    ?
);

INSERT INTO PRODUTOARMAZEM (
    PRODUTOCULTURAID,
    ARMAZEMESPACOID,
    QUANTIDADE
) VALUES (
    ?,
    ?,
    ?
);

INSERT INTO QUINTA (
    ID,
    DESIGNACAO
) VALUES (
    ?,
    ?
);

INSERT INTO RECEITA (
    ID,
    DESIGNACAO
) VALUES (
    ?,
    ?
);

INSERT INTO RECEITAFATOR (
    RECEITAID,
    FATORPRODUCAOID,
    QUANTIDADE,
    UNIDADE
) VALUES (
    ?,
    ?,
    ?,
    ?
);

INSERT INTO REGA (
    ESPACOID
) VALUES (
    ?
);

INSERT INTO REGISTOSENSOR (
    ID,
    VALOR,
    DATAREGISTO,
    SENSORID,
    CADERNOCAMPOID
) VALUES (
    ?,
    ?,
    ?,
    ?,
    ?
);

INSERT INTO SENSOR (
    ID,
    TIPOSENSORID
) VALUES (
    ?,
    ?
);

INSERT INTO SENSORESTACAO (
    SENSORID,
    ESTACAOMETEOROLOGICAID
) VALUES (
    ?,
    ?
);

INSERT INTO SENSORPARCELA (
    SENSORID,
    PARCELAESPACOID
) VALUES (
    ?,
    ?
);

INSERT INTO SETOR (
    ID,
    DESIGNACAO
) VALUES (
    ?,
    ?
);

INSERT INTO SISTEMAREGA (
    QUINTAID,
    DEBITOMAXIMO
) VALUES (
    ?,
    ?
);

INSERT INTO TIPOCULTURA (
    ID,
    DESIGNACAO
) VALUES (
    ?,
    ?
);

INSERT INTO TIPOOPERACAO (
    ID,
    DESIGNACAO
) VALUES (
    ?,
    ?
);

INSERT INTO TIPOPRODUTO (
    ID,
    DESIGNACAO
) VALUES (
    ?,
    ?
);

INSERT INTO TIPOSENSOR (
    ID,
    DESIGNACAO,
    UNIDADE
) VALUES (
    ?,
    ?,
    ?
);