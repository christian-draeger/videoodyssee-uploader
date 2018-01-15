$(document).ready(function () {
    stepBarDemo();
});

function stepBarDemo() {

    var i = 0;
    var circle = $('#upload i');

    setRunning(circle, 0);

    var stepTitle = $('.notification-bar li');
    var line = $('.notification-bar .line');

    setInterval(function () {

        if (i >= 1 && i <= 5) {
            setSuccess(circle, line, stepTitle, i);
        }
        /*  if (i === 4) {
              setError(circle, stepTitle, i);
          }*/

        i++;
    }, 3000);
}

function setRunning(circle, index) {
    circle.eq(index).empty().addClass("ion-gear-b running");
}

function setSuccess(circle, line, title, index) {
    var i = index - 1;
    circle.eq(i).removeClass().addClass("ion-checkmark-circled success");
    line.css("background",
        "grey linear-gradient(" +
        "to bottom, " +
        "forestgreen " + linePercent(i) + "%, " +
        "grey " + (linePercent(i) + 20) + "%)");
    title.eq(i).addClass("animated pulse");
    setRunning(circle, index);
}

function setError(circle, title, index) {
    var i = index - 1;
    circle.eq(i).removeClass().addClass("ion-close-circled error");
    title.eq(i).addClass("animated pulse");
}

function linePercent(index) {

    switch (index) {
        case 0:
            return 0;
        case 1:
            return 25;
        case 2:
            return 50;
        case 3:
            return 75;
        case 4:
            return 100;
        default:
            return 100;
    }
}
