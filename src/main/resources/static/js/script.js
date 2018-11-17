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



function validate(e) {
    var fname = $("#fname").val();
    var lname = $("#lname").val();
    var email = $("#exampleDropdownFormEmail1").val();
    var password = $("#passwordId").val();
    var confirmPassword = $("#confirmPasswordId").val();

    if (firstNameValid(fname) == false) {
        console.log("false first name")
        e.preventDefault();
        }


    if (lastNameValid(lname) == false) {
        console.log("false first name")
        e.preventDefault();
    }

    if (emailValid(email) == false) {
        console.log("false email")
        e.preventDefault();
    }

    if (passwordValid(password) == false) {
        console.log("false password")
        e.preventDefault();
    }

    if (confirmPasswordValid(confirmPassword) == false) {
        console.log("false confirm password")
        e.preventDefault();
    }

}


function firstNameValid(name) {
    console.log("true first name")
    firstNameRegex = /^[a-zA-Zа-яА-Я'][a-zA-Zа-яА-Я-' ]+[a-zA-Zа-яА-Я']?$/u;
    return firstNameRegex.test(name);

}

function lastNameValid(name) {
    console.log("true last name")
    lastNameRegex = /^[a-zA-Zа-яА-Я'][a-zA-Zа-яА-Я-' ]+[a-zA-Zа-яА-Я']?$/u;
    return lastNameRegex.test(name);

}

function emailValid(email) {
    console.log("true email")
    emailRegex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    return emailRegex.test(email);

}


function passwordValid(password) {
    console.log("true password")
    passwordRegex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])([a-zA-Z0-9]{8})$/;
    return passwordRegex.test(password);

}

function confirmPasswordValid(password) {
    console.log("true confirm password")
    confirmPasswordRegex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])([a-zA-Z0-9]{8})$/;
    return confirmPasswordRegex.test(password);

}
















