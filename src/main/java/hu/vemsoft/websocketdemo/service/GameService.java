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

	@Autowired
	private GameStepService gameStepService;

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
		gameStepService.initNewGame(gameId);
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
		GameCell gameCell = gameCellService.findByGameIdAndCellId(gameStep.getGameId(), gameStep.getCellIdOne());
		gameCell.setRevealed(true);
		gameCellService.save(gameCell);
		gameStep.setClassOne(gameCell.getRevealedClass());
		gameStepService.saveLastGameStep(gameStep);
		gameState.setFirstGuess(true);
		gameState.setLastStep(gameStep);
		gameStateService.save(gameState);
		return gameState;
	}

	@Transactional
	public GameState handleSecondStep(GameStep gameStep) {
		GameState gameState = gameStateService.findByGameId(gameStep.getGameId());
		GameCell gameCell = gameCellService.findByGameIdAndCellId(gameStep.getGameId(), gameStep.getCellIdTwo());
		gameStep.setClassTwo(gameCell.getRevealedClass());
		gameStepService.saveLastGameStep(gameStep);
		manageMatchingStepsCase(gameState, gameStep, gameCell);
		gameState.setFirstGuess(false);
		gameState.setLastStep(gameStep);
		gameStateService.save(gameState);
		return gameState;
	}

	private void manageMatchingStepsCase(GameState gameState, GameStep gameStep, GameCell secondStepGameCell) {
		if (gameStep.getClassOne().equals(gameStep.getClassTwo())) {
			secondStepGameCell.setRevealed(true);
			gameCellService.save(secondStepGameCell);
			gameStateService.updatePoints(gameState);
		} else {
			GameCell firstStepGameCell = gameCellService.findByGameIdAndCellId(gameStep.getGameId(),
					gameStep.getCellIdOne());
			firstStepGameCell.setRevealed(false);
			gameCellService.save(firstStepGameCell);
			gameStateService.switchRounds(gameState);
		}
	}


}
