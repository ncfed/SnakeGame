package ru.game.snake.obj;

import ru.game.snake.main.GameBoard;

public abstract class GameObject implements Runnable {

    GameBoard board;
    int numOfXCells, numOfYCells, sleepTime;

    abstract void move();

    public void run() {
        Thread currentThread = Thread.currentThread();
        while (!currentThread.isInterrupted()) {
            try {
                if (board.play) {
                    synchronized (board) {
                        move();
                    }
                }
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

}
