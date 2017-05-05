package ru.game.snake.obj;

import ru.game.snake.main.GameBoard;
import java.util.ArrayList;

public class Snake extends GameObject {

    private final ArrayList<SnakeCell> snake = new ArrayList<>();
    private String direction = "right";
    private final int initialLength;
    private int prevTailX, prevTailY; //coordinates of tail before current move
    private boolean leftTurnAllowed = true, rightTurnAllowed = true; //needed to forbid the snake turn backwards
    private boolean moved = false;

    public Snake(GameBoard board) {
        this.board = board;
        initialLength = board.initialSnakeLength;
        numOfXCells = board.numOfXCells;
        numOfYCells = board.numOfYCells;
        sleepTime = board.snakeSleepTime;
        for (int i = 0; i < initialLength; i++) {
            snake.add(new SnakeCell(initialLength - i - 1, 0));
        }
    }

    private void setDirection(String direction) {
        this.direction = direction;
    }

    public int size() {
        return snake.size();
    }

    public int xOfCell(int index) {
        return snake.get(index).getX();
    }

    public int yOfCell(int index) {
        return snake.get(index).getY();
    }

    public void addCell() {
        snake.add(snake.size(), new SnakeCell(prevTailX, prevTailY));
    }

    public void removeCell() {
        if (snake.size() > initialLength) {
            snake.remove(snake.get(snake.size() - 1));
        }
    }

    /**
     * Decreases the sleep time period each time a green or red frog is eaten.
     * 
     * @see GameBoard#checkForCollisions() 
     */
    public void increaseSpeed() {
        if (sleepTime > 250) {
            sleepTime -= 3;
        }
    }

    /**
     * Returns the snake move state. 
     * 
     * @return <tt>true</tt> if snake moved.
     * @see GameBoard#collisionControlThread
     */
    public boolean hasMoved() {
        return moved;
    }

    /**
     * Resets the move flag after handling.
     * 
     * @see GameBoard#collisionControlThread
     */
    public void resetMoveFlag() {
        moved = false;
    }

    /**
     * Checks if the snake has a cell with specified coordinates.<br>
     * Used by frogs to avoid the cell where the snake is.
     *
     * @param x
     * @param y
     * @return <tt>true</tt> if the snake has a cell with specified coordinates.
     * @see Frog
     */
    public boolean hasCell(int x, int y) {
        for (SnakeCell cell : snake) {
            if (cell.getX() == x & cell.getY() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Turns the snake left depending on its direction.
     */
    public void turnLeft() {
        if (leftTurnAllowed) {
            switch (direction) {
                case "up":
                    setDirection("left");
                    break;
                case "right":
                    setDirection("up");
                    break;
                case "down":
                    setDirection("right");
                    break;
                case "left":
                    setDirection("down");
                    break;
                default:
                    break;
            }
        }
        leftTurnAllowed = false;
    }

    /**
     * Turns the snake right depending on its direction.
     */
    public void turnRight() {
        if (rightTurnAllowed) {
            switch (direction) {
                case "up":
                    setDirection("right");
                    break;
                case "right":
                    setDirection("down");
                    break;
                case "down":
                    setDirection("left");
                    break;
                case "left":
                    setDirection("up");
                    break;
                default:
                    break;
            }
        }
        rightTurnAllowed = false;
    }

    /**
     * Moves the snake depending on its direction.
     */
    @Override
    public void move() {
        board.notifyObservers("EraseSnake");
        prevTailX = snake.get(snake.size() - 1).getX();
        prevTailY = snake.get(snake.size() - 1).getY();
        int x = snake.get(0).getX();
        int y = snake.get(0).getY();
        switch (direction) {
            case "up":
                y--;
                break;
            case "right":
                x++;
                break;
            case "down":
                y++;
                break;
            case "left":
                x--;
                break;
            default:
                break;
        }
        if (x < 0) {
            x = numOfXCells - 1;
        } else if (x > numOfXCells - 1) {
            x = 0;
        }
        if (y < 0) {
            y = numOfYCells - 1;
        } else if (y > numOfYCells - 1) {
            y = 0;
        }
        snake.remove(snake.size() - 1);
        snake.add(0, new SnakeCell(x, y));
        leftTurnAllowed = true;
        rightTurnAllowed = true;
        moved = true;
    }

}
