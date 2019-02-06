package com.google.webp;

import android.graphics.Bitmap;

public class webpJNI {

    // Used to load the 'webp-jni-lib' library on application startup.
    static {
        System.loadLibrary("webp-jni-lib");
    }

    public static int GetDemuxVersion() {
        return WebPGetDemuxVersion();
    }
    public static long Demux(byte[] bytes, long size) { return WebPDemux(bytes, size); }
    public static int DemuxGetFrameCount(long dmux) { return WebPDemuxGetI(dmux, WEBP_FF_FRAME_COUNT); }
    public static int DemuxGetCanvasWidth(long dmux) { return WebPDemuxGetI(dmux, WEBP_FF_CANVAS_WIDTH); }
    public static int DemuxGetCanvasHeight(long dmux) { return WebPDemuxGetI(dmux, WEBP_FF_CANVAS_HEIGHT); }

    public static long DemuxGetFrame(long dmux, int frame) { return WebPDemuxGetFrame(dmux, frame); }
    public static void DemuxReleaseIterator(long iter) {
        WebPDemuxReleaseIterator(iter);
    }
    public static void DemuxDelete(long dmux) {
        WebPDemuxDelete(dmux);
    }

    public static int GetDecoderVersion() {
        return WebPGetDecoderVersion();
    }
    public static int  IterGetDurationMs(long iter) { return WebPIterGetDurationMs(iter); }
    public static int  IterGetWidth(long iter) { return WebPIterGetWidth(iter); }
    public static int  IterGetHeight(long iter) { return WebPIterGetHeight(iter); }
    public static int  IterGetOffsetX(long iter) { return WebPIterGetOffsetX(iter); }
    public static int  IterGetOffsetY(long iter) { return WebPIterGetOffsetY(iter); }
    public static int  IterGetBlendMethod(long iter) { return WebPIterGetBlendMethod(iter); }
    public static void IterDecodeToBitmap(long iter, Bitmap bitmap, int width, int height) { WebPIterDecodeToBitmap(iter, bitmap, width, height); }

    public static long InitDecoderConfig() {
        return WebPInitDecoderConfig();
    }

    public static void ReleaseDecoderConfig(long config) {
        WebPReleaseDecoderConfig(config);
    }

    public static final int WEBP_FF_FORMAT_FLAGS = 0;      // bit-wise combination of WebPFeatureFlags
                                                           // corresponding to the 'VP8X' chunk (if present).
    public static final int WEBP_FF_CANVAS_WIDTH = 1;
    public static final int WEBP_FF_CANVAS_HEIGHT =2;
    public static final int WEBP_FF_LOOP_COUNT =3;         // only relevant for animated file
    public static final int WEBP_FF_BACKGROUND_COLOR = 4;  // idem.
    public static final int WEBP_FF_FRAME_COUNT = 5;       // Number of frames present in the demux object.
        // In case of a partial demux, this is the number
        // of frames seen so far, with the last frame
        // possibly being partial.

    public static final int WEBP_MUX_DISPOSE_NONE = 0;       // Do not dispose.
    public static final int WEBP_MUX_DISPOSE_BACKGROUND = 1; // Dispose to background color.

    public static final int WEBP_MUX_BLEND = 0;            // Blend.
    public static final int WEBP_MUX_NO_BLEND = 1;         // Do not blend.

    static native int  WebPGetDecoderVersion();
    static native int  WebPGetDemuxVersion();

    static native long WebPDemuxGetFrame(long dmux, int frame);
    static native int  WebPIterGetDurationMs(long iter);
    static native int  WebPIterGetWidth(long iter);
    static native int  WebPIterGetHeight(long iter);
    static native int  WebPIterGetOffsetX(long iter);
    static native int  WebPIterGetOffsetY(long iter);
    static native int  WebPIterGetBlendMethod(long iter);
    static native void WebPIterDecodeToBitmap(long iter, Bitmap bitmap, int width, int height);

    static native void WebPDemuxReleaseIterator(long iter);
    static native long WebPDemux(byte[] bytes, long size);
    static native int  WebPDemuxGetI(long dmux, int feature);
    static native void WebPDemuxDelete(long dmux);
    static native long WebPInitDecoderConfig();
    static native void WebPReleaseDecoderConfig(long config);
}
