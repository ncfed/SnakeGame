package snakegame;

import java.util.ArrayList;

public class Snake {

    private ArrayList<Cell> snake;
    private String direction;
    private int numberOfXCells, numberOfYCells, initialLength;
    private int prevTailX, prevTailY; //coordinates of tail before movement
    private boolean leftTurnAllowed, rightTurnAllowed; //needed to forbid the snake turn backwards

    public Snake(int initialLength, int numberOfXCells, int numberOfYCells) {
        this.numberOfXCells = numberOfXCells;
        this.numberOfYCells = numberOfYCells;
        this.initialLength = initialLength;
        snake = new ArrayList<>();
        for (int i = 0; i < initialLength; i++) {
            snake.add(i, new Cell(initialLength - i - 1, 0));
        }
        setDirection("right");
        leftTurnAllowed = true;
        rightTurnAllowed = true;
    }

    /**
     * Places the snake to start position.
     */
    public void reset() {
        while (size() > initialLength) {
            removeCell();
        }
        for (int i = 0; i < initialLength; i++) {
            snake.get(i).setX(initialLength - i - 1);
            snake.get(i).setY(0);
        }
        setDirection("right");
        leftTurnAllowed = true;
        rightTurnAllowed = true;
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
        snake.add(snake.size(), new Cell(prevTailX, prevTailY));
    }

    public void removeCell() {
        if (snake.size() > initialLength) {
            snake.remove(snake.get(snake.size() - 1));
        }
    }

    /**
     * Checks if the snake has a cell with specified coordinates.<br>
     * Used by frogs to avoid the cell where the snake is.
     *
     * @param x
     * @param y
     * @return <tt>true</tt> if the snake has a cell with specified coordinates.
     * @see Frogs
     */
    public boolean hasCell(int x, int y) {
        for (Cell cell : snake) {
            if (cell.getX() == x & cell.getY() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves the snake depending on its direction.
     */
    public void move() {
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
            x = numberOfXCells - 1;
        } else if (x > numberOfXCells - 1) {
            x = 0;
        }
        if (y < 0) {
            y = numberOfYCells - 1;
        } else if (y > numberOfYCells - 1) {
            y = 0;
        }
        snake.remove(snake.size() - 1);
        snake.add(0, new Cell(x, y));
        leftTurnAllowed = true;
        rightTurnAllowed = true;
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

}
