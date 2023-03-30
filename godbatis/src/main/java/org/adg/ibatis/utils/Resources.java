package org.adg.ibatis.utils;

import java.io.InputStream;

/**
 *godbatis提供的一个工具类
 * 这个工具类专门完成类路径的加载
 * @author Brain
 * @since 1.0
 * @version 1.0
 */
public class Resources {

    private Resources(){}
    /**
     * 从类路径中加载资源
     * @param resource 类路径
     * @return 指向资源文件的一个输入流
     * */
    public static InputStream getResourceAsStream(String resource){
        return ClassLoader.getSystemClassLoader().getResourceAsStream(resource);
    }
}
