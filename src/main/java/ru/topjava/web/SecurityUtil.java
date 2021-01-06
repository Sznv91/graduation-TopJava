package ru.topjava.web;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.topjava.AuthorizedUser;
import ru.topjava.entity.AbstractBaseEntity;

import static java.util.Objects.requireNonNull;

public class SecurityUtil {

//    private static int id = AbstractBaseEntity.START_SEQ;

    public static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    private SecurityUtil() {
    }

    public static AuthorizedUser get() {
        return requireNonNull(safeGet(), "No authorized user found");
    }

    public static int authUserId() {
        return get().getUserTo().id();
    }

    /*public static int authUserId() {
        return id;
    }

    public static void setAuthUserId(int id) {
        SecurityUtil.id = id;
    }
*/
}