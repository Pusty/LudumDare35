package me.pusty.game.ticks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.pusty.game.main.GameClass;
import me.pusty.util.AbstractGameClass;
import me.pusty.util.PixelLocation;
import me.pusty.util.Tick;

public class StartScreen extends Tick{

	public StartScreen(AbstractGameClass engine) {
		super(engine);
	}

	

	@Override
	public boolean keyEvent(AbstractGameClass e,int type,int keycode) {
			if(type==0) {
				e.getSound().playClip("start",null,null);
				((GameClass)e).startGame();
				return true;
			}
		
		return false;
	}
	
	
	
	@Override
	public void show() {
		
	}

	int ticks = 0;
	@Override
	public void tick(AbstractGameClass e, float delta) {
		if(ticks>0)ticks--;
		if(ticks==0)ticks=50;
	}
	

	@Override
	public void mouse(AbstractGameClass engine, int screenX, int screenY,
			int pointer, int button) {
		
	}

	@Override
	public void render(AbstractGameClass e, float delta) {
		SpriteBatch batch  = e.getBatch();
		int index = Math.min(3, ((int)Math.floor(((float)(50-ticks)/50)*4)));
//		batch.draw(e.getImageHandler().getImage("title_"+index),0,0);
		

		
	}
	
	
	public static void renderSmallText(AbstractGameClass en,SpriteBatch g,PixelLocation loc,String txt){
		if(txt == null || txt.trim() == "")return;
		for(int a=0;a<txt.length();a++) {
			TextureRegion image = en.getImageHandler().getImage("small_"+txt.toUpperCase().toCharArray()[a]);
			if(image==null) continue;
			g.draw(image, loc.getX() + a*5, loc.getY() ,image.getRegionWidth(),image.getRegionHeight());
		}
	}



}
