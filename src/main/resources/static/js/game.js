import {data_handler} from "./data_handler.js";

const gameData = JSON.parse(localStorage.getItem('game'));
const username = localStorage.getItem('username');
const isFirstPlayer = localStorage.getItem('username') === gameData.userNameOne;
const SIZE = 4;
const DEFAULT_CLASSNAME = 'fa fa-question';

let stompClient;
let activeGameStep = null;

init();

function init() {
    setupConnection();
    initScoreBoard();
    createMap();
    addChatActivity();
    addModalActivity();
    startGame();
// socket.addEventListener('first-guess', showOthersFirstIcon)
// socket.addEventListener('second-guess', endOthersRound)
// socket.addEventListener('ask-new-game', displayNewGameTick)
// socket.addEventListener('replay-game', replayGame)
// window.addEventListener('win', endGame)
    // showModal('win')
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

function initScoreBoard() {
	let opponent = gameData.userNameOne === username ? gameData.userNameTwo : gameData.userNameOne;
	document.querySelector('#player-one-name').innerHTML = username;
	document.querySelector('#player-two-name').innerHTML = opponent;
}

function createMap() {
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

function addChatActivity() {
	document.querySelector('#chat-button').addEventListener('click', sendMessage)
}

function startGame() {
	if (username === gameData.userNameOne) {
		addShowingFunctionality()
	}
}

function addShowingFunctionality() {
	const cells = [...document.querySelectorAll('.active')];
	cells.forEach(cell => cell.addEventListener('click', sendGameStep))
}

function removeShowingFunctionality() {
	const cells = [...document.querySelectorAll('.active')];
	cells.forEach(cell => cell.removeEventListener('click', sendGameStep))
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
	// Send a chat message to the server
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
			updateScoreBar(gameState);
		}
	}
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

function updateScoreBar(gameState) {
	document.querySelector('#player-one-score').innerHTML = isFirstPlayer ? gameState.firstPlayerPoints.toString() : gameState.secondPlayerPoints.toString();
	document.querySelector('#player-two-score').innerHTML = isFirstPlayer ? gameState.secondPlayerPoints.toString() : gameState.firstPlayerPoints.toString();
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

function revealCell(cellId, className) {
	const cell = document.querySelector(`#cell-${cellId}`);
	const icon = cell.querySelector('i');
    icon.classList.remove('fa-question');
    icon.classList.add(className);
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


function showIcon() {
    localStorage.setItem('rounds', (parseInt(localStorage.getItem('rounds')) + 1).toString())
    localStorage.setItem(`guess-${parseInt(localStorage.getItem('rounds')) % 2}`, this.id.split('-')[1])
    const icon = this.querySelector('i');
    icon.classList.remove('fa-question');
    icon.classList.add(this.dataset.solvedclass);
    let dataToServer = JSON.stringify({roomNumber: localStorage.getItem('room').toString(), cellNumber: this.id.split('-')[1].toString()})

    if (parseInt(localStorage.getItem('rounds')) % 2 === 0) {
        socket.emit('second-guess', dataToServer)
        removeShowingFunctionality()
        const guessOne = document.querySelector(`#cell-${localStorage.getItem('guess-0')}`)
        const guessTwo = document.querySelector(`#cell-${localStorage.getItem('guess-1')}`)
        setTimeout(function () {
            if (guessOne.querySelector('i').classList[1] !== guessTwo.querySelector('i').classList[1]) {
                hideIcon(guessOne);
                hideIcon(guessTwo);
            } else {
                guessOne.classList.remove('active')
                guessTwo.classList.remove('active')
                guessOne.classList.add('inactive')
                guessTwo.classList.add('inactive')
                let currentScore = document.querySelector(' #player-one-score').innerHTML;
                document.querySelector(' #player-one-score').innerHTML = (parseInt(currentScore) + 1).toString();
                checkForEndGame();
            }
        }, 2000)
    } else {
        this.removeEventListener('click', showIcon)
        socket.emit('first-guess', dataToServer)
    }
}

function showOthersFirstIcon(data) {
    const icon = document.querySelector(`#cell-${data} i`);
    icon.classList.remove('fa-question');
    icon.classList.add(icon.closest('td').dataset.solvedclass);
    localStorage.setItem('opponent-guess', data);
}

function endOthersRound(data) {
    const guessOne = document.querySelector(`#cell-${localStorage.getItem('opponent-guess')} i`);
    const guessTwo = document.querySelector(`#cell-${data} i`);
    guessTwo.classList.remove('fa-question');
    guessTwo.classList.add(guessTwo.closest('td').dataset.solvedclass);
    setTimeout(function () {
        if (guessOne.classList[1] !== guessTwo.classList[1]) {
            hideIcon(guessOne.closest('td'));
            hideIcon(guessTwo.closest('td'))
        } else {
            guessOne.closest('td').classList.remove('active');
            guessTwo.closest('td').classList.remove('active');
            guessOne.closest('td').classList.add('inactive');
            guessTwo.closest('td').classList.add('inactive');
            let currentScore = document.querySelector(' #player-two-score').innerHTML;
            document.querySelector(' #player-two-score').innerHTML = (parseInt(currentScore) + 1).toString();
            checkForEndGame();
        }
        addShowingFunctionality();
    }, 2000);
}

function checkForEndGame() {
    const activeCells = document.querySelectorAll('.active');
    if (activeCells.length === 0) {
        let winEvent = new CustomEvent('win', {bubbles: true, cancelable: true});
        window.dispatchEvent(winEvent);
    }
}

function hideIcon(cell) {
    const icon = cell.querySelector('i');
    icon.classList.remove(cell.dataset.solvedclass);
    icon.classList.add('fa-question');
}

function endGame() {
    const ownScore = parseInt(document.querySelector('#player-one-score').innerHTML);
    const opponentScore = parseInt(document.querySelector('#player-two-score').innerHTML);
    if (opponentScore < ownScore) {
        showModal('win');
    } else if (ownScore < opponentScore) {
        showModal('loose');
    } else {
        showModal('draw');
    }
}

function showModal(situation) {
    switch (situation) {
        case 'win':
            document.querySelector('.game-result').innerHTML = 'Congratulations! You won!';
            break
        case 'loose':
            document.querySelector('.game-result').innerHTML = 'Oooops, you lost...';
            break
        default:
            document.querySelector('.game-result').innerHTML = "End of tha game! It's a draw!";
    }
    document.querySelector('#player-one-decision-name').innerHTML = localStorage.getItem('username');
    document.querySelector('#player-two-decision-name').innerHTML = localStorage.getItem('opponent');
    $('#winModal').modal({backdrop: 'static', keyboard: false});
}

function addModalActivity() {
    document.querySelector('#new-game-button').addEventListener('click', startNewGame)
}

function startNewGame() {
    document.querySelector('#player-one-decision-content').innerHTML = `<i class="fa fa-check" aria-hidden="true"></i>`;
    socket.emit('ask-new-game', localStorage.getItem('room'));
}

function displayNewGameTick() {
    const tickElement = `<i class="fa fa-check" aria-hidden="true"></i>`
    document.querySelector('#player-two-decision-content').innerHTML = tickElement;
    if (document.querySelector('#player-one-decision-content').innerHTML === tickElement) {
        socket.emit('replay-game', localStorage.getItem('room'));
    }
}

function replayGame(map) {
    localStorage.setItem('map', map);
    window.location.replace('/game');
}
