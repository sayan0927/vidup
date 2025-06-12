function goToLink(videoId) {

    var link = "/videos/permitted/" + videoId + "/page/dash";

    window.location.href = link;
}


function displaySubscriptionNotification(message) {

    const msg = message.split("-");
    const first = msg[0];
    const uname = msg[1];

    const img_src = "/profile_image_by_uname/" + uname;

    // Create a new div element for the toast notification
    var toastDiv = document.createElement('notification_div');
    toastDiv.id = 'temp';
    toastDiv.className = 'max-w-xs bg-white border border-gray-200 rounded-xl shadow-lg fixed top-4 right-4'; // Add positioning classes
    toastDiv.setAttribute('role', 'alert');

    // Set inner HTML of the toast div
    toastDiv.innerHTML = `
        <!-- Toast -->
<div class="max-w-xs relative bg-white border border-gray-200 rounded-xl shadow-lg" role="alert">
  <div class="flex p-4">
    
    <div class="ms-4 me-5">

      <h3 class="text-gray-800 fonttext-sm">
        <span >${first}</span> 
      </h3>
     
      
      <div class="mt-1 text-sm text-gray-600 font-semibold">
             ${uname}
      </div>
    </div>
  </div>
</div>
<!-- End Toast -->
    `;

    // Append the toast div to the body
    document.body.appendChild(toastDiv);

    // Remove the toast after a certain duration (e.g., 5 seconds)
    setTimeout(function () {
        toastDiv.remove();
    }, 1000);
}


function check(vid) {
    var uid = document.getElementById('details').getAttribute('data-uid') || 'defaultValue';
    var session_token = document.getElementById('details').getAttribute('data-sessiontoken') || 'defaultValue';

}

