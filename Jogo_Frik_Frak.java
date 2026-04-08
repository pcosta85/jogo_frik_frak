import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class Jogo_Frik_Frak extends JPanel {

    enum Jogador {
        JOGADOR1, JOGADOR2
    }

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
    private final int passo = 90;
    private final int deslocamentoY = 60;

    private Jogador jogadorAtual = Jogador.JOGADOR1;

    public Jogo_Frik_Frak() {
        setBackground(new Color(230, 230, 230));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                criarPontos();
                repaint();
            }
        });

        criarPontos();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (PontoTabuleiro p : pontos) {
                    if (p.contem(e.getX(), e.getY())) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (p.estaVazio()) {
                                p.ocupante = jogadorAtual;
                                alternarJogador();
                                repaint();
                            }
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            if (!p.estaVazio()) {
                                p.ocupante = null;
                                repaint();
                            }
                        }
                        break;
                    }
                }
            }
        });
    }

    private void alternarJogador() {
        if (jogadorAtual == Jogador.JOGADOR1) {
            jogadorAtual = Jogador.JOGADOR2;
        } else {
            jogadorAtual = Jogador.JOGADOR1;
        }
    }

    public void resetarTabuleiro() {
        for (PontoTabuleiro p : pontos) {
            p.ocupante = null;
        }
        jogadorAtual = Jogador.JOGADOR1;
        repaint();
    }

    private void criarPontos() {
        int cx = getWidth() / 2;
        int cy = getHeight() / 2 + deslocamentoY;

        if (cx == 0) cx = 425;
        if (cy == deslocamentoY) cy = 325 + deslocamentoY;

        pontos.clear();

        pontos.add(new PontoTabuleiro(cx, cy));

        int[][] direcoes = {
            { 1, 0}, {-1, 0},
            { 0, 1}, { 0,-1},
            { 1, 1}, {-1,-1},
            { 1,-1}, {-1, 1}
        };

        for (int[] d : direcoes) {
            int dx = d[0];
            int dy = d[1];

            pontos.add(new PontoTabuleiro(cx + dx * passo, cy + dy * passo));
            pontos.add(new PontoTabuleiro(cx + dx * 2 * passo, cy + dy * 2 * passo));
            pontos.add(new PontoTabuleiro(cx + dx * 3 * passo, cy + dy * 3 * passo));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2 + deslocamentoY;

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));

        g2.drawLine(cx - 3 * passo, cy, cx + 3 * passo, cy);
        g2.drawLine(cx, cy - 3 * passo, cx, cy + 3 * passo);
        g2.drawLine(cx - 3 * passo, cy - 3 * passo, cx + 3 * passo, cy + 3 * passo);
        g2.drawLine(cx - 3 * passo, cy + 3 * passo, cx + 3 * passo, cy - 3 * passo);

        for (PontoTabuleiro p : pontos) {
            desenharPontoBase(g2, p.x, p.y);

            if (p.ocupante != null) {
                desenharPeca(g2, p.x, p.y, p.ocupante);
            }
        }

        desenharInfoTurno(g2);
    }

    private void desenharPontoBase(Graphics2D g2, int x, int y) {
        g2.setColor(Color.GRAY);
        g2.fillOval(x - 5, y - 5, 10, 10);
    }

    private void desenharPeca(Graphics2D g2, int x, int y, Jogador jogador) {
        if (jogador == Jogador.JOGADOR1) {
            g2.setColor(Color.BLACK);
            g2.fillOval(x - 10, y - 10, 20, 20);
        } else {
            g2.setColor(Color.WHITE);
            g2.fillOval(x - 10, y - 10, 20, 20);
            g2.setColor(Color.BLACK);
            g2.drawOval(x - 10, y - 10, 20, 20);
        }
    }

    private void desenharInfoTurno(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 18));

        String texto;
        if (jogadorAtual == Jogador.JOGADOR1) {
            texto = "Vez: Jogador 1 (Preto)";
        } else {
            texto = "Vez: Jogador 2 (Branco)";
        }

        g2.setColor(Color.BLACK);
        g2.drawString(texto, 20, 30);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(850, 650);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tabuleiro - Jogo Frik Frak");

            Jogo_Frik_Frak tabuleiro = new Jogo_Frik_Frak();

            JButton btnResetar = new JButton("Resetar Tabuleiro");
            btnResetar.addActionListener(e -> tabuleiro.resetarTabuleiro());

            JPanel painelPrincipal = new JPanel(new BorderLayout());
            painelPrincipal.add(tabuleiro, BorderLayout.CENTER);
            painelPrincipal.add(btnResetar, BorderLayout.SOUTH);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(painelPrincipal);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}