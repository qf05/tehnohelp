"use strict";

var formStatus = $(".submit-status");

$(".order-submit").click(function (event) {
    event.preventDefault();
    var btn = $(this);
    var form = document.forms[0];
    var link = form.childNodes[5].childNodes[1];
    var data = $(form).serialize();
    $.ajax({
        url: "/grabber",
        method: "POST",
        data: data,
    }).done(function (data) {
        //form.reset();
        btn.attr("disabled", false);
        showStatus(formStatus, data.error, data.message);
        document.getElementById("form-date").value = data.date;
    }).fail(function (err) {
        btn.attr("disabled", false);
        showStatus(formStatus, err.response.error, err.response.message);
    });
});


document.getElementById('form-link').addEventListener('click', function() {
    this.select();
});


function showStatus(target, error, message) {
    target.text(message).css("background", error ? "#c73131" : "#49b94c").addClass("fadeInUp").delay(3000).queue(function (next) {
        $(this).addClass("fadeOutDown");
        target.text(message).css("background", "#ffff00");
        next();
    }).delay(500).queue(function (next) {
        $(this).removeClass("fadeInUp fadeOutDown");
        next();
    });

}