package snakegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.SwingUtilities;

public class SnakeGameWindow extends javax.swing.JFrame implements Observer, Observable {

    private List<Observer> observers;
    int numOfXCells, numOfYCells, frogsEaten = 0;
    int i, j, x, y; //drawing cycle counters and cell coordinates
    ArrayList<Cell> snake, frogs;
    Image offScrImg;
    Graphics offScrGraph;
    boolean playPressed = false;
    Color gridColor = new Color(0, 153, 51, 100);

    public SnakeGameWindow(int m, int n, ArrayList<Cell> snake, ArrayList<Cell> frogs) {
        initComponents();
        observers = new LinkedList<>();
        numOfXCells = m;
        numOfYCells = n;
        this.snake = snake;
        this.frogs = frogs;
        setSize(25 * m, 25 * m);
        setLocationRelativeTo(null);
        show();
    }

    /** 
     * x - X coordinate of current cell of snake;
     * y - Y coordinate of current cell of snake;
     * 
     * width and height of each cell of field are being calculated for each cell of snake:
     * jPanel1.getWidth() / numOfXCells;
     * 
     * calculations of oval start position, its width and height:
     * offScrGraph.fillOval(
     *  x + X offset from top left corner of current cell of field, 
     *  y + Y offset from top left corner of current cell of field, 
     *  width of current cell of field divided by some coefficient, 
     *  height of current cell of field divided by some coefficient
     * );
    **/ 
    public void drawSnake() {
        offScrGraph.setColor(Color.yellow);
        //head
        x = snake.get(0).getX() * jPanel1.getWidth() / numOfXCells;
        y = snake.get(0).getY() * jPanel1.getHeight() / numOfYCells;
        offScrGraph.fillOval(x+jPanel1.getWidth()/numOfXCells/4, y+jPanel1.getHeight()/numOfYCells/4, jPanel1.getWidth()/numOfXCells/2, jPanel1.getHeight()/numOfYCells/2);
        //body
        for (int i = 0; i < snake.size()-1; i++) {
            x = snake.get(i).getX() * jPanel1.getWidth() / numOfXCells;
            y = snake.get(i).getY() * jPanel1.getHeight() / numOfYCells;
            offScrGraph.fillOval(x+jPanel1.getWidth()/numOfXCells/3, y+jPanel1.getHeight()/numOfYCells/3, jPanel1.getWidth()/numOfXCells/3, jPanel1.getHeight()/numOfYCells/3);
        }
        //tail
        x = snake.get(snake.size()-1).getX() * jPanel1.getWidth() / numOfXCells;
        y = snake.get(snake.size()-1).getY() * jPanel1.getHeight() / numOfYCells;
        offScrGraph.fillOval(x-2+jPanel1.getWidth()/numOfXCells/2, y-2+jPanel1.getHeight()/numOfYCells/2, 4, 4);
        
        jPanel1.getGraphics().drawImage(offScrImg, 0, 0, jPanel1);
    }

    public void drawFrogs() {
        offScrGraph.setColor(Color.green);
        for (Cell cell : frogs) {
            x = cell.getX() * jPanel1.getWidth() / numOfXCells;
            y = cell.getY() * jPanel1.getHeight() / numOfYCells;
            offScrGraph.fillOval(x+jPanel1.getWidth()/numOfXCells/3, y+jPanel1.getHeight()/numOfYCells/3, jPanel1.getWidth()/numOfXCells/3, jPanel1.getHeight()/numOfYCells/3);
        }
        jPanel1.getGraphics().drawImage(offScrImg, 0, 0, jPanel1);
    }

    public void draw() {
        //background
        offScrGraph.setColor(jPanel1.getBackground());
        offScrGraph.fillRect(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        //grid
        offScrGraph.setColor(gridColor);
        for (i = 1; i < numOfXCells; i++) {
            x = i * jPanel1.getWidth() / numOfXCells;
            offScrGraph.drawLine(x, 0, x, jPanel1.getHeight());
        }
        for (j = 1; j < numOfYCells; j++) {
            y = j * jPanel1.getHeight() / numOfYCells;
            offScrGraph.drawLine(0, y, jPanel1.getWidth(), y);
        }
        
        jPanel1.getGraphics().drawImage(offScrImg, 0, 0, jPanel1);
        
        drawSnake();
        drawFrogs(); 
    }

    public void drawGameOver() {
        //background
        offScrGraph.setColor(Color.lightGray);
        offScrGraph.fillRect(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        //text message
        offScrGraph.setColor(Color.red);
        offScrGraph.drawString("GAME OVER. YOUR SCORE: " + frogsEaten, jPanel1.getWidth()/8*2, jPanel1.getHeight()/2);
        
        jPanel1.getGraphics().drawImage(offScrImg, 0, 0, jPanel1);
        
        playPressed = false;
        jButton1.setText("Play");
        jLabel2.setText("0");
        notifyObservers("PlayPressed");
    }      

    //method is for testing, but it's too complex. to be fixed later!
    public void checkCollisions() {
        int snakeHeadX = snake.get(0).getX(), snakeHeadY = snake.get(0).getY();
        for (Cell frogCell : frogs) {
            if (snakeHeadX == frogCell.getX() & snakeHeadY == frogCell.getY()) {
                //notifyObservers(frogCell.toString());
                frogsEaten++;
                jLabel2.setText(String.valueOf(frogsEaten));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Snake Game");

        jPanel1.setBackground(new java.awt.Color(0, 102, 51));
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
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Score:");

        jLabel2.setText("0");

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
                .addGap(0, 835, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentResized
        offScrImg = createImage(jPanel1.getWidth(), jPanel1.getHeight());
        offScrGraph = offScrImg.getGraphics();
        draw();
    }//GEN-LAST:event_jPanel1ComponentResized

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        playPressed = !playPressed;
        if (playPressed) {
            jButton1.setText("Pause");
        } else {
            jButton1.setText("Play");
        }
        notifyObservers("PlayPressed");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        playPressed = false;
        frogsEaten = 0;
        jButton1.setText("Play");
        jLabel2.setText("0");
        notifyObservers("PlayPressed");
    }//GEN-LAST:event_jButton2ActionPerformed

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
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
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

    @Override
    public void update(String message) {
        switch (message) {
            case "BoundaryIsReached":
                drawGameOver();
                break;
            default:
                checkCollisions();
                draw();
                break;
        }
    }
}
