package com.deathhit.snake.core;

import java.util.LinkedList;

public class SnakeGameModel {
    private final GameMap gameMap;

    private final LinkedList<SnakeSegment> segments = new LinkedList<>();

    public SnakeGameModel(GameMap gameMap, SnakeSegment head, int x, int y){
        this.gameMap = gameMap;

        segments.add(head);

        head.set(x, y);

        gameMap.put(head, x, y);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");

        for(SnakeSegment segment : segments)
            result.append("(").append(segment.x).append(",").append(segment.y).append(")");

        return result.append("}").toString();
    }

    public SnakeSegment getSegment(int index){
        return segments.get(index);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public int getSnakeSize(){
        return segments.size();
    }

    public void snakeConcat(SnakeSegment segment, int x, int y){
        segment.set(x, y);

        segments.add(segment);

        gameMap.put(segment, x, y);
    }

    public void snakeMove(int x, int y){
        for(SnakeSegment segment : segments){
            int tempX = segment.x;
            int tempY = segment.y;

            segment.set(x, y);

            gameMap.put(segment, x, y);

            x = tempX;
            y = tempY;
        }

        gameMap.remove(x, y);
    }
}
