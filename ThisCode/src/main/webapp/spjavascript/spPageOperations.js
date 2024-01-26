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
    //videoField.classList.toggle('none');
    //serverHeader.classList.toggle('none');
    //chatField.classList.toggle('none');
    //memberList.classList.toggle('none');
    //mediaInterface.classList.toggle('none');
    //containerFluid.classList.toggle('video-container');
}

// const micro = document.getElementById('microphone');
// let flag = true;
// micro.addEventListener('click',()=>{
//     if(flag) {
//         micro.innerHTML = '<i class="fa-solid fa-microphone-slash fa-sm"></i>';
//         flag = false;
//     } else {
//         micro.innerHTML = '<i class="fa-solid fa-microphone fa-sm"></i>';
//         flag = true;
//     }

// });

let selectedElement = null;
function toggleClickedState(element) {
    if (selectedElement && selectedElement !== element) {
        selectedElement.classList.remove('clicked');
    }
    element.classList.add('clicked');
    selectedElement = element;
}
let selectedChannel = null;
function toggleChannelState(element) {
    if (selectedChannel && selectedChannel !== element) {
        selectedChannel.classList.remove('clicked');
    }

    element.classList.add('clicked');
    selectedChannel = element;
}

function modalToggle(element) {
    const elem = document.getElementById(element);
    elem.classList.toggle('open');
}

function invFriendForm(id) {
    const invitationInput = document.getElementById('invitationInput');
    invitationInput.value = id;
}


//チャットに人が入ってきたときのレイアウトを変える関数
window.addEventListener('load', () => {
    var parentDiv = document.getElementById('video-play-field');

    var mutationCallback = function (mutationsList, observer) {
        for (var mutation of mutationsList) {
            if (mutation.type === 'childList') {
                const videoElement = document.querySelectorAll('video');
                if (videoElement.length >= 3) {
                    for (let elem of videoElement) {
                        elem.classList.add('over');
                    }
                } else {
                    for (let elem of videoElement) {
                        elem.classList.remove('over');
                    }
                }
            }
        }
    };

    var observer = new MutationObserver(mutationCallback);

    var config = { childList: true, subtree: true };

    observer.observe(parentDiv, config);
})

window.addEventListener('load', () => {
    var draggableDiv = document.getElementById('draggableDiv');
    var offsetX, offsetY, isDragging = false;

    draggableDiv.style.top = 660 + 'px';
    draggableDiv.style.left = (window.outerWidth - 144) / 2 + 'px';

    // タッチダウンイベント
    draggableDiv.addEventListener('touchstart', function (e) {
        isDragging = true;
        var touch = e.touches[0];
        offsetX = touch.clientX - draggableDiv.getBoundingClientRect().left;
        offsetY = touch.clientY - draggableDiv.getBoundingClientRect().top;
        draggableDiv.style.cursor = 'grabbing';
    });

    // タッチムーブイベント
    document.addEventListener('touchmove', function (e) {
        if (!isDragging) return;

        var touch = e.touches[0];
        var x = touch.clientX - offsetX;
        var y = touch.clientY - offsetY;

        draggableDiv.style.left =`${x}px`;
        draggableDiv.style.top = `calc(${y}px - 48px)`;
    });

    // タッチアップイベント
    document.addEventListener('touchend', function () {
        isDragging = false;
        draggableDiv.style.cursor = 'grab';
    });
})

function togglePage(element) {
    const elem = document.getElementById(element);
    elem.classList.toggle('openPage');
}


window.globalFunction = {};
window.globalFunction.toggleClickedState = toggleClickedState;
window.globalFunction.toggleChannelState = toggleChannelState;
window.globalFunction.videoChat = videoChat;

