package risk.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import risk.controller.MainController;


/**
 * This class display a dialog to ask user for number of players and map file to use.
 * @author Gurpreet
 * @author Gunpreet
 * @version 1.0
 */
public class SetUpDialog {
	
	/**
	 * JFrame for dialog boxes.
	 */
	private JFrame frame;
	
	/**
	 * Button to edit map.
	 */
	private JButton mapEdit;
	
	/**
	 * Button to begin the game.
	 */
	private JButton playGame;
	
	/**
	 * Stores the path of the map file uploaded.
	 */
	private String mapRead = null;
	
	public void loadSaveGameOption(){
		JFrame frame1 = new JFrame();
		frame1.setLayout(new BoxLayout(frame1.getContentPane(),BoxLayout.Y_AXIS));
		JButton newGame = new JButton("New Game");
		JButton loadGame = new JButton("Load Game");
		frame1.add(newGame);
		frame1.add(loadGame);
		frame1.pack();
		frame1.setVisible(true);
		
		newGame.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				frame1.dispose();
				MainController.getInstance().singleGameInit();
				
			}
		});
		
		loadGame.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				frame1.dispose();
				JFrame saveFileLoad = new JFrame("Saved File Chooser");
				saveFileLoad.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				saveFileLoad.validate();
				saveFileLoad.setVisible(true);
				/*JFileChooser to ask user to choose a map file.*/
				JFileChooser jfc = new JFileChooser();
				jfc.setCurrentDirectory(new File("./"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "sav");
				jfc.setFileFilter(filter);

				int returnValue = jfc.showOpenDialog(frame);
				String saveFileRead = null;
				/*Get the path of the map file chosen*/
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					saveFileRead = selectedFile.getAbsolutePath();
					saveFileLoad.dispose();
				}
				MainController.getInstance().singleGameLoadInit(saveFileRead);	
			}
			
		});
	}
	
	/**
	 * Ask user to enter an integer value.
	 * @param min Minimum value for spinner.
	 * @param max Maximum value for Spinner.
	 * @param message The message to be displayed on the spinner.
	 * @return number of players entered by user or by default 2.
	 */
	public int getInput(int min, int max, String message){
		 JPanel box = new JPanel();
		 SpinnerModel sm = new SpinnerNumberModel(min, min, max, 1); 
		 JSpinner inputSpinner = new JSpinner(sm);
         box.add(new JLabel("Input"));
         box.add(inputSpinner);
         
         int result = JOptionPane.showConfirmDialog(null, box, message, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
         if (result == JOptionPane.OK_OPTION) {
             return (int) inputSpinner.getValue();
         }
		return min;
	}
	
	/**
	 * Ask user to enter name of player one by one.
	 * @return string array containing number of players.
	 */
	public String[][] getPlayerInfo() {
		int n = getInput(2,6,"Enter number of Players");
		String[][] playerNames = new String[n][2];
		String[] behaviors = {"aggressive", "benevolent", "cheater", "human", "random"};
		JPanel panel = new JPanel();
		JTextField field = new JTextField(10);
		JComboBox<String> options = new JComboBox<String>(behaviors);
		panel.add(new JLabel("Name: "));
		panel.add(field);
		panel.add(new JLabel("Type: "));
		panel.add(options);
		for(int i=0;i< n;){
			field.setText("");
			options.setSelectedIndex(0);
			int s = JOptionPane.showConfirmDialog(
					frame,
					panel,
                    "Enter name of player "+ (i+1),
                    JOptionPane.OK_CANCEL_OPTION);

			if (s == JOptionPane.OK_OPTION) {
				playerNames[i][0] = field.getText();
				playerNames[i][1] = (String) options.getSelectedItem();
				i++;
			}
		}
		return playerNames;
	}
	
	/**
	 * Places army on the selected countries.
	 * @param countryList List of countries where the player can place armies.
	 * @param message Message to be displayed for the dialog box.
	 * @return country name selected.
	 */
	public String placeArmyDialog(String[] countryList, String message) {
		JComboBox<String> countriesList = new JComboBox<String>(countryList);
		String[] options = {"OK"};		
		JOptionPane.showOptionDialog(null, countriesList, message, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,		
		options, options[0]);
		String country = countryList[countriesList.getSelectedIndex()];
		return country;
	}
	
	/**
	 * Ask user for the map file to be used for the game.
	 * @param newExtension extension of the file to be picked.
	 * @return mapRead Stores the absolute path of the map file read.
	 */
	public String getMapInfo(String newExtension) {
		JFrame frame = new JFrame("Map File Chooser");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.validate();
		frame.setVisible(true);
		/*JFileChooser to ask user to choose a map file.*/
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File("./data/map"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Map Files", newExtension);
		jfc.setFileFilter(filter);

		int returnValue = jfc.showOpenDialog(frame);
		/*Get the path of the map file chosen*/
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			mapRead = selectedFile.getAbsolutePath();
			frame.dispose();
			if(mapRead.substring(mapRead.lastIndexOf("."),mapRead.length()).equalsIgnoreCase("."+newExtension)){
				return mapRead;
			}
		}
		if(newExtension.equals("map")) {
			return getMapInfo(newExtension);
		}
		return null;
	}
	
	/**
	 * Displays frame to choose from Map Edit and Play Game options at the start.
	 */
	public void chooseMapEditorOrPlayGame() {
		frame = new JFrame("Choose one:");
		frame.setSize(new Dimension(200,200));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mapEdit = new JButton("Edit Map");
		playGame = new JButton("Play Game");
		frame.setLayout(new FlowLayout());
		frame.add(mapEdit);
		frame.add(playGame);
		frame.validate();
		frame.setVisible(true);
	}
	
	/**
	 * Returns the frame to be used to dispose it after selection of an option.
	 * @return JFrame
	 */
	public JFrame chooseOptionFrame() {
		return this.frame;
	}
	
	/**
	 * Sets action listener to map edit button.
	 * @param newAction ActionListener for map edit button.
	 */
	public void mapEditAction(ActionListener newAction) {
		this.mapEdit.addActionListener(newAction);
	}
	
	/**
	 * Sets action listener to Play Game button.
	 * @param newAction ActionListener for Play Game button
	 */
	public void playGameAction(ActionListener newAction) {
		this.playGame.addActionListener(newAction);
	}
	
	/**
	 * This method display a dialog box with two buttons for user to select game mode.
	 * @return returns the string object containing game mode.
	 */
	public String gameMode() {
		JFrame frame = new JFrame("Map File Chooser");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.validate();
		frame.setVisible(true);
		Object[] options = {"Single Mode", "Tournament Mode"};
		int n = JOptionPane.showOptionDialog(frame,	"Please select a mode.", "Game Mode",	
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		frame.dispose();
		if(n==0) {
			return "single";
		}
		else if(n==1) {
			return "tournament";
		}
		return "noMode";
	}

	public String[] getPlayerBehavior(String[] playerInfo) {
		String[] behaviors = new String[playerInfo.length];
		for(int i=0;i<playerInfo.length;i++) {
			behaviors[i] = "human";
		}
		return behaviors;
	}

}