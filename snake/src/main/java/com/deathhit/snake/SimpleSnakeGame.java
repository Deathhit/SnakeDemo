package com.deathhit.snake;

import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.deathhit.snake.core.SnakeGameController;
import com.deathhit.snake.core.SnakeGameModel;

public class SimpleSnakeGame extends SnakeGameController {
    public enum Turn {
        CLOCK_WISE,
        COUNTER_CLOCK_WISE
    }

    private boolean isTurned;

    public SimpleSnakeGame(@NonNull SnakeGameModel model, @NonNull Looper looper) {
        super(model, looper);
    }

    public SimpleSnakeGame(@NonNull SnakeGameModel model, @NonNull Looper looper, @Nullable Handler.Callback callback) {
        super(model, looper, callback);
    }

    @Override
    public void run() {
        isTurned = false;

        super.run();
    }

    @Override
    public void handleMessage(Message message) {
        if(isTurned)
            return;

        Point d = getDirection();

        int temp = d.x;

        switch (message.what){
            case Constants.MESSAGE_TURN_CCW :
                d.x = d.y;
                d.y = -temp;
                break;
            case Constants.MESSAGE_TURN_CW :
                d.x = -d.y;
                d.y = temp;
                break;
        }

        isTurned = true;
    }

    @Override
    public Message onContactNothing() {
        Message message = Message.obtain();

        message.what = Constants.MESSAGE_MOVE;

        return message;
    }

    public void turn(Turn turn){
        Message message = Message.obtain();

        switch (turn){
            case CLOCK_WISE:
                message.what = Constants.MESSAGE_TURN_CW;
                break;
            case COUNTER_CLOCK_WISE:
                message.what = Constants.MESSAGE_TURN_CCW;
                break;
        }

        sendMessage(message);
    }
}
