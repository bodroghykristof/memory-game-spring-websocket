package hu.vemsoft.websocketdemo.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.vemsoft.websocketdemo.constants.PlayerIndex;
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
	
	public void updatePoints(GameState gameState) {
		if (gameState.getOnRound().equals(PlayerIndex.FIRST)) {
			gameState.setFirstPlayerPoints(gameState.getFirstPlayerPoints() + 1);
		} else {
			gameState.setSecondPlayerPoints(gameState.getSecondPlayerPoints() + 1);
		}
	}

	public void switchRounds(GameState gameState) {
		gameState.setOnRound(gameState.getOnRound() == PlayerIndex.FIRST ? PlayerIndex.SECOND : PlayerIndex.FIRST);
	}
	
	@Transactional
	public void deleteByGameId(int gameId) {
		gameStateRepository.deleteByGameId(gameId);
	}

}
