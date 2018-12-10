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

function sendMessage(msg) {
    stompClient.send('/message/sender-id' + loggedUserId + '/receiver-id' + recipientId, {}, JSON.stringify({
        content: msg,
        senderId: loggedUserId
    }));
    console.log("message gone");
}

function wsInit() {
    let socket = new SockJS("/message-web-socket");
    stompClient = Stomp.over(socket);
// connecting
    stompClient.connect({}, function () {
        console.log("Connected successfully!");
        // following the channel
        stompClient.subscribe('/topic/msg-answer/id' + loggedUserId, function (answer) {
            let msgArray = JSON.parse(answer.body);
            console.log(msgArray);
            $("#nav-msg-block").empty();
            $("#msg-block").empty();
            for (let msgObj of msgArray) {
                // console.log("MESSAGE CAME:", msgObj);
                let p = $('<p/>', {text: msgObj.content,}).addClass("alert alert-primary");
                p.prepend($('<b/>', {text: msgObj.sender.firstName + ": "}));
                if (msgObj.sender.id === loggedUserId || (msgObj.sender.id === recipientId && $("#send-btn").text() === 'Close')) {
                    $("#msg-block").append(p);
                } else {
                    $("#nav-msg-block").append(p);
                }
            }
            ;
        });
    });
}

function wsDisconnect() {
    stompClient.disconnect();
}