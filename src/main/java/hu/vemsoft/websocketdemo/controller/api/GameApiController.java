package hu.vemsoft.websocketdemo.controller.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.vemsoft.websocketdemo.entity.Game;
import hu.vemsoft.websocketdemo.entity.GameCell;
import hu.vemsoft.websocketdemo.entity.GameState;
import hu.vemsoft.websocketdemo.entity.GameStep;
import hu.vemsoft.websocketdemo.service.GameCellService;
import hu.vemsoft.websocketdemo.service.GameService;
import hu.vemsoft.websocketdemo.service.GameStateService;
import hu.vemsoft.websocketdemo.service.GameStepService;

@RestController
@RequestMapping("/game")
public class GameApiController {
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private GameCellService gameCellService;
	
	@Autowired
	private GameStateService gameStateService;
	
	@Autowired
	private GameStepService gameStepService;
	
	@GetMapping()
	public List<Game> findAll() {
		return gameService.findAll();
	}
	
	@PostMapping("/init/{gameId}")
	public void initNewGame(@PathVariable("gameId") int gameId) throws Exception {
		Optional<Game> game = gameService.findById(gameId);
		if (game.isPresent()) {
			gameService.initNewGame(game.get());			
		} else throw new Exception("The requested game does not exist");
	}
	
	@GetMapping("/cells/{gameId}")
	public List<GameCell> getCells(@PathVariable("gameId") int gameId) {
		return gameCellService.findGuessedCellsByGameId(gameId);
	}
	
	@GetMapping("/state/{gameId}")
	public GameState getGameState(@PathVariable("gameId") int gameId) {
		GameState gameState = gameStateService.findByGameId(gameId);
		GameStep lastStep = gameStepService.findByGameId(gameId);
		gameState.setLastStep(lastStep);
		return gameState;
	}

}
