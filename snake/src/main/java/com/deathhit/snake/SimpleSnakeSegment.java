package com.deathhit.snake;

import android.os.Message;

import com.deathhit.snake.core.SnakeGameController;
import com.deathhit.snake.core.SnakeGameModel;
import com.deathhit.snake.core.SnakeSegment;

public class SimpleSnakeSegment extends SnakeSegment {
    @Override
    public Message onContact(SnakeGameController controller) {
        SnakeGameModel model = controller.getModel();

        Message message = Message.obtain();

        if(this != model.getSegment(model.getSnakeSize()-1)) {
            controller.setRunning(false);

            message.what = Constants.MESSAGE_DIE;

            return message;
        }else{
            model.snakeMove(controller.getNextX(), controller.getNextY());

            return controller.onContactNothing();
        }
    }
}
