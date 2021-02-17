package hu.vemsoft.websocketdemo.controller.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import hu.vemsoft.websocketdemo.service.GameService;

@Component
public class GameWebsocketEventListener {

	@Autowired
	private GameWebsocketConnectionHandler connectionHandler;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@Autowired
	private GameService gameService;

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
				if (gameService.findById(gameId).isPresent()) {
					gameService.deleteById(gameId);
				}
			}
		};

		new Thread(checkRefreshOrPageLeave).start();

	}

}
