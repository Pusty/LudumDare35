package me.pusty.util;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public abstract class Tick implements Screen, InputProcessor  {
	
	public static boolean down;
	public static boolean up;
	public static boolean left;
	public static boolean right;
	
	public static boolean enter;
	public static boolean shift;
	public static boolean pause;
	public static boolean esc;


	
	
	AbstractGameClass engine;
	public Tick(AbstractGameClass engine){
		this.engine = engine;
	}
	public AbstractGameClass E() { return engine; }
	protected static int ticks=5;
	
	public abstract void tick(AbstractGameClass engine,float delta);
	public  abstract void mouse(AbstractGameClass engine,int screenX, int screenY, int pointer, int button);
	public abstract void render(AbstractGameClass e,float delta);
	
	public void genericMouse(AbstractGameClass engine,int type,int screenX, int screenY, int pointer, int button){}
	public boolean keyEvent(AbstractGameClass e,int type,int keycode){return false;}
	
	
	@Override
	public boolean keyDown(int keycode) {
		if(keyEvent(E(),0,keycode)) return true;
		
		switch(keycode) {
			case Keys.UP:
				up = true;
				break;
			case Keys.DOWN:
				down = true;
				break;
			case Keys.SPACE:
				enter = true;
				break;
		
			case Keys.ENTER:
				enter = true;
				break;
			case Keys.SHIFT_LEFT:
				shift=true;
				break;
			case Keys.ESCAPE:
				esc = true;
				break;
			case Keys.W:
				up = true;
				break;
			case Keys.S:
				down = true;
				break;
			case Keys.A:
				left = true;	
				break;
			case Keys.D:
				right = true;	
				break;
			case Keys.LEFT:
				left = true;	
				break;
			case Keys.RIGHT:
				right = true;
				break;
			case Keys.P:
//				if(!engine.getDebugMode())break;
				if(engine.getEndGame()!=0)break;
				if(!pause){
				engine.setTimeRunning(!engine.isTimeRunning());
				pause=true;
				}
				break;
	}
		return true;
	}
	@Override
	public boolean keyUp(int keycode) {
		if(keyEvent(E(),1,keycode)) return true;
		switch(keycode) {
		case Keys.UP:
			up = false;
			break;
		case Keys.DOWN:
			down = false;
			break;
		case Keys.SPACE:
			enter = false;
			break;
	
		case Keys.ENTER:
			enter = false;
			break;
		case Keys.SHIFT_LEFT:
			shift= false;
			break;
		case Keys.ESCAPE:
			esc = false;
			break;
		case Keys.M:
			break;
		case Keys.W:
			up = false;
			break;
		case Keys.S:
			down = false;
			break;
		case Keys.A:
			left = false;	
			break;
		case Keys.D:
			right = false;	
			break;
		case Keys.LEFT:
			left = false;	
			break;
		case Keys.RIGHT:
			right = false;
			break;
		case Keys.P:
			pause= false;
			break;
}
		return true;
	}
	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Ray ray = engine.getCamera().getPickRay(screenX, screenY);
		Vector3 out = new Vector3();
		ray.getEndPoint(out, engine.getCamera().near);
		mouse(E(),(int)out.x,(int)out.y,pointer,button);
		return true;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Ray ray = engine.getCamera().getPickRay(screenX, screenY);
		Vector3 out = new Vector3();
		ray.getEndPoint(out, engine.getCamera().near);
		genericMouse(E(),3,(int)out.x,(int)out.y,pointer,button);
		return true;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Ray ray = engine.getCamera().getPickRay(screenX, screenY);
		Vector3 out = new Vector3();
		ray.getEndPoint(out, engine.getCamera().near);
		genericMouse(E(),4,(int)out.x,(int)out.y,pointer,0);
		return true;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Ray ray = engine.getCamera().getPickRay(screenX, screenY);
		Vector3 out = new Vector3();
		ray.getEndPoint(out, engine.getCamera().near);
		genericMouse(E(),5,(int)out.x,(int)out.y,0,0);
		return true;
	}
	
	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	@Override
	public void render(float delta) {
		if(engine.isTimeRunning())
			tick(E(),delta);
		E().cameraTick();
		
		render(E(),delta);

	}
	
	@Override
	public void resize(int width, int height) {
	}
	@Override
	public void pause() {
	}
	@Override
	public void resume() {
	}
	@Override
	public void hide() {
	}
	@Override
	public void dispose() {
	}
	
}
