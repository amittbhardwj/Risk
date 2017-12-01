package risk.model.gamemode;

import risk.controller.GameController;
import risk.controller.MainController;

public class SingleMode implements Mode{

	private MainController mController;
	private static String myMap;
	private String myBmp;
	private String[][] myPlayers;
	private int myMoveLimit;
	private GameController controller;
	
	public SingleMode(String map, String bmp, String[][] players, int moveLimit, MainController nController) {
		mController = nController;
		myMap = map;
		myBmp = bmp;
		myPlayers = players;
		myMoveLimit = moveLimit;
	}

	
	public SingleMode(String map, String[][] players, int moveLimit, MainController nController) {
		mController = nController;
		myMap = map;
		myPlayers = players;
		myMoveLimit = moveLimit;
	}


	public void updateResults(String winnerPlayer) {
		String[][] data = {{myMap , winnerPlayer}};
		mController.setResults(data);
	}
	
	public void start() {
		if(myBmp!=null) {
			controller = new GameController(myMap, myBmp, myPlayers, myMoveLimit);
		}
		else {
			controller = new GameController(myMap, myPlayers, myMoveLimit);
		}
	}
	
	public static void main(String[] arg) {
		String[][] myPs = {{"Gur","aggressive"},{"Raj","aggressive"}};
		SingleMode s = new SingleMode("D:\\Gurpreet\\Study\\Meng\\SEM6\\SOEN6441\\project\\World2005.map", "D:\\Gurpreet\\Study\\Meng\\SEM6\\SOEN6441\\project\\World2005.bmp", myPs, 0, MainController.getInstance());
		MainController.getInstance().setMode(s);
		s.start();
	}
	
	public static String getMapName(){
		return myMap;
	}
	
}
