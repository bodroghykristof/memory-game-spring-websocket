package hu.vemsoft.websocketdemo.repository;

import org.springframework.data.repository.CrudRepository;

import hu.vemsoft.websocketdemo.entity.GameState;

public interface GameStateRepository extends CrudRepository<GameState, Integer> {

	GameState findByGameId(int gameId);

}
