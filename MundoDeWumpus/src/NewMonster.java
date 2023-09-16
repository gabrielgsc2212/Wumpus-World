import java.awt.*;
import java.util.Random;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class NewMonster extends Creature 
{
      private JLabel label;
      private boolean[][] hasPoco;
      private boolean[][] hasMadeira;
      private boolean[][] hasOuro;
      private boolean[][] hasWumpus;
      public boolean[][] hasNewMonster;
      private boolean[][] hasPlayer;
      private boolean[][] playerPath;
      public boolean newMonsterAlive;
      private Random moveRandom = new Random();
      public int health;
      public JCheckBox debugOption;

      public NewMonster(boolean newMonsterAlive, int row, int col, int TAMANHO_TABULEIRO, int TAMANHO_CASA, JPanel tabuleiro, boolean[][] hasNewMonster, boolean[][] hasPoco, boolean[][] hasMadeira, boolean[][] hasOuro, boolean[][] hasWumpus, boolean[][] hasPlayer, boolean[][] playerPath, int health, JCheckBox debugOption) 
      {
            super(row, col, TAMANHO_TABULEIRO, TAMANHO_CASA, tabuleiro);
            this.debugOption = debugOption;
            this.hasPoco = hasPoco;
            this.hasWumpus = hasWumpus;
            this.hasMadeira = hasMadeira;
            this.hasOuro = hasOuro;
            this.hasNewMonster = hasNewMonster;
            this.hasPlayer = hasPlayer;
            this.playerPath = playerPath;
            this.newMonsterAlive = newMonsterAlive;
            this.health = health;
            setPosition(row, col);
            setLayout(null);
            label = new JLabel("MONSTRO");
            label.setForeground(Color.BLACK);
            label.setFont(new Font(label.getFont().getName(), Font.BOLD, 9));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setBounds(0, 0, TAMANHO_CASA, TAMANHO_CASA);
            add(label);
            setBackground(Color.RED);
            setOpaque(false);
            setPreferredSize(new Dimension(TAMANHO_CASA, TAMANHO_CASA));
      }

      @Override
      public void mover(int keyCode)
      {
            boolean showMap = debugOption.isSelected();
            limparPosicaoAnterior();
            if (!newMonsterAlive)
            {
                  setVisible(false);
                  return;
            }
            else
            {
                  int newRol, newCol;

                  do
                  {
                        newRol = row;
                        newCol = col;

                        int direction = moveRandom.nextInt(9);

                        if (direction == 0 && newCol < TAMANHO_TABULEIRO - 1 && newRol < TAMANHO_TABULEIRO - 1)
                        {
                              newCol += 2;
                              newRol += 1;
                        }
                        if (direction == 1 && newCol < TAMANHO_TABULEIRO - 1 && newRol > 0)
                        {
                              newCol += 2;
                              newRol -= 1;
                        }
                        if (direction == 2 && newCol > 0 && newRol < TAMANHO_TABULEIRO - 1)
                        {
                              newCol -= 2;
                              newRol += 1;
                        }
                        if (direction == 3 && newCol > 0 && newRol > 0)
                        {
                              newCol -= 2;
                              newRol -= 1;
                        }
                        if (direction == 4 && newCol < TAMANHO_TABULEIRO - 1 && newRol < TAMANHO_TABULEIRO - 1)
                        {
                              newCol += 1;
                              newRol += 2;
                        }
                        if (direction == 5 && newCol < TAMANHO_TABULEIRO - 1 && newRol > 0)
                        {
                              newCol += 1;
                              newRol -= 2;
                        }
                        if (direction == 6 && newCol > 0 && newRol < TAMANHO_TABULEIRO - 1)
                        {
                              newCol -= 1;
                              newRol += 2;
                        }
                        if (direction == 7 && newCol > 0 && newRol > 0)
                        {
                              newCol -= 1;
                              newRol -= 2;
                        }
                        if (direction == 8){}
                        //Se cair em 8, não irá se mover
                        
                  }while (!novaPosicaoValida(newRol, newCol) || hasPoco[newRol][newCol] || hasWumpus[newRol][newCol]);

                  setPosition(newRol, newCol);
                  if (showMap)
                  {
                        for (int row = 0; row < TAMANHO_TABULEIRO; row++) 
                        {
                              for (int col = 0; col < TAMANHO_TABULEIRO; col++) 
                              {
                                    JPanel casa = (JPanel) tabuleiro.getComponent(row * TAMANHO_TABULEIRO + col);
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

      @Override
      public void limparPosicaoAnterior()
      {
            hasNewMonster[row][col] = false;
            JPanel casaAnterior = (JPanel) tabuleiro.getComponent(row * TAMANHO_TABULEIRO + col);

            if (hasMadeira[row][col])
            {
                  casaAnterior.setBackground(new Color(139, 69, 19));
            }
            else if (hasOuro[row][col])
            {
                  casaAnterior.setBackground(Color.YELLOW);
            }
            else
            {
                  casaAnterior.setBackground(Color.BLACK);
            }

            if (!newMonsterAlive)
            {
                  casaAnterior.setBackground(Color.WHITE);
            }
      }

      @Override
      public void setPosition(int row, int col) 
      {
            this.row = row;
            this.col = col;
            JPanel casaAtual = (JPanel) tabuleiro.getComponent(row * TAMANHO_TABULEIRO + col);
            casaAtual.add(this);
            casaAtual.revalidate();            
            casaAtual.setBackground(Color.RED);
            hasNewMonster[row][col] = true;
            if (playerPath[row][col])
            {
                  playerPath[row][col] = false;                  
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
}