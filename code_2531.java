

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.MathUtils;

import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 15:13:46 - 15.06.2010
 */
public class game extends BaseGameActivity {
        // ===========================================================
        // Constants
        // ===========================================================

        private static final int CAMERA_WIDTH = 720;
        private static final int CAMERA_HEIGHT = 480;

        // ===========================================================
        // Fields
        // ===========================================================

        private Camera mCamera;
        private Texture mShipTexture;
        private Texture mCircleTexture;
        private TextureRegion mShipTextureRegion;
        private TextureRegion mCircleTextureRegion;
       // private TimerHandler spriteTimerHandler;
		// ===========================================================
        // Constructors
        // ===========================================================

        // ===========================================================
        // Getter & Setter
        // ===========================================================

        // ===========================================================
        // Methods for/from SuperClass/Interfaces
        // ===========================================================
        /**
         * Create a Sprite at a specified location
         * @param pX is the X Position of your Sprite
         * @param pY is the Y Position of your Sprite
         */
        private void createSprite(float pX, float pY) {
                Sprite sprite = new Sprite(pX, pY, this.mCircleTextureRegion);
                this.mEngine.getScene().getTopLayer().addEntity(sprite);
        }
        
        
      //createSpriteSpawnTimeHandler;
       
        /**
         * Creates a Timer Handler used to Spawn Sprites
         */
        private void createSpriteSpawnTimerHandler()
        {
                TimerHandler spriteTimerHandler;
               
                this.getEngine().registerUpdateHandler(spriteTimerHandler = new TimerHandler(200,true,new ITimerCallback()
                {                      
                    @Override
                    public void onTimePassed(final TimerHandler pTimerHandler)
                    {
                       
                        //Random Position Generator
								final float xPos = MathUtils.random(30.0f, (CAMERA_WIDTH - 30.0f));
                                final float yPos = MathUtils.random(30.0f, (CAMERA_HEIGHT - 30.0f));
                               
                                createSprite(xPos, yPos);
                    }
                }));
        }
        
        //*/
        @Override
        public Engine onLoadEngine() {
                Toast.makeText(this, "Touch & Drag the ship!", Toast.LENGTH_LONG).show();
                this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
                return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
        }

        @Override
        public void onLoadResources() {
                this.mShipTexture = new Texture(64, 64, TextureOptions.BILINEAR);
                this.mShipTextureRegion = TextureRegionFactory.createFromAsset(this.mShipTexture, this, "gfx/ship.png", 0, 0);
                
                this.mCircleTexture = new Texture(12,12, TextureOptions.BILINEAR);
                this.mCircleTextureRegion = TextureRegionFactory.createFromAsset(this.mCircleTexture, this, "gfx/circle.png",0,0);
        
                this.mEngine.getTextureManager().loadTextures(this.mShipTexture,this.mCircleTexture);
                
        }

        @Override
        public Scene onLoadScene() {
                this.mEngine.registerUpdateHandler(new FPSLogger());
                
                
                
                final Scene scene = new Scene(1);
                scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

                
                final int centerX = (CAMERA_WIDTH - this.mShipTextureRegion.getWidth()) / 2;
                final int centerY = (CAMERA_HEIGHT - this.mShipTextureRegion.getHeight()) / 2;
                final Sprite Ship = new Sprite(centerX, centerY, this.mShipTextureRegion) {
                        @Override
                        public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                                this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
                                return true;
                        }
                };
                Ship.setScale(1);
                scene.getTopLayer().addEntity(Ship);
                scene.registerTouchArea(Ship);
                scene.setTouchAreaBindingEnabled(true);
                createSpriteSpawnTimerHandler();
                return scene;
        }

        @Override
        public void onLoadComplete() {

        }

        // ===========================================================
        // Methods
        // ===========================================================

        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================
}