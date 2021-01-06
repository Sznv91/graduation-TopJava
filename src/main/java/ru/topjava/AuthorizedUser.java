package ru.topjava;


//import ru.javawebinar.topjava.to.UserTo;
//import ru.javawebinar.topjava.util.UserUtil;
import ru.topjava.entity.User;
import ru.topjava.to.UserTo;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    private UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), true, true, true, true, user.getRoles());
        this.userTo = UserTo.getUserTo(user); //UserUtil.asTo(user);
    }

    public int getId() {
        System.out.println("SPRING SECURITY SPRING SECURITY SPRING SECURITY user to id " + userTo.getId());
        return userTo.getId();
    }

    public void update(UserTo newTo) {
        userTo = newTo;
    }

    public UserTo getUserTo() {
        return userTo;
    }

    @Override
    public String toString() {
        return userTo.toString();
    }
}