/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.dozen.commonbase.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static java.lang.System.in;

public class FileUtil {

    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }

    public static boolean copyfile(File fromFile, File toFile, Boolean rewrite) {
        if (!fromFile.exists()) {
            return false;
        }
        if (!fromFile.isFile()) {
            return false;
        }
        if (!fromFile.canRead()) {
            return false;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }
        try {
            java.io.FileInputStream fosfrom = new java.io.FileInputStream(fromFile);
            java.io.FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); //将内容写到新文件当中
            }
            fosfrom.close();
            fosto.close();
        } catch (Exception ex) {
            Log.e("readfile", ex.getMessage());
        }
        return true;
    }

    /*
     * 从Apk本地Assets复制文件到指定文件夹
     *
     */
    public static boolean copyFileFromAssets(Context context, String filePath, String fileName) {
        boolean result = false;
//        String filepath="data/data/包名（写错会出现没有权限的错误信息）/databases";
//        String fileName="paipai_doc_first.doc";

        File dir = new File(filePath);
        File file = new File(dir, fileName);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        // 检查 SQLite 数据库文件是否存在
        if (!file.exists()) {
            try {
                file.createNewFile();

                inputStream = context.getAssets().open(fileName);
                outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len;

                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                result = true;

            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {

            try {
                file.delete();
                file.createNewFile();

                inputStream = context.getAssets().open(fileName);
                outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len;

                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                result = true;

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        return result;
    }

    /**
     * 保存文件到本地
     *
     * @throws Exception
     */
    public static File writeOcrStrtoFile(String result, String outPath, String outFileName) throws Exception {
        File dir = new File(outPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File txt = new File(outPath + "/" + outFileName);
        if (!txt.exists()) {
            txt.createNewFile();
        }
        byte bytes[] = new byte[512];
        bytes = result.getBytes();
        int b = bytes.length; // 是字节的长度，不是字符串的长度
        FileOutputStream fos = new FileOutputStream(txt);
//		fos.write(bytes, 0, b);
        fos.write(bytes);
        fos.flush();
        fos.close();
        return txt;
    }


    //从系统返回的uir中获取文件
    public static File uriToFile(Uri uri, Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;

    }

    //保存bitmap为本地文件
    public static File getFile(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        File file = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            int x = 0;
            byte[] b = new byte[1024 * 100];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 把batmap 转file
     *
     * @param bitmap
     * @param filepath
     */
    public static File saveBitmapFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 根据路径 转bitmap
     *
     * @param urlpath
     * @return
     */
    public static Bitmap getBitMBitmap(String urlpath) {

        Bitmap map = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            map = BitmapFactory.decodeStream(in);
            // TODO Auto-generated catch block
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    //根据图片文件路径转成byte[]
    public static byte[] readFileToByteArray(String path) {
        File file = new File(path);
        if (!file.exists()) {
            Log.e("TAG", "File doesn't exist!");
            return null;
        }
        try {
            FileInputStream in = new FileInputStream(file);
            long inSize = in.getChannel().size();//判断FileInputStream中是否有内容
            if (inSize == 0) {
                Log.d("TAG", "The FileInputStream has no content!");
                return null;
            }

            byte[] buffer = new byte[in.available()];//in.available() 表示要读取的文件中的数据长度
            in.read(buffer);  //将文件中的数据读到buffer中
            return buffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                return null;
            }
            //或IoUtils.closeQuietly(in);
        }
    }

    //bitmap生成byteArray
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0,i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                //F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }

}
