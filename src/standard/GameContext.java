package standard;
import action.CharacterAction;
import ia.battle.core.BattleField;
import ia.battle.core.WarriorData;
import ia.battle.core.actions.Action;
import state.*;

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
	public Action gameAction(Character character) {
		BattleField bf = BattleField.getInstance();
		WarriorData wd = bf.getEnemyData();
		WarriorData hd = bf.getHunterData();
		
		player.setWarrior(character);
		player.setBattlefield(bf);
	
		//Normal Attack
		if (wd.getInRange() && !hd.getInRange()) {
			this.setState(new AttackState());
		}
		
		//Running Away from Hunter
		else if (bf.calculateDistance(character.getPosition(), hd.getFieldCell())<10) {
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
		System.out.print(state.toString());
		return state.characterAction(player);
	}
	

}