/**
 * Copyright 2019 Contrite Observer
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.rajawali3d.rajawaliwebpanimation;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.google.webp.webpJNI;

import org.rajawali3d.materials.textures.ASingleTexture;
import org.rajawali3d.materials.textures.TextureManager;

import java.io.IOException;
import java.io.InputStream;

import static android.graphics.Bitmap.Config.ARGB_8888;


/**
 * Creates a texture from an animated WebP.
 */
public class AnimatedWebpTexture extends ASingleTexture {
    private byte[] mData;
    private long mDemux;
    private int mNumFrames;
    private int mBackgroundColor = 0;
    private int currentFrame = 0;

    public AnimatedWebpTexture(String name, int resourceId) throws IOException {
        super(TextureType.DIFFUSE, name);
        Context context = TextureManager.getInstance().getContext();
        AssetFileDescriptor fd = context.getResources().openRawResourceFd(resourceId);
        mData = new byte[(int)fd.getLength()];
        fd.close();
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        long read = inputStream.read(mData);

        mDemux = webpJNI.Demux(mData, read);
        mNumFrames = webpJNI.DemuxGetFrameCount(mDemux);

        mWidth = webpJNI.DemuxGetCanvasWidth(mDemux);
        mHeight = webpJNI.DemuxGetCanvasHeight(mDemux);
        mBackgroundColor = webpJNI.DemuxGetBackgroundColor(mDemux);
        mBitmap = Bitmap.createBitmap(mWidth, mHeight, ARGB_8888);

        DecodeFrame(currentFrame);
    }

    private void DecodeFrame(int frame) {
        long iter = webpJNI.DemuxGetFrame(mDemux, frame+1);
        webpJNI.IterDecodeToBitmap(iter, mBitmap, mBitmap.getWidth(), mBitmap.getHeight(), mBackgroundColor);
        webpJNI.DemuxReleaseIterator(iter);
        setBitmap(mBitmap);
    }

    @Override
    public AnimatedWebpTexture clone() {
        return null;
    }

    public void update() {
        currentFrame++;
        DecodeFrame(currentFrame%mNumFrames);
        TextureManager.getInstance().replaceTexture(this);
    }
}