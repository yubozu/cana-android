package cn.ac.ict.cana.models;

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

    public History(long historyUserId, String historyType){
        userId = historyUserId;
        type = historyType;

        // TODO: set as app storage path;
        filePath = "test file path";
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
