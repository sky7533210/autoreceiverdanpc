package com.main;
import java.io.FileInputStream;

import com.bean.Config;

import javazoom.jl.player.Player;

public class PlayerMusic extends Thread {
	private Player player;
	@Override
	public void run() {
		 try {	        
	         player=  new Player(new FileInputStream(Config.MUSICPATH));
	         player.play();     	        
	        } catch (Exception ex) {
	           ex.printStackTrace();
	        }
	}
	public void close(){
		 player.close();
	}
} 