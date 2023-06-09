package com.gmxteam.funkydomino.activities.examples;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.SmoothCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class ZoomExample extends BaseExample {

    private static final int CAMERA_WIDTH = 720;

    private static final int CAMERA_HEIGHT = 480;

    private SmoothCamera mSmoothCamera;

    private BitmapTextureAtlas mBitmapTextureAtlas;

    private TextureRegion mFaceTextureRegion;

    /**
         * 
         * @return
         */
    @Override
    public Engine onLoadEngine() {
        Toast.makeText(this, "Touch and hold the scene and the camera will smoothly zoom in.\nRelease the scene it to zoom out again.", Toast.LENGTH_LONG).show();
        this.mSmoothCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, 10, 10, 1.0f);
        return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mSmoothCamera));
    }

    /**
         * 
         */
    @Override
    public void onLoadResources() {
        this.mBitmapTextureAtlas = new BitmapTextureAtlas(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);
        this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
    }

    /**
         * 
         * @return
         */
    @Override
    public Scene onLoadScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());
        final Scene scene = new Scene();
        scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
        final int centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
        final int centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
        scene.attachChild(new Sprite(centerX - 25, centerY - 25, this.mFaceTextureRegion));
        scene.attachChild(new Sprite(centerX + 25, centerY - 25, this.mFaceTextureRegion));
        scene.attachChild(new Sprite(centerX, centerY + 25, this.mFaceTextureRegion));
        scene.setOnSceneTouchListener(new IOnSceneTouchListener() {

            @Override
            public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
                switch(pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        ZoomExample.this.mSmoothCamera.setZoomFactor(5.0f);
                        break;
                    case TouchEvent.ACTION_UP:
                        ZoomExample.this.mSmoothCamera.setZoomFactor(1.0f);
                        break;
                }
                return true;
            }
        });
        return scene;
    }

    /**
         * 
         */
    @Override
    public void onLoadComplete() {
    }
}
