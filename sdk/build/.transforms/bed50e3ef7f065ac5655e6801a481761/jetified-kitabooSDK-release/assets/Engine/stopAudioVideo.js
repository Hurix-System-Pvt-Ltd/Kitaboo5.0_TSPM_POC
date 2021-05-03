function stopAudioVideo() {
    var audioPlayer = document.getElementsByTagName('audio')[0];
    if(audioPlayer != undefined){
    audioPlayer.pause();
    }
    var videoPlayer = document.getElementsByTagName('video')[0];
    if(videoPlayer != undefined){
    videoPlayer.pause();
    }
}
