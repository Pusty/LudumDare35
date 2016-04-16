package game.engine.entity;

import me.pusty.game.main.GameClass;
import me.pusty.util.AbstractGameClass;
import me.pusty.util.PixelLocation;
import me.pusty.util.RawAnimation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class Entity{
	
	PixelLocation location;
	RawAnimation animation=null;
	float sizex;
	float sizez;
	boolean init=false;

	public Entity(int x,int z){
		location=new PixelLocation(x,z);
	}
	
	public void playAnimation() {
		
	}

	
	public void tickTraveled(AbstractGameClass game) {
		
	}


	public PixelLocation getLocation(){
		return location;
	}
	public int getX(){
		return location.getX();
	}
	public int getY(){
		return location.getY();
	}

	

	public void render(AbstractGameClass e,SpriteBatch g) {
		try {
			TextureRegion image = e.getImageHandler().getImage(getImage());
			PixelLocation cam = ((GameClass)e).getCamLocation();
			PixelLocation move = new PixelLocation(getX() - cam.getX(), getY() - cam.getY());
			g.draw(image,move.getX(),move.getY());
		} catch(Exception ex) { System.err.println(getImage()); }
	}
	
	String img=null;
	
	
	public String getTextureName() {
		return "empty";
	}
	
	public String getImage() {
		if(img!=null)
			return img;
		return getTextureName();
	}
	public void setAnimation(RawAnimation a){
		if(a!=null)
			animation=a.getWorkCopy();
		else
			animation=null;
	}
	public boolean isAnimationNull(){
		return animation==null;
	}
	public void initTick(AbstractGameClass engine,int ind) {
		if(!init) {
			//INIT HERE
			init = true;
		}
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
	}
	
	public void setDefault(){
		img=null;
	}
	
	public void setImage(String i) {
		img = i;
	}
	
	
}
