package risk.model.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import risk.model.gamemode.GameDriver;
import risk.model.map.CountryNode;

/**
 * Class for Benevolent player that implements the PlayerStrategy interface.
 * @author Gunpreet
 * @version 1.3
 */
public class BenevolentStrategy implements PlayerStrategy {
	
	/**
	 * GameDriver instance for benevolent player.
	 */
	private GameDriver driver = new GameDriver();
	
	public BenevolentStrategy(GameDriver nDriver) {
		driver = nDriver;
	}
	
	/**
	 * Reinforcement phase of benevolent player that reinforces its weakest countries.
	 * @see risk.model.player.PlayerStrategy#reinforcementPhase(int, java.lang.String[])
	 */
	@Override
	public void reinforcementPhase(int armies, String[] countryList) {
		reinforcement(armies, countryList);
		driver.nottifyObservers(driver.getTurnManager().getPhase());
		driver.changePhase();
	}

	/**
	 * Attack phase: benevolent player never attacks.
	 * @see risk.model.player.PlayerStrategy#attackPhase(java.util.ArrayList)
	 */
	@Override
	public void attackPhase(ArrayList<String> countryList) {
		/*skip attack phase.*/
		driver.nottifyObservers(driver.getTurnManager().getPhase());
		driver.changePhase();
	}
	
	/**
	 * Fortification phase of benevolent player: fortifies in order to move armies to weakest country.
	 * @see risk.model.player.PlayerStrategy#fortificationPhase(java.util.ArrayList)
	 */
	@Override
	public void fortificationPhase(ArrayList<String> countryList) {
		fortify(countryList);
		driver.nottifyObservers(driver.getTurnManager().getPhase());
		driver.changePhase();
	}

	/**
	 * Distribute armies in startup phase.
	 */
	@Override
	public String placeArmy(String[] strings, String string) {
		return strings[new Random().nextInt(strings.length)];
	}
	
	/**
	 * sort countries according to armies count.
	 * @param countryList list of country nodes to be sorted.
	 * @return sorted list of country nodes.
	 */
	private ArrayList<CountryNode> sortCountries(ArrayList<CountryNode> countryList){
		Collections.sort(countryList, new Comparator<CountryNode>(){

			@Override
			public int compare(CountryNode o1, CountryNode o2) {
				return o1.getArmiesCount() - o2.getArmiesCount();
			}
		});
		return countryList;
	}

	@Override
	public int selectDiceNumber(int diceToRoll, String pName) {
		
		return diceToRoll;
	}

	@Override
	public int moveArmies(int aArmies, int maxArmies, String message) {
		return new Random().nextInt(maxArmies+1-aArmies) + aArmies;
	}
	
	@Override
	public String getStrategyName() {
		return "benevolent";
	}

	public void reinforcement(int armies, String[] countryList) {
		ArrayList<CountryNode> countries = new ArrayList<CountryNode>();
		/*get country node for corresponding country name.*/
		for(String c: countryList){
			countries.add(driver.getCountry(c));
		}
		
		/*sort countries according to armies count in descending order.*/
		countries = sortCountries(countries);
		
		/*get the list of weak countries.*/
		int countOfWeakCountries = 1;
		ArrayList<CountryNode> weakCountryList = new ArrayList<CountryNode>();
		weakCountryList.add(countries.get(0));
		for(int i= 1; i < countries.size(); i++){
			if(countries.get(i).getArmiesCount() == countries.get(i-1).getArmiesCount()){
				weakCountryList.add(countries.get(i));
				countOfWeakCountries++;
			}
			else{
				break;
			}
		}
		Player player = driver.getCurrentPlayer();
		
		/*get the integer round-off of the armies to be alloted to each weak country.*/
		int armiesToBeReinforced = (int)(armies/countOfWeakCountries);
		for( CountryNode country: weakCountryList){
			driver.getCurrentPlayer().shiftArmiesOnReinforcement(country.getCountryName(), armiesToBeReinforced);
//			country.addArmy(armiesToBeReinforced);
//			player.removeArmies(armiesToBeReinforced);
		}
		
		/*Move the armies remaining into the first weakest country in the list.*/
		int playerArmiesLeft = player.getArmiesCount();
				
		if(!(playerArmiesLeft == 0)){
			weakCountryList.get(0).addArmy(playerArmiesLeft);
			player.removeArmies(playerArmiesLeft);
		}
	}
	
	public void fortify(ArrayList<String> countryList) {
		ArrayList<CountryNode> countries = new ArrayList<CountryNode>();
		/*get country node for corresponding country name.*/
		for(String c: countryList){
			countries.add(driver.getCountry(c));
		}
		
		/*sort countries according to armies count in descending order.*/
		countries = sortCountries(countries);
		
		/*fortify the weakest country.*/
		CountryNode weakest = countries.get(0);
		CountryNode strongest = countries.get(countries.size()-1);
		int average = (int)(weakest.getArmiesCount() + strongest.getArmiesCount()) / 2;
		driver.getCurrentPlayer().getArmiesShiftedAfterFortification(strongest.getCountryName(), weakest.getCountryName(), average);
	}

}
