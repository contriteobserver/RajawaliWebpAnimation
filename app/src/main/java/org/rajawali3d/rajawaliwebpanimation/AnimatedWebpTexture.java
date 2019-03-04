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
    private long mDecoder;

    public AnimatedWebpTexture(String name, int resourceId) throws IOException {
        super(TextureType.DIFFUSE, name);

        Context context = TextureManager.getInstance().getContext();
        AssetFileDescriptor fd = context.getResources().openRawResourceFd(resourceId);
        byte[] mData = new byte[(int)fd.getLength()];
        fd.close();
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        long read = inputStream.read(mData);

        mDecoder = webpJNI.AnimDecoder(mData, read);
        mWidth = webpJNI.AnimDecoderGetCanvasWidth(mDecoder);
        mHeight = webpJNI.AnimDecoderGetCanvasHeight(mDecoder);
        mBitmap = Bitmap.createBitmap(mWidth, mHeight, ARGB_8888);
        webpJNI.AnimDecoderGetNextBitmap(mDecoder, mBitmap);
    }

    @Override
    public AnimatedWebpTexture clone() {
        return null;
    }

    public void update() {
        if(webpJNI.AnimDecoderHasMoreFrames(mDecoder)==false) {
            webpJNI.AnimDecoderReset(mDecoder);
        }
        webpJNI.AnimDecoderGetNextBitmap(mDecoder, mBitmap);
        TextureManager.getInstance().replaceTexture(this);
    }
}