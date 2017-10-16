package com.stouduo.dcw.util;

import com.stouduo.dcw.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {


    public static String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal instanceof User ? ((User) principal).getUsername() : ((String) principal);
    }

}
