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
		boolean hunterAway = bf.calculateDistance(character.getPosition(), hd.getFieldCell()) > 10 ? true:false;
		
		System.out.print("Hunter at : " + bf.calculateDistance(character.getPosition(), hd.getFieldCell()));
		player.setWarrior(character);
		player.setBattlefield(bf);
	
		if (hunterAway) {
			//Atacando Enemigo
			if (wd.getInRange() && character.getHealth()>10) {
				this.setState(new AttackState());
			}
			//Low Health
			else if (wd.getInRange() && character.getHealth()<=10) {
				this.setState(new SuicideState());
			}
			//Picking SI
			else if (character.getHealth()>40 && !wd.getInRange()) {
				//TODO: Esto esta levantando una excepcion y hace que muera el Character
				this.setState(new PickingSIState());
			}
			
			//Buscando Enemigo
			else {
				double enemyDistance = bf.calculateDistance(character.getPosition(), wd.getFieldCell());
				//Luring
				if (character.getRange()+5>enemyDistance && character.getRange()<=enemyDistance) {
					this.setState(new WaitState());
				}else {
					this.setState(new SearchState());
				}
			}
		}
		//Running Away from Hunter
		else if (!hunterAway) {
			//Low Health
			if (character.getHealth()<=10) {
				this.setState(new SuicideState());
			}else {
				this.setState(new RunAwayState());
			}
		}
		
		System.out.print(state.toString() + "\n");
		return state.characterAction(player);
	}
	

}