var order = document.getElementById('details').getAttribute('data-view_order') || 'defaultValue';

order = order.replace("[", "");
order = order.replace("]", "");
order = order.replace(" ", "");

order = order.split(",");


var n = order.length;
var nextVid = new Map();

for (let i = 0; i < n; i++) {
    let curr = order[i];
    let next = order[i];

    if ((i + 1) == n) next = order[0]; else next = order[i + 1];

    nextVid.set(parseInt(curr), parseInt(next));

}

function deleteVideoFromPlaylist(videoId, playlistId) {
    console.log(playlistId);
    console.log(videoId);

    const url = "/playlists/" + playlistId + "/delete/" + videoId;
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


function playFromStart() {
    const videos = document.querySelectorAll('.video-js');

    videos.forEach(v => {
        let player = videojs(v.id);

        console.log(player);
        player.pause();
    });

    const first = (order[0]);
    let player = videojs(first);
    player.currentTime(0);
    startPlaying(player);

}

function playNext() {
    const videos = document.querySelectorAll('.video-js');
    console.log(videos);


    for (let i = 0; i < videos.length; i++) {
        let v = videos[i];

        var p = null;
        console.log(v);
        console.log(v.id);

        p = videojs(v.id);
        console.log(p);

        console.log(p.id());

        console.log(p.paused());

        if (!p.paused()) {
            let currId = p.id();

            let nextId = nextVid.get(parseInt(currId));

            console.log(currId + " cur");
            console.log(nextId);

            p.pause();
            console.log("next is " + nextId);
            let newp = videojs(nextId.toString());
            startPlaying(newp);

            break;
        }
    }
}


function onClick(videoId) {
    const videos = document.querySelectorAll('.video-js');

    videos.forEach(v => {
        var player = videojs(v.id);
        if (player.id() != videoId) player.pause();
    });

    console.log(videoId);
    let player = videojs(parseInt(videoId));
    startPlaying(player);

}

function startPlaying(vidplayer) {
    vidplayer.play()
    var vid = vidplayer.id();

    //  highlightVideoName(vid);

    highlightTableRow(vid);


}

function highlightTableRow(vid) {
    var rows = document.querySelectorAll('tr');
    for (var i = 0; i < rows.length; i++) {

        var row = rows[i];
        var rId = row.getAttribute('id');
        var expectedId = "row-" + vid;

        if (rId == expectedId) {
            // HIGHLIGHT the row
            row.classList.remove('bg-white', 'border-b', 'dark:bg-gray-800', 'dark:border-gray-700', 'hover:bg-gray-50', 'dark:hover:bg-gray-600');
            row.classList.add('bg-slate-300', 'border-b', 'dark:bg-gray-800', 'dark:border-gray-700', 'hover:bg-gray-50', 'dark:hover:bg-gray-600');

        } else {
            row.classList.remove('bg-white', 'border-b', 'dark:bg-gray-800', 'dark:border-gray-700', 'hover:bg-gray-50', 'dark:hover:bg-gray-600');
            row.classList.remove('bg-slate-300', 'border-b', 'dark:bg-gray-800', 'dark:border-gray-700', 'hover:bg-gray-50', 'dark:hover:bg-gray-600');
            row.classList.add('bg-white', 'border-b', 'dark:bg-gray-800', 'dark:border-gray-700', 'hover:bg-gray-50', 'dark:hover:bg-gray-600');
        }
    }
}

function highlightVideoName(vid) {
    // Select all <p> tags with id that starts with 'vname_'
    var pTags = document.querySelectorAll('p');
    for (var i = 0; i < pTags.length; i++) {

        var pTag = pTags[i];
        var pId = pTag.getAttribute('id');
        var expectedId = "vname-" + vid;

        if (pId == expectedId) {
            // HIGHLIGHT the row
            pTag.classList.remove('text-xl', 'text-slate-700');
            pTag.classList.add('text-xl', 'text-blue-500');
        } else {
            pTag.classList.remove('text-xl', 'text-slate-700');
            pTag.classList.remove('text-xl', 'text-blue-500');
            pTag.classList.add('text-xl', 'text-slate-700');
        }
    }
}


function onEnded(videoId) {

    console.log("endedddd" + videoId);
    //var player = videojs(videoId);
    //player.pause();
    const videos = document.querySelectorAll('.video-js');
    let playing = false;

    videos.forEach(v => {
        let player = videojs(v.id);
        console.log(videoId + " " + parseInt(videoId) + " " + nextVid);
        console.log(nextVid.get(parseInt(videoId)) + "   " + player.id());
        if (player.id() != videoId && !playing && nextVid.get(videoId) == player.id()) {
            startPlaying(player);
            playing = true;

        }
    });

}

