function updateUser() {
    const form = document.getElementById('update_user_form');
    const formData = new FormData(form);

    console.log(formData);

    const url = "/users/update";
    const xhr = new XMLHttpRequest();


    const password = formData.get('password');
    const passConfirm = formData.get('passwordConfirm');
    if (password !== passConfirm) {
        alert("Passwords don't match!");
        return false;
    }

    if (password && password.length < 5) {

        alert("Password must be at least 5 characters");
        return false;
    }

    const email = formData.get('email');
    if (email && !validEmail(email)) {
        alert("Invalid email address");
        return false;
    }


    xhr.open('PUT', url, true);
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
    xhr.setRequestHeader("enctype", "multipart/form-data");


    xhr.onreadystatechange = function () {

        console.log("xhr");
        if (xhr.readyState === 4) {
            console.log(xhr.status);
            console.log(xhr.responseText);

            if (xhr.status === 200) {
                alert("Profile Updated");
                location.reload();
            }
            if (xhr.status === 409 || xhr.status === 400 || xhr.status === 500) {
                alert(xhr.responseText);
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