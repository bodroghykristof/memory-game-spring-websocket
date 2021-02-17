package hu.vemsoft.websocketdemo.controller.websocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class GameWebsocketConnectionHandler {
	
	private Map<Integer, Boolean> mapForUserExit = new HashMap<>();
	
	public void registerUserExit(Integer gameId) {
		mapForUserExit.put(gameId, true);
	}
	
	public void registerUserConnect(Integer gameId) {
		mapForUserExit.put(gameId, false);
	}
	
	public boolean hasUserExitPermanently(Integer gameId) {
		return mapForUserExit.get(gameId);
	}
	
}
