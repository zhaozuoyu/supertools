package cn.zzy.spring.controller;

import cn.zzy.spring.aspect.PointcutComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaozuoyu
 * @date 2021/9/18
 */
@RestController
@RequestMapping("/aspect")
public class AspectController {

    @Autowired
    private PointcutComponent pointcutComponent;

    @RequestMapping("/execute")
    public ResponseEntity<String> rollbackLarkVideo() {
        pointcutComponent.execute();
        return new ResponseEntity<>("aspect already execute", HttpStatus.OK);
    }
}
