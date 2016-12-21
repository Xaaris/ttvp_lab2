
public class GameLogic {
	
	private static GameLogic gameLogic = null;
	
	private GameLogic() {
	}

	public static GameLogic getInstance() {
		if (gameLogic == null) {
			gameLogic = new GameLogic();
		}
		return gameLogic;
	}

}
