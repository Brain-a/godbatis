package org.adg.ibatis.core;

import java.sql.Connection;

/**
 * Managed事务管理器
 * @author Brain
 * @since 1.0
 * @version 1.0
 */
public class ManagedTransaction implements Transaction{
    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void close() {

    }

    @Override
    public void openConnection() {

    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
