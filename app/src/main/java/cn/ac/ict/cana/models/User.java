package cn.ac.ict.cana.models;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class User {
    public long id;
    public String name;
    public int age;
    public boolean gender;

    public User(long user_id, String user_name, int user_age, boolean user_gender) {
        id = user_id;
        name = user_name;
        age = user_age;
        gender = user_gender;
    }

    public User(String user_name, int user_age, boolean user_gender) {
        id = 0;
        name = user_name;
        age = user_age;
        gender = user_gender;
    }
}
