package org.adg.ibatis.core;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * JDBC事务管理器(godbatis框架目前只对JdbcTransaction进行实现)
 * @author Brain
 * @since 1.0
 * @version 1.0
 */
public class JdbcTransaction implements Transaction{
    /**
     * 数据源属性
     */
    private DataSource dataSource;

    /**自动提交标记
     * true表示自动提交
     * false表示不采用自动提交，需要手动提交
     */
    private boolean autoCommit;

    /**
     * 连接对象
     */
    private Connection connection;

    /**
     * 创建事务管理器对象
     * @param dataSource 数据源对象
     * @param autoCommit 自动提交标志
     */
    public JdbcTransaction(DataSource dataSource, boolean autoCommit) {
        this.dataSource = dataSource;
        this.autoCommit = autoCommit;
    }

    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Connection getConnection() {
        return connection;

    }

    @Override
    public void openConnection(){
        if (connection == null) {
            try {
                connection =  dataSource.getConnection();
                //开启事务
                connection.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
