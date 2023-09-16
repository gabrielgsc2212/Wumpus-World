import java.awt.*;
import java.util.Random;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Wumpus extends Creature
{
      private JLabel label;
      private JLabel labelFedor;
      private JPanel fedorCasa;
      private boolean[][] hasPoco;
      private boolean[][] hasMadeira;
      private boolean[][] hasOuro;
      public boolean[][] hasWumpus;
      private boolean[][] hasPlayer;
      private boolean[][] hasNewMonster;
      private boolean[][] playerPath;
      public boolean wumpusAlive; 
      private Random moveRandom = new Random();
      public JCheckBox debugOption;
      public int health;

      public Wumpus(boolean wumpusAlive, int row, int col, int TAMANHO_TABULEIRO, int TAMANHO_CASA, JPanel tabuleiro, boolean[][] hasWumpus, boolean[][] hasPoco, boolean[][] hasMadeira, boolean[][] hasOuro, boolean[][] hasPlayer, boolean[][] playerPath, boolean[][] hasNewMonster, int health, JCheckBox debugOption) 
      {
            super(row, col, TAMANHO_TABULEIRO, TAMANHO_CASA, tabuleiro);
            this.debugOption = debugOption;
            this.hasPoco = hasPoco;
            this.hasMadeira = hasMadeira;
            this.hasOuro = hasOuro;
            this.hasWumpus = hasWumpus;
            this.hasPlayer = hasPlayer;
            this.playerPath = playerPath;
            this.hasNewMonster = hasNewMonster;
            this.wumpusAlive = true;
            this.health = health;

            setPosition(row, col);
            setLayout(null);

            label = new JLabel("WUMPUS");
            label.setForeground(Color.BLACK);
            label.setFont(new Font(label.getFont().getName(), Font.BOLD, 8));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setBounds(0, 0, TAMANHO_CASA, TAMANHO_CASA);
            label.setBackground(Color.MAGENTA);
            add(label);

            setOpaque(false);
            setPreferredSize(new Dimension(TAMANHO_CASA, TAMANHO_CASA));
            removeFedor(row + 1, col);
            removeFedor(row - 1, col);
            removeFedor(row, col + 1);
            removeFedor(row, col - 1);
      }

      @Override
      public void mover(int keyCode) 
      {
            boolean showMap = debugOption.isSelected();

            limparPosicaoAnterior();
            if (wumpusAlive == false)
            {
                  setVisible(false);
                  return;
            }
            else
            {
                  if (hasPlayer[row][col])
                  {
                        health -= 100;
                  }

                  int newRow, newCol;

                  do 
                  {
                        newRow = row;
                        newCol = col;
                  
                        int direction = moveRandom.nextInt(4);
                  
                        if (direction == 0 && newCol < TAMANHO_TABULEIRO - 1) 
                        {
                              newCol++;
                        } 
                        else if (direction == 1 && newCol > 0) 
                        {
                              newCol--;
                        } 
                        else if (direction == 2 && newRow > 0) 
                        {
                              newRow--;
                        } 
                        else if (direction == 3 && newRow < TAMANHO_TABULEIRO - 1) 
                        {
                              newRow++;
                        }
                  } while (!novaPosicaoValida(newRow, newCol) || hasPoco[newRow][newCol] || hasNewMonster[newRow][newCol]);

                  if (hasPlayer[newRow][newCol])
                  {
                        setPosition(newRow, newCol);
                        health -= 100;
                  }
                  else
                  {
                        setPosition(newRow, newCol);
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
      }

      @Override
      public void limparPosicaoAnterior()
      {
            hasWumpus[row][col] = false;
            JPanel casaAnterior = (JPanel) tabuleiro.getComponent(row * TAMANHO_TABULEIRO + col);
            removeFedor(row + 1, col);
            removeFedor(row - 1, col);
            removeFedor(row, col + 1);
            removeFedor(row, col - 1);

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

            if (!wumpusAlive)
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
                  hasWumpus[row][col] = true;
                  casaAtual.add(this);
                  casaAtual.revalidate();            
                  casaAtual.setBackground(Color.MAGENTA);
                  if (playerPath[row][col])
                  {
                        playerPath[row][col] = false;                  
                  }

                  addFedor(row + 1, col);
                  addFedor(row - 1, col);
                  addFedor(row, col + 1);
                  addFedor(row, col - 1);
      }

      private void addFedor(int row, int col) 
      {
            if (row >= 0 && row < TAMANHO_TABULEIRO && col >= 0 && col < TAMANHO_TABULEIRO) 
            {
                  fedorCasa = (JPanel) tabuleiro.getComponent(row * TAMANHO_TABULEIRO + col);
                  labelFedor = new JLabel("Fedor!");
                  labelFedor.setForeground(Color.BLACK);
                  fedorCasa.add(labelFedor);
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

      private void removeFedor(int row, int col)
      {
            if (row >= 0 && row < TAMANHO_TABULEIRO && col >= 0 && col < TAMANHO_TABULEIRO) 
            {
                  JPanel casa = (JPanel) tabuleiro.getComponent(row * TAMANHO_TABULEIRO + col);
                  Component[] components = casa.getComponents();
                  for (Component component : components) 
                  {
                        if (component instanceof JLabel) 
                        {
                              JLabel label = (JLabel) component;
                              if (label.getText().equals("Fedor!")) 
                              {
                                    casa.remove(label);
                                    casa.revalidate();
                                    casa.repaint();
                                    break;
                              }
                        }
                  }
            }
      }
}