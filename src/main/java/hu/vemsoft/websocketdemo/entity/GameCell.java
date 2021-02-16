package hu.vemsoft.websocketdemo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GameCell {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer gameId;
	private Integer cellId;
	private String revealedClass;
	
	protected GameCell() {}

	public GameCell(int gameId, Integer cellId, String revealedClass) {
		super();
		this.gameId = gameId;
		this.cellId = cellId;
		this.revealedClass = revealedClass;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public Integer getCellId() {
		return cellId;
	}

	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	public String getRevealedClass() {
		return revealedClass;
	}

	public void setRevealedClass(String revealedClass) {
		this.revealedClass = revealedClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cellId == null) ? 0 : cellId.hashCode());
		result = prime * result + gameId;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((revealedClass == null) ? 0 : revealedClass.hashCode());
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
		GameCell other = (GameCell) obj;
		if (cellId == null) {
			if (other.cellId != null)
				return false;
		} else if (!cellId.equals(other.cellId))
			return false;
		if (gameId != other.gameId)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (revealedClass == null) {
			if (other.revealedClass != null)
				return false;
		} else if (!revealedClass.equals(other.revealedClass))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GameCell [id=" + id + ", gameId=" + gameId + ", cellId=" + cellId + ", revealedClass=" + revealedClass
				+ "]";
	}

}
