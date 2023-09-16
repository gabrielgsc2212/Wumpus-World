import java.awt.*;
import javax.swing.*;
import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameScreen extends JFrame implements MouseListener
{
    private final int TAMANHO_TABULEIRO = 15;
    private final int TAMANHO_CASA = 40;
    private Timer timer;
    private JLabel timerLabel;
    private JLabel listaItensLabel;
    private long startTime;
    private JPanel tabuleiro;
    private Wumpus wumpus;
    private Player player;
    private NewMonster newMonster;
    private boolean[][] hasPoco;
    private boolean[][] hasMadeira;
    private boolean[][] hasOuro;
    private boolean[][] hasWumpus;
    private boolean[][] hasNewMonster;
    private boolean[][] hasPlayer;
    private boolean[][] playerPath;
    private boolean iluminarCima;
    private boolean iluminarBaixo;
    private boolean iluminarDireita;
    private boolean iluminarEsquerda;
    public boolean temBateria = true;
    public boolean wumpusAlive = true;
    public boolean newMonsterAlive = true;
    public int count = 0;
    public int maxUse = 2;
    public int health = 100;
    public JLabel HPlabel;
    public JTextArea historicoTextArea;
    public GameScreen gameParametro;
    public JCheckBox debugOption;

    public GameScreen() 
    {
        setTitle("Caverna dos monstros");
        setSize(918, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);

        hasPoco = new boolean[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        hasMadeira = new boolean[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        hasOuro = new boolean[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        hasWumpus = new boolean[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        hasNewMonster = new boolean[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        hasPlayer = new boolean[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        playerPath = new boolean[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];

        tabuleiro = createTabuleiroPanel();
        getContentPane().add(tabuleiro);

        debugOption = new JCheckBox("Debug");

        JPanel ladoDireito = createLadoDireitoPanel();
        getContentPane().add(ladoDireito);

        startTime = System.currentTimeMillis();

        timer = new Timer(1000, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                String formattedTime = dateFormat.format(new Date(elapsedTime));
                timerLabel.setText("Tempo: " + formattedTime);
            }
        });
        timer.start();

        insertRandomItems("POÇO", 5);
        insertRandomItems("MADEIRA", 3);
        insertRandomItems("OURO", 1);

        initializeWumpus();
        initializeNewMonster();

        player = new Player(TAMANHO_TABULEIRO - 1, 0, TAMANHO_TABULEIRO, TAMANHO_CASA, tabuleiro, health, hasPlayer, hasPoco, hasMadeira, hasOuro, listaItensLabel, hasWumpus, hasNewMonster, HPlabel, historicoTextArea, playerPath, wumpus, newMonster, debugOption);
        player.requestFocus();

        player.addToHistorico("Cuidado! Há dois monstros à solta!");
        player.addToHistorico("Se cuide...");
        player.addToHistorico("Você possui " + maxUse + " baterias ao todo.");

        repaintTabuleiro();

        addKeyListener(new KeyListener() 
        {
            @Override
            public void keyPressed(KeyEvent e) 
            {                
                int keyCode = e.getKeyCode();
                
                if (keyCode == KeyEvent.VK_L)
                {
                    count++;
                    if (count > maxUse)
                    {
                        player.addToHistorico("A bateria da lanterna já esgotou.");
                        temBateria = false;
                    }
                    else
                    {
                        player.addToHistorico("Você utilizou a " + count + "° bateria.");
                        temBateria = true;
                        useLanterna();
                        return;
                    }
                }
                else if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_D)
                {
                    wumpus.mover(e.getKeyCode());
                    newMonster.mover(e.getKeyCode());
                    player.mover(e.getKeyCode());
                    
                    if (player.checkWinCondition() || player.checkLoseCondition())
                    {
                        SwingUtilities.invokeLater(new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                new MenuRestart(player);
                            }
                        });
                        setVisible(false);
                    }
                }
                else
                {
                    return;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
            @Override
            public void keyTyped(KeyEvent e) {}
        });

        tabuleiro.addMouseListener(this);
    }

    private void insertRandomItems(String item, int count) 
    {
        Random random = new Random();
        int remainingCount = count;

        while (remainingCount > 0) 
        {
            int row, col;
            do 
            {
                row = random.nextInt(TAMANHO_TABULEIRO);
                col = random.nextInt(TAMANHO_TABULEIRO);
            } while (row == TAMANHO_TABULEIRO - 1 && col == 0);

            JPanel casa = (JPanel) tabuleiro.getComponent(row * TAMANHO_TABULEIRO + col);

            if (casa.getBackground() == Color.WHITE) 
            {
                switch (item) 
                {
                case "POÇO":
                    hasPoco[row][col] = true;
                    JLabel labelPoco = new JLabel("POÇO");
                    labelPoco.setForeground(Color.BLACK);
                    casa.setBackground(Color.BLACK);
                    casa.add(labelPoco);
                    Font fontPoco = labelPoco.getFont();
                    labelPoco.setFont(new Font(fontPoco.getName(), Font.PLAIN, 11));
                    labelPoco.setHorizontalAlignment(SwingConstants.CENTER);
                    labelPoco.setVerticalAlignment(SwingConstants.CENTER);

                    addBrisaIfValid(row - 1, col);
                    addBrisaIfValid(row + 1, col);
                    addBrisaIfValid(row, col - 1);
                    addBrisaIfValid(row, col + 1);

                    break;

                case "MADEIRA":
                    hasMadeira[row][col] = true;
                    JLabel labelMadeira = new JLabel("MADEIRA");
                    labelMadeira.setForeground(Color.BLACK);
                    casa.setBackground(new Color(139, 69, 19));
                    casa.add(labelMadeira);
                    Font fontMadeira = labelMadeira.getFont();
                    labelMadeira.setFont(new Font(fontMadeira.getName(), Font.PLAIN, 9));
                    labelMadeira.setHorizontalAlignment(SwingConstants.CENTER);
                    labelMadeira.setVerticalAlignment(SwingConstants.CENTER);

                    break;

                case "OURO":
                    hasOuro[row][col] = true;
                    JLabel labelOuro = new JLabel("OURO");
                    labelOuro.setForeground(Color.BLACK);
                    casa.setBackground(Color.YELLOW);
                    casa.add(labelOuro);
                    Font fontOuro = labelOuro.getFont();
                    labelOuro.setFont(new Font(fontOuro.getName(), Font.PLAIN, 11));
                    labelOuro.setHorizontalAlignment(SwingConstants.CENTER);
                    labelOuro.setVerticalAlignment(SwingConstants.CENTER);

                    break;
                }
                remainingCount--;
            }
        }
    }

    private void addBrisaIfValid(int row, int col) 
    {
        if (row >= 0 && row < TAMANHO_TABULEIRO && col >= 0 && col < TAMANHO_TABULEIRO) 
        {
            JPanel brisaCasa = (JPanel) tabuleiro.getComponent(row * TAMANHO_TABULEIRO + col);
            JLabel labelBrisa = new JLabel("Brisa...");
            labelBrisa.setForeground(Color.BLACK);
            brisaCasa.add(labelBrisa);
        }
    }

    private JPanel createTabuleiroPanel() 
    {
        tabuleiro = new JPanel(new GridLayout(TAMANHO_TABULEIRO, TAMANHO_TABULEIRO));
        tabuleiro.setBounds(0, 0, TAMANHO_TABULEIRO * TAMANHO_CASA, TAMANHO_TABULEIRO * TAMANHO_CASA);

        for (int row = 0; row < TAMANHO_TABULEIRO; row++) 
        {
            for (int col = 0; col < TAMANHO_TABULEIRO; col++) 
            {
                JPanel casa = new JPanel();
                casa.setBackground(Color.WHITE);
                tabuleiro.add(casa);
                casa.addMouseListener(this);
                hasWumpus[row][col] = false;
                hasNewMonster[row][col] = false;
                hasPlayer[row][col] = false;
                playerPath[row][col] = false;
            }
        }
        return tabuleiro;
    }

    private JPanel createLadoDireitoPanel() 
    {
        JPanel ladoDireito = new JPanel();
        ladoDireito.setLayout(new BorderLayout());
    
        JPanel HPpanel = new JPanel(new BorderLayout());
        HPpanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        HPlabel = new JLabel("VIDA:" + health + "/100");
        HPlabel.setHorizontalAlignment(JLabel.CENTER);
        HPpanel.add(HPlabel, BorderLayout.CENTER);
        Font font = HPlabel.getFont();
        HPlabel.setFont(new Font(font.getName(), Font.BOLD, 25)); 
    
        JPanel mochilaPanel = new JPanel(new BorderLayout());
        mochilaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        JLabel mochilaLabel = new JLabel("MOCHILA");
        mochilaLabel.setHorizontalAlignment(JLabel.CENTER);
        mochilaPanel.add(mochilaLabel, BorderLayout.NORTH);
        listaItensLabel = new JLabel("Itens:");
        mochilaPanel.add(listaItensLabel, BorderLayout.CENTER);
    
        JPanel historicoPanel = new JPanel(new BorderLayout());
        historicoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        JLabel historicoLabel = new JLabel("HISTÓRICO");
        historicoLabel.setHorizontalAlignment(JLabel.CENTER);
        historicoPanel.add(historicoLabel, BorderLayout.NORTH);
        
        historicoTextArea = new JTextArea(14, 20);
        historicoTextArea.setEditable(false);
        historicoTextArea.setFocusable(false);
        JScrollPane historicoScrollPane = new JScrollPane(historicoTextArea);
        historicoPanel.add(historicoScrollPane, BorderLayout.CENTER);
    
        JPanel historicoTempoPanel = new JPanel(new BorderLayout());
        
        JPanel timerPanel = new JPanel(new BorderLayout());
        timerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        timerLabel = new JLabel("Tempo: 00:00");
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        timerPanel.add(timerLabel, BorderLayout.CENTER);
        timerPanel.setPreferredSize(new Dimension(300, 50));
        Font fontTimer = timerLabel.getFont();
        timerLabel.setFont(new Font(fontTimer.getName(), Font.PLAIN, 20));

        JPanel debugPanel = new JPanel(new BorderLayout());
        debugPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        debugOption.setFocusable(false);
        debugPanel.add(debugOption, BorderLayout.WEST);
        debugPanel.setPreferredSize(new Dimension(300, 10));
    
        historicoTempoPanel.add(historicoPanel, BorderLayout.NORTH);
        historicoTempoPanel.add(debugPanel, BorderLayout.CENTER);
        historicoTempoPanel.add(timerPanel, BorderLayout.SOUTH);
    
        ladoDireito.add(HPpanel, BorderLayout.NORTH);
        ladoDireito.add(mochilaPanel, BorderLayout.CENTER);
        ladoDireito.add(historicoTempoPanel, BorderLayout.SOUTH);
    
        HPpanel.setPreferredSize(new Dimension(300, 100));
        mochilaPanel.setPreferredSize(new Dimension(300, 150));
        historicoTempoPanel.setPreferredSize(new Dimension(300, 350));
    
        ladoDireito.setBounds(TAMANHO_TABULEIRO * TAMANHO_CASA, 0, 300, TAMANHO_TABULEIRO * TAMANHO_CASA);

        return ladoDireito;
    }
    

    public void initializeGameScreen() 
    {
        setVisible(true);
        requestFocusInWindow();
    }

    private void initializeNewMonster()
    {
        Random random2 = new Random();
        int newMonsterRow, newMosterCol;

        do
        {
            newMonsterRow = random2.nextInt(TAMANHO_TABULEIRO);
            newMosterCol = random2.nextInt(TAMANHO_TABULEIRO);
        } while ((newMonsterRow == 14 && newMosterCol == 0 || hasPoco[newMonsterRow][newMosterCol] || hasWumpus[newMonsterRow][newMosterCol]));

        newMonster = new NewMonster(newMonsterAlive, newMonsterRow, newMosterCol, TAMANHO_TABULEIRO, TAMANHO_CASA, tabuleiro, hasNewMonster, hasPoco, hasMadeira, hasOuro, hasWumpus, hasPlayer, playerPath, health, debugOption);

        newMonster.setPosition(newMonsterRow, newMosterCol);
        hasNewMonster[newMonsterRow][newMosterCol] = true;
    }

    private void initializeWumpus() 
    {
        Random random = new Random();
        int wumpusRow, wumpusCol;

        do 
        {
            wumpusRow = random.nextInt(TAMANHO_TABULEIRO);
            wumpusCol = random.nextInt(TAMANHO_TABULEIRO);
        } while ((wumpusRow == 14 && wumpusCol == 0) || hasPoco[wumpusRow][wumpusCol]);

        wumpus = new Wumpus(wumpusAlive, wumpusRow, wumpusCol, TAMANHO_TABULEIRO, TAMANHO_CASA, tabuleiro, hasWumpus, hasPoco, hasMadeira, hasOuro, hasPlayer, playerPath, hasNewMonster, health, debugOption);

        wumpus.setPosition(wumpusRow, wumpusCol);
        hasWumpus[wumpusRow][wumpusCol] = true;
    }

    public void repaintTabuleiro()
    {
        for (int row = 0; row < TAMANHO_TABULEIRO; row++) 
        {
            for (int col = 0; col < TAMANHO_TABULEIRO; col++) 
            {
                if (!hasPlayer[row][col])
                {
                    JPanel casa = (JPanel) tabuleiro.getComponent(row * TAMANHO_TABULEIRO + col);
                    casa.setBackground(Color.BLACK);
                }
            }
        }
    }

    public void useLanterna() 
    {
        iluminarCima = false;
        iluminarBaixo = false;
        iluminarEsquerda = false;
        iluminarDireita = false;
        temBateria = true;

        addKeyListener(new KeyListener() 
        {
        @Override
        public void keyTyped(KeyEvent e){}
        @Override
        public void keyReleased(KeyEvent e){}

        @Override
        public void keyPressed(KeyEvent e)
        {
            if (temBateria)
            {         
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_UP)
                {
                    iluminarBaixo = false;
                    iluminarDireita = false;
                    iluminarEsquerda = false;
                    iluminarCima = true;
                    temBateria = false;
                }

                else if (keyCode == KeyEvent.VK_DOWN) 
                {
                    iluminarBaixo = true;
                    iluminarDireita = false;
                    iluminarEsquerda = false;
                    iluminarCima = false;
                    temBateria = false;
                }

                else if (keyCode == KeyEvent.VK_LEFT) 
                {
                    iluminarBaixo = false;
                    iluminarDireita = false;
                    iluminarEsquerda = true;
                    iluminarCima = false;
                    temBateria = false;
                }

                else if (keyCode == KeyEvent.VK_RIGHT) 
                {
                    iluminarBaixo = false;
                    iluminarDireita = true;;
                    iluminarEsquerda = false;
                    iluminarCima = false;
                    temBateria = false;
                }
            }

            if (iluminarCima) 
            {
                for (int i = player.row; i >= 0; i--) 
                {
                    JPanel casa = (JPanel) tabuleiro.getComponent(i * TAMANHO_TABULEIRO + player.col);
                    casa.setBackground(Color.WHITE);

                    if (hasPoco[i][player.col])
                    {
                        casa.setBackground(Color.DARK_GRAY);
                    }
                    if (hasOuro[i][player.col])
                    {
                        casa.setBackground(Color.YELLOW);
                    }
                    if (hasMadeira[i][player.col])
                    {
                        casa.setBackground(new Color(139, 69, 19));
                    }
                    if (wumpus.hasWumpus[i][player.col])
                    {
                        casa.setBackground(Color.MAGENTA);
                    }
                    if (newMonster.hasNewMonster[i][player.col])
                    {
                        casa.setBackground(Color.RED);
                    }
                }
            } 

            if (iluminarBaixo) 
            {
                for (int i = player.row; i < TAMANHO_TABULEIRO; i++) 
                {
                    JPanel casa = (JPanel) tabuleiro.getComponent(i * TAMANHO_TABULEIRO + player.col);
                    casa.setBackground(Color.WHITE);
                    if (hasPoco[i][player.col])
                    {
                        casa.setBackground(Color.DARK_GRAY);
                    }
                    if (hasOuro[i][player.col])
                    {
                        casa.setBackground(Color.YELLOW);
                    }
                    if (hasMadeira[i][player.col])
                    {
                        casa.setBackground(new Color(139, 69, 19));
                    }
                    if (wumpus.hasWumpus[i][player.col])
                    {
                        casa.setBackground(Color.MAGENTA);
                    }
                    if (newMonster.hasNewMonster[i][player.col])
                    {
                        casa.setBackground(Color.RED);
                    }
                }
            }

            if (iluminarEsquerda) 
            {
                for (int i = player.col; i >= 0; i--) 
                {
                    JPanel casa = (JPanel) tabuleiro.getComponent(player.row * TAMANHO_TABULEIRO + i);
                    casa.setBackground(Color.WHITE);
                    if (hasPoco[player.row][i])
                    {
                        casa.setBackground(Color.DARK_GRAY);
                    }
                    if (hasOuro[player.row][i])
                    {
                        casa.setBackground(Color.YELLOW);
                    }
                    if (hasMadeira[player.row][i])
                    {
                        casa.setBackground(new Color(139, 69, 19));
                    }
                    if (wumpus.hasWumpus[player.row][i])
                    {
                        casa.setBackground(Color.MAGENTA);
                    }
                    if (newMonster.hasNewMonster[player.row][i])
                    {
                        casa.setBackground(Color.RED);
                    }
                }
            }

            if (iluminarDireita) 
            {
                for (int i = player.col; i < TAMANHO_TABULEIRO; i++) 
                {
                    JPanel casa = (JPanel) tabuleiro.getComponent(player.row * TAMANHO_TABULEIRO + i);
                    casa.setBackground(Color.WHITE);
                    if (hasPoco[player.row][i])
                    {
                        casa.setBackground(Color.DARK_GRAY);
                    }
                    if (hasOuro[player.row][i])
                    {
                        casa.setBackground(Color.YELLOW);
                    }
                    if (hasMadeira[player.row][i])
                    {
                        casa.setBackground(new Color(139, 69, 19));
                    }
                    if (wumpus.hasWumpus[player.row][i])
                    {
                        casa.setBackground(Color.MAGENTA);
                    }
                    if (newMonster.hasNewMonster[player.row][i])
                    {
                        casa.setBackground(Color.RED);
                    }
                }
            }

            iluminarBaixo = false;
            iluminarDireita = false;
            iluminarEsquerda = false;
            iluminarCima = false;
        }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) 
    {
        JPanel casaClicada = (JPanel) e.getSource();
        int componentIndex = tabuleiro.getComponentZOrder(casaClicada);
        int row = componentIndex / TAMANHO_TABULEIRO;
        int col = componentIndex % TAMANHO_TABULEIRO;
        player.dispararFlecha(row, col);
        player.coletarItem(player.row, player.col);
    }
    @Override
    public void mousePressed(MouseEvent e){}
    @Override
    public void mouseReleased(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e){}
}