package com.deathhit.snakedemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public final class AssetsProvider {
    /**sh_bat.png : 128*16 => Sprite 2 frames**/
    public static Bitmap[] createBatMoveSprite(@NonNull Context context, int width, int height, @Nullable Paint paint){
        Bitmap sheet = getBitmapFromAsset(context, "sh_bat.png");

        Bitmap[] sprite = new Bitmap[2];

        assert sheet != null;
        for(int i=0;i<2;i++) {
            Rect src = new Rect(i * 15, 0, (i + 1) * 15, 16);

            sprite[i] = createBitmapFromSheet(sheet, src, width, height, paint);
        }

        return sprite;
    }

    public static Bitmap createBackgroundBitmap(@NonNull Context context, int width, int height, int unitLength, @Nullable Paint paint){
        Bitmap floor = createFloorBitmap(context, unitLength, unitLength, paint);

        return multiply(floor, width, height);
    }

    /**items.png**/
    public static Bitmap createFoodBitmap(@NonNull Context context, int width, int height, @Nullable Paint paint){
        Bitmap sheet = getBitmapFromAsset(context, "items.png");

        Rect src =  new Rect(32, 224, 48, 240);

        assert sheet != null;
        return createBitmapFromSheet(sheet, src, width, height, paint);
    }

    /**tiles.png**/
    public static Bitmap createFloorBitmap(@NonNull Context context, int width, int height, @Nullable Paint paint){
        Bitmap sheet = getBitmapFromAsset(context, "tiles.png");

        Rect src =  new Rect(64, 16, 80, 32);

        assert sheet != null;
        return createBitmapFromSheet(sheet, src, width, height, paint);
    }

    /**tiles.png**/
    public static Bitmap createWallBitmap(@NonNull Context context, int width, int height, @Nullable Paint paint){
        Bitmap sheet = getBitmapFromAsset(context, "tiles.png");

        Rect src =  new Rect(64, 0, 80, 16);

        assert sheet != null;
        return createBitmapFromSheet(sheet, src, width, height, paint);
    }

    /**Create bitmap by scaling source bitmap from sheet to target size.**/
    private static Bitmap createBitmapFromSheet(@NonNull Bitmap sheet, @NonNull Rect src, int width, int height, @Nullable Paint paint){
        Bitmap.Config config = sheet.getConfig();

        Canvas canvas = new Canvas();

        Bitmap bitmap;

        Rect dst = new Rect(0, 0, width, height);

        bitmap = Bitmap.createBitmap(width, height, config);

        canvas.setBitmap(bitmap);
        canvas.drawBitmap(sheet, src, dst, paint);

        return bitmap;
    }

    /***Create bitmap by decoding asset.***/
    private static Bitmap getBitmapFromAsset(@NonNull Context context, @NonNull String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream inputStream;
        Bitmap bitmap;

        try {
            inputStream = assetManager.open(filePath);

            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            return null;
        }

        return bitmap;
    }


    /***Multiply source bitmap to fill target bitmap.***/
    private static Bitmap multiply(@NonNull Bitmap src, @NonNull Bitmap dst){	//Repeatedly draw dst to src until dst is completely drawn.
        Canvas c = new Canvas(dst);
        int srcW = src.getWidth(),
                srcH = src.getHeight();

        for(int i=0;i<dst.getHeight();i+=srcH){
            for(int j=0;j<dst.getWidth();j+=srcW)
                c.drawBitmap(src, j, i, null);
        }

        return dst;
    }

    /***Create bitmap by multiplying source bitmap.***/
    private static Bitmap multiply(@NonNull Bitmap src, int dstWidth, int dstHeight){
        return multiply(src, Bitmap.createBitmap(dstWidth, dstHeight, src.getConfig()));
    }
}
