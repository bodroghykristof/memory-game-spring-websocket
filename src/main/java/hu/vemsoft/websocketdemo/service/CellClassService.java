package hu.vemsoft.websocketdemo.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import hu.vemsoft.websocketdemo.entity.CellClass;
import hu.vemsoft.websocketdemo.repository.CellClassRepository;

@Service
public class CellClassService {
	
	@Autowired
	private CellClassRepository cellClassRepository;
	

	public List<CellClass> getClassesByBoardSize(int boardSize) {
		return cellClassRepository.getClassesByBoardSize(PageRequest.of(0,  (int) Math.pow(boardSize, 2) / 2));
	}
	
	@PostConstruct
	public void addCellClasses() {
		cellClassRepository.save(new CellClass("fa-star"));
		cellClassRepository.save(new CellClass("fa-shower"));
		cellClassRepository.save(new CellClass("fa-university"));
		cellClassRepository.save(new CellClass("fa-car"));
		cellClassRepository.save(new CellClass("fa-anchor"));
		cellClassRepository.save(new CellClass("fa-bell"));
		cellClassRepository.save(new CellClass("fa-bicycle"));
		cellClassRepository.save(new CellClass("fa-bullseye"));
		cellClassRepository.save(new CellClass("fa-bath"));
		cellClassRepository.save(new CellClass("fa-envelope-open"));
		cellClassRepository.save(new CellClass("fa-handshake-o"));
		cellClassRepository.save(new CellClass("fa-snowflake-o"));
		cellClassRepository.save(new CellClass("fa-thermometer"));
		cellClassRepository.save(new CellClass("fa-user-circle"));
		cellClassRepository.save(new CellClass("fa-arrows"));
		cellClassRepository.save(new CellClass("fa-archive"));
		cellClassRepository.save(new CellClass("fa-asterisk"));
		cellClassRepository.save(new CellClass("fa-adjust"));
		cellClassRepository.save(new CellClass("fa-bar-chart"));
		cellClassRepository.save(new CellClass("fa-battery"));
		cellClassRepository.save(new CellClass("fa-bed"));
		cellClassRepository.save(new CellClass("fa-birthday-cake"));
		cellClassRepository.save(new CellClass("fa-bluetooth"));
		cellClassRepository.save(new CellClass("fa-bolt"));
		cellClassRepository.save(new CellClass("fa-bomb"));
		cellClassRepository.save(new CellClass("fa-book"));
		cellClassRepository.save(new CellClass("fa-bug"));
		cellClassRepository.save(new CellClass("fa-calendar"));
		cellClassRepository.save(new CellClass("fa-camera"));
		cellClassRepository.save(new CellClass("fa-check"));
		cellClassRepository.save(new CellClass("fa-cloud"));
		cellClassRepository.save(new CellClass("fa-code"));
		cellClassRepository.save(new CellClass("fa-coffee"));
		cellClassRepository.save(new CellClass("fa-cog"));
		cellClassRepository.save(new CellClass("fa-cube"));
		cellClassRepository.save(new CellClass("fa-database"));
		cellClassRepository.save(new CellClass("fa-diamond"));
		cellClassRepository.save(new CellClass("fa-eye"));
		cellClassRepository.save(new CellClass("fa-fighter-jet"));
		cellClassRepository.save(new CellClass("fa-film"));
		cellClassRepository.save(new CellClass("fa-filter"));
		cellClassRepository.save(new CellClass("fa-fire"));
		cellClassRepository.save(new CellClass("fa-flag"));
		cellClassRepository.save(new CellClass("fa-flask"));
		cellClassRepository.save(new CellClass("fa-futbol-o"));
		cellClassRepository.save(new CellClass("fa-glass"));
		cellClassRepository.save(new CellClass("fa-globe"));
		cellClassRepository.save(new CellClass("fa-heart"));
		cellClassRepository.save(new CellClass("fa-home"));
		cellClassRepository.save(new CellClass("fa-key"));
		cellClassRepository.save(new CellClass("fa-leaf"));
		cellClassRepository.save(new CellClass("fa-legal"));
		cellClassRepository.save(new CellClass("fa-lock"));
	}

}
