<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Thiscord</title>
<!-- bootstrap css -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM"
	crossorigin="anonymous">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.min.js"
	integrity="sha512-9KkIqdfN7ipEW6B6k+Aq20PV31bjODg4AA52W+tYtAE0jE0kMx49bjJ3FgvS56wzmyfMUHbQ4Km2b7l9+Y/+Eg=="
	crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.css"
	integrity="sha512-bs9fAcCAeaDfA4A+NiShWR886eClUcBtqhipoY5DM60Y1V3BbVQlabthUBal5bq8Z8nnxxiyb1wfGX2n76N1Mw=="
	crossorigin="anonymous" referrerpolicy="noreferrer" />
<script
	src=" https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.js"
	integrity="sha512-Zt7blzhYHCLHjU0c+e4ldn5kGAbwLKTSOTERgqSNyTB50wWSI21z0q6bn/dEIuqf6HiFzKJ6cfj2osRhklb4Og=="
	crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.min.css"
	integrity="sha512-hvNR0F/e2J7zPPfLC9auFe3/SE0yG4aJCOd/qxew74NN7eyiSKjr7xJJMu1Jy2wf7FXITpWS1E/RY8yzuXN7VA=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@100;200;300;400;500;600;700;800;900&family=Poppins:wght@200;300;400;500;600;900&display=swap"
	rel="stylesheet">

<!-- Googleフォント -->
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@100;200;300;400;500;600;700;800;900&family=Poppins:wght@200;300;400;500;600;900&display=swap"
	rel="stylesheet">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@100;200;300;400;500;600;700;800;900&family=Open+Sans:wght@300;400;500;600;700;800&family=Poppins:wght@200;300;400;500;600;900&display=swap"
	rel="stylesheet">

<!-- 独自css -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/general.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/homepage.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/phoneChatPage.css">
<!-- 独自javascript -->
</head>
<body>
	<div class="thiscord-title">
		<h6>Thiscord</h6>
	</div>

	<div id="container-fluid" class="container-fluid">

		<!---------------------------------- サーバー一覧(server-list) -------------------------------->
		<div id="server-list" class="server-scroll server-list">

			<!-- ホームボタン -->
			<div class="server-list-item">
				<div class=" pill_d1140c wrapper__3670f">
					<span class="item_f9d377 none"></span>
				</div>

				<a class="home-button"
					onclick="toggleClickedState(this); joinHome();"> <i
					class="fa-brands fa-discord fa-xl"></i>
				</a>
			</div>

			<div class="server-list-item">
				<div class="guildSeparator"></div>
			</div>

			<div id="room-list"></div>

			<!-- サーバー作成ボタン -->
			<div class="server-list-item">
				<a class="plus_button" data-bs-toggle="modal"
					data-bs-target="#createServerModal"> <i
					class="fa-solid fa-plus fa-xl"></i>
				</a>
			</div>
		</div>

		<!---------------------------- チャンネルメヘッダー(channel-header) --------------------------->
		<div class="radius-lefttop text-center channel-header text-color">
			<div id="server"></div>
			<i class="fa-solid fa-angle-down fa-xs"></i>
		</div>
		<!-------------------------------- チャンネル一覧 (channel-list)------------------------------->
		<div class="channel-list">
			<!-- 一覧 -->
			<div class="channel-scroll text-box">
				<div id="flendListWrapper" class="none">
					<div class="channel-list-friend">
						<i class="fa-solid fa-user-group fa-sm"></i>
						<label>フレンド</label>
					</div>
					<div class="channel-title-nav">
						<div class="flexbox">
							<div class="flex-item title-item">
								<i class="fa-solid fa-angle-down fa-xs"></i>
								<span class="channel-title">フレンド一覧</span>
							</div>
							<div class="flex-item">
								<a href="/ThisCord/createChannel.jsp" class="invitation-link">
									<i class="fa-solid fa-plus fa-sm"></i>
								</a>
							</div>
						</div>
					</div>
				</div>
				
				<div class="none" id="home-channel-fland-list"></div>
				
				<div id="channelsWrapper" class="channelsWrapper">
					<div class="channel-title-nav">
						<div class="flexbox">
							<div class="flex-item title-item">
								<i class="fa-solid fa-angle-down fa-xs"></i> <span
									class="channel-title">テキストチャンネル</span>
							</div>
							<div class="flex-item">
								<a href="/ThisCord/createChannel.jsp" class="invitation-link">
									<i class="fa-solid fa-plus fa-sm"></i>
								</a>
							</div>
						</div>
					</div>

					<div id="channels-list"></div>

					<div class="channel-title-nav">
						<div class="flexbox">
							<div class="flex-item title-item">
								<i class="fa-solid fa-angle-down fa-xs"></i> <span
									class="channel-title">ボイスチャンネル</span>
							</div>
							<div class="flex-item">
								<a id="videotest" href="/ThisCord/createChannel.jsp"
									class="invitation-link"> <i class="fa-solid fa-plus fa-sm"></i>
								</a>
							</div>
						</div>
					</div>
					
					<div id="voice-channels-list"></div>
					
				</div>
			</div>

			<div class="user-info">
				<div id="media-interface" class="media-interface none">
					<div class="flexbox" style="height: 38px;">
						<div style="line-height: 17px;">
							<p id="status">通話中</p>
							<p id="target">test/test</p>
						</div>

						<div>
							<button class="interface-item-i">
								<i class="fa-solid fa-signal fa-sm"></i>
							</button>
							<button class="interface-item-i" onclick="closeVoiceChannel();">
								<i class="fa-solid fa-phone-slash fa-sm"></i>
							</button>
						</div>
					</div>
					<div class="flexbox">
						<button class="interface-item-button" onclick="stopVideo();">
							<i class="fa-solid fa-video-slash fa-sm"></i>
						</button>
						<button class="interface-item-button">
							<i class="fa-solid fa-desktop fa-sm"></i>
						</button>
					</div>
				</div>
				<div class="d-flex align-items-end h-10 user-conf">
					<div class="user-field">
						<div id="user"></div>
						<div class=".buttns">
							<button class="interface-item-i" id="microphone">
								<i class="fa-solid fa-microphone fa-sm"></i>
							</button>
							<button class="interface-item-i">
								<i class="fa-solid fa-headphones fa-sm"></i>
							</button>
							<button class="interface-item-i">
								<i class="fa-solid fa-gear fa-sm"></i>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-------------------------------- ビデオフィールド(video-field)------------------------------->
		<div id="video-field" class="video-field none">
			
			<div id="video-play-field" class="video-play-field container">
				<video id="local_video" class="video-element" autoplay></video>
			</div>
			
			<div class="video-controller">
				<div class="controller-wrapper">
					<button class="contoroller-item">
						<i class="fa-solid fa-video fa-sm"></i>
					</button>
					<button class="contoroller-item-phone-slash"
						onclick="closeVoiceChannel();">
						<i class="fa-solid fa-phone-slash fa-lg"></i>
					</button>
				</div>
			</div>
		</div>

		<!---------------------------- チャットメニューバー(server-header) ---------------------------->
		<div id="server-header" class="text-center server-header">
			<div class="channle-name">
				<div id="serverHeaderWrapper" class="flexbox">
					<div class="flex-item hash-tag">#</div>
					<div class="flex-item channel-title">
						<div id="channel"></div>
					</div>
				</div>
			</div>
			<!----------------------------------------ホームページヘッダー------------------------------------------->
			<div id="home-nav" class="none">
				<div class="home-nav-warper">
					<div class="friend-warper">
						<i class="fa-solid fa-user-group fa-sm"></i>
						<label>フレンド</label>
					</div>
					<div class="separator home-item"></div>
					<div class="home-item">
						<button class="home-item-all-button focus">全て表示</button>
					</div>
					<div class="home-item">
						<button class="home-item-friend-button">フレンドに追加</button>
					</div>
				</div>
			</div>
		</div>

		<!------------------------------- チャットフィールド(chat-field) ------------------------------>
		<div id="chat-field" class="chat-field text-box text-color">
			<div id="chat-scroll" class="chat-scroll">
				<div id="friend-form-warper" class="none">
					<h1 style="font-size: 1.2em;">フレンドに追加</h1>
					<p style="font-size: 0.8em;">ThiscordユーザーIDでフレンドを追加する</p>
					<div id="friend-form" class="friend-form">
						<input type="text" id="friendId" placeholder="ThiscordユーザーIDでフレンドを追加できます。">
						<button onclick="sendFriendRequest()">フレンド申請を送信</button>
					</div>
					<div id="ok" class="none" style="color:#2fbb70; font-size:0.8em;">フレンドを登録しました。</div>
					<div id="ng" class="none" style="color:#f23f42; font-size:0.8em;">フレンド登録に失敗しました。</div>
				</div>
				<!-- ホームページ　フレンドリスト -->
				<div id="friend-list-warpe" class="none">
					<p style="margin :0; border-bottom: 1px solid #3f4147; padding: 40px 0px 15px 0px; font-size: 0.8em;">すべてのフレンド</p>
					<div id="friend-list"></div>
				</div>
				
				<div class="d-flex mb-3">
					<div id="message-container"></div>
				</div>
			</div>
			<!-- メッセージ入力フィールド -->
			<div id="inputField" class="message-form-wrapper">
				<div id="chat" class="message_div">
					<i class="fa-solid fa-hashtag"></i> <input type="text"
						id="message-input" class="message_input" placeholder="メッセージを送信"
						onkeydown="handleKeyPress(event)">
				</div>
			</div>
		</div>
		<!-------------------------------- メンバーリスト(member-list) -------------------------------->
		<div id="member-list" class="member-scroll member-list">
			<div id="member-list-wrapper">
				<div class="text-box">
					<h3 class="meber-list-title">メンバー</h3>
					<div id="members-list"></div>
				</div>
			</div>
			<div id="home-info-list" class="none">
				<div class="info-wrapper" id="info-wrapper"></div>
			</div>
		</div>

		<!--
		<div class="col-md">
		スマホ用画面
		</div>
		-->

	</div>

	<!-- ****************************************** モーダル群 ****************************************** -->

	<!-- サーバを作成するモーダル -->
	<div class="modal fade" id="createServerModal" tabindex="-1"
		aria-labelledby="createServerModal" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content modal-content2">
				<div class="center_content">
					<h1 class="modal-title modal-title2" id="exampleModalLabel">サーバーをカスタマイズ</h1>
				</div>
				<button type="button" class="btn-close btn-close2 ml-5" data-bs-dismiss="modal"
					aria-label="Close"></button>

				<div class="center_content">
					<p>新しいサーバーの名前とアイコンを設定して、個性を出しましょう。設定内容は後から変更できません。</p>
					<!--アイコンのプレビューと編集-->

					<div class="select-icon-container">
						<img id='preview' class="select-icon-img"
							src="${pageContext.request.contextPath}/resource/user_icons/default2.png">
						<div class="plus_button_icon">
							<a data-bs-toggle="modal" data-bs-target="#exampleModal"> <i
								class="fa-solid fa-plus fa-lg"></i>
							</a>
						</div>
					</div>
				</div>
				<!--サーバーの名入力フォーム-->
				<form id="imageForm" action="fn/makeserver" method="post"
					enctype="multipart/form-data" onsubmit="return false">
					<div class="mb-3">
						<label for="exampleFormControlInput1" class="form-label">サーバー名</label>
						<input type="text" name="server_name"
							class="form-control input_text" id="server_name" required>
					</div>
					<p>
						<small>サーバーを作成すると、Thiscordの<a href="">コミュニティガイドライン</a>に同意したことになります。
						</small>
					</p>
					<input type="hidden" id="editedImageField" name="editedImage">
					<input type="hidden" id="MakeServerUserId" name="userId">
					<div class="sub">
						<button type="button" id="form_submit" class="btn_sub"
							onclick="form_crea('imageForm')">新規作成</button>
					</div>
				</form>

				<div class="con">
					<div class="back">
						<div class="back-botan" data-bs-dismiss="modal">戻る</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- サーバのアイコンを編集するモーダル -->
	<div class="modal fade" id="exampleModal" tabindex="-1"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content edit-modal" style="padding-bottom: 0px;">
				<div class="modal-header">
					<h1 class="modal-title fs-5" id="exampleModalLabel">サーバーアイコン</h1>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<input type="file" id="imageInput" name="image" accept="image/*">
					<div style="width: 300px; height: 300px;">
						<img id="cropper-tgt" style="width: 300px; height: 300px;">
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-bs-toggle="modal" data-bs-dismiss="modal">閉じる</button>
					<button type="button" id="cropButton" class="btn btn-primary"
						data-bs-toggle="modal" data-bs-dismiss="modal">確定</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="invitationIconModal" tabindex="-1" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content invitation-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">フレンドをサーバーに招待する</h1>
                    <button type="button" data-bs-dismiss="modal" aria-label="Close" class="close"><i class="fa-solid fa-xmark"></i></button>
                </div>
                <div class="modal-body invitation-body">
                    <div class="invitation-form">
                        <form class="invitation-form" action="fn/invite" method="post">
                            <label>ユーザーIDを入力してフレンドを追加</label>
                            <div class="invitation-wrapper">
                                <input class="invitation-input" type="text" name="userId" id="invitationInput">
                                <input id="inputServerId" type="hidden" name="serverId">
                                <input id="formUserId" type="hidden" name="id">
                                <button class="invitation-button" type="submit">送信</button>
                            </div>
                        </form>
                        <div id="invFriendList"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
	
	<script
		src="${pageContext.request.contextPath}/javascript/chatWebSocket.js"></script>
	<script
		src="${pageContext.request.contextPath}/javascript/createServer.js"></script>
	<script src="${pageContext.request.contextPath}/javascript/multi.js"></script>
	<script
		src="${pageContext.request.contextPath}/javascript/chatPageOperations.js"></script>
	<script src="https://kit.fontawesome.com/c82cac4dcf.js"
		crossorigin="anonymous"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"
		integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p"
		crossorigin="anonymous"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"
		integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF"
		crossorigin="anonymous"></script>

</body>
</html>