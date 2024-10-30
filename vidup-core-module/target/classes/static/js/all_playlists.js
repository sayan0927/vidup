
function deletePlaylist(playlistId)
{
    console.log("will delelte "+playlistId);


    const url = "/playlists/" + playlistId + "/delete";
    var xhr = new XMLHttpRequest();
    xhr.open('DELETE', url, true);
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");

    var resp;

    xhr.onreadystatechange = function () {
        console.log(xhr.readyState + " " + xhr.status + " " + xhr.responseText);
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log("Deleted successfully:", JSON.parse(xhr.responseText));
                location.reload(true);
            } else {
                console.log("Failed to Comment", xhr.status, xhr.statusText);
            }
        }
    };

    xhr.send();

}

function validPlayListName(pname)
{
    return (!(pname=='' || pname==""))
}

function createPlaylist()
{
    const playlistName = document.getElementById('pname').value;
    if(!validPlayListName(playlistName))
    {
        console.log("playlist name error");
        alert("Invalid playlist name");
        return;
    }
    console.log(playlistName);

    const url = "/playlists/create";
    var xhr = new XMLHttpRequest();
    xhr.open('POST', url, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");

    var resp;

    xhr.onreadystatechange = function () {
        console.log(xhr.readyState + " " + xhr.status + " " + xhr.responseText);
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log("Playlist Created successfully:", JSON.parse(xhr.responseText));
                location.reload(true);
            } else {
                console.log("Failed to Comment", xhr.status, xhr.statusText);
            }
        }
    };

    var params = "pname=" + encodeURIComponent(playlistName);
    xhr.send(params);
}

function toggleCreatePlaylistModal()
{
    const modal = document.getElementById('create_playlist');
    // Toggle the 'hidden' class on the modal to show/hide it
    modal.classList.toggle('hidden');
}