let stompClient;
let loggedUserId = $("#logged-user-id").val();
let recipientId;
let selectedConversationId;

wsInit();

$("#send-message-btn").click(function () {
    let input = $("#message-input");
    let msg = input.val();
    input.val('');

    console.log({msg});

    sendMessage(msg);
});

function sendMessage(msg) {
    stompClient.send('/message/private-message', {}, JSON.stringify(
        {
            content: msg,
            senderId: loggedUserId,
            recipientId: recipientId,
            conversationId: selectedConversationId
        }
    ));
}

$("#send-btn").click(function () {
    let msg = $("#msg").val();

    console.log('sending message: ', msg);
    sendSimpleMessage(msg);
});

$("#nav-send-btn").click(function () {
    let msg = $("#msg-nav").val();

    console.log('sending message: ', msg);
    sendSimpleMessage(msg);
});

function wsInit() {
    let socket = new SockJS("/conversation-web-socket");
    stompClient = Stomp.over(socket);
    connectToConversations();
    // loadConversations();


}

function sendSimpleMessage(msg) {
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
        stompClient.subscribe('/topic/conversation-for-id' + loggedUserId, function (answer) {
            updateConversation(answer);
        });
    });
}

function loadConversations(answer) {
    console.log("INSIDE LOAD CONVERSATIONS FUNCTION");
    let conversationsArray = JSON.parse(answer.body);
    console.log({conversationsArray});

    $("#conversation-container").empty();

    for (const conversationsArrayElement of conversationsArray) {
        displayConversation(conversationsArrayElement, false);
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

function displayConversation(conversation, isNew) {
    console.log({conversationsArrayElement: conversation});
    let div = $('<div/>').addClass("list-group-item list-group-item-action row px-0 mx-0 rounded-0");
    div.attr("id", "conversation-" + conversation.id);

    let friend = conversation.users[0].id != loggedUserId ? conversation.users[0] : conversation.users[1];

    let p = $('<p/>', {text: conversation.theLastMessage != null ? conversation.theLastMessage.content : ''}).addClass("mx-2 px-2 py-0");
    p.prepend('<b>' + friend.firstName + " " + friend.lastName + ": " + '</b><br />');

    div.append(p);
    if (isNew) {
        $("#conversation-container").prepend(div);
    } else {
        $("#conversation-container").append(div);
    }

    console.log({div});
    div.on("click", function () {
        $("#messages-header").html('<a href="/id' + friend.id + '">' + friend.firstName + ' ' + friend.lastName + '</a>');

        let socket = new SockJS("/message-web-socket");
        let stompClientForMsg = Stomp.over(socket);
        $("#conversation-container .active").removeClass("active");
        div.addClass("active");

        selectedConversationId = conversation.id;

        recipientId = friend.id;

        stompClientForMsg.connect({}, function () {
            console.log("Connected to: MessageWebSocket");
            stompClientForMsg.subscribe('/topic/messages-list-for-conversation-id' + conversation.id, function (answer) {
                if (selectedConversationId == conversation.id)
                    loadMessages(answer);
            });
            messagesRequest(stompClientForMsg, conversation.id);
        }, function (error) {
            console.log(error);
        });
    });

}

function updateConversation(answer) {
    let newConversation = JSON.parse(answer.body);

    if (selectedConversationId != newConversation.id) {
        $("#conversation-" + newConversation.id).remove();
        displayConversation(newConversation, true);
    }
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
        p.addClass("alert");
        if (message.sender.id == loggedUserId) {
            p.addClass("alert-primary text-right rounded-left ml-4");
        } else {
            p.addClass("alert-secondary text-left rounded-right mr-4");
        }
        msgContainer.append(p);
    }
}

function conversationRequest() {
    console.log("LOADING CONVERSATIONS...");
    stompClient.send('/conversation/conversations-request-for-id' + loggedUserId), {}, JSON.stringify({});
}

function messagesRequest(stompClientForMsg, conversationId) {
    console.log("LOADING MESSAGES...");
    stompClientForMsg.send('/message/messages-for-conversation-id' + conversationId), {}, JSON.stringify({});
}

function wsDisconnect() {
    stompClient.disconnect();
}