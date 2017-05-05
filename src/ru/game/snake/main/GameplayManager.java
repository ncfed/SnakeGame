package ru.game.snake.main;

import java.util.LinkedList;
import java.util.List;

public class GameplayManager implements Observer, Observable {

    private List<Observer> observers = new LinkedList<>();
    private boolean run;

    public GameplayManager() {
        run = false;
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
                if (!run) {
                    run = true;
                    notifyObservers("StartGame");
                } else {
                    notifyObservers("TogglePause");
                }
                break;
            case "PausePressed":
                notifyObservers("TogglePause");
                break;
            case "StopPressed":
                notifyObservers("StopGame");
                break;
            case "GameOver":
                run = false;
                notifyObservers("ResetGame");
                break;
            case "LeftMouseButtonPressed":
                notifyObservers("TurnSnakeLeft");
                break;
            case "RightMouseButtonPressed":
                notifyObservers("TurnSnakeRight");
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
