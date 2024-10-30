var stompClient = null;

function connect() {

    console.log('uid' + currentUserId + ' connected');
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        // stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        subscribeToNotifications();

        //requesting notifications
        stompClient.send("/app/notifications/pending/my",{});
    });
}


function handleSearch()
{
    console.log("handlind");

    const form = document.getElementById('search_form');
    const formData = new FormData(form);

    console.log(formData);

    const url = new URL("/videos/search");
    url.searchParams.append("search_key",formData.get('search_key'));
    const xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');

    xhr.onload = function () {

        if(xhr.readyState === 4) {
            if(xhr.status === 200) {
                var dto = JSON.parse(xhr.responseText);
                console.log(dto);

                removeAllPlayers();
                fillVideoContainerWithVideos(dto);
            }
        }
    }
    xhr.send(formData);
}

function fillVideoContainerWithVideos(dto)
{
    var url = document.getElementById('data').getAttribute('data-source_url') || 'defaultValue';
   // url ='ginglob';
    const token = document.getElementById('data').getAttribute('data-session_token') || 'defaultValue';
    var uid = document.getElementById('data').getAttribute('data-currentUserId') || 'defaultValue';

    const greet = function greet() {
        console.log('Hey there clicker!');
    }
    console.log(dto);
    const videoContainer = document.getElementById('video_container');
    dto.forEach(videoDto => {
        console.log(url," is");
        const videoItem = document.createElement('div');
   //     videoItem.className = 'video-link-small video-item';
        const video = videoDto.video;
        videoItem.id = `player${video.id}`;



        videoItem.innerHTML = `
                    <div class="video-link-small video-item" id="player${video.id}">
                        
                        
                        
                                        <video id="${video.id}" name="${video.id}"  onclick="window.location.href = 'https://www.example.com/videos-page'"
                               class="video-js vjs-default-skin custom-small-video-player" preload controls loop
                              data-setup='{"autoplay": false }'>
                            <source src="http://${url}/video/stream/${video.id}/${token}/${uid}"
                                    type="video/mp4">
                        </video>
                                    
                        

                        <div class="text-container">
                            <div class="video-text" style="margin-left: 10px;">
                                <p class="text-gray-600 font-bold">${video.name}</p>
                                <div class="flex items-center space-x-4">
                                    <a href="/users/${video.creator.id}/channel/page">
                                        <img src="/users/${video.creator.id}/profile_img"
                                             style="border-radius: 100%; object-fit: cover; width: 50px; height: 50px;"/>
                                    </a>
                                    <a href="/users/${video.creator.id}/channel/page">
                                        <p class="text-gray-600 font-semibold" style="font-size: 22px">${video.creator.userName}</p>
                                    </a>
                                    <p class="font-serif" style="font-size:18px;">${video.views} views</p>
                                    <p style="font-size:15px; margin-left: 10px">1</p>
                                </div>
                            </div>
                        </div>
                    </div>
                `;


        videoContainer.append(videoItem);
        let player = document.getElementById(video.id);
        player.addEventListener("click",function(){
            console.log("ylf");
        });
        console.log(player);


    });

    var videoDivs = document.querySelectorAll('.custom-small-video-player');
    videoDivs.forEach(function(div) {
        var player =  document.getElementById(div.id);



    });
   // displayPlayers();

}

function removeAllPlayers()
{
    const videoContainer = document.getElementById('video_container');
    videoContainer.innerHTML = ''; // C
}

function displayPlayers()
{
    var videoDivs = document.querySelectorAll('.custom-small-video-player');
    videoDivs.forEach(function(div) {
        var player =  document.getElementById(div.id);
        console.log(player);
    });
}

function handleTagsSelected()
{
    displayPlayers();
    const selectElement = document.getElementById("tags");

    var player = document.getElementById('player'+451);

    const selectedOptions = Array.from(selectElement.selectedOptions);
    //const selectedTags = selectedOptions.map(option => option.text);
    const selectedTagsId = selectedOptions.map(option => option.value);

    if(selectedTagsId.length ===1 && selectedTagsId[0] === 'all'){

        showAllPlayers();
        return;
    }

    const videoElements = document.querySelectorAll('#video_container video');
    // Retrieve the IDs of the video elements
    const videoIds = Array.from(videoElements).map(video => video.getAttribute('name'));


    const url = new URL("/videos/tag_filter");
    const xhr = new XMLHttpRequest();

    selectedTagsId.forEach(tag => url.searchParams.append("tags", tag));
    videoIds.forEach(videoId => url.searchParams.append("videoIds", videoId));

    xhr.open("GET", url, true);

    xhr.onload = function() {

        if(xhr.readyState === 4) {

            if(xhr.status===200)
            {
                const response = JSON.parse(xhr.responseText);

                console.log(response);
                hideAllPlayers();
                showPlayersForVideos(response);


            }
        }
    }
    xhr.send();

}

function showAllPlayers()
{
    var videoDivs = document.querySelectorAll('.video-item');
    videoDivs.forEach(function(div) {
        var player =  document.getElementById(div.id);
        player.hidden=false;
    });
}



function hideAllPlayers()
{
    var videoDivs = document.querySelectorAll('.video-item');
    videoDivs.forEach(function(div) {
        var player =  document.getElementById(div.id);
        player.hidden=true;
    });
}

function showPlayersForVideos(videoDTOlist)
{




    for(var i = 0; i < videoDTOlist.length; i++)
    {
        const vid = videoDTOlist[i].video.id;

        var player = document.getElementById('player'+vid);
        player.hidden=false;
    }
}