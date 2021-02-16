package hu.vemsoft.websocketdemo.controller.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import hu.vemsoft.websocketdemo.entity.ChatMessage;
import hu.vemsoft.websocketdemo.entity.Game;
import hu.vemsoft.websocketdemo.entity.GameState;
import hu.vemsoft.websocketdemo.entity.GameStep;
import hu.vemsoft.websocketdemo.service.GameService;

@Controller
public class GameWebsocketController {
	
	@Autowired
	private GameService gameService;
	
    @MessageMapping("/room")
    @SendTo("/topic/room")
    public Game saveRoom(@Payload Game game) {
    	return gameService.save(game);
    }
    
    @MessageMapping("/game/step/{id}")
    @SendTo("/topic/game/step/{id}")
    public GameState executeGameStep(@DestinationVariable int id, GameStep gameStep) {
    	if (gameStep.getCellIdTwo() == null) {
    		return gameService.handleFirstStep(gameStep);
    	} else {
    		return gameService.handleSecondStep(gameStep);
    	}
    }
    
    @MessageMapping("/game/chat/{id}")
    @SendTo("/topic/game/chat/{id}")
    public ChatMessage sendGameChatMessage(@DestinationVariable int id, ChatMessage message) {
    	System.out.println("Message transferred: " + message);
    	return message;
    }

}
