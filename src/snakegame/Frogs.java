package snakegame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Frogs implements Observer, Observable {

    private List<Observer> observers;
    private ArrayList<Cell> frogs = new ArrayList<Cell>();
    private int newDirection;
    private int x, y; //coordinates of cell
    private int numberOfXCells, numberOfYCells;
    private Random random = new Random();

    public Frogs(int numberOfFrogs, int numberOfXCells, int numberOfYCells) {
        observers = new LinkedList<>();
        setGridSize(numberOfXCells, numberOfYCells);
        for (int i = 0; i < numberOfFrogs; i++) {
            frogs.add(i, new Cell(random.nextInt(numberOfXCells), random.nextInt(numberOfYCells)));
        }
    }

    public ArrayList<Cell> getFrogs() {
        return frogs;
    }

    private void setGridSize(int numberOfXCells, int numberOfYCells) {
        this.numberOfXCells = numberOfXCells;
        this.numberOfYCells = numberOfYCells;
    }
    
    private void removeFrog(String cell){
        //frogs.remove(cell.);
    }
    
    private void addFrog(){
        
    }

    public void move() {
        for (Cell cell : frogs) {
            x = cell.getX();
            y = cell.getY();
            newDirection = random.nextInt(4)+1;
            switch (newDirection) {
                case 1:
                    if(y>0){y--;}
                    break;
                case 2:
                    if(x<numberOfXCells-1){x++;}
                    break;
                case 3:
                    if(y<numberOfYCells-1){y++;}
                    break;
                case 4:
                    if(x>0){x--;}
                    break;
            }
            cell.setX(x);
            cell.setY(y);
        }
        notifyObservers("DrawFrogs");
    }

    @Override
    public void update(String message) {
        switch (message) {
            case "MoveFrogs":
                move();
                break;
            default:
                removeFrog(message);
                addFrog();
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
