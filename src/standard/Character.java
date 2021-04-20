package standard;
import ia.battle.core.FieldCell;
import ia.battle.core.Warrior;
import ia.battle.core.actions.Action;
import ia.exceptions.RuleException;
import state.PickingSIState;
import state.RunAwayState;
import state.SearchState;

public class Character extends Warrior {
	GameContext context = null;
	public GameContext getContext() {
		return context;
	}

	public Character(String name, int health, int defense, int strength, int speed, int range) throws RuleException {
		super(name, health, defense, strength, speed, range);
	}
	
	@Override
	public Action playTurn(long tick, int actionNumber) {

		context = GameContext.getInstance();
		return context.setState(new SearchState(this));

	
	}

	@Override
	public void wasAttacked(int damage, FieldCell source) {
		context = GameContext.getInstance();
		context.setState(new RunAwayState());
	}

	@Override
	public void enemyKilled() {
		context = GameContext.getInstance();
		context.setState(new PickingSIState());
	}

	@Override
	public void worldChanged(FieldCell oldCell, FieldCell newCell) {
		// TODO Auto-generated method stub
		
	}
	
}
