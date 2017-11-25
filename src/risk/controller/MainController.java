package risk.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import risk.view.SetUpDialog;
import risk.view.mapeditor.MapFrame;

public class MainController {
	
	/**
	 * Stores object of SetUpDialog class.
	 */
	private SetUpDialog setupBox;
	
	/**
	 * ActionListener to add listener to "Edit Map" button.
	 */
	private ActionListener mapEditListener;
	
	/**
	 * ActionListener to add listener to "Play Game" button.
	 */
	private ActionListener playGameListener;
	
	/**
	 * Method to initialize setupBox and listeners.
	 */
	public void initialize() {
		setupBox = new SetUpDialog();
		chooseMapEditorOrPlayGame();
		mapEditorListener();
		playGameListener();
	}

	/**
	 * Sets listener for Edit Map button.
	 */
	public void mapEditorListener() {
		mapEditListener =  new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MapFrame newMapFrame = new MapFrame();
				setupBox.chooseOptionFrame().dispose();
			}
		};
		this.setupBox.mapEditAction(mapEditListener);
	}
	
	/**
	 * Sets listener for Play Game button.
	 */
	public void playGameListener() {
		playGameListener =  new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				init();
				setupBox.chooseOptionFrame().dispose();
			}
		};
		this.setupBox.playGameAction(playGameListener);
	}
	
	/**
	 * Calls chooseMapEditorOrPlayGame() function of the SetUpDialog class to display Edit Map and Play Game options.
	 */
	public void chooseMapEditorOrPlayGame() {
		this.setupBox.chooseMapEditorOrPlayGame();
	}
	
	/**
	 * This method is responsible for taking input from user to whether user wants 
	 * to play tournament or single game, and accordingly create the tournament or single game object.
	 */
	private void init() {
		String mode = this.setupBox.gameMode();
		if(mode.equals("single")) {
			GameController controller = new GameController(this.setupBox);
		}
		else if(mode.equals("tournament")){
			System.out.print("Tournament");
		}
		else {
			init();
		}
	}

}