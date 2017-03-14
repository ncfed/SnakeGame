package snakegame;

public class SnakeGame {

    public static void main(String[] args) {
        
        //test command console parameters. to be handled later!
        String m = "20"; //args[0]; - numOfXCells
        String n = "20"; //args[1]; - numOfYCells
        String s = "5"; //args[2]; - snakeLength
        String f = "10"; //args[3]; - numOfFrogs
        //String snakeFreezeTime = args[4];
        //final int FROG_FREEZE_TIME;
        
        //parsed parameters
        int numOfXCells = Integer.parseInt(m);
        int numOfYCells = Integer.parseInt(n);
        int snakeLength = Integer.parseInt(s);
        int numOfFrogs = Integer.parseInt(f);
        
        SnakeGameProcess game = new SnakeGameProcess(); //controller
        Snake snake = new Snake(snakeLength, numOfXCells, numOfYCells); //model
        Frogs frogs = new Frogs(numOfFrogs, numOfXCells, numOfYCells);  //model
        SnakeGameWindow window = new SnakeGameWindow(numOfXCells, numOfYCells, snake.getSnake(), frogs.getFrogs()); //view
        
        game.registerObserver(snake);
        game.registerObserver(frogs);
        snake.registerObserver(window);
        frogs.registerObserver(window);
        window.registerObserver(game);
    }
    
}
