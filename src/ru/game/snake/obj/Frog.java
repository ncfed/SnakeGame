package ru.game.snake.obj;

import ru.game.snake.main.GameBoard;
import java.util.Random;

public class Frog extends GameObject {

    private int x, y;
    private final String frogType;
    private final Random random = new Random();

    public Frog(GameBoard board, String frogType, int sleepTime) {
        this.board = board;
        numOfXCells = board.numOfXCells;
        numOfYCells = board.numOfYCells;
        this.frogType = frogType;
        this.sleepTime = sleepTime;
        boolean placeFound = false;
        int randomX = 0;
        int randomY = 0;
        while (!placeFound) {
            randomX = random.nextInt(numOfXCells);
            randomY = random.nextInt(numOfYCells);
            placeFound = board.cellIsFree(randomX, randomY);
        }
        setX(randomX);
        setY(randomY);
        board.updateFrogsMap(randomX, randomY, frogType);
    }

    private void setX(int x) {
        this.x = x;
    }

    private void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return frogType;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    /**
     * Moves the frog to new free cell with random coordinates<br>
     * and marks it on the map.
     */
    @Override
    public void move() {
        int x = getX();
        int y = getY();
        if (board.cellIsSurrounded(x, y)) {
            return;
        }
        int oldX = x;
        int oldY = y;
        boolean placeFound = false;
        while (!placeFound) {
            int newDirection = random.nextInt(4) + 1;
            switch (newDirection) {
                case 1:
                    y--;
                    break;
                case 2:
                    x++;
                    break;
                case 3:
                    y++;
                    break;
                case 4:
                    x--;
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
            if (!board.cellIsFree(x, y)) {
                x = oldX;
                y = oldY;
            } else {
                placeFound = true;
            }
        }
        board.setCurrentFrog(this);
        board.notifyObservers("EraseFrog");
        board.updateFrogsMap(oldX, oldY, "Empty");
        setX(x);
        setY(y);
        board.notifyObservers("DrawFrog");
        board.updateFrogsMap(x, y, frogType);
    }

}
