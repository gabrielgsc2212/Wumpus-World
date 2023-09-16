import java.awt.*;
import javax.swing.*;

public class MenuTitle extends JPanel 
{
    public MenuTitle() 
    {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(100, 0, 0, 0);

        JLabel label1 = new JLabel("Bem vindo ao");
        JLabel label2 = new JLabel("MUNDO DE WUMPUS");

        Font titleFont = new Font("Candara", Font.BOLD, 65);
        label1.setFont(titleFont);
        label2.setFont(titleFont);

        Color titleColor = new Color(65,105,225);
        label1.setForeground(titleColor);
        label2.setForeground(titleColor);

        label1.setHorizontalAlignment(JLabel.CENTER);
        label2.setHorizontalAlignment(JLabel.CENTER);

        add(label1, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 0);
        add(label2, gbc);
    }
}