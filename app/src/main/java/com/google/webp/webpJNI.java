package com.google.webp;

import android.graphics.Bitmap;

public class webpJNI {

    // Used to load the 'webp-jni-lib' library on application startup.
    static {
        System.loadLibrary("webp-jni-lib");
    }

    public static long AnimDecoder(byte[] bytes, long size) { return WebPAnimDecoder(bytes, size); }
    public static int  AnimDecoderGetCanvasWidth(long dec) { return WebPAnimInfoGetCanvasWidth(WebPAnimDecoderGetInfo(dec)); }
    public static int  AnimDecoderGetCanvasHeight(long dec) { return WebPAnimInfoGetCanvasHeight(WebPAnimDecoderGetInfo(dec)); }
    public static void AnimDecoderGetNextBitmap(long dec, Bitmap bitmap) { WebPAnimDecoderGetNextBitmap(dec, bitmap); }
    public static boolean AnimDecoderHasMoreFrames(long dec) { return WebPAnimDecoderHasMoreFrames(dec); }
    public static void AnimDecoderReset(long dec) { WebPAnimDecoderReset(dec); }

    static native long WebPAnimDecoder(byte[] bytes, long size);
    static native long WebPAnimDecoderGetInfo(long dec);
    static native int  WebPAnimInfoGetCanvasWidth(long info);
    static native int  WebPAnimInfoGetCanvasHeight(long info);
    static native int  WebPAnimDecoderGetNextBitmap(long dec, Bitmap bitmap);
    static native boolean  WebPAnimDecoderHasMoreFrames(long dec);
    static native void WebPAnimDecoderReset(long dec);

}
