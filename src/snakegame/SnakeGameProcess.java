package snakegame;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SnakeGameProcess implements Observer, Observable {

    private List<Observer> observers;
    private boolean play;
    private int snakeSleepTime;
    private Timer timer;
    private TimerTask snakeTimerTask, greenFrogsTimerTask, redFrogsTimerTask, blueFrogsTimerTask;

    public SnakeGameProcess(int snakeSleepTime) {
        observers = new LinkedList<>();
        play = false;
        this.snakeSleepTime = snakeSleepTime;
        startGame();
    }

    /**
     * Launches timers for snake and frogs.
     *
     * @see java.util.Timer
     * @see java.util.TimerTask
     */
    private void startGame() {
        timer = new Timer();

        snakeTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (play) {
                    notifyObservers("MoveSnake");
                }
            }
        };
        timer.scheduleAtFixedRate(snakeTimerTask, 0, snakeSleepTime);

        greenFrogsTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (play) {
                    notifyObservers("MoveGreenFrogs");
                }
            }
        };
        timer.scheduleAtFixedRate(greenFrogsTimerTask, 0, snakeSleepTime * 5);

        redFrogsTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (play) {
                    notifyObservers("MoveRedFrogs");
                }
            }
        };
        timer.scheduleAtFixedRate(redFrogsTimerTask, 0, snakeSleepTime * 4);

        blueFrogsTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (play) {
                    notifyObservers("MoveBlueFrogs");
                }
            }
        };
        timer.scheduleAtFixedRate(blueFrogsTimerTask, 0, snakeSleepTime * 6);
    }

    /**
     * Updates the game process state depending on a message<br>
     * received from the observable object.
     *
     * @param message
     * @see SnakeGameWindow
     */
    @Override
    public void update(String message) {
        switch (message) {
            case "PlayPressed":
                play = true;
                break;
            case "PausePressed":
                play = false;
                break;
            case "LeftMouseButtonPressed":
                notifyObservers("TurnSnakeLeft");
                break;
            case "RightMouseButtonPressed":
                notifyObservers("TurnSnakeRight");
                break;
            case "GameOver":
                play = false;
                break;
            case "StartNewGame":
                notifyObservers("ResetGameBoard");
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
