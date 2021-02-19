import {data_handler} from "./data_handler.js";
import {file_handler} from "./service/file_handler.js";


const joinButton = '<button class="join-button">Join Room</button>';
const startButton = '<button class="start-button">Start Game</button>';
localStorage.setItem('username', 'Default');

let stompClient;

init();

async function init() {
	await loadGames();
	connectToWebsocket();
    addCreatingRoomFunctionality();
    addJoiningRoomsFunctionality();
}

function loadGames() {
	return data_handler._api_get('/game', function(games) {
		for (let game of games) {
			let comment = game.userNameTwo === null ? joinButton : '';
			let gameDiv = createGameDiv(game, comment);
			let selector = game.userNameTwo === null ? '.join-room' : '.spectate-room';
			document.querySelector(selector).appendChild(gameDiv);
		}
	})
}

function createGameDiv(game, comment) {
	let gameDiv = document.createElement('div');
	gameDiv.classList.add('room');
	gameDiv.dataset.roomId = game.id;
	gameDiv.innerHTML = gameDivContent(game, comment);
	if (game.image) {
		const imageTag = `<img src=${game.image} style="width: 200px;" alt="image-${game.id}"></img>`;
		gameDiv.innerHTML += imageTag;
	}
	return gameDiv;
}

function gameDivContent(gameObj, comment) {
	return `<h4>Game number ${gameObj.id}</h4>
			<p><b>Player one:</b> <span class='playerOneData'>${gameObj.userNameOne}</span></p>
			<p><b>Player two:</b> <span class='playerTwoData'>${gameObj.userNameTwo}</span></p>
			<p><b>Board size:</b> <span class='boardSizeData'>${gameObj.boardSize}</span></p>
			<p><b>Started:</b> <span class='startedData'>${gameObj.hasStarted}</span></p>
			<p class="commentData">${comment}</p>`;
}

function connectToWebsocket() {
	
    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => stompClient.subscribe('/topic/room', gameRoomChange),
    		() => console.log("Connection error"));
	
}

function addCreatingRoomFunctionality() {
	const createButton = document.querySelector('#create-button');
	createButton.addEventListener('click', createNewRoom)
}

function addJoiningRoomsFunctionality() {
	const joinButtons = [...document.querySelectorAll('.join-button')];
	joinButtons.forEach(button => button.removeEventListener('click', joinRoom));
	joinButtons.forEach(button => button.addEventListener('click', joinRoom));
}

function addStartGameFunctionality() {
	const startButton = document.querySelector('.start-button');
	startButton.addEventListener('click', startGame);
}

async function startGame() {
	const roomCard = this.closest('.room');
	const gameObj = getGameObjFromCardWithoutPlayerTwo(roomCard);
	gameObj.userNameTwo = roomCard.querySelector('.playerTwoData').innerHTML;
	gameObj.hasStarted = true;
	await data_handler._api_post(`/game/init/${gameObj.id}`, {}, () => {});
	stompClient.send("/app/room", {}, JSON.stringify(gameObj));
}

function createNewRoom(e) {
	e.preventDefault();
    const username = document.querySelector('#username-input').value;
    localStorage.setItem('username', username);
    const boardSize = document.querySelector('#boardsize-input').value;
    const fileUploaded = document.querySelector('#room-image');
    if (username !== '') {
    	
	    if (fileUploaded.files.length > 0) {
	        file_handler._read_file_input(fileUploaded, (result) => {
	        	const gameData = {userNameOne: username, boardSize: boardSize, image: result};
	            stompClient.send("/app/room", {}, JSON.stringify(gameData));
	        })    
	      } else {
	    	const gameData = {userNameOne: username, boardSize: boardSize};
	    	stompClient.send("/app/room", {}, JSON.stringify(gameData));
	      }
    }

}

function gameRoomChange(data) {
	const newRoom = JSON.parse(data.body);
	const username = localStorage.getItem('username');
	
	// The game is set to start, redirecting players to game page...
	if (newRoom.hasStarted && (newRoom.userNameOne === username || newRoom.userNameTwo === username)) {
		localStorage.setItem('game', JSON.stringify(newRoom));
		window.location.replace('/index.html');
	}
	
	// Client is the owner of the room
	if (newRoom.userNameOne === username) {
		
		// Client has just created a new room
		if (newRoom.userNameTwo === null) {
			let newRoomDiv = createGameDiv(newRoom, 'Waiting for opponent to join...');
			document.querySelector('.create-room form').remove();
			document.querySelector('.create-room').appendChild(newRoomDiv);
		}
		// Opponent has joined to client's room
		else {
			document.querySelector('.create-room .room .commentData').innerHTML = startButton;
			document.querySelector('.create-room .room .playerTwoData').innerHTML = newRoom.userNameTwo;
			addStartGameFunctionality();
		}
	} else if (newRoom.userNameTwo === null) {
		// Someone created a room which is now visible to client
		let newRoomDiv = createGameDiv(newRoom, joinButton);
		document.querySelector('.join-room').appendChild(newRoomDiv);
		addJoiningRoomsFunctionality();
	}
}

function getGameObjFromCardWithoutPlayerTwo(roomCard) {
	const id = parseInt(roomCard.dataset.roomId);
	const playerOne = roomCard.querySelector('.playerOneData').innerHTML;
	const playerTwo = localStorage.getItem('username');
	const boardSize = parseInt(roomCard.querySelector('.boardSizeData').innerHTML);
	const started = roomCard.querySelector('.startedData').innerHTML === 'true' ? true : false;
	const image =  roomCard.querySelector('img') ? roomCard.querySelector('img').src : null;
	return {id: id, userNameOne: playerOne,  boardSize: boardSize, hasStarted: started, image: image};
}

function joinRoom() {
	
	const roomCard = this.closest('.room');
	const gameObj = getGameObjFromCardWithoutPlayerTwo(roomCard);
	gameObj.userNameTwo = localStorage.getItem('username');
	
	roomCard.querySelector('.playerTwoData').innerHTML = localStorage.getItem('username');
	roomCard.querySelector('.commentData').innerHTML = 'Waiting for host to start game...';
	
	stompClient.send("/app/room", {}, JSON.stringify(gameObj))
}


function addJoiningRoomFunctionality() {
    const joinButtons = [...document.querySelectorAll('.join-button')];
    joinButtons.forEach(button => button.addEventListener('click', joinRoom))
}


