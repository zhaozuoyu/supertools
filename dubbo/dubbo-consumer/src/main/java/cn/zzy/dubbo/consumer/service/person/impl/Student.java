package cn.zzy.dubbo.consumer.service.person.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzy.dubbo.consumer.service.person.Person;

/**
 * @author zhaozuoyu
 * @date 2020/12/1
 */
public class Student implements Person {

    private static final Logger logger = LoggerFactory.getLogger(Student.class);

    @Override
    public void say() {
        logger.info("Hello Everyone! I am a student.");
    }
}
