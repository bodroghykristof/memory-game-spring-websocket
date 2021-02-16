package hu.vemsoft.websocketdemo.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import hu.vemsoft.websocketdemo.constants.PlayerIndex;

@Entity
public class GameState {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer gameId;
	private Integer firstPlayerPoints;
	private Integer secondPlayerPoints;

	@Enumerated(EnumType.STRING)
	private PlayerIndex onRound;

	private boolean isFirstGuess;

	@Transient
	private GameStep lastStep;
	
	protected GameState() {}

	public GameState(Integer gameId) {
		super();
		this.gameId = gameId;
		this.firstPlayerPoints = 0;
		this.secondPlayerPoints = 0;
		this.onRound = PlayerIndex.FIRST;
		this.isFirstGuess = true;
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

	public Integer getFirstPlayerPoints() {
		return firstPlayerPoints;
	}

	public void setFirstPlayerPoints(Integer firstPlayerPoints) {
		this.firstPlayerPoints = firstPlayerPoints;
	}

	public Integer getSecondPlayerPoints() {
		return secondPlayerPoints;
	}

	public void setSecondPlayerPoints(Integer secondPlayerPoints) {
		this.secondPlayerPoints = secondPlayerPoints;
	}

	public PlayerIndex getOnRound() {
		return onRound;
	}

	public void setOnRound(PlayerIndex onRound) {
		this.onRound = onRound;
	}

	public boolean isFirstGuess() {
		return isFirstGuess;
	}

	public void setFirstGuess(boolean isFirstGuess) {
		this.isFirstGuess = isFirstGuess;
	}

	public GameStep getLastStep() {
		return lastStep;
	}

	public void setLastStep(GameStep lastStep) {
		this.lastStep = lastStep;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
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
		GameState other = (GameState) obj;
		if (gameId == null) {
			if (other.gameId != null)
				return false;
		} else if (!gameId.equals(other.gameId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GameState [id=" + id + ", gameId=" + gameId + ", firstPlayerPoints=" + firstPlayerPoints
				+ ", secondPlayerPoints=" + secondPlayerPoints + ", onRound=" + onRound + ", isFirstGuess="
				+ isFirstGuess + "]";
	}

}
