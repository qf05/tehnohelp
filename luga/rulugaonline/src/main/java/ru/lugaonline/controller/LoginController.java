package ru.lugaonline.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import ru.lugaonline.model.User;

import static ru.lugaonline.Params.*;

@RestController
@RequestMapping("/login")
public class LoginController {

//    private final String sharedKey = "SHARED_KEY";
//
//    private static final String SUCCESS_STATUS = "success";
//    private static final String ERROR_STATUS = "error";
//    private static final int CODE_SUCCESS = 100;
//    private static final int AUTH_FAILURE = 102;

//    @GetMapping
//    public String list() {
//        return new BaseResponse(SUCCESS_STATUS, 1);
//    }

    @PostMapping
    public RedirectView login(@RequestParam(value = "tel") String tel) {
        String url;
        if (!tel.equals("9219241224")) {
            User user = new User(tel);

            //save User if not found else user = from db

            url = "https://oauth.vk.com/authorize?client_id=" + APP_ID
                    + "&redirect_url=" + AFTER_LOGIN_URL
                    + "&response_type=token&scope=groups,friends,offline&v=5.92";

        } else {
            System.out.println("Admin");
            url = MAIN_URL + "/admin"; //login_admin"; //???????
        }
        return new RedirectView(url);
    }
}
