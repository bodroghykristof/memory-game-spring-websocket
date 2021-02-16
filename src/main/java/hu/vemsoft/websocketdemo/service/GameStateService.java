package hu.vemsoft.websocketdemo.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.vemsoft.websocketdemo.entity.GameState;
import hu.vemsoft.websocketdemo.repository.GameStateRepository;

@Service
public class GameStateService {
	
	@Autowired
	private GameStateRepository gameStateRepository;
	
	@Transactional
	public void initNewGame(int gameId) {
		gameStateRepository.save(new GameState(gameId));
	}
	
	@Transactional
	public GameState findByGameId(int gameId) {
		return gameStateRepository.findByGameId(gameId);
	}
	
	@Transactional
	public void save(GameState gameState) {
		gameStateRepository.save(gameState);
	}

}
