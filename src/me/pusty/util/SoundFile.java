package me.pusty.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class SoundFile {

	Sound sound;
	
	boolean loop;
	public SoundFile(FileHandle file, boolean loop) {
		this.loop=loop;
		sound = Gdx.audio.newSound(file);
	}

	public  void start(PixelLocation player,PixelLocation loc) {
		float l = 1f;
		if(player != null && loc != null) {
			float distance = 8*16;
			l = Math.max(0f,(distance-PixelLocation.getDistance(player, loc))/distance);
		}
		l = l * 0.75f;
		if(loop)
			sound.loop(l);
		else
			sound.play(l);
	}
	
	public void close() {
		sound.stop();
		sound.dispose();
	}
//	public AudioClip getClip(){
//		return mainclip;
//	}

}
