/**
 * すまほよう
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

const ip = constIp;
const port = constPort;

document.addEventListener("DOMContentLoaded", init);
function disableScroll(event) {
	event.preventDefault();
}
async function init() {
	await getUserInfo();

	const firstServer = roomsMap.entries().next().value;
	let firstServerId = null;

	if (firstServer != null) {
		firstServerId = firstServer[0];
		registerNotice();
		await joinRoom(firstServerId);
	} else {
		registerNotice();
	}
	setSwipe(".fieldWrapper");
	initform();
	getFriend('invFriendList');

}

//スワイプイベントの設定
function setSwipe(elem) {
	let t = document.querySelector(elem);
	let startX;
	let startY;
	let moveX;
	let moveY;
	let dist = 50;
	let oldX = 0;
	let oldY = 0;

	t.addEventListener("touchstart", function (e) {
		e.preventDefault();
		oldX = startX = e.touches[0].pageX;
		oldY = startY = e.touches[0].pageY;
	});

	t.addEventListener("touchmove", function (e) {

		moveX = e.changedTouches[0].pageX;
		moveY = e.changedTouches[0].pageY;

		var chatScrollElement = document.getElementById("chat-scroll");
		let sum = chatScrollElement.scrollTop + (oldY - moveY);
		if (sum < 0) {
			chatScrollElement.scrollTop = 0;
		} else if (sum > chatScrollElement.scrollHeight - chatScrollElement.clientHeight) {
			chatScrollElement.scrollTop = chatScrollElement.scrollHeight - chatScrollElement.clientHeight;
		} else {
			chatScrollElement.scrollTop = sum;
		}

		oldX = moveX;
		oldY = moveY;
	});

	t.addEventListener("touchend", function (e) {
		let footer = document.getElementById("footer");
		if (startX > moveX && startX > moveX + dist) {
			document.querySelector(".chatField").classList.add("active");
			footer.classList.remove("open-footer");
		}
		if (startX < moveX && startX + dist < moveX) {
			document.querySelector(".chatField").classList.remove("active");
			footer.classList.add("open-footer");
		}
	});
}

function toggleFooter() {
	let footer = document.getElementById("footer");
	if (footer.classList.contains("notActive")) {
		footer.classList.remove("open-footer");
	} else {
		footer.classList.add("open-footer");
	}
}

function toggleChatField() {
	document.querySelector(".chatField").classList.toggle("active");
	document.getElementById("footer").classList.toggle('open-footer');
}

//フレンド追加モーダルにフレンドリストを表示する関数
async function getFriend(element) {
	const elm = document.getElementById(element);

	try {
		const response = await fetch('/ThisCord/fn/getfriendList?userId=' + userid);
		if (response.ok) {
			const json = await response.json();

			for (let friend of json.friendList) {
				elm.innerHTML +=
					`<div class="fland-box">
					  <div class="inv-friend-button">
					    <div class="icon-name-wrapper">
					      <img class="fland_icon_img" src="/ThisCord/resource/user_icons/${friend.user_icon}" />
					      <div style="line-height: 17px; padding:4px 0px 4px 8px;">
					        <p id="user-name">${friend.user_name}</p>
					        <p id="user-id">${friend.user_name}-${friend.user_id}</p>
					      </div>
						  <button class="inv-button" value="招待" onclick="invFriendForm(${friend.user_id})">
							<p>招待</p>
						</button>
					    </div>
					  </div>
					</div>`;
			}

		} else {
			console.error("Failed to fetch room information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
}

// クエリ文字列からidパラメータの値を取得する関数
var queryString = window.location.search;

function getIdFromQueryString(name) {
	var urlParams = new URLSearchParams(queryString);
	return urlParams.get(name);
}

async function getUserInfo() {
	try {
		const response = await fetch("/ThisCord/fn/getuserinfo?id=" + getIdFromQueryString('id'));

		if (response.ok) {

			userinfo = await response.json();
			username = userinfo.user_name;
			userid = userinfo.user_id;
			user_icon = userinfo.user_icon;
			rooms = userinfo.servers;
			roomsMap = new Map(Object.entries(rooms));
			createRoomB(roomsMap);
		} else {
			console.error("Failed to fetch room information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
}

async function createRoomB(roomInfo) {
	const roomListDiv = document.getElementById("room-list");
	roomListDiv.innerHTML = "";
	for (const [roomId, roomName] of roomInfo) {
		const src = roomName[1];
		if (src === defaultSrc) {
			roomListDiv.innerHTML += '<div class="server-list-item noSwipe"><a class="server-icon" id="server-id-' + roomId + '" onclick=" joinRoom(\'' + roomId + '\');"  ><div class="server-name">' + roomName[0] + '</div></a></div>';
		} else {
			roomListDiv.innerHTML += '<div class="server-list-item noSwipe"><a class="server-icon" id="server-id-' + roomId + '" onclick=" joinRoom(\'' + roomId + '\');"  ><img id="retryImage" src="/ThisCord/' + src + '" onerror="retryImageLoad(this, 10, 1000)"></img></a></div>';
		}

	}
}

function createChannelButton(channelInfo) {
	const channelsListDiv = document.getElementById("channels-list");
	channelsListDiv.innerHTML = "";

	for (const [channel_id, channel_name] of channelInfo) {
		channelsListDiv.innerHTML +=
			'<div class="text-channels noSwipe" id="channel-id-' + channel_id + '">' +
			'<a class="textIcon" href="javascript:joinChannel(\'' + channel_id + '\')">' +
			'<i class="fa-solid fa-hashtag fa-sm mx-r-5" style="margin-right: 5px;"></i>' +
			channel_name +
			'</a>' +
			'<a class="invitationIcon" onclick="modalToggle(\'invitation-modal\')"><i class="fa-solid fa-user-plus fa-xs"></i></a>	' +
			'</div>';
	}
}


//サーバーに参加する関数
async function joinRoom(roomId) {
	if (chatSocket) {
		chatSocket.close();
	}
	fieldClear();

	nowRoomId = roomId;
	await getServerInfo(nowRoomId);

	const infoDiv = document.querySelector("#server");
	infoDiv.innerHTML = roomsMap.get(roomId)[0];
	createChannelButton(channelsMap);
	createVoiceChannelButton(voiceChannelsMap);
	const firstTextChannel = channelsMap.entries().next().value;
	const firstTextChannelId = firstTextChannel[0];

	let voiceIds = [];
	for(const [channel_id, channel_name] of voiceChannelsMap){
		voiceIds.push(channel_id);
	}

	let json =
	{
		type: 'joinServer',
		serverId: roomId,
		user: username,
		icon: user_icon,
		channels: voiceIds
	};
	noticeSocket.send(JSON.stringify(json));
	joinChannel(firstTextChannelId);
	toggleHome();
	initform();
}

function joinChannel(channel_id) {
	if (chatSocket) {
		chatSocket.close();
	}
	const infoDiv = document.querySelector("#channel");
	infoDiv.innerHTML = channelsMap.get(channel_id);

	nowChannelId = channel_id;
	chatSocket = new WebSocket(`ws://${ip}:${port}/ThisCord/chat/${nowRoomId}/${nowChannelId}/${userinfo.user_id}`);
	chatSocket.onopen = event => {
		console.log("接続開始");
		getMessageInfo(channel_id);
	};

	chatSocket.onmessage = event => {
		const chat = document.getElementById("message-container");
		const rep = JSON.parse(event.data);
		//chat.innerHTML += '<img src="'+rep.usericon+'" >'+rep.username + " " + rep.date + "<br>" + rep.message + "<br><br>";
		chat.innerHTML +=
			'<div class="message-wrapper">' +
			'<div>' +
			'<img class=" chat-icon" src="/ThisCord/resource/user_icons/' + rep.usericon + '" >' +
			'</div>' +

			'<div class="wrapper-item">' +
			'<span class="message-user-name">' + rep.username + '</span>' +
			'<span class="message-date">' + rep.date + '</span>' +
			'<p class="message-text">' + rep.message + '</p>' +
			'</div>' +
			'</div>';
		scrollEnd(500);

	};

	chatSocket.onclose = event => {
		console.log("切断");
	};
	const currentElemnt = document.querySelector('#channel-id-' + channel_id);
	window.globalFunction.toggleChannelState(currentElemnt);
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
						'<img class="member-icon" src="/ThisCord/resource/user_icons/' + user_name[1] + '"></img>' +
						'<span class="member-name">' + user_name[0] + '</span>' +
						'<i class="server-host fa-solid fa-crown fa-xs"></i>' +
						'</div>';
				} else {
					memberListDiv.innerHTML +=
						'<div class="member-wrapper">' +
						'<img class="member-icon" src="/ThisCord/resource/user_icons/' + user_name[1] + '"></img>' +
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
		} else {
			console.error("Failed to fetch room information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
	const currentElement = document.querySelector('#server-id-' + nowRoomId);
	window.globalFunction.toggleClickedState(currentElement);
}

//ボイスチャンネルのボタンを生成する関数
function createVoiceChannelButton(channelInfo) {
	const channelsListDiv = document.getElementById("voice-channels-list");
	channelsListDiv.innerHTML = "";

	for (const [channel_id, channel_name] of channelInfo) {
		channelsListDiv.innerHTML += '<div class="text-channels" id="channel-id-' + channel_id + '"><a class="voice-channel-linc" onclick="modalToggle(\'video_modal\'); joinVoiceChannel(\'' + channel_id + '\', \'' + username + '\',\'' + user_icon + '\')"><i class="fa-solid fa-volume-low fa-sm" style="margin-right: 5px;"></i> ' + channel_name + '</a></div><div id="channelMember-' + channel_id + '"></div>';
	}
}

//メッセージ送信関数
function sendMessage() {
	const messageInput = document.getElementById("message-input");
	const message = messageInput.value;
	if (message) {	//メッセージが空の場合にEnterを押しても処理されなくなる
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

function fieldClear() {
	const chat = document.getElementById("message-container");
	const memberListDiv = document.getElementById("members-list");
	const channelsListDiv = document.getElementById("channels-list");
	chat.innerHTML = "";
	memberListDiv.innerHTML = "";
	channelsListDiv.innerHTML = "";
}

function registerNotice() {
	noticeSocket = new WebSocket(`ws://${ip}:${port}/ThisCord/notice/${userid}/${username}/${user_icon}`);

	noticeSocket.onopen = event => {
		console.log("接続開始");
	};

	noticeSocket.onmessage = event => {
		let json = JSON.parse(event.data);
		let member = json.members;
		let vcId = json.voiceChannelid;
		let type = json.type;

		if(type == 'invite'){
			alert('招待されました');
			getUserInfo();	
		}
		console.log(JSON.stringify(json));
		createVoiceChannelIcon(member, vcId);
	};

	noticeSocket.onclose = event => {
		console.log("通知サーバー切断 :", event);
	};
}

function createVoiceChannelIcon(members, channelId) {
	const videoChannelElement = document.getElementById('channelMember-' + channelId);
	videoChannelElement.innerHTML = "";
	for (let member of members) {

		//videoChannelElement.innerHTML +='<div>'+ member.user +'</div>';
		videoChannelElement.innerHTML +=
			'<div class="voice-channel-member">' +
			'<img class="voice-channel-icon" src="/ThisCord/resource/user_icons/' + member.icon + '">' +
			'<div class="voice-channel-user">' + member.user + '</div>' +
			'</div>';
	}
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
				chat.innerHTML +=
					'<div class="message-wrapper">' +
					'<div>' +
					'<img class=" chat-icon" src="/ThisCord/resource/user_icons/' + message.user_icon + '" >' +
					'</div>' +

					'<div class="wrapper-item">' +
					'<span class="message-user-name">' + message.user_name + '</span>' +
					'<span class="message-date">' + message.send_date + '</span>' +
					'<p class="message-text">' + message.message + '</p>' +
					'</div>' +
					'</div>';
			}

			scrollEndfast()
		} else {
			console.error("Failed to fetch message information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
}

function toggleHome() {
	channelsWrapper.classList.remove('none');
	//serverHeaderWrapper.classList.remove('none');
	//inputField.classList.remove('none');
	//memberListWrapper.classList.remove('none');
	//messageContainer.classList.remove('none');
	//flendListWrapper.classList.add('none');
	//homeNav.classList.add('none');
	//friendFormWarper.classList.add('none');
	//friendListWarpe.classList.add('none');

	//homeChannelFlandList.classList.add('none');
	//homeInfoList.classList.add('none');
	//homeContainerFluid.style.gridTemplateColumns = "72px 240px calc(100% - 552px) 240px";
	//homeContainerFluid.classList.remove('homepageGrid');

	joinHomeFlag = false;
}

//自動スクロール
const main = document.getElementById('chat-scroll');
function scrollEndfast() {
	main.scrollTop = main.scrollHeight;
	scrollEndfast();
}

function scrollEnd(duration) {
	var scrollHeight = main.scrollHeight;
	var startPosition = main.scrollTop;
	var startTime = performance.now();

	function scrollAnimation(currentTime) {
		var elapsed = currentTime - startTime;
		var progress = elapsed / duration;

		main.scrollTop = startPosition + progress * (scrollHeight - startPosition);

		if (progress < 1) {
			requestAnimationFrame(scrollAnimation);
		}
	}

	requestAnimationFrame(scrollAnimation);
}

function closeVoiceChannel() {
	joinVoiceChannel(nowVcId, nowUser, nowIcon);
}

const homeContainerFluid = document.getElementById('container-fluid');
let joinVoiceFlag = false;
let nowVcId = null;
let nowUser = null;
let nowIcon = null;

function closeVoiceChannel() {
  joinVoiceChannel(nowVcId, nowUser, nowIcon);
  sendDisconnectVoiceChannel(nowVcId, nowUser);

  window.multi.stopVideo();
  window.globalFunction.videoChat();
  window.multi.hangUp();

  joinVoiceFlag = false;
  nowUser = null;
  nowVcId = null;
  nowIcon = null;
}

function joinVoiceChannel(channelId, user, icon) {
  if (!joinVoiceFlag) { // 既に参加している場合は切断

    sendJoinVoiceChannel(channelId, user, icon);
    joinVoiceFlag = true;
    nowVcId = channelId;

    window.globalFunction.videoChat();
    window.multi.connect(channelId);

    console.log("チャンネルに参加 nowVcChannelId:" + nowVcId + ":" + channelId, ':', user, ':', icon);
  }
}

//通知サーバーにボイスチャンネルに参加したことを通知するJSON
async function sendJoinVoiceChannel(voiceChannelId, user, icon) {
	let json =
	{
		type: 'joinVoiceChannel',
		serverId: nowRoomId,
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
		serverId: nowRoomId,
		voiceChannelid: voiceChannelId,
		user: user,
	};
	noticeSocket.send(JSON.stringify(json));
}

function invFriendForm(id){
	const invitationInput = document.getElementById('invitationInput');
	invitationInput.value = id;
}

function retryImageLoad(imgElement, maxRetries, retryInterval) {
	let retries = 0;

	function loadImage() {
		imgElement.onload = function() {
			console.log('Image loaded successfully.');
		};

		imgElement.onerror = function() {
			if (retries < maxRetries) {
				retries++;

				setTimeout(loadImage, retryInterval);
			}
		};
		imgElement.src = imgElement.src;
	}
	loadImage();
}


function initform() {
	const serverNameIn = document.getElementById('server_name');
	console.log(username);
	if (serverNameIn.value.length == 0) {
		console.log(serverNameIn.value);

		serverNameIn.value = username + 'のサーバー';
	} else {
		console.log(serverNameIn.value);
	}

	const inputServerId = document.getElementById('inputServerId');
	inputServerId.value = nowRoomId;
	console.log(nowRoomId);

	const MakeServerUserId = document.getElementById('MakeServerUserId');
	MakeServerUserId.value = userid;
	

	invFriendList
	getFriend('invFriendList');
	getFriend('singleChatFriendList');
	
	const formUserId = document.getElementById('formUserId');
	formUserId.value = userid;


}

function form_clear(formId) {
	var form = document.getElementById(formId);
	var submitButton = document.getElementById('form_submit');

	form.setAttribute('data-submitting', 'true');
	form.submit();

}


//
document.addEventListener('DOMContentLoaded', function() {
	const inviteForm = document.getElementById('inviteForm');

	inviteForm.addEventListener('submit', (event) => {
		event.preventDefault();
		
		const inviteUserId = document.getElementById('invitationInput').value;
		let json =
		{
			type: 'invite',
			serverId: nowRoomId,
			inviteUserId: inviteUserId
		};
	
		console.log(json);
		noticeSocket.send(JSON.stringify(json));
	
		inviteForm.submit();
	})

});
