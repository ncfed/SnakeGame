package snakegame;

import java.util.LinkedList;
import java.util.List;

public class SnakeGameBoard implements Observer, Observable {

    private List<Observer> observers;
    private int numOfXCells, numOfYCells, snakeLength, numOfGreenFrogs, numOfRedFrogs, numOfBlueFrogs;
    public String[][] frogsMap;
    public Snake snake;
    public Frogs greenFrogs, redFrogs, blueFrogs;

    public SnakeGameBoard(int numOfXCells, int numOfYCells, int snakeLength, int numOfGreenFrogs, int numOfRedFrogs, int numOfBlueFrogs) {
        observers = new LinkedList<>();
        this.numOfXCells = numOfXCells;
        this.numOfYCells = numOfYCells;
        this.snakeLength = snakeLength;
        this.numOfGreenFrogs = numOfGreenFrogs;
        this.numOfRedFrogs = numOfRedFrogs;
        this.numOfBlueFrogs = numOfBlueFrogs;
        createSnake();
        createFrogsMap();
        createFrogs();
    }

    /**
     * Creates a map of frogs positions and fills it with "Empty" values.<br>
     * The map is used to check if the snake ate a frog<br>
     * and by frogs so they could know where to be born and to move.
     *
     * @see #checkCollisions()
     * @see Frogs
     */
    private void createFrogsMap() {
        frogsMap = new String[numOfXCells][numOfYCells];
        for (int i = 0; i < numOfXCells; i++) {
            for (int j = 0; j < numOfYCells; j++) {
                frogsMap[i][j] = "Empty";
            }
        }
    }

    /**
     * Creates a snake with specified initial length.<br>
     * Number of X and Y cells will let the snake know<br>
     * where the borders of gameboard are to move through them.
     *
     * @see Snake
     */
    private void createSnake() {
        snake = new Snake(snakeLength, numOfXCells, numOfYCells);
    }

    /**
     * Creates 3 frogs families with specified parameters.<br>
     * Number of X and Y cells will let the frogs know<br>
     * where the borders of gameboard are to move through them.<br>
     * The frogs can check the snake position while moving and mark their
     * current position on the map.
     *
     * @see Frogs
     */
    private void createFrogs() {
        greenFrogs = new Frogs(numOfGreenFrogs, numOfXCells, numOfYCells, snake, frogsMap, "Green");
        redFrogs = new Frogs(numOfRedFrogs, numOfXCells, numOfYCells, snake, frogsMap, "Red");
        blueFrogs = new Frogs(numOfBlueFrogs, numOfXCells, numOfYCells, snake, frogsMap, "Blue");
    }

    /**
     * Checks if the snake ate itself or a frog by comparing the coordinates<br>
     * of snake head, body and the map of frogs positions.<br>
     * If something is happened, notifies the observers about this event.
     *
     * @see Snake
     * @see #frogsMap
     */
    private void checkForCollisions() {
        int snakeHeadX = snake.xOfCell(0);
        int snakeHeadY = snake.yOfCell(0);
        for (int i = 1; i < snake.size(); i++) {
            if (snake.xOfCell(i) == snakeHeadX & snake.yOfCell(i) == snakeHeadY) {
                notifyObservers("SnakeAteItself");
                return;
            }
        }
        switch (frogsMap[snakeHeadX][snakeHeadY]) {
            case "Green":
                snake.addCell();
                greenFrogs.addFrog();
                greenFrogs.removeFrog(snakeHeadX, snakeHeadY);
                notifyObservers("GreenFrogIsEaten");
                break;
            case "Red":
                snake.removeCell();
                redFrogs.addFrog();
                redFrogs.removeFrog(snakeHeadX, snakeHeadY);
                notifyObservers("RedFrogIsEaten");
                break;
            case "Blue":
                notifyObservers("BlueFrogIsEaten");
                break;
            default:
                break;
        }
    }

    /**
     * Fills the map of frogs positions with "Empty" values and <br>
     * resets the snake and the frogs positions after last gameplay.<br>
     *
     * @see #frogsMap
     * @see Snake
     * @see Frogs
     */
    private void resetGameBoard() {
        for (int i = 0; i < numOfXCells; i++) {
            for (int j = 0; j < numOfYCells; j++) {
                frogsMap[i][j] = "Empty";
            }
        }

        snake.reset();
        greenFrogs.reset();
        redFrogs.reset();
        blueFrogs.reset();
    }

    /**
     * Updates the gameboard state depending on a message<b>
     * received from the observable object.
     *
     * @param message
     * @see SnakeGameProcess
     */
    @Override
    public void update(String message) {
        switch (message) {
            case "MoveSnake":
                notifyObservers("EraseSnake");
                snake.move();
                checkForCollisions();
                notifyObservers("SnakeMoved");
                break;
            case "TurnSnakeLeft":
                notifyObservers("EraseSnake");
                snake.turnLeft();
                checkForCollisions();
                notifyObservers("SnakeTurnedLeft");
                break;
            case "TurnSnakeRight":
                notifyObservers("EraseSnake");
                snake.turnRight();
                checkForCollisions();
                notifyObservers("SnakeTurnedRight");
                break;

            case "MoveGreenFrogs":
                notifyObservers("EraseGreenFrogs");
                greenFrogs.move();
                checkForCollisions();
                notifyObservers("GreenFrogsMoved");
                break;
            case "MoveRedFrogs":
                notifyObservers("EraseRedFrogs");
                redFrogs.move();
                checkForCollisions();
                notifyObservers("RedFrogsMoved");
                break;
            case "MoveBlueFrogs":
                notifyObservers("EraseBlueFrogs");
                blueFrogs.move();
                checkForCollisions();
                notifyObservers("BlueFrogsMoved");
                break;

            case "ResetGameBoard":
                resetGameBoard();
                notifyObservers("GameBoardReset");
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
