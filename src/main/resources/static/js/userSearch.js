window.addEventListener('resize', function(event){
    locateSearchResults();
});

function locateSearchResults() {
    let searchInput = $('#userNameSurname')[0];
    let searchResults = $('#userSearchResultList')[0];
    // console.log(searchResults);
    // console.log('dsfdssssssssssssssssssssssssssssssssssssssssssss');
    // console.log(searchInput);
    // console.log('dsfdssssssssssssssssssssssssssssssssssssssssssss');
    let offsetTop = searchInput.offsetTop;
    let offsetLeft = searchInput.offsetLeft;
    let height = searchInput.offsetHeight;
    // console.log(height, offsetTop, offsetLeft);

    searchResults.style.position = "absolute";
    console.log(searchResults.style.top = (offsetTop + height) + "px");
    console.log(searchResults.style.left = offsetLeft + "px");
    // console.log(searchResults);

}

function searchUser() {

    let fullName = $("#userNameSurname").val().split(' ');

    let userName;
    let surname;
    if (fullName.length == 1) {
        userName = fullName[0];
        surname = "";
    } else if (fullName.length == 2) {
        userName = fullName[0];
        surname = fullName[1];
    } else {
        userName = "";
        surname = "";
    }


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
            locateSearchResults();
            console.log("succeed request!");
            let searchList = $('#userSearchResultList');
            let searchDataList = $('#usersDataList');
            // let users = [];
            $(".app").remove();
            let foundUsers = response;
            console.log(foundUsers);
            let it = 0;
            $.each(foundUsers, function (i, f) {
                it = i + 1;
                console.log(it);
                if (it == 5) {
                    searchList.append('<a class="list-group-item list-group-item-action list-group-item-warning app color-red" style="color: red; width: 100%"> Find All </a>');

                } else if (it < 5) {
                    searchList.append('<a class="list-group-item list-group-item-action list-group-item-secondary app" style="  width: 200px" href="id' + f.id + '">' + f.firstName + ' ' + f.lastName + '</a>');
                }
                // searchDataList.append('<option name="" class="app" onclick="goToUserPage(event)">' + f.firstName + ' ' + f.lastName + '</option>');

            })

        },
        error: function (err) {

            console.log(err);
            console.log("not succeed request!");
        }
    });

}
