package hu.vemsoft.websocketdemo.service;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.vemsoft.websocketdemo.entity.GameCell;
import hu.vemsoft.websocketdemo.repository.GameCellRepository;

@Service
public class GameCellService {

	private List<Integer> classIndices = Arrays.asList(5, 13, 1, 11, 7, 6, 12, 4, 15, 2, 3, 14, 16, 8, 10, 9);
	private List<String> classes = Arrays.asList("fa-star", "fa-shower", "fa-university", "fa-car", "fa-anchor",
			"fa-bell", "fa-bicycle", "fa-bullseye");

	@Autowired
	private GameCellRepository gameCellRepository;

	@Transactional
	public void initNewGame(int gameId) {
		
		for (int i = 0; i < classIndices.size(); i++) {
			
			int cellId = classIndices.get(i);
			String revealedClass = classes.get(i / 2);
			GameCell cell = new GameCell(gameId, cellId, revealedClass);
			gameCellRepository.save(cell);
			
			System.out.println(cell);
			
		}
		

	}
	
	@Transactional
	public GameCell findByGameIdAndCellId(int gameId, Integer cellId) {
		return gameCellRepository.findByGameIdAndCellId(gameId, cellId);
	}
	
	@Transactional
	public void save(GameCell gameCell) {
		gameCellRepository.save(gameCell);
	}
	
	@Transactional
	public List<GameCell> findGuessedCellsByGameId(int gameId) {
		return gameCellRepository.findGuessedCellsByGameId(gameId);
	}

}
