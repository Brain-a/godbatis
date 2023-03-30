package org.adg.ibatis.core;

import java.sql.Connection;

/**
 * 事务管理器接口
 * 所有的事务管理器都应该实现该接口
 * JDBC事务管理器，MANAGED事务管理器都应该实现这个接口
 * Transaction事务管理器，提供事务管理方法
 * @author Brain
 * @since 1.0
 * @version 1.0
 */
public interface Transaction {

    /**
     * 提交事务
     */
    void commit();
    /**
     * 回滚事务
     */
    void rollback();
    /**
     * 关闭事务
     */
    void close();
    /**
     * 开启数据库的连接
     */
    void openConnection();
    /**
     * 获取数据库连接对象的
     * @return 数据库连接对象
     **/
    Connection getConnection();

}
