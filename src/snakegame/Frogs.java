package snakegame;

import java.util.ArrayList;
import java.util.Random;

public class Frogs {

    private ArrayList<Cell> frogs;
    private Snake snake;
    private String[][] frogsMap;
    private String frogType;
    private int numberOfFrogs, numberOfXCells, numberOfYCells;
    private Random random;

    public Frogs(int numberOfFrogs, int numberOfXCells, int numberOfYCells, Snake snake, String[][] frogsMap, String frogType) {
        this.numberOfFrogs = numberOfFrogs;
        this.numberOfXCells = numberOfXCells;
        this.numberOfYCells = numberOfYCells;
        this.snake = snake;
        this.frogsMap = frogsMap;
        this.frogType = frogType;
        frogs = new ArrayList<>();
        random = new Random();
        for (int i = 0; i < numberOfFrogs; i++) {
            boolean placeFound = false;
            int randomX = 0;
            int randomY = 0;
            while (!placeFound) {
                randomX = random.nextInt(numberOfXCells);
                randomY = random.nextInt(numberOfYCells);
                placeFound = cellIsFree(randomX, randomY);
            }
            frogs.add(i, new Cell(randomX, randomY));
            frogsMap[randomX][randomY] = frogType;
        }
    }

    /**
     * Places the frogs at new random positions after last gameplay and marks
     * them on the map.
     */
    public void reset() {
        for (int i = 0; i < numberOfFrogs; i++) {
            boolean placeFound = false;
            int randomX = 0;
            int randomY = 0;
            while (!placeFound) {
                randomX = random.nextInt(numberOfXCells);
                randomY = random.nextInt(numberOfYCells);
                placeFound = cellIsFree(randomX, randomY);
            }
            frogs.get(i).setX(randomX);
            frogs.get(i).setY(randomY);
            frogsMap[randomX][randomY] = frogType;
        }
    }

    /**
     * Returns the type of the frog family. Used to draw the frogs<br>
     * with the appropriate color.
     *
     * @return the type of the frog family.
     * @see SnakeGameWindow
     */
    public String type() {
        return frogType;
    }

    public int size() {
        return frogs.size();
    }

    public int xOfCell(int index) {
        return frogs.get(index).getX();
    }

    public int yOfCell(int index) {
        return frogs.get(index).getY();
    }

    /**
     * Checks if there is no snake and frog in cell with specified coordinates.
     *
     * @param x
     * @param y
     * @return <tt>true</tt> if cell with specified coordinates is free.
     */
    private boolean cellIsFree(int x, int y) {
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
     * @see #move()
     */
    private boolean cellIsSurrounded(int x, int y) {
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
                    xToCheck = numberOfXCells - 1;
                } else if (xToCheck > numberOfXCells - 1) {
                    xToCheck = 0;
                }
                if (yToCheck < 0) {
                    yToCheck = numberOfYCells - 1;
                } else if (yToCheck > numberOfYCells - 1) {
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
     * Removes the frog with specified coordinates when it is eaten by the
     * snake.
     *
     * @param x
     * @param y
     */
    public void removeFrog(int x, int y) {
        for (int i = 0; i <= frogs.size() - 1; i++) {
            if (frogs.get(i).getX() == x & frogs.get(i).getY() == y) {
                frogs.remove(i);
                frogsMap[x][y] = "Empty";
                return;
            }
        }
    }

    /**
     * Adds new frog when other frog is eaten by the snake.
     *
     * @see #removeFrog(int, int)
     */
    public void addFrog() {
        boolean placeFound = false;
        int randomX = 0;
        int randomY = 0;
        while (!placeFound) {
            randomX = random.nextInt(numberOfXCells);
            randomY = random.nextInt(numberOfYCells);
            placeFound = cellIsFree(randomX, randomY);
        }
        frogs.add(frogs.size() - 1, new Cell(randomX, randomY));
        frogsMap[randomX][randomY] = frogType;
    }

    /**
     * Moves each movable frog in the family to new free cell with random
     * coordinates<br>
     * and marks it on the map.
     */
    public void move() {
        for (Cell cell : frogs) {
            int x = cell.getX();
            int y = cell.getY();
            if (cellIsSurrounded(x, y)) {
                continue;
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
                    x = numberOfXCells - 1;
                } else if (x > numberOfXCells - 1) {
                    x = 0;
                }
                if (y < 0) {
                    y = numberOfYCells - 1;
                } else if (y > numberOfYCells - 1) {
                    y = 0;
                }
                if (!cellIsFree(x, y)) {
                    x = oldX;
                    y = oldY;
                } else {
                    placeFound = true;
                }
            }
            cell.setX(x);
            cell.setY(y);
            frogsMap[oldX][oldY] = "Empty";
            frogsMap[x][y] = frogType;
        }
    }

}
