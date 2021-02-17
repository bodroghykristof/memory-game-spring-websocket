package hu.vemsoft.websocketdemo.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.vemsoft.websocketdemo.entity.CellClass;
import hu.vemsoft.websocketdemo.entity.GameCell;
import hu.vemsoft.websocketdemo.repository.GameCellRepository;

@Service
public class GameCellService {

	@Autowired
	private GameCellRepository gameCellRepository;
	
	@Autowired
	private CellClassService cellClassService;

	@Transactional
	public void initNewGame(int gameId, int boardSize) {
		
		List<CellClass> distinctClassNames = cellClassService.getClassesByBoardSize(boardSize);
		List<CellClass> classNamesDuplicated = Stream.concat(distinctClassNames.stream(), distinctClassNames.stream())
													.collect(Collectors.toList());
		Collections.shuffle(classNamesDuplicated);
		
		int cellId = 1;
		for (CellClass cellClass : classNamesDuplicated) {
			GameCell cell = new GameCell(gameId, cellId, cellClass.getName());
			gameCellRepository.save(cell);
			cellId++;
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
	
	@Transactional
	public void deleteByGameId(int gameId) {
		gameCellRepository.deleteByGameId(gameId);
	}

}
