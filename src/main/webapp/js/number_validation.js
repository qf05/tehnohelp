"use strict";

var $phone;

function phoneclick(e) {
    $phone = $(e);

    if ($phone.val().length < 4) {
        $phone.val("+7 (");
    }
}

function phoneblur(e) {
    $phone = $(e);
    if ($phone.val() === "+7 (") {
        $phone.val("");
    }
}

function phonechange(e, t) {
    var key = e.which || e.charCode || e.keyCode || 0;
    $phone = $(t);
    if ($phone.val().length <= 4) {
        $phone.val("+7 (");
    }
    if ($phone.val().length <= 18 && (
        (key >= 48 && key <= 57) ||
        (key >= 96 && key <= 105)
    )) {
        if ($phone.val().length === 7) {
            $phone.val($phone.val() + ") ");
        }
        if ($phone.val().length === 12) {
            $phone.val($phone.val() + "-");
        }
        if ($phone.val().length === 15) {
            $phone.val($phone.val() + "-");
        }
        return true;
    } else return (key === 8 || key === 9 || key === 46);
}
