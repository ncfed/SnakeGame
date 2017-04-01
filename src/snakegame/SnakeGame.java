package snakegame;

public class SnakeGame {

    public static void main(String[] args) {

        //default initial values
        int[] gameOptions
                = {15, //number of X cells
                    15, //number of Y cells
                    3, //initial snake length
                    8, //number of green frogs
                    6, //number of red frogs
                    4, //number of blue frogs
                    500}; //snake sleep time (ms)

        //message that will be displayed when user launches the game with the "help" key
        String helpMessage = "The Snake Game, v1.0\n\n"
                + "Controls:\n"
                + "Left mouse button - left turn\n"
                + "Right mouse button - right turn\n\n"
                + "Food:\n"
                + "Green frog - +1 point\n"
                + "Red frog - +2 points, -1 snake cell\n"
                + "Blue frog - game over\n\n"
                + "Launch parameters:\n"
                + "1. Number of X cells (default: " + gameOptions[0] + ")\n"
                + "2. Number of Y cells (default: " + gameOptions[1] + ")\n"
                + "3. Initial snake length (default: " + gameOptions[2] + ")\n"
                + "4. Number of green frogs (default: " + gameOptions[3] + ")\n"
                + "5. Number of red frogs (default: " + gameOptions[4] + ")\n"
                + "6. Number of blue frogs (default: " + gameOptions[5] + ")\n"
                + "7. Snake sleep time (ms) (default: " + gameOptions[6] + ")\n\n"
                + "Example:\n"
                + "java -jar SnakeGame.jar 15 15 3 8 6 4 500\n";

        if (args.length > 0) {
            if (args[0].equals("help")) {
                System.out.println(helpMessage);
                System.exit(0);
            }
            for (int i = 0; i < 7; i++) {
                try {
                    gameOptions[i] = Integer.parseInt(args[i]);
                } catch (NumberFormatException e) {
                    System.out.println("Argument #" + Integer.toString(i + 1) + " is not integer.");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Argument #" + Integer.toString(i + 1) + " is not specified.");
                }
            }
        }

        //number of X cells
        if (gameOptions[0] < 10 | gameOptions[0] > 30) {
            gameOptions[0] = 15;
        }
        System.out.println("Number of X cells: " + gameOptions[0]);

        //number of Y cells
        if (gameOptions[1] < 10 | gameOptions[1] > 30) {
            gameOptions[1] = gameOptions[0];
        }
        System.out.println("Number of Y cells: " + gameOptions[1]);

        //initial snake length
        if (gameOptions[2] < 3 | gameOptions[2] > gameOptions[0] - 5) {
            gameOptions[2] = 3;
        }
        System.out.println("Snake length: " + gameOptions[2]);

        //number of green frogs
        if (gameOptions[3] < 8 | gameOptions[3] > 20) {
            gameOptions[3] = 8;
        }
        System.out.println("Number of green frogs: " + gameOptions[3]);

        //number of red frogs
        if (gameOptions[4] < 6 | gameOptions[4] > 15) {
            gameOptions[4] = 6;
        }
        System.out.println("Number of red frogs: " + gameOptions[4]);

        //number of blue frogs
        if (gameOptions[5] < 4 | gameOptions[5] > 10) {
            gameOptions[5] = 4;
        }
        System.out.println("Number of blue frogs: " + gameOptions[5]);

        //snake sleep time (ms)
        if (gameOptions[6] < 200 | gameOptions[6] > 1000) {
            gameOptions[6] = 500;
        }
        System.out.println("Snake sleep time: " + gameOptions[6] + " ms");

        SnakeGameBoard board = new SnakeGameBoard(gameOptions[0], gameOptions[1], gameOptions[2], gameOptions[3], gameOptions[4], gameOptions[5]); //model
        SnakeGameWindow window = new SnakeGameWindow(gameOptions[0], gameOptions[1], board.snake, board.greenFrogs, board.redFrogs, board.blueFrogs); //view
        SnakeGameProcess game = new SnakeGameProcess(gameOptions[6]); //controller

        game.registerObserver(board);
        board.registerObserver(window);
        window.registerObserver(game);

    }

}
