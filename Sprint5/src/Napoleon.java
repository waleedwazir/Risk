// put your code here

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Napoleon implements Bot {
	// The public API of YourTeamName must not change
	// You cannot change any other classes
	// YourTeamName may not alter the state of the board or the player objects
	// It may only inspect the state of the board and the player objects
	// So you can use player.getNumUnits() but you can't use player.addUnits(10000), for example
	
	private BoardAPI board;
	private PlayerAPI player;
	int botAttacks = 5;
	int indexOfTarget;
	int indexOfAttacker;
	
	Napoleon(BoardAPI inBoard, PlayerAPI inPlayer) {
		board = inBoard;	
		player = inPlayer;
		// put your code here
		return;
	}
	
	public String getName () {
		String command = "";
		// put your code here
		command = "BOT";
		return(command);
	}

	public String getReinforcement () {
		String command = "";
		// put your code here
		command = GameData.COUNTRY_NAMES[(int)(Math.random() * GameData.NUM_COUNTRIES)];
		command = command.replaceAll("\\s", "");
		command += " 1";
		return(command);
	}
	
	public String getPlacement (int forPlayer) {
		String command = "";
		// put your code here
		command = GameData.COUNTRY_NAMES[(int)(Math.random() * GameData.NUM_COUNTRIES)];
		command = command.replaceAll("\\s", "");
		return(command);
	}
	
	public String getCardExchange () {
		String command = "";
		// put your code here
		command = "skip";
		return(command);
	}

	public String getBattle () {

		String command;
		getTarget();
		if(botAttacks > 0)
		{

			 command = GameData.COUNTRY_NAMES[indexOfAttacker].replaceAll(" ","") +" "+GameData.COUNTRY_NAMES[indexOfTarget].replaceAll(" ","")+" "+calcNumberAttackingTroops();
			 botAttacks--;
		}else
		{
			command = "skip";
			botAttacks = 5;
		}
		return(command);
	}

	public String getDefence (int countryId) {
		String command = "";
		// put your code here
		if(board.getNumUnits(countryId) > 1)
		{
			command = "2";
		}else
		{
			command = "1";
		}

		return(command);
	}

	public String getMoveIn (int attackCountryId) {
		String command = "";
		// put your code here
		command = "0";
		return(command);
	}

	public String getFortify () {
		String command = "";
		// put code here
		command = "skip";
		return(command);
	}




	/*Auxiliary methods*/

	private void getTarget()
	{
		try
		{
			double highestWinChance = -1;
			ArrayList<Integer> playerCountries = getPlayerOwnedCountryIndexes();
			for(int i=0;i<playerCountries.size();i++)
			{
				int playerCountryIndex = playerCountries.get(i);
				int [] adjacentCountries = GameData.ADJACENT[playerCountryIndex];
				for(int j=0;j<adjacentCountries.length;j++)
				{
					if(player.getId() != board.getOccupier(adjacentCountries[j]))
					{
						double winChance = winChance(board.getNumUnits(playerCountryIndex), board.getNumUnits(adjacentCountries[j]));
						if(highestWinChance < winChance)
						{
							highestWinChance = winChance;
							indexOfTarget = adjacentCountries[j];
							indexOfAttacker = playerCountryIndex;
						}
					}
				}
			}
		}catch (FileNotFoundException e)
		{
			System.out.println("File not found");
		}
	}

	public ArrayList<Integer> getPlayerOwnedCountryIndexes()
	{
		ArrayList<Integer> playerCountryIndexes = new ArrayList<>();
		for(int i=0;i<GameData.NUM_CONTINENTS;i++)
		{
			int[] continentIds = GameData.CONTINENT_COUNTRIES[i];
			for(int j = 0;j<continentIds.length;j++)
			{
				if(player.getId() == board.getOccupier(continentIds[j]))
				{
					playerCountryIndexes.add(continentIds[j]);
				}

			}
		}
		return playerCountryIndexes;
	}
	public int calcNumberAttackingTroops()
	{
		if(board.getNumUnits(indexOfAttacker) >= 3)
		{
			return 3;
		}else
		{
			return board.getNumUnits(indexOfAttacker);
		}
	}


	private boolean completesContinent(int countryId)
	{
		//returns index of continent country is in
		//returns -1 if it cannot be found
		int continentIndex = countryContinentIndex(countryId);
		//increments for every country within a continent owned by the player
		int conquered = 0;

		if(continentIndex != -1)
		{
			int[] continentIds = GameData.CONTINENT_COUNTRIES[continentIndex];
			for(int i=0;i<continentIds.length;i++)
			{
				if(board.getOccupier(continentIds[i]) == player.getId())
				{
					conquered++;
				}
			}

			//conquering the territory on this country will complete the continent
			if(conquered == continentIds.length-1)
			{
				return true;
			}
		}
		//false or invalid selection
		return false;
	}

	private int countryContinentIndex(int countryId)
	{
		for(int i=0;i<GameData.NUM_CONTINENTS;i++)
		{
			int[] continentIds = GameData.CONTINENT_COUNTRIES[i];
			for(int j = 0;j<continentIds.length;j++)
			{
				if(countryId == continentIds[j])
				{
					return i;
				}
			}
		}
		return -1;
	}

	public double winChance(int attackingTroops, int defendingTroops) throws FileNotFoundException {
		int row = ((attackingTroops-1)*30)+defendingTroops;
		Scanner scanner = new Scanner(new File("./src/napoleonData.csv"));
		scanner.useDelimiter(",");
		int i = 1;
		while (scanner.hasNext()){
			if(i == row)
				return scanner.nextDouble();
			scanner.nextLine();
			i++;
		}
		return 0;
	}

}
