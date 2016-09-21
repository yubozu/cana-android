package cn.ac.ict.cana.events;

import cn.ac.ict.cana.models.User;

/**
 * Author: saukymo
 * Date: 9/21/16
 */
public class NewUser {
    final public User user;
    public NewUser(User user) {
        this.user = user;
    }
}
