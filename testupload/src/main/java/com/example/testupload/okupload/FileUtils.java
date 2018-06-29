package com.example.testupload.okupload;

/**
 * <p>@author : tyh</p>
 * <p>@time : 2018/06/28</p>
 * <p>@for : $s$</p>
 * <p></p>
 */
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUtils {


    /**
     * 将文件分割成块来上传
     * @param offset 偏移长度
     * @param file   目标文件
     * @param blockSize 分块数量
     * @return 分块后的byte数据集合
     */
    public static byte[] getBlock(long offset, File file, int blockSize) {
        byte[] result = new byte[blockSize];
        RandomAccessFile accessFile = null;
        try {
            accessFile = new RandomAccessFile(file, "r");
            accessFile.seek(offset);
            int readSize = accessFile.read(result);
            if (readSize == -1) {
                return null;
            } else if (readSize == blockSize) {
                return result;
            } else {
                byte[] tmpByte = new byte[readSize];
                System.arraycopy(result, 0, tmpByte, 0, readSize);
                return tmpByte;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (accessFile != null) {
                try {
                    accessFile.close();
                } catch (IOException e1) {
                }
            }
        }
        return null;
    }

}