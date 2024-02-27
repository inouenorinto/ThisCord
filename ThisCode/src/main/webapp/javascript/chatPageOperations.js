const videoField = document.getElementById('video-field');
const serverHeader = document.getElementById('server-header');
const chatField = document.getElementById('chat-field');
const memberList = document.getElementById('member-list');
const containerFluid = document.getElementById('container-fluid');
const mediaInterface = document.getElementById('media-interface');

let videoDisplay = false;

function videoChat() {
    if (videoDisplay == false) {
        videoDisplay = true;
    } else {
        videoDisplay = false;
    }
    videoField.classList.toggle('none');
    serverHeader.classList.toggle('none');
    chatField.classList.toggle('none');
    memberList.classList.toggle('none');
    mediaInterface.classList.toggle('none');
    containerFluid.classList.toggle('video-container');
}

const micro = document.getElementById('microphone');
let flag = true;
micro.addEventListener('click', () => {
    if (flag) {
        micro.innerHTML = '<i class="fa-solid fa-microphone-slash fa-sm"></i>';
        flag = false;
    } else {
        micro.innerHTML = '<i class="fa-solid fa-microphone fa-sm"></i>';
        flag = true;
    }

});

let selectedElement = null;
function toggleClickedState(element) {
    console.log(element);
    if (selectedElement && selectedElement !== element) {
        selectedElement.classList.remove('clicked');
    }
    element.classList.add('clicked');
    selectedElement = element;
}
let selectedChannel = null;
function toggleChannelState(element) {
    console.log(element);
    if (selectedChannel && selectedChannel !== element) {
        selectedChannel.classList.remove('clicked');
    }

    element.classList.add('clicked');
    selectedChannel = element;
}
window.addEventListener('load', () => {
	const form = document.getElementById('createChannelForm');

    form.addEventListener('submit', (event) => {
        event.stopPropagation();
        event.preventDefault();

        const formData = new FormData(form);
        const options = {
            method: 'POST',
            body: formData,
        }

        const url = form.getAttribute('action');
        fetch(url, options)
        .then(response => {
            if (response.ok) {
                joinRoom(nowRoomId);
                form.reset();
                modalToggle('createChannelModal')
            } else {
                throw new Error('ネットワークエラー');
            }
        });
    })
})


window.globalFunction = {};
window.globalFunction.toggleClickedState = toggleClickedState;
window.globalFunction.toggleChannelState = toggleChannelState;
window.globalFunction.videoChat = videoChat;
