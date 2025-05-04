package vti.common.utils;

import java.util.List;

public class FilterRole {

    private FilterRole() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static List<String> getRootRole(List<String> roles) {
        return roles.stream().filter(role -> !role.contains(":") && !role.contains("default-roles")).toList();
    }
}
