package hu.vemsoft.websocketdemo.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.vemsoft.websocketdemo.constants.PlayerIndex;
import hu.vemsoft.websocketdemo.entity.Game;
import hu.vemsoft.websocketdemo.entity.GameCell;
import hu.vemsoft.websocketdemo.entity.GameState;
import hu.vemsoft.websocketdemo.entity.GameStep;
import hu.vemsoft.websocketdemo.repository.GameRepository;

@Service
public class GameService {

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private GameCellService gameCellService;

	@Autowired
	private GameStateService gameStateService;

	public List<Game> findAll() {
		List<Game> games = new ArrayList<>();
		for (Game game : gameRepository.findAll()) {
			games.add(game);
		}
		return games;
	}

	@Transactional
	public void initNewGame(int gameId) {
		gameCellService.initNewGame(gameId);
		gameStateService.initNewGame(gameId);
	}

	@Transactional
	public Game save(Game game) {
		return gameRepository.save(game);
	}

	@Transactional
	public void saveAll(List<Game> games) {
		gameRepository.saveAll(games);
	}

	@Transactional
	public GameState handleFirstStep(GameStep gameStep) {
		GameState gameState = gameStateService.findByGameId(gameStep.getGameId());
		gameState.setFirstGuess(true);
		GameCell gameCell = gameCellService.findByGameIdAndCellId(gameStep.getGameId(), gameStep.getCellIdOne());
		gameStep.setClassOne(gameCell.getRevealedClass());
		gameState.setLastStep(gameStep);
		gameStateService.save(gameState);
		return gameState;
	}

	@Transactional
	public GameState handleSecondStep(GameStep gameStep) {
		GameState gameState = gameStateService.findByGameId(gameStep.getGameId());
		gameState.setFirstGuess(false);
		GameCell gameCell = gameCellService.findByGameIdAndCellId(gameStep.getGameId(), gameStep.getCellIdTwo());
		gameStep.setClassTwo(gameCell.getRevealedClass());
		updatePointsAndOnRound(gameState, gameStep);
		gameState.setLastStep(gameStep);
		gameStateService.save(gameState);
		return gameState;
	}

	private void updatePointsAndOnRound(GameState gameState, GameStep gameStep) {
		if (gameStep.getClassOne().equals(gameStep.getClassTwo())) {	
			if (gameState.getOnRound().equals(PlayerIndex.FIRST)) {
				gameState.setFirstPlayerPoints(gameState.getFirstPlayerPoints() + 1);
			} else {
				gameState.setSecondPlayerPoints(gameState.getSecondPlayerPoints() + 1);
			}
		} else {
			gameState.setOnRound(gameState.getOnRound() == PlayerIndex.FIRST ? PlayerIndex.SECOND : PlayerIndex.FIRST);
		}
	}

}
