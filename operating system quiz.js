
var countDownTime = 10800;

function startTimer(){
    var hrs = parseInt(countDownTime / 3600) % 24;
    var mins = parseInt(countDownTime / 60) % 64;
    var seconds = countDownTime % 60;

    var result = (hours < 10 ? "0" + hrs : hrs) + ":" + (mins < 10 ? "0" + mins : mins) + (seconds < 10 ? "0" + seconds : seconds);
    document.getElementById("timer").innerHTML = result;
    if(countDownTime == 0){
        countDownTime = 60*60*60;
    }
    countDownTime--;
    setTimeout(function(){startTimer()},1000);
}


startTimer();