$('#passwordId, #confirmPasswordId').on(function () {
    if ($('#passwordId').val() == $('#confirmPasswordId').val()) {
        $('#message').html('Matching').css('color', 'green');
    } else
        $('#message').html('Not Matching').css('color', 'red');
});


/*  $('#fname').on('fname', function () {
      var varName = /[A-Z][a-zA-Z][^#&<>\"~;$^%{}?]{1,20}$/
          return varName.test(String(name))})
      }*/

/* $('#exampleDropdownFormEmail1').on('exampleDropdownFormEmail1', function () {
     var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
     return re.test(String(email).toLowerCase())})*/

function validate(event) {
    let fname = $("#fname").val();
    let lname = $("#lname").val();
    let email = $("#exampleDropdownFormEmail1").val();
    let password = $("#passwordId").val();
    let confirmPassword = $("#confirmPasswordId").val();

    if (
        (validateName(fname) == false)
        || (validateName(lname) == false)
        || (validateEmail(email) == false)
        || (validatePassword(password) == false)
        || (isPasswordMatches(password, confirmPassword) == false)
    ) {
        console.log('prevent def');
        event.preventDefault();
    }else console.log('data are valid');

}

function validateNewPassword(event) {
    var password = $("#passwordId").val();
    var confirmPassword = $("#confirmPasswordId").val();

    if ((validatePassword(password) == false)
        || (isPasswordMatches(password, confirmPassword) == false)
    ) {
        console.log('prevent def');
        event.preventDefault();
    }else console.log('data are valid');
}


function validateName(name) {
    const regexName = /^[a-zA-Z ]{2,30}$/;

    if (regexName.test(name)) {
        console.log('name is valid');
        // $("#fnamemassage").html("<span style='color: transparent'>write a valid name!</span><br />");
        return true;
    }
    else {
        // $("#fnamemassage").html("<font style='color: red'>write a valid name!</font><br />");
        //alert("You have entered an invalid name");
        return false;
    }
}

function validateSingleEmail(event) {
    let email = $("#mail").val();
    if (validateEmail(email) == false) {console.log('prevent def');
        event.preventDefault();
    }else console.log('data are valid');
}

function validateEmail(mail) {
    const regexEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

    if (regexEmail.test(mail)) {
        console.log('email is valid');
        // $("#emailmassage").html("<span style='color: transparent'>write a valid email!</span><br />");
        return (true)

    } else {
        //alert("You have entered an invalid email address!");
        // $("#emailmassage").html("<span style='color: red'>write a valid email!</span><br />");
        console.log('email is invalid');
        return (false)

    }
}

function isPasswordMatches(password, rePassword) {
    $("#repasswordmassage").html("<span style='color: transparent'>password doesn`t matches</span><br />");
    if (password != rePassword) {
        $("#repasswordmassage").html("<span style='color: red'>password doesn`t matches</span><br />");
        console.log('password not matches');
        return (false);
    }
    return (true);
}

function validatePassword(password) {
    const regexPassword = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/;

    if (regexPassword.test(password)) {
        console.log('password is valid');
        // $("#passwordmassage").html("<span style='color: transparent'>should contain at least one digit, lower case, upper case!</span>");
        return true;
    }
    else {
        // alert("You have entered an invalid Password");
        // $("#passwordmassage").html("<span style='color: red'>should contain at least one digit, lower case, upper case!</span>");
        console.log('password is invalid');
        return false;
    }
}