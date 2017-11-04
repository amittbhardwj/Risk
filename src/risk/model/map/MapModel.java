package risk.model.map;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import risk.controller.Controller;
import risk.model.CountryNode;
import risk.view.mapeditor.MapFrame;

/**
 * This implements the model code for map editor.
 * It contains the main logic for map editor
 * @author Harinder
 * @author Jyotsna
 */
public class MapModel {
	
	Hashtable<String, Boolean> countryTable = new Hashtable<String, Boolean>();
	
	/**
	 * Reference to the MapNode object
	 */
	MapNode mapNode;
	
	/**
	 * Stores path of newly created map file.
	 */
	private String newFilePath;
	
	/**
	 * Stores path of existing map file.
	 */
	private String existingFilePath;
	
	/**
	 * MapNode arraylist containing all the map information
	 */
	ArrayList<MapNode> continents = new ArrayList<MapNode>();
		
	/**
	 * MapWriter object for writing the map contents to the map file
	 */
	MapWriter mapWriter = new MapWriter();
	
	/**
	 * New controller object.
	 */
	Controller controller = new Controller();
	
	/**
	 * initializes the continent list with existing continents in the map file. 
	 * @param continents
	 */
	public void writeExistingMap(ArrayList<MapNode> continents) {
		this.continents = continents;
	}
	
	/**
	 * checks for unique continents.
	 * @param cn receives the continent to be checked for uniqueness.
	 * @return true if the continent already exist.
	 */
	public boolean checkContinentExist(String cn) {
		Boolean continentExist = false;
		for (MapNode con: continents){
			if(con.getContinentName().compareTo(cn)==0){
				continentExist = true;
			}
		}
		return continentExist;
	}
	
	/**
	 * Function to add a new continent
	 * @param cn1 receives continent name.
	 * @param countryArr arrayList of countries within the continent.
	 * @param cv1 control value of the continent.
	 */
	public void addContinents(String cn1,ArrayList<CountryNode> countryArr,int cv1) {
		continents.add(new MapNode(cn1, countryArr, cv1));
	}
	
	/**
	 * function to get the list of continents.
	 * @return the continent arrayList.
	 */
	public ArrayList<MapNode> getContinents() {
		return continents;
	}
	
	/**
	 * function to implement validations before saving the map file.
	 * @return true if map is valid.
	 */
	public boolean checkOnSaveMap() {
		Boolean saveMap = true;
		for (MapNode i :continents) {
			if(i.getCountries().length == 0) {
				saveMap = false;
			}
			for (CountryNode country : i.getCountries()) {
				if(country.getNeighbourCountries().length == 0) {
					saveMap = false;
				}
				//connected map check
				if(!connectedMap()) {
					saveMap = false;
				}
			}
		}
		return saveMap;
	}
	
	public boolean connectedMap() {
		
		for (MapNode mapNode : continents) {
			for (CountryNode cNode : mapNode.getCountries()) {
				countryTable.put(cNode.getCountryName(), false);
			}
		}
		String first = countryTable.keySet().iterator().next();
		search(first);
		
		if(countryTable.containsValue(false)) {
			return false;
		}else {
			return true;
		}
	}
	
	public void search(String s)
    {
        // Mark the current node as visited by setting it true 
        countryTable.put(s, true);
        for (MapNode mapNode : continents) {
			for (CountryNode cNode : mapNode.getCountries()) {
				if(cNode.getCountryName().compareTo(s)==0) {
					// Recur for all the connected neighbor countries
			        Iterator<CountryNode> i = cNode.getNeighbours().listIterator();
			        while (i.hasNext())
			        {
			            CountryNode n = i.next();
			            if (countryTable.get(n.getCountryName())==false)
			                search(n.getCountryName());
			        }
				}
			}
		}
        
    }
	
	/**
	 * Function to save new map file.
	 */
	public void saveMapFile() {
		mapWriter.writeMap(continents);
		newFilePath = mapWriter.getMapFilePath();
	}
	
	/**
	 * Function to save existing map file.
	 * @param path receives the path of the existing map file.
	 */
	public void saveToExistingMapFile(String path) {
		mapWriter.writeMapExisting(continents, path);
		existingFilePath = mapWriter.getMapFilePath();
	}
	
	/**
	 * Function to get the path of the new map file.
	 * @return path of the new map file.
	 */
	public String newFilePath() {
		return newFilePath;
	}
	
	/**
	 * Function to get the path of the existing map file.
	 * @return the path of the existing map file.
	 */
	public String existingFilePath() {
		return existingFilePath;
	}
	
	/**
	 * function to get the final path of the file saved.
	 * @return the path of the file saved.
	 */
	public String getFinalPath() {
		if(MapFrame.selectedAction().compareTo("new")==0) {
			System.out.println(newFilePath());
			return newFilePath();
		}
		else if(MapFrame.selectedAction().compareTo("existing")==0){
			System.out.println(existingFilePath());
			return existingFilePath();
		}
		return null;
	}
	
	/**
	 * function to check for unique countries.
	 * @param cn1 receives the country name
	 * @return true if the country is already present.
	 */
	public boolean checkCountryExist(String cn1) {
		Boolean countryExist = false;
		for (MapNode node: continents) {
			for (CountryNode country : node.getCountries()) {
				if(country.getCountryName().compareTo(cn1)==0) {
					countryExist=true;
				}
			}
		}
		return countryExist;
	}
}