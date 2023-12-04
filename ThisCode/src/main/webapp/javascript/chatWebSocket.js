/**
*chatpage.jspのWebSocketに関するjavascript
*/
let defaultSrc = 'default';

let nowRoomId = null;
let nowRoomHostId = null;
let rooms = null;
let roomsMap = null;
let nowChannelId = null;
let channelsMap = null;
let voiceChannelsMap = null;
let chatDiv = null;
let chatSocket = null;
let noticeSocket = null;
let username = null;
let userid = null;
let userinfo = null;
let user_icon = null;

const ip = 'localhost';

document.addEventListener("DOMContentLoaded", init);

//ログインしたらデフォルトで一番上のサーバーを表示する
async function init() {
	await getUserInfo();

	const firstServer = roomsMap.entries().next().value;
	const firstServerId = firstServer[0];
	registerNotice();
	await joinRoom(firstServerId);
}

//サーバーからユーザーデータを取得する関数
async function getUserInfo() {
	try {
		const response = await fetch("/ThisCord/fn/getuserinfo");

		if (response.ok) {

			userinfo = await response.json();
			username = userinfo.user_name;
			userid = userinfo.user_id;
			user_icon = userinfo.user_icon;
			rooms = userinfo.servers;
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
		'<div class="user-box">' +
		'<img class="user_icon_img" src="resource/user_icons/' + user_icon + '"></img>' +
		'<div style="line-height: 17px; padding:4px 0px 4px 8px;">' +
		'<p id="user-name">' + userinfo.user_name + '</p>' +
		'<p id="user-id">' + userinfo.user_name + '-' + userid + '</p>' +
		'</div>' +
		'</div>';
}
//通知サーバーのコネクションの初期化
function registerNotice() {
	noticeSocket = new WebSocket("ws://localhost:8080/ThisCord/notice/" + userid + "/" + username + "/" + user_icon);

	noticeSocket.onopen = event => {
		console.log("接続開始");
	};

	noticeSocket.onmessage = event => {
		let json = JSON.parse(event.data);
		let member = json.members;
		let vcId = json.voiceChannelid;
		createVoiceChannelIcon(member, vcId);
	};

	noticeSocket.onclose = event => {
		console.log("通知サーバー切断 :", event);
	};
}

//ボイスチャンネルに参加してるユーザーの表示をする関数
function createVoiceChannelIcon(members, channelId) {
	const videoChannelElement = document.getElementById('channelMember-' + channelId);
	console.log(videoChannelElement);
	videoChannelElement.innerHTML = "";
	for (let member of members) {

		//videoChannelElement.innerHTML +='<div>'+ member.user +'</div>';
		videoChannelElement.innerHTML +=
			'<div class="voice-channel-member">' +
			'<img class="voice-channel-icon" src="resource/user_icons/' + member.icon + '">' +
			'<div class="voice-channel-user">' + member.user + '</div>' +
			'</div>';
	}
}

//jspのほかの切断ボタンから通話を切るための関数
function closeVoiceChannel() {
	joinVoiceChannel(nowVcId, nowUser, nowIcon);
}

//通知サーバーにボイスチャンネルに参加したことを通知する
let joinVoiceFlag = false;
let nowVcId = null;
let nowUser = null;
let nowIcon = null;
function joinVoiceChannel(channelId, user, icon) {
	//共通の通話終了ボタンからチャンネル切断できるように変数を保存する
	if (joinVoiceFlag) {
		sendDisconnectVoiceChannel(nowVcId, user);
		
		window.multi.hangUp();
		window.multi.stopVideo();
		window.globalFunction.videoChat();
		
		if (channelId === nowVcId) {
			joinVoiceFlag = false;
			nowVcId = null;
		} else {
			sendJoinVoiceChannel(channelId, user, icon);
			joinVoiceFlag = true;
			nowVcId = channelId;
			
			window.globalFunction.videoChat();
			window.multi.connect();
		}

	} else {
		sendJoinVoiceChannel(channelId, user, icon);
		console.log(window.globalFunction);
		joinVoiceFlag = true;
		nowVcId = channelId;
		
		window.globalFunction.videoChat();
		window.multi.connect();

		console.log("チャンネルに参加 nowVcChannelId:" + nowVcId + ":" + channelId, ':', user, ':', icon);
	}
}

//通知サーバーにボイスチャンネルに参加したことを通知するJSON
function sendJoinVoiceChannel(voiceChannelId, user, icon) {
	let json =
	{
		type: 'joinVoiceChannel',
		serverId: nowChannelId,
		voiceChannelid: voiceChannelId,
		user: user,
		icon: icon
	};
	noticeSocket.send(JSON.stringify(json));
}

//通知サーバーにボイスチャンネルから切断したことを通知するJSON
function sendDisconnectVoiceChannel(voiceChannelId, user) {
	let json =
	{
		type: 'disconnectVoiceChannel',
		serverId: nowChannelId,
		voiceChannelid: voiceChannelId,
		user: user,
	};
	noticeSocket.send(JSON.stringify(json));
}

async function getMessageInfo(channel_id) {
	try {
		const response = await fetch("/ThisCord/fn/getmessageinfo?channel_id=" + channel_id);

		if (response.ok) {
			const jmessage = await response.json();
			const messages = new Map(Object.entries(jmessage));

			const chat = document.getElementById("message-container");

			chat.innerHTML = "";

			for (const [key, message] of messages) {
				console.log(message.message);
				chat.innerHTML += '<div class="message-wrapper">'+
					    '<div>'+
					        '<img class=" chat-icon" src="resource/user_icons/'+message.user_icon+'" >'+
					    '</div>'+
					    
					    '<div class="wrapper-item">'+
					        '<span class="message-user-name">'+message.user_name +'</span>'+
					        '<span class="message-date">'+message.send_date+'</span>'+
					        '<p class="message-text">'+message.message+'</p>'+
					    '</div>'+
					'</div>';
			}
		} else {
			console.error("Failed to fetch message information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
}



//サーバーのボタンを生成する関数
async function createRoomB(roomInfo) {
	const roomListDiv = document.getElementById("room-list");
	roomListDiv.innerHTML = "";
	for (const [roomId, roomName] of roomInfo) {
		console.log(roomName[1]);
		const src = roomName[1];
		if (src === defaultSrc) {
			roomListDiv.innerHTML += '<div class="server-list-item"><a class="server-icon" id="server-id-' + roomId + '" onclick=" joinRoom(\'' + roomId + '\');"  ><div class="server-name">' + roomName[0] + '</div></a></div>';
		} else {
			roomListDiv.innerHTML += '<div class="server-list-item"><a class="server-icon" id="server-id-' + roomId + '" onclick=" joinRoom(\'' + roomId + '\');"  ><img id="retryImage" src="' + src + '" onerror="retryImageLoad(this, 10, 1000)"></img></a></div>';
		}

	}
}

// テキストチャンネルのボタンを生成する関数
function createChannelButton(channelInfo) {
	const channelsListDiv = document.getElementById("channels-list");
	channelsListDiv.innerHTML = "";

	for (const [channel_id, channel_name] of channelInfo) {

		channelsListDiv.innerHTML += '<div class="text-channels" id="channel-id-' + channel_id + '"><a href="javascript:joinChannel(\'' + channel_id + '\')"><i class="fa-solid fa-hashtag fa-sm mx-r-5" style="margin-right: 5px;"></i> ' + channel_name + '</a></div>';
	}
}
//ボイスチャンネルのボタンを生成する関数
function createVoiceChannelButton(channelInfo) {
	const channelsListDiv = document.getElementById("voice-channels-list");
	channelsListDiv.innerHTML = "";

	for (const [channel_id, channel_name] of channelInfo) {
		channelsListDiv.innerHTML += '<div class="text-channels" id="channel-id-' + channel_id + '"><a onclick="joinVoiceChannel(\'' + channel_id + '\', \'' + username + '\',\'' + user_icon + '\')"><i class="fa-solid fa-volume-low fa-sm" style="margin-right: 5px;"></i> ' + channel_name + '</a></div><div id="channelMember-' + channel_id + '"></div>';
	}
}

 function joinChannel(channel_id) {
     if (chatSocket) {
         chatSocket.close();
     }
     const infoDiv = document.querySelector("#channel");
     infoDiv.innerHTML = channelsMap.get(channel_id);
 
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
         //chat.innerHTML += '<img src="'+rep.usericon+'" >'+rep.username + " " + rep.date + "<br>" + rep.message + "<br><br>";
     	chat.innerHTML +=
		'<div class="message-wrapper">'+
		    '<div>'+
		        '<img class=" chat-icon" src="resource/user_icons/'+rep.usericon+'" >'+
		    '</div>'+
		    
		    '<div class="wrapper-item">'+
		        '<span class="message-user-name">'+rep.username +'</span>'+
		        '<span class="message-date">'+rep.date+'</span>'+
		        '<p class="message-text">'+rep.message+'</p>'+
		    '</div>'+
		'</div>';
     
     };
 
     chatSocket.onclose = event => {
         console.log("切断");
     };
     const currentElemnt = document.querySelector('#channel-id-'+channel_id);
     window.globalFunction.toggleChannelState(currentElemnt);
 }

//サーバーに参加する関数
async function joinRoom(roomId) {
	if (chatSocket) {
		chatSocket.close();
	}
	fieldClear();

	nowRoomId = roomId;
	console.log(roomsMap.get(roomId));
	await getServerInfo(nowRoomId);

	const infoDiv = document.querySelector("#server");
	infoDiv.innerHTML = roomsMap.get(roomId)[0];
	createChannelButton(channelsMap);
	createVoiceChannelButton(voiceChannelsMap);

	const firstTextChannel = channelsMap.entries().next().value;
	const firstTextChannelId = firstTextChannel[0];

	let json =
	{
		type: 'joinServer',
		serverId: roomId,
		user: username,
		icon: user_icon
	};
	noticeSocket.send(JSON.stringify(json));
	console.log(JSON.stringify(json));
	joinChannel(firstTextChannelId);
}

//サーバーの情報を取得する関数
async function getServerInfo(roomId) {
	try {
		const response = await fetch("/ThisCord/fn/getserverinfo?roomId=" + roomId);
		if (response.ok) {

			roominfo = await response.json();
			const members = roominfo.member;
			nowRoomHostId = roominfo.host_id;
			membersMap = new Map(Object.entries(members));

			//メンバー一覧に表示する処理
			for (const [user_id, user_name] of membersMap) {
				const memberListDiv = document.getElementById("members-list");

				if (nowRoomHostId == user_id) {
					memberListDiv.innerHTML +=
						'<div class="member-wrapper">' +
						'<img class="member-icon" src="resource/user_icons/' + user_name[1] + '"></img>' +
						'<span class="member-name">' + user_name[0] + '</span>' +
						'<i class="server-host fa-solid fa-crown fa-xs"></i>' +
						'</div>';
				} else {
					memberListDiv.innerHTML +=
						'<div class="member-wrapper">' +
						'<img class="member-icon" src="resource/user_icons/' + user_name[1] + '"></img>' +
						'<span class="member-name">' + user_name[0] + '</span>' +
						'</div>';
				}
			}
			const channels = roominfo.channels;
			const voice_channels = roominfo.voice_channels;
			channelsMap = new Map(Object.entries(channels));
			voiceChannelsMap = new Map(Object.entries(voice_channels))
			createRoomB(roomsMap);
			createChannelButton(channelsMap);
			createVoiceChannelButton(voiceChannelsMap);
			console.log(roominfo);
		} else {
			console.error("Failed to fetch room information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
	const currentElement = document.querySelector('#server-id-' + nowRoomId);
	window.globalFunction.toggleClickedState(currentElement);
}

function sendMessage() {
	const messageInput = document.getElementById("message-input");
	const message = messageInput.value;
	let json =
	{
		nowRoomId: nowRoomId,
		nowRoomName: roomsMap.get(nowRoomId),
		nowChannelId: nowChannelId,
		nowChannelName: channelsMap.get(nowChannelId),
		username: userinfo.user_name,
		usericon: user_icon,
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


//画像を非同期でとってくる
function retryImageLoad(imgElement, maxRetries, retryInterval) {
	let retries = 0;

	function loadImage() {
		imgElement.onload = function() {
			// 画像の読み込みが成功した場合
			console.log('Image loaded successfully.');
		};

		imgElement.onerror = function() {
			// 画像の読み込みがエラーの場合
			if (retries < maxRetries) {
				retries++;
				console.log(`Retrying image load, attempt ${retries}`);
				setTimeout(loadImage, retryInterval);
			} else {
				console.error(`Failed to load image after ${maxRetries} attempts.`);
			}
		};

		// 画像の読み込みを開始
		imgElement.src = imgElement.src;
	}

	// 初回の画像読み込みを開始
	loadImage();
}