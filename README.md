# :snake:The Snake Game:frog:

![Gameplay screenshot](https://github.com/ncfed/SnakeGame/blob/master/Screenshot.jpg "Gameplay screenshot")

## :video_game:Controls:
*Left mouse button* - turn left  
*Right mouse button* - turn right  

## :meat_on_bone:Food:
*Green frog* - +1 point  
*Red frog* - +2 points, -1 snake cell  
*Blue frog* - game over

## :bar_chart:Launch parameters:
1. Number of X cells (default: 15)
2. Number of Y cells (default: 15)
3. Initial snake length (default: 3)
4. Number of green frogs (default: 8)
5. Number of red frogs (default: 6)
6. Number of blue frogs (default: 4)
7. Snake sleep time (ms) (default: 500)

**Example:** `java -jar SnakeGame.jar 15 15 3 8 6 4 500`  

* *Launch the game with no keys to use default values:* `java -jar SnakeGame.jar`  
* *Use the "help" key to get information:* `java -jar SnakeGame.jar help`
