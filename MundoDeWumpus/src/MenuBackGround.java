import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class MenuBackGround extends JPanel 
{
    private BufferedImage backgroundImage;

    public MenuBackGround() 
    {
        setPreferredSize(new Dimension(900, 600));
        setLayout(new BorderLayout());

        try 
        {
            backgroundImage = ImageIO.read(getClass().getResource("menu_bg.png"));
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        if (backgroundImage != null) 
        {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}