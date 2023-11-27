 /**
 *chatpage.jspのWebSocketに関するjavascript
 */
 let defaultSrc='default';

 let nowRoomId = null;
 let rooms = null;
 let roomsMap = null;
 let nowChannelId = null;
 let channelsMap = null;
 let chatDiv = null;
 let chatSocket = null;
 let username = null;
 let userid = null;
 let userinfo = null;
 let user_icon = null;
 
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
     	'<div class="user-box">'+
         	'<img class="user_icon_img" src="resource/user_icons/' + user_icon + '"></img>'+
         	'<div style="line-height: 17px; padding:4px 0px 4px 8px;">'+
         		'<p id="user-name">'+userinfo.user_name + '</p>'+
         		'<p id="user-id">'+userinfo.user_name+'-'+ userid +'</p>'+
         	'</div>'+
         '</div>';
 }
 
 
//サーバーのボタンを生成する関数
 function createRoomB(roomInfo) {
     const roomListDiv = document.getElementById("room-list");
     roomListDiv.innerHTML = "";
     for (const [roomId, roomName] of roomInfo) {
         console.log(roomName[1]);
         const src = roomName[1];
         if(src === defaultSrc) {
             roomListDiv.innerHTML += '<div class="server-list-item"><a class="server-icon" id="server-id-'+ roomId +'" onclick=" joinRoom(\'' + roomId + '\');"  ><div class="server-name">'+roomName[0]+'</div></a></div>';
         } else {
             roomListDiv.innerHTML += '<div class="server-list-item"><a class="server-icon" id="server-id-'+ roomId +'" onclick=" joinRoom(\'' + roomId + '\');"  ><img src="' + src +'"></img></a></div>';
         }
         
     }
 }
 
// テキストチャンネルのボタンを生成する関数
 function createChannelButton(channelInfo) {
     const channelsListDiv = document.getElementById("channels-list");
     channelsListDiv.innerHTML = "";
 
     for (const [channel_id, channel_name] of channelInfo) {
 
         channelsListDiv.innerHTML += '<div class="text-channels" id="channel-id-'+channel_id+'"><a href="javascript:joinChannel(\'' + channel_id +'\')"><i class="fa-solid fa-hashtag fa-sm mx-r-5"></i> '+ channel_name + '</a></div>';
     }
 }

//テキストチャンネルに参加する関数
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
     };
 
     chatSocket.onmessage = event => {
         console.log("Received message: " + event.data);
         const chat = document.getElementById("message-container");
         const rep = JSON.parse(event.data);
         //chat.innerHTML += '<img src="'+rep.usericon+'" >'+rep.username + " " + rep.date + "<br>" + rep.message + "<br><br>";
     	chat.innerHTML +=
		'<div class="message-wrapper">'+
		    '<div>'+
		        '<img class=" chat-icon" src="'+rep.usericon+'" >'+
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
    
    const firstTextChannel = channelsMap.entries().next().value;
 	const firstTextChannelId = firstTextChannel[0];
 	
 	joinChannel(firstTextChannelId);
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
     const currentElement = document.querySelector('#server-id-'+nowRoomId);
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
         usericon:user_icon,
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
 
 //ログインしたらデフォルトで一番上のサーバーを表示する
async function init() {
    await getUserInfo();

    const firstServer = roomsMap.entries().next().value;
    const firstServerId = firstServer[0];
    await joinRoom(firstServerId);

//	const firstTextChannel = channelsMap.entries().next().value;
// 	const firstTextChannelId = firstTextChannel[0];
// 	
// 	joinChannel(firstTextChannelId);
}
 
 document.addEventListener("DOMContentLoaded", init);
 