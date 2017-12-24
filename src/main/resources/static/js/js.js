$(document).ready(function () {
    var url = $(location).attr('href');
    var isForm = url.indexOf('#upload') !== -1;

    if (isForm) {
        $('.login-box.back').show();

        stepBarDemo();

    } else {
        $('.login-box.front').show();
    }

    $('.login-box.front button[type=submit]').click(function () {
        $('.login-box.front').addClass("animated bounceOutUp");
    });
});

function stepBarDemo() {

    var i = 0;
    var circle = $('#upload i');
    var stepTitle = $('.notification-bar li');
    var line = $('.notification-bar .line');

    setInterval(function () {

        setRunning(circle, line, i);

        if (i >= 1 && i < 4) {
            setSuccess(circle, line, stepTitle, i);
        }
        if (i === 4) {
            setError(circle, line, stepTitle, i);
        }

        i++;
    }, 5000);
}

function setRunning(circle, index) {
    circle.eq(index).empty().addClass("ion-gear-b running");
}

function setSuccess(circle, line, title, index) {
    var i = index -1;
    circle.eq(i).removeClass().addClass("ion-checkmark-circled success");
    line.css("background", "grey linear-gradient(to bottom, #02ff00 " + linePercent(i) + "%, grey " + (linePercent(i)+20) + "%)");
    title.eq(i).addClass("animated pulse");
}

function setError(circle, title, index) {
    var i = index -1;
    circle.eq(i).removeClass().addClass("ion-close-circled error");
    title.eq(i).addClass("animated pulse");
}

function linePercent(index) {

    switch(index) {
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

$.getScript("https://cdnjs.cloudflare.com/ajax/libs/particles.js/2.0.0/particles.min.js", function () {
    particlesJS('particles-js',
        {
            "particles": {
                "number": {
                    "value": 100,
                    "density": {
                        "enable": true,
                        "value_area": 1000
                    }
                },
                "color": {
                    "value": "#b71e4c"
                },
                "shape": {
                    "type": "circle",
                    "stroke": {
                        "width": 3,
                        "color": "#b71e4c"
                    },
                    "polygon": {
                        "nb_sides": 5
                    },
                    "image": {
                        "width": 100,
                        "height": 100
                    }
                },
                "opacity": {
                    "value": 0.5,
                    "random": false,
                    "anim": {
                        "enable": true,
                        "speed": 1,
                        "opacity_min": 0.1,
                        "sync": false
                    }
                },
                "size": {
                    "value": 5,
                    "random": true,
                    "anim": {
                        "enable": false,
                        "speed": 40,
                        "size_min": 0.1,
                        "sync": false
                    }
                },
                "line_linked": {
                    "enable": true,
                    "distance": 150,
                    "color": "#b71e4c",
                    "opacity": 0.4,
                    "width": 3
                },
                "move": {
                    "enable": true,
                    "speed": 3,
                    "direction": "none",
                    "random": false,
                    "straight": false,
                    "out_mode": "out",
                    "attract": {
                        "enable": false,
                        "rotateX": 600,
                        "rotateY": 1200
                    }
                }
            },
            "interactivity": {
                "detect_on": "canvas",
                "events": {
                    "onhover": {
                        "enable": true,
                        "mode": "repulse"
                    },
                    "onclick": {
                        "enable": true,
                        "mode": "push"
                    },
                    "resize": true
                },
                "modes": {
                    "grab": {
                        "distance": 400,
                        "line_linked": {
                            "opacity": 1
                        }
                    },
                    "bubble": {
                        "distance": 400,
                        "size": 40,
                        "duration": 2,
                        "opacity": 8,
                        "speed": 3
                    },
                    "repulse": {
                        "distance": 100
                    },
                    "push": {
                        "particles_nb": 4
                    },
                    "remove": {
                        "particles_nb": 2
                    }
                }
            },
            "retina_detect": true,
            "config_demo": {
                "hide_card": false,
                "background_color": "#000000",
                "background_image": "",
                "background_position": "50% 50%",
                "background_repeat": "no-repeat",
                "background_size": "cover"
            }
        }
    );
});
