import java.awt.*;
import javax.swing.*;

public class MenuRestart 
{
      private static JFrame frame;
      private static RestartTitle restartTitle;
      private static RestartButtons restartButtons;
      public Player player;

      public MenuRestart(Player player) 
      {
            this.player = player;

            MenuBackGround background = new MenuBackGround();

            if (player.checkWinCondition())
            {
                  frame = new JFrame("VOCÊ GANHOU");
            }
            
            if (player.checkLoseCondition())
            {
                  frame = new JFrame("VOCÊ PERDEU");
            }

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(918, 640));
            frame.setResizable(false);

            restartButtons = new RestartButtons();
            restartTitle = new RestartTitle(player);

            background.setLayout(new BorderLayout());
            background.add(restartTitle, BorderLayout.NORTH);
            background.add(restartButtons, BorderLayout.CENTER);

            frame.add(background);
            frame.pack();
            frame.setVisible(true);
      }
    
      public static void hideScreen() 
      {
            frame.setVisible(false);
      }
}