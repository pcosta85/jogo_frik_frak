import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Jogo_Frik_Frak2 extends JPanel {

    enum Jogador { JOGADOR1, JOGADOR2 }

    static class PontoTabuleiro {
        int x, y;
        Jogador ocupante;

        public PontoTabuleiro(int x, int y) {
            this.x = x;
            this.y = y;
            this.ocupante = null;
        }

        public boolean contem(int mx, int my) {
            return Math.hypot(mx - x, my - y) <= 12;
        }

        public boolean estaVazio() {
            return ocupante == null;
        }
    }

    private final List<PontoTabuleiro> pontos = new ArrayList<>();
    private int passo = 90;
    private final int deslocamentoY = 40;

    private Jogador jogadorAtual = Jogador.JOGADOR1;
    private int pecasJ1 = 0, pecasJ2 = 0;

    private boolean faseColocacao = true;
    private boolean faseTroca = false;

    private PontoTabuleiro selecionado = null;

    public Jogo_Frik_Frak2() {
        setBackground(new Color(230, 230, 230));

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                passo = Math.min(getWidth(), getHeight()) / 8;
                criarPontos();
                repaint();
            }
        });

        criarPontos();

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for (PontoTabuleiro p : pontos) {

                    if (p.contem(e.getX(), e.getY())) {

                        // 🔹 FASE 1 - COLOCAÇÃO
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
                        // 🔹 FASE 2 - MOVIMENTO E TROCA
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

    private void criarPontos() {
        int cx = getWidth() / 2;
        int cy = getHeight() / 2 + deslocamentoY;

        if (cx == 0) cx = 425;
        if (cy == deslocamentoY) cy = 325;

        pontos.clear();
        pontos.add(new PontoTabuleiro(cx, cy));

        int[][] dir = {
                {1,0},{-1,0},{0,1},{0,-1},
                {1,1},{-1,-1},{1,-1},{-1,1}
        };

        for (int[] d : dir) {
            for (int i = 1; i <= 3; i++) {
                pontos.add(new PontoTabuleiro(cx + d[0]*passo*i, cy + d[1]*passo*i));
            }
        }
    }

    private boolean ehVizinho(PontoTabuleiro a, PontoTabuleiro b) {
        for (int dx = -passo; dx <= passo; dx += passo) {
            for (int dy = -passo; dy <= passo; dy += passo) {
                if ((Math.abs(dx) + Math.abs(dy) == passo || Math.abs(dx) == Math.abs(dy) && Math.abs(dx) == passo)
                        && a.x + dx == b.x && a.y + dy == b.y) {
                    return true;
                }
            }
        }
        return false;
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
        for (int i = 1; i <= 3; i++) {
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
        faseTroca = false;
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
        int tamanho = 3 * passo;

        int x1 = cx - tamanho;
        int y1 = cy - tamanho;
        int x2 = cx + tamanho;
        int y2 = cy + tamanho;

        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);

        g2.drawRect(x1, y1, tamanho*2, tamanho*2);
        g2.drawLine(x1, cy, x2, cy);
        g2.drawLine(cx, y1, cx, y2);
        g2.drawLine(x1, y1, x2, y2);
        g2.drawLine(x1, y2, x2, y1);

        for (PontoTabuleiro p : pontos) {
            g2.setColor(Color.GRAY);
            g2.fillOval(p.x - 5, p.y - 5, 10, 10);

            if (selecionado == p) {
                g2.setColor(Color.RED);
                g2.drawOval(p.x - 12, p.y - 12, 24, 24);
            }

            if (p.ocupante != null) {
                if (p.ocupante == Jogador.JOGADOR1) {
                    g2.setColor(Color.BLACK);
                    g2.fillOval(p.x - 10, p.y - 10, 20, 20);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillOval(p.x - 10, p.y - 10, 20, 20);
                    g2.setColor(Color.BLACK);
                    g2.drawOval(p.x - 10, p.y - 10, 20, 20);
                }
            }
        }

        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Vez: " + (jogadorAtual == Jogador.JOGADOR1 ? "Preto" : "Branco"), 20, 30);
        String fase = faseColocacao ? "Colocação" : "Movimento/Troca";
        g2.drawString("Fase: " + fase, 20, 50);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Frik Frak");
            Jogo_Frik_Frak2 jogo = new Jogo_Frik_Frak2();
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