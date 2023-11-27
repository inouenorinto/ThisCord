/**
 *chatpage.jspのWebSocketに関するjavascript
 */

let nowRoomId = null;
let rooms = null;
let roomsMap = null;
let nowChannelId = null;
let channelsMap = null;
let chatDiv = null;
let chatSocket = null;
let username = null;
let userinfo = null;
let user_icon = null;

async function getUserInfo() {
	try {
		const response = await fetch("/ThisCord/fn/getuserinfo");
		if (response.ok) {

			userinfo = await response.json();
			username = userinfo.user_name;
			user_icon = userinfo.user_icon;
			rooms = userinfo.rooms;
			roomsMap = new Map(Object.entries(rooms));
			createRoomB(roomsMap);
			console.log(userinfo);
		} else {
			console.error("Failed to fetch room information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
	const infoDiv = document.querySelector("#user");
	infoDiv.innerHTML =
		'<img class="user_icon_img" src="${pageContext.request.contextPath}' + user_icon + '"></img>'+
		userinfo.user_name + " ";
}

async function getServerInfo(roomId) {
	try {
		const response = await fetch("/ThisCord/fn/getserverinfo?roomId=" + roomId);
		if (response.ok) {

			roominfo = await response.json();
			const members = roominfo.member;
			membersMap = new Map(Object.entries(members));
			for (const [user_id, user_name] of membersMap) {
				const memberListDiv = document.getElementById("members-list");
				memberListDiv.innerHTML += user_name + '<dir>';
			}

			const channels = roominfo.channels;
			channelsMap = new Map(Object.entries(channels));

			createRoomB(roomsMap);
			createChannelButton(channelsMap);
			console.log(roominfo);
		} else {
			console.error("Failed to fetch room information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
}

async function getMessageInfo(channel_id) {
	try {
		const response = await fetch("/ThisCord/fn/getmessageinfo?channel_id=" + channel_id);
		
		if (response.ok) {
			const jmessage = await response.json();
			const messages = new ArrayList(Object.entries(jmessage));
			for (const message in messages) {
				const chat = document.getElementById("message-container");
				chat.innerHTML += message.username + " " + message.date + "<br>" + message.message + "<br><br>";
			}
			const channels = roominfo.channels;
			channelsMap = new Map(Object.entries(channels));
	
			createRoomB(roomsMap);
			createChannelButton(channelsMap);
			console.log(roominfo);
		} else {
			console.error("Failed to fetch room information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
}

function createRoomB(roomInfo) {
	const roomListDiv = document.getElementById("room-list");
	roomListDiv.innerHTML = "";

	for (const [roomId, roomName] of roomInfo) {
		console.log(roomName[1]);
		roomListDiv.innerHTML +=
			"<a href=\"javascript:joinRoom(" + roomId + ")\" data-bs-toggle=\"tooltip\" data-bs-title=\"" + roomName[0] + "\">" +
			"<img class=\"server_icon\" src=\"${pageContext.request.contextPath}\\" + roomName[1] + "\"></img></a><dir>";

	}
}

function createChannelButton(channelInfo) {
	const channelsListDiv = document.getElementById("channels-list");
	channelsListDiv.innerHTML = "";

	for (const [channel_id, channel_name] of channelInfo) {

		channelsListDiv.innerHTML += "<i class=\"fa-solid fa-hashtag fa-sm\"></i> <a href=\"javascript:joinChannel(" + channel_id + ")\">" + channel_name + "<\a><dir>";
	}
}

function joinChannel(channel_id) { 
	if (chatSocket) {
		chatSocket.close();
	}
	const infoDiv = document.querySelector("#channel");
	infoDiv.innerHTML = "チャンネル：" + channelsMap.get(channel_id) + " ";

	nowChannelId = channel_id;
	console.log("url: " + "ws://localhost:8080/ThisCord/chat/" + nowRoomId + "/" + nowChannelId + "/" + userinfo.user_id);
	chatSocket = new WebSocket("ws://localhost:8080/ThisCord/chat/" + nowRoomId + "/" + nowChannelId + "/" + userinfo.user_id);

	chatSocket.onopen = event => {
		console.log("接続開始");
		getMessageInfo(channel_id);
	};

	chatSocket.onmessage = event => {
		console.log("Received message: " + event.data);
		const chat = document.getElementById("message-container");
		const rep = JSON.parse(event.data);
		chat.innerHTML += rep.username + " " + rep.date + "<br>" + rep.message + "<br><br>";
	};

	chatSocket.onclose = event => {
		console.log("切断");
	};
}

async function joinRoom(roomId) {
	if (chatSocket) {
		chatSocket.close();
	}
	fieldClear();

	nowRoomId = roomId;
	getServerInfo(nowRoomId);

	const infoDiv = document.querySelector("#server");
	infoDiv.innerHTML = "サーバー：" + nowRoomId + " ";
	createChannelButton(channelsMap);
}

function sendMessage() {
	const messageInput = document.getElementById("message-input");
	const message = messageInput.value;
	let json =
	{
		nowRoomId: nowRoomId,
		nowRoomName: roomsMap.get(nowRoomId),
		nowChannelId: nowChannelId,
		nowChanneName: channelsMap.get(nowChannelId),
		username: userinfo.user_name,
		date: getDate(),
		message: message
	};

	chatSocket.send(JSON.stringify(json));
	messageInput.value = "";
}

function fieldClear() {
	const chat = document.getElementById("message-container");
	const memberListDiv = document.getElementById("members-list");
	const channelsListDiv = document.getElementById("channels-list");
	chat.innerHTML = "";
	memberListDiv.innerHTML = "";
	channelsListDiv.innerHTML = "";
}

function getDate() {
	var today = new Date();
	var year = today.getFullYear();
	var month = today.getMonth() + 1;
	var day = today.getDate();
	var hours = today.getHours();
	var minutes = today.getMinutes();

	return year + '/' + month + '/' + day + ' ' + hours + ':' + minutes;
}

function form_crea(formId) {
	var form = document.getElementById(formId);
	var submitButton = document.getElementById('form_submit');

	form.setAttribute('data-submitting', 'true');

	form.submit();
}

function handleKeyPress(event) {
	if (event.key === "Enter") {
		sendMessage();

	}
}

document.addEventListener("DOMContentLoaded", getUserInfo);
