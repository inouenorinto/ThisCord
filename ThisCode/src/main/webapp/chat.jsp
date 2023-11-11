<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<!DOCTYPE html>
	<html>

	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">

		<title>Thiscord</title>

		<!-- cropper.js -->
		<script src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.min.js"
			integrity="sha512-9KkIqdfN7ipEW6B6k+Aq20PV31bjODg4AA52W+tYtAE0jE0kMx49bjJ3FgvS56wzmyfMUHbQ4Km2b7l9+Y/+Eg=="
			crossorigin="anonymous" referrerpolicy="no-referrer"></script>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.css"
			integrity="sha512-bs9fAcCAeaDfA4A+NiShWR886eClUcBtqhipoY5DM60Y1V3BbVQlabthUBal5bq8Z8nnxxiyb1wfGX2n76N1Mw=="
			crossorigin="anonymous" referrerpolicy="noreferrer" />
		<script src=" https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.js"
			integrity="sha512-Zt7blzhYHCLHjU0c+e4ldn5kGAbwLKTSOTERgqSNyTB50wWSI21z0q6bn/dEIuqf6HiFzKJ6cfj2osRhklb4Og=="
			crossorigin="anonymous" referrerpolicy="no-referrer">
		</script>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.min.css"
			integrity="sha512-hvNR0F/e2J7zPPfLC9auFe3/SE0yG4aJCOd/qxew74NN7eyiSKjr7xJJMu1Jy2wf7FXITpWS1E/RY8yzuXN7VA=="
			crossorigin="anonymous" referrerpolicy="no-referrer" />
		<link
			href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@100;200;300;400;500;600;700;800;900&family=Poppins:wght@200;300;400;500;600;900&display=swap"
			rel="stylesheet">
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"
			integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">

		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/general.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/chatpage.css">

		<script>
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
					usernme = userinfo.user_name;
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
			"<img class=\"user_icon\" src=\"${pageContext.request.contextPath}\\"+ user_icon+"\"></img>"+
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

		function createRoomB(roomInfo) {
			const roomListDiv = document.getElementById("room-list");
			roomListDiv.innerHTML = "";

			for (const [roomId, roomName] of roomInfo) {
				
				roomListDiv.innerHTML += 
				"<a href=\"javascript:joinRoom(" + roomId + ")\" data-bs-toggle=\"tooltip\" data-bs-title=\""+roomName[0]+"\">"+
				"<img class=\"server_icon\" src=\"${pageContext.request.contextPath}\\"+ roomName[1]+"\"></img></a><dir>";
				
			}
		}

		function createChannelButton(channelInfo) {
			const channelsListDiv = document.getElementById("channels-list");
			channelsListDiv.innerHTML = "";

			for (const [channel_id, channel_name] of channelInfo) {

				channelsListDiv.innerHTML += " <a href=\"javascript:joinChannel(" + channel_id + ")\">#" + channel_name + "<\a><dir>";
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
			};

			chatSocket.onmessage = event => {
				// メッセージを表示するロジックをここに追加
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

	        // 送信中フラグを設定
	        form.setAttribute('data-submitting', 'true');

	        // フォーム送信
	        form.submit();
			
		}


	    function handleKeyPress(event) {
	        // エンターキーが押されたかを確認
	        if (event.key === "Enter") {
	        	sendMessage();
	            
	        }
	    }

		document.addEventListener("DOMContentLoaded", getUserInfo);
				
		</script>
	</head>

	<body>
		<h6>Thiscord</h6>
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
		
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-1 scroll no-padding">

					<!-- サーバ作成ボタン -->
					<div class="plus_button">
						<a data-bs-toggle="modal" data-bs-target="#createServerModal"> 
							<i class="fa-solid fa-plus fa-xl" style="color: rgb(41, 117, 75);"></i>
						</a>
					</div>
					<!-- サーバ一覧 -->
					<div id="room-list"></div>
				</div>

				<!-- サーバを作成するモーダル -->
				<div class="modal fade" id="createServerModal" tabindex="-1" aria-labelledby="exampleModalLabel"
					aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="center_content">
								<h1 class="modal-title" id="exampleModalLabel">サーバーをカスタマイズ</h1>
							</div>
							<button type="button" class="btn-close ml-5" data-bs-dismiss="modal"
								aria-label="Close"></button>

							<div class="center_content">
								<p>新しいサーバーの名前とアイコンを設定して、個性を出しましょう。設定内容は後から変更できるわけがありません。</p>
								<!--アイコンのプレビューと編集-->

								<div class="user_icon_div">
									<img id='preview' class="user_icon"
										src="${pageContext.request.contextPath}/resource/user_icons/default2.png">
									<div class="plus_button_icon">
										<a data-bs-toggle="modal" data-bs-target="#exampleModal"> 
											<i class="fa-solid fa-plus fa-lg"></i>
										</a>
									</div>
								</div>
							</div>
							<!--サーバーの名入力欄-->
							<form id="imageForm" action="fn/makeserver" method="post" enctype="multipart/form-data" onsubmit = "return false">
								<div class="mb-3">
									<label for="exampleFormControlInput1" class="form-label">サーバー名</label>
									<input type="text" name="server_name" class="form-control input_text" id="exampleFormControlInput1">
								</div>
								<p><small>サーバーを作成すると、Thiscordの<a href="">コミュニティガイドライン</a>に同意したことになります。</small></p>
								<input type="hidden" id="editedImageField" name="editedImage">
								<div class="sub">
									<button type="button" id="form_submit" class="btn_sub" onclick="submitForm('imageForm')">新規作成</button>
								</div>
							</form>

							<div class="con">
								<div class="back">
									<div class="back-botan">戻る</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<!-- サーバのアイコンを編集するモーダル -->
				<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel"
					aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<h1 class="modal-title fs-5" id="exampleModalLabel">サーバーアイコン</h1>
								<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
							</div>
							<div class="modal-body">
								<input type="file" id="imageInput" name="image" accept="image/*">
								<div style="width: 300px; height: 300px;">
									<img id="cropper-tgt" style="width: 300px; height: 300px;">
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary" data-bs-toggle="modal" data-bs-target="#createServerModal">閉じる</button>
								<button type="button" id="cropButton" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createServerModal">確定</button>
							</div>
						</div>
					</div>
				</div>

				<!-- チャンネル一覧 -->
				<div class="col-md-2 b-color-chat-side radiusleftup no-padding h-100">
					<!-- メニューバー -->
					<div class="radiusleftup menubar text-center"><div id="server"></div></div>
					<div>
						<a href="/ThisCord/createChannel.jsp">
							<button>チャンネルの追加</button>
						</a>
					</div>
					<!-- 一覧 -->
					<div class="scrollbar text-box">
						<div id="channels-list"></div>
					</div>
					<div id="user"></div>
				</div>

				<!-- テキストチャット -->
				<div class="col-md-7 b-color-chat chatposi no-padding h-100">
					<!-- メニューバー -->
					<div class="menubar text-center"><div id="channel"></div></div>
					<!-- チャット -->
					<div class="text-color scrollbar text-box">
						<div>
							<div id="message-container"></div>
						</div>
					</div>
					<div class="b-color-chat">
						<div id="chat" class="message_div">
							<div id="input-container" class="input_div">
								<i class="fa-solid fa-hashtag"></i>
								<input type="text" id="message-input" class="message_input" placeholder="メッセージを送信" onkeydown="handleKeyPress(event)">
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-2 b-color-chat-side no-padding scrollbar h-100">
					<div class="text-box">
						ユーザー<br>
						<div id="members-list"></div>
					</div>
				</div>
				<!--
			<div class="col-md">
			スマホ用画面
			</div>
			-->

			</div>
		</div>
		<div class="col-md-1">
			<div class="scroll">
				<div id="room-list"></div>
			</div>
		</div>

		<div class="col-md-11">
			<div id="spa1" style="display: none;">
				<a href="/ThisCord/invitation.jsp">
					<button>メンバーを招待</button>
				</a>
			</div>
		</div>
		
		<div id="chat" style="display: none;">
			<div id="input-container">
				<input type="text" id="message-input" placeholder="メッセージを入力" onkeydown="handleKeyPress(event)">
				<button onclick="sendMessage()">送信</button>
			</div>
		</div>
				
		<script src="${pageContext.request.contextPath}/javascript/createServer.js"></script>
		<script src="https://kit.fontawesome.com/c82cac4dcf.js" crossorigin="anonymous"></script>
	</body>
	</html>