package Ghreborn.model.players.skills;

import java.util.Optional;

import Ghreborn.Server;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.players.Client;

public class Skilling {
	
	Client player;
	
	private Optional<Skill> skill = Optional.empty();
	
	public Skilling(Client player) {
		this.player = player;
	}
	
	public void add(CycleEvent event, int ticks) {
		CycleEventHandler.getSingleton().addEvent(this, event, ticks);
	}
	
	public void stop() {
		Server.getEventHandler().stop(player, "skilling");
		CycleEventHandler.getSingleton().stopEvents(this);
		skill = Optional.empty();
	}
	
	public boolean isSkilling() {
		return skill.isPresent();
	}
	
	public Skill getSkill() {
		return skill.orElse(null);
	}
	
	public void setSkill(Skill skill) {
		this.skill = Optional.of(skill);
	}

}
