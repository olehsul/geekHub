// language=JQuery-CSS
// $("#msg-dialog-btn").click(function () {
//     if ($("#msg-dialog-btn").text() === 'Write message') {
//         $("#msg-main-block").css('display', 'block');
//         $("#msg-dialog-btn").html('Close');
//     } else if ($("#msg-dialog-btn").text() === 'Close'){
//         $("#msg-main-block").css('display', 'none');
//         $("#msg-dialog-btn").html('Write message');
//     }
//
// });

function messagePopup() {
    
}

function createConversationOrMessage(id) {
    console.log(id);
    let friendId = id;

    let stringURL = '/createConversationOrMessage';
    let dataJSON = JSON.stringify({friendId});

    $.ajax({
        url: stringURL,
        type: 'POST',
        contentType: 'application/json',
        data: dataJSON,
        success: function (response) {
            console.log("succeed request!");
            console.log(response);
            window.location.replace("/messages");
        },
        error: function (err) {

            console.log(err);
            console.log("not succeed request!");
        }
    });
}