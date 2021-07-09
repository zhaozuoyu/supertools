package cn.zzy.javassist.generate.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.zzy.javassist.ApplicationRun;
import javassist.*;

/**
 * @author zhaozuoyu
 * @date 2020/12/4
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRun.class)
public class GenerateClassTest {

    private static final Logger logger = LoggerFactory.getLogger(GenerateClassTest.class);

    @Test
    public void createPersonTest() {
        try {
            ClassPool pool = ClassPool.getDefault();
            // 1. 创建一个空类
            CtClass cc = pool.makeClass("cn.zzy.javassist.entity.Person");
            // 2. 新增一个字段 private String username;
            CtField username = new CtField(pool.get("java.lang.String"), "username", cc);
            // 访问级别是 private
            username.setModifiers(Modifier.PRIVATE);
            // 初始值是 "elise"
            cc.addField(username, CtField.Initializer.constant("elise"));

            CtField age = new CtField(pool.get("int"), "age", cc);
            age.setModifiers(Modifier.PRIVATE);
            cc.addField(age, CtField.Initializer.constant(22));

            // 3. 生成 getter、setter 方法
            cc.addMethod(CtNewMethod.setter("setUsername", username));
            cc.addMethod(CtNewMethod.getter("getUsername", username));

            // 4. 添加无参的构造函数
            CtConstructor cons = new CtConstructor(new CtClass[] {}, cc);
            cons.setBody("{username = \"lucy\";}");
            cc.addConstructor(cons);

            // 5. 添加有参的构造函数
            cons = new CtConstructor(new CtClass[] {pool.get("java.lang.String")}, cc);
            // $0=this / $1,$2,$3... 代表方法参数
            cons.setBody("{$0.username = $1;}");
            cc.addConstructor(cons);

            // 6. 创建一个名为printUsername方法，无参数，无返回值，输出username值
            CtMethod ctMethod = new CtMethod(CtClass.voidType, "printUsername", new CtClass[] {}, cc);
            ctMethod.setModifiers(Modifier.PUBLIC);
            ctMethod.setBody("{System.out.println(username);}");
            cc.addMethod(ctMethod);

            String path = System.getProperty("user.dir");
            path = path + "\\src\\main\\java\\";
            logger.info(path);

            // 这里会将这个创建的类对象编译为.class文件
            String s = cc.toString();
            logger.info(s);
            cc.writeFile(path);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}
