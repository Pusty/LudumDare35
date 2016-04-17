package me.pusty.game.ticks;

import game.engine.entity.Entity;
import game.engine.entity.Player;
import game.engine.main.Config;
import game.engine.world.Chunk;
import game.engine.world.ChunkGenerator;
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
		case Keys.Q:
			if(type==0)
				player.wantToChange(e);
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

	ChunkGenerator chunkGenerator = null;
	
	@Override
	public void tick(AbstractGameClass e, float delta) {
		GameClass game = (GameClass)e;	
		World world = game.getWorld();
		if(ticks>0)
			ticks--;
		if(ticks==0)
			ticks=50;
		
		Player player = world.getPlayer();
		
		if(chunkGenerator==null)
			chunkGenerator = new ChunkGenerator();

		
		
		
		int playerCXOld = (game.getWorld().getPlayer().getLocation().toBlock().getX()/16)-1;
		
		
		
		
		game.cameraTick();

		player.tickTraveled(e);
		

		
		int playerCXNew = (game.getWorld().getPlayer().getLocation().toBlock().getX()/16)-1;
		
		if(playerCXOld!=playerCXNew) {
			world.getChunkArray()[0].fill(world.getChunkArray()[1]);
			world.getChunkArray()[1].fill(world.getChunkArray()[2]);
			
			chunkGenerator.generate(world);
		}
		
			
			for(int entityIndex=0;entityIndex<world.getEntityArray().length;entityIndex++) {
				Entity entity = world.getEntityArray()[entityIndex];
				if(entity==null)continue;
				entity.tickTraveled(e);
			}
		
	}
	
	public static boolean collisonBlock(AbstractGameClass e,Entity entity,PixelLocation loc,int x,int y,int id) {
		if(id==-1) return false;
		if(id>=9) {
			((GameClass)e).setFinished(true);
			((GameClass)e).setTimeRunning(false);
			((GameClass)e).setTimeout(30);
			e.getSound().playClip("win");
			return false;
		}
		if(id>2)
			return false;
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
		GameClass game = (GameClass)engine;
			if(PixelLocation.getDistance(new PixelLocation(screenX,screenY), new PixelLocation(28+16,28+16)) < 16) {
				game.getWorld().getPlayer().wantToChange(engine);
			}else {
				game.getWorld().getPlayer().jump(game);
			}
				
	}

	@Override
	public void render(AbstractGameClass e, float delta) {
		SpriteBatch batch  = e.getBatch();
//		batch.setColor(159f/255f,201f/255f,199f/255f,1f);
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
						renderBlock(e,batch,blockLocation.getX(), blockLocation.getY(),blockID);
				}
			}
		}
		
		world.getPlayer().renderTick(e, -1);
		world.getPlayer().render(e, batch);
	}

	public static void renderBlock(AbstractGameClass e,SpriteBatch b,int x,int y,int id) {
		if(id<0) return;
		PixelLocation cam = ((GameClass)e).getCamLocation();
		PixelLocation move = new PixelLocation( x*Config.tileSize - cam.getX(), y*Config.tileSize - cam.getY());
			b.draw(e.getImageHandler().getImage("tile_"+id), move.getX(), move.getY());
	}

}
