package cn.zzy.dubbo.consumer.service.member.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.zzy.dubbo.consumer.ApplicationRun;
import cn.zzy.dubbo.consumer.service.member.MemberService;
import cn.zzy.dubbo.dto.UserDTO;

/**
 * @author zhaozuoyu
 * @date 2020/12/1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRun.class)
public class MemberServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceTest.class);

    @Autowired
    private MemberService memberService;

    @Test
    public void saveUserTest() {
        try {
            UserDTO userDTO =
                new UserDTO().setId(100001).setUsername("elise").setAge(22).setGender(1).setPassword("elise.password");
            Boolean result = memberService.saveUser(userDTO);
            logger.info("result:{}", result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
