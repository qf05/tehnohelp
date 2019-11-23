package ru.lugaonline.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/message")
public class MessageController {

//        @GetMapping
//    public void openForm() {
////        return new BaseResponse(SUCCESS_STATUS, 1);
//    }

    @PostMapping
    public RedirectView sendMessage(@RequestParam(value = "tel") String tel, @RequestParam(value = "message") String message) {
        return null;
    }
}
