
var currentUserId = document.getElementById('data').getAttribute('data-currentUserId') || 'defaultValue';
var currentUserName = document.getElementById('data').getAttribute('data-currentUserName') || 'defaultValue';



connect(); // Connect to WebSocket when the page loads

function setNotificationAsSeen(notificationId,client)
{

    console.log('Seen notification: ' + notificationId);
    client.send("/app/notifications/set_seen/"+notificationId,{});
}

function subscribeToNotifications(client) {
    console.log("trying to sub  to notifications");
    client.subscribe('/queue/notifications/'+currentUserId , function (message) {

        console.log(message.body.toString()+" received");

        const str = message.body.toString();
        const notification = JSON.parse(str);

        const type = notification.type;


        if(type==="subscribed_user_upload")
            displayUploadBySubscriberNotification(notification)
        else if(type==="comment")
            displayCommentNotification(notification);
        else if(type==="like" || type==="dislike")
            displayLikeDislikeReaction(notification);
        else if(type==="my_upload")
        {
            const options = notification.optionalMessage;
            const success = options['success'];
            console.log(options);
            console.log("status: "+status.toString());

            if(success===true)
                displaySuccessfulUploadNotification(notification);
            else
                displayFailedUploadNotification(notification);
        }

        setNotificationAsSeen(notification.notificationId,client);
    });
}

function connect() {


    console.log('uid' + currentUserId + ' connected');
    var socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);
    console.log(stompClient+' lala');
    stompClient.connect({ 'userId': currentUserId }, function (frame) {
        // stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        subscribeToNotifications(stompClient);

        //requesting notifications
        stompClient.send("/app/notifications/pending/my",{});
    });
}

function displayFailedUploadNotification(notification)
{
    const nId = notification.notificationId;


    const generatingUserName = notification.generatingUserName;


    const options = notification.optionalMessage;
    console.log("options "+options);
    const vname = options['videoName'];

    const img_src =    "/users/permitted/"+generatingUserName+"/profile_img_by_uname";
    const type = notification.type;
    const message =   "Your uploaded video " + vname;
    const message2 = "Failed";
    const message3 = "due to error " + options['errormessage'];

    // Create a new div element for the toast notification
    var toastDiv = document.createElement('notification_div');
    toastDiv.id = 'toastNotification'+nId;
    toastDiv.className = 'max-w-xs bg-white border border-gray-200 rounded-xl shadow-lg fixed top-4 right-4'; // Add positioning classes
    toastDiv.setAttribute('role', 'alert');

    // Set inner HTML of the toast div
    toastDiv.innerHTML = `
        <!-- Toast -->
<div class="max-w-xs relative bg-white border border-gray-200 rounded-xl shadow-lg" role="alert">
  <div class="flex p-4">
    <div class="flex-shrink-0">
      <img class="inline-block size-8 rounded-full" src=${img_src} alt="Image Description">
      <button type="button" onclick="closeToast('${nId}')" class="absolute top-3 end-3 inline-flex flex-shrink-0 justify-center items-center size-5 rounded-lg text-gray-800 opacity-50 hover:opacity-100 focus:outline-none focus:opacity-100">
        <span class="sr-only">Close</span>
        <svg class="flex-shrink-0 size-4" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M18 6 6 18"></path>
          <path d="m6 6 12 12"></path>
        </svg>
      </button>
    </div>
    <div class="ms-4 me-5">
      <h3 class="text-gray-800 font-medium text-sm">
        <span class="font-semibold">${generatingUserName}</span> ${message}
      </h3>
     
      
      <div class="mt-2 text-sm text-gray-600 font-extrabold">
                        ${vname}
      </div>
      
      <div class="mt-2 text-sm text-gray-500 font-extrabold">
                        ${message2}
      </div>
      
       <div class="mt-2 text-sm text-black-400 font-extrabold">
                        ${message3}
      </div>
      
      <div class="mt-3">
      </div>
    </div>
  </div>
</div>
<!-- End Toast -->
    `;

    // Append the toast div to the body
    document.body.appendChild(toastDiv);

    // Remove the toast after a certain duration (e.g., 5 seconds)
    setTimeout(function() {
        toastDiv.remove();
    }, 50000);
}

function displaySuccessfulUploadNotification(notification)
{
    const nId = notification.notificationId;


    const generatingUserName = notification.generatingUserName;


    const options = notification.optionalMessage;
    console.log("options "+options);
    const vname = options['videoName'];

    const img_src =    "/users/permitted/"+generatingUserName+"/profile_img_by_uname";
    const type = notification.type;
    const message =   "Your uploaded video " + vname;
    const message2 = "was successful";

    // Create a new div element for the toast notification
    var toastDiv = document.createElement('notification_div');
    toastDiv.id = 'toastNotification'+nId;
    toastDiv.className = 'max-w-xs bg-white border border-gray-200 rounded-xl shadow-lg fixed top-4 right-4'; // Add positioning classes
    toastDiv.setAttribute('role', 'alert');

    // Set inner HTML of the toast div
    toastDiv.innerHTML = `
        <!-- Toast -->
<div class="max-w-xs relative bg-white border border-gray-200 rounded-xl shadow-lg" role="alert">
  <div class="flex p-4">
    <div class="flex-shrink-0">
      <img class="inline-block size-8 rounded-full" src=${img_src} alt="Image Description">
      <button type="button" onclick="closeToast('${nId}')" class="absolute top-3 end-3 inline-flex flex-shrink-0 justify-center items-center size-5 rounded-lg text-gray-800 opacity-50 hover:opacity-100 focus:outline-none focus:opacity-100">
        <span class="sr-only">Close</span>
        <svg class="flex-shrink-0 size-4" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M18 6 6 18"></path>
          <path d="m6 6 12 12"></path>
        </svg>
      </button>
    </div>
    <div class="ms-4 me-5">
      <h3 class="text-gray-800 font-medium text-sm">
        <span class="font-semibold">${generatingUserName}</span> ${message}
      </h3>
     
      
      <div class="mt-2 text-sm text-gray-600 font-extrabold">
                        ${vname}
      </div>
      
      <div class="mt-2 text-sm text-gray-500 font-extrabold">
                        ${message2}
      </div>
      
      <div class="mt-3">
      </div>
    </div>
  </div>
</div>
<!-- End Toast -->
    `;

    // Append the toast div to the body
    document.body.appendChild(toastDiv);

    // Remove the toast after a certain duration (e.g., 5 seconds)
    setTimeout(function() {
        toastDiv.remove();
    }, 50000);
}


function displayUploadBySubscriberNotification(message)
{
    const delim = "`";
    const parts = message.split(delim);
    const nId = parts[0];



    const generatingUserName = parts[1];
    const videoId = parts[3];
    const type = parts[4]
    const vname = parts[5]

    const img_src = "/users/"+generatingUserName+"/profile_img_by_uname";

    // Create a new div element for the toast notification
    var toastDiv = document.createElement('notification_div');
    toastDiv.id = 'toastNotification'+nId;
    toastDiv.className = 'max-w-xs bg-white border border-gray-200 rounded-xl shadow-lg fixed top-4 right-4'; // Add positioning classes
    toastDiv.setAttribute('role', 'alert');

    // Set inner HTML of the toast div
    toastDiv.innerHTML = `
        <!-- Toast -->
<div class="max-w-xs relative bg-white border border-gray-200 rounded-xl shadow-lg" role="alert">
  <div class="flex p-4">
    <div class="flex-shrink-0">
      <img class="inline-block size-8 rounded-full" src=${img_src} alt="Image Description">
      <button type="button" onclick="closeToast('${nId}')" class="absolute top-3 end-3 inline-flex flex-shrink-0 justify-center items-center size-5 rounded-lg text-gray-800 opacity-50 hover:opacity-100 focus:outline-none focus:opacity-100">
        <span class="sr-only">Close</span>
        <svg class="flex-shrink-0 size-4" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M18 6 6 18"></path>
          <path d="m6 6 12 12"></path>
        </svg>
      </button>
    </div>
    <div class="ms-4 me-5">
      <h3 class="text-gray-800 font-medium text-sm">
        <span class="font-semibold">${generatingUserName}</span> ${type}
      </h3>
     
      
      <div class="mt-1 text-sm text-gray-600">
                        ${vname}
      </div>
      
      <div class="mt-3">
        <a href="/videos/${videoId}/page">Watch Now</a>
      </div>
    </div>
  </div>
</div>
<!-- End Toast -->
    `;

    // Append the toast div to the body
    document.body.appendChild(toastDiv);

    // Remove the toast after a certain duration (e.g., 5 seconds)
    setTimeout(function() {
        toastDiv.remove();
    }, 50000);
}





function playNotificationSound() {
    // Create an Audio object and set its source to the notification sound file
    var audio = new Audio("reaction_notification.mp3");

    // Play the audio
    audio.play();

}

function closeToast(nId) {
    // Find the toast notification element
    var toastDiv = document.getElementById('toastNotification'+nId);

    // Remove the toast notification from the DOM
    if (toastDiv) {
        toastDiv.remove();
    }
}

function displayLikeDislikeReaction(notification)
{
    const nId = notification.notificationId;


    const generatingUserName = notification.generatingUserName;


    const options = notification.optionalMessage;
    console.log("options "+options);
    const vname = options['videoName'];

    const img_src =    "/users/permitted/"+generatingUserName+"/profile_img_by_uname";
    const type = notification.type;
    const message =   type === "like" ?  "liked your video" : "disliked your video";

    // Create a new div element for the toast notification
    var toastDiv = document.createElement('notification_div');
    toastDiv.id = 'toastNotification'+nId;
    toastDiv.className = 'max-w-xs bg-white border border-gray-200 rounded-xl shadow-lg fixed top-4 right-4'; // Add positioning classes
    toastDiv.setAttribute('role', 'alert');

    // Set inner HTML of the toast div
    toastDiv.innerHTML = `
        <!-- Toast -->
<div class="max-w-xs relative bg-white border border-gray-200 rounded-xl shadow-lg" role="alert">
  <div class="flex p-4">
    <div class="flex-shrink-0">
      <img class="inline-block size-8 rounded-full" src=${img_src} alt="Image Description">
      <button type="button" onclick="closeToast('${nId}')" class="absolute top-3 end-3 inline-flex flex-shrink-0 justify-center items-center size-5 rounded-lg text-gray-800 opacity-50 hover:opacity-100 focus:outline-none focus:opacity-100">
        <span class="sr-only">Close</span>
        <svg class="flex-shrink-0 size-4" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M18 6 6 18"></path>
          <path d="m6 6 12 12"></path>
        </svg>
      </button>
    </div>
    <div class="ms-4 me-5">
      <h3 class="text-gray-800 font-medium text-sm">
        <span class="font-semibold">${generatingUserName}</span> ${message}
      </h3>
     
      
      <div class="mt-2 text-sm text-gray-600 font-extrabold">
                        ${vname}
      </div>
      
      <div class="mt-3">
      </div>
    </div>
  </div>
</div>
<!-- End Toast -->
    `;

    // Append the toast div to the body
    document.body.appendChild(toastDiv);

    // Remove the toast after a certain duration (e.g., 5 seconds)
    setTimeout(function() {
        toastDiv.remove();
    }, 50000);
}

function displayCommentNotification(notification)
{
    const nId = notification.notificationId;


    const generatingUserName = notification.generatingUserName;
    const type = notification.type;

    const options = notification.optionalMessage;
    console.log("options "+options);
    const vname = options['videoName'];

    const img_src =    "/users/permitted/"+generatingUserName+"/profile_img_by_uname";
    const message1 = "commented";
    const message2 = " on your video";
    const txt = options['commentText'];

    console.log(txt);
    // Create a new div element for the toast notification
    var toastDiv = document.createElement('notification_div');
    toastDiv.id = 'toastNotification'+nId;
    toastDiv.className = 'max-w-xs bg-white border border-gray-200 rounded-xl shadow-lg fixed top-4 right-4'; // Add positioning classes
    toastDiv.setAttribute('role', 'alert');

    // Set inner HTML of the toast div
    toastDiv.innerHTML = `
        <!-- Toast -->
<div class="max-w-xs relative bg-white border border-gray-200 rounded-xl shadow-lg" role="alert">
  <div class="flex p-4">
    <div class="flex-shrink-0">
      <img class="inline-block size-8 rounded-full" src=${img_src} alt="Image Description">
      <button type="button" onclick="closeToast('${nId}')" class="absolute top-3 end-3 inline-flex flex-shrink-0 justify-center items-center size-5 rounded-lg text-gray-800 opacity-50 hover:opacity-100 focus:outline-none focus:opacity-100">
        <span class="sr-only">Close</span>
        <svg class="flex-shrink-0 size-4" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M18 6 6 18"></path>
          <path d="m6 6 12 12"></path>
        </svg>
      </button>
    </div>
    <div class="ms-4 me-5">
      <h3 class="text-gray-800 font-medium text-sm">
        <span class="font-semibold">${generatingUserName}</span> ${message1}
      </h3>
       
       
       <div class="mt-2 text-sm text-amber-950 font-mono">
                        ${txt}
      </div>
      
      <div class="mt-2 text-sm text-amber-950 font-mono">
                        ${message2}
      </div>
      
      <div class="mt-2 text-sm text-gray-600 font-extrabold">
                        ${vname}
      </div>
      
     
      
      
      
      <div class="mt-3">
      </div>
    </div>
  </div>
</div>
<!-- End Toast -->
    `;

    // Append the toast div to the body
    document.body.appendChild(toastDiv);

    // Remove the toast after a certain duration (e.g., 5 seconds)
    setTimeout(function() {
        toastDiv.remove();
    }, 50000);
}