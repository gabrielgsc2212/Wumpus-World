import java.awt.*;
import javax.swing.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.awt.event.KeyEvent;

public class Player extends Creature 
{
    private JLabel label;
    private LinkedList<String> mochila;
    private boolean[][] playerPath;
    private boolean[][] hasPlayer;
    private boolean[][] hasPoco;
    private boolean[][] hasWumpus;
    private boolean[][] hasMadeira;
    private boolean[][] hasOuro;
    private boolean[][] hasNewMonster;
    private JLabel mochilaLabel;
    public int health;
    public JLabel HPlabel;
    private boolean encontrado;
    public boolean criaFlecha;
    public boolean wumpusAlive;
    public boolean newMonsterAlive;
    public JTextArea historicoTextArea;
    public Wumpus wumpus;
    public NewMonster newMonster;
    public JCheckBox debugOption;

    public Player(int row, int col, int TAMANHO_TABULEIRO, int TAMANHO_CASA, JPanel tabuleiro, int health, boolean[][] hasPlayer, boolean[][] hasPoco, boolean[][] hasMadeira, boolean[][] hasOuro, JLabel mochilaLabel, boolean[][] hasWumpus, boolean[][] hasNewMonster, JLabel HPlabel, JTextArea historicoTextArea, boolean[][] playerPath, Wumpus wumpus, NewMonster newMonster, JCheckBox debugOption)
    {
        super(row, col, TAMANHO_TABULEIRO, TAMANHO_CASA, tabuleiro);
        this.debugOption = debugOption;
        this.mochilaLabel = mochilaLabel;
        this.hasPoco = hasPoco;
        this.hasMadeira = hasMadeira;
        this.hasOuro = hasOuro;
        this.hasWumpus = hasWumpus;
        this.hasPlayer = hasPlayer;
        this.hasNewMonster = hasNewMonster;
        this.health = health;
        this.HPlabel = HPlabel;
        this.historicoTextArea = historicoTextArea;
        this.playerPath = playerPath;
        this.wumpus = wumpus;
        this.newMonster = newMonster;
        mochila = new LinkedList<>();

        setPosition(row, col);
        setLayout(null);

        label = new JLabel("YOU");
        label.setForeground(Color.BLACK);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 14));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 0, TAMANHO_CASA, TAMANHO_CASA);
        add(label);

        setOpaque(false);
        setPreferredSize(new Dimension(TAMANHO_CASA, TAMANHO_CASA));
    }

    @Override
    public void mover(int keyCode) 
    {
        boolean showMap = debugOption.isSelected();

        int newRow = row;
        int newCol = col;

        if (keyCode == KeyEvent.VK_W) 
        {
            newRow--;
        } 
        else if (keyCode == KeyEvent.VK_S) 
        {
            newRow++;
        } 
        else if (keyCode == KeyEvent.VK_A) 
        {
            newCol--;
        } 
        else if (keyCode == KeyEvent.VK_D) 
        {
            newCol++;
        }

        if (novaPosicaoValida(newRow, newCol)) 
        {

            if (hasPoco[newRow][newCol])
            {
                tampaPoco();
            }
            
            if (hasNewMonster[newRow][newCol])
            {
                health -= 50;
                addToHistorico("Você trombou com o jovem monstro.");
                addToHistorico("Você perdeu 50 de vida!");
                HPlabel.setText("VIDA: " + health + "/100");
            }

            limparPosicaoAnterior();
            setPosition(newRow, newCol);
            coletarItem(newRow, newCol);
            
            if (showMap)
            {
                  for (int row = 0; row < TAMANHO_TABULEIRO; row++) 
                  {
                        for (int col = 0; col < TAMANHO_TABULEIRO; col++) 
                        {
                            JPanel casa = (JPanel) tabuleiro.getComponent(row * TAMANHO_TABULEIRO + col);
                            casa.setBackground(Color.WHITE);
                            if (hasOuro[row][col])
                            {
                                casa.setBackground(Color.YELLOW);
                            }
                            else if (hasPoco[row][col])
                            {
                                casa.setBackground(Color.DARK_GRAY);
                            }
                            else if (hasMadeira[row][col])
                            {
                                casa.setBackground(new Color(139, 69, 19));
                            }
                            else if (hasWumpus[row][col])
                            {
                                casa.setBackground(Color.MAGENTA);
                            }
                            else if (hasNewMonster[row][col])
                            {
                                casa.setBackground(Color.RED);
                            }
                            else if (hasPlayer[row][col])
                            {
                                casa.setBackground(Color.CYAN);
                            }
                            else
                            {
                                casa.setBackground(Color.WHITE);
                            }
                        }
                  }
            }
            else
            {
                repaintTabuleiro();
            }
        } 
    }

    public void tampaPoco()
    {
        String madeira = "MADEIRA";
        encontrado = false;

        Iterator<String> iterator = mochila.iterator();
        while (iterator.hasNext())
        {
            String item = iterator.next();
            if (item.equals(madeira))
            {
                encontrado = true;
                break;
            }
        }

        if (encontrado)
        {
            hasPoco[row][col] = false;
            mochila.remove("MADEIRA");
            addToHistorico("Você usou MADEIRA para não cair no poço.");
        }
        else
        {
            health -= 100;
        }
    }

    public boolean checkWinCondition()
    {
        String gold = "OURO";
        encontrado = false;

        Iterator<String> iterator = mochila.iterator();
        while (iterator.hasNext())
        {
            String item = iterator.next();
            if (item.equals(gold))
            {
                encontrado = true;
                break;
            }
        }

        return encontrado && hasPlayer[TAMANHO_TABULEIRO - 1][0];
    }

    @Override
    public void limparPosicaoAnterior()
    {
        JPanel casaAnterior = (JPanel) tabuleiro.getComponent(row * TAMANHO_TABULEIRO + col);
        hasPlayer[row][col] = false;
        playerPath[row][col] = true;
        casaAnterior.setBackground(Color.WHITE);
    }

    @Override
    public void setPosition(int row, int col) 
    {
        this.row = row;
        this.col = col;
        JPanel casaAtual = (JPanel) tabuleiro.getComponent(row * TAMANHO_TABULEIRO + col);
        casaAtual.setBackground(Color.CYAN);
        casaAtual.add(this);
        casaAtual.revalidate();
        hasPlayer[row][col] = true;
    }

    public boolean checkLoseCondition()
    {
        int row = getPlayerRow();
        int col = getPlayerCol();

        return hasWumpus[row][col] || health <= 0;
    }

    public void coletarItem(int row, int col) 
    {
        if (mochila.size() < 3) 
        {
            if (hasMadeira[row][col]) 
            {
                mochila.add("MADEIRA");
                hasMadeira[row][col] = false;
                addToHistorico("Você coletou: Madeira!");
            } 
            else if (hasOuro[row][col]) 
            {
                mochila.add("OURO");
                hasOuro[row][col] = false;
                addToHistorico("Você coletou: Ouro!");
            }
            String itens = String.join(", ", mochila);
            mochilaLabel.setText("Itens: " + itens);
        }
    }

    @Override
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
                    if (playerPath[row][col])
                    {
                        casa.setBackground(Color.WHITE);
                    }
                }
            }
        }
    }

    public void addToHistorico(String texto) 
    {
        historicoTextArea.append(texto + "\n");
        historicoTextArea.setCaretPosition(historicoTextArea.getDocument().getLength());
    }

    public void dispararFlecha(int row, int col)
    {
        String flecha = "MADEIRA";
        encontrado = false;

        Iterator<String> iterator = mochila.iterator();
        while (iterator.hasNext())
        {
            String item = iterator.next();
            if (item.equals(flecha))
            {
                criaFlecha = true;
                break;
            }
        }

        if (criaFlecha)
        {
            int playerRow = getPlayerRow();
            int playerCol = getPlayerCol();

            if ( row <= playerRow + 2  && row >= playerRow - 2) 
            {
                if ( col <= playerCol + 2 && col >= playerCol - 2)
                {
                    if (novaPosicaoValida(row, col)) 
                    {
                        if (hasWumpus[row][col]) 
                        {
                            hasWumpus[row][col] = false;
                            wumpus.wumpusAlive = false;
                            addToHistorico("Você disparou uma flecha e matou o Wumpus!");
                            mochila.remove(flecha);
                            criaFlecha = false;
                            coletarItem(row, col);
                            return;
                        } 
                        else if (hasNewMonster[row][col])
                        {
                            hasNewMonster[row][col] = false;
                            newMonster.newMonsterAlive = false;
                            addToHistorico("Você disparou uma flecha e matou o monstro jovem!");
                            mochila.remove(flecha);
                            criaFlecha = false;
                            coletarItem(row, col);
                            return;
                        }
                        else
                        {
                            addToHistorico("Você disparou uma flecha, mas não acertou nenhum monstro.");
                            mochila.remove(flecha);
                            criaFlecha = false;
                            coletarItem(row, col);
                            return;    
                        }
                    }
                }
            }
            else
            {
                addToHistorico("Posição fora do alcance do arco.");
            }
            criaFlecha = false;
        }
        else
        {
            addToHistorico("Você não possui madeira para disparar uma flecha.");
        }
    }

    private int getPlayerRow() 
    {
        int row = 0;
        for(int i = 0; i < TAMANHO_TABULEIRO; i++) 
        {
            for(int j = 0; j < TAMANHO_TABULEIRO; j++) 
            {
                if(hasPlayer[i][j]) 
                {
                    row = i;
                    break;
                }
            }
        }
        return row;
    }

    private int getPlayerCol() 
    {
        int col = 0;
        for(int i = 0; i < TAMANHO_TABULEIRO; i++) 
        {
            for(int j = 0; j < TAMANHO_TABULEIRO; j++) 
            {
                if(hasPlayer[i][j]) 
                {
                    col = j;
                    break;
                }
            }
        }
        return col;
    }
}