#  Jogo Frik Frak (Java Swing)

##  Descrição

Este projeto implementa o jogo **Frik Frak** utilizando **Java Swing**, com interface gráfica interativa.

O jogo é baseado num tabuleiro 3x3 com ligações entre pontos (modelo em grafo), onde dois jogadores competem para formar uma linha de 3 peças.

---

##  Conceitos Utilizados

- Conceção e Análise de Algoritmos (CAA)
- Interface gráfica com Java Swing
- Estrutura de dados 
- Eventos de rato 
- Lógica de jogo por fases

---

##  Funcionalidades

  - Tabuleiro dinâmico 
  - Fase de colocação de peças
  - Fase de movimento entre pontos vizinhos
  - Verificação automática de vitória
  - Destaque da peça selecionada
  - Botão de reset do jogo

---

##  Regras do Jogo

###  Fase 1 – Colocação
- Cada jogador coloca **3 peças**
- Jogadores alternam turnos
- Não pode colocar em posição ocupada

###  Fase 2 – Movimento

- Jogadores movem peças para **pontos vizinhos**
- Apenas movimentos válidos 

###  Vitória

- Ganha quem formar uma linha de **3 peças**

  - Horizontal
  - Vertical
  - Diagonal

---

##  Tecnologias

- Java
- Java Swing
- AWT

---

##  Como Executar

### 1. Compilar

```bash
javac Jogo_Frik_Frak3.java
```

### 2. Executar

```bash
java Jogo_Frik_Frak3
```

---

##  Estrutura do Código

### Classe principal

- `Jogo_Frik_Frak3` - painel principal do jogo

### Classes internas

- `PontoTabuleiro` - representa cada ponto do tabuleiro
- `Jogador` - enumeração dos jogadores

---

##  Lógica Importante

###  Grafo de ligações

Cada ponto tem uma lista de vizinhos:

```java
List<PontoTabuleiro> vizinhos
```

---

###  Verificação de vitória

Verifica 4 direções:

```java
{1,0}  // horizontal
{0,1}  // vertical
{1,1}  // diagonal
{1,-1} // diagonal inversa
```

---

##  Reset do Jogo

Botão "Reset" reinicia:

- Tabuleiro
- Jogadores
- Fase do jogo

---

##  Interface

- Peças pretas - Jogador 1
- Peças brancas - Jogador 2
- Seleção - círculo vermelho
- Linhas - conexões do grafo

---

## - Possíveis Melhorias

  - Sons ao jogar
  - Melhorar design gráfico
  - Sistema de pontuação

---

##  Licença

Uso educacional 

---

##  Autor

Paulo Costa
Curso Engenharia Informática
2ºAno
Conceção e Análise de Algoritmos
