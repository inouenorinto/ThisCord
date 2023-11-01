<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Insert title here</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM"
	crossorigin="anonymous">

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/general.css">

</head>
<body>
	<h6>
		Thiscord
		<div id="username">この要素にIDを設定します</div>

		</h1>
		<script
			src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
			integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
			crossorigin="anonymous"></script>

		<div class="container-fluid b-color-gray1">
			<div class="row h-100">
				<div class="col-md-1">
					<div class="scrroll">

						<button onclick="joinRoom('room1')"
							style="background: none; border: none;">
							<img src="${pageContext.request.contextPath}/img/ino.jpg"
								class="rounded-circle server-list-img server-img" alt="サーバーアイコン">
						</button>

						<button onclick="joinRoom('room2')"
							style="background: none; border: none;">
							<img src="${pageContext.request.contextPath}/img/ino.jpg"
								class="rounded-circle server-list-img server-img" alt="サーバーアイコン">
						</button>

						<button onclick="joinRoom('room3')"
							style="background: none; border: none;">
							<img src="${pageContext.request.contextPath}/img/ino.jpg"
								class="rounded-circle server-list-img server-img" alt="サーバーアイコン">
						</button>

						<button onclick="joinRoom('room4')"
							style="background: none; border: none;">
							<img src="${pageContext.request.contextPath}/img/ino.jpg"
								class="rounded-circle server-list-img server-img" alt="サーバーアイコン">
						</button>

						<button onclick="joinRoom('room5')"
							style="background: none; border: none;">
							<img src="${pageContext.request.contextPath}/img/ino.jpg"
								class="rounded-circle server-list-img server-img" alt="サーバーアイコン">
						</button>

						<button onclick="joinRoom('room6')"
							style="background: none; border: none;">
							<img src="${pageContext.request.contextPath}/img/ino.jpg"
								class="rounded-circle server-list-img server-img" alt="サーバーアイコン">
						</button>

						<button onclick="joinRoom('room7')"
							style="background: none; border: none;">
							<img src="${pageContext.request.contextPath}/img/ino.jpg"
								class="rounded-circle server-list-img server-img" alt="サーバーアイコン">
						</button>
						<button onclick="joinRoom('room7')"
							style="background: none; border: none;">
							<img src="${pageContext.request.contextPath}/img/ino.jpg"
								class="rounded-circle server-list-img server-img" alt="サーバーアイコン">
						</button>
						<button onclick="joinRoom('room7')"
							style="background: none; border: none;">
							<img src="${pageContext.request.contextPath}/img/ino.jpg"
								class="rounded-circle server-list-img server-img" alt="サーバーアイコン">
						</button>

					</div>
				</div>
				<div class="col-md-2 border border-dark b-color1 radiusleftup">
					チャンネル一覧</div>
				<div
					class="col-md-7 border border-dark text-color b-color2 chatposi pt-2 pb-5">
					<div id="res"></div>

					<div id="chat" class="chatposic">
						<input type="text" id="message" placeholder="メッセージを入力">
						<button onclick="sendMessage()" class="formcss">送信</button>
						<br>
					</div>
				</div>
				
				<div class="col-md-2 border border-dark b-color1">所属メンバー</div>

			</div>
		</div>

		<script>
				
		let room = null;
		const chatDiv = document.getElementById("chat");
		let chatSocket = null;
		let username = null;
		var element = document.getElementById("username");

		async function joinRoom(roomname) {
			room = roomname;
			//roomSelect.disabled = true;
			chatDiv.style.display = "block";

			try {
				const response = await fetch("/ThisCord/get-username");
				if (response.ok) {
					username = await response.text();
				} else {
					console.error("ユーザー名を取得できねえ");
				}
			} catch (error) {
				console.error("エラー: " + error);
				return;
			}

			console.log("ユーザー名: " + username);
			console.log("ルーム名: "+room);
			console.log("ws://localhost:8080/ThisCord/chat/room1/${username}");
			chatSocket = new WebSocket("ws://localhost:8080/ThisCord/chat/"+room+"/${username}");

			chatSocket.onopen = event => {
				console.log("サーバーに接続した");
			};

			chatSocket.onmessage = event => {
				const chat = document.getElementById("res");
				console.log(event.data);
				var json = JSON.parse(event.data);
				console.log(json.username);
				chat.innerHTML += 	"<div class=\"row g-1\">"+
										"<img src=\"${pageContext.request.contextPath}/img/ino.jpg\" class=\"rounded-circle user-icon\" height=\"50\">"+
										"<div class=\"col-auto\">"+
											"<span class=\"h5\">"+json.username+" </span>"+
											"<span>"+json.date+"</span>"+"<br>"+
											"<span class=\"h5\">"+json.text+"</span>"+
										"</div>"+
									"</div>"+
									"<br>";
			};

			chatSocket.onclose = event => {
				console.log("サーバーから切断した");
			};
		}

		// メッセージを送信する処理
		function sendMessage() {
			const messageInput = document.getElementById("message");
			const message = messageInput.value;
			chatSocket.send(message);
			messageInput.value = "";

			const messageData = {
				    username: username,
				    date: getDate(),
				    text: message
			};

			const messageJSON = JSON.stringify(messageData);
			chatSocket.send(messageJSON);
			console.log( getDate() );
		}

		// Enterキーでの送信処理
		chatDiv.addEventListener('keypress', test_ivent);
		function test_ivent(e) {
			if (e.keyCode === 13) {
				sendMessage(); // sendMessageを呼び出してメッセージを送信
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
	</script>
</body>
</html>