package ru.game.snake.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import ru.game.snake.obj.Frog;
import ru.game.snake.obj.Snake;

public class GameWindow extends javax.swing.JFrame implements Observer, Observable {

    private List<Observer> observers = new LinkedList<>();
    private boolean playPressed;
    private final int numOfXCells;
    private final int numOfYCells;
    private int score, frogsEaten, snakeLength, maxSnakeLength;
    private final GameBoard board;
    private final Color backgroundColor = Color.black;
    private final Color gridColor = new Color(0, 150, 50, 100); //semitransparent dark green
    private Image offScrImg;
    private Graphics offScrGraph;
    private final Image formIcon = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/ru/game/snake/img/logo.png"));

    public GameWindow(GameBoard board) {
        initComponents();
        this.board = board;
        numOfXCells = board.numOfXCells;
        numOfYCells = board.numOfYCells;
        score = 0;
        frogsEaten = 0;
        snakeLength = board.getSnake().size();
        maxSnakeLength = snakeLength;
        jLabel6.setText(String.valueOf(snakeLength));
        setTitle("Snake Game (" + board.numOfGreenFrogs + " green, " + board.numOfRedFrogs + " red, " + board.numOfBlueFrogs + " blue frogs)");
        setSize(30 * numOfXCells, 30 * numOfYCells);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Resets main variables and redraws all objects after last gameplay.
     */
    private void initNewGame() {
        playPressed = false;
        jButton1.setText("Start");
        jButton2.setEnabled(false);
        score = 0;
        frogsEaten = 0;
        snakeLength = board.getSnake().size();
        maxSnakeLength = snakeLength;
        jLabel2.setText("0");
        jLabel4.setText("0");
        jLabel6.setText(String.valueOf(snakeLength));
        drawGrid();
        drawSnake();
        drawAllFrogs();
    }

    /**
     * Draws a gameboard by filling a rectangle and drawing lines<br>
     * with appropriate colors.
     */
    private void drawGrid() {
        //background
        offScrGraph.setColor(backgroundColor);
        offScrGraph.fillRect(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        //grid
        offScrGraph.setColor(gridColor);
        for (int i = 1; i < numOfXCells; i++) {
            int x = i * jPanel1.getWidth() / numOfXCells;
            offScrGraph.drawLine(x, 0, x, jPanel1.getHeight());
        }
        for (int j = 1; j < numOfYCells; j++) {
            int y = j * jPanel1.getHeight() / numOfYCells;
            offScrGraph.drawLine(0, y, jPanel1.getWidth(), y);
        }

        jPanel1.getGraphics().drawImage(offScrImg, 0, 0, jPanel1);
    }

    /**
     * Draws a snake.
     * <p>
     * <b>x</b> - <i>X</i> coordinate of current cell of snake;<br>
     * <b>y</b> - <i>Y</i> coordinate of current cell of snake;
     * <p>
     * width and height of each appropriate cell of field are being calculated
     * for each cell of snake:<br>
     * <i>jPanel1.getWidth() / numOfXCells;</i>
     * <p>
     * calculations of oval start position, its width and height:<br>
     * <i>offScrGraph.fillOval(<br>
     * x + X offset from top left corner of current cell of field,<br>
     * y + Y offset from top left corner of current cell of field,<br>
     * width of current cell of field divided by some coefficient,<br>
     * height of current cell of field divided by some coefficient<br> );
     */
    private void drawSnake() {
        Snake snake = board.getSnake();
        offScrGraph.setColor(Color.yellow);
        //head
        int x = snake.xOfCell(0) * jPanel1.getWidth() / numOfXCells;
        int y = snake.yOfCell(0) * jPanel1.getHeight() / numOfYCells;
        offScrGraph.fillOval(x + jPanel1.getWidth() / numOfXCells / 4, y + jPanel1.getHeight() / numOfYCells / 4, jPanel1.getWidth() / numOfXCells / 2, jPanel1.getHeight() / numOfYCells / 2);
        //body
        for (int i = 1; i < snake.size() - 1; i++) {
            x = snake.xOfCell(i) * jPanel1.getWidth() / numOfXCells;
            y = snake.yOfCell(i) * jPanel1.getHeight() / numOfYCells;
            offScrGraph.fillOval(x + jPanel1.getWidth() / numOfXCells / 3, y + jPanel1.getHeight() / numOfYCells / 3, jPanel1.getWidth() / numOfXCells / 3, jPanel1.getHeight() / numOfYCells / 3);
        }
        //tail
        x = snake.xOfCell(snake.size() - 1) * jPanel1.getWidth() / numOfXCells;
        y = snake.yOfCell(snake.size() - 1) * jPanel1.getHeight() / numOfYCells;
        offScrGraph.fillOval(x - 2 + jPanel1.getWidth() / numOfXCells / 2, y - 2 + jPanel1.getHeight() / numOfYCells / 2, 4, 4);

        jPanel1.getGraphics().drawImage(offScrImg, 0, 0, jPanel1);
    }

    /**
     * Erases a snake by filling its current position with background color,<br>
     * so the snake could be drawn on its next move.
     */
    private void eraseSnake() {
        Snake snake = board.getSnake();
        offScrGraph.setColor(backgroundColor);
        for (int i = 0; i < snake.size(); i++) {
            int x = snake.xOfCell(i) * jPanel1.getWidth() / numOfXCells;
            int y = snake.yOfCell(i) * jPanel1.getHeight() / numOfYCells;
            offScrGraph.fillOval(x + jPanel1.getWidth() / numOfXCells / 4, y + jPanel1.getHeight() / numOfYCells / 4, jPanel1.getWidth() / numOfXCells / 2, jPanel1.getHeight() / numOfYCells / 2);
        }
        jPanel1.getGraphics().drawImage(offScrImg, 0, 0, jPanel1);
    }

    /**
     * Draws all frogs.
     */
    private void drawAllFrogs() {
        for (Frog frog : board.getFrogs()) {
            switch (frog.getType()) {
                case "Green":
                    offScrGraph.setColor(Color.green);
                    break;
                case "Red":
                    offScrGraph.setColor(Color.red);
                    break;
                case "Blue":
                    offScrGraph.setColor(Color.blue);
                    break;
                default:
                    break;
            }
            int x = frog.getX() * jPanel1.getWidth() / numOfXCells;
            int y = frog.getY() * jPanel1.getHeight() / numOfYCells;
            offScrGraph.fillOval(x + jPanel1.getWidth() / numOfXCells / 3, y + jPanel1.getHeight() / numOfYCells / 3, jPanel1.getWidth() / numOfXCells / 3, jPanel1.getHeight() / numOfYCells / 3);
        }
        jPanel1.getGraphics().drawImage(offScrImg, 0, 0, jPanel1);
    }

    /**
     * Draws a frog.
     * 
     * @see GameBoard#getCurrentFrog()
     */
    private void drawFrog() {
        Frog frog = board.getCurrentFrog();
        switch (frog.getType()) {
            case "Green":
                offScrGraph.setColor(Color.green);
                break;
            case "Red":
                offScrGraph.setColor(Color.red);
                break;
            case "Blue":
                offScrGraph.setColor(Color.blue);
                break;
            default:
                break;
        }
        int x = frog.getX() * jPanel1.getWidth() / numOfXCells;
        int y = frog.getY() * jPanel1.getHeight() / numOfYCells;
        offScrGraph.fillOval(x + jPanel1.getWidth() / numOfXCells / 3, y + jPanel1.getHeight() / numOfYCells / 3, jPanel1.getWidth() / numOfXCells / 3, jPanel1.getHeight() / numOfYCells / 3);
        jPanel1.getGraphics().drawImage(offScrImg, 0, 0, jPanel1);
    }

    /**
     * Erases a frog by filling its current position with background color,<br>
     * so it could be drawn on its next move.
     * 
     * @see GameBoard#getCurrentFrog()
     */
    private void eraseFrog() {
        Frog frog = board.getCurrentFrog();
        int x = frog.getX() * jPanel1.getWidth() / numOfXCells;
        int y = frog.getY() * jPanel1.getHeight() / numOfYCells;
        offScrGraph.setColor(backgroundColor);
        offScrGraph.fillOval(x + jPanel1.getWidth() / numOfXCells / 3, y + jPanel1.getHeight() / numOfYCells / 3, jPanel1.getWidth() / numOfXCells / 3, jPanel1.getHeight() / numOfYCells / 3);
        jPanel1.getGraphics().drawImage(offScrImg, 0, 0, jPanel1);
    }

    /**
     * Updates the stats each time a green or red frog is eaten.
     *
     * @param increase
     */
    private void updateStats(int increase) {
        score += increase;
        snakeLength = board.getSnake().size();
        if (snakeLength > maxSnakeLength) {
            maxSnakeLength = snakeLength;
        }
        jLabel2.setText(String.valueOf(score));
        jLabel4.setText(String.valueOf(++frogsEaten));
        jLabel6.setText(String.valueOf(snakeLength));
    }

    /**
     * Displays "Game Over" message dialog with game results<br>
     * depending on event that happened.
     *
     * @param gameOverEvent
     */
    private void showGameOverMessage(String gameOverEvent) {
        String gameOverDialogTitle = "Game Over. ";
        switch (gameOverEvent) {
            case "SnakeAteItself":
                gameOverDialogTitle += "The snake ate itself.";
                break;
            case "BlueFrogIsEaten":
                gameOverDialogTitle += "The blue frog is eaten.";
                break;
            case "GameIsStopped":
                gameOverDialogTitle += "The game is stopped.";
                break;
            default:
                break;
        }
        String gameOverMessage = "Your score: " + score + " point(s). Your snake ate "
                + frogsEaten + " frog(s),\nit was "
                + snakeLength + " cell(s) long. Highest length value: "
                + maxSnakeLength + " cell(s)!";
        Object[] options = {"Got it!"};
        JOptionPane.showOptionDialog(rootPane, gameOverMessage, gameOverDialogTitle, 
                JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        //JOptionPane.showMessageDialog(rootPane, gameOverMessage, gameOverDialogTitle, JOptionPane.INFORMATION_MESSAGE, gameOverMessageIcon);
        notifyObservers("GameOver");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setIconImage(formIcon);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setToolTipText("");
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentResized(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 614, Short.MAX_VALUE)
        );

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Stop");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Score:");

        jLabel2.setText("0");

        jLabel3.setText("Frogs eaten:");

        jLabel4.setText("0");

        jLabel5.setText("Snake length:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(0, 660, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jButton1)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Redraws all objects if game window is being resized.
     *
     * @param evt
     */
    private void jPanel1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentResized
        offScrImg = createImage(jPanel1.getWidth(), jPanel1.getHeight());
        offScrGraph = offScrImg.getGraphics();
        drawGrid();
        drawSnake();
        drawAllFrogs();
    }//GEN-LAST:event_jPanel1ComponentResized

    /**
     * The "Play/Pause" button event handler.
     *
     * @param evt
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        playPressed = !playPressed;
        if (playPressed) {
            jButton1.setText("Pause");
            jButton2.setEnabled(true);
            notifyObservers("PlayPressed");
        } else {
            jButton1.setText("Play");
            notifyObservers("PausePressed");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * The "Stop" button event handler.
     *
     * @param evt
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        notifyObservers("StopPressed");
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * The mouse buttons event handler.
     *
     * @param evt
     */
    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        if (SwingUtilities.isLeftMouseButton(evt)) {
            notifyObservers("LeftMouseButtonPressed");
        } else if (SwingUtilities.isRightMouseButton(evt)) {
            notifyObservers("RightMouseButtonPressed");
        }
    }//GEN-LAST:event_jPanel1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    /**
     * Updates the display depending on a message<br>
     * received from the observable object.
     *
     * @param message
     * @see GameBoard
     */
    @Override
    public void update(String message) {
        switch (message) {
            case "EraseSnake":
                eraseSnake();
                break;
            case "DrawSnake":
                drawSnake();
                break;            

            case "EraseFrog":
                eraseFrog();
                break;
            case "DrawFrog":
                drawFrog();
                break;
                
            case "GreenFrogIsEaten":
                updateStats(1);
                break;
            case "RedFrogIsEaten":
                updateStats(2);
                break;
                
            case "SnakeAteItself":
                showGameOverMessage(message);
                break;    
            case "BlueFrogIsEaten":
                showGameOverMessage(message);
                break;
            case "GameIsStopped":
                showGameOverMessage(message);
                break;
            case "GameReset":
                initNewGame();
                break;

            default:
                break;
        }
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

}
