// put your code here

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Napoleon implements Bot {
	// The public API of YourTeamName must not change
	// You cannot change any other classes
	// YourTeamName may not alter the state of the board or the player objects
	// It may only inspect the state of the board and the player objects
	// So you can use player.getNumUnits() but you can't use player.addUnits(10000), for example
	
	private BoardAPI board;
	private PlayerAPI player;
	double attackThreshHold;
	int indexOfTarget;
	int indexOfAttacker;
	int turn = 1;
	int startingTroops;
	boolean stopAttacking = false;
	boolean startOfCombat = true;

	/**Weightings**/
	int depthCutoffWeight = 10;
	double depthScalerWeight = 1.5;
	double nearCompleteContinentWeight = 1.1;
	double completesContinentWeight = 10;
	double attackThreshHoldWeight = 67;
	double borderWeight = 1.1;
	double neutralDeterrentWeight = 5;
	double moveInWeight = 0.5;
	double moveInRefinedWeight = 0.1;
	double enemyTargetWeight = 8;
	double enemyWinChanceWeight = 55;
	double defendingScaleWeight = 100;
	double weakContinentBorderWeight = 1.1;
	double safeContinentWeight = 2;
	double troopsToSafeContinentWeight = 2;
	int troopThresholdWeight = 4;
	double largeAccumulationDeterrent = 0.8;

	Napoleon(BoardAPI inBoard, PlayerAPI inPlayer) {
		board = inBoard;	
		player = inPlayer;
		// put your code here
		return;
	}


	/**
	 * -----Public API-----
	 * **/
	public String getName () {
		String command = "";
		// put your code here
		command = "Napoleon";
		return(command);
	}

	public String getReinforcement (){
		String command = "";
		// put your code here
		try {
			command = GameData.COUNTRY_NAMES[getReinforceCountry()];
		}catch (FileNotFoundException ex){ }
		command = command.replaceAll("\\s", "");
		command += " 1";
		return(command);
	}
	
	public String getPlacement (int forPlayer) {
		String command = "";
		// put your code here
		int chosenId = -1;
		int biggestCluster = 0;
		int leastProtected = 1000;
		ArrayList<Integer> neutralCountries = getPlayerOwnedCountryIndexes(forPlayer);
		for(int countryId:neutralCountries){
			for(Integer adjacentId:getAdjacentCountry(countryId)){
				if(adjacentId == getEnemyId()){
					if(board.getNumUnits(countryId) < leastProtected){
						leastProtected = board.getNumUnits(countryId);
						if(getClusterValue(adjacentId) > biggestCluster)
							biggestCluster = getClusterValue(adjacentId);
							chosenId = countryId;
					}
				}
			}
		}
		if(chosenId == -1){
			Collections.shuffle(neutralCountries);
			command = GameData.COUNTRY_NAMES[neutralCountries.get(0)];
		}else{
			command = GameData.COUNTRY_NAMES[chosenId];
		}
		command = command.replaceAll("\\s", "");
		return(command);
	}
	
	public String getCardExchange () {
		String command = "";
		if(player.isForcedExchange() || !isWinning())
		{
			command = convertInsignia(getValidInsigniaIds());

		}else
		{
			command = "skip";
		}
		return(command);
	}

	public String getBattle () {

		if(startOfCombat)
		{
			startingTroops = getTotalUnits(player.getId());
			startOfCombat = false;
		}else
		{
			checkLossesInEarlyGame(getTotalUnits(player.getId()));
		}

		attackThreshHold = 0;
		String command;
		getTarget();
		if(!stopAttacking && attackThreshHold >= attackThreshHoldWeight)
		{
			 command = GameData.COUNTRY_NAMES[indexOfAttacker].replaceAll(" ","") +" "+GameData.COUNTRY_NAMES[indexOfTarget].replaceAll(" ","")+" "+calcNumberAttackingTroops();
		}else
		{
			stopAttacking = false;
			startOfCombat = true;
			command = "skip";
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
		if(encapsulated(indexOfTarget)){
			moveInWeight -= moveInRefinedWeight;
			double units = board.getNumUnits(attackCountryId)*moveInWeight;
			moveInWeight += moveInRefinedWeight;
			command = String.valueOf((int) Math.floor(units));
		}else {
			if(encapsulated(attackCountryId))
				moveInWeight += moveInRefinedWeight;
			double units = board.getNumUnits(attackCountryId)*moveInWeight;
			if(encapsulated(attackCountryId))
				moveInWeight -= moveInRefinedWeight;
			units--;
			command = String.valueOf((int)Math.floor(units));
		}
		return(command);
	}

	public String getFortify () {
		String command = "";
		getContinentBalance(player.getId());
		int gifterId = getGifterCountry();
		try {
			if(gifterId != -1)
			{
				int units = board.getNumUnits(gifterId) > 4 ? board.getNumUnits(gifterId) - 2: board.getNumUnits(gifterId) - 1;
				command = GameData.COUNTRY_NAMES[gifterId].replaceAll(" ","") +" "+GameData.COUNTRY_NAMES[receiverId(gifterId)].replaceAll(" ","")+" "+units;
			}else
			{
				command = "skip";
			}
		}catch (FileNotFoundException ex){ }
		turn ++;
		return(command);
	}


	/**
	 * -----Auxiliary methods-----
	 * */

	/**Card Methods*/

	//Changes card set ids into appropriate command
	private String convertInsignia(int[] insignia)
	{
		String command = "";
		for(int i=0;i<insignia.length;i++)
		{
			if (insignia[i] == 0)
				command += "i";
			else if (insignia[i] == 1)
				command += "c";
			else if (insignia[i] == 2)
				command += "a";
			else if (insignia[i] == 3)
				command += "w";
			else
				command += "";
		}
		return command;
	}

	//returns valid card set ids in Napoleon's hand
	private int[] getValidInsigniaIds()
	{
		for(int i=0;i<Deck.SETS.length;i++)
		{
			if(player.isCardsAvailable(Deck.SETS[i]))
			{
					return Deck.SETS[i];
			}
		}

		return null;
	}
	/**End of card methods*/

	/**Attacking Methods*/
	//Sets country id of attacking and defending countries wit highest win chance.
	//determines if bot will attack
	private void getTarget()
	{
		try
		{
			if(isWinning()){
				enemyTargetWeight = 8;
			}else{
				enemyTargetWeight = 5;
			}
			double highestWinChance = -1;
			ArrayList<Integer> playerCountries = getPlayerOwnedCountryIndexes(player.getId());
			for(int i=0;i<playerCountries.size();i++)
			{
				int playerCountryIndex = playerCountries.get(i);
				int [] adjacentCountries = GameData.ADJACENT[playerCountryIndex];
				for(int j=0;j<adjacentCountries.length;j++)
				{
					if(player.getId() != board.getOccupier(adjacentCountries[j]))
					{
						double winChance = winChance(board.getNumUnits(playerCountryIndex), board.getNumUnits(adjacentCountries[j]));
						if(completesContinent(adjacentCountries[j], player.getId()) && winChance >= 45){
							winChance+=completesContinentWeight;
						}
						if(board.getOccupier(indexOfTarget) != getEnemyId())
							winChance -= neutralDeterrentWeight;
						if(board.getOccupier(indexOfTarget) == getEnemyId() && winChance>enemyWinChanceWeight){
							winChance += enemyTargetWeight;
						}
						if(winChance>=90){
							winChance += (depthOfAttack(adjacentCountries[j])*depthScalerWeight);
						}
						if(highestWinChance < winChance)
						{
							highestWinChance = winChance;
							attackThreshHold = highestWinChance;
							indexOfTarget = adjacentCountries[j];
							indexOfAttacker = playerCountryIndex;
						}
					}
				}
			}
		}catch (FileNotFoundException | InterruptedException e)
		{
			System.out.println("File not found");
		}
	}

	private int depthOfAttack(int targetId) throws InterruptedException {
		ArrayList<Integer> neighbours = getAdjacentCountry(targetId);
		ArrayList<Integer> visited = new ArrayList<>();
		visited.add(targetId);
		int sum = 1;
		for(Integer neighbourId:neighbours){
			if(board.getOccupier(neighbourId) != player.getId() && !isVisited(neighbourId, visited)){
				sum = Math.max(sum, 1 + depthOfAttackHelper(neighbourId, visited, sum));
			}
		}

		return sum;
	}

	private int depthOfAttackHelper(int targetId, ArrayList<Integer> visited, int sum){
		if(sum >= depthCutoffWeight){
			return sum;
		}
		ArrayList<Integer> neighbours = getAdjacentCountry(targetId);
		visited.add(targetId);
		ArrayList<Integer> neighbourSums = new ArrayList<>();
		for(Integer neighbourId:neighbours){
			if(board.getOccupier(neighbourId) != player.getId() && !isVisited(neighbourId, visited)){
				ArrayList<Integer> visitedParse = new ArrayList<>();
				visitedParse.addAll(visited);
				neighbourSums.add(Math.max(sum, depthOfAttackHelper(neighbourId, visitedParse, sum+1)));
			}
		}

		for(Integer neighbourSum:neighbourSums){
			sum = Math.max(sum, neighbourSum);
		}
		return sum;
	}

	private boolean isVisited(int targetId, ArrayList<Integer> visited){
		if(visited.contains(targetId))
			return true;
		else
			return false;
	}


	//determines best number of attacking troops
	private int calcNumberAttackingTroops()
	{
		int troops = board.getNumUnits(indexOfAttacker);
		if(troops > 3)
		{
			return 3;
		}else if(troops == 3)
		{
			return 2;
		}else
		{
			return 1;
		}
	}

	//Determines the chance of winning based on parsed number of troops
	public double winChance(int attackingTroops, int defendingTroops) throws FileNotFoundException {
		if(attackingTroops<31) {
			int row = ((attackingTroops - 1) * 30) + defendingTroops;
			Scanner scanner = new Scanner(new File("./src/napoleonData.csv"));
			scanner.useDelimiter(",");
			int i = 1;
			while (scanner.hasNext()) {
				if (i == row)
					return scanner.nextDouble();
				scanner.nextLine();
				i++;
			}
		}else if(attackingTroops>defendingTroops){
			return 100;
		}
		return 0;
	}

	/**
	 * Auxillary Method
	 * */
	private ArrayList<Integer> getAdjacentCountry(int countryId){
		ArrayList<Integer> countries = new ArrayList<>();
		for(Integer country:GameData.ADJACENT[countryId]){
			countries.add(country);
		}
		return countries;
	}

	private HashMap<Integer,Double> getContinentBalance(int playerId)
	{
		HashMap<Integer,Double> balances = new HashMap<>();
		double countriesInContinent = 0;
		for(int i=0;i<6;i++)
		{
			for(int countryId:getPlayerOwnedCountryIndexes(playerId))
			{
				if(getCountryContinentIndex(countryId) == i)
				{
					countriesInContinent++;
				}
			}
			balances.put(i,countriesInContinent/GameData.CONTINENT_COUNTRIES[i].length);
		}
		return balances;
	}

	//returns true if a country completes a continent
	private boolean completesContinent(int countryId, int playerId)
	{
		//returns index of continent country is in
		//returns -1 if it cannot be found
		int continentIndex = getCountryContinentIndex(countryId);
		//increments for every country within a continent owned by the player
		int conquered = 0;

		if(continentIndex != -1)
		{
			int[] continentIds = GameData.CONTINENT_COUNTRIES[continentIndex];
			for(int i=0;i<continentIds.length;i++)
			{
				if(board.getOccupier(continentIds[i]) == playerId)
				{
					conquered++;
				}
			}

			//conquering the territory on this country will complete the continent
			if(conquered >= continentIds.length-1)
			{
				return true;
			}
		}
		//false or invalid selection
		return false;
	}

	//returns true if the borders of the continent are surrounded by non enemy territories
	private boolean isContinentSafe(int continentId)
	{
		boolean safe = true;
		for(int countryId:GameData.CONTINENT_COUNTRIES[continentId])
		{
			for(int adjacent:GameData.ADJACENT[countryId])
			{
				if(getCountryContinentIndex(adjacent) != continentId)
				{
					 if(board.getOccupier(adjacent) == getEnemyId())
					 {
					 	safe = false;
					 }
				}
			}
		}
		return safe;
	}

	//returns true if the country is a continent border and it has less troops than an adjacent territory from another continent that is owned by an enemy
	private boolean isWeakContinentBorder(int countryId)
	{
		int continentId = getCountryContinentIndex(countryId);
		if(ownsContinent(continentId,player.getId()))
		{
			for(int adjacent:GameData.ADJACENT[countryId])
			{
				if(getCountryContinentIndex(adjacent) != continentId)
				{
					if(board.getOccupier(adjacent) == getEnemyId() && board.getNumUnits(countryId) < board.getNumUnits(adjacent))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean ownsContinent(int continentId,int playerId)
	{
		boolean ret = true;
		for(int countryId:GameData.CONTINENT_COUNTRIES[continentId])
		{
			if(board.getOccupier(countryId) != playerId)
			{
				ret = false;
			}
		}
		return ret;
	}

	//returns continent id that the countryId is in.
	private int getCountryContinentIndex(int countryId)
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


	//returns arraylist of countryIds that player owns
	public ArrayList<Integer> getPlayerOwnedCountryIndexes(int playerId)
	{
		ArrayList<Integer> playerCountryIndexes = new ArrayList<>();
		for(int i=0;i<GameData.NUM_CONTINENTS;i++)
		{
			int[] continentIds = GameData.CONTINENT_COUNTRIES[i];
			for(int j = 0;j<continentIds.length;j++)
			{
				if(playerId == board.getOccupier(continentIds[j]))
				{
					playerCountryIndexes.add(continentIds[j]);
				}

			}
		}
		return playerCountryIndexes;
	}

	private int getEnemyId(){
		if(player.getId() == 0)
			return 1;
		else
			return 0;
	}

	//temp method
	private boolean isWinning()
	{
		if(turn > 2)
		{
			if(getExtraReinforcements(player.getId()) > getExtraReinforcements(getEnemyId()))
				return true;
			else
				return false;
		}
		return true;
	}

	//changes a boolean to stop Napoleon attacking if too many troops are lost in early combat
	private void checkLossesInEarlyGame(int currentTroops)
	{
		if(turn < 3)
		{
			int troopThreshHold = startingTroops/troopThresholdWeight;
			int troopsLost = (startingTroops - currentTroops);
			if( troopsLost >= troopThreshHold)
			{
				stopAttacking = true;
			}else
			{
				stopAttacking = false;
			}
		}
	}
	//returns total units owned by a player
	private int getTotalUnits(int playerId)
	{
		int total = 0;
		for(int countryId:getPlayerOwnedCountryIndexes(playerId))
		{
			total += board.getNumUnits(countryId);
		}
		return total;
	}
	//returns how many extra reinforcements a player gets
	private int getExtraReinforcements(int playerId)
	{
		int ret = (int) getPlayerOwnedCountryIndexes(getEnemyId()).size()/3;

		if(ownsContinent(0,playerId))
			ret += 5;
		if(ownsContinent(1,playerId))
			ret += 5;
		if(ownsContinent(2,playerId))
			ret += 7;
		if(ownsContinent(3,playerId))
			ret += 2;
		if(ownsContinent(4,playerId))
			ret += 2;
		if(ownsContinent(5,playerId))
			ret += 3;

		return ret;
	}
	/**
	 * -----END of Auxillary methods-----
	 */


	/**
	 * -----Placement Methods-----
	 */
	private int getReinforceCountry() throws FileNotFoundException {
		double total = 0;
		HashMap<Integer, Double> raffle = new HashMap<Integer, Double>();
		ArrayList<Integer> countries = getPlayerOwnedCountryIndexes(player.getId());
		for(int i = 0;i<countries.size();i++){
			raffle.put(countries.get(i), getCountryPriority(countries.get(i)));
		}
		double max = 0;
		int ret = 0;
		for(Map.Entry<Integer,Double> entry:raffle.entrySet()){
			if(entry.getValue()>max){
				max = entry.getValue();
				ret = entry.getKey();
			}
		}
		return ret;
	}

	private double getCountryPriority(int countryId) throws FileNotFoundException{
		double priority = 1, defScale = 1;
		double completesContinent = 0;
		double weakContinentBorder = 0;
		double safeContinent = 0;
		int sumEnemyTroops = 0;

		for(int checkId:GameData.ADJACENT[countryId]) {
			if(board.getOccupier(checkId) != board.getOccupier(countryId)) {
				sumEnemyTroops += board.getNumUnits(checkId)-1;
				for(int adjacent:GameData.ADJACENT[checkId]){
					if(board.getOccupier(adjacent) == player.getId() && adjacent != countryId && board.getNumUnits(adjacent) == 31){
						priority = 0;
					}
				}
			}
			if(board.getOccupier(checkId) == player.getId()){
				if(board.getNumUnits(checkId) > 15){
					priority *= largeAccumulationDeterrent;
				}else {
					priority *= borderWeight;
				}
			}
		}
		double defChance = 1-(winChance(sumEnemyTroops, board.getNumUnits(countryId)));



		priority *= defScale+(defChance/defendingScaleWeight);

		int countryCompletesContinent = isNearCompleteContinent(countryId);
		if(countryCompletesContinent != -1)
			if(isContinentSafe(getCountryContinentIndex(countryId)) && numAllyTroopsSurrounding(countryCompletesContinent) <= (board.getNumUnits(countryCompletesContinent)*troopsToSafeContinentWeight))
				safeContinent = safeContinentWeight;
			completesContinent = nearCompleteContinentWeight + safeContinent;
		if(isWeakContinentBorder(countryId))
			weakContinentBorder = weakContinentBorderWeight;
		priority *= 1+(getClusterValue(countryId)/100) + completesContinent + weakContinentBorder;

		if(encapsulated(countryId))
			priority*=0;
		return priority;
	}

	private int getClusterValue(int countryId){
		int sum = 0;
		int occupierId = board.getOccupier(countryId);
		ArrayList<Integer> visited = new ArrayList<>();
		Queue<Integer> queue = new LinkedList<Integer>();
		queue.add(countryId);
		while(!queue.isEmpty()){
			int check = queue.remove();
			if(board.getOccupier(check) == occupierId){
				sum++;
				visitNeighbours(check, visited, queue);
			}
		}
		return sum;
	}

	private void visitNeighbours(int countryId, ArrayList<Integer> visited, Queue<Integer> queue){
		visited.add(countryId);
		for(Integer num:GameData.ADJACENT[countryId]){
			if(!isVisited(visited, num))
				queue.add(num);
		}
	}

	private boolean isVisited(ArrayList<Integer> visited, int check){
		boolean ret = false;
		for(Integer num:visited){
			if(check == num)
				ret = true;
		}
		return ret;
	}

	//returns true if country is surrounded by only ally countries
	private boolean encapsulated(int countryId){
		boolean ret = true;
		for(int checkId:GameData.ADJACENT[countryId]){
			if(board.getOccupier(checkId)!=board.getOccupier(countryId)){
				ret = false;
			}
		}
		return ret;
	}
	//returns the countryId of the country that will gift units to fortify
	private int getGifterCountry()
	{
		HashMap<Integer,Integer> encapsulatedCountries = encapsulatedCountries();
		if(encapsulatedCountries.isEmpty())
		{
			return -1;
		}
		int max = -1;
		int giftCountryId = -1;
		for(int key:encapsulatedCountries.keySet())
		{
			int troops = encapsulatedCountries.get(key);
			if(troops > max)
			{
				max = troops;
				giftCountryId = key;
			}
		}
		return giftCountryId;
	}

	//returns a hashmap of encapsulated countryIds and their troop sizes
	private HashMap<Integer,Integer> encapsulatedCountries()
	{
		HashMap<Integer,Integer> encapsulatedCountries = new HashMap<>();
		for(int countryId:getPlayerOwnedCountryIndexes(player.getId()))
		{
			int numTroops = board.getNumUnits(countryId);
			if(encapsulated(countryId) && numTroops > 1)
			{
				encapsulatedCountries.put(countryId,numTroops);
			}
		}
		return encapsulatedCountries;
	}

	//returns an arraylist of countryIds of countries connected to the argument ID
	private ArrayList<Integer> connectedCountries(int countryId)
	{
		ArrayList<Integer> connected = new ArrayList<>();
		for(int connectedId:getPlayerOwnedCountryIndexes(player.getId()))
		{
			if(board.isConnected(countryId,connectedId) && countryId != connectedId)
			{
				connected.add(connectedId);
			}
		}
		return connected;
	}

	//returns the ID of the connected country with the largest number of troops
	private int receiverId(int countryId) throws FileNotFoundException
	{
		double max = 0;
		int receiverId = -1;
		HashMap<Integer, Double> raffle = new HashMap<Integer, Double>();
		for (int connectedId : connectedCountries(countryId))
			raffle.put(connectedId, getCountryPriority(connectedId));

		for (Map.Entry<Integer, Double> entry : raffle.entrySet())
			if (entry.getValue() > max)
			{
				max = entry.getValue();
				receiverId = entry.getKey();
			}
		return receiverId;
	}

	/*returns true isthere is a country adjacent to the countryId passed as an argument
	that would complete a continent*/
	private int isNearCompleteContinent(int countryId)
	{
		for(int adjacent: GameData.ADJACENT[countryId])
			if(completesContinent(adjacent,player.getId()) && board.getOccupier(adjacent) != player.getId())
				return adjacent;
		return -1;
	}

	//returns the number of possible attacking troops from ally countries surrounding an enemy territory
	private int numAllyTroopsSurrounding(int enemyId)
	{
		int troops = 0;
		for(int adjacent: GameData.ADJACENT[enemyId])
		{
			if(board.getOccupier(adjacent) == player.getId())
			{
				troops += board.getNumUnits(adjacent) - 1;
			}
		}
		return troops;
	}

	/**
	 * END of Placement Methods
	 * */

}
