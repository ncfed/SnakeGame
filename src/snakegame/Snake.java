package snakegame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Snake implements Observer, Observable {  
    
    private List<Observer> observers;
    private ArrayList<Cell> snake = new ArrayList<Cell>();
    private String direction;
    private int x, y; //coordinates of cell
    private int numberOfXCells, numberOfYCells, length;
    
    public Snake(int length, int numberOfXCells, int numberOfYCells){
        observers = new LinkedList<>();
        setGridSize(numberOfXCells, numberOfYCells);
        this.length = length;
        for(int i=0;i<length;i++){
            snake.add(i, new Cell(length-i-1, 0));
        }
        setDirection("right");
    } 
    
    public ArrayList<Cell> getSnake(){
        return snake;
    }
    
    public void addCell(){
        //snake.add(length, new Cell(, ));
    }
    
    private void setGridSize(int numberOfXCells, int numberOfYCells){
        this.numberOfXCells = numberOfXCells;
        this.numberOfYCells = numberOfYCells;
    }
    
    private void setDirection(String direction){
        this.direction = direction;
    }
    
    public void move() {
        x = snake.get(0).getX();
        y = snake.get(0).getY();
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
        }
        if (x < 0 || x > numberOfXCells - 1 | y < 0 || y > numberOfYCells - 1) {
            notifyObservers("BoundaryIsReached");
        } else {
            snake.remove(snake.size() - 1);
            snake.add(0, new Cell(x, y));
            notifyObservers("DrawSnake");
        }
    }
    
    public void turnLeft() {
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
        }
    }

    public void turnRight(){
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
        }
    }

    @Override
    public void update(String message) {
        switch(message){
            case "MoveSnake":
                move();
                break;
            case "TurnLeft":
                turnLeft();
                break;
            case "TurnRight":
                turnRight();
                break;
            default:
                addCell();
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
