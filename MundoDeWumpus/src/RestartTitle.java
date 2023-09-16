import java.awt.*;
import javax.swing.*;

public class RestartTitle extends JPanel
{
      public Player player;
      public Color titleColor;
      public JLabel label;

      public RestartTitle(Player player) 
      {
            this.player = player;
            setLayout(new GridBagLayout());
            setOpaque(false);
      
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(100, 0, 0, 0);
      
                  if (player.checkLoseCondition())
                  {
                        label = new JLabel("DERROTA");
                        titleColor = Color.RED;
                  }

                  if (player.checkWinCondition())
                  {
                        label = new JLabel("VITÓRIA");
                        titleColor = Color.BLUE;
                  }
            
            Font titleFont = new Font("Candara", Font.BOLD, 80);
            label.setFont(titleFont);
            label.setForeground(titleColor);
            label.setHorizontalAlignment(JLabel.CENTER);
  
            add(label, gbc);
      }     
}