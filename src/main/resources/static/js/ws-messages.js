let stompClient;
let loggedUserId = $("#logged-user-id").val();
let recipientId = $("#recipient-id").val();

wsInit();

$("#send-btn").click(function () {
    let msg = $("#msg").val();

    console.log('sending message: ', msg);
    sendMessage(msg);
});

$("#nav-send-btn").click(function () {
    let msg = $("#msg-nav").val();

    console.log('sending message: ', msg);
    sendMessage(msg);
});

function wsInit() {
    let socket = new SockJS("/message-web-socket");
    stompClient = Stomp.over(socket);
    connectToConversations();
    setTimeout(loadConversations, 3000);
    // loadConversations();


}
function sendMessage(msg) {
    stompClient.send('/message/sender-id' + loggedUserId + '/receiver-id' + recipientId, {}, JSON.stringify({
        content: msg,
        senderId: loggedUserId
    }));
    console.log("message gone");
}

function loadConversations() {
    console.log("LOADING MESAGES...");
    stompClient.send('/conversation/conversation-for-id' + loggedUserId), {}, JSON.stringify({});
}

function connectToConversations() {
    stompClient.connect({}, function () {
        console.log("Connected successfully!");
        // following the channel
        stompClient.subscribe('/topic/conversations-list-for-id' + loggedUserId, function (answer) {
            console.log("INSIDE SUBSCRIBE FUNCTION");
            let conversationsArray = JSON.parse(answer.body);
            console.log({conversationsArray});


            // let msgArray = JSON.parse(answer.body);
            // console.log(msgArray);
            // let msgNavBlock = $("#nav-msg-block");
            // let $msgBlock = $("#msg-block");
            // msgNavBlock.empty();
            // $msgBlock.empty();
            // for (let msgObj of msgArray) {
            //     // console.log("MESSAGE CAME:", msgObj);
            //     let p = $('<p/>', {text: msgObj.content,}).addClass("alert alert-primary");
            //     p.prepend($('<b/>', {text: msgObj.sender.firstName + ": "}));
            //     if (msgObj.sender.id === loggedUserId || (msgObj.sender.id === recipientId && $("#send-btn").text() === 'Close')) {
            //         $msgBlock.append(p);
            //     } else {
            //         msgNavBlock.append(p);
            //     }
            // }
            // ;
        });
    });
}


function wsDisconnect() {
    stompClient.disconnect();
}