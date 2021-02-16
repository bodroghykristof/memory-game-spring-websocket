package hu.vemsoft.websocketdemo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GameStep {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int gameId;
	private Integer cellIdOne;
	private Integer cellIdTwo;
	private String classOne;
	private String classTwo;

	protected GameStep() {
	}

	public GameStep(int gameId) {
		super();
		this.gameId = gameId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public Integer getCellIdOne() {
		return cellIdOne;
	}

	public void setCellIdOne(Integer cellIdOne) {
		this.cellIdOne = cellIdOne;
	}

	public Integer getCellIdTwo() {
		return cellIdTwo;
	}

	public void setCellIdTwo(Integer cellIdTwo) {
		this.cellIdTwo = cellIdTwo;
	}

	public String getClassOne() {
		return classOne;
	}

	public void setClassOne(String classOne) {
		this.classOne = classOne;
	}

	public String getClassTwo() {
		return classTwo;
	}

	public void setClassTwo(String classTwo) {
		this.classTwo = classTwo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cellIdOne == null) ? 0 : cellIdOne.hashCode());
		result = prime * result + ((cellIdTwo == null) ? 0 : cellIdTwo.hashCode());
		result = prime * result + ((classOne == null) ? 0 : classOne.hashCode());
		result = prime * result + ((classTwo == null) ? 0 : classTwo.hashCode());
		result = prime * result + gameId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameStep other = (GameStep) obj;
		if (cellIdOne == null) {
			if (other.cellIdOne != null)
				return false;
		} else if (!cellIdOne.equals(other.cellIdOne))
			return false;
		if (cellIdTwo == null) {
			if (other.cellIdTwo != null)
				return false;
		} else if (!cellIdTwo.equals(other.cellIdTwo))
			return false;
		if (classOne == null) {
			if (other.classOne != null)
				return false;
		} else if (!classOne.equals(other.classOne))
			return false;
		if (classTwo == null) {
			if (other.classTwo != null)
				return false;
		} else if (!classTwo.equals(other.classTwo))
			return false;
		if (gameId != other.gameId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GameStep [id=" + id + ", gameId=" + gameId + ", cellIdOne=" + cellIdOne + ", cellIdTwo=" + cellIdTwo
				+ ", classOne=" + classOne + ", classTwo=" + classTwo + "]";
	}

}
