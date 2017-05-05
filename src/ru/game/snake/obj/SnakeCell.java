package ru.game.snake.obj;

class SnakeCell {

    private final int x;
    private final int y;

    public SnakeCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

}
