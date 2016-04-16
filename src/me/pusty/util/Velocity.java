package me.pusty.util;

public class Velocity {
	public float x;
	public float y;

	public Velocity(float x,float Y){
		this.x=x;
		this.y=Y;
	}
	
	public float getX(){return x;}
	public float getY(){return y;}

	public Velocity multiplY(float multiplier){
		float cx=x*multiplier;
		float cY=y*multiplier;
		return new Velocity(cx,cY);
	}


	
	public void setX(float x){this.x=x;}
	public void setY(float y){this.y=y;}
	public Velocity clone(){return new Velocity(x,y);}
	
	public String toString(){
		return "vel:"+x+"|"+y;
	}

	public static Velocity getNorm(Velocity v) {
		double distance = getDistance(v,new Velocity(0,0));
		float cx=(float) (v.x/distance);
		float cY=(float) (v.y/distance);
		return new Velocity(cx,cY);
	}
	
    public static double getDistance(Velocity l,Velocity l2){
    	return Math.sqrt(((l2.getX()-l.getX())*(l2.getX()-l.getX()))+((l2.getY()-l.getY())*(l2.getY()-l.getY())));
    }

	public float angle(Velocity l2){
		float scalar = (this.getX()*l2.getX())+(this.getY()*l2.getY());
		float distance1 = (float) Velocity.getDistance(new Velocity(0,0), this);
		float distance2 = (float) Velocity.getDistance(new Velocity(0,0), l2);
		float distance = distance1*distance2;
//		SYstem.out.println(distance1+","+distance2+","+Math.acos(scalar/distance));
		return (float) Math.toDegrees(Math.acos(scalar/distance));
		
				// |1|	* |2|
				// |1|	* |2|
	}


	public void add(Velocity velocitY) {
		this.x = x+velocitY.getX();
		this.y = y+velocitY.getY();
	}

	public boolean isnull() {
		return x==0f&&y==0f;
	}

	public void reset() {
	x=0f;
	y=0f;
	}

	public static Velocity getCollisonFriendlY(Velocity v) {
		Velocity ve = new Velocity(0,0);
//		ve.setX(v.getX()/(0.25f/4)*(0.25f/4));
//		ve.setY(v.getY()/(0.25f/4)*(0.25f/4));
		ve.setX((((int)(v.getX()*10000))/625*625)/10000f);
		ve.setY((((int)(v.getY()*10000))/625*625)/10000f);
		return ve;
	}


	public Velocity redirect() {
		return new Velocity(-x,-y);
	}
	public Velocity redirectRight(Velocity nullp) {
		Velocity v = new Velocity(x-nullp.x,x-nullp.y);
		float ax =(float) Math.toDegrees(Math.acos(x));
		float ay =(float) Math.toDegrees(Math.asin(y));
		if(ay<=0)ax=360f-ax;
		ax=ax-90;
		v=new Velocity((float)Math.cos(Math.toRadians(ax)),(float)Math.sin(Math.toRadians(ax)));
		return new Velocity(v.x,v.y);
	}
	public Velocity redirectLeft(Velocity nullp) {
		Velocity v = new Velocity(x-nullp.x,x-nullp.y);
		float ax =(float) Math.toDegrees(Math.acos(x));
		float ay =(float) Math.toDegrees(Math.asin(y));
		if(ay<=0)ax=360f-ax;
		ax=ax+90;
		v=new Velocity((float)Math.cos(Math.toRadians(ax)),(float)Math.sin(Math.toRadians(ax)));
		return new Velocity(v.x,v.y);
	}
}
