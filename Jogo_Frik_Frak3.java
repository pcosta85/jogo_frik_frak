import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Jogo_Frik_Frak3 extends JPanel {

    enum Jogador { JOGADOR1, JOGADOR2 }

    static class PontoTabuleiro {
        int x, y;
        Jogador ocupante;
        List<PontoTabuleiro> vizinhos = new ArrayList<>();

        public PontoTabuleiro(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean contem(int mx, int my) {
            return Math.hypot(mx - x, my - y) <= 12;
        }

        public boolean estaVazio() {
            return ocupante == null;
        }
    }

    private final List<PontoTabuleiro> pontos = new ArrayList<>();
    private int passo = 80;
    private final int deslocamentoY = 40;

    private Jogador jogadorAtual = Jogador.JOGADOR1;
    private int pecasJ1 = 0, pecasJ2 = 0;

    private boolean faseColocacao = true;
    private PontoTabuleiro selecionado = null;

    public Jogo_Frik_Frak3() {
        setBackground(new Color(230, 230, 230));

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                passo = Math.min(getWidth(), getHeight()) / 6;
                criarPontos();
                repaint();
            }
        });

        criarPontos();

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for (PontoTabuleiro p : pontos) {

                    if (p.contem(e.getX(), e.getY())) {

                        // FASE 1 - COLOCAÇÃO
                        if (faseColocacao) {
                            if (p.estaVazio()) {

                                if (jogadorAtual == Jogador.JOGADOR1 && pecasJ1 >= 3) return;
                                if (jogadorAtual == Jogador.JOGADOR2 && pecasJ2 >= 3) return;

                                p.ocupante = jogadorAtual;

                                if (jogadorAtual == Jogador.JOGADOR1) pecasJ1++;
                                else pecasJ2++;

                                if (verificarVitoria(p)) return;

                                if (pecasJ1 == 3 && pecasJ2 == 3) faseColocacao = false;

                                alternarJogador();
                            }
                        }
                        // FASE 2 - MOVIMENTO
                        else {
                            if (selecionado == null) {
                                if (p.ocupante == jogadorAtual) {
                                    selecionado = p;
                                }
                            } else {
                                if (p.estaVazio() && ehVizinho(selecionado, p)) {
                                    p.ocupante = selecionado.ocupante;
                                    selecionado.ocupante = null;

                                    if (verificarVitoria(p)) return;

                                    selecionado = null;
                                    alternarJogador();
                                } else {
                                    selecionado = null;
                                }
                            }
                        }
                        repaint();
                        break;
                    }
                }
            }
        });
    }

    private void alternarJogador() {
        jogadorAtual = (jogadorAtual == Jogador.JOGADOR1) ? Jogador.JOGADOR2 : Jogador.JOGADOR1;
    }

    // 🔹 Criar pontos + ligações reais (GRAFO)
    private void criarPontos() {
        int cx = getWidth() / 2;
        int cy = getHeight() / 2 + deslocamentoY;

        if (cx == 0) cx = 425;
        if (cy == deslocamentoY) cy = 325;

        pontos.clear();

        PontoTabuleiro[][] grid = new PontoTabuleiro[3][3];

        // criar pontos
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = new PontoTabuleiro(
                        cx + (i - 1) * passo,
                        cy + (j - 1) * passo
                );
                pontos.add(grid[i][j]);
            }
        }

        // ligações horizontais e verticais
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (i > 0) conectar(grid[i][j], grid[i - 1][j]);
                if (j > 0) conectar(grid[i][j], grid[i][j - 1]);
            }
        }

        // diagonais (como na imagem)
        conectar(grid[0][0], grid[1][1]);
        conectar(grid[1][1], grid[2][2]);

        conectar(grid[2][0], grid[1][1]);
        conectar(grid[1][1], grid[0][2]);
    }

    private void conectar(PontoTabuleiro a, PontoTabuleiro b) {
        a.vizinhos.add(b);
        b.vizinhos.add(a);
    }

    // 🔹 Agora usa grafo (100% correto)
    private boolean ehVizinho(PontoTabuleiro a, PontoTabuleiro b) {
        return a.vizinhos.contains(b);
    }

    private boolean verificarVitoria(PontoTabuleiro p) {
        int[][] dirs = { {1,0},{0,1},{1,1},{1,-1} };

        for (int[] d : dirs) {
            int count = 1;
            count += contar(p, d[0], d[1]);
            count += contar(p, -d[0], -d[1]);

            if (count >= 3) {
                JOptionPane.showMessageDialog(this,
                        "Vitória: " + (p.ocupante == Jogador.JOGADOR1 ? "Jogador 1 (Preto)" : "Jogador 2 (Branco)"));
                resetar();
                return true;
            }
        }
        return false;
    }

    private int contar(PontoTabuleiro p, int dx, int dy) {
        int count = 0;
        for (int i = 1; i <= 2; i++) {
            int nx = p.x + dx * passo * i;
            int ny = p.y + dy * passo * i;

            for (PontoTabuleiro outro : pontos) {
                if (outro.x == nx && outro.y == ny && outro.ocupante == p.ocupante) {
                    count++;
                }
            }
        }
        return count;
    }

    private void resetar() {
        for (PontoTabuleiro p : pontos) p.ocupante = null;
        pecasJ1 = pecasJ2 = 0;
        faseColocacao = true;
        jogadorAtual = Jogador.JOGADOR1;
        selecionado = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int cx = getWidth() / 2;
        int cy = getHeight() / 2 + deslocamentoY;
        int tamanho = passo;

        int x1 = cx - tamanho;
        int y1 = cy - tamanho;
        int largura = tamanho * 2;

        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLACK);

        // quadrado arredondado
        // desenhar ligações reais entre pontos (igual à imagem)
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLACK);

        for (PontoTabuleiro p : pontos) {
            for (PontoTabuleiro viz : p.vizinhos) {
                g2.drawLine(p.x, p.y, viz.x, viz.y);
            }
        }

        // pontos e peças
        for (PontoTabuleiro p : pontos) {
            g2.setColor(Color.BLACK);
            g2.fillOval(p.x - 6, p.y - 6, 12, 12);

            if (selecionado == p) {
                g2.setColor(Color.RED);
                g2.drawOval(p.x - 12, p.y - 12, 24, 24);
            }

            if (p.ocupante != null) {
                if (p.ocupante == Jogador.JOGADOR1) {
                    g2.setColor(Color.BLACK);
                    g2.fillOval(p.x - 12, p.y - 12, 24, 24);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillOval(p.x - 12, p.y - 12, 24, 24);
                    g2.setColor(Color.BLACK);
                    g2.drawOval(p.x - 12, p.y - 12, 24, 24);
                }
            }
        }

        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Vez: " + (jogadorAtual == Jogador.JOGADOR1 ? "Preto" : "Branco"), 20, 30);
        String fase = faseColocacao ? "Colocação" : "Movimento";
        g2.drawString("Fase: " + fase, 20, 50);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Frik Frak");
            Jogo_Frik_Frak3 jogo = new Jogo_Frik_Frak3();

            JButton reset = new JButton("Reset");
            reset.addActionListener(e -> jogo.resetar());

            f.add(jogo, BorderLayout.CENTER);
            f.add(reset, BorderLayout.SOUTH);

            f.setSize(900, 700);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}