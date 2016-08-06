package main.players;

import main.enums.Color;
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
	
	public void println(String s) {
		System.out.println(Color.NONE + s + Color.NONE);
	}

	@Override
	public void message(String message) {
		System.out.println("[" + System.currentTimeMillis() + "] " + message);
	}
}
