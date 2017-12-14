;(function ($, window, document, undefined) {

    var date = new Date();
    var today = date.toISOString().substr(0, 10);
    var datePicker = $('#today');

    datePicker.attr('value', today);
    datePicker.attr('max', today);

    $('.inputfile').each(function () {
        var $input = $(this),
            $label = $input.next('label'),
            labelVal = $label.html();

        $input.on('change', function (e) {
            var fileName = '';

            if (e.target.value)
                fileName = e.target.value.split('\\').pop();

            if (fileName)
                $label.find('span').html(fileName);
            else
                $label.html(labelVal);
        });

        // Firefox bug fix
        $input
            .on('focus', function () {
                $input.addClass('has-focus');
            })
            .on('blur', function () {
                $input.removeClass('has-focus');
            });
    });
})(jQuery, window, document);
