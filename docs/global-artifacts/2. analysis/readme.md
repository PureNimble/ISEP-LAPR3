# Análise

O processo de construção do modelo de domínio é baseado nas especificações do cliente, especialmente nos substantivos (para _conceitos_) e verbos (para _relações_) usados.

## Racional para identificar classes conceituais de domínio ##
Para identificar classes conceituais de domínio, comece fazendo uma lista de classes conceituais candidatas inspiradas na lista de categorias sugerida no livro "Applying UML and Patterns: An Introduction to Object-Oriented Analysis and Design and Iterative Development".


### _Lista de Categorias de Classe Conceitual_ ###

**Transações de Negócios**

* Produto;

---

**Itens de Linha de Transação**

* Operação;
* Produto;


---

**Produto/Serviço relacionado a uma Transação ou Item de Linha de Transação**

* Operação;


---

**Registros de Transação**

* Caderno de Campo;
* Operação;

---

**Papéis de Pessoas ou Organizações**

* Gestor Agrícola;
* Cliente;
* Condutor;
* Gestor de Distribuição;

---

**Lugares**

* Quinta;
* Armazém;
* Garagem;
* Estábulo;
* Parcela;
* Estação Meteorológica;
---

**Eventos Notáveis**

* Operação;
* Fertilização;

---

**Objetos Físicos**

* Plantação;
* Sensor;
* Sistema de Rega;


---

**Descrições de Coisas**

* Registro do Sensor;
* Caderno de Campo;
* Espaço;


---

**Recipientes**

* Armazém;

---

**Elementos de Recipientes**

* Produtos;

---

**Organizações**

* Quinta

---

### **Racional para identificar associações entre classes conceituais** ###

Uma associação é uma relação entre instâncias de objetos que indica uma conexão relevante e que vale a pena lembrar, ou é derivável da Lista de Associações Comuns:


| Concept (A)   |  Association |  Concept (B)         |
|----------	    |:------------:| ------:              |
|Espaco         | pode ser     |  Parcela             |
|               | pode ser     |  Estabulo            |
|               | pode ser     |  Armazem             |
|               | pode ser     |  Garagem             |
|               | pode ser     |  Sistema de rega     |
|Quinta         | contém       |  Espaços             |
|Gestor Agricula| regista      |  Caderno de Campo    |
|Estação Meteorológica| contém |  Sensores            |
|Parcela        | contém       | Plantações           |
|Plantação      | é de uma     | Cultura              |
|               | contém       | Sensores             |
|               | são feitas   | Operações            |
|Sistema de rega| contém       | Setores de rega      |
|Armazem        | contém       | Produtos             |
|Operação       | pode ter     | Fator de produção    |
|Fator de produção| contém     | Ficha Técnica        |




## Domain Model



![Domain Model](./svg/ConceptualModel.svg)


