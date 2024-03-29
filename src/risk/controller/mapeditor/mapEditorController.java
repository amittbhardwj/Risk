package risk.controller.mapeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import risk.model.map.CountryNode;
import risk.model.map.MapModel;
import risk.model.map.MapNode;
import risk.model.map.MapReader;
import risk.view.mapeditor.ExistingMap;
import risk.view.mapeditor.ExistingMapEditor;
import risk.view.mapeditor.MapFileChooser;
import risk.view.mapeditor.NewMap;

/**
 * mapEditorController perform action listeners for 
 * New Map and Existing Map buttons in @see MapFrame view 
 * and choosing map file action.
 * @see MapFileChooser
 * @author jyotsna
 * @author Harinder
 */
public class mapEditorController {

	/**
	 * mapChooser variable for storing the reference of the class MapFileChooser
	 */
	private MapFileChooser mapChooser;

	/**
	 * existingMapEditor variable for storing the reference of the class ExistingMapEditor 
	 */
	private ExistingMapEditor existingMapEditor;

	/**
	 * newMap variable for storing the reference of the class NewMap.
	 */
	private NewMap newMap;

	/**
	 * action listener applied on button "Choose Map File" for selecting map file
	 */	
	private ActionListener existingBtnAction;

	/**
	 * Stores the path of the file chosen
	 */
	public String path = "";

	/**
	 * creates a new variable and storing the reference of the class
	 */
	private MapModel mapModel = new MapModel();
	
	/**
	 * existingMap stores the reference of the class ExistingMap
	 */
	ExistingMap existingMap;
	
	/**
	 *  creates a new variable and storing the reference of the class
	 */
	ExistingMap existingMap1 = new ExistingMap();
	
	/**
	 * creates a new variable and storing the reference of the class
	 */
	MapReader mapReader = new MapReader();

	/**
	 * Calls the readMap function of MapReader to read the map file
	 * @param filename address of the map file to be loaded
	 */
	public void MapRead(String filename) {
		MapReader mapReader = new MapReader();
		mapReader.readMap(filename);
	}

	/**
	 * Function to browse the map file on the local system
	 * This function implements the ActionListener events for the map file chooser button
	 */
	public void mapFileChooserActions() {
		try {
			/*creates object of mapFileChooser*/
			this.mapChooser = new MapFileChooser();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		/*actionListener for choose map file button*/
		existingBtnAction=(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new java.io.File("user.home"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Map Files", "map");
				fc.setFileFilter(filter);
				fc.setDialogTitle("Choose your Conquest Map File");
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					/*get the path of the selected file*/
					path = fc.getSelectedFile().getAbsolutePath();
					
					ArrayList<MapNode> map = mapReader.readMap(fc.getSelectedFile().getAbsolutePath());
					mapModel.writeExistingMap(map);
					
					if(mapModel.checkOnSaveMap()) {
						if(mapModel.checkConnectedContinent()) {
							//dialog box
							existingMap1.successfullLoad();
							/*pass the existing map file information to the existing file editor*/
							existingMap = new ExistingMap(mapReader.readMap(fc.getSelectedFile().getAbsolutePath()));
							/*actionListener for edit button of existing map frame*/
							existingMap.addActionsToBtnEdit(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									existingMap.setVisible(false);
									ArrayList<MapNode> existing_map_Info = existingMap.getExistingMapInfo();
									mapModel.writeExistingMap(existing_map_Info);
									existingMapEditor = new ExistingMapEditor(existing_map_Info);
									existingMapEditor.setVisible(true);
									existingMapActions();
								}
							});
							existingMap.setVisible(true);
						}else {
							existingMap1.showUnconnectedContinentError();
						}
						
					}else {
						existingMap1.cannotLoadMapError();
					}
				}
			}
		});
		this.mapChooser.openFileChooseBtnAction(existingBtnAction);
	}	

	/**
	 * Function to get the path of map file 
	 * @return path of map file.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Contains actionListeners for all NewMap buttons.
	 * @see NewMap
	 */
	public void newMapActions() {
		newMap = new NewMap();
		newMap.setVisible(true);

		/**
		*action listener for adding the continent
		*/
		newMap.addActionsToBtnAddContinent(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newMap.enableContinentFields();
			}
		});

		/**
		*action listener for adding the country.
		*/
		newMap.addActionsToBtnAddCountry(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newMap.enableCountryfield();
			}
		});

		/**
		*action listener to save all the selected changes.
		*/
		newMap.addActionsToBtnDone(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cn = newMap.getContinentName();
				String cv = newMap.getControlValue();
				/*
				 * if continent name or control value fields are empty,
				 * give 'enter values' error dialog box. 
				 */
				if(cn.compareTo("")==0 || cv.compareTo("")==0){
					newMap.enterValuesError();
				}else{
					int control_value= Integer.parseInt(cv);
					Boolean continentExist1 = mapModel.checkContinentExist(cn);
					/*check first that the continent is unique*/
					if(!continentExist1){
						ArrayList<CountryNode> countryArr = new ArrayList<CountryNode>();
						mapModel.addContinents(cn, countryArr, control_value);
						newMap.clearComboBoxContents();
						for(MapNode i: mapModel.getContinents()){
							String continent = i.getContinentName();
							newMap.setContinentsComboBox(continent);
						}
					}
				}
				newMap.disableContinentField();
			}
		});

		/**
		*action listener for adding the neighbours
		*/
		newMap.addActionsToBtnAddNeighbours(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newMap.enableJList();
				String sCountrytToAddNeighbour = newMap.getSelectedCountryForNeighbours();
				newMap.clearNeighboursJList();

				for (MapNode node : mapModel.getContinents()){
					for (CountryNode countryNode : node.getCountries()){
						/*add all possible neighbor countries to the JList*/
						if(sCountrytToAddNeighbour.compareTo(countryNode.getCountryName())==0)
							continue;
						newMap.addPossibleNeighboursToJList(countryNode.getCountryName());
					}
				}
			}
		});

		/**
		*action listener for deleting the neighbours
		*/
		newMap.addActionsToBtnDeleteNeighbours(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newMap.enableJList_1();
				String sCountryToDeleteNeighbour = newMap.getSelectedCountryForNeighbourDeletion();
				newMap.clearNeighboursJList_1();

				for (MapNode node : mapModel.getContinents()){
					for (CountryNode countryNode : node.getCountries()){
						if(sCountryToDeleteNeighbour.compareTo(countryNode.getCountryName())==0) {
							for (CountryNode neighbour : countryNode.getNeighbourCountries() ) {
								newMap.addPossibleNeighboursToJList_1(neighbour.getCountryName());
							}
						}
					}
				}
			}
		});

		/**
		*action listener for selecting the neighbours.
		*/
		newMap.addActionsToBtnSelectedNeighbours(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(newMap.getNeighboursList().isEmpty()) {
					newMap.noSelectedNeighboursError();
				}
				else {
					ArrayList<CountryNode> neighbours= new ArrayList<CountryNode>();
					/*Get the selected neighbors from the JList 
					 *and add them to the neighbors arrayList
					 **/
					for (Object ncountry : newMap.getNeighboursList()){
						CountryNode cn =  new CountryNode(ncountry.toString(), null, null,null);
						neighbours.add(cn);
					}
					String sCountrytToAddNeighbour = newMap.getSelectedCountryForNeighbours();
					for (MapNode node : mapModel.getContinents()){
						for (CountryNode cNode : node.getCountries()){
							/*When the selected country is found for which 
							 * neighbors need to added, add all the neighbors in arrayList
							 * to this countryNode
							 * */
							if(sCountrytToAddNeighbour.compareTo(cNode.getCountryName())==0)
								for (CountryNode neighbourNode : neighbours){
									cNode.addNeighbour(neighbourNode);
								}

						}
					}
					/*When a country A adds B as its neighbor,
					 * then B also adds A in its list of neighbors,
					 * forming bidirectional links between the countries.*/
					for (CountryNode neighbour : neighbours) {
						for (MapNode node : mapModel.getContinents()) {
							for (CountryNode countryNode : node.getCountries()) {
								if(countryNode.getCountryName().compareTo(neighbour.getCountryName())==0) {
									countryNode.addNeighbour(new CountryNode(sCountrytToAddNeighbour, null, null,null));
								}
							}
						}
					}
					newMap.successAddedNeighbours();
				}	
			}
		});

		/**
		*action listener for deleting the selected neighbours
		*/
		newMap.addActionsToBtnDeleteSelectedNeighbours(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(newMap.getNeighboursList_1().isEmpty()) {
					newMap.noSelectedNeighboursError();
				}
				else {
					ArrayList<CountryNode> neighbours_1= new ArrayList<CountryNode>();
					/*get the list of neighbors selected to be deleted 
					 * and store them in an arraList.
					 * */
					for (Object ncountry : newMap.getNeighboursList_1()){
						CountryNode cn =  new CountryNode(ncountry.toString(), null, null,null);
						neighbours_1.add(cn);
					}
					String sCountrytToDeleteNeighbour = newMap.getSelectedCountryForNeighbourDeletion();
					for (MapNode node : mapModel.getContinents()){
						for (CountryNode cNode : node.getCountries()){
							/*When the selected country is found, delete its neighbors.
							 */
							if(sCountrytToDeleteNeighbour.compareTo(cNode.getCountryName())==0)
								for (CountryNode neighbourNode : neighbours_1){
									cNode.removeNeighbour(neighbourNode);	
								}
						}
					}
					/*Delete the neighbor link from both sides.
					 */
					for (CountryNode neighbour : neighbours_1) {
						for (MapNode node : mapModel.getContinents()) {
							for (CountryNode countryNode : node.getCountries()) {
								if(countryNode.getCountryName().compareTo(neighbour.getCountryName())==0) {
									countryNode.removeNeighbour(new CountryNode(sCountrytToDeleteNeighbour, null, null,null));
								}
							}
						}
					}
					newMap.successDeletedNeighbours();
				}	
			}
		});


		/**
		*action listener for deleting the continent
		*/
		newMap.addActionsToBtnDeleteContinent(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<MapNode> continents = mapModel.getContinents();
				String delete_continent = newMap.getContinentToDelete();
				for (MapNode i :continents) {
					if(i.getContinentName().compareTo(delete_continent)==0) {
						continents.remove(i);
						break;
					}
				}
				/*Update view
				 */
				newMap.clearNeighboursJList();
				newMap.clearComboBoxContents();
				newMap.clearCountryComBoxContents();
				for(MapNode i: continents) {
					newMap.setContinentsComboBox(i.getContinentName());
					for (CountryNode countryNode : i.getCountries()){
						newMap.setCountriesComboBox(countryNode.getCountryName());
						newMap.addPossibleNeighboursToJList(countryNode.getCountryName());
					}
				}
			}
		});

		/**
		*action listener for saving the changes.
		*/
		newMap.addActionsToBtnSave(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mapModel.checkOnSaveMap()) {
					if(mapModel.checkConnectedContinent()) {
						/*if map satisfies all the validations, save it.*/
						mapModel.saveMapFile();
						newMap.successfullySaved();
					}else {
						newMap.nullCountryError();
					}	
				}else {
					newMap.nullCountryError();
				}
			}
		});

		/**
		*action listener for deleting the country.
		*/
		newMap.addActionsToBtnDeleteCountry(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedcountry = newMap.getCountryForDeletion();
				newMap.clearNeighboursJList();
				newMap.clearCountryComBoxContents();
				ArrayList<MapNode> continents = mapModel.getContinents();
				
				/*find the selected country and delete it*/
				for (MapNode node: continents) {
					for (CountryNode temp : node.getCountries()) {
						if(temp.getCountryName().compareTo(selectedcountry)==0) {
							node.removeCountry(temp);
						}
					}
				}
				/*Update the JFrame contents*/
				for (MapNode node: continents) {
					for (CountryNode temp : node.getCountries()) {
						newMap.setCountriesComboBox(temp.getCountryName());
						newMap.addPossibleNeighboursToJList(temp.getCountryName());
					}
				}
			}
		});

		/**
		* action listener for adding the existing continent
		*/
		newMap.addActionsToBtnAdd(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Boolean continentExist = newMap.checkContinentExist();
				if(continentExist) {
					String cn1 = newMap.getCountryName();
					if(cn1.compareTo("")==0) {
						newMap.enterValuesError();
					}else {
						String selectedContinent = newMap.getSelectedContinent();
						Boolean countryExist = mapModel.checkCountryExist(cn1);
						/*if the country is unique and does not exist before*/
						if(!countryExist) {
							/*arrayList to store neighbors*/
							ArrayList<CountryNode> neighbours= new ArrayList<CountryNode>();
							for (Object ncountry : newMap.getNeighboursList()) {
								CountryNode cn =  new CountryNode(ncountry.toString(), null, null,null);
								neighbours.add(cn);
							}
							newMap.clearNeighboursJList();
							newMap.clearCountryComBoxContents();
							for (MapNode node: mapModel.getContinents()) {
								/*get the value of selected continent and add country to it*/
								if(selectedContinent.compareTo(node.getContinentName())==0) {
									int a[]= {250,250};
									CountryNode newCountry = new CountryNode(cn1,  neighbours , a,null);
									node.addCountry(newCountry);
								}
								/*update the contents of JFrame*/
								for (CountryNode temp : node.getCountries()) {
									newMap.addPossibleNeighboursToJList(temp.getCountryName());
									newMap.setCountriesComboBox(temp.getCountryName());

								}
							}
						}else {
							newMap.countryAlreadyExistError();
						}
					}
				}else {
					newMap.nullContinentError();
				}
				newMap.disableCountryfield();
			}
		});

	}

	/**
	 * Contains actionListeners for all ExistingMapEditor buttons.
	 * @see ExistingMapEditor
	 */
	public void existingMapActions() {

		/*actionListeners for all the existing map editor buttons*/
		
		/**
		*action listener for adding the continent
		*/
		existingMapEditor.addActionsToBtnAddContinent(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				existingMapEditor.enableContinentFields();
			}
		});

		/**
		*action listener for adding the country
		*/
		existingMapEditor.addActionsToBtnAddCountry(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				existingMapEditor.enableCountryfield();
			}
		});

		/**
		*action listener for deleting the neighbours
		*/
		existingMapEditor.addActionsToBtnDeleteNeighbours(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				existingMapEditor.enableJList_1();
				String sCountrytToDeleteNeighbour = existingMapEditor.getSelectedCountryForNeighbourDeletion();
				existingMapEditor.clearNeighboursJList_1();

				/*find the selected country whose neighbors are to be deleted
				 * and display its all possible neighbors
				 */
				for (MapNode node : mapModel.getContinents()){
					for (CountryNode countryNode : node.getCountries()){
						if(sCountrytToDeleteNeighbour.compareTo(countryNode.getCountryName())==0) {
							for (CountryNode neighbour : countryNode.getNeighbourCountries() ) {
								existingMapEditor.addPossibleNeighboursToJList_1(neighbour.getCountryName());
							}
						}
					}
				}
			}
		});

		/**
		*action listener for deleting the selected neighbours
		*/
		existingMapEditor.addActionsToBtnDeleteSelectedNeighbours(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(existingMapEditor.getNeighboursList_1().isEmpty()) {
					existingMapEditor.noSelectedNeighboursError();
				}else {
					ArrayList<CountryNode> neighbours_1= new ArrayList<CountryNode>();
					String sCountrytToDeleteNeighbour = existingMapEditor.getSelectedCountryForNeighbourDeletion();
					for (Object ncountry : existingMapEditor.getNeighboursList_1()){
						CountryNode cn =  new CountryNode(ncountry.toString(), null, null, null);
						neighbours_1.add(cn);
					}
					/*remove the selected neighbors*/
					for (MapNode node : mapModel.getContinents()){
						for (CountryNode cNode : node.getCountries()){
							if(sCountrytToDeleteNeighbour.compareTo(cNode.getCountryName())==0)
								for (CountryNode neighbourNode : neighbours_1){
									cNode.removeNeighbour(neighbourNode);
								}
						}
					}
					/*delete the neighbor link from both sides*/
					for (CountryNode neighbour : neighbours_1) {
						for (MapNode node : mapModel.getContinents()) {
							for (CountryNode countryNode : node.getCountries()) {
								if(countryNode.getCountryName().compareTo(neighbour.getCountryName())==0) {
									countryNode.removeNeighbour(new CountryNode(sCountrytToDeleteNeighbour, null, null, null));
								}
							}
						}
					}
					existingMapEditor.successDeletedNeighbours();
				}	
			}
		});

		/**
		*action listener forsaving the changes made
		*/
		existingMapEditor.addActionsToBtnDone(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cn = existingMapEditor.getContinentName();
				String cv = existingMapEditor.getControlValue();
				if(cn.compareTo("")==0 || cv.compareTo("")==0){
					existingMapEditor.enterValuesError();
				}else{
					int control_value= Integer.parseInt(cv);
					Boolean continentExist1 = mapModel.checkContinentExist(cn);
					/*if the continent is not already present, add it*/
					if(!continentExist1){
						ArrayList<CountryNode> countryArr = new ArrayList<CountryNode>();
						mapModel.addContinents(cn, countryArr, control_value);
						existingMapEditor.clearComboBoxContents();
						for(MapNode i: mapModel.getContinents()){
							String continent = i.getContinentName();
							/*update the JFrame*/
							existingMapEditor.setContinentsComboBox(continent);
						}
					}
				}
				existingMapEditor.disableContinentField();
			}
		});

		/**
		*action listener for adding the neighbours
		*/
		existingMapEditor.addActionsToBtnAddNeighbours(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				existingMapEditor.enableJList();
				String sCountrytToAddNeighbour = existingMapEditor.getSelectedCountryForNeighbours();
				existingMapEditor.clearNeighboursJList();
				for (MapNode node : mapModel.getContinents()){
					for (CountryNode countryNode : node.getCountries()){
						/*add all the possible neighbors for a country*/
						if(sCountrytToAddNeighbour.compareTo(countryNode.getCountryName())==0)
							continue;
						existingMapEditor.addPossibleNeighboursToJList(countryNode.getCountryName());
					}
				}
			}
		});

		/**
		*action listener for selecting the neighbours
		*/
		existingMapEditor.addActionsToBtnSelectedNeighbours(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(existingMapEditor.getNeighboursList().isEmpty()) {
					existingMapEditor.noSelectedNeighboursError();
				}else {
					/*create a neighbor arrayList and add all the possible neighbors to it*/
					ArrayList<CountryNode> neighbours= new ArrayList<CountryNode>();
					String sCountrytToAddNeighbour = existingMapEditor.getSelectedCountryForNeighbours();
					for (Object ncountry : existingMapEditor.getNeighboursList()){
						CountryNode cn =  new CountryNode(ncountry.toString(), null, null, null);
						neighbours.add(cn);
					}
					/*get the selected country and add neighbors to it.*/
					for (MapNode node : mapModel.getContinents()){
						for (CountryNode cNode : node.getCountries()){
							if(sCountrytToAddNeighbour.compareTo(cNode.getCountryName())==0)
								for (CountryNode neighbourNode : neighbours){
									cNode.addNeighbour(neighbourNode);	
								}
						}
					}
					/*create bidirectional link between adjacent countries.*/
					for (CountryNode neighbour : neighbours) {
						for (MapNode node : mapModel.getContinents()) {
							for (CountryNode countryNode : node.getCountries()) {
								if(countryNode.getCountryName().compareTo(neighbour.getCountryName())==0) {
									countryNode.addNeighbour(new CountryNode(sCountrytToAddNeighbour, null, null, null));
								}
							}
						}
					}
					existingMapEditor.successAddedNeighbours();
				}
			}
		});

		/**
		*action listener for deleting the continent
		*/
		existingMapEditor.addActionsToButtonDeleteContinent(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<MapNode> continents = mapModel.getContinents();
				String delete_continent = existingMapEditor.getContinentToDelete();
				for (MapNode i :continents) {
					if(i.getContinentName().compareTo(delete_continent)==0) {
						continents.remove(i); // remove the continent.
						break;
					}
				}
				existingMapEditor.clearComboBoxContents();
				for(MapNode i: continents) {
					existingMapEditor.setContinentsComboBox(i.getContinentName());
				}
				/*update the JFrame*/
				existingMapEditor.clearNeighboursJList();
				existingMapEditor.clearComboBoxContents();
				existingMapEditor.clearCountryComBoxContents();
				for(MapNode i: continents) {
					existingMapEditor.setContinentsComboBox(i.getContinentName());
					for (CountryNode countryNode : i.getCountries()){
						existingMapEditor.setCountriesComboBox(countryNode.getCountryName());
						existingMapEditor.addPossibleNeighboursToJList(countryNode.getCountryName());
					}
				}
			}
		});

		
		/**
		*action listener for saving the changes
		*/
		existingMapEditor.addActionsToBtnSave(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mapModel.checkOnSaveMap()) {
					if(mapModel.checkConnectedContinent()) {
						mapModel.saveToExistingMapFile(getPath());
						existingMapEditor.successMessage();
					}
					
				}else {
					existingMapEditor.nullCountryError();
				}
			}
		});

		
		/**
		*action listener for deleting the country
		*/
		existingMapEditor.addActionsToBtnDeleteCountry(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedcountry = existingMapEditor.getCountryForDeletion();
				existingMapEditor.clearNeighboursJList();
				existingMapEditor.clearCountryComBoxContents();
				ArrayList<MapNode> continents = mapModel.getContinents();
				for (MapNode node: continents) {
					for (CountryNode temp : node.getCountries()) {
						if(temp.getCountryName().compareTo(selectedcountry)==0) {
							node.removeCountry(temp); //delete the countrynode
						}
					}
				}
				for (MapNode node: continents) {
					for (CountryNode temp : node.getCountries()) {
						existingMapEditor.setCountriesComboBox(temp.getCountryName());
						existingMapEditor.addPossibleNeighboursToJList(temp.getCountryName());
					}
				}
			}
		});

		/**
		*action listener for adding the existing continent
		*/
		existingMapEditor.addActionsToBtnAdd(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Boolean continentExist = existingMapEditor.checkContinentExist();
				if(continentExist) {
					String cn1 = existingMapEditor.getCountryName();
					if(cn1.compareTo("")==0) {
						existingMapEditor.enterValuesError();
					}else {
						String selectedContinent = existingMapEditor.getSelectedContinent();
						Boolean countryExist = mapModel.checkCountryExist(cn1);
						/*if the country is not already present, then add it
						 * else give error
						 */
						if(!countryExist) {
							ArrayList<CountryNode> neighbours= new ArrayList<CountryNode>();
							for (Object ncountry : existingMapEditor.getNeighboursList()) {//check
								CountryNode cn =  new CountryNode(ncountry.toString(), null, null, null);
								neighbours.add(cn);
							}
							existingMapEditor.clearNeighboursJList();
							existingMapEditor.clearCountryComBoxContents();
							for (MapNode node: mapModel.getContinents()) {
								/*when the appropriate continent is found, add country to it*/
								if(selectedContinent.compareTo(node.getContinentName())==0) {
									int a[]= {250,250};
									CountryNode newCountry = new CountryNode(cn1,  neighbours , a, null);
									node.addCountry(newCountry);
								}
								for (CountryNode temp : node.getCountries()) {
									existingMapEditor.addPossibleNeighboursToJList(temp.getCountryName());
									existingMapEditor.setCountriesComboBox(temp.getCountryName());
								}
							}
						}else {
							existingMapEditor.countryAlreadyExistError();
						}
					}
				}else {
					existingMapEditor.nullContinentError();
				}
				existingMapEditor.disableCountryfield();
			}
		});
	}
}

