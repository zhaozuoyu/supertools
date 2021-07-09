package cn.zzy.dubbo.consumer.service.person.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.dubbo.common.extension.ExtensionLoader;

import cn.zzy.dubbo.consumer.ApplicationRun;
import cn.zzy.dubbo.consumer.service.person.Person;

/**
 * @author zhaozuoyu
 * @date 2020/12/1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRun.class)
public class PersonTest {

    private static final Logger logger = LoggerFactory.getLogger(PersonTest.class);

    /**
     * dubbo spi加载机制测试
     */
    @Test
    public void spiTest() {
        try {
            // 注意接口类前要加@SPI注解，否则无法
            ExtensionLoader<Person> extensionLoader = ExtensionLoader.getExtensionLoader(Person.class);
            Person teacher = extensionLoader.getExtension("teacher");
            Person student = extensionLoader.getExtension("student");
            teacher.say();
            student.say();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
