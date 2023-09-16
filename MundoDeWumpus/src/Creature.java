import javax.swing.*;

public abstract class Creature extends JLabel 
{
    protected int row;
    protected int col;
    protected int TAMANHO_TABULEIRO;
    protected int TAMANHO_CASA;
    protected JPanel tabuleiro;

    public Creature(int row, int col, int TAMANHO_TABULEIRO, int TAMANHO_CASA, JPanel tabuleiro) 
    {
        this.row = row;
        this.col = col;
        this.TAMANHO_TABULEIRO = TAMANHO_TABULEIRO;
        this.TAMANHO_CASA = TAMANHO_CASA;
        this.tabuleiro = tabuleiro; 
    }

    public abstract void mover(int keyCode);

    protected abstract void setPosition(int row, int col);

    protected abstract void limparPosicaoAnterior();

    protected abstract void repaintTabuleiro();

    protected boolean novaPosicaoValida(int newRow, int newCol) 
    {
        return newRow >= 0 && newRow < TAMANHO_TABULEIRO && newCol >= 0 && newCol < TAMANHO_TABULEIRO;
    }
}