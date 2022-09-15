package com.hl.weblib;

import android.content.Context;
import android.util.Log;
import android.webkit.WebResourceResponse;

import com.hl.weblib.core.AssetResourceLoader;
import com.hl.weblib.core.Constants;
import com.hl.weblib.core.PackageInfo;
import com.hl.weblib.core.PackageInstaller;
import com.hl.weblib.core.ResourceManager;
import com.hl.weblib.core.util.Logger;
import com.hl.weblib.inner.AssetResourceLoaderImpl;
import com.hl.weblib.inner.PackageInstallerImpl;
import com.hl.weblib.inner.ResourceManagerImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 离线包管理器
 */
public class PackageManager {
    private static final int STATUS_PACKAGE_CANUSE = 1;
    private volatile static PackageManager instance;
    private Context context;
    private ResourceManager resourceManager;
    private PackageInstaller packageInstaller;
    private AssetResourceLoader assetResourceLoader;
    private Lock resourceLock;
    private Map<String, Integer> packageStatusMap = new HashMap<>();

    public static synchronized PackageManager getInstance() {
        if (instance == null) {
            instance = new PackageManager();
        }
        return instance;
    }

    private PackageManager() {
        resourceLock = new ReentrantLock();
    }

    public void init(Context context) {
        Logger.d("PackageManager " + "init");
        this.context = context;

        resourceManager = new ResourceManagerImpl(context);
        packageInstaller = new PackageInstallerImpl(context);
        assetResourceLoader = new AssetResourceLoaderImpl(context);
        performLoadAssets();
    }

    //获取预置在assets中的离线包并解压到相应目录
    private void performLoadAssets() {
        for (int i = 0; i < Constants.LOCAL_ASSET_LIST.length; i++) {
            PackageInfo packageInfo = assetResourceLoader.load(Constants.LOCAL_ASSET_LIST[i]);
            if (packageInfo == null) {
                return;
            }
            Logger.d("PKGM2 "+packageInfo.getPackageId());
            Logger.d("performLoadAssets "+"installPackage");
            installPackage(packageInfo.getPackageId(), packageInfo, true);
        }
    }

    public WebResourceResponse getResource(String url) {
        synchronized (packageStatusMap) {

            String packageId = resourceManager.getPackageId(url);
            Integer status = packageStatusMap.get(packageId);
            Logger.d("WebResourceResponse " + status + " | " + url + " | " + packageId + "| packageStatusMap size:" + packageStatusMap.size());
            if (status == null) {
                return null;
            }
            if (status != STATUS_PACKAGE_CANUSE) {
                return null;
            }
        }
        WebResourceResponse resourceResponse = null;

        synchronized (resourceManager) {
            resourceResponse = resourceManager.getResource(url);
        }
        return resourceResponse;
    }

    //将预置在assets或刚下载的离线包解压到指定目录
    private void installPackage(String packageId, PackageInfo packageInfo, boolean isAssets) {
        if (packageInfo != null) {
            //暂时关闭对下载文件的MD5校验
//            boolean isValid = (isAssets && assetValidator.validate(packageInfo)) || validator.validate(packageInfo);
//            if (isValid) {
            resourceLock.lock();
            boolean isSuccess = packageInstaller.install(packageInfo, isAssets);
            resourceLock.unlock();
            //安装失败情况下，不做任何处理，因为资源既然资源需要最新资源，失败了，就没有必要再用缓存了
            if (isSuccess) {
                Logger.d("installPackage "+"version" + packageInfo.getVersion() + "| isAssets " + isAssets);
                resourceManager.updateResource(packageInfo.getPackageId(), packageInfo.getVersion());
                //更新安装成功的离线包版本到packageIndex.json
//                updatePackageIndexFile(packageInfo.getPackageId(), packageInfo.getVersion());
                synchronized (packageStatusMap) {
                    packageStatusMap.put(packageId, STATUS_PACKAGE_CANUSE);
                }
            }
//            }
        }
    }

}
