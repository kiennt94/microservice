package vti.common.utils;


public class ConstantUtils {

    private ConstantUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    public static final String BEARER = "Bearer ";
    public static final int MAX_PAGE_SIZE = 100;

    public static final String CUSTOM_EXCEPTION = "Custom exception: {}";
    public static final String CREATE_SUCCESSFULLY = "Create successfully";
    public static final String UPDATE_SUCCESSFULLY = "Update successfully";
    public static final String DELETE_SUCCESSFULLY = "Delete successfully";

    public static final String ACCOUNT_ID_NOT_EXISTS = "account.id.not.exists";
    public static final String ACCOUNT_USERNAME_NOT_EXISTS = "account.username.not.exists";
    public static final String ACCOUNT_EMAIL_EXISTS = "account.email.exists";
    public static final String ACCOUNT_USERNAME_EXISTS = "account.username.exists";

    public static final String DEPARTMENT_ID_NOT_EXISTS = "department.id.not.exists";
    public static final String DEPARTMENT_ID_EXISTS_IN_ACCOUNT = "department.id.exists.reference.key.account";
    public static final String DEPARTMENT_NAME_EXISTS = "department.name.exists";

    public static final String POSITION_ID_NOT_EXISTS = "position.id.not.exists";

    public static final String ROLES_EMPTY = "roles.empty";
    public static final String ROLE_NOT_EXISTS = "role.not.exists";
}
