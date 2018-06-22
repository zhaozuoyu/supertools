package cn.zzy.dubbo.dao;

import cn.zzy.common.pojo.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public interface BaseDao<T,keyType extends Serializable> extends CrudRepository<T,keyType > {

}
