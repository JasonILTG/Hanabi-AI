package main.players;

import main.event.Event;
import main.game.HanabiGame;
import main.moves.Move;

public class Human
		extends Player
{
	// TODO implement user interface methods
	
	public Human(String name, HanabiGame game)
	{
		super(name, game);
	}
	
	@Override
	public void onEvent(Event event)
	{
		
	}
	
	@Override
	public Move getNextMove()
	{
		return null;
	}
	
	@Override
	protected Class<? extends Event>[] getListenableEventClasses()
	{
		return new Class[0];
	}
	
	public void message(String msg)
	{
		System.out.println(msg);
	}
}
