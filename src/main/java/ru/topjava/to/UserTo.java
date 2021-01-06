package ru.topjava.to;

import ru.topjava.entity.Role;
import ru.topjava.entity.User;

import java.util.Objects;
import java.util.Set;

public class UserTo {

    private final Integer id;
    private final String name;
    private final String email;
    private final String password;
    private final Set<Role> roles;

    public UserTo(User user) {
        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        password = user.getPassword();
        roles = user.getRoles();
    }

    public static UserTo getUserTo(User user) {
        return new UserTo(user);
    }

    public int getId() {
        return id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTo userTo = (UserTo) o;
        return id.equals(userTo.id) &&
                Objects.equals(name, userTo.name) &&
                email.equals(userTo.email) &&
                password.equals(userTo.password) &&
                roles.equals(userTo.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, roles);
    }
}
