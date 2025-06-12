var videoId = document.getElementById('data').getAttribute('data-videoId') || 'defaultValue';
var userId = document.getElementById('data').getAttribute('data-currentUserId') || 'defaultValue'
var baseUrl = document.getElementById('data').getAttribute('data-dash_url') || 'defaultValue'

function start() {

    var url = "http://" + baseUrl + "/" + videoId;
    var videoElement = document.querySelector('video');
    var player = dashjs.MediaPlayer().create();
    player.initialize(videoElement, url, true);
    player.setInitialMediaSettingsFor('audio', {
        lang: 'et-ET'
    })
    var controlbar = new ControlBar(player);
    controlbar.initialize();

}

