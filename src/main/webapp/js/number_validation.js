"use strict";

var $phone;
$('#form-phone')
    .keydown(function (e) {
        var key = e.which || e.charCode || e.keyCode || 0;

        $phone = $(this);

        // Don't let them remove the starting '('
        if ($phone.val().length === 4 && (key === 8 || key === 46)) {
            $phone.val('+7 (');
            return false;
        }
        // Reset if they highlight and type over first char.
        else if ($phone.val().charAt(0) !== '+') {
            $phone.val('+7 (' + String.fromCharCode(e.keyCode) + '');
        }

        // Auto-format- do not expose the mask as the user begins to type
        if (key !== 8 && key !== 9) {
            if ($phone.val().length === 7) {
                $phone.val($phone.val() + ') ');
            }
            if ($phone.val().length === 12) {
                $phone.val($phone.val() + '-');
            }
            if ($phone.val().length === 15) {
                $phone.val($phone.val() + '-');
            }
        }

        // Allow numeric (and tab, backspace, delete) keys only
        return (key === 8 ||
            key === 9 ||
            key === 46 ||
            (key >= 48 && key <= 57) ||
            (key >= 96 && key <= 105));
    })

    .bind('focus click', function () {
        $phone = $(this);

        if ($phone.val().length === 0) {
            $phone.val('+7 (');
        }
        else {
            var val = $phone.val();
            $phone.val('').val(val); // Ensure cursor remains at the end
        }
    })

    .blur(function () {
        $phone = $(this);

        if ($phone.val() === '+7 (') {
            $phone.val('');
        }
    });