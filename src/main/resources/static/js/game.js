import {data_handler} from "./data_handler.js";

const gameData = JSON.parse(localStorage.getItem('game'));
const username = localStorage.getItem('username');
const isFirstPlayer = localStorage.getItem('username') === gameData.userNameOne;
const SIZE = 4;
const DEFAULT_CLASSNAME = 'fa fa-question';

let stompClient;
let activeGameStep = null;

init();

async function init() {
    setupConnection();
    const guessedCells = await fetchGameCells();
    const gameState = await fetchGameState();
    console.log(gameState);
    activeGameStep = (gameState.lastStep.cellIdTwo !== null || gameState.lastStep.cellIdOne === null) ? null : gameState.lastStep;
    initScoreBoardNames();
    setScores(gameState);
    createMap(guessedCells);
    addChatActivity();
    manageGuessingAcces(gameState);
}

function setupConnection() {
	
	const gameId = gameData.id;
	let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
    	stompClient.subscribe(`/topic/game/step/${gameData.id}`, gameRound);
    	stompClient.subscribe(`/topic/game/chat/${gameData.id}`, displayChatMessage);
    }, () => console.log("Connection error"));
	
}

function fetchGameCells() {
	return data_handler._api_get_no_callback(`/game/cells/${gameData.id}`);
}

function fetchGameState() {
	return data_handler._api_get_no_callback(`/game/state/${gameData.id}`);
}

function initScoreBoardNames() {
	let opponent = gameData.userNameOne === username ? gameData.userNameTwo : gameData.userNameOne;
	document.querySelector('#player-one-name').innerHTML = username;
	document.querySelector('#player-two-name').innerHTML = opponent;
}

function createMap(guessedCells) {
	createEmptyTable();
	revealGuessedCells(guessedCells);
}

function createEmptyTable() {
	const gameField = document.querySelector('.game-field');
	let gameTable = document.createElement('table');
	let tableContent = `<tr>`
		for (let cell = 1; cell <=SIZE**2; cell++) {
			tableContent += `<td id="cell-${cell}" class="active"><i class="fa fa-question" aria-hidden="true"></i></td>`
				if (cell % SIZE === 0 && cell !== SIZE**2) {
					tableContent += `</tr><tr>`
				}
		}
	tableContent += `</tr>`
		gameTable.innerHTML = tableContent;
	gameField.appendChild(gameTable);
}

function revealGuessedCells(guessedCells) {
	for (let cell of guessedCells) {
		let cellElement = document.querySelector(`#cell-${cell.cellId} i`);
		cellElement.className = `fa ${cell.revealedClass}`;
		cellElement.parentElement.classList.remove('active');
	}
}

function addChatActivity() {
	document.querySelector('#chat-button').addEventListener('click', sendMessage)
}


function addShowingFunctionality() {
	const cells = [...document.querySelectorAll('.active')];
	cells.forEach(cell => cell.addEventListener('click', sendGameStep))
}

function removeShowingFunctionality() {
	const cells = [...document.querySelectorAll('.active')];
	cells.forEach(cell => cell.removeEventListener('click', sendGameStep));
}

function sendGameStep() {
	this.removeEventListener('click', sendGameStep);
	const cellId = convertElementIdToCellId(this.id);
	if (activeGameStep === null) {
		activeGameStep = {gameId: gameData.id, cellIdOne: cellId, cellIdTwo: null};
	} else {
		activeGameStep.cellIdTwo = cellId;
	}
	stompClient.send(`/app/game/step/${gameData.id}`, {}, JSON.stringify(activeGameStep));
}

function convertElementIdToCellId(elementId) {
	return parseInt(elementId.split('-')[1]);
}

function sendMessage() {
	const messageContent = document.querySelector('#chat-input').value;
	const messageObj = {username: username, message: messageContent};
	stompClient.send(`/app/game/chat/${gameData.id}`, {}, JSON.stringify(messageObj));
}

function gameRound(data) {
	const gameState = JSON.parse(data.body);
	if (gameState.lastStep.cellIdTwo === null) {
		revealCell(gameState.lastStep.cellIdOne, gameState.lastStep.classOne);
		activeGameStep = gameState.lastStep;
	} else {
		revealCell(gameState.lastStep.cellIdTwo, gameState.lastStep.classTwo);
		activeGameStep = null;
		
		if (gameState.lastStep.classOne !== gameState.lastStep.classTwo) {
			showAndHide(gameState);	
		} else {
			showPermanently(gameState.lastStep);
			manageGuessingAcces(gameState);
			setScores(gameState);
		}
	}
}

function revealCell(cellId, className) {
	const cell = document.querySelector(`#cell-${cellId}`);
	const icon = cell.querySelector('i');
	icon.classList.remove('fa-question');
	icon.classList.add(className);
}

function showAndHide(gameState) {
	let cellOne = document.getElementById(`cell-${gameState.lastStep.cellIdOne}`);
	let cellTwo = document.getElementById(`cell-${gameState.lastStep.cellIdTwo}`);
	
	removeShowingFunctionality();
	
	setTimeout(() => {
		cellOne.querySelector('i').className = DEFAULT_CLASSNAME;
		cellTwo.querySelector('i').className = DEFAULT_CLASSNAME;
		manageGuessingAcces(gameState);
	}, 2000);
}

function showPermanently(lastStep) {
	let cellOne = document.getElementById(`cell-${lastStep.cellIdOne}`);
	cellOne.classList.remove('active');
	let cellTwo = document.getElementById(`cell-${lastStep.cellIdTwo}`);
	cellTwo.classList.remove('active');
	
}

function manageGuessingAcces(gameState) {
	if (playerCanGuess(gameState)) {
		addShowingFunctionality();
	} else {
		removeShowingFunctionality();
	}
}

function playerCanGuess(gameState) {
	return (isFirstPlayer && gameState.onRound === 'FIRST') 
			|| (!isFirstPlayer && gameState.onRound === 'SECOND');
}

function setScores(gameState) {
	document.querySelector('#player-one-score').innerHTML = isFirstPlayer ? gameState.firstPlayerPoints.toString() : gameState.secondPlayerPoints.toString();
	document.querySelector('#player-two-score').innerHTML = isFirstPlayer ? gameState.secondPlayerPoints.toString() : gameState.firstPlayerPoints.toString();
}

function displayChatMessage(data) {
	
	const message = JSON.parse(data.body);
	const messageElement = createMessageElement(message);
	const messageBoard = document.querySelector('.message-board');
	messageBoard.appendChild(messageElement);
	
	document.querySelector('#chat-input').value = '';

}

function createMessageElement(messageObj) {
	const element = document.createElement('div');
	element.innerHTML = `<b>${messageObj.username}</b>: ${messageObj.message}`;
	return element;
}