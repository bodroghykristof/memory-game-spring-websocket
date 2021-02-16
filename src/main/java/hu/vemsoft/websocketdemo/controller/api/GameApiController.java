package hu.vemsoft.websocketdemo.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.vemsoft.websocketdemo.entity.Game;
import hu.vemsoft.websocketdemo.service.GameService;

@RestController
@RequestMapping("/game")
public class GameApiController {
	
	@Autowired
	private GameService gameService;
	
	@GetMapping()
	public List<Game> findAll() {
		return gameService.findAll();
	}
	
	@PostMapping("/init/{gameId}")
	public void initNewGame(@PathVariable("gameId") int gameId) {
		gameService.initNewGame(gameId);
	}

}
