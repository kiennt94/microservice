package vti.common.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import vti.common.dto.AccountDto;

public class SecurityUtil {

    // Ngăn việc khởi tạo class
    private SecurityUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static AccountDto getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AccountDto accountDto) {
            return accountDto;
        }
        throw new IllegalStateException("Current user is not authenticated");
    }

    public static String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    public static int getCurrentAccountId() {
        return getCurrentUser().getAccountId();
    }

    public static String getCurrentFullName() {
        return getCurrentUser().getFullName();
    }
}
