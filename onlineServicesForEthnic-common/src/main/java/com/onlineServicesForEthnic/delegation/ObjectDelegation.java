package com.onlineServicesForEthnic.delegation;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.function.Function;


/**
 * <p>一个对象委托池小工具</p>
 *
 * @param <T> 委托对象类
 */
public class ObjectDelegation<T> extends GenericObjectPool<T> {
    public ObjectDelegation(PooledObjectFactory<T> factory) {
        super(factory);
    }

    public <R> R execute(Function<T, R> action) throws Exception {
        T o = this.borrowObject();
        R value;
        try {
            value = action.apply(o);
        } finally {
            this.returnObject(o);
        }
        return value;
    }
}