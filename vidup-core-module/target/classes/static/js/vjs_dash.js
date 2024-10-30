


var videoId = document.getElementById('data').getAttribute('data-videoId') || 'defaultValue';
var userId = document.getElementById('data').getAttribute('data-currentUserId') || 'defaultValue'


function start() {


    console.log("videojs");
    var url = 'https://media.axprod.net/TestVectors/v7-Clear/Manifest_1080p.mpd';


    //  var url = 'http://localhost:8000/videos/permitted/dash_manifest/'+videoId;

    var videoElement = document.querySelector('video');


    var player = videojs(videoElement,{
        html5: {
            dash: {
                setLimitBitrateByPortal:false,
                setMaxAllowedBitrateFor: ['video', 2000],

            }
        }
    });


    player.src({src: url, type: 'application/dash+xml'});
    player.dashHlsBitrateSwitcher({
        showInfo: false,
    });
    player.play();


}

