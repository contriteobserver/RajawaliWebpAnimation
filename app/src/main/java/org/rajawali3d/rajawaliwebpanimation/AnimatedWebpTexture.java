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
    private long mDecoder;
    private byte[] mData;

    public AnimatedWebpTexture(String name, int resourceId) throws IOException {
        super(TextureType.DIFFUSE, name);

        Context context = TextureManager.getInstance().getContext();
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        mData = new byte[inputStream.available()];
        long read = inputStream.read(mData);

        mDecoder = webpJNI.AnimDecoder(mData, read);
        mWidth = webpJNI.AnimDecoderGetCanvasWidth(mDecoder);
        mHeight = webpJNI.AnimDecoderGetCanvasHeight(mDecoder);
        mBitmap = Bitmap.createBitmap(mWidth, mHeight, ARGB_8888);
    }

    @Override
    public AnimatedWebpTexture clone() {
        return null;
    }

    public void update() {
        webpJNI.AnimDecoderGetNextBitmap(mDecoder, mBitmap);
        TextureManager.getInstance().replaceTexture(this);

        if(webpJNI.AnimDecoderHasMoreFrames(mDecoder)==false) {
            webpJNI.AnimDecoderReset(mDecoder);
        }
    }
}