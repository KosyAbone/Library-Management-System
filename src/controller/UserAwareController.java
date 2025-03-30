package controller;

/**
 *
 * @author kossy
 */
import Model.User;

public interface UserAwareController {
    void setUser(User user);
}