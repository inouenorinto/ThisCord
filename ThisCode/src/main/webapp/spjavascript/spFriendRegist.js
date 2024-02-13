///**
// * スマホ版フレンド申請送信用のJavaScript
// */

 
 //フレンド申請を送信する
async function sendFriendRequest() {
	const friendId = document.getElementById("friendId").value;
	const friendForm = document.getElementById('friend-form');
	console.log(friendId);
	try {
		const response = await fetch(`/ThisCord/fn/friendRequest?userId=${userid}&friendId=${friendId}`);

		if (response.ok) {
			console.log("ok");
			document.getElementById("friendId").value = "";
			friendForm.classList.toggle('ok');
		} else {
			console.error("Failed to fetch room information");
		}
	} catch (error) {
		console.error("Error: " + error);
	}
}