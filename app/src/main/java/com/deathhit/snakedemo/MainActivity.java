package com.deathhit.snakedemo;

import android.animation.TimeAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.deathhit.core.BaseActivity;
import com.deathhit.snake.Constants;
import com.deathhit.snake.Food;
import com.deathhit.snake.SimpleSnakeGame;
import com.deathhit.snake.SimpleSnakeSegment;
import com.deathhit.snake.Wall;
import com.deathhit.snake.core.GameMap;
import com.deathhit.snake.core.MapObject;
import com.deathhit.snake.core.SnakeGameModel;

public final class MainActivity extends BaseActivity implements SurfaceHolder.Callback, Handler.Callback, TimeAnimator.TimeListener{
    public static final String TAG = MainActivity.class.getName();

    private final int MAP_WIDTH = 11;
    private final int MAP_HEIGHT = 11;

    private Bitmap[] batMoveSprite;

    private Bitmap backgroundBitmap;
    private Bitmap foodBitmap;
    private Bitmap wallBitmap;

    private SimpleSnakeGame game;

    private SurfaceHolder surfaceHolder;

    private TimeAnimator timeAnimator;

    private int batMoveSpriteIndex = 0;

    private long deltaTime = 0;

    private int unitLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SurfaceView surfaceView = findViewById(R.id.surfaceView);

        surfaceView.getHolder().addCallback(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.turn(SimpleSnakeGame.Turn.COUNTER_CLOCK_WISE);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.turn(SimpleSnakeGame.Turn.CLOCK_WISE);
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        GameMap gameMap = new GameMap(MAP_WIDTH, MAP_HEIGHT);

        gameMap.put(new Food(), 7, 5);

        for(int i=0;i<MAP_WIDTH;i++) {
            gameMap.put(new Wall(), i, 0);
            gameMap.put(new Wall(), i, MAP_HEIGHT-1);
        }

        for(int i=0;i<MAP_HEIGHT;i++){
            gameMap.put(new Wall(), 0, i);
            gameMap.put(new Wall(), MAP_WIDTH-1, i);
        }

        gameMap.remove(5, 0);
        gameMap.remove(5, MAP_HEIGHT-1);
        gameMap.remove(0,5);
        gameMap.remove(MAP_WIDTH-1, 5);

        SnakeGameModel model = new SnakeGameModel(gameMap, new SimpleSnakeSegment(), 4, 4);

        model.snakeConcat(new SimpleSnakeSegment(), 3, 4);
        model.snakeConcat(new SimpleSnakeSegment(), 2, 4);

        HandlerThread thread = new HandlerThread(TAG);

        thread.start();

        game = new SimpleSnakeGame( model, thread.getLooper(), this);

        game.setDirection(1, 0);

        game.setDelay(500);

        game.start();
    }

    @Override
    public void surfaceChanged(final SurfaceHolder surfaceHolder, final int format, final int width, final int height) {
        this.surfaceHolder = surfaceHolder;

        unitLength = width / MAP_WIDTH > height / MAP_HEIGHT ? height / MAP_HEIGHT : width / MAP_WIDTH;

        backgroundBitmap = AssetsProvider.createBackgroundBitmap(this, unitLength*MAP_WIDTH, unitLength*MAP_HEIGHT, unitLength, null);
        batMoveSprite = AssetsProvider.createBatMoveSprite(this, unitLength, unitLength, null);
        foodBitmap = AssetsProvider.createFoodBitmap(this, unitLength, unitLength, null);
        wallBitmap = AssetsProvider.createWallBitmap(this, unitLength, unitLength, null);

        timeAnimator = new TimeAnimator();

        timeAnimator.setTimeListener(this);

        timeAnimator.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        timeAnimator.setTimeListener(null);
        timeAnimator.end();
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what){
            case Constants.MESSAGE_EAT :
            case Constants.MESSAGE_MOVE :
                return true;
            case Constants.MESSAGE_DIE :
                timeAnimator.setTimeListener(null);
                return true;
        }

        return false;
    }

    @Override
    public void onTimeUpdate(TimeAnimator timeAnimator, long totalTime, long deltaTime) {
        this.deltaTime += deltaTime;

        if (this.deltaTime > 250) {
            batMoveSpriteIndex = (batMoveSpriteIndex + 1) % 2;

            this.deltaTime = 0;
        }

        SnakeGameModel model = game.getModel();

        Canvas canvas = surfaceHolder.lockCanvas();

        if (canvas != null) {
            canvas.drawBitmap(backgroundBitmap, 0, 0, null);

            GameMap map = model.getGameMap();

            for (int y = 0; y < MAP_HEIGHT; y++) {
                for (int x = 0; x < MAP_WIDTH; x++) {
                    MapObject object = map.get(x, y);

                    if (object instanceof Wall)
                        canvas.drawBitmap(wallBitmap, x * unitLength, y * unitLength, null);
                    else if(object instanceof Food)
                        canvas.drawBitmap(foodBitmap, x * unitLength, y * unitLength, null);
                }
            }

            for (int i = 0; i < model.getSnakeSize(); i++)
                canvas.drawBitmap(batMoveSprite[batMoveSpriteIndex], model.getSegment(i).x * unitLength, model.getSegment(i).y * unitLength, null);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
