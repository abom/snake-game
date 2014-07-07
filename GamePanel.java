import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

public class GamePanel extends JPanel {
    Snake snake;
    Rat rat;

    String message;
    boolean drawMessage;
    // with specific parts
    public GamePanel() {
        setSize(Config.panelSize);
        setBounds(0, 0, Config.panelWidth, Config.panelHeight);
    
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.ORANGE);

        drawMessage = false;
        message = "";
    }

    public Snake getSnake() {
        return snake;
    }

    public Rat getRat() {
        return rat;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
        setSnakeBorders();
    }

    public void setRat(Rat rat) {
        this.rat = rat;
        snake.setRat(rat);
    }

    public void setSnakeBorders() {
        snake.setBorders(new Rectangle2D.Double(0.0, 0.0, getWidth(), getHeight()));
    }

    public void showMessage(String message) {
        drawMessage = true;
        this.message = message;
        repaint();
    }

    public void removeMessage() {
        drawMessage = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        if(drawMessage)
        {

            g2d.setFont(g2d.getFont().deriveFont(48f));
            g2d.drawString(message, 10, getHeight() / 2);
            return;
        }

        snake.draw(g2d);
        rat.draw(g2d);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Config.panelWidth, Config.panelHeight);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }    

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

}