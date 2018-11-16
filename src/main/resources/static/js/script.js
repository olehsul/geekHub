$('#passwordId, #confirmPasswordId').on('keyup', function () {
    if ($('#passwordId').val() == $('#confirmPasswordId').val()) {
        $('#message').html('Matching').css('color', 'green');
    } else
        $('#message').html('Not Matching').css('color', 'red');
});