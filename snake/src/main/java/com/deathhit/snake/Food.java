package com.deathhit.snake;

import android.os.Message;

import com.deathhit.snake.core.MapObject;
import com.deathhit.snake.core.SnakeGameController;
import com.deathhit.snake.core.SnakeGameModel;
import com.deathhit.snake.core.SnakeSegment;

public class Food implements MapObject {
    @Override
    public Message onContact(SnakeGameController controller) {
        SnakeGameModel model = controller.getModel();

        SnakeSegment last = model.getSegment(model.getSnakeSize()-1);

        model.snakeConcat(new SimpleSnakeSegment(), last.x, last.y);

        model.snakeMove(controller.getNextX(), controller.getNextY());

        model.getGameMap().putAtRandom(this);   //Put food somewhere else to make it looks like a new food being generated

        Message message = Message.obtain();

        message.what = Constants.MESSAGE_EAT;

        return message;
    }
}
