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

function validate(e) {
    var fname = $("#fname").val();
    var lname = $("#lname").val();
    var email1 = $("#exampleDropdownFormEmail1").val();
    var password = $("#passwordId").val();
    var confirmPassword = $("#confirmPasswordId").val();

    if (nameValid(fname) == false) {
        console.log("sgsewefw")
        e.preventDefault();


        }

}


function nameValid(name) {
    console.log("sdgs")
    nemeRegex = /[A-Z][a-zA-Z][^#&<>\"~;$^%{}?]{1,20}$/;
    return nemeRegex.test(name);

}















