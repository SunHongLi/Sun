package com.hl.weblib.core.util;

import android.app.Application;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * gson 工具
 */
public class GsonUtils {

    private static Gson gson = new Gson();

    /**
     * 在格式错误时不抛异常, 返回null, 为了处理服务器500+的情况, 会返回一个普通字符串
     */
    public static <T> T fromJsonIgnoreException(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (Throwable ignore) {
            return null;
        }
    }

    /**
     * BufferedReader(缓冲区读取内容，避免中文乱码)(因为输入的数据有可能出现中文，所以此处使用字符流完成)
     *
     * @param fileName XXX.json
     */
    public static <T> T parseFromAssetJson(Application context, String fileName, Class<T> classOfT) {
        try {
            return fromJsonIgnoreException(context.getAssets().open(fileName), classOfT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 在格式错误时不抛异常, 返回null, 为了处理服务器500+的情况, 会返回一个普通字符串
     */
    public static <T> T fromJsonIgnoreException(InputStream json, Class<T> classOfT) {
        Reader reader = null;
        BufferedReader bufferedReader = null;
        T entity = null;
        try {
            //json:字节输入流
            //InputStreamReader:将字节流变成字符流
            //BufferedReader：将字符流放到字符流缓冲区之中
            reader = new InputStreamReader(json);
            bufferedReader = new BufferedReader(reader);
            entity = gson.fromJson(bufferedReader, classOfT);
        } catch (Throwable ignore) {

        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                }
            }
        }
        return entity;
    }
}
