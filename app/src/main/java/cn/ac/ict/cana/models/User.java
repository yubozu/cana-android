package cn.ac.ict.cana.models;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class User {
    public long id;
    public final String name;
    public final int age;
    public final boolean gender;

    public User(long userId, String userName, int userAge, boolean userGender) {
        id = userId;
        name = userName;
        age = userAge;
        gender = userGender;
    }

    public User(String userName, int userAge, boolean userGender) {
        id = 0;
        name = userName;
        age = userAge;
        gender = userGender;
    }
}
