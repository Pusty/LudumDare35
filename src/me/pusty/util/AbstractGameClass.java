package me.pusty.util;

import java.util.HashMap;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public abstract class AbstractGameClass extends Game {
	
	boolean timeRunning=true;
	float tickSpeed=1f;
	int endGame = 0;
	TextureLoader pictureloader;
	SoundLoader soundloader;
	int ticksRunning=0;
	
	HashMap<String,Integer> variables;
	
	RawAnimationHandler animationHandler;
	
	OrthographicCamera camera = null;
	SpriteBatch batch = null;
	BitmapFont font = null;
	
	public int getTicksRunning(){return ticksRunning;}
	public void resetTicks(){ticksRunning=0;}
	public void addTicks(){ticksRunning=ticksRunning+1;}
	
	boolean running=true;
	int idleTime;

	public int getEndGame(){return endGame;}
	public void setEndGame(int inx){endGame=inx;}

	public float getTickSpeed(){return tickSpeed;}
	public void setSpeed(float s){tickSpeed=s;}
	public SoundLoader getSound(){return soundloader;}
	
	
	public AbstractGameClass(){
		pictureloader=new TextureLoader();
		soundloader=new SoundLoader();
		animationHandler=new RawAnimationHandler();
		variables = new HashMap<String,Integer>();
		
	}
	public void startInit() {
		preInit();
		Init();
		postInit();
		
		initStartScreen(true);
	}
	
	public abstract void initStartScreen(boolean start);
	
	public HashMap<String,Integer> getVariable() { return variables; }
	public RawAnimationHandler getAnimationHandler() {
		return animationHandler;
	}
	public boolean isTimeRunning(){return timeRunning;}
	public void setTimeRunning(boolean b){timeRunning=b;}

	public TextureLoader getImageHandler(){return pictureloader;}
	
	public boolean isRunning(){
		return running;
	}
	public void setRunning(boolean b){
		running=b;
	}
	
	public void setIdleTime(int i){
		idleTime = i;
	}
	
	public int getIdleTime() {
		return idleTime;
	}
	
	public abstract void preInit();
	public abstract void Init();
	public abstract void postInit();
	
	
	
	
	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}
	
	public BitmapFont getFont() {
		return font;
	}

	public void setFont(BitmapFont font) {
		this.font = font;
	}
	
	@Override
	public void create () {
			normalInit();

	}
	
	public void normalInit() {
		try {
		//Load Variables
		} catch(Exception e){e.printStackTrace();}
		initializeFBO();
		startInit();
	}
	
	
	@Override
	public void resize(int w,int h) {
		initializeFBO();
	}

	@Override
	public void render () {      
		sleep(30); //SET FPS!
	}
	
	private long diff, start = com.badlogic.gdx.utils.TimeUtils.millis();

	public void sleep(int fps) {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT |  GL20.GL_DEPTH_BUFFER_BIT );
	    if(fps>0){
	      diff = com.badlogic.gdx.utils.TimeUtils.millis() - start;
	      long targetDelay = 1000/fps;
	      if (diff < targetDelay) {
//	    	  initializeFBO(); // remaking the BufferedFrame. May be removed for performance sake! (only there cuz main screen has no background)
	    	  realRender();
	        }else 
	        	start = com.badlogic.gdx.utils.TimeUtils.millis();      
			batch.begin();
	  		batch.draw(fbo.getColorBufferTexture(), 0, 0, camera.viewportWidth, camera.viewportHeight, 0, 0, 1, 1);
	  		batch.end();

	        
	    } 
	}
	  FrameBuffer fbo;
	public void initializeFBO() {
		if(fbo != null) fbo.dispose();
		fbo = new FrameBuffer(Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		if(batch != null) batch.dispose();
		batch = new SpriteBatch();

	}
	
	public void cameraTick() {
		
	}
	public void realRender() {
			fbo.begin();
        	if (isRunning()) {
      	      camera.update();
        		batch.setProjectionMatrix(camera.combined);
        		batch.begin();
        		super.render();
        		batch.end();
        	}
        	fbo.end();
	}
	
	
		@Override
		public void dispose() {
			batch.dispose();
   	    	font.dispose();
   	    	SoundLoader.close();
        }



		
}
