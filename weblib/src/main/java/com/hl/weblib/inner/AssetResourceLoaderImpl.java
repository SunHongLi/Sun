package com.hl.weblib.inner;

import android.content.Context;
import android.text.TextUtils;

import com.hl.weblib.core.AssetResourceLoader;
import com.hl.weblib.core.PackageInfo;
import com.hl.weblib.core.PackageStatus;
import com.hl.weblib.core.ResourceInfoEntity;
import com.hl.weblib.core.util.FileUtils;
import com.hl.weblib.core.util.GsonUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Function:
 * Date:2022/9/15
 * Author: sunHL
 */
public class AssetResourceLoaderImpl implements AssetResourceLoader {
    private Context context;

    public AssetResourceLoaderImpl(Context context) {
        this.context = context;
    }

    @Override
    public PackageInfo load(String path) {
        InputStream inputStream = null;

        //读取assets目录下的离线包
        inputStream = openAssetInputStream(path);
        if (inputStream == null) {
            return null;
        }

        String indexInfo = FileUtils.getStringForZip(inputStream);

        if (TextUtils.isEmpty(indexInfo)) {
            return null;
        }

        //基于assets下某个离线包生成ResourceInfoEntity实例，可获取离线包的version、packageId和ResourceInfo（具体每个远程资源对应一个本地资源）数组
        ResourceInfoEntity assetEntity = GsonUtils.fromJsonIgnoreException(indexInfo, ResourceInfoEntity.class);
        if (assetEntity == null) {
            return null;
        }

        //将assets目录下某个资源包拷贝到设定的目录下PackageAssetsName: ${root}/assets/${packageId}/${version}/package.zip
        String assetPath =
                FileUtils.getPackageAssetsName(context, assetEntity.getPackageId(), assetEntity.getVersion());

        inputStream = openAssetInputStream(path);
        if (inputStream == null) {
            return null;
        }
        boolean isSuccess = FileUtils.copyFile(inputStream, assetPath);
        if (!isSuccess) {
            return null;
        }
        FileUtils.safeCloseFile(inputStream);


        /*String md5 = MD5Utils.calculateMD5(new File(assetPath));
        if (TextUtils.isEmpty(md5)) {
            return null;
        }*/

        //将assets文件下某个压缩资源包的信息转化成PackageInfo实例的属性
        PackageInfo info = new PackageInfo();
        info.setPackageId(assetEntity.getPackageId());
        info.setStatus(PackageStatus.onLine);
        info.setVersion(assetEntity.getVersion());
//        info.setMd5(md5);
        return info;
    }

    private InputStream openAssetInputStream(String path) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(path);
        } catch (IOException e) {
        }
        if (inputStream == null) {
            return null;
        }
        return inputStream;
    }

}
