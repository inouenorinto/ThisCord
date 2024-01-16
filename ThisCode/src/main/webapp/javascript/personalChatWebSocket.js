function joinPersonalChat(user_id) {
	console.log("joinPersonalChat_TestMessage");
	
	joinPersonalChatView();
	
}
function joinPersonalChatView() {
	
	serverHeaderWrapper.classList.remove('none');
	inputField.classList.remove('none');
	messageContainer.classList.remove('none');
	homeNav.classList.add('none');
	friendFormWarper.classList.add('none');
	friendListWarpe.classList.add('none');
	
	homeContainerFluid.style.gridTemplateColumns = "72px 240px calc(100% - 729px) 417px";
}









