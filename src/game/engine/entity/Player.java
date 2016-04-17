package game.engine.entity;

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
			return "fox_right";
		}else {
			return "player_right";
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
	
	public String getAirTexture() {
		if(isFox)
			return "fox_air"; 
		else
			return "player_air"; 
	}
	
	@Override
	public String getImage() {
		String prefix = wantToChange?"EV":"";
		
		if(img!=null)
			return img;
		if(isJumping) {
			float percent = ((float)3-(runningTraveled/5))/3;
			int frame = Math.min(1,(int)(percent*2))  ; // frame = process * framecount
			return getAirTexture()+prefix+"_"+frame;
		}else if(getDirection() != 0) {
				float percent = ((float)getSpeed()-runningTraveled)/getSpeed();
				int frame = Math.min(3,(int)(percent*4))  ; // frame = process * framecount
				return getMovingTexture()+prefix+"_"+frame;
		}
		return getTextureName();
	}
	
	@Override
	public void jump(AbstractGameClass e) {
		if(onGround && !isJumping) {
			if(isFox) {
				traveled = 15;
				if(isAnimationNull())
					setAnimation(e.getAnimationHandler().getAnimation("fox_jump"));
			}
			else {
				traveled = 20;
				if(isAnimationNull())
					setAnimation(e.getAnimationHandler().getAnimation("player_jump"));
			}
			isJumping=true;
			onGround=false;
			e.getSound().playClip("jump");
		}
	}
	
	
	public boolean hasDirections() { return false; }

	boolean isFox=false;
	public BlockLocation[] getHitbox(PixelLocation l) {
		if(isFox)
			return GameScreen.getAxBHitBox(l, 1, 1);
		else
			return GameScreen.getAxBHitBox(l, 1, 2);
	}

	boolean wantToChange=false;
	public void wantToChange(AbstractGameClass a) {
		wantToChange=true;
	}
	int changeTimer=-1;
	public int getChangeTimer() {
		return changeTimer;
	}
	
	public void tickTraveled(AbstractGameClass e) {
		
		GameClass game = (GameClass)e;
		super.tickTraveled(e);
		
		if(isAnimationNull()) {
				setAnimation(e.getAnimationHandler().getAnimation("player_idle")); //idle
		}
		
		World world = game.getWorld();
	
		if(changeTimer>0)
			changeTimer--;
		if(changeTimer==0) {
			changeTimer--;
			wantToChange(game);
		}

		if(wantToChange && onGround) {
			isFox=!isFox;
			
			BlockLocation[] blocks = getHitbox(getLocation());
			boolean collision = false;
			for(int b=0;b<blocks.length;b++)
				if(GameScreen.collisonBlock(game,this,getLocation(),blocks[b].getX(),blocks[b].getY(),world.getBlockID(blocks[b].getX(),blocks[b].getY()))) {
					collision = true;
					break;
				}
			isFox=!isFox;
			
			if(!collision) {
				isFox = !isFox;				
				if(isFox)
					changeTimer=30;
				else
					changeTimer=-1;
				e.getSound().playClip("change");
				if(isFox)
					setAnimation(game.getAnimationHandler().getAnimation("player_tran"));
				else
					setAnimation(game.getAnimationHandler().getAnimation("fox_tran"));
				wantToChange=false;
				
			}
		}
		

		
		
		{
			setDirection(1);
			
			Velocity velo = getVelocity();
			if(velo==null) velo = new Velocity(0,0);
			velo.add(getAddLocation(true));

			
			if(!getJumping()) {
					if(getY()-(getY()/Config.tileSize*Config.tileSize) == 1)
						velo.add(new Velocity(0,-1));
					else
						velo.add(new Velocity(0,-2));
			}
			
	
			PixelLocation oldLoc = getLocation().clone();
	
			PixelLocation newLoc = getLocation().addVelocity(velo);
				if(newLoc.x != getX() || newLoc.y != getY()) {
					BlockLocation[] blocks = getHitbox(newLoc);
					boolean collision = false;
					if(!collision)
					for(int b=0;b<blocks.length;b++)
						if(GameScreen.collisonBlock(game,this,newLoc,blocks[b].getX(),blocks[b].getY(),world.getBlockID(blocks[b].getX(),blocks[b].getY()))) {
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
								if(GameScreen.collisonBlock(game,this,newLoc,blocksY[b].getX(),blocksY[b].getY(),world.getBlockID(blocksY[b].getX(),blocksY[b].getY()))) {
									collision = true;
									break;
								}
							if(!collision)  {
								getLocation().set(newLoc);
								onGround = false;
							}
							else if(velo.getY()<0) {
									if(!onGround) {
									setGround(true);
									e.getSound().playClip("ground");
									}
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
								if(GameScreen.collisonBlock(game,this,newLoc,blocksX[b].getX(),blocksX[b].getY(),world.getBlockID(blocksX[b].getX(),blocksX[b].getY()))) {
									collision = true;
									break;
								}
							if(!collision) 
									getLocation().set(newLoc);
						
						}
						
					}
					
				}
				
				if(getLocation().getY() <= 0 || (oldLoc.getX() == getLocation().getX()))  {
					game.setGameOver(true);
					game.setTimeRunning(false);
					e.getSound().playClip("death");
					game.setTimeout(30);
				}

		}
	}

	
}


