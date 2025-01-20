package com.dhondoi.nonaseblak.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.StaticLayout;
import android.text.TextPaint;

import androidx.core.content.res.ResourcesCompat;

/**
 * A helper class for performing operations related to Bitmaps, including color conversion, text rendering, and more.
 *
 */
public class BitmapHelper {

    private Context context;

    public BitmapHelper(Context context) {
        this.context = context;
    }

    /**
     * Converts a color bitmap to a grayscale bitmap.
     *
     * @param bitmap The input color bitmap.
     * @return The resulting grayscale bitmap.
     */
    public Bitmap convertToGrayscale(Bitmap bitmap) {
        Bitmap grayscaleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(grayscaleBitmap);
        Paint paint = new Paint();

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f); // Set saturation to 0 for grayscale

        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0f, 0f, paint);

        return grayscaleBitmap;
    }

    /**
     * Converts a bitmap into a 1-bit per pixel (1bpp) monochrome image represented as a byte array.
     *
     * @param bitmap The input bitmap to convert.
     * @return A byte array representing the monochrome image.
     */
    public byte[] bitmapToByteArray(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bytesPerRow = (width + 7) / 8; // Calculate the number of bytes per row (8 pixels per byte)

        byte[] data = new byte[height * bytesPerRow];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelColor = bitmap.getPixel(x, y);
                int grayscaleValue = (Color.red(pixelColor) + Color.green(pixelColor) + Color.blue(pixelColor)) / 3;
                int bitValue = (grayscaleValue < 128) ? 1 : 0;

                // Calculate the position in the byte array
                int byteIndex = y * bytesPerRow + x / 8;
                int bitIndex = 7 - (x % 8); // 1bpp, most significant bit is on the left

                // Set the bit in the byte
                data[byteIndex] |= (bitValue << bitIndex);
            }
        }

        return data;
    }

    /**
     * Converts a multiline text into a bitmap with specified font and size.
     *
     * @param text The text to render on the bitmap.
     * @param fontId The resource ID of the desired font.
     * @param fontSize The font size for the text.
     * @param maxWidth The maximum width of the bitmap.
     * @return A bitmap containing the rendered text.
     */
    public Bitmap textToMultilineBitmap(String text, int fontId, float fontSize, int maxWidth) {
        // Create a TextPaint object with the desired font and size
        TextPaint paint = new TextPaint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(fontSize);

        // Set the desired font
        Typeface typeface = ResourcesCompat.getFont(context, fontId);
        paint.setTypeface(Typeface.create(typeface, Typeface.BOLD));

        // Create a StaticLayout to handle multiline text
        StaticLayout staticLayout = StaticLayout.Builder.obtain(text,0,text.length(),paint,maxWidth).build();

        // Create a bitmap with the calculated dimensions
        Bitmap bitmap = Bitmap.createBitmap(maxWidth, staticLayout.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a canvas and draw the text on the bitmap
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        staticLayout.draw(canvas);

        return bitmap;
    }
}
