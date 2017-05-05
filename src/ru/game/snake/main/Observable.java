package ru.game.snake.main;

public interface Observable {
      
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String message);
    
}
