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
    let socket = new SockJS("/conversation-web-socket");
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
        stompClient.subscribe('/topic/conversations-list-for-id' + loggedUserId, loadConversations(answer));
    });
}

function loadConversations(answer) {
    console.log("INSIDE SUBSCRIBE FUNCTION");
    let conversationsArray = JSON.parse(answer.body);
    console.log({conversationsArray});

    let $conversationContainer = $("#conversation-container");
    $conversationContainer.empty();

    for (const conversationsArrayElement of conversationsArray) {
        console.log({conversationsArrayElement});
        let div = $('<div/>').addClass("list-group-item list-group-item-action row px-0 mx-0 rounded-0");
        // div.attr("id","conversation-" + conversationsArrayElement.id);
        let p = $('<p/>', {text: conversationsArrayElement.theLastMessage.sender.firstName,}).addClass("mx-2");

        div.append(p);
        $conversationContainer.append(div);

        div.onclick(function () {
            let socket = new SockJS("/message-web-socket");
            let stompClientForMsg = Stomp.over(socket);

            stompClientForMsg.subscribe('/topic/messages-for-conversation-id' + conversationsArrayElement.id, loadMessages(answer));
        });
    }

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

}

function loadMessages(answer) {
    console.log("INSIDE MESSAGES FUNCTION");

    let messagesArray = JSON.parse(answer.body);
    let msgContainer = $("#messages-container");
    msgContainer.empty();

    for (const message of messagesArray) {
        let p = $('<p />', {text: message.content});
        p.addClass("alert rounded-left ml-4"); //  alert-primary text-right
        if (message.sender.id === loggedUserId) {
            p.addClass("alert-primary text-right");
        } else {
            p.addClass("alert-secondary text-left");
        }
        msgContainer.append(p);
    }
}


function wsDisconnect() {
    stompClient.disconnect();
}