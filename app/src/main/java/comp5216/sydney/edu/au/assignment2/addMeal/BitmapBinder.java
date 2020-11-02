package comp5216.sydney.edu.au.assignment2.addMeal;

import android.graphics.Bitmap;
import android.os.Binder;

public class BitmapBinder extends Binder {
    private Bitmap bitmap;

    BitmapBinder(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    Bitmap getBitmap() {
        return bitmap;
    }
}