"use strict";

(function () {
  function scrollTo(anchor) {
    if ($(anchor).offset() !== undefined) {
      $("html, body").animate({ scrollTop: $(anchor).offset().top - 50 }, 2500);
    } else {
      $("html, body").scrollTop(0);
    }
  }

  function gridOffset(grid, boxes) {
    if (window.matchMedia("(min-width: 992px)").matches) {
      grid.css("margin-left", "150px");
    } else {
      grid.removeAttr("style");
    }
  }

  function closeMenu() {
    var nav = $(".navbar-collapse");
    if (nav.hasClass("show")) {
      nav.slideToggle(300, null, function () {
        $(".navbar-toggler").removeClass("is-active");
        nav.removeAttr("style");
        nav.removeClass("show");
      });
    }
  }

  function showStatus(target, error, message) {
    target.text(message).css("background", error ? "#c73131" : "#49b94c").addClass("fadeInUp").delay(4000).queue(function (next) {
      $(this).addClass("fadeOutDown");
      next();
    }).delay(500).queue(function (next) {
      $(this).removeClass("fadeInUp fadeOutDown");
      next();
    });
  }

  function startDigits() {
    var c = document.querySelector("#c_digits");
    var ctx = c.getContext("2d");

    c.width = $(".page-content").width();
    c.height = $(".page-content").height();

    var digits = "01";
    digits = digits.split("");

    var font_size = 14;
    var columns = c.width / font_size;
    var drops = [];

    for (var x = 0; x < columns; x++) {
      drops[x] = 1;
    }

    //drawing the characters
    function draw() {
      ctx.fillStyle = "rgba(255, 255, 255, 0.05)";
      ctx.fillRect(0, 0, c.width, c.height);

      ctx.fillStyle = "#226dad";
      ctx.font = font_size + "px arial";
      //looping over drops
      for (var i = 0; i < drops.length; i++) {
        var text = digits[Math.floor(Math.random() * digits.length)];
        ctx.fillText(text, i * font_size, drops[i] * font_size);

        if (drops[i] * font_size > c.height / 3 && Math.random() > 0.975) {
          drops[i] = 0;
        }

        drops[i]++;
      }
    }

    startDigits.interval = setInterval(draw, 83);

    // reset canvas
    window.addEventListener("resize", function (e) {
      clearInterval(startDigits.interval);

      c.width = $(".page-content").width();
      c.height = $(".page-content").height();

      ctx.fillStyle = "#fff";
      ctx.fillRect(0, 0, c.width, c.height);
      columns = c.width / font_size;

      for (var x = 0; x < columns; x++) {
        drops[x] = 1;
      }
      startDigits.interval = setInterval(draw, 83);
    });
  }

  window.addEventListener("load", function () {
    var CommonView = Barba.BaseView.extend({
      namespace: "common",
      onEnterCompleted: function onEnterCompleted() {
        if (this.namespace !== "home" && window.matchMedia("(min-width: 768px)").matches) {
          startDigits();
        }

        window.VK.Widgets.Group("vk_subscribe", {
          mode: 1,
          no_cover: 1,
          width: "280px",
          height: "100px"
        }, 184345402);

        new Waypoint({
          element: $(".footer"),
          offset: "70%",
          handler: function handler(direction) {
            $(".contact").addClass("animated fadeInLeft");
            $(".order-form").addClass("animated fadeInRight");
          }
        });

        $(".order-submit").click(function (event) {
          event.preventDefault();
          var btn = $(this);
          var form = document.forms[0];
          var phone = form.childNodes[3].childNodes[1];
          var msg = form.childNodes[5].childNodes[1];

          phone.classList.remove("invalid");
          phone.nextElementSibling.style.display = "none";
          phone.nextElementSibling.textContent = "";
          if (!phone.value.length) {
            phone.classList.add("invalid");
            phone.nextElementSibling.textContent = "Необходимо указать номер телефона";
            phone.nextElementSibling.style.display = "block";
          }

          var phoneDigits = phone.value.replace(/\D/g, '').length;

          if (phone.value.length && phoneDigits < 11) {
            phone.classList.add("invalid");
            phone.nextElementSibling.textContent = "Номер ввёден неправильно";
            phone.nextElementSibling.style.display = "block";
          }

          msg.classList.remove("invalid");
          msg.nextElementSibling.style.display = "none";
          msg.nextElementSibling.textContent = "";
          if (msg.value.length == "") {
            msg.classList.add("invalid");
            msg.nextElementSibling.style.display = "block";
            msg.nextElementSibling.textContent = "Необходимо написать сообщение";
          }

          if (msg.value.length && phoneDigits === 11) {
            var data = $(form).serialize();
            var formStatus = $(".submit-status");
            btn.attr("disabled", true);

            $.ajax({
              url: "/sendmessage",
              method: "POST",
              data: data
            }).done(function (data) {
              form.reset();
              btn.attr("disabled", false);
              showStatus(formStatus, data.error, data.message);
            }).fail(function (err) {
              btn.attr("disabled", false);
              showStatus(formStatus, err.response.error, err.response.message);
            });
          }
        });
      },
      onLeave: function onLeave() {
        if (startDigits.interval !== undefined) {
          clearInterval(startDigits.interval);
          startDigits.interval = null;
        }
      }
    });

    var Homepage = CommonView.extend({
      namespace: "home",
      onEnterCompleted: function onEnterCompleted() {
        CommonView.onEnterCompleted.call(this);

        var parallax = -0.5;
        var $bg_images = $(".hero");
        var offset_tops = [];
        $bg_images.each(function (i, el) {
          offset_tops.push($(el).offset().top);
        });
        $(window).scroll(function () {
          var dy = $(this).scrollTop();
          $bg_images.each(function (i, el) {
            var ot = offset_tops[i];
            $(el).css("background-position", "50% " + (dy - ot) * parallax + "px");
          });
        });

        $(".order-link").click(function (event) {
          event.preventDefault();
          scrollTo($(this).attr("href"));
        });
        $(".arrow-link").click(function () {
          event.preventDefault();
          scrollTo($(this).attr("href"));
        });

        var grid = $(".diamond-grid");
        var boxes = $(".diamond-box");
        gridOffset(grid, boxes);
        window.onresize = function (event) {
          gridOffset(grid, boxes);
        };

        new Waypoint({
          element: $(".hero"),
          handler: function handler(direction) {
            $(".hero-content").addClass("animated");
            $(".hero-content h1").addClass("animated fadeInDown");
            $(".hero-content span").addClass("animated fadeIn");
            $(".order-link").addClass("animated zoomIn");
            $(".arrow-link").addClass("animated fadeIn");
          }
        });

        new Waypoint({
          element: document.querySelector(".services"),
          offset: window.matchMedia("(min-width: 1440px)").matches ? "70%" : "50%",
          handler: function handler(direction) {
            $(".services .heading").addClass("animated fadeInUp");
            grid.addClass("grid_show");
            $(".service_schema").addClass("svg_anim_move");
            $(".service_svg_dot").each(function (_, item) {
              item.style.transform = "matrix(-1,0,0,1," + item.dataset.pos + ") scale(1)";
            });
          }
        });
        new Waypoint({
            element: $(".about"),
            offset: window.matchMedia("(min-width: 1440px)").matches ? "70%" : "60%",
            handler: function handler(direction) {
                $(".about .heading").addClass("animated fadeInUp");
                $(".about-text").addClass("animated fadeIn");
                $(".about #vk_groups").addClass("animated fadeInRight");
            }
        });

        new Waypoint({
            element: $(".advantages"),
            offset: window.matchMedia("(min-width: 1440px)").matches ? "70%" : "60%",
            handler: function handler(direction) {
                $(".advantages .heading").addClass("animated fadeInUp");
                $(".adv-pic-1, .adv-pic-2").addClass("animated zoomIn");
                $(".adv-option").addClass("animated fadeInRight");
            }
        });

        new Waypoint({
          element: $(".bonuses"),
          offset: "70%",
          handler: function handler(direction) {
            $(".bonuses .heading").addClass("animated fadeInUp");
            $(".bonus-item").addClass("animated zoomIn");
            // $(".adv-option").addClass("animated fadeInRight");
          }
        });

        if (window.matchMedia("(min-width: 768px)").matches) {
          VK.Widgets.Group("vk_groups", { mode: 4, wide: 1, no_cover: 1, height: "480" }, 184345402);
        }
      }
    });

    var cctv = CommonView.extend({
      namespace: "cctv"
    });
    var intercom = CommonView.extend({
      namespace: "intercom"
    });
    var learning = CommonView.extend({
      namespace: "learning"
    });
    var business = CommonView.extend({
      namespace: "business"
    });
    var support = CommonView.extend({
      namespace: "support"
    });

    var internet = CommonView.extend({
      namespace: "internet"
    });

    var myTransition = Barba.BaseTransition.extend({
      start: function start() {
        var _this = this;

        this.newContainerLoading.then(function () {
          return _this.fadeIn();
        }).then(this.finish.bind(this));
      },
      fadeIn: function fadeIn() {
        $("body").css({ overflow: "hidden" });
        return $(this.oldContainer).animate({ opacity: 0 }).promise();
      },
      finish: function finish() {
        var _this2 = this;

        $("body").removeAttr("style");
        $("html, body").scrollTop(0);
        $(".page-overlay").animate({ opacity: 0 }, 1000);
        var el = $(this.newContainer);

        $(this.oldContainer).hide();

        el.css({
          visibility: "visible",
          opacity: 0
        });

        el.animate({ opacity: 1 }, 400, function () {
          _this2.done();
        });
      }
    });

    Barba.Pjax.getTransition = function () {
      return myTransition;
    };

    Homepage.init();
    cctv.init();
    intercom.init();
    learning.init();
    business.init();
    support.init();
    internet.init();

    Barba.Pjax.start();

    Barba.Pjax.originalPreventCheck = Barba.Pjax.preventCheck;

    Barba.Pjax.preventCheck = function (evt, element) {
      if (element && element.getAttribute("href") && element.getAttribute("href").indexOf("#") > -1) {
        return true;
      } else {
        return Barba.Pjax.originalPreventCheck(evt, element);
      }
    };

    Barba.Dispatcher.on("linkClicked", function (target, event) {
      closeMenu();
      var href = $(target).attr("href");
      var hashIndex = $(target).attr("href").indexOf("#");
      if (hashIndex > -1) {
        var hashData = href.substr(hashIndex);
        if ($(hashData).offset() !== undefined) {
          scrollTo(hashData);
        }
      }
    });

    Barba.Dispatcher.on("transitionCompleted", function (target, event) {
      if (window.location.hash !== "") {
        setTimeout(function () {
          return scrollTo(window.location.hash);
        }, 400);
      } else {
        setTimeout(function () {
          return $("html, body").scrollTop(0);
        }, 200);
      }
    });

    $(".navbar-toggler").click(function (e) {
      $(this).toggleClass("is-active");
    });
  });
})();