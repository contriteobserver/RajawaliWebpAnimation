package org.rajawali3d.rajawaliwebpanimation;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.view.SurfaceView;

public class MainActivity extends AppCompatActivity {
    Renderer renderer;
    SurfaceView view;
    ArcballCamera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.glsl_content);
        camera = new ArcballCamera(this, view);
        renderer = new AnimatedRenderer(this);

        view.setSurfaceRenderer(renderer);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                renderer.onTouchEvent(motionEvent);
                return false;
            }
        });
    }

    class AnimatedRenderer extends Renderer {
        AnimatedWebpTexture texture;

        public AnimatedRenderer(Context context) {
            super(context);
        }

        @Override
        protected void initScene() {
            try {
                DirectionalLight key = new DirectionalLight(-3,4,5);
                getCurrentScene().setBackgroundColor(Color.CYAN);
                key.setPower(5.0f);
                getCurrentScene().addLight(key);

                texture = new AnimatedWebpTexture("checkerboard", R.raw.checkerboard01);
                Material material = new Material();
                material.addTexture(texture);
                material.setColorInfluence(0);
                material.setDiffuseMethod(new DiffuseMethod.Lambert());
                material.setAmbientColor(Color.LTGRAY);
                material.enableLighting(true);

                Object3D obj = new Cube(2);
                obj.setMaterial(material);
                obj.setTransparent(true);
                getCurrentScene().addChild(obj);

                RotateOnAxisAnimation anim = new RotateOnAxisAnimation(new Vector3(3,4,5), 360);
                anim.setDurationMilliseconds(4321);
                anim.setRepeatMode(Animation.RepeatMode.INFINITE);
                anim.setTransformable3D(obj);
                getCurrentScene().registerAnimation(anim);
                //anim.play();

                getCurrentCamera().setPosition(3,4,5);
                getCurrentCamera().setLookAt(obj.getPosition());
            } catch (Exception e) {
                Log.d(getLocalClassName(), e.getMessage());
            }
        }

        @Override
        protected void onRender(long ellapsedRealtime, double deltaTime) {
            super.onRender(ellapsedRealtime, deltaTime);
            try {
                texture.update();
            } catch (Exception e) {
                Log.d(getLocalClassName(), e.getMessage());
            }
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }
    }
}