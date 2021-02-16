package hu.vemsoft.websocketdemo.repository;

import org.springframework.data.repository.CrudRepository;

import hu.vemsoft.websocketdemo.entity.Game;

public interface GameRepository extends CrudRepository<Game, Integer>{

}
