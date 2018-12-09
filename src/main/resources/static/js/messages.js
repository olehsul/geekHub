// language=JQuery-CSS
$("#msg-dialog-btn").click(function () {
    if ($("#msg-dialog-btn").text() === 'Write message') {
        $("#msg-main-block").css('display', 'block');
        $("#msg-dialog-btn").html('Close');
    } else if ($("#msg-dialog-btn").text() === 'Close'){
        $("#msg-main-block").css('display', 'none');
        $("#msg-dialog-btn").html('Write message');
    }
});

function messagePopup() {
    
}

$("#dropdown-message-box").click(function () {

});