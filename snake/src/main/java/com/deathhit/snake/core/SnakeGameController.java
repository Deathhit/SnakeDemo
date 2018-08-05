package com.deathhit.snake.core;

import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class SnakeGameController extends Handler implements Runnable{
    private final SnakeGameModel model;

    private long delay = 1000;

    private Point direction = new Point(0, 0);

    private boolean running = true;

    public SnakeGameController(@NonNull SnakeGameModel model, @NonNull Looper looper){
        this(model, looper, null);
    }

    public SnakeGameController(@NonNull SnakeGameModel model, @NonNull Looper looper, @Nullable Callback callback) {
        super(looper, callback);

        this.model = model;
    }

    @Override
    public void run() {
        MapObject object = getNextObject();

        Message message;

        if(object != null)
            message = object.onContact(this);
        else {
            message = onContactNothing();

            model.snakeMove(getNextX(), getNextY());
        }

        sendMessage(message);

        if(!running)
            return;

        postDelayed(this, delay);
    }

    public long getDelay(){
        return delay;
    }

    public Point getDirection() {
        return direction;
    }

    public SnakeGameModel getModel() {
        return model;
    }

    public MapObject getNextObject(){
        return model.getGameMap().get(getNextX(), getNextY());
    }

    public int getNextX(){
        int width = model.getGameMap().getWidth();

        int dstX = model.getSegment(0).x + direction.x;

        if(dstX >= 0)
            dstX = dstX % width;
        else
            dstX = width + dstX % width;

        return dstX;
    }

    public int getNextY(){
        int height = model.getGameMap().getWidth();

        int dstY = model.getSegment(0).y + direction.y;

        if(dstY >= 0)
            dstY = dstY % height;
        else
            dstY = height + dstY % height;

        return dstY;
    }

    public boolean isRunning(){
        return running;
    }

    public void setDelay(long delay){
        this.delay = delay;
    }

    public void setDirection(int dX, int dY){
        direction.set(dX, dY);
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    public void start(){
        post(this);
    }

    public abstract Message onContactNothing();
}
