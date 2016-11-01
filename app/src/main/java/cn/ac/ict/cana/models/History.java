package cn.ac.ict.cana.models;

import android.content.Context;

import static java.util.UUID.randomUUID;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class History {
    public long id;
    public final String uuid;
    public final String type;
    public String filePath;
    public boolean isUpload;
    public String createdTime;
    public int rating;
    public String doctor;
    public boolean ClinicalStatus;
    public boolean PDMedicine;
    public int Dopamine;

    public History(String uuid, String historyType, String filePath, int rating, String doctor, boolean ClinicalStatus, boolean PDMedicine, int Dopamine) {
        this.uuid = uuid;
        this.isUpload = false;
        this.filePath = filePath;
        this.type = historyType;
        this.rating = rating;
        this.doctor = doctor;
        this.ClinicalStatus = ClinicalStatus;
        this.PDMedicine = PDMedicine;
        this.Dopamine = Dopamine;
    }

    public static String getFilePath(Context context, String type) {
        String ext;
        switch (type) {
            case "Face":
                ext = ".mp4";
                break;
            case "Sound":
                ext = ".3gp";
                break;
            default:
                ext = ".txt";
        }
        return context.getFilesDir().getAbsolutePath()+"/" + randomUUID().toString() + ext;
    }

    public History(long historyId, String uuid, String historyType, String historyFilePath, boolean historyIsUpload, String historyCreatedTime, int rating, String doctor, boolean ClinicalStatus, boolean PDMedicine, int Dopamine) {
        id = historyId;
        this.uuid = uuid;
        type = historyType;
        filePath = historyFilePath;
        isUpload = historyIsUpload;
        createdTime = historyCreatedTime;
        this.rating = rating;
        this.doctor = doctor;
        this.ClinicalStatus = ClinicalStatus;
        this.PDMedicine = PDMedicine;
        this.Dopamine = Dopamine;
    }

}
