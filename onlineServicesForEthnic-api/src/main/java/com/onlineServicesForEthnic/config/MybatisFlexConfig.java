package com.onlineServicesForEthnic.config;

import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import com.mybatisflex.core.logicdelete.impl.IntegerLogicDeleteProcessor;
import com.mybatisflex.core.mybatis.FlexConfiguration;
import com.mybatisflex.spring.boot.ConfigurationCustomizer;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import com.mybatisflex.spring.boot.SqlSessionFactoryBeanCustomizer;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@MapperScan("com.onlineServicesForEthnic.mapper")
public class MybatisFlexConfig implements MyBatisFlexCustomizer, ConfigurationCustomizer, SqlSessionFactoryBeanCustomizer {


    //MybatisFlex配置
    @Override
    public void customize(FlexGlobalConfig flexGlobalConfig) {
        //逻辑删除处理器
        LogicDeleteManager.setProcessor(new IntegerLogicDeleteProcessor());
        //主键
        FlexGlobalConfig.KeyConfig keyConfig = new FlexGlobalConfig.KeyConfig();
        keyConfig.setKeyType(KeyType.Generator);
        keyConfig.setValue(KeyGenerators.flexId);
        flexGlobalConfig.setKeyConfig(keyConfig);
    }

    //Mybatis配置
    @Override
    public void customize(FlexConfiguration flexConfiguration) {
        //设置日志控制台输出
        flexConfiguration.setLogImpl(StdOutImpl.class);
        //二级缓存
        flexConfiguration.setCacheEnabled(true);

    }

    //SqlSessionFactory配置
    @Override
    public void customize(SqlSessionFactoryBean sqlSessionFactoryBean) {

    }
}
