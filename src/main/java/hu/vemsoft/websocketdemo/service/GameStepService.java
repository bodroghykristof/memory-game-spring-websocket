package hu.vemsoft.websocketdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.vemsoft.websocketdemo.entity.GameStep;
import hu.vemsoft.websocketdemo.mapper.GameStepMapper;
import hu.vemsoft.websocketdemo.repository.GameStepRepository;

@Service
public class GameStepService {
	
	@Autowired
	private GameStepRepository gameStepRepository;
	
	@Autowired
	private GameStepMapper gameStepMapper;
	
	public void initNewGame(int gameId) {
		gameStepRepository.save(new GameStep(gameId));
	}
	
	public void save(GameStep gameStep) {
		gameStepRepository.save(gameStep);
	}

	public void saveLastGameStep(GameStep gameStep) {
		GameStep lastGameStep = gameStepRepository.findByGameId(gameStep.getGameId());
		gameStepMapper.updateGameStep(lastGameStep, gameStep);
		gameStepRepository.save(lastGameStep);
	}
	
	public GameStep findByGameId(int gameId) {
		return gameStepRepository.findByGameId(gameId);
	}

	
}
