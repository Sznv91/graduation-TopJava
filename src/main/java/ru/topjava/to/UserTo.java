package ru.topjava.to;

import ru.topjava.entity.Role;
import ru.topjava.entity.User;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

public class UserTo {

    private final int id;
    private final String name;
    private final String password;
    private final Set<Role> roles;

    public UserTo(User user) {
        id = user.getId();
        name = user.getName();
        password = user.getPassword();
        roles = user.getRoles();
    }

    public static UserTo getUserTo(User user) {
        return new UserTo(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTo userTo = (UserTo) o;
        return id == userTo.id &&
                name.equals(userTo.name) &&
                password.equals(userTo.password) &&
                roles.equals(userTo.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, roles);
    }
}
