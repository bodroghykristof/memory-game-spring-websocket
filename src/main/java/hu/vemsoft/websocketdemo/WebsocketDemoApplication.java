package hu.vemsoft.websocketdemo;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.vemsoft.websocketdemo.entity.Game;
import hu.vemsoft.websocketdemo.service.GameService;

@SpringBootApplication
public class WebsocketDemoApplication {
	
	@Autowired
	private GameService gameService;

	public static void main(String[] args) {
		SpringApplication.run(WebsocketDemoApplication.class, args);
		try {
			Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@PostConstruct
	public void addSampleGames() {
		Game gameOne = new Game("John", "Emily", 6, true);
		Game gameTwo = new Game("Edward", "Lucie", 7, true);
		Game gameThree = new Game("Alen", "Sandy", 10, false);
		Game gameFour = new Game("Sophie", 5);
		Game gameFive = new Game("Susan", 7);
		Game gameSix = new Game("Tracy", 7);
		
		List<Game> runningGames = List.of(gameOne, gameTwo, gameThree, gameFour, gameFive, gameSix);
		gameService.saveAll(runningGames);
		
	}

}
