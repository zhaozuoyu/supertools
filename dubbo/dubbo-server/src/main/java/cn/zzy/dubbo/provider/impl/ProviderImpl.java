package cn.zzy.dubbo.provider.impl;

import cn.zzy.dubbo.dao.BaseDao;
import cn.zzy.dubbo.provider.Provider;
import com.alibaba.dubbo.config.annotation.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service(version = "1.0.0")
public class ProviderImpl<T,keyType extends Serializable> implements Provider<T,keyType>{

    private BaseDao<T,keyType> baseDao;

    @Override
    public List<T> findAll() {
        List<T> list = new ArrayList<T>();
        Iterable<T> all = baseDao.findAll();
        Iterator<T> iterator = all.iterator();
        while (iterator.hasNext()){
            T next = iterator.next();
            list.add(next);
        }
        return list;
    }
}
