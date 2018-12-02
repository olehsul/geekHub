$("#btn-send-friend-request").click(function () {

    let userPageId = $("#friend-requested-id").val();
    let loggedUserId = $("#user-requesting-id").val();
    console.log(userPageId, loggedUserId);

    // let stringURL = "@{'/friend-request-{friendId}-from-{userId}'(friendId=${userPage.id}, userId=${loggedUser.id})}";
    // let stringURL = '/friend-request-' + loggedUserId + '-from-' + userPageId;
    let stringURL = '/friend-request';
    let dataJSON = JSON.stringify({friendId: userPageId, userId: loggedUserId});
    console.log(dataJSON);

    $.ajax({
        url: stringURL,
        type: 'POST',
        contentType: 'application/json',
        data: dataJSON,
        success: function (response) {
            console.log("succeed request!");
            console.log(response);
        },
        error: function (err) {
            console.log(err);
            console.log("not succeed request!");
        }
    });
});

function acceptFriendRequest(id) {
    console.log(id);
    let friendId = id;

    let stringURL = '/acceptFriendRequest';
    let dataJSON = JSON.stringify({friendId});

    $.ajax({
        url: stringURL,
        type: 'POST',
        contentType: 'application/json',
        data: dataJSON,
        success: function (response) {
            console.log("succeed request!");
            console.log(response);
        },
        error: function (err) {
            console.log(err);
            console.log("not succeed request!");
        }
    });
}


function deleteFriend(id) {

    console.log(id);
    let friendId = id;

    let stringURL = '/deleteFriend';
    let dataJSON = JSON.stringify({friendId});

    $.ajax({
        url: stringURL,
        type: 'POST',
        contentType: 'application/json',
        data: dataJSON,
        success: function (response) {
            console.log("succeed request!");
            console.log(response);
        },
        error: function (err) {
            console.log(err);
            console.log("not succeed request!");
        }
    });
}