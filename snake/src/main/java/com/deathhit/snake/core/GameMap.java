package com.deathhit.snake.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

public class GameMap {
    private final SparseArray<MapObject> objects;

    private final int height;
    private final int width;

    public GameMap(int width, int height){
        objects = new SparseArray<>(width*height);

        this.height = height;
        this.width = width;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder("{");

        for(int i=0;i<objects.size();i++){
            int key = objects.keyAt(i);

            result.append("(").append(key%width).append(",").append(key/width).append(",").append(objects.valueAt(i).getClass().getSimpleName()).append(")");
        }

        return result.append("}").toString();
    }

    @Nullable
    public MapObject get(int x, int y){
        return objects.get(getKey(x, y));
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public void put(MapObject object, int x, int y){
        objects.put(getKey(x, y), object);
    }

    public void putAtRandom(@NonNull MapObject object){
        int x,y;

        while(true){
            x = (int)(Math.random()*getWidth());
            y = (int)(Math.random()*getHeight());

            if(get(x,y) == null){
                put(object, x, y);
                break;
            }
        }
    }

    public void remove(int x, int y){
        objects.remove(getKey(x, y));
    }

    private int getKey(int x, int y){
        return x + y*width;
    }
}
