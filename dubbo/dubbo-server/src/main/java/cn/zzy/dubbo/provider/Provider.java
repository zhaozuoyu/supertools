package cn.zzy.dubbo.provider;


import java.io.Serializable;
import java.util.List;

public interface Provider<T,keyType extends Serializable>{

    List<T> findAll();

}
