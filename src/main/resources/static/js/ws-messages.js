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
    // loadConversations();


}
function sendMessage(msg) {
    stompClient.send('/message/sender-id' + loggedUserId + '/receiver-id' + recipientId, {}, JSON.stringify({
        content: msg,
        senderId: loggedUserId
    }));
    console.log("message gone");
}

function connectToConversations() {
    stompClient.connect({}, function () {
        console.log("Connected successfully!");
        // following the channel
        stompClient.subscribe('/topic/conversations-list-for-id' + loggedUserId, function (answer) {
            loadConversations(answer);
        });
        conversationRequest();
    });
}

function conversationRequest() {
    console.log("LOADING CONVERSATIONS...");
    stompClient.send('/conversation/conversation-for-id' + loggedUserId), {}, JSON.stringify({});
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

        console.log({div});
        div.on("click", function () {
            let socket = new SockJS("/message-web-socket");
            let stompClientForMsg = Stomp.over(socket);
            $("#conversation-container .active").removeClass("active");
            div.addClass("active");
            stompClientForMsg.connect({}, function (frame) {
                console.log("Connected: - " + frame);
                stompClientForMsg.subscribe('/topic/messages-list-for-conversation-id' + conversationsArrayElement.id, function (answer) {
                    loadMessages(answer);
                });
                messagesRequest(stompClientForMsg, conversationsArrayElement.id);
            }, function (error) {
                console.log(error);
            });
            // setTimeout(messagesRequest(stompClientForMsg), 1000)
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

function messagesRequest(stompClientForMsg, conversationId) {
    console.log("LOADING MESSAGES...");
    stompClientForMsg.send('/message/messages-for-conversation-id' + conversationId), {}, JSON.stringify({});
}

function loadMessages(answer) {
    console.log("INSIDE MESSAGES FUNCTION");
    console.log({answer});

    let messagesArray = JSON.parse(answer.body);
    let msgContainer = $("#messages-container");
    msgContainer.empty();

    for (const message of messagesArray) {
        let p = $('<p />', {text: message.content});
        console.log({message});
        p.addClass("alert"); //  alert-primary text-right
        if (message.sender.id == loggedUserId) {
            p.addClass("alert-primary text-right rounded-left ml-4");
        } else {
            p.addClass("alert-secondary text-left rounded-right mr-4");
        }
        msgContainer.append(p);
    }
}


function wsDisconnect() {
    stompClient.disconnect();
}