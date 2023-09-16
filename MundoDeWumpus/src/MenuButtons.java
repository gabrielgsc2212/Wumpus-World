import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MenuButtons extends JPanel 
{
    public GameScreen gameScreen;

    public MenuButtons() 
    {
        setLayout(new GridBagLayout());
        setOpaque(false);

        JButton jogarButton = new JButton("Jogar");
        JButton sairButton = new JButton("Sair");

        Font buttonFont = jogarButton.getFont().deriveFont(Font.BOLD, 24);
        jogarButton.setFont(buttonFont);
        sairButton.setFont(buttonFont);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 20, 0);

        add(jogarButton, constraints);

        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        add(sairButton, constraints);

        sairButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                System.exit(0);
            }
        });

        jogarButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                MenuWumpus.hideMenu();
                SwingUtilities.invokeLater(() -> {
                    gameScreen = new GameScreen();
                    gameScreen.initializeGameScreen();
                });
            }
        });
    }
}