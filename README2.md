#  Jogo Frik Frak (Java Swing)

##  Descrição

Este projeto implementa o jogo **Frik Frak** utilizando **Java Swing**, com interface gráfica interativa.

O jogo é baseado num tabuleiro 3x3 com ligações entre pontos (modelo em grafo), onde dois jogadores competem para formar uma linha de 3 peças.

---

o jogo suporta dois modos:
-  Jogador vs Jogador
-  Jogador vs Computador
  
---

##  Conceitos Utilizados

- Conceção e Análise de Algoritmos (CAA)
- Interface gráfica com Java Swing
- Estrutura de dados 
- Eventos de rato 
- Lógica de jogo por fases
- Noções básicas de Inteligência Artificial

---

##  Funcionalidades

  - Tabuleiro dinâmico 
  - Fase de colocação de peças
  - Fase de movimento entre pontos vizinhos
  - Verificação automática de vitória
  - Destaque da peça selecionada
  - Botão de reset do jogo
  - Escolha do modo de jogo (PVP ou PVC)
  - Jogada automática do computador

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

### Importação das bibliotecas
```Java
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
```
Estas linhas importam as bibliotecas necessárias para o funcionamento do programa.

A biblioteca `java.awt` é usada para desenhar elementos gráficos, como linhas, círculos, cores e fontes.
A biblioteca `java.awt`.event permite capturar eventos, como cliques do rato e redimensionamento da janela.
A biblioteca `ArrayList` e `List` permite guardar os pontos do tabuleiro numa lista dinâmica.
A biblioteca `javax.swing` é usada para criar a interface gráfica, como janela, painel, botão e mensagens.

---

### Classe principal
```Java
public class Jogo_Frik_Frak3 extends JPanel
```

Esta linha cria a classe principal do jogo.

A classe chama-se `Jogo_Frik_Frak3` e herda de `JPanel`.
Isto significa que o jogo será desenhado dentro de um painel gráfico.

O `JPanel` é usado para desenhar o tabuleiro, as peças e as informações do jogo.

---

### Enumeração dos jogadores
```java
enum Jogador { JOGADOR1, JOGADOR2 }
```

Esta linha cria dois jogadores possíveis:

- `JOGADOR1`
- `JOGADOR2`

No jogo, o `JOGADOR1` representa as peças pretas e o `JOGADOR2` representa as peças brancas ou o computador.

---

### Enumeração do modo de jogo
```java
enum ModoJogo { PVP, PVC }
```

Esta enumeração define os modos disponíveis:
- PVP significa jogador contra jogador;
- PVC significa jogador contra computador.

---

### Classe interna PontoTabuleiro
```java
static class PontoTabuleiro {
    int x, y;
    Jogador ocupante;
    List<PontoTabuleiro> vizinhos = new ArrayList<>();
}
```

Esta classe representa cada ponto do tabuleiro.

Cada ponto possui:

- `x` e `y`: coordenadas na tela;
- `ocupante`: indica se o ponto está ocupado por algum jogador;
- `vizinhos`: lista dos pontos ligados a este ponto.

Ou seja, cada posição do tabuleiro sabe onde está localizada e quais posições estão ligadas a ela.

---

### Construtor do ponto
```java
public PontoTabuleiro(int x, int y) {
    this.x = x;
    this.y = y;
}
```

Este construtor recebe as coordenadas `x` e `y` e guarda esses valores no ponto criado.

A palavra `this` indica que o valor pertence ao próprio objeto.

---

### Método contem
```java
public boolean contem(int mx, int my) {
    return Math.hypot(mx - x, my - y) <= 12;
}
```

Este método verifica se o clique do rato foi feito dentro de um ponto do tabuleiro.

- `mx` representa a posição X do rato;
- `my` representa a posição Y do rato;
- `Math.hypot` calcula a distância entre o clique e o ponto.

Se a distância for menor ou igual a 12, o programa considera que o jogador clicou naquele ponto.

---

### Método estaVazio
```java
public boolean estaVazio() {
    return ocupante == null;
}
```

Este método verifica se o ponto está vazio.

Se `ocupante == null`, significa que nenhuma peça está naquele ponto.

---

### Lista de pontos do tabuleiro
```java
private final List<PontoTabuleiro> pontos = new ArrayList<>();
``` 

Esta linha cria uma lista para guardar todos os pontos do tabuleiro.
Como o tabuleiro tem 3x3, serão criados 9 pontos.

---

### Variáveis principais do jogo
```java
private int passo = 80;
private final int deslocamentoY = 40;
```
A variável `passo` define a distância entre os pontos do tabuleiro.
A variável `deslocamentoY` desloca o tabuleiro um pouco para baixo na janela.

```java
private Jogador jogadorAtual = Jogador.JOGADOR1;
```

Esta linha define que o primeiro jogador a jogar será o `JOGADOR1`

```java
private int pecasJ1 = 0, pecasJ2 = 0;
```

Estas variáveis contam quantas peças cada jogador já colocou no tabuleiro.
Cada jogador pode colocar no máximo 3 peças.

```java
private boolean faseColocacao = true;
```

Esta variável guarda o ponto selecionado pelo jogador durante a fase de movimento.

```java
private ModoJogo modo = ModoJogo.PVP;
```

Esta linha define o modo inicial como jogador contra jogador.

---

### Construtor do jogo
```java
public Jogo_Frik_Frak3() {
    setBackground(new Color(230, 230, 230));
```

Este é o construtor da classe principal.
Ele define a cor de fundo do painel com um cinzento claro.

---

### Redimensionamento da janela
```java
addComponentListener(new ComponentAdapter() {
    public void componentResized(ComponentEvent e) {
        passo = Math.min(getWidth(), getHeight()) / 6;
        criarPontos();
        repaint();
    }
});
```

Este bloco é executado quando a janela muda de tamanho.
O programa recalcula o valor de `passo`, recria os pontos do tabuleiro e redesenha tudo com `repaint()`.
Assim, o tabuleiro adapta-se ao tamanho da janela.

---

### Criação inicial dos pontos
```java
criarPontos();
```

Esta linha chama o método responsável por criar os 9 pontos do tabuleiro.

---

### Captura do clique do rato
```java
addMouseListener(new MouseAdapter() {
    public void mouseClicked(MouseEvent e) {
```

Este bloco permite que o programa reaja quando o jogador clica no tabuleiro.
O objeto `MouseEvent` e guarda a posição onde o rato foi clicado.

---

### Percorrer todos os pontos
```java
for (PontoTabuleiro p : pontos) {
```

Este ciclo percorre todos os pontos do tabuleiro para verificar em qual deles o jogador clicou.

---

### Verificar se o clique foi no ponto
```java
if (p.contem(e.getX(), e.getY())) {
```

Esta linha verifica se o clique aconteceu dentro do ponto atual.
Se sim, o programa executa a jogada.

---

### Fase de colocação
```java
if (faseColocacao) {
    if (p.estaVazio()) {
```

Este bloco é executado quando o jogo ainda está na fase de colocar peças.
Primeiro verifica se o ponto está vazio.

```java
if (jogadorAtual == Jogador.JOGADOR1 && pecasJ1 >= 3) return;
if (jogadorAtual == Jogador.JOGADOR2 && pecasJ2 >= 3) return;
```

Estas linhas impedem que um jogador coloque mais de 3 peças.
Se o jogador já tiver 3 peças, a jogada é ignorada.

```java
p.ocupante = jogadorAtual;
```

Esta linha coloca a peça do jogador atual no ponto clicado.

```java
if (jogadorAtual == Jogador.JOGADOR1) pecasJ1++;
else pecasJ2++;
```

Aqui o programa aumenta o contador de peças do jogador que acabou de jogar.

```java
if (verificarVitoria(p)) return;
```

Depois de colocar a peça, o programa verifica se houve vitória.
Se houver vitória, a função termina.

```java
if (pecasJ1 == 3 && pecasJ2 == 3) faseColocacao = false;
```

Quando os dois jogadores já colocaram as 3 peças, o jogo muda para a fase de movimento.

```java
alternarJogador();
```

Esta linha passa a vez para o outro jogador.

---

### Fase de movimento
```java
else {
    if (selecionado == null) {
```

Este bloco acontece quando já terminou a fase de colocação.
Se ainda não existe peça selecionada, o jogador deve escolher uma das suas peças.

```java
if (p.ocupante == jogadorAtual) {
    selecionado = p;
}
```

Se o ponto clicado pertence ao jogador atual, esse ponto fica selecionado.

```java
else {
    if (p.estaVazio() && ehVizinho(selecionado, p)) {
```

Se já existe uma peça selecionada, o segundo clique deve ser numa posição vazia e vizinha.

```java
p.ocupante = selecionado.ocupante;
selecionado.ocupante = null;
```

Estas linhas movem a peça:
- A peça sai do ponto selecionado;
- Vai para o novo ponto escolhido.

```java
if (verificarVitoria(p)) return;
```

Depois do movimento, verifica-se se o jogador venceu.

```java
selecionado = null;
alternarJogador();
```

Depois da jogada, a seleção é limpa e a vez passa para o outro jogador.

---

### Redesenhar o tabuleiro
```java
repaint();
```

Esta linha redesenha o painel, atualizando visualmente o tabuleiro e as peças.

--- 

### Jogada do computador
```java
if (modo == ModoJogo.PVC && jogadorAtual == Jogador.JOGADOR2) {
    SwingUtilities.invokeLater(() -> jogadaComputador());
}
```

Se o modo for contra o computador e for a vez do `JOGADOR2`, o programa chama automaticamente a jogada do computador.

O `SwingUtilities.invokeLater` garante que a jogada será executada corretamente na interface gráfica.

---

### Alternar jogador
```java
private void alternarJogador() {
    jogadorAtual = (jogadorAtual == Jogador.JOGADOR1) ? Jogador.JOGADOR2 : Jogador.JOGADOR1;
}
```

Este método troca a vez dos jogadores.
- Se for a vez do Jogador 1, passa para o Jogador 2.
- Se for a vez do Jogador 2, passa para o Jogador 1.

--- 

### Método jogadaComputador
```java
private void jogadaComputador() {
```

Este método contém a lógica da jogada automática do computador.

```java
if (jogadorAtual != Jogador.JOGADOR2) return;
```

Esta linha garante que o computador só joga quando for realmente a vez dele.

---

### Computador na fase de colocação
```java
if (faseColocacao) {
    for (PontoTabuleiro p : pontos) {
        if (p.estaVazio()) {
```

Na fase de colocação, o computador procura o primeiro ponto vazio.

```java
p.ocupante = Jogador.JOGADOR2;
pecasJ2++;
```

Estas linhas colocam uma peça branca no ponto vazio encontrado.

```java
if (verificarVitoria(p)) return;
```

Depois de jogar, verifica se venceu.

```java
if (pecasJ1 == 3 && pecasJ2 == 3)
    faseColocacao = false;
```

Se os dois jogadores já colocaram 3 peças, o jogo muda para a fase de movimento.

```java
alternarJogador();
repaint();
return;
```

O computador termina a jogada, passa a vez ao jogador humano e atualiza o tabuleiro.

---

### Computador na fase de movimento
```java
else {
    for (PontoTabuleiro origem : pontos) {
        if (origem.ocupante == Jogador.JOGADOR2) {
```

Na fase de movimento, o computador procura uma das suas peças.

```java
for (PontoTabuleiro destino : origem.vizinhos) {
    if (destino.estaVazio()) {
```

Depois procura um ponto vizinho vazio para mover a peça.

```java
destino.ocupante = origem.ocupante;
origem.ocupante = null;
```

Estas linhas executam o movimento da peça do computador.

---

### Criação do tabuleiro
```java
private void criarPontos() {
```

Este método cria os pontos do tabuleiro.

```java
int cx = getWidth() / 2;
int cy = getHeight() / 2 + deslocamentoY;
```

Estas linhas definem valores padrão caso a janela ainda não tenha tamanho definido.

```java
pontos.clear();
```

Antes de criar os pontos, a lista é limpa.
Isto evita duplicação de pontos quando a janela é redimensionada.

```java
PontoTabuleiro[][] grid = new PontoTabuleiro[3][3];
```

Cria uma matriz 3x3 para representar o tabuleiro.

```java
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
```

Estes ciclos percorrem linhas e colunas da matriz.

```java
grid[i][j] = new PontoTabuleiro(
    cx + (i - 1) * passo,
    cy + (j - 1) * passo
);
```

Esta linha cria cada ponto na posição correta.
O cálculo distribui os pontos em forma de grelha 3x3.

```java
pontos.add(grid[i][j]);
```

Cada ponto criado é adicionado à lista geral `pontos`.

---

### Ligação entre os pontos
```java
if (i > 0) conectar(grid[i][j], grid[i - 1][j]);
if (j > 0) conectar(grid[i][j], grid[i][j - 1]);
```

Estas linhas conectam os pontos horizontalmente e verticalmente.

```java
conectar(grid[0][0], grid[1][1]);
conectar(grid[1][1], grid[2][2]);
conectar(grid[2][0], grid[1][1]);
conectar(grid[1][1], grid[0][2]);
```

Estas linhas criam as ligações diagonais do tabuleiro.

---

### Método conectar
```java
private void conectar(PontoTabuleiro a, PontoTabuleiro b) {
    a.vizinhos.add(b);
    b.vizinhos.add(a);
}
```

Este método liga dois pontos.

Como a ligação deve funcionar nos dois sentidos, o ponto `a` é vizinho do ponto `b`, e o ponto `b` também é vizinho do ponto `a`.


