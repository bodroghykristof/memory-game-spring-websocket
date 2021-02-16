package hu.vemsoft.websocketdemo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String userNameOne;
	private String userNameTwo;
	private Integer boardSize;
	private boolean hasStarted;

	protected Game() {
	}

	public Game(String userNameOne, int boardSize) {
		super();
		this.userNameOne = userNameOne;
		this.boardSize = boardSize;
		this.hasStarted = false;
	}

	public Game(String userNameOne, String userNameTwo, Integer boardSize, boolean hasStarted) {
		super();
		this.userNameOne = userNameOne;
		this.userNameTwo = userNameTwo;
		this.boardSize = boardSize;
		this.hasStarted = hasStarted;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserNameOne() {
		return userNameOne;
	}

	public void setUserNameOne(String userNameOne) {
		this.userNameOne = userNameOne;
	}

	public String getUserNameTwo() {
		return userNameTwo;
	}

	public void setUserNameTwo(String userNameTwo) {
		this.userNameTwo = userNameTwo;
	}

	public Integer getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(Integer boardSize) {
		this.boardSize = boardSize;
	}

	public boolean isHasStarted() {
		return hasStarted;
	}

	public void setHasStarted(boolean hasStarted) {
		this.hasStarted = hasStarted;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", userNameOne=" + userNameOne + ", userNameTwo=" + userNameTwo + ", boardSize="
				+ boardSize + ", hasStarted=" + hasStarted + "]";
	}

}
