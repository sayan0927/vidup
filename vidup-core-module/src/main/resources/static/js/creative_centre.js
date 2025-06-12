var currentUserId = document.getElementById('data').getAttribute('data-currentUserId') || 'defaultValue';


function main() {
    document.addEventListener('DOMContentLoaded', function () {
        const fileInput = document.getElementById('file');
        const descriptionInput = document.getElementById('description');
        const videoNameInput = document.getElementById('video_name');

        fileInput.addEventListener('change', function () {
            const selectedFile = fileInput.files[0];
            if (selectedFile) {
                const fileNameWithoutExtension = selectedFile.name.replace(/\.[^/.]+$/, ''); // Remove file extension
                // Update description with the filename (without extension)
                descriptionInput.value = `Description of : ${fileNameWithoutExtension}`;

                // Update video label with the filename (without extension)
                videoNameInput.value = fileNameWithoutExtension;

                // You can also update the label placeholder text if needed
                const videoLabelPlaceholder = document.querySelector('[for="video_name"]');
                if (videoLabelPlaceholder) {
                    videoLabelPlaceholder.classList.add('peer-placeholder-shown');
                }
            } else {
                // Reset description and video label if no file is selected
                descriptionInput.value = '';
                videoNameInput.value = '';
            }
        });
    });
}

main();


function uploadVideo() {
    const form = document.getElementById('video_upload_form');
    const formData = new FormData(form);

    // Validate file input
    const fileInput = document.getElementById('file');
    const videoNameInput = document.getElementById('video_name');

    console.log(formData)

    if (!fileInput.files.length || !videoNameInput.value) {
        alert('Please select a video file and enter a video name.');
        return;
    }

    // Create XMLHttpRequest object
    const xhr = new XMLHttpRequest();
    // const url = "http://localhost:8000/videos/upload";

    const url = "/videos/upload";

    console.log("url " + url);
    xhr.open('POST', url, true);
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
    xhr.setRequestHeader("enctype", "multipart/form-data");
    console.log("uploading");
    // Set up a handler for when the request finishes
    xhr.onreadystatechange = function () {
        console.log(xhr.readyState, xhr.status + " " + xhr.statusText);

        if (xhr.readyState === 4) {
            if (xhr.status === 200 || xhr.status === 201) {
                alert('Video Was Uploaded.\n You Will be Notified after Video is processed and made ready');
                // location.reload();
            } else alert('An error occurred during the video upload. Please try again.');
        }
    };

    // Send the data
    xhr.send(formData);
}

