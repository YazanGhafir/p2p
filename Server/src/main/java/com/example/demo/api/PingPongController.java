package com.example.demo.api;

import com.example.demo.model.Ping;
import com.example.demo.model.SenderNotice;
import com.example.demo.model.TransporterNotice;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/vi/pingpong")
@RestController
public class PingPongController {

    PingPongController(){}

    @PostMapping
    public Ping pingpong (@RequestBody Ping ping){
        return new Ping("ping");
    }

}
