package snakegame;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SnakeGameProcess implements Observer, Observable{
    
    private List<Observer> observers;
    boolean play = false, gameOver = false;
    Timer timer;
    TimerTask snakeTimerTask, frogsTimerTask;
     
    public SnakeGameProcess(){
        observers = new LinkedList<>();
        timer = new Timer();
        snakeTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(play){
                    notifyObservers("MoveSnake");
                }
            }
        };
        timer.scheduleAtFixedRate(snakeTimerTask, 0, 500);
        frogsTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(play){
                    notifyObservers("MoveFrogs");
                }
            }
        };
        timer.scheduleAtFixedRate(frogsTimerTask, 0, 1500);
    }

    @Override
    public void update(String message) {
        switch(message){
            case "PlayPressed":
                play = !play;
                break;
            case "LeftMouseButtonPressed":
                notifyObservers("TurnLeft");
                break;
            case "RightMouseButtonPressed":
                notifyObservers("TurnRight");
                break;
            case "GameOver":
                gameOver = true;
                break;
            default:
                notifyObservers(message);
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
        for (Observer observer : observers)
            observer.update(message);
    }
    
}
