package com.hl.weblib.inner;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceResponse;

import com.hl.weblib.core.Constants;
import com.hl.weblib.core.ResourceInfo;
import com.hl.weblib.core.ResourceInfoEntity;
import com.hl.weblib.core.ResourceKey;
import com.hl.weblib.core.ResourceManager;
import com.hl.weblib.core.util.FileUtils;
import com.hl.weblib.core.util.GsonUtils;
import com.hl.weblib.core.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 资源管理类实现
 */
public class ResourceManagerImpl implements ResourceManager {
    private Map<ResourceKey, ResourceInfo> resourceInfoMap;
    private Context context;
    private Lock lock;

    public ResourceManagerImpl(Context context) {
        resourceInfoMap = new ConcurrentHashMap<>(16);
        this.context = context;
        lock = new ReentrantLock();
//        validator = new DefaultResourceValidator();
    }

    /**
     * 获取资源信息
     *
     * @param url 请求地址
     */
    @Override
    public WebResourceResponse getResource(String url) {
        //根据拦截的远程URL生成ResourceKey的实例，并据此在ResourceInfoMap中查找对应的本地资源，即ResourceInfo的实例
        ResourceKey key = new ResourceKey(url);

//        if (!lock.tryLock()) {
        Log.d("WebResourceResponse", url + "|" + "resourceInfoMap:" + resourceInfoMap.size());
//            return null;
//        }
        ResourceInfo resourceInfo = resourceInfoMap.get(key);


//        lock.unlock();
        if (resourceInfo == null) {
            return null;
        }
        //对于mimetype不在拦截范围的文件，则返回null并清除相应的key
       /* if (!MimeTypeUtils.checkIsSupportMimeType(resourceInfo.getMimeType())) {
            Logger.d("getResource [" + url + "]" + " is not support mime type,"+resourceInfo.getMimeType());
            safeRemoveResource(key);
            return null;
        }*/
        //如果对应的本地文件不存在，则返回null并清除相应的key
        InputStream inputStream = FileUtils.getInputStream(resourceInfo.getLocalPath());
        if (inputStream == null) {///data/user/0/com.hl.sun/files/offlinepackage/main/1/work/package/mobile-web-best-practice.html
            Logger.d("getResource [" + url + "]" + " inputStream is null");
            safeRemoveResource(key);
            return null;
        }
        //对资源文件进行md5校验
      /*  if (validator != null && !validator.validate(resourceInfo)) {
            safeRemoveResource(key);
            return null;
        }*/
        Log.d("qingqiu4", url);
        //高版本安卓返回资源的同时需要在响应头设置Access-Control相关字段
        WebResourceResponse response;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Map<String, String> header = new HashMap<>(2);
            header.put("Access-Control-Allow-Origin", "*");
            header.put("Access-Control-Allow-Headers", "Content-Type");
            response = new WebResourceResponse(resourceInfo.getMimeType(), "UTF-8", 200, "ok", header, inputStream);
        } else {
            response = new WebResourceResponse(resourceInfo.getMimeType(), "UTF-8", inputStream);
        }
        return response;
    }

    private void safeRemoveResource(ResourceKey key) {
        if (lock.tryLock()) {
            resourceInfoMap.remove(key);
            lock.unlock();
        }
    }

    @Override
    public boolean updateResource(String packageId, String version) {
        boolean isSuccess = false;
        //获取离线包的index.json
        String indexFileName =
                FileUtils.getPackageWorkName(context, packageId, version) + File.separator + Constants.RESOURCE_MIDDLE_PATH
                        + File.separator + Constants.RESOURCE_INDEX_NAME;
        Logger.d("updateResource indexFileName: " + indexFileName);
        File indexFile = new File(indexFileName);
        if (!indexFile.exists()) {
            Logger.e("updateResource indexFile is not exists ,update Resource error ");
            return isSuccess;
        }
        if (!indexFile.isFile()) {
            Logger.e("updateResource indexFile is not file ,update Resource error ");
            return isSuccess;
        }
        FileInputStream indexFis = null;
        try {
            indexFis = new FileInputStream(indexFile);
        } catch (Exception e) {

        }

        if (indexFis == null) {
            Logger.e("updateResource indexStream is error,  update Resource error ");
            return isSuccess;
        }
        //基于index.json生成对应的ResourceInfoEntity实例entity
        ResourceInfoEntity entity = GsonUtils.fromJsonIgnoreException(indexFis, ResourceInfoEntity.class);
        if (indexFis != null) {
            try {
                indexFis.close();
            } catch (IOException e) {

            }
        }
        if (entity == null) {
            return isSuccess;
        }
        List<ResourceInfo> resourceInfos = entity.getItems();
        isSuccess = true;
        if (resourceInfos == null) {
            return isSuccess;
        }
        String workPath = FileUtils.getPackageWorkName(context, packageId, version);
        //遍历entity的items，即resourceInfo集合，更新每个resourceInfo中packageId、localPath等属性，并更新到ResourceInfoMap
        for (ResourceInfo resourceInfo : resourceInfos) {
            if (TextUtils.isEmpty(resourceInfo.getPath())) {
                continue;
            }
            resourceInfo.setPackageId(packageId);
            String path = resourceInfo.getPath();
            path = path.startsWith(File.separator) ? path.substring(1) : path;
            resourceInfo.setLocalPath(
                    workPath + File.separator + Constants.RESOURCE_MIDDLE_PATH + File.separator + path);
            lock.lock();
            resourceInfoMap.put(new ResourceKey(resourceInfo.getRemoteUrl()), resourceInfo);
            lock.unlock();
        }
        return isSuccess;
    }

    @Override
    public String getPackageId(String url) {
        if (!lock.tryLock()) {
            return null;
        }
        ResourceInfo resourceInfo = resourceInfoMap.get(new ResourceKey(url));
        lock.unlock();
        if (resourceInfo != null) {//  /js/app.7ffe3e39.js
            return resourceInfo.getPackageId();
        }
        return null;
    }
}