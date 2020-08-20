package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class TestController {
    @RequestMapping(value = "/hi")
    public ResponseEntity<String> getProduct() {
        return new ResponseEntity<>("Sử dụng tài khoản dưới đây để đăng nhập vào ứng dụng MoMo Testing hoặc Website để thanh toán", HttpStatus.OK);
    }
    @RequestMapping(value = "/returnUrl")
    public ResponseEntity<String> returnUrl(@RequestParam Map<String, String> parameterMap) {
        System.out.println("#############returnUrl");
        System.out.println(parameterMap.toString());
        return new ResponseEntity<>(parameterMap.toString(), HttpStatus.OK);
    }
    @RequestMapping(value = "/notifyOder",
            method = RequestMethod.POST,
            produces = "application/x-www-form-urlencoded;charset=UTF-8")
    public ResponseEntity<String> notifyOder(@RequestParam Map<String, String> parameterMap) {
        System.out.println("#############notifyOder");
        System.out.println(parameterMap.toString());
        return new ResponseEntity<>(parameterMap.toString(), HttpStatus.OK);
    }
}
