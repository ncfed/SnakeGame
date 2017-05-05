package ru.game.snake.main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import ru.game.snake.obj.Frog;
import ru.game.snake.obj.Snake;

public class GameBoard implements Observer, Observable {

    private List<Observer> observers = new LinkedList<>();
    public boolean play = false;
    private boolean currentGameRun;
    final int numOfGreenFrogs;
    final int numOfRedFrogs;
    final int numOfBlueFrogs;
    public int numOfXCells, numOfYCells, initialSnakeLength, snakeSleepTime;
    private int frogsCounter;
    private String[][] frogsMap;
    private Snake snake;
    private Thread snakeThread;
    private Frog currentFrog;
    private ArrayList<Frog> frogs;
    private ArrayList<Thread> frogsThreads;
    private Thread collisionControlThread;

    public GameBoard(int[] gameOptions) {
        numOfXCells = gameOptions[0];
        numOfYCells = gameOptions[1];
        initialSnakeLength = gameOptions[2];
        numOfGreenFrogs = gameOptions[3];
        numOfRedFrogs = gameOptions[4];
        numOfBlueFrogs = gameOptions[5];
        snakeSleepTime = gameOptions[6];
        resetGame();
    }

    /**
     * Resets a map of frogs positions by filling it with "Empty" values.<br>
     * The map is used to check if the snake ate a frog<br>
     * and by frogs so they could know where to be born and to move.
     *
     * @see #checkForCollisions()
     * @see Frog
     */
    private void resetFrogsMap() {
        frogsMap = new String[numOfXCells][numOfYCells];
        for (int i = 0; i < numOfXCells; i++) {
            for (int j = 0; j < numOfYCells; j++) {
                frogsMap[i][j] = "Empty";
            }
        }
    }

    /**
     * Creates the frogs map, the snake and the frogs.<br>
     * Initializes a thread, that will handle the collision situations.
     * 
     * @see #frogsMap
     * @see #checkForCollisions()
     * @see Snake
     * @see Frog
     */
    private void resetGame() {
        collisionControlThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (currentGameRun) {
                    try {
                        if (snake.hasMoved()) {
                            snake.resetMoveFlag();
                            checkForCollisions();
                        }
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }, "Collision Controller");
        resetFrogsMap();
        createSnake();
        frogsCounter = 0;
        frogs = new ArrayList<>();
        frogsThreads = new ArrayList<>();
        for (int i = 0; i < numOfGreenFrogs; i++) {
            createFrog("Green", snakeSleepTime * 5);
        }
        for (int i = 0; i < numOfRedFrogs; i++) {
            createFrog("Red", snakeSleepTime * 4);
        }
        for (int i = 0; i < numOfBlueFrogs; i++) {
            createFrog("Blue", snakeSleepTime * 6);
        }
    }

    /** 
     * Starts all threads.
     */
    private void startGame() {
        play = true;
        currentGameRun = true;
        snakeThread.start();
        for (Thread frogThread : frogsThreads) {
            frogThread.start();
        }
        collisionControlThread.start();
    }

    /**
     * Interrupts all threads and notifies the observers about interrupting event.
     * 
     * @param event 
     */
    private void interruptGame(String event) {
        play = false;
        currentGameRun = false;
        snakeThread.interrupt();
        for (Thread frogThread : frogsThreads) {
            frogThread.interrupt();
        }
        notifyObservers(event);
    }

    /**
     * Creates a snake and its thread.
     *
     * @see Snake
     */
    private void createSnake() {
        snake = new Snake(this);
        snakeThread = new Thread(snake, "Snake");
    }

    /**
     * Creates a frog and its thread, adds them to corresponding array lists.
     *
     * @see Frog
     */
    private void createFrog(String type, int sleepTime) {
        Frog newFrog = new Frog(this, type, sleepTime);
        frogs.add(newFrog);
        Thread newFrogThread = new Thread(newFrog, "Frog-" + ++frogsCounter + "-" + type);
        frogsThreads.add(newFrogThread);
    }

    /**
     * Removes the frog with specified index and its thread from corresponding<br>
     * array lists, interrupts the thread and clears current frogs map cell.
     * 
     * @param index 
     */
    private void removeFrog(int index) {
        Frog frogToRemove = frogs.get(index);
        frogsMap[frogToRemove.getX()][frogToRemove.getY()] = "Empty";
        frogsThreads.get(index).interrupt();
        frogsThreads.remove(index);
        frogs.remove(index);
    }

    /**
     * Replaces the frog with specified coordinates when it is eaten by the
     * snake.
     *
     * @param x
     * @param y
     */
    private void replaceFrog(int x, int y) {
        for (int i = 0; i <= frogs.size() - 1; i++) {
            if (frogs.get(i).getX() == x & frogs.get(i).getY() == y) {
                Frog eatenFrog = frogs.get(i);
                removeFrog(i);
                createFrog(eatenFrog.getType(), eatenFrog.getSleepTime());
                setCurrentFrog(frogs.get(frogs.size() - 1));
                notifyObservers("DrawFrog");
                frogsThreads.get(frogsThreads.size() - 1).start();
                return;
            }
        }
    }

    /**
     * Marks the frog to be erased or drawn.
     * 
     * @param frog
     * @see GameWindow#eraseFrog()
     * @see GameWindow#drawFrog()
     */
    public void setCurrentFrog(Frog frog) {
        currentFrog = frog;
    }

    /**
     * Provides the frog to erase or draw.
     * 
     * @return the frog to be erased or drawn.
     * @see GameWindow#eraseFrog()
     * @see GameWindow#drawFrog()
     */
    Frog getCurrentFrog() {
        return currentFrog;
    }

    /**
     * Provides the snake to be erased of drawn.
     * 
     * @return the snake to be erased of drawn.
     * @see GameWindow#eraseSnake()
     * @see GameWindow#drawSnake()
     */
    Snake getSnake() {
        return snake;
    }

    /**
     * Provides all frogs to be drawn.
     * 
     * @return all frogs to be drawn.
     * @see GameWindow#drawAllFrogs()
     */
    ArrayList<Frog> getFrogs() {
        return frogs;
    }

    /**
     * Marks the frogs map cell with specified coordinates
     * 
     * @param x
     * @param y
     * @param frogType 
     */
    public void updateFrogsMap(int x, int y, String frogType) {
        frogsMap[x][y] = frogType;
    }

    /**
     * Checks if there is no snake and frog in cell with specified coordinates.
     *
     * @param x
     * @param y
     * @return <tt>true</tt> if cell with specified coordinates is free.
     */
    public boolean cellIsFree(int x, int y) {
        return snake.hasCell(x, y) == false & frogsMap[x][y].equals("Empty");
    }

    /**
     * Checks if cell with specified coordinates is surrounded by snake and/or
     * frogs.<br>
     * Used by "Move" method to check the ability of each frog to move.<br>
     * Counts the occupied cells around the cell with specified coordinates.<br>
     * If all 8 cells are occupied, current cell is considered as "surrounded".
     *
     * @param x
     * @param y
     * @return <tt>true</tt> if cell with specified coordinates is surrounded.
     * @see Frog#move()
     */
    public boolean cellIsSurrounded(int x, int y) {
        int numOfOccupiedCells = 0;
        int xToCheck;
        int yToCheck;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                xToCheck = i;
                yToCheck = j;
                if (xToCheck == x & yToCheck == y) {
                    continue;
                }
                if (xToCheck < 0) {
                    xToCheck = numOfXCells - 1;
                } else if (xToCheck > numOfXCells - 1) {
                    xToCheck = 0;
                }
                if (yToCheck < 0) {
                    yToCheck = numOfYCells - 1;
                } else if (yToCheck > numOfYCells - 1) {
                    yToCheck = 0;
                }
                if (!cellIsFree(xToCheck, yToCheck)) {
                    numOfOccupiedCells++;
                }
            }
        }
        return (numOfOccupiedCells == 8);
    }

    /**
     * Checks if the snake ate itself or a frog by comparing the coordinates<br>
     * of snake head, body and the map of frogs positions.<br>
     * If something is happened, handles the event.
     *
     * @see Snake
     * @see #frogsMap
     * @see #interruptGame(java.lang.String)
     */
    private void checkForCollisions() {
        int snakeHeadX = snake.xOfCell(0);
        int snakeHeadY = snake.yOfCell(0);
        for (int i = 1; i < snake.size(); i++) {
            if (snake.xOfCell(i) == snakeHeadX & snake.yOfCell(i) == snakeHeadY) {
                notifyObservers("DrawSnake");
                interruptGame("SnakeAteItself");
                return;
            }
        }
        switch (frogsMap[snakeHeadX][snakeHeadY]) {
            case "Green":
                snake.addCell();
                notifyObservers("DrawSnake");
                snake.increaseSpeed();
                replaceFrog(snakeHeadX, snakeHeadY);
                notifyObservers("GreenFrogIsEaten");
                break;
            case "Red":
                snake.removeCell();
                notifyObservers("DrawSnake");
                snake.increaseSpeed();
                replaceFrog(snakeHeadX, snakeHeadY);
                notifyObservers("RedFrogIsEaten");
                break;
            case "Blue":
                notifyObservers("DrawSnake");
                interruptGame("BlueFrogIsEaten");
                break;
            default:
                notifyObservers("DrawSnake");
                break;
        }
    }

    /**
     * Updates the gameboard state depending on a message<br>
     * received from the observable object.
     *
     * @param message
     * @see SnakeGameProcess
     */
    @Override
    public void update(String message) {
        switch (message) {
            case "StartGame":
                startGame();
                break;
            case "TogglePause":
                play = !play;
                break;
            case "TurnSnakeLeft":
                snake.turnLeft();
                break;
            case "TurnSnakeRight":
                snake.turnRight();
                break;
            case "StopGame":
                interruptGame("GameIsStopped");
                break;
            case "ResetGame":
                resetGame();
                notifyObservers("GameReset");
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
