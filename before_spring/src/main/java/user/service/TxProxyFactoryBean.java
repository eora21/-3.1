package user.service;

import java.lang.reflect.Proxy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

public class TxProxyFactoryBean implements FactoryBean<Object> {
    private final Object target;
    private final PlatformTransactionManager transactionManager;
    private final Class<?> serviceInterface;
    private final String pattern;

    public TxProxyFactoryBean(Object target, PlatformTransactionManager transactionManager, Class<?> serviceInterface) {
        this.target = target;
        this.transactionManager = transactionManager;
        this.serviceInterface = serviceInterface;
        this.pattern = "";
    }

    public TxProxyFactoryBean(Object target, PlatformTransactionManager transactionManager, Class<?> serviceInterface,
                              String pattern) {
        this.target = target;
        this.transactionManager = transactionManager;
        this.serviceInterface = serviceInterface;
        this.pattern = pattern;
    }

    @Override
    public Object getObject() throws Exception {
        TransactionHandler transactionHandler = new TransactionHandler(target, transactionManager, pattern);
        return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{serviceInterface}, transactionHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
