#include <stdlib.h>
#include <android/bitmap.h>
#include <webp/demux.h>
#include <math.h>
#include "jni.h"
#include "webp/types.h"
#include "webp/decode.h"

jint JNICALL Java_com_google_webp_webpJNI_WebPGetDecoderVersion(JNIEnv *jenv, jclass jcls) {
    jint jresult = 0 ;
    int result;

    (void)jenv;
    (void)jcls;
    result = (int)WebPGetDecoderVersion();
    jresult = (jint)result;
    return jresult;
}

jint JNICALL Java_com_google_webp_webpJNI_WebPGetDemuxVersion(JNIEnv *jenv, jclass jcls) {
    jint jresult = 0 ;
    int result;

    (void)jenv;
    (void)jcls;
    result = (int)WebPGetDemuxVersion();
    jresult = (jint)result;
    return jresult;
}

jlong JNICALL Java_com_google_webp_webpJNI_WebPDemux(JNIEnv *jenv, jclass jcls, jbyteArray bytes, jlong size) {
    jlong jresult = 0 ;
    WebPData* data = (WebPData *) malloc(sizeof(WebPData));
    data->bytes = (*jenv)->GetByteArrayElements(jenv, bytes, NULL);
    data->size = size;
    WebPDemuxer* result;

    (void)jenv;
    (void)jcls;
    result = WebPDemux(data);
    jresult = (jlong)result;
    return jresult;
}

jlong JNICALL Java_com_google_webp_webpJNI_WebPDemuxGetFrame(JNIEnv *jenv, jclass jcls, jlong dmuxLong, jint jframe) {
    jlong jresult = 0;
    WebPDemuxer* dmux = (WebPDemuxer*) dmuxLong;
    int frame = jframe;
    WebPIterator* iter = malloc(sizeof(WebPIterator));
    int result;

    (void)jenv;
    (void)jcls;
    result = WebPDemuxGetFrame(dmux, frame, iter);
    jresult = (jlong) iter;
    return jresult;
}

void JNICALL Java_com_google_webp_webpJNI_WebPDemuxDelete(JNIEnv *jenv, jclass jcls, jlong dmuxLong) {
    WebPDemuxer* dmux = (WebPDemuxer*) dmuxLong;

    (void)jenv;
    (void)jcls;
    if(dmux) WebPDemuxDelete(dmux);
}

jint JNICALL Java_com_google_webp_webpJNI_WebPIterGetDurationMs(JNIEnv *jenv, jclass jcls, jlong iterLong) {
    jint jresult = 0 ;
    WebPIterator* iter = (WebPIterator*) iterLong;
    int result;

    (void)jenv;
    (void)jcls;
    result = iter->duration;
    jresult = (jint)result;
    return jresult;
}

jint JNICALL Java_com_google_webp_webpJNI_WebPIterGetWidth(JNIEnv *jenv, jclass jcls, jlong iterLong) {
    jint jresult = 0 ;
    WebPIterator* iter = (WebPIterator*) iterLong;
    int result;

    (void)jenv;
    (void)jcls;
    result = iter->width;
    jresult = (jint)result;
    return jresult;
}

jint JNICALL Java_com_google_webp_webpJNI_WebPIterGetHeight(JNIEnv *jenv, jclass jcls, jlong iterLong) {
    jint jresult = 0 ;
    WebPIterator* iter = (WebPIterator*) iterLong;
    int result;

    (void)jenv;
    (void)jcls;
    result = iter->height;
    jresult = (jint)result;
    return jresult;
}

jint JNICALL Java_com_google_webp_webpJNI_WebPIterGetOffsetX(JNIEnv *jenv, jclass jcls, jlong iterLong) {
    jint jresult = 0 ;
    WebPIterator* iter = (WebPIterator*) iterLong;
    int result;

    (void)jenv;
    (void)jcls;
    result = iter->x_offset;
    jresult = (jint)result;
    return jresult;
}

jint JNICALL Java_com_google_webp_webpJNI_WebPIterGetOffsetY(JNIEnv *jenv, jclass jcls, jlong iterLong) {
    jint jresult = 0 ;
    WebPIterator* iter = (WebPIterator*) iterLong;
    int result;

    (void)jenv;
    (void)jcls;
    result = iter->y_offset;
    jresult = (jint)result;
    return jresult;
}

jint JNICALL Java_com_google_webp_webpJNI_WebPIterGetBlendMethod(JNIEnv *jenv, jclass jcls, jlong iterLong) {
    jint jresult = 0 ;
    WebPIterator* iter = (WebPIterator*) iterLong;
    int result;

    (void)jenv;
    (void)jcls;
    result = iter->blend_method;
    jresult = (jint)result;
    return jresult;
}

uint8_t blend(uint8_t underlay, uint8_t overlay, uint8_t factor) {
    int result = underlay * (0xff-factor) + overlay * factor;
    return (uint8_t) (result / 0xff);
}

void JNICALL Java_com_google_webp_webpJNI_WebPIterDecodeToBitmap(JNIEnv *jenv, jclass jcls, jlong iterLong, jobject bitmap, jint jwidth, jint jheight) {
    WebPIterator* iter = (WebPIterator*) iterLong;
    uint8_t* pixels;
    int result;
    int width;
    int height;
    uint8_t* buffer = NULL;
    uint8_t* ptr = NULL;
    int canvasX = jwidth;
    int canvasY = jheight;

    result = AndroidBitmap_lockPixels(jenv, bitmap, &pixels);
    if (result < 0) { return; }

    size_t stride= 4;
    const uint8_t* bytes = iter->fragment.bytes;
    size_t size = iter->fragment.size;
    int    offsetX = iter->x_offset;
    int    offsetY = iter->y_offset;
    buffer = WebPDecodeRGBA(bytes, size, &width, &height);
    ptr = buffer;

    switch(iter->blend_method) {
        case WEBP_MUX_NO_BLEND:
            for(int i=0; i<canvasX*canvasY; i++) {
                pixels[0+i*stride] = *(ptr+0);
                pixels[1+i*stride] = *(ptr+1);
                pixels[2+i*stride] = *(ptr+2);
                pixels[3+i*stride] = *(ptr+3);
                ptr+=stride;
            }
            break;
        default:
            for (int y = offsetY; y < (offsetY + height); y++) {
                for (int x = offsetX; x < (offsetX + width); x++) {
                    uint8_t a = *(ptr+3);
                    uint8_t r = pixels[0 + (y * canvasX + x) * stride];
                    uint8_t g = pixels[1 + (y * canvasX + x) * stride];
                    uint8_t b = pixels[2 + (y * canvasX + x) * stride];

                    pixels[0 + (y * canvasX + x) * stride] = blend(r, *(ptr + 0), a);
                    pixels[1 + (y * canvasX + x) * stride] = blend(g, *(ptr + 1), a);
                    pixels[2 + (y * canvasX + x) * stride] = blend(b, *(ptr + 2), a);

                    ptr+=stride;
                }
            }
            break;
    }
    if(buffer) free(buffer);

    AndroidBitmap_unlockPixels(jenv, bitmap);
    (void)jenv;
    (void)jcls;

    return;
}

jint JNICALL Java_com_google_webp_webpJNI_WebPDemuxGetI(JNIEnv *jenv, jclass jcls, jlong dmuxLong, jint jfeature) {
    jint jresult = 0 ;
    WebPDemuxer* dmux = (WebPDemuxer*) dmuxLong;
    WebPFormatFeature feature = jfeature;
    int result;

    (void)jenv;
    (void)jcls;
    result = (int)WebPDemuxGetI(dmux, feature);
    jresult = (jint)result;
    return jresult;
}

void JNICALL Java_com_google_webp_webpJNI_WebPDemuxReleaseIterator(JNIEnv *jenv, jclass jcls, jlong iterLong) {
    WebPIterator* iter = (WebPIterator*) iterLong;
    (void)jenv;
    (void)jcls;
    WebPDemuxReleaseIterator(iter);
}

jlong JNICALL Java_com_google_webp_webpJNI_WebPInitDecoderConfig(JNIEnv *jenv, jclass jcls) {
    jlong jresult = 0 ;
    WebPDecoderConfig* config = malloc(sizeof(WebPDecoderConfig));

    (void)jenv;
    (void)jcls;
    WebPInitDecoderConfig(config);
    jresult = (jlong)config;
    return jresult;
}

void JNICALL Java_com_google_webp_webpJNI_WebPReleaseDecoderConfig(JNIEnv *jenv, jclass jcls, jlong configLong) {
    WebPDecoderConfig* config = configLong;

    (void)jenv;
    (void)jcls;
    if(config) free(config);
}

