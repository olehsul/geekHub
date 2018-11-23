function validateNewPassword(event) {
    let recoveryPassword = $("#recoveryPasswordId").val();
    let recoveryConfirmPassword = $("#recoveryConfirmPasswordId").val();

    if (validatePassword(recoveryPassword) == false) {
        $('#recoveryPasswordId').val("").attr("placeholder", "wrong password").css('border-color', 'red').addClass("placeholderRed");
    }

    if (recoveryPassword !== recoveryConfirmPassword) {
        event.preventDefault();
        $('#recoveryConfirmPasswordId').val("").attr("placeholder", "password is not match").css('border-color', 'red').addClass("placeholderRed");

    }
    else console.log('password is mach');

    if ((validatePassword(recoveryPassword) == false)
        || (isPasswordMatches(recoveryPassword, recoveryConfirmPassword) == false)
        || (validatePassword(recoveryPassword) == false)
        || (validatePassword(recoveryConfirmPassword) == false)
    ) {
        console.log('prevent def');
        event.preventDefault();
    } else console.log('data are valid');
}

function validate(event) {
    let fname = $("#fname").val();
    let lname = $("#lname").val();
    let email = $("#exampleDropdownFormEmail1").val();
    let password = $("#passwordId").val();
    let confirmPassword = $("#confirmPasswordId").val();

    if (password !== confirmPassword) {
        event.preventDefault();
        $('#confirmPasswordId').val("").attr("placeholder", "password is not mach").css('border-color', 'red').addClass("placeholderRed");
    }
    else console.log('password is mach');

    if (validateFirstName(fname) == false) {
        $('#fname').val("").attr("placeholder", "wrong first name").css('border-color', 'red').addClass("placeholderRed");
    }

    if (validateLastName(lname) == false) {
        $('#lname').val("").attr("placeholder", "wrong last name").css('border-color', 'red').addClass("placeholderRed");
    }

    if (validateEmail(email) == false) {
        $('#exampleDropdownFormEmail1').val("").attr("placeholder", "wrong email").css('border-color', 'red').addClass("placeholderRed");
    }

    if (validatePassword(password) == false) {
        $('#passwordId').val("").attr("placeholder", "wrong password").css('border-color', 'red').addClass("placeholderRed");
    }

    if (
        (validateFirstName(fname) == false)
        || (validateLastName(lname) == false)
        || (validateEmail(email) == false)
        || (validatePassword(password) == false)
        || (isPasswordMatches(password, confirmPassword) == false)
    ) {
        console.log('prevent def');

        event.preventDefault();
    } else console.log('data are valid');
}

function validateFirstName(name) {
    //   const regexNameLatine = /^[a-zA-Z ]{2,30}$/;
    const regexName = /^[a-zA-Zа-яА-Я'][a-zA-Zа-яА-Я-' ]+[a-zA-Zа-яА-Я']{2,30}?$/u;

    if (regexName.test(name)) {
        console.log('name is valid');
        //    $("#errorFirstNameMessaage").html("<span style='color: transparent'>write a valid name!</span><br />");
        return true;
    }
    else {
        //   $("#fname").placeholder="error";
        //alert("You have entered an invalid name");
        return false;
    }
}

function validateLastName(name) {
    //const regexName = /^[a-zA-Z ]{2,30}$/;
    const regexName = /^[a-zA-Zа-яА-Я'][a-zA-Zа-яА-Я-' ]+[a-zA-Zа-яА-Я']{2,30}?$/u;
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
    let recoveryEmail = $("#mail").val();

    if (validateEmail(recoveryEmail) == false) {
        $('#mail').val("").attr("placeholder", "wrong email").css('border-color', 'red').addClass("placeholderRed");
    }


    if (validateEmail(recoveryEmail) == false) {
        console.log('prevent def');
        event.preventDefault();
    } else console.log('data are valid');
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
    $("#repasswordmassage").html("<span style='color: 'transparent'>password doesn`t matches</span><br />");
    if (password != rePassword) {
        $("#repasswordmassage").html("<span style='color: 'red'>password doesn`t matches</span><br />");
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

