package hu.vemsoft.websocketdemo.mapper;

import org.springframework.stereotype.Component;

import hu.vemsoft.websocketdemo.entity.GameStep;

@Component
public class GameStepMapper {
	
	public void updateGameStep(GameStep old, GameStep fresh) {
		old.setCellIdOne(fresh.getCellIdOne());
		old.setClassOne(fresh.getClassOne());
		old.setCellIdTwo(fresh.getCellIdTwo());
		old.setClassTwo(fresh.getClassTwo());
	}

}
