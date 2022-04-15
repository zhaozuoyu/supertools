package cn.zzy.spring.aspect;


import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zhaozuoyu
 * @date 2021/9/18
 */
@Component
public class PointcutComponent {

    private static final Logger logger = LoggerFactory.getLogger(PointcutComponent.class);

    @Pointcut("execution(* *.execute(..))")
    public void execute() {
        logger.info("**********************execute pointcut method**********************");
    }
}
