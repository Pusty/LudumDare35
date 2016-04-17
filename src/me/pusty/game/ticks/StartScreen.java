package me.pusty.game.ticks;

import game.engine.main.Config;
import game.engine.world.Chunk;
import game.engine.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.pusty.game.main.GameClass;
import me.pusty.util.AbstractGameClass;
import me.pusty.util.BlockLocation;
import me.pusty.util.PixelLocation;
import me.pusty.util.Tick;

public class StartScreen extends Tick{

	boolean startScreen=true;
	public StartScreen(AbstractGameClass engine,boolean sScreen) {
		super(engine);
		startScreen=sScreen;
	}
	
	public boolean isStartScreen() {
		return startScreen;
	}

	

	@Override
	public boolean keyEvent(AbstractGameClass e,int type,int keycode) {
			if(type==0) {
				e.getSound().playClip("win");
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
		engine.getSound().playClip("win");
		((GameClass)engine).startGame();
	}

	@Override
	public void render(AbstractGameClass e, float delta) {
//		SpriteBatch batch  = e.getBatch();
//		int index = Math.min(3, ((int)Math.floor(((float)(50-ticks)/50)*4)));
//		batch.draw(e.getImageHandler().getImage("title_"+index),0,0);
		
			SpriteBatch batch  = e.getBatch();
			batch.setColor(179f/255f,191f/255f,168f/255f,1f);
			batch.draw(e.getImageHandler().getImage("empty"),0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
			batch.setColor(1,1,1,1);
			
			GameClass game = (GameClass)e;	
			World world = game.getWorld();
			
			

			BlockLocation playerLocation = game.getWorld().getPlayer().getLocation().toBlock();
			int playerCX = (playerLocation.getX()/16)-1;
			for(int chunkIndex=0;chunkIndex<game.getWorld().getChunkArray().length;chunkIndex++) {
				Chunk c = game.getWorld().getChunkArray()[chunkIndex];			
				batch.draw(e.getImageHandler().getImage("background_"+0), ((playerCX+c.getChunkX()) * c.getSizeX())*Config.tileSize - ((GameClass)e).getCamLocation().getX(), 
						((c.getChunkY()) * c.getSizeY())*Config.tileSize - ((GameClass)e).getCamLocation().getY()); 
			}
			for(int chunkIndex=0;chunkIndex<game.getWorld().getChunkArray().length;chunkIndex++) {
				Chunk c = game.getWorld().getChunkArray()[chunkIndex];
				int blockID = 0;
				BlockLocation blockLocation;
				if(c.isEmptyWorld())continue;
				for (int by = 0; by < c.getSizeY(); by++) {
					for (int bx = 0; bx < c.getSizeX(); bx++) {
						blockID =  c.getBlockID(bx, by);
						blockLocation = new BlockLocation((playerCX+c.getChunkX()) * c.getSizeX()
								+ bx, c.getChunkY() * c.getSizeY() + by);
							GameScreen.renderBlock(e,batch,blockLocation.getX(), blockLocation.getY(),blockID);
					}
				}
			}
			world.getPlayer().renderTick(e, -1);
			world.getPlayer().render(e, batch);
			
			
		if(isStartScreen()) {
			batch.draw(e.getImageHandler().getImage("startScreen"),0,0);
		}else {
			batch.draw(e.getImageHandler().getImage("endScreen"),0,0);
		}
		
		
	
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
