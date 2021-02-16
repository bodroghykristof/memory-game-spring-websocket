package hu.vemsoft.websocketdemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.vemsoft.websocketdemo.entity.Game;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer>{

}
