package hu.vemsoft.websocketdemo.repository;

import org.springframework.data.repository.CrudRepository;

import hu.vemsoft.websocketdemo.entity.GameCell;

public interface GameCellRepository extends CrudRepository<GameCell, Integer>{

	GameCell findByGameIdAndCellId(int gameId, Integer cellId);

}
