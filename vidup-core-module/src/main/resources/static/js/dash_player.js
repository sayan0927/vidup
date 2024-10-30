var videoId = document.getElementById('data').getAttribute('data-videoId') || 'defaultValue';
var userId = document.getElementById('data').getAttribute('data-currentUserId') || 'defaultValue'
var baseUrl = document.getElementById('data').getAttribute('data-dash_url') || 'defaultValue'

function start() {



    console.log("dashjs");

 //   var url = 'https://media.axprod.net/TestVectors/v7-Clear/Manifest_1080p.mpd';


   // var url = 'http://localhost:8000/videos/permitted/dash_manifest/'+videoId;


    var url = "http://"+baseUrl+"/"+videoId;
   // var url = baseUrl;

    console.log(url+" url");


    var videoElement = document.querySelector('video');


    console.log("returned");
    var player = dashjs.MediaPlayer().create();
    player.initialize(videoElement, url, true);
    player.setInitialMediaSettingsFor('audio', {
        lang: 'et-ET'
    })


    var controlbar = new ControlBar(player);
    controlbar.initialize();


}

