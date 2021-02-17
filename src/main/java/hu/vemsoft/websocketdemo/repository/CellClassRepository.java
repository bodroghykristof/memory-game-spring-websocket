package hu.vemsoft.websocketdemo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import hu.vemsoft.websocketdemo.entity.CellClass;

public interface CellClassRepository extends CrudRepository<CellClass, Integer>{

	@Query(value = "select cc from CellClass cc")
	List<CellClass> getClassesByBoardSize(Pageable pageable);

}
