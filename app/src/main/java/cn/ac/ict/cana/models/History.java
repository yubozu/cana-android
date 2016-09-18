package cn.ac.ict.cana.models;

import android.content.Context;

import static java.util.UUID.randomUUID;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class History {
    public long id;
    public final long userId;
    public final String type;
    public String filePath;
    public boolean isUpload;
    public String createdTime;
    public String ext;

    public History(Context context, long historyUserId, String historyType){
        userId = historyUserId;
        type = historyType;

        switch (type) {
            case "Face":
                ext = ".mp4";
                break;
            case "Sound":
                ext = ".mp3";
                break;
            default:
                ext = ".txt";
        }
        filePath = context.getFilesDir().getAbsolutePath()+"/" + randomUUID().toString() + ext;

        isUpload = false;
    }

    public History(long historyId, long historyUserId, String historyType, String historyFilePath, boolean historyIsUpload, String historyCreatedTime) {
        id = historyId;
        userId = historyUserId;
        type = historyType;
        filePath = historyFilePath;
        isUpload = historyIsUpload;
        createdTime = historyCreatedTime;
    }

}
