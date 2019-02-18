package com.crazywah.tools;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;

/**
 * 图片操作类
 */
public class PictureUtils {

    public static final String FILE_PATH = "~/PiedPiper";

    /**
     * 将 BASE64 编码的图片还原为
     *
     * @param base64        经过 BASE64 编码的字符串
     * @param fileName      MD5 加密的（用户名 + 保存时间）图片名
     * @param accountName   MD5 加密的用户名
     * @throws FileAlreadyExistsException 文件已存在异常
     * @throws IOException                输入输出异常
     */
    public static void savePic(String base64, String accountName, String fileName) throws FileAlreadyExistsException, IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] read = decoder.decodeBuffer(base64);
        File dir = new File(FILE_PATH + accountName + "/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(FILE_PATH + fileName);
        if (file.exists()) {
            throw new FileAlreadyExistsException(dir.getAbsolutePath() + "/" + fileName);
        } else {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(read);
            fos.flush();
            fos.close();
        }

    }

    /**
     * 读取一个图片为 BASE64 字符串
     *
     * @param fileName 要读取的文件的文件名
     * @return BASE64 编码的字符串
     * @throws FileNotFoundException 没有这个图片异常
     * @throws IOException           输入输出异常
     */
    public static String readPic(String accountName, String fileName) throws FileNotFoundException, IOException {
        String base64 = null;
        BASE64Encoder encoder = new BASE64Encoder();
        File file = new File(FILE_PATH + "/" + accountName + "/" + fileName);
        if (!file.exists()) {
            throw new FileNotFoundException(FILE_PATH + "/" + accountName + "/" + fileName + ".jpg");
        } else {
            FileInputStream fis = new FileInputStream(file);
            byte[] read = new byte[fis.available()];
            fis.read(read);
            base64 = encoder.encode(read);
            fis.close();
        }
        return base64;
    }

    /**
     * 清除某用户名下的所有图片
     *
     * @param accountName
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void clearAccountPic(String accountName) throws FileNotFoundException, IOException {
        File dir = new File(FILE_PATH + "/" + accountName + "/");
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                file.deleteOnExit();
            }
        }
        dir.deleteOnExit();
    }

    /**
     * 用 MD5 加密图片名称
     * @param accountId
     * @param time
     * @return
     */
    public static String getPicName(String accountId, long time) {
        return CheckSumBuilder.getMD5(accountId + time + ".jpg");
    }

}
