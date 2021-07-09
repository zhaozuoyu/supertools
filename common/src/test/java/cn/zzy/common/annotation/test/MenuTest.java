package cn.zzy.common.annotation.test;

import java.lang.reflect.Field;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzy.common.annotation.Menu;
import cn.zzy.common.dto.MenuDTO;

/**
 * @author zhaozuoyu
 * @date 2020/11/27
 */
public class MenuTest {

    private static final Logger logger = LoggerFactory.getLogger(MenuTest.class);

    @Test
    public void test() {
        try {
            MenuDTO menuDTO = new MenuDTO("tv", "airConditioner");
            Class<? extends MenuDTO> menuDTOClass = menuDTO.getClass();
            Class<MenuDTO> menuDTOClass1 = MenuDTO.class;
            Object object = new Object();
            Class<?> aClass = object.getClass();

            Field[] fields = menuDTOClass.getDeclaredFields();
            for (Field field : fields) {
                Menu menu = field.getAnnotation(Menu.class);

                logger.info("fileld:{},id:{},type:{}", field.getName(), menu.id(), menu.type());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
