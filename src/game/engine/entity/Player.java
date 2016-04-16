package game.engine.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import game.engine.main.Config;
import game.engine.world.World;
import me.pusty.game.main.GameClass;
import me.pusty.game.ticks.GameScreen;
import me.pusty.util.AbstractGameClass;
import me.pusty.util.BlockLocation;
import me.pusty.util.PixelLocation;
import me.pusty.util.Velocity;



public class Player extends EntityLiving {

	public Player(int x, int y) {
		super(x, y);
	}
	
	public String getTextureName() {
		if(this.getDirection()==0)
		return "fox_sit";
		if(this.getLastDirection()==1)
			return "fox_right";
		if(this.getLastDirection()==2)
			return "fox_left";
		return "player";
	}
	public String getMovingTexture() {
		return null;
	}
	
	@Override
	public void jump(AbstractGameClass e) {
		if(onGround && !isJumping) {
			traveled = 20;
			isJumping=true;
			onGround=false;
			e.getSound().playClip("jump_player",((GameClass)e).getWorld().getPlayer().getLocation(),getLocation());
		}
	}
	
	
	public boolean hasDirections() { return false; }


	
	public void tickTraveled(AbstractGameClass e) {
		
		GameClass game = (GameClass)e;
		super.tickTraveled(e);
		
		if(isAnimationNull()) {
				setAnimation(e.getAnimationHandler().getAnimation("player_idle")); //idle
		}
		
		World world = game.getWorld();
	
		

		
		
		
		
		{
			if(getDirection()!=0) {
				if(!Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D))
					setDirection(0);
			}
			
			Velocity velo = getVelocity();
			if(velo==null) velo = new Velocity(0,0);
			velo.add(getAddLocation(true));

			
			if(!getJumping()) {
					if(getY()-(getY()/Config.tileSize*Config.tileSize) == 1)
						velo.add(new Velocity(0,-1));
					else
						velo.add(new Velocity(0,-2));
			}
			
	
	
			PixelLocation newLoc = getLocation().addVelocity(velo);
				if(newLoc.x != getX() || newLoc.y != getY()) {
					BlockLocation[] blocks = GameScreen.getAxBHitBox(newLoc,1,2);
					boolean collision = false;
					if(newLoc.x < 0 || newLoc.x > world.getSizeX()*Config.tileSize-Config.tileSize) collision = true;
					if(newLoc.y < 0 || newLoc.y > world.getSizeY()*Config.tileSize-Config.tileSize) collision = true;
					if(!collision)
					for(int b=0;b<blocks.length;b++)
						if(GameScreen.collisonBlock(this,newLoc,blocks[b].getX(),blocks[b].getY(),world.getBlockID(blocks[b].getX(),blocks[b].getY()))) {
							collision = true;
							break;
						}
					if(!collision) 
							getLocation().set(newLoc);
					else {
						collision = false;
						if(velo.getY() != 0f) {					
							newLoc = getLocation().addVelocity(new Velocity(0f,velo.getY()));
							BlockLocation[] blocksY = GameScreen.getAxBHitBox(newLoc,1,2);
							if(newLoc.y < 0 || newLoc.y > world.getSizeY()*Config.tileSize-Config.tileSize) collision = true;
							if(!collision)
							for(int b=0;b<blocksY.length;b++)
								if(GameScreen.collisonBlock(this,newLoc,blocksY[b].getX(),blocksY[b].getY(),world.getBlockID(blocksY[b].getX(),blocksY[b].getY()))) {
									collision = true;
									break;
								}
							if(!collision) 
									getLocation().set(newLoc);
							else if(velo.getY()<0) {
									setGround(true);
							}
							else if(velo.getY()>0) {
								setJumping(false);
							}
						
							
						}
						if((collision || velo.getY() == 0f) && velo.getX() != 0f) {
							newLoc = getLocation().addVelocity(new Velocity(velo.getX(),0f));
							BlockLocation[] blocksX = GameScreen.getAxBHitBox(newLoc,1,2);
							collision = false;
							if(newLoc.x < 0 || newLoc.x > world.getSizeX()*Config.tileSize-Config.tileSize) collision = true;
							if(!collision)
							for(int b=0;b<blocksX.length;b++)
								if(GameScreen.collisonBlock(this,newLoc,blocksX[b].getX(),blocksX[b].getY(),world.getBlockID(blocksX[b].getX(),blocksX[b].getY()))) {
									collision = true;
									break;
								}
							if(!collision) 
									getLocation().set(newLoc);
						}
						
					}
				}

		}
	}

	
}


