package me.pusty.game.ticks;

import game.engine.entity.Entity;
import game.engine.entity.Player;
import game.engine.main.Config;
import game.engine.world.Chunk;
import game.engine.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;











import me.pusty.game.main.GameClass;
import me.pusty.util.AbstractGameClass;
import me.pusty.util.BlockLocation;
import me.pusty.util.PixelLocation;
import me.pusty.util.Tick;

public class GameScreen extends Tick{

	public GameScreen(AbstractGameClass engine) {
		super(engine);
	}

	

	@Override
	public boolean keyEvent(AbstractGameClass e,int type,int keycode) {
		Player player = ((GameClass)e).getWorld().getPlayer();
		switch(keycode) {
		case Keys.D:
			if(type==0)
				player.queueDirection(1);
			return true;
		case Keys.A:
			if(type==0)
				player.queueDirection(2);
			return true;
		case Keys.SPACE:
			if(type==0)
				player.jump(e);
			return true;
		}
		
		
		return false;
	}
	
	
	
	@Override
	public void show() {
		
	}

	@Override
	public void tick(AbstractGameClass e, float delta) {
		GameClass game = (GameClass)e;	
		World world = game.getWorld();
		if(ticks>0)
			ticks--;
		if(ticks==0)
			ticks=50;
		
		Player player = world.getPlayer();
		
		
		
		game.cameraTick();

		player.tickTraveled(e);
			
			for(int entityIndex=0;entityIndex<world.getEntityArray().length;entityIndex++) {
				Entity entity = world.getEntityArray()[entityIndex];
				if(entity==null)continue;
				entity.tickTraveled(e);
			}
		
	}
	
	public static boolean collisonBlock(Entity entity,PixelLocation loc,int x,int y,int id) {
		if(id==-1) return false;
		if(id==0)return false;
		return true;
	}
	
	/** a=x , b=y*/
	public static BlockLocation[] getAxBHitBox(PixelLocation loc,int a,int b) {
		BlockLocation[][] bl = new BlockLocation[a*b][];
		int adding=0;
		int indexU=0;
		for(int aI=0;aI<a;aI++)
			for(int bI=0;bI<b;bI++) {
				bl[indexU] = loc.add(new PixelLocation(Config.tileSize*aI,Config.tileSize*bI)).toBlocks();
				adding=adding+bl[indexU].length;
				indexU++;
			}
		BlockLocation[] result = new BlockLocation[adding];
		int index = 0;
		
		for(int u=0;u<bl.length;u++)
			for(int i=0;i<bl[u].length;i++) {
				result[index] = bl[u][i];
				index++;
			}
		return result;
	}
	
	
	

	@Override
	public void mouse(AbstractGameClass engine, int screenX, int screenY,
			int pointer, int button) {
		
	}

	@Override
	public void render(AbstractGameClass e, float delta) {
		SpriteBatch batch  = e.getBatch();
		batch.setColor(159f/255f,201f/255f,199f/255f,1f);
		batch.draw(e.getImageHandler().getImage("empty"),0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.setColor(1,1,1,1);
		
		GameClass game = (GameClass)e;	
		World world = game.getWorld();
		
		

		
		for(int chunkIndex=0;chunkIndex<game.getWorld().getChunkArray().length;chunkIndex++) {
			Chunk c = game.getWorld().getChunkArray()[chunkIndex];
			int blockID = 0;
			BlockLocation blockLocation;
			if(c.isEmptyWorld())continue;
			if(PixelLocation.getDistance(new BlockLocation(c.getChunkX() * c.getSizeX()
					+ 8, c.getChunkY() * c.getSizeY() + 8).toPixelLocation(),game.getCamLocation()) > 8*8*3)continue;
			for (int by = 0; by < c.getSizeY(); by++) {
				for (int bx = 0; bx < c.getSizeX(); bx++) {
					blockID =  c.getBlockID(bx, by);
					blockLocation = new BlockLocation(c.getChunkX() * c.getSizeX()
							+ bx, c.getChunkY() * c.getSizeY() + by);
						renderBlock(e,batch,blockLocation.getX(), blockLocation.getY(),blockID);
				}
			}
		}
		
		for(int entityIndex=0;entityIndex<world.getEntityArray().length;entityIndex++) {
			Entity entity = world.getEntityArray()[entityIndex];
			if(entity==null)continue;
			entity.renderTick(e, entityIndex);
			entity.render(e, batch);
		}
		world.getPlayer().renderTick(e, -1);
		world.getPlayer().render(e, batch);

	
	}

	private void renderBlock(AbstractGameClass e,SpriteBatch b,int x,int y,int id) {
		if(id<0) return;
		PixelLocation cam = ((GameClass)e).getCamLocation();
		PixelLocation move = new PixelLocation( x*Config.tileSize - cam.getX(), y*Config.tileSize - cam.getY());
//		if(true) { //Animated block
//			RawAnimation an = e.getAnimationHandler().getAnimation("tile_"+id);
//			int frame = ((int)Math.floor(((float)(50-ticks)/50)*an.getFrameDelays().length));
//			String textureName = an.getImage(frame);
//			
//			b.draw(e.getImageHandler().getImage(textureName), move.getX(), move.getY());
//		}else
			b.draw(e.getImageHandler().getImage("tile_"+id), move.getX(), move.getY());
	}

}
