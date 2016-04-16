package game.engine.entity;



import me.pusty.game.main.GameClass;
import me.pusty.util.AbstractGameClass;
import me.pusty.util.PixelLocation;
import me.pusty.util.Velocity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class EntityLiving extends Entity {

	int direction = 0;
	int lastDirection = 1;
	boolean isJumping = false;
	boolean onGround = true;
	Velocity velocity = null;
	public EntityLiving(int x, int z) {
		super(x, z);
	}
	
	public Velocity getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Velocity v) {
		velocity = v;
	}
	
	public int getLastDirection() {
		return lastDirection;
	}
	public int getDirection() {
		return direction;
	}
	public void setJumping(boolean b) {
		isJumping=b;
	}
	
	
	public boolean getJumping() {
		return isJumping;
	}
	
	public boolean onGround() {
		return onGround;
	}
	
	public void setGround(boolean b) {
		onGround=b;
	}
	
	public void jump(AbstractGameClass e) {
		if(onGround && !isJumping) {
			traveled = 20;
			isJumping=true;
			onGround=false;
			e.getSound().playClip("jump",((GameClass)e).getWorld().getPlayer().getLocation(),getLocation());
		}
	}
	
	public void setDirection(int d) {
		direction = d;
		if(direction!=0)
			lastDirection=direction;
	}
	public void setLastDirection(int d) {
			lastDirection=d;
	}
	public String getMovingTexture() {
		return null;
	}
	public String getAirTexture() { return "empty"; }
	
	public boolean hasDirections() { return false; }
	
	public String getImage() {
		if(img!=null)
			return img;
		if(isJumping) {
			float percent = ((float)3-(runningTraveled/5))/3;
			int frame = Math.min(1,(int)(percent*2))  ; // frame = process * framecount
			return getAirTexture()+"_"+frame;
		}else if(getDirection() != 0) {
				float percent = ((float)getSpeed()-runningTraveled)/getSpeed();
				int frame = Math.min(3,(int)(percent*4))  ; // frame = process * framecount
				return getMovingTexture()+"_"+frame;
		}
		return getTextureName();
	}
	
	int runningTraveled=0;
	public int getSpeed() {
		return 15;
	}

	public void render(AbstractGameClass e,SpriteBatch g) {
		try {
			TextureRegion image = e.getImageHandler().getImage(getImage());
			PixelLocation cam = ((GameClass)e).getCamLocation();
			PixelLocation move = new PixelLocation(getX() - cam.getX(), getY() - cam.getY());
			g.draw(image,move.getX(),move.getY());
			
			
		} catch(Exception ex) { System.err.println(getImage()); }
	}
	
	
	public void renderTick(AbstractGameClass engine,int ind){
		if(animation!=null) {			
			String img = animation.getFrame();
			if(img!=null)
			setImage(img);
			else
			{setAnimation(null);setDefault();}
		}else if(img!=null)
			setDefault();
		
		if(engine.isTimeRunning())
		runningTraveled++;
		if(runningTraveled>getSpeed())
			runningTraveled=0;
		if(getDirection()==0)
			runningTraveled=0;
	}
	
	public Velocity getAddLocation(boolean tick) {
		
		Velocity location = new Velocity(0,0);
		if(direction==1)
			location.add(new Velocity(1,0));
		else if(direction==2)
			location.add(new Velocity(-1,0));
		
		if(isJumping)
				location.add(new Velocity(0, (int)Math.ceil((float)traveled/10)));
		
			
		return location;
	}
	
	
	

	boolean setDirection = false;
	int setDirectionInt = 0;
	boolean setDirectionNull = false;
	
	public void queueDirection(int d) {
		setDirection = true;
		if(d == 0) {
			setDirectionNull = true;
		}else {
			setDirectionNull = false;
			setDirectionInt = d;
		}
		
	}
	int traveled = 0;

	public int getTraveled() {
		return traveled;
	}
	public void tickTraveled(AbstractGameClass game) {
		if(traveled > 0)
			traveled--;
		
		if(traveled == 0 && isJumping)
			isJumping = false;
		
		if(setDirection || setDirectionNull) {
			if(setDirectionNull && setDirectionInt==0) 
				setDirection(0);
			else
				setDirection(setDirectionInt);
			setDirectionInt = 0;
			setDirection = false;
			setDirectionNull = false;
		}
	}

	
	
}
