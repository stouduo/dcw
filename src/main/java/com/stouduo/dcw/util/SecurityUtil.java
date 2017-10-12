package com.stouduo.dcw.util;

        import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {


    public static String getUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
