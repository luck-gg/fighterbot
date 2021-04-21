package standard;
import ia.battle.core.FieldCell;
import ia.battle.core.Warrior;
import ia.battle.core.actions.Action;
import ia.exceptions.RuleException;
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
		if (context.getState()==null) {
			context.setState(new SearchState(this));
		}else {
			context.getState().setCharacter(this);
		}
		return context.gameAction();
	}

	@Override
	public void wasAttacked(int damage, FieldCell source) {
		/*context = GameContext.getInstance();
		if (this.getHealth()<10) {
			context.setState(new SuicideState(this));
		}*/
	}

	@Override
	public void enemyKilled() {
		/*
		context = GameContext.getInstance();
		context.setState(new SearchState(this));
		*/
	}

	@Override
	public void worldChanged(FieldCell oldCell, FieldCell newCell) {
		// TODO Auto-generated method stub
		
	}
	
}
