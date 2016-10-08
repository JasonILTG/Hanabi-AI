package game.logic.event;

import game.player.AbstractPlayer;

public class HintEvent
		extends AbstractEvent
{
	public final AbstractPlayer target;
	
	public HintEvent(AbstractPlayer actor, AbstractPlayer target)
	{
		super(actor);
		this.target = target;
	}
}
