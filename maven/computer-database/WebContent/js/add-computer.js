//Bind the event handler to the "submit" JavaScript event
var nameValid = false;
var introducedValid = true;
var discontinuedValid = true;
checkFormValid();

$('#computerName').keyup(function(){
	nameValid = $('#computerName').val().length != 0;
	setSuccessClass($('#computerName'), nameValid);
	checkFormValid();
});

$('#introduced').keyup(function(){
	if($('#introduced').val().length != 0){
		introducedValid = isValidDate($('#introduced').val());
	}else{
		introducedValid = true;
	}
	setSuccessClass($('#introduced'), introducedValid);
	checkFormValid();
});

$('#discontinued').keyup(function(){
	if($('#discontinued').val().length != 0){
		introducedValid = isValidDate($('#discontinued').val());
	}else{
		introducedValid = true;
	}
	setSuccessClass($('#discontinued'), introducedValid);
	checkFormValid();
});

function isValidDate(dateAsString) {
	var parts = dateAsString.split('-');
	var date = new Date(parts[0], parts[1] - 1, parts[2]);
	return date && (date.getMonth() + 1) == parts[1];
}

function checkFormValid(){
	if(nameValid && introducedValid && discontinuedValid){
		 $('#submitButton').prop('disabled', false);
	}else{
		 $('#submitButton').prop('disabled', true);
	}
}

function setSuccessClass(element, success){
	if(success){
		$(element).parent().addClass('has-success').removeClass('has-error');
		$(element).parent().find('div').hide();
	}else{
		$(element).parent().addClass('has-error').removeClass('has-success');
		$(element).parent().find('div').show();
	}
}

function checkFields(){
	console.log("checking");
	nameValid = $('#computerName').val().length != 0;
	setSuccessClass($('#computerName'), nameValid);
	checkFormValid();
}

