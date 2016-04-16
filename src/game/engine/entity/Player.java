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
		if(isFox)   {
			if(this.getLastDirection()==0)
				return "fox_sit";
			if(this.getLastDirection()==1)
				return "fox_right";
			if(this.getLastDirection()==2)
				return "fox_left";
			return "fox_sit";
		}else {
			if(this.getLastDirection()==0)
				return "player";
			if(this.getLastDirection()==1)
				return "player_right";
			if(this.getLastDirection()==2)
				return "player_left";
			return "player_sit";
		}
	}
	public String getMovingTexture() {
		if(isFox)
			return "fox_moving";
		else
			return "player_moving";
	}
	
	int adding=0;
	public Velocity getAddLocation(boolean tick) {
		
		Velocity location = new Velocity(0,0);
		if(direction==1)
			if(isFox && isJumping)
				location.add(new Velocity(3,0));
			else
				location.add(new Velocity(2,0));
		else if(direction==2)
			if(isFox && isJumping)
				location.add(new Velocity(-3,0));
			else
				location.add(new Velocity(-2,0));
		
		if(isJumping) {
			if(isFox) {
					location.add(new Velocity(0, (int)Math.ceil((float)traveled/10)));
			}else
				location.add(new Velocity(0, (int)Math.ceil((float)traveled/10)));
		}
		
		adding++;
		if(adding>5)
			adding=0;
		
			
		return location;
	}
	
	
	@Override
	public void jump(AbstractGameClass e) {
		if(onGround && !isJumping) {
			if(isFox)
				traveled = 20;
			else
				traveled = 20;
			isJumping=true;
			onGround=false;
			e.getSound().playClip("jump_player",((GameClass)e).getWorld().getPlayer().getLocation(),getLocation());
		}
	}
	
	
	public boolean hasDirections() { return false; }

	boolean isFox=true;
	public BlockLocation[] getHitbox(PixelLocation l) {
		if(isFox)
			return GameScreen.getAxBHitBox(l, 2, 1);
		else
			return GameScreen.getAxBHitBox(l, 1, 2);
	}

	boolean wantToChange=false;
	public void wantToChange() {
		wantToChange=true;
	}
	
	public void tickTraveled(AbstractGameClass e) {
		
		GameClass game = (GameClass)e;
		super.tickTraveled(e);
		
		if(isAnimationNull()) {
				setAnimation(e.getAnimationHandler().getAnimation("player_idle")); //idle
		}
		
		World world = game.getWorld();
	
		

		if(wantToChange && onGround) {
			isFox=!isFox;
			BlockLocation[] blocks = getHitbox(getLocation());
			boolean collision = false;
			for(int b=0;b<blocks.length;b++)
				if(GameScreen.collisonBlock(this,getLocation(),blocks[b].getX(),blocks[b].getY(),world.getBlockID(blocks[b].getX(),blocks[b].getY()))) {
					collision = true;
					break;
				}
			isFox=!isFox;
			
			if(!collision) {
				isFox = !isFox;
				wantToChange=false;
			}
		}
		
		
		
		{
			setDirection(1);
//			if(getDirection()!=0) {
//				if(!Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D))
//					setDirection(0);
//			}
			
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
					BlockLocation[] blocks = getHitbox(newLoc);
					boolean collision = false;
					if(!collision)
					for(int b=0;b<blocks.length;b++)
						if(GameScreen.collisonBlock(this,newLoc,blocks[b].getX(),blocks[b].getY(),world.getBlockID(blocks[b].getX(),blocks[b].getY()))) {
							collision = true;
							break;
						}
					if(!collision)  {
							getLocation().set(newLoc);
							onGround=false;
					}else {
						collision = false;
						if(velo.getY() != 0f) {					
							newLoc = getLocation().addVelocity(new Velocity(0f,velo.getY()));
							BlockLocation[] blocksY = getHitbox(newLoc);
							if(!collision)
							for(int b=0;b<blocksY.length;b++)
								if(GameScreen.collisonBlock(this,newLoc,blocksY[b].getX(),blocksY[b].getY(),world.getBlockID(blocksY[b].getX(),blocksY[b].getY()))) {
									collision = true;
									break;
								}
							if(!collision)  {
								getLocation().set(newLoc);
								onGround = false;
							}
							else if(velo.getY()<0) {
									setGround(true);
							}
							else if(velo.getY()>0) {
								setJumping(false);
							}
						
							
						}
						if((collision || velo.getY() == 0f) && velo.getX() != 0f) {
							newLoc = getLocation().addVelocity(new Velocity(velo.getX(),0f));
							BlockLocation[] blocksX = getHitbox(newLoc);
							collision = false;
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


