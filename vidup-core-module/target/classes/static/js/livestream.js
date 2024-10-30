
var currentUserId = document.getElementById('data').getAttribute('data-currentUserId') || 'defaultValue';
var currentUserName = document.getElementById('data').getAttribute('data-currentUserName') || 'defaultValue';

class recording{
    constructor(){}
}
let recChunks = [];
let mediaRecorder;
let stream;
let stopRec = false;
let chunkId = 0;
let recordingId = -1;

function toggleRecordingModal() {
    const modal = document.getElementById('record_content');

    const time  = new Date().getTime().toString();

    const rName = currentUserName+"-"+time;

    // Toggle the 'hidden' class on the modal to show/hide it
    modal.classList.toggle('hidden');

    document.getElementById("recording_name").value = rName;
    document.getElementById("recording_description").value = rName;

}

function setRecordingId()
{
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/live_record/start');
    xhr.setRequestHeader("Access-Control-Allow-Origin", "*");

    xhr.onload = function () {

        if(xhr.readyState === 4 && xhr.status === 200 ) {
            const recording = JSON.parse(xhr.responseText);
            recordingId= recording.id;
            console.log(JSON.parse(xhr.responseText));
        }
    };

    xhr.send();
}

async function startRecording() {
    stopRec = false;

    //set new recording id
    setRecordingId();

    // Request access to webcam and microphone
    stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });

    mediaRecorder = new MediaRecorder(stream, {
        audioBitsPerSecond: 128000,
        videoBitsPerSecond: 2500000,
        mimeType: 'video/webm;codecs=vp8,opus'

    });
    //Add to Buffer
    mediaRecorder.ondataavailable = function(ev) {
        recChunks.push(ev.data);
        console.log("added size"+ev.data.size);
    }

    mediaRecorder.start(50);  // Adjust time for smaller chunks if necessary
}

async function stopRecording() {
    stopRec = true;
    chunkId = 0;
    if (mediaRecorder) {
        mediaRecorder.stop();
    }

    if (stream) {
        const tracks = stream.getTracks();
        tracks.forEach(track => track.stop());
    }

 //   await sendRecordingToServerInWhole();
    await sendRecordingToServerInChunks();

   // location.reload();

}

async function sendRecordingToServerInWhole()
{
    let blob = new Blob(recChunks, { 'type' : recChunks[0].type });
    recChunks = [];

    const formData = new FormData();
    formData.append('chunk', blob);
    formData.append('recording_id',recordingId.toString());

    try {
        await fetch('/live_record/upload/whole', {
            method: 'POST',
            body: formData,

        });
    } catch (error) {
        console.error('Error posting data:', error);
    }
}

async function sendRecordingToServerInChunks()
{
    for(let i=0;i<recChunks.length;i++){

        let arr=[];
        arr.push(recChunks[i]);
        let blob = new Blob(arr, { 'type' : recChunks[0].type });
        const formData = new FormData();
        formData.append('chunk', blob);
        formData.append("chunk_id",i.toString());
       // formData.append('recording_id',recordingId.toString());

        console.log("sending chunk "+i+" with size"+blob.size);

        try {
            await fetch('/live_record/upload/chunk/'+recordingId, {
                method: 'POST',
                body: formData,

            });
        } catch (error) {
            console.error('Error posting data:', error);
        }
    }

    await markUploadComplete(recordingId);

    recChunks = [];
}

async function markUploadComplete(id)
{
    try {
        await fetch('/live_record/upload/complete/'+id, {
            method: 'POST',


        });
    } catch (error) {
        console.error('Error posting data:', error);
    }
}



