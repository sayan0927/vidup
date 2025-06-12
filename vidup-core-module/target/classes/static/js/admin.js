function dismissReport(videoId) {
    const url = "/admin/reported/" + videoId + "/dismiss";
    var xhr = new XMLHttpRequest();
    xhr.open('DELETE', url, true);
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");

    var resp;

    xhr.onreadystatechange = function () {
        console.log(xhr.readyState + " " + xhr.status + " " + xhr.responseText);
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log("Dismissed successfully:");
                location.reload(true);
            } else {
                console.log("Failed to Delete", xhr.status, xhr.statusText);
            }
        }
    };

    xhr.send();
}

function deleteVideo(videoId) {
    const url = "/videos/" + videoId + "/delete";
    var xhr = new XMLHttpRequest();
    xhr.open('DELETE', url, true);
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");

    var resp;

    xhr.onreadystatechange = function () {
        console.log(xhr.readyState + " " + xhr.status + " " + xhr.responseText);
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log("Deleted successfully:");
                location.reload(true);
            } else {
                console.log("Failed to Delete", xhr.status, xhr.statusText);
            }
        }
    };

    xhr.send();
}










