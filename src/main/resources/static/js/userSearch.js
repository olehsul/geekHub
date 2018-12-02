function searchUser(event) {

    let fullName = $("#userNameSurname").val().split(' ');

    let userName;
    let surname;
    if (fullName.length == 1) {
        userName = fullName[0]
        surname = "";
    } else if (fullName.length == 2) {
        userName = fullName[0];
        surname = fullName[1];
    } else {
        userName = "";
        surname = "";
    }

    // let userName = fullName[0] + "";
    // let surname = fullName[1] + "";

    console.log(name, surname);

    let stringURL = '/findUser';
    let dataJSON = JSON.stringify({firstName: userName, lastName: surname});
    console.log(dataJSON);

    $.ajax({
        url: stringURL,
        type: 'POST',
        contentType: 'application/json',
        data: dataJSON,
        success: function (response) {
            console.log("succeed request!");
            console.log(response);
            let searchList = $('#userSearchResultList');
            let searchDataList = $('#usersDataList');
            let users = [];
            $(".app").remove();
            let foundUsers = response;
            $.each(foundUsers, function (i, f) {
                console.log(f);
                console.log('=-==-=-=--=-=-=');
                users.push([f.id, f.firstName, f.lastName]);
                console.log(f.lastName);
                searchList.append('<li class="app"><a href="id' + f.id + '">' + f.firstName + ' ' + f.lastName + '</a></li>');
                searchDataList.append('<option class="app"><a href="id' + f.id + '">' + f.firstName + ' ' + f.lastName + '</a></option>');

            })

        },
        error: function (err) {

            console.log(err);
            console.log("not succeed request!");
        }
    });

}
