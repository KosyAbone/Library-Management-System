package controller;

import Model.User;

public interface UserAwareController {
    void setUser(User user);
}