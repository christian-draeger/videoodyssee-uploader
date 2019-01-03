;

(function ($, window, document, undefined) {
    var events = {};
    $('#getScheduleButton').on('click', function () {
        var scheduleUrl = $('#scheduleUrl').val();
        $.get('/getData?url=' + scheduleUrl, function (data) {
            var eventAcronym;
            if (data.hasOwnProperty('schedule') && data.schedule.hasOwnProperty('conference')) {
                eventAcronym = data.schedule.conference.acronym;
            }
            if (data.hasOwnProperty('schedule') && data.schedule.hasOwnProperty('day')) {
                for (var eventDay = 0; eventDay < data.schedule.day.length; eventDay++) {
                    var theDay = data.schedule.day[eventDay];
                    var eventDate = theDay.date;
                    for (var eventRoom = 0; eventRoom < theDay.room.length; eventRoom++) {
                        var theRoom = theDay.room[eventRoom];
                        if (theRoom.hasOwnProperty('event')) {
                            for (var AnEvent = 0; AnEvent < theRoom.event.length; AnEvent++) {
                                var recordingEvent = createRecordingEvent(eventDate, eventAcronym, theRoom.event[AnEvent]);
                                events[recordingEvent.uuid] = recordingEvent;
                            }
                            if (!Array.isArray(theRoom.event)) {
                                var recordingEvent = createRecordingEvent(eventDate, eventAcronym, theRoom.event);
                                events[recordingEvent.uuid] = recordingEvent;
                            }
                        }
                    }
                }
            }
            var eventListElement = $('#events');
            eventListElement.find('option').remove().end().append(new Option("---SELECT---"));
            $.each(events, function (key, thisEvent) {
                var newOption = new Option(thisEvent.title, key);
                eventListElement.append($(newOption));
            });
            eventListElement.prop('disabled', false);
        }, "json");
    });

    $('#events').on('change', function () {
        myEvent = events[this.value];
        console.log(myEvent);
        $("input[name='title']").val(myEvent.title);
        $("textarea[name='description']").val(myEvent.description);
        $("input[name='videoUrl']").val(myEvent.videoUrl);
        $("input[name='releaseDate']").val(myEvent.releaseDate);
        $("input[name='uuid']").val(myEvent.uuid);
        $("input[name='link']").val(myEvent.link);
        $("select[name='language']").val(myEvent.language);
        $("select[name='conference']").val(myEvent.conference);
    });


})(jQuery, window, document);

function createRecordingEvent(eventDate, eventAcronym, theEvent) {
    var recordingEvent = {};
    recordingEvent.conference = eventAcronym;
    recordingEvent.title = theEvent.title;
    recordingEvent.description = theEvent.description;
    recordingEvent.language = convertLanguageToVoctowebVocabular(theEvent.language);
    recordingEvent.videoUrl = theEvent.video_download_url;
    recordingEvent.link = theEvent.url;
    recordingEvent.uuid = theEvent.guid;
    recordingEvent.releaseDate = eventDate;
    return recordingEvent;
}

function convertLanguageToVoctowebVocabular(language) {
    var voctowebLanguage;
    switch (language) {
        case "de":
            voctowebLanguage = "deu";
            break;
        case "en":
            voctowebLanguage = "eng";
            break;
        case "fr":
            voctowebLanguage = "fra";
            break;
    }
    return voctowebLanguage;

}
