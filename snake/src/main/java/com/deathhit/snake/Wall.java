package com.deathhit.snake;

import android.os.Message;

import com.deathhit.snake.core.MapObject;
import com.deathhit.snake.core.SnakeGameController;

public class Wall implements MapObject {
    @Override
    public Message onContact(SnakeGameController controller) {
        controller.setRunning(false);

        Message message = Message.obtain();

        message.what = Constants.MESSAGE_DIE;

        return message;
    }
}
