function unsubscribe(creatorId)
{

    const url = "/subscriptions/unsubscribe/"+creatorId;
    var xhr = new XMLHttpRequest();
    xhr.open('DELETE',url,true);
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");

    xhr.onload = function () {

        if(xhr.readyState === 4 && xhr.status === 200 ) {
            alert("You Unsubscribed From This User");
            location.reload()
        }
    };

    xhr.send();
}