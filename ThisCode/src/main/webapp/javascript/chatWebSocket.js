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

const ip = constIp;
const port = constPort;

document.addEventListener("DOMContentLoaded", init);


//ログインしたらデフォルトで一番上のサーバーを表示する
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

	initform();
}

// クエリ文字列からidパラメータの値を取得する関数
var queryString = window.location.search;

function getIdFromQueryString(name) {
	var urlParams = new URLSearchParams(queryString);
	return urlParams.get(name);
}
//サーバーからユーザーデータを取得する関数
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
			console.log('rooms-------------'+roomsMap);
		} else {
			console.error("Failed to fetch room information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
	const infoDiv = document.querySelector("#user");
	infoDiv.innerHTML =
		'<div class="user-box">' +
		'<img class="user_icon_img" src="/ThisCord/resource/user_icons/' + user_icon + '" onerror="retryImageLoad(this, 10, 1000)"></img>' +
		'<div style="line-height: 17px; padding:4px 0px 4px 8px;">' +
		'<p id="user-name">' + userinfo.user_name + '</p>' +
		'<p id="user-id">' + userinfo.user_name + '-' + userid + '</p>' +
		'</div>' +
		'</div>';
}
//通知サーバーのコネクションの初期化
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

//ボイスチャンネルに参加してるユーザーの表示をする関数
function createVoiceChannelIcon(members, channelId) {
	const videoChannelElement = document.getElementById('channelMember-' + channelId);

	console.log("メンバーの人数：" + members.length);
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

//jspのほかの切断ボタンから通話を切るための関数
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
}

function joinVoiceChannel(channelId, user, icon) {
  if (joinVoiceFlag) { // 既に参加している場合は切断
    //homeContainerFluid.style.gridTemplateColumns = "72px 240px calc(100% - 552px) 240px";
    homeContainerFluid.classList.add("video-grid-container");
    sendDisconnectVoiceChannel(nowVcId, user);

    window.multi.stopVideo();
    window.globalFunction.videoChat();
    window.multi.hangUp();

    if (channelId === nowVcId) { // 同じチャンネルの場合は切断
      joinVoiceFlag = false;
      nowUser = null;
      nowVcId = null;
      nowIcon = null;
    } else { // 別のチャンネルに参加
      //homeContainerFluid.style.gridTemplateColumns = "72px 240px calc(100% - 312px) 0px";
      homeContainerFluid.classList.remove("video-grid-container");
      sendJoinVoiceChannel(channelId, user, icon);
      joinVoiceFlag = true;
      nowVcId = channelId;

      window.globalFunction.videoChat();
      window.multi.connect(channelId);
    }
  } else { // 参加
    //homeContainerFluid.style.gridTemplateColumns = "72px 240px calc(100% - 312px) 0px";
    homeContainerFluid.classList.remove("video-grid-container");
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

async function getMessageInfo(channel_id) {
	try {
		var response;
		
		if (nowRoomId == -1) {
			response = await fetch("/ThisCord/fn/getpersonalmessageinfo?channel_id=" + channel_id);
		} else {
			response = await fetch("/ThisCord/fn/getmessageinfo?channel_id=" + channel_id);
		}
		
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



//サーバーのボタンを生成する関数
async function createRoomB(roomInfo) {
	const roomListDiv = document.getElementById("room-list");
	roomListDiv.innerHTML = "";
	for (const [roomId, roomName] of roomInfo) {
		const src = roomName[1];
		if (src === defaultSrc) {
			roomListDiv.innerHTML += '<div class="server-list-item"><a class="server-icon" id="server-id-' + roomId + '" onclick=" joinRoom(\'' + roomId + '\');"  ><div class="server-name">' + roomName[0] + '</div></a></div>';
		} else {
			roomListDiv.innerHTML += '<div class="server-list-item"><a class="server-icon" id="server-id-' + roomId + '" onclick=" joinRoom(\'' + roomId + '\');"  ><img id="retryImage" src="/ThisCord/' + src + '" onerror="retryImageLoad(this, 10, 1000)"></img></a></div>';
		}

	}
}

// テキストチャンネルのボタンを生成する関数
function createChannelButton(channelInfo) {
	const channelsListDiv = document.getElementById("channels-list");
	channelsListDiv.innerHTML = "";

	for (const [channel_id, channel_name] of channelInfo) {
		channelsListDiv.innerHTML +=
			'<div class="text-channels" id="channel-id-' + channel_id + '">' +
			'<a class="textIcon" href="javascript:joinChannel(\'' + channel_id + '\')">' +
			'<i class="fa-solid fa-hashtag fa-sm mx-r-5" style="margin-right: 5px;"></i>' +
			channel_name +
			'</a>' +
			'<a class="invitationIcon"  data-bs-toggle="modal" data-bs-target="#invitationIconModal"><i class="fa-solid fa-user-plus fa-xs"></i></a>	' +
			'</div>';
	}
}


//ボイスチャンネルのボタンを生成する関数
function createVoiceChannelButton(channelInfo) {
	const channelsListDiv = document.getElementById("voice-channels-list");
	channelsListDiv.innerHTML = "";

	for (const [channel_id, channel_name] of channelInfo) {
		channelsListDiv.innerHTML += '<div class="text-channels" id="channel-id-' + channel_id + '"><a class="voice-channel-linc" onclick="joinVoiceChannel(\'' + channel_id + '\', \'' + username + '\',\'' + user_icon + '\')"><i class="fa-solid fa-volume-low fa-sm" style="margin-right: 5px;"></i> ' + channel_name + '</a></div><div id="channelMember-' + channel_id + '"></div>';
	}
}

//テキストチャンネルに参加する関数
function joinChannel(channel_id) {
	if (chatSocket) {
		chatSocket.close();
	}
	if (nowRoomId != -1) {
		const infoDiv = document.querySelector("#channel");
		infoDiv.innerHTML = channelsMap.get(channel_id);
	}
	
	nowChannelId = channel_id;
	chatSocket = new WebSocket(`ws://${ip}:${port}/ThisCord/chat/${nowRoomId}/${nowChannelId}/${userinfo.user_id}`);
	console.log(`ws://${ip}:${port}/ThisCord/chat/${nowRoomId}/${nowChannelId}/${userinfo.user_id}`);

	chatSocket.onopen = event => {
		console.log("接続開始");
		getMessageInfo(channel_id);
	};

	chatSocket.onmessage = event => {
		console.log("Received message: " + event.data);
		const chat = document.getElementById("message-container");
		const rep = JSON.parse(event.data);
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
			//createRoomB(roomsMap);
			//createChannelButton(channelsMap);
			//createVoiceChannelButton(voiceChannelsMap);
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

	const MakeServerUserId = document.getElementById('MakeServerUserId');
	MakeServerUserId.value = userid;
	

	invFriendList
	getFriend('invFriendList');
	
	const formUserId = document.getElementById('formUserId');
	formUserId.value = userid;

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
					  <button class="inv-friend-button" onclick="invFriendForm(${friend.user_id})">
					    <div class="icon-name-wrapper">
					      <img class="fland_icon_img" src="/ThisCord/resource/user_icons/${friend.user_icon}" />
					      <div style="line-height: 17px; padding:4px 0px 4px 8px;">
					        <p id="user-name">${friend.user_name}</p>
					        <p id="user-id">${friend.user_name}-${friend.user_id}</p>
					      </div>
					    </div>
					  </button>
					</div>`;
			}

		} else {
			console.error("Failed to fetch room information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
}

function form_clear(formId) {
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



/**
 * home画面のjavascript
 * 
 */
const server_div = document.getElementById('server');
const channelsWrapper = document.getElementById('channelsWrapper');
const flendListWrapper = document.getElementById('flendListWrapper');
const serverHeaderWrapper = document.getElementById('serverHeaderWrapper');
const inputField = document.getElementById('inputField');
const homeChatField = document.getElementById('chat-field');
const messageContainer = document.getElementById('message-container');
const homeNav = document.getElementById('home-nav');
const memberListWrapper = document.getElementById('member-list-wrapper');
const friendFormWarper = document.getElementById('friend-form-warper');
const friendListWarpe = document.getElementById('friend-list-warpe');

const homeChannelFlandList = document.getElementById('home-channel-fland-list');
//const homeContainerFluid = document.getElementById('container-fluid');
const homeInfoList = document.getElementById('home-info-list');

let joinHomeFlag = false;
function joinHome() {
	server_div.innerHTML = 'Home';
	channelsWrapper.classList.add('none');
	serverHeaderWrapper.classList.add('none');
	inputField.classList.add('none');
	messageContainer.classList.add('none');
	memberListWrapper.classList.add('none');
	flendListWrapper.classList.remove('none');
	homeNav.classList.remove('none');
	friendFormWarper.classList.remove('none');
	friendListWarpe.classList.remove('none');
	homeChannelFlandList.classList.remove('none');

	homeInfoList.classList.remove('none');
	//homeContainerFluid.style.gridTemplateColumns = "72px 240px calc(100% - 729px) 417px";
	homeContainerFluid.classList.add('homepageGrid');
	
	if (!joinHomeFlag) {
		joinHomeFlag = true;
	}
	getFriendList();
	showInfo();
}
function toggleHome() {
	channelsWrapper.classList.remove('none');
	serverHeaderWrapper.classList.remove('none');
	inputField.classList.remove('none');
	memberListWrapper.classList.remove('none');
	messageContainer.classList.remove('none');
	flendListWrapper.classList.add('none');
	homeNav.classList.add('none');
	friendFormWarper.classList.add('none');
	friendListWarpe.classList.add('none');

	homeChannelFlandList.classList.add('none');
	homeInfoList.classList.add('none');
	//homeContainerFluid.style.gridTemplateColumns = "72px 240px calc(100% - 552px) 240px";
	homeContainerFluid.classList.remove('homepageGrid');

	joinHomeFlag = false;
}

//ホームページでユーザ情報を表示する
function showInfo() {
	const infoElement = document.getElementById('info-wrapper');
	const htmlCode = `
    <div class="info-header"></div>
    <img class="info-icon-user-img" src="/ThisCord/resource/user_icons/default1.png">
    <div class="info-card">
      <div class="info-top">
        <div id="info-user-name">${username}</div>
        <div id="info-user-id">${username}-${userid}</div>
      </div>
      <div class="info-friend-count">
        <div style="font-size:14px; font-waight: 700;">Thiscordフレンド数</div>
        <div style="color: #b5bac1; font-size: 12px;">2人</div>
      </div>
      <div class="info-logout">
        <a>
          <i class="fa-solid fa-right-from-bracket"></i>
          ログアウトする
        </a>
      </div>
    </div>
  `;

	infoElement.innerHTML = htmlCode;
}


//フレンド申請を送信する
async function sendFriendRequest() {
	const friendId = document.getElementById("friendId").value;
	const friendForm = document.getElementById('friend-form');
	console.log(friendId);
	try {
		const response = await fetch(`/ThisCord/fn/friendRequest?userId=${userid}&friendId=${friendId}`);

		if (response.ok) {
			console.log("ok");
			getFriendList();
			document.getElementById("friendId").value = "";
			friendForm.classList.toggle('ok');
		} else {
			console.error("Failed to fetch room information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
}

function invFriendForm(id){
	const invitationInput = document.getElementById('invitationInput');
	const inputServerId = document.getElementById('inputServerId');
	invitationInput.value = id;
	inputServerId.value = nowRoomId;
	console.log(nowRoomId);
}

//フレンドリストを表示する関数
async function getFriendList() {
	const friendListDiv = document.getElementById('friend-list');
	friendListDiv.innerHTML = "";
	homeChannelFlandList.innerHTML = '';
	try {
		const response = await fetch('/ThisCord/fn/getfriendList?userId=' + userid);
		if (response.ok) {
			const json = await response.json();

			for (let friend of json.friendList) {
				friendListDiv.innerHTML +=
					'<div class="fland-box">' +
					'<div class="icon-name-wrapper">' +
					'<img class="fland_icon_img" src="/ThisCord/resource/user_icons/' + friend.user_icon + '"></img>' +
					'<div style="line-height: 17px; padding:4px 0px 4px 8px;">' +
					'<p id="user-name">' + friend.user_name + '</p>' +
					'<p id="user-id">' + friend.user_name + '-' + friend.user_id + '</p>' +
					'</div>' +
					'</div>' +
					'<div class="about-button">' +
					'<i class="fa-solid fa-ellipsis-vertical"></i>' +
					'</div>' +
					'</div>';

				homeChannelFlandList.innerHTML +=
					'<a class="channel-fland-box" href="javascript:joinPersonalChat('+friend.user_id+')">' +
					'<img class="fland_icon_img" src="/ThisCord/resource/user_icons/' + friend.user_icon + '"></img>' +
					'<div style="line-height: 17px; padding:4px 0px 4px 8px;">' +
					'<p id="user-name">' + friend.user_name + '</p>' +
					'</div>' +
					'</a>';
			}

		} else {
			console.error("Failed to fetch room information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
}

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



