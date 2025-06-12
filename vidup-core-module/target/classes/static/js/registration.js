function handleRegistration() {
    const form = document.getElementById('registration_form');
    const formData = new FormData(form);


    console.log(formData.get('password'));
    console.log(formData.get('passwordConfirm'));
    console.log(formData);


    for (let [field, input] of formData.entries()) {
        console.log(field, input);

        if (!input) {
            if (field !== 'file') {
                alert(field + " cannot be empty");
                return false;
            }
        }

        if (field === 'email') {

            const isValid = validEmail(input);
            //  const isValid = validator.isEmail(input);
            console.log(isValid);

            if (!isValid) {
                alert("Invalid email address");
                return false;
            }
        }

    }

    const password = formData.get('password');
    const passConfirm = formData.get('passwordConfirm');


    if (password !== passConfirm) {
        alert("Passwords don't match!");
        return false;
    }

    if (password.length < 5) {

        alert("Password must be at least 5 characters");
        return false;
    }

    if (confirm("Ensure Your Email Address Is Correct\n " + "Without a valid Mail you cannot activate your account\n" + "Link to activate your account will be sent to this address") === false) {
        console.log("cancelled");
        return;
    }


    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/users/register', true);
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
    xhr.setRequestHeader("enctype", "multipart/form-data");


    console.log(formData);


    xhr.onload = function () {

        if (xhr.readyState === 4) {
            if (xhr.status === 201) {
                alert("Please check your Email for instructions to activate your Account ");
                return;
            }

            if (xhr.status === 500) {
                alert(xhr.responseText);
                return false;
            }

            if (xhr.status === 409) {
                alert(xhr.responseText);
                return false;
            }
        }
    }

    xhr.send(formData);


}

function validEmail(email) {
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const isValid = emailPattern.test(email);
    console.log(isValid);
    return isValid;
}