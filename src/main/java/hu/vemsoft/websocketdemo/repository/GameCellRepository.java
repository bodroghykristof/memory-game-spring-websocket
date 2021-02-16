package hu.vemsoft.websocketdemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import hu.vemsoft.websocketdemo.entity.GameCell;

public interface GameCellRepository extends CrudRepository<GameCell, Integer>{

	GameCell findByGameIdAndCellId(int gameId, Integer cellId);
	
	@Query(value = "select c from GameCell c where c.gameId = :gameId and c.isRevealed = true")
	List<GameCell> findGuessedCellsByGameId(@Param("gameId") int gameId);

}
