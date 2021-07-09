package cn.zzy.dubbo.consumer.service.pet.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.zzy.dubbo.consumer.service.pet.PetService;

/**
 * @author zhaozuoyu
 * @date 2020/12/7
 */
@Service
public class DefaultPetServiceImpl implements PetService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultPetServiceImpl.class);

    @Override
    public void swimming() {
        logger.info("fish swimming at stream");
    }
}
