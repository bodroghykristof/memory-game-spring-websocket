package hu.vemsoft.websocketdemo.controller.websocket;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import hu.vemsoft.websocketdemo.entity.Game;
import hu.vemsoft.websocketdemo.entity.GameState;
import hu.vemsoft.websocketdemo.service.GameService;
import hu.vemsoft.websocketdemo.service.GameStateService;

@Component
public class GameWebsocketEventListener {

	@Autowired
	private GameWebsocketConnectionHandler connectionHandler;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@Autowired
	private GameService gameService;
	
	@Autowired
	private GameStateService gameStateService;

	private static final int WAITING_TIME_IN_MILLISECONDS = 5000;

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		Integer gameId = (Integer) headerAccessor.getSessionAttributes().get("gameId");

		if (gameId != null) {
			connectionHandler.registerUserExit(gameId);
			waitForReconnection(gameId);
		}
	}

	private void waitForReconnection(int gameId) {

		Runnable checkRefreshOrPageLeave = () -> {

			try {
				Thread.sleep(WAITING_TIME_IN_MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (connectionHandler.hasUserExitPermanently(gameId)) {
				messagingTemplate.convertAndSend("/topic/game/info/" + gameId, "Opponent has left the game");
				Optional<Game> gameOptional = gameService.findById(gameId);

				if (gameOptional.isPresent()) {
					
					GameState gameState = gameStateService.findByGameId(gameId);
					
					if (gameState.isFinished()) {
						gameService.deleteById(gameId);
					} else {
						gameState.setFinished(true);
						gameStateService.save(gameState);
					}
				}
			}
		};
		new Thread(checkRefreshOrPageLeave).start();
	}
}
