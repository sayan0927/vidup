function addVideoToPlaylist(videoId) {
    const playListId = document.getElementById('playlist_id_select').value;

    console.log("to playlist " + playListId);

    const url = "/playlists/" + playListId + "/add/" + videoId;
    var xhr = new XMLHttpRequest();
    xhr.open('PUT', url, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");

    var resp;

    xhr.onreadystatechange = function () {
        console.log(xhr.readyState + " " + xhr.status + " " + xhr.responseText);
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                alert("Video Added To Selected Playlist");
            } else {
                console.log(xhr.status, xhr.statusText);

            }
        }
    };
    xhr.send();

}

async function handleSubscribe(creatorId) {

    var subscribedPrev = await subscribedAlready(creatorId);

    if (subscribedPrev == null) return;

    if (subscribedPrev) unsubscribe(creatorId); else subscribe(creatorId);

}

function downloadVideo(videoId) {
    const url = "/videos/" + videoId + "/download";
    window.open(url, "_blank");

}

function unsubscribe(creatorId) {
    const url = "/subscriptions/unsubscribe/" + creatorId;
    var xhr = new XMLHttpRequest();
    xhr.open('DELETE', url, true);
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");

    xhr.onload = function () {

        if (xhr.readyState === 4 && xhr.status === 200) {
            alert("You Unsubscribed From This User");
        }
    };

    xhr.send();
}

function subscribe(creatorId) {
    const url = "/subscriptions/subscribe/" + creatorId;
    var xhr = new XMLHttpRequest();
    xhr.open('POST', url, true);
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");

    xhr.onreadystatechange = function () {

        if (xhr.readyState === 4 && xhr.status === 200) {
            alert("Subscribed successfully");
        } else if (xhr.readyState === 4 && xhr.status === 406) {
            alert(xhr.responseText);
        } else if (xhr.readyState === 4 && xhr.status === 401) {
            alert("Please Login First");
        }

    };

    xhr.send();
}

function subscribedAlready(creatorId) {
    const url = "/subscriptions/subscribed_prev/" + creatorId;
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    console.log("fks")
    xhr.onload = function () {

        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var subscribedPrev = JSON.parse(xhr.responseText);
                if (!subscribedPrev) subscribe(creatorId); else unsubscribe(creatorId);
            } else if (xhr.status === 401) {
                alert("Please Login First");
            }

        } else return null;
    };
    xhr.send();
}


// Function to populate select element with playlistNames
function populateSelectElement(selectElement, playlists) {
    // Clear existing options
    selectElement.innerHTML = '';


    // Populate select element with fetched countries
    playlists.forEach(playlist => {
        const option = document.createElement('option');
        const parts = playlist.split("-");
        option.value = parts[0];
        option.textContent = parts[1];
        selectElement.appendChild(option);
    });
}


function fetchPlayLists() {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        const url = "/playlists/my";
        xhr.open('GET', url, true);

        xhr.onload = function () {
            if (xhr.status >= 200 && xhr.status < 300) {

                const playlistsJSon = JSON.parse(xhr.responseText);
                const playlists = extractIdentifierFromPlaylists(playlistsJSon);
                resolve(playlists);
            } else {
                reject(new Error('Request failed with status:', xhr.status));
            }
        };

        xhr.onerror = function () {
            reject(new Error('Request failed'));
        };

        xhr.send();
    });
}


function extractIdentifierFromPlaylists(playlists) {
    const identifierSet = new Set(); // Use a Set to avoid duplicate countries
    playlists.forEach(playlistDTO => {

        console.log(playlistDTO);
        const playlist = playlistDTO.playlist;
        identifierSet.add(playlist.id + '-' + playlist.name);
        if (playlist.name) {

            // identifierSet.add(playlist.id+'-'+playlist.name);
        }
    });
    return Array.from(identifierSet); // Convert Set back to Array
}


function toggleAddPlaylistModal() {
    const modal = document.getElementById('add_to_playlist');
    const selectElement = document.querySelector('#add_to_playlist select');
    const addButton = document.getElementById('addbutton');

    // Toggle the 'hidden' class on the modal to show/hide it
    modal.classList.toggle('hidden');

    // If modal is opened, fetch playlists
    if (!modal.classList.contains('hidden')) {
        fetchPlayLists().then(playlists => {

            //populate select box
            populateSelectElement(selectElement, playlists);

            // Add event listener to select element to check selected value
            selectElement.addEventListener('change', function () {
                // Disable add button if selected value is empty, otherwise enable it
                addButton.disabled = selectElement.value === '';
            });

            // Check initial value
            addButton.disabled = selectElement.value === '';
        }).catch(error => {
            console.error('Error fetching playlists:', error);
        });
    }
}

function reportVideo(videoId) {

    console.log("doing things");
    console.log(videoId);

    const url = "/videos/" + videoId + "/report";
    var params = "";

    console.log(url);
    var xhr = new XMLHttpRequest();
    xhr.open('POST', url, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");

    var resp;

    xhr.onreadystatechange = function () {
        console.log(xhr.readyState + " " + xhr.status + " " + xhr.responseText);
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log("Video reported successfully:", xhr.responseText);
                alert("Video Has Been Reported Successfully");
            } else if (xhr.status === 401) {
                alert("Please Login First");
            }
        }
    };

    var params = "reason=" + encodeURIComponent("reason");
    xhr.send(params);
}

function changeLanguage(videoId) {


    var uid = document.getElementById('data').getAttribute('data-currentUserId') || 'defaultValue';
    var session_token = document.getElementById('data').getAttribute('data-sessiontoken') || 'defaultValue';
    var url = document.getElementById('data').getAttribute('data-source_url') || 'defaultValue';
    var e = document.getElementById("lang_select");
    var lang = e.options[e.selectedIndex].value;


    let new_src = "http://" + url + "/" + videoId + "/" + lang + "/" + session_token + "/" + uid;

    console.log(new_src + " src is");

    console.log(videoId);

    var videoElements = document.querySelectorAll('.video-js');
    console.log("ele are " + videoElements.length);
    var target;
    // Loop through each video element
    videoElements.forEach(function (videoElement) {
        // Get the Video.js player instance associated with the video element
        console.log(videoElement);
        var player = videojs(videoElement.id);
        if (player.id() == videoId) {
            target = player;
        }
    });

    var currTime = target.currentTime();
    target.src({type: "video/mp4", src: new_src});
    target.currentTime(currTime);
}

function postComment(videoId) {
    console.log("doing things");
    var commentText = document.getElementById("commentBox").value;
    const url = "/videos/" + videoId + "/comment";
    var params = commentText;

    var xhr = new XMLHttpRequest();
    xhr.open('POST', url, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");

    var resp;

    xhr.onreadystatechange = function () {
        console.log(xhr.readyState + " " + xhr.status + " " + xhr.responseText);
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log("Commented successfully:", JSON.parse(xhr.responseText));
                updateCommentsDisplay(JSON.parse(xhr.responseText));
            } else if (xhr.status === 401) {
                alert("Please Login First");
            }
        }
    };

    var params = "commentText=" + encodeURIComponent(commentText);
    xhr.send(params);
}

function convertLocalDateTime(localDateTimeString) {
    // Parse the LocalDateTime string
    const date = new Date(localDateTimeString);

    // Extract the day, month, and year
    const day = String(date.getDate()).padStart(2, '0');
    const year = String(date.getFullYear()).slice(-2);

    // Month names
    const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    const month = monthNames[date.getMonth()];

    // Format as dd/Mon/yy
    return `${day}/${month}/${year}`;
}

function updateCommentsDisplay(newComment) {
    console.log("cmtsdisplay");
    console.log(newComment);
    var newCommentHTML = document.createElement('div');
    var cmtDate = newComment.commentDateTime
    var formattedDate = convertLocalDateTime(cmtDate);
    var img_src = "/users/permitted/" + newComment.commenter.id + "/profile_img";
    console.log(img_src + " src");

    console.log((formattedDate));


    newCommentHTML.className = 'flex items-center space-x-4 mb-10';
    newCommentHTML.innerHTML = `
                    <p>
                        <img src=   ${img_src} 
                             style="border-radius: 100%; object-fit: cover; width: 50px; height: 50px;"/>
                    </p>
                    <div>
                        <p>
                            <span class="font-serif" style="font-size:20px;">${newComment.commenter.userName}</span>
                            <span style="font-size:10px; margin-left: 10px">${formattedDate} </span>
                        </p>
                        <p class="text-black font-mono">${newComment.commentText}</p>
                    </div>
                    <br><br>
                `;

    // Append the new comment to the displayComments div
    // Prepend the new comment to the displayComments div
    var displayComments = document.getElementById('displayComments');
    if (displayComments.firstChild) {
        displayComments.insertBefore(newCommentHTML, displayComments.firstChild);
    } else {
        displayComments.appendChild(newCommentHTML);
    }

    // Clear the input field
    document.getElementById('commentBox').value = '';
}

function handleLike(videoId) {

    const checkUrl = "/videos/" + videoId + "/reactions/liked_prev";
    var xhr = new XMLHttpRequest();
    xhr.open('GET', checkUrl, true);


    xhr.onload = function () {

        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                {
                    var likedAlready = JSON.parse(xhr.responseText);
                    if (!likedAlready) reactOnVideo("like", videoId, 'POST'); else reactOnVideo("neutral", videoId, 'PUT');
                }
            } else if (xhr.status === 401) {
                alert("Please Login First");
            }


        } else {
            console.error('Request failed with status', xhr.status);
        }
    };
    xhr.send();
}

function handleDislike(videoId) {

    const checkUrl = "/videos/" + videoId + "/reactions/disliked_prev";
    var xhr = new XMLHttpRequest();
    xhr.open('GET', checkUrl, true);


    xhr.onload = function () {
        console.log(xhr.readyState + " " + xhr.status + " " + xhr.responseText + " kak");


        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var dislikedAlready = JSON.parse(xhr.responseText);
                if (!dislikedAlready) reactOnVideo("dislike", videoId, 'POST'); else reactOnVideo("neutral", videoId, 'PUT');
            } else if (xhr.status === 401) {
                alert("Please Login First");
            }
        }


    };
    xhr.send();
}


function reactOnVideo(reaction, videoId, method) {

    const url = "/videos/" + videoId + "/reactions/react/" + reaction;
    var xhr = new XMLHttpRequest();
    xhr.open(method, url, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");

    var resp;

    xhr.onreadystatechange = function () {
        console.log(xhr.readyState + " " + xhr.status + " " + xhr.responseText);
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                updateLikeAndDislikeButtonCount(videoId);
            } else {
                console.log("Failed to Like:", xhr.status, xhr.statusText);
            }
        }
    };
    xhr.send();

}


function likeVideo(videoId) {
    const likeUrl = "/videos/" + videoId + "/reactions/react/like/";
    var xhrLike = new XMLHttpRequest();
    xhrLike.open('POST', likeUrl, true);
    xhrLike.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhrLike.setRequestHeader("Access-Control-Allow-Origin", "*");

    var resp;

    xhrLike.onreadystatechange = function () {
        console.log(xhrLike.readyState + " " + xhrLike.status + " " + xhrLike.responseText);
        if (xhrLike.readyState === 4) {
            if (xhrLike.status === 200) {
                console.log("Video Liked");
                updateLikeAndDislikeButtonCount(videoId);
            } else {
                console.log("Failed to Like:", xhrLike.status, xhrLike.statusText);
            }
        }
    };
    xhrLike.send();
}

function updateLikeAndDislikeButtonCount(videoId) {

    console.log("will update");
    const url = "/videos/" + videoId + "/reactions/count";
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);

    xhr.onload = function () {
        if (xhr.status >= 200 && xhr.status < 300) {
            var jsonResponse = JSON.parse(xhr.responseText);
            console.log(jsonResponse);
            console.log(jsonResponse.likes);
            console.log(jsonResponse.dislikes);
            document.getElementById("likebutton").textContent = jsonResponse.likes;
            document.getElementById("dislikebutton").textContent = jsonResponse.dislikes;

        } else {
            console.error('Request failed with status', xhr.status);
        }
    };

    xhr.onerror = function () {
        console.error('Request failed');
    };
    xhr.send();
}



