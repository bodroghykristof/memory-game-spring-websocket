package hu.vemsoft.websocketdemo.repository;

import org.springframework.data.repository.CrudRepository;

import hu.vemsoft.websocketdemo.entity.GameStep;

public interface GameStepRepository extends CrudRepository<GameStep, Integer> {

	GameStep findByGameId(int gameId);

	void deleteByGameId(int gameId);

}
