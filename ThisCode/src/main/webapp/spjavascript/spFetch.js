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
async function getUserInfo() {
	try {
		const response = await fetch("/ThisCord/fn/getuserinfo?id=1");

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
		'<img class="user_icon_img" src="/ThisCord/resource/user_icons/' + user_icon + '" onerror="retryImageLoad(this, 10, 1000)"></img>' +
		'<div style="line-height: 17px; padding:4px 0px 4px 8px;">' +
		'<p id="user-name">' + userinfo.user_name + '</p>' +
		'<p id="user-id">' + userinfo.user_name + '-' + userid + '</p>' +
		'</div>' +
		'</div>';
}

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


getUserInfo();