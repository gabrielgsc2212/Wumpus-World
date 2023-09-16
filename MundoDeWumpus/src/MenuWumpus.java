import java.awt.*;
import javax.swing.*;

public class MenuWumpus
{
    private static JFrame frame;
    private static MenuTitle titlePanel;
    private static MenuButtons buttonPanel;

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() 
    {
        frame = new JFrame("Bem vindo ao MUNDO DE WUMPUS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(918, 640));
        frame.setResizable(false);

        MenuBackGround background = new MenuBackGround();
        titlePanel = new MenuTitle();
        buttonPanel = new MenuButtons();

        background.setLayout(new BorderLayout());

        background.add(titlePanel, BorderLayout.NORTH);
        background.add(buttonPanel, BorderLayout.CENTER);

        frame.add(background);
        frame.pack();
        frame.setVisible(true);
    }

    public static void hideMenu() 
    {
        frame.setVisible(false);
    }
}