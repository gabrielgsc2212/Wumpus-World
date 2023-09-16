import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RestartButtons extends JPanel
{
        public RestartButtons() 
        {
            setLayout(new GridBagLayout());
            setOpaque(false);
    
            JButton playAgain = new JButton("Jogar novamente");
            JButton sairButton = new JButton("Sair");
    
            Font buttonFont = playAgain.getFont().deriveFont(Font.BOLD, 24);
            playAgain.setFont(buttonFont);
            sairButton.setFont(buttonFont);
    
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.insets = new Insets(0, 0, 20, 0);
    
            add(playAgain, constraints);
    
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
    
            playAgain.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    MenuRestart.hideScreen();
                    SwingUtilities.invokeLater(() -> {
                        GameScreen gameScreen = new GameScreen();
                        gameScreen.initializeGameScreen();
                    });
                }
            });
      }     
}