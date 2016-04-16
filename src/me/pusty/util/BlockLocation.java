package me.pusty.util;

import game.engine.main.Config;

public class BlockLocation {
	public int x;
	public int y;
	public BlockLocation(int x,int y){
		this.x=x;
		this.y=y;
	}
	public PixelLocation toPixelLocation() {
		return new PixelLocation(this.x*Config.tileSize,this.y*Config.tileSize);
	}
	public BlockLocation sub(BlockLocation l){
		int cx=getX()-l.getX();
		int cy=getY()-l.getY();
		return new BlockLocation(cx,cy);
	}
	
	public static BlockLocation getNorm(BlockLocation v) {
		double distance = getDistance(v,new BlockLocation(0,0));
		int cx=(int) (v.x/distance);
		int cy=(int) (v.y/distance);
		return new BlockLocation(cx,cy);
	}
	
	
	public BlockLocation subToDirection(BlockLocation l){
		int cx=l.getX()-getX();
		int cy=l.getY()-getY();
		return new BlockLocation((int)Math.sin(cx)/2,(int)Math.sin(cy)/2);
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
	public void setX(int x){this.x=x;}
	public void setY(int y){this.y=y;}
	public BlockLocation clone(){return new BlockLocation(x,y);}
    public static double getDistance(BlockLocation l,BlockLocation l2){
    	return Math.sqrt(((l2.getX()-l.getX())*(l2.getX()-l.getX()))+((l2.getY()-l.getY())*(l2.getY()-l.getY())));
    }
    

	public BlockLocation add(BlockLocation a) {
		int cx = x + a.x;
		int cy = y + a.y;
 
		return new BlockLocation(cx,cy);
	}
 

 


	
	public boolean sameAs(BlockLocation loc){
		if(this.x==loc.x && this.y==loc.y)return true;
		return false;
	}
	
	
	
	public String toString(){
		return x+"|"+y;
	}

	public BlockLocation multiply(BlockLocation BlockLocation) {
		int cx = x*BlockLocation.x;
		int cy = y*BlockLocation.y;
		return new BlockLocation(cx,cy);
	}

	public void set(BlockLocation l) {
		this.setX(l.getX());
		this.setY(l.getY());
	}

	
	public BlockLocation redirect() {
		return new BlockLocation(-x,-y);
	}


}
