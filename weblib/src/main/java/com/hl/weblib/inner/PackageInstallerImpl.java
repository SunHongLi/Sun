package com.hl.weblib.inner;

import android.content.Context;

import com.hl.weblib.core.PackageInfo;
import com.hl.weblib.core.PackageInstaller;
import com.hl.weblib.core.util.FileUtils;
import com.hl.weblib.core.util.Logger;

/**
 * Function:
 * Date:2022/9/15
 * Author: sunHL
 */
public class PackageInstallerImpl implements PackageInstaller {
    private Context context;
    private final static String TAG = "PackageInstallerImpl";

    public PackageInstallerImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean install(PackageInfo packageInfo, boolean isAssets) {
        boolean isSuccess;
        //获取刚下载的离线包download.zip的路径，或者是预先加载到assets的离线包package.zip的路径
        String downloadFile =
                isAssets ? FileUtils.getPackageAssetsName(context, packageInfo.getPackageId(), packageInfo.getVersion())
                        : FileUtils.getPackageDownloadName(context, packageInfo.getPackageId(), packageInfo.getVersion());
        String willCopyFile = downloadFile;

        //获取即将被更新的离线包update.zip的路径
        String updateFile =
                FileUtils.getPackageUpdateName(context, packageInfo.getPackageId(), packageInfo.getVersion());

        //拷贝downloadFile(download.zip 或 res.zip)或合并增量包生成的merge.zip到update.zip,并删除刚被拷贝的文件
        isSuccess = FileUtils.copyFileCover(willCopyFile, updateFile);
        if (!isSuccess) {
            Logger.e("[" + packageInfo.getPackageId() + "] : " + "copy file error ");
            return false;
        }
        isSuccess = FileUtils.delFile(willCopyFile);
        if (!isSuccess) {
            Logger.e("[" + packageInfo.getPackageId() + "] : " + "delete will copy file error ");
            return false;
        }

        //解压已经更新过的update.zip资源包到work目录下
        String workPath = FileUtils.getPackageWorkName(context, packageInfo.getPackageId(), packageInfo.getVersion());
        try {
            isSuccess = FileUtils.unZipFolder(updateFile, workPath);
        } catch (Exception e) {
            isSuccess = false;
        }
        if (!isSuccess) {
            Logger.e("[" + packageInfo.getPackageId() + "] : " + "unZipFolder error ");
            return false;
        }
        if (isSuccess) {
            FileUtils.deleteFile(willCopyFile);
            //cleanOldFileIfNeed(packageInfo.getPackageId(), packageInfo.getVersion(), lastVersion);
        }

        return isSuccess;
    }
}
