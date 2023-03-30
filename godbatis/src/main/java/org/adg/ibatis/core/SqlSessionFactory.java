package org.adg.ibatis.core;

import java.util.Map;

/**
 * sqlSessionFactory对象：
 *      一个数据库对应一个sqlSessionFactory对象，
 *      通过sqlSessionFactory对象可以获取sqlSession对象。（开启会话）
 *      一个sqlSessionFactory对象可以开启多个sqlSession会话
 * @author Brain
 * @since 1.0
 * @version 1.0
 */
public class SqlSessionFactory {
    /**
     * 事务管理器属性
     * sqlSessionFactory类中的事务管理器是可以灵活切换的
     * sqlSessionFactory类中应该有一个事务管理器接口
     */
    private Transaction transaction;

    /**
     *存放sql语句的map集合
     * key是sqlId
     * value是对应的SQL标签信息对象
     */
    private Map<String,MapperStatement> mapperStatements;

    /**
     * 获取sql会话对象
     * @return SqlSession
     */
    public SqlSession openSession(){
        //开启会话的前提是开启连接
        transaction.openConnection();
        //创建sqlSession对象
        //sqlSessionFactory对象有两个属性，并且都需要传入进去，所以传个this即可
        SqlSession sqlSession = new SqlSession(this);
        return sqlSession;
    }
    /**
     *无参数构造方法
     */
    public SqlSessionFactory() {
    }

    public SqlSessionFactory(Transaction transaction, Map<String, MapperStatement> mapperStatements) {
        this.transaction = transaction;
        this.mapperStatements = mapperStatements;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Map<String, MapperStatement> getMapperStatements() {
        return mapperStatements;
    }

    public void setMapperStatements(Map<String, MapperStatement> mapperStatements) {
        this.mapperStatements = mapperStatements;
    }


}
