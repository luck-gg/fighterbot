package standard;
import action.CharacterAction;
import ia.battle.core.BattleField;
import ia.battle.core.WarriorData;
import state.AttackState;
import state.CharacterState;
import state.SearchState;

public class GameContext {
	
	private CharacterState state = null;
	private CharacterAction player = new CharacterAction();

	private static GameContext instance = null;
	
	public static GameContext getInstance() {
		if (instance == null) {
			instance = new GameContext(); 
		}
		return instance;
	}
	
    private GameContext(){
    }
    
	public void setState(CharacterState state) {
		this.state = state;
	}
	public void gameAction(Character character) {
		player.setWarrior(character);
		BattleField bf = BattleField.getInstance();
		WarriorData wd = bf.getEnemyData();
		WarriorData hd = bf.getHunterData();
		
		//Normal Attack
		if (wd.getInRange() && !hd.getInRange()) {
			this.setState(new AttackState());
		}
		//Running Away from Hunter
		else if (hd.getInRange()) {
			this.setState(new RunAwayState());
		}
		//Picking SI
		else if (character.getHealth()>40 && !wd.getInRange() && !hd.getInRange()) {
			this.setState(new PickingSIState());
		}
		//Low Health
		else if (character.getHealth()<20 || state.toString()=="SUICIDE") {
			this.setState(new SuicideState());
		}
		//Normal Search
		else {
			this.setState(new SearchState());
		}
			
		state.characterAction(player);
	}
	

}