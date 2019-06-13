package com.example.wyy.sharecard;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created on 15/9/22.
 * 拍照核心处理
 */
public class CameraCore {

    //调用系统相机的Code
    public static final int REQUEST_TAKE_PHOTO_CODE = 1001;
    //拍照裁剪的Code
    public static final int REQUEST_TAKE_PHOTO_CROP_CODE = 1003;
    //调用系统图库的Code
    public static final int REQUEST_TAKE_PICTURE_CODE = 1002;
    //调用系统图库裁剪Code
    public static final int REQUEST_TAKE_PICTURE_CROP_CODE = 1004;
    //裁剪的Code
    public static final int REQUEST_TAKE_CROP_CODE = 1005;
    //截取图片的高度
    public static final int REQUEST_HEIGHT = 400;
    //截取图片的宽度
    public static final int REQUEST_WIDTH = 400;
    //用来存储照片的URL
    private Uri photoURL;
    //调用照片的Activity
    private Activity activity;
    //回调函数
    private CameraResult cameraResult;

    public CameraCore(CameraResult cameraResult, Activity activity) {
        this.cameraResult = cameraResult;
        this.activity = activity;
    }

    //调用系统图库,对Intent参数进行封装
    protected Intent startTakePicture() {
        //this.photoURL = photoURL;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");//从所有图片中进行选择
        return intent;
    }

    //获取系统相册
    public void getPhoto2Album() {
        activity.startActivityForResult(startTakePicture(),REQUEST_TAKE_PICTURE_CODE);
        Toast.makeText(activity, "进入相册",Toast.LENGTH_SHORT).show();
    }



    //处理onActivityResult
    public void onResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //选择系统图库
                case REQUEST_TAKE_PICTURE_CODE:

                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(intent);
                    } else {
                        //4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(intent);

                    }
                    break;

                default:
                    break;
            }
        }
    }



    //4.4以下系统使用这个方法处理图片
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);

        if (!TextUtils.isEmpty(imagePath)) {
            cameraResult.onSuccess(imagePath);
        } else {
            cameraResult.onFail("文件没找到");
        }

    }

    //4.4及以上系统使用这个方法处理图片
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(activity, uri)) {
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //Uri的anthority是media格式的话，document id还需要再一次解析
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                //Uri是document,直接取出document id进行处理
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri,则使用普通方式处理
            imagePath = getImagePath(uri, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = uri.getPath();
        }

        if (!TextUtils.isEmpty(imagePath)) {
            cameraResult.onSuccess(imagePath);
        } else {
            cameraResult.onFail("文件没找到");
        }

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = activity.getContentResolver().query(uri, null, selection, null, null);
        if ( cursor != null ) {
            if ( cursor.moveToFirst() ) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }







    public void onRestoreInstanceState(Bundle savedInstanceState){
        photoURL = savedInstanceState.getParcelable("photoURL");
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("photoURL", photoURL);
    }



    //回调实例
    public interface CameraResult{
    	//成功回调
    	public void onSuccess(String filePaht);
    	//失败
    	public void onFail(String message);
    }
}