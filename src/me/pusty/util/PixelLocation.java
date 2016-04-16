package me.pusty.util;

import game.engine.main.Config;

public class PixelLocation {
	public int x;
	public int y;
	public PixelLocation(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public PixelLocation sub(PixelLocation l){
		int cx=getX()-l.getX();
		int cy=getY()-l.getY();
		return new PixelLocation(cx,cy);
	}
	
	public BlockLocation toBlock() {
		return new BlockLocation((int)Math.round((float)x/Config.tileSize),(int)Math.round((float)y/Config.tileSize));
	}
	
	public BlockLocation[] toBlocks() {
		BlockLocation[] result = null;
		if(x%Config.tileSize==0 && y%Config.tileSize==0) {
			result = new BlockLocation[1];
			result[0] = new BlockLocation(x/Config.tileSize,y/Config.tileSize);
		}else if(x%Config.tileSize==0 && y%Config.tileSize!=0) {
			result = new BlockLocation[2];
			result[0] = new BlockLocation(x/Config.tileSize,(int) Math.ceil((double)y/Config.tileSize));
			result[1] = new BlockLocation(x/Config.tileSize,(int) Math.floor((double)y/Config.tileSize));
		}else if(x%Config.tileSize!=0 && y%Config.tileSize==0) {
			result = new BlockLocation[2];
			result[0] = new BlockLocation((int)Math.ceil((double)x/Config.tileSize),y/Config.tileSize);
			result[1] = new BlockLocation((int)Math.floor((double)x/Config.tileSize),y/Config.tileSize);
		}else {
			result = new BlockLocation[4];
			result[0] = new BlockLocation((int)Math.ceil((double)x/Config.tileSize),(int) Math.ceil((double)y/Config.tileSize));
			result[1] = new BlockLocation((int)Math.floor((double)x/Config.tileSize),(int) Math.floor((double)y/Config.tileSize));	
			result[2] = new BlockLocation((int)Math.floor((double)x/Config.tileSize),(int) Math.ceil((double)y/Config.tileSize));
			result[3] = new BlockLocation((int)Math.ceil((double)x/Config.tileSize),(int) Math.floor((double)y/Config.tileSize));
		}
		return result;
	}
	
	public static PixelLocation getNorm(PixelLocation v) {
		double distance = getDistance(v,new PixelLocation(0,0));
		int cx=(int) (v.x/distance);
		int cy=(int) (v.y/distance);
		return new PixelLocation(cx,cy);
	}
	
	
	public PixelLocation subToDirection(PixelLocation l){
		int cx=l.getX()-getX();
		int cy=l.getY()-getY();
		return new PixelLocation((int)Math.sin(cx)/2,(int)Math.sin(cy)/2);
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
	public void setX(int x){this.x=x;}
	public void setY(int y){this.y=y;}
	public PixelLocation clone(){return new PixelLocation(x,y);}
    public static int getDistance(PixelLocation l,PixelLocation l2){
    	return (int)Math.sqrt(((l2.getX()-l.getX())*(l2.getX()-l.getX()))+((l2.getY()-l.getY())*(l2.getY()-l.getY())));
    }
    

	public PixelLocation add(PixelLocation a) {
		int cx = x + a.x;
		int cy = y + a.y;
 
		return new PixelLocation(cx,cy);
	}
 

 


	
	public boolean sameAs(PixelLocation loc){
		if(this.x==loc.x && this.y==loc.y)return true;
		return false;
	}
	
	
	
	public String toString(){
		return x+"|"+y;
	}

	public PixelLocation multiply(PixelLocation BlockLocation) {
		int cx = x*BlockLocation.x;
		int cy = y*BlockLocation.y;
		return new PixelLocation(cx,cy);
	}

	public void set(PixelLocation l) {
		this.setX(l.getX());
		this.setY(l.getY());
	}

	
	public PixelLocation redirect() {
		return new PixelLocation(-x,-y);
	}

	public PixelLocation addVelocity(Velocity velo) {
		PixelLocation p = new PixelLocation(x,y);
		p = p.add(new PixelLocation(Math.round(velo.getX()),Math.round(velo.getY())));
		return p;
	}


}
