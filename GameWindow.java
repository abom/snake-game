import java.util.Formatter;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class GameWindow extends JFrame implements WindowListener, KeyListener, SnakeAction {
	private GamePanel gamePanel;
    private JPanel statusPanel;

    private Snake gameSnake;
    private Rat gameRat;

    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JLabel speedLabel;

    // for timer and user controls
    private Timer countTimer, gameTimer;
    private Direction currentDirection, lastDirection;

    private int speed, score, elapsed, counts;
    private boolean gameStarted, messageRepeat;


    private JRadioButtonMenuItem lowSpeed;
    private JRadioButtonMenuItem mediumSpeed;
    private JRadioButtonMenuItem highSpeed;

	public GameWindow() {
        setResizable(false);
        setIconImage((new ImageIcon("art/snake_head.png").getImage()));
        // it's better to be have a fixed size :D
        //setResizable(false);
		// for key press
        addKeyListener(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Snake Game");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS)); // new FlowLayout());

        initGamePanel();
        initStatusPanel();
        initMenuBar();

        countTimer = new Timer(800, new CountTimerHandle());
        gameTimer = new Timer(1, new GameTimerHandle());

        setSpeed(150);


        pack();
        setSize(getWidth() + 30, getHeight());
        setLocationRelativeTo(null);

        reset();
    }

    public void initGamePanel() {
        // init game panel
        gamePanel = new GamePanel();        
        add(gamePanel);
    }

    public void initStatusPanel() {
        // init status panel
        statusPanel = new JPanel();
        scoreLabel = new JLabel("0 x ",
                                new ImageIcon("art/rat.png"),
                                JLabel.CENTER);

        scoreLabel.setHorizontalTextPosition(JLabel.LEFT);
        scoreLabel.setVerticalTextPosition(JLabel.CENTER);

        timeLabel = new JLabel("00:00", JLabel.CENTER);
        speedLabel = new JLabel("Medium", JLabel.CENTER);

        statusPanel.setLayout(new GridLayout(8, 1));//(new BoxLayout(statusPanel, BoxLayout.Y_AXIS)); // new FlowLayout()); //
        statusPanel.add(new JLabel("Score", JLabel.CENTER));
        statusPanel.add(scoreLabel);
        statusPanel.add(new JLabel("________", JLabel.CENTER));
        statusPanel.add(new JLabel("Time", JLabel.CENTER));
        statusPanel.add(timeLabel);   
        statusPanel.add(new JLabel("________", JLabel.CENTER));     
        statusPanel.add(new JLabel("Speed", JLabel.CENTER));
        statusPanel.add(speedLabel);
        add(statusPanel);
    }

    public void initMenuBar() {
        JMenuBar menuBar = new JMenuBar ();
        JMenu gameMenu = new JMenu("Game");
        JMenu speedMenu = new JMenu("Speed");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem newGameItem = new JMenuItem("New game"); //, icon);
        JMenuItem exitItem = new JMenuItem("Exit");
        
        ButtonGroup speedGroup = new ButtonGroup();
        lowSpeed = new JRadioButtonMenuItem("Low");
        lowSpeed.setActionCommand("250");
        mediumSpeed = new JRadioButtonMenuItem("Medium");
        mediumSpeed.setActionCommand("150");
        highSpeed = new JRadioButtonMenuItem("High");
        highSpeed.setActionCommand("50");
        mediumSpeed.setSelected(true);

        speedGroup.add(lowSpeed);
        speedGroup.add(mediumSpeed);
        speedGroup.add(highSpeed);

        JMenuItem showHelpItem = new JMenuItem("How to play");
        JMenuItem aboutItem = new JMenuItem("About"); 

        newGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                startNewGame();
            }
        });


        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                exitGame();
            }
        });

        showHelpItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null, "The following keyboard keys:\n\n - Enter: start a new game\n - Space: pause current game\n - Escape: stop current game\n - Arrows: turn", "How to play", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null, "Snake Game\nVersion: 0.01\nCoding and Artwork:\n - Sherif Adel\n - Mahmoud Sayed\n - Abdelrahman Ghanem", "About the game", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        SpeedAction speedAction = new SpeedAction();

        lowSpeed.addActionListener(speedAction);
        mediumSpeed.addActionListener(speedAction);
        highSpeed.addActionListener(speedAction);

        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        speedMenu.add(lowSpeed);
        speedMenu.add(mediumSpeed);
        speedMenu.add(highSpeed);

        helpMenu.add(showHelpItem);
        helpMenu.add(aboutItem);

        menuBar.add(gameMenu);
        menuBar.add(speedMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
        newGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                reset();
            }
        });
    }

    private class SpeedAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            // don't change while playing
            //if(gameStarted)
            //    return;

            setSpeed(Integer.parseInt(event.getActionCommand()));
            String speedText = ((JRadioButtonMenuItem) event.getSource()).getText();
                
            if(speedLabel == null)
                return;
                
            speedLabel.setText(speedText);
        }
    }

    public void reset() {
        gameSnake = new Snake();
        gameSnake.setSnakeActionListener(this);
        // move to center
        gameSnake.moveTo(Config.snakeX, Config.snakeY);

        gameRat = new Rat(Config.panelWidth, Config.panelHeight);
        gameRat.escapeFrom(gameSnake);

        gamePanel.setSnake(gameSnake);
        gamePanel.setRat(gameRat);

        score = 0;
        elapsed = 0;
        counts = 3;
        gameStarted = false;
        messageRepeat = false;

        scoreLabel.setText("0 x ");
        updateElapsedTime();

        setFocusable(true);

        if(!gameStarted)
            countTimer.start();
    }

    public void setSpeed(int s) {
        speed = s;
        if(gameTimer == null)
            return;
        gameTimer.setDelay(speed);
    }

    public void start() {
        gameStarted = true;
        countTimer.start();
    }

    public void stop() {
        gameTimer.stop();
        gameStarted = false;
        reset();
    }

    public void pause() {
        if(!gameStarted)
            return;

        if(gameTimer.isRunning())
            gameTimer.stop();
        else
            gameTimer.start();
    }

    public void startNewGame() {
        reset();
        start();
    }


    public void updateElapsedTime() {
        elapsed += speed;
        // http://stackoverflow.com/questions/266825/how-to-format-a-duration-in-java-e-g-format-hmmss
        timeLabel.setText(toTime(elapsed));
    }

    public String toTime(int msecs) {
        int secs = msecs / 1000;
        return String.format("%02d:%02d", secs / 60, secs % 60);
    }


    public void exitGame() {
        countTimer.stop();
        dispose();
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }    

    // to stop timers when closed
    @Override
    public void windowClosing(WindowEvent event) {
        countTimer.stop();
        gameTimer.stop();
    }    

    @Override
    public void windowClosed(WindowEvent event) {
    }

    @Override
    public void windowOpened(WindowEvent event) {
    }

    @Override
    public void windowIconified(WindowEvent event) {
    }

    @Override
    public void windowDeiconified(WindowEvent event) {
    }

    @Override
    public void windowActivated(WindowEvent event) {
    }

    @Override
    public void windowDeactivated(WindowEvent event) {
    }

    @Override
    public void keyPressed(KeyEvent event) {
        currentDirection = gameSnake.getDirection();
        Direction d = currentDirection;

        switch(event.getKeyCode()) {
            case KeyEvent.VK_UP:
                d = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
                d = Direction.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                d = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                d = Direction.RIGHT;
                break;
            case KeyEvent.VK_SPACE:
                pause();
                break;
            case KeyEvent.VK_ENTER:
                if(!gameStarted)
                    startNewGame();
                break;
            case KeyEvent.VK_ESCAPE:
                if(gameStarted)
                    stop();
                break;
            default:
                d = currentDirection;
                break;
        }

        // return if the new direction is the same or reverse direction
        if(d == currentDirection || d == gameSnake.reverseDirection(currentDirection))
            return;
        else
        {
            // this would response faster to user
            // actions instead of waiting for timer
            // to take the step
            // and make the timer step
            // if it's in the same direction only
            gameSnake.setDirection(d);
            gameSnake.step();
        }

        // step after would make two steps :D
        // but if timer iterval is slow
        // it would step later
        // gameSnake.step();
    }

    @Override
    public void keyReleased(KeyEvent event) {

    } 

    @Override
    public void snakeHitsRat() {
        gameRat.escapeFrom(gameSnake);
        gameSnake.addNormalPart();
        scoreLabel.setText(++score + " x");
    }

    @Override
    public void snakeHitsItself() {
        stop();
        JOptionPane.showMessageDialog(this, "Game Over!", "Try Again!", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void snakeHitsBorders() {
        stop();
        JOptionPane.showMessageDialog(this, "Game Over!", "Try Again!", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void partOutOfBorders(SnakePart part) {

    }

    private class CountTimerHandle implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(!gameStarted) {
                if(messageRepeat)
                    gamePanel.showMessage("Press Enter to Play!");
                else
                    gamePanel.showMessage("");
                messageRepeat = !messageRepeat;
            }
            else {
                if(counts == 0) {
                    gamePanel.removeMessage();
                    gameTimer.start();
                    countTimer.stop();
                    return;
                }
                gamePanel.showMessage(Integer.toString(counts));
                counts--;
            }
            
        }
    }

    private class GameTimerHandle implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            // check for last direction then step
            // if don't step if changed
            // and call step() on the key event?
            // this is way

            // the next way have some delay, because the next step
            // would wait the the timer to do
            updateElapsedTime();

            if(lastDirection == gameSnake.getDirection())
                gameSnake.step();
            lastDirection = gameSnake.getDirection();

            gamePanel.repaint();
        }
    }
}