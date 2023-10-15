package com.ecoharvest.userservice.constants;

public class ApplicationConstants {
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String AUTH_HEADER = "authorization";
    public static final String AUTH_TOKEN_TYPE = "Bearer";
    public static final String AUTH_TOKEN_PREFIX = AUTH_TOKEN_TYPE + " ";
    public static final String ROLES = "roles";
    public static final String USER_ID = "userId";
    public static final String USERNAME_NOT_FOUND_ERR_MSG = "Username not found.";
    public static final String ID_NOT_FOUND_ERR_MSG = "Id not found.";
    public static final String REGISTER_ERR_MSG = "Invalid register details.";
    public static final String REGISTER_USERNAME_ERR_MSG = "Username already in use.";
    public static final String LOGIN_ERR_MSG = "Invalid login details.";
    public static final String RETRIEVE_USER_LIST_ERR_MSG = "Unable to retrieve user list.";
    public static final String ERR_MSG = "An error has occurred.";
    public static final String AUTHENTICATION_API_PATH = "/api/authentication";
    public static final String USER_API_PATH = "/api/user";
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String RETRIEVE_USER_LIST = "retrieve/list";
    public static final String RETRIEVE_USER = "retrieve";
    public static final String UPDATE_USER_DETAILS = "update/details";
    public static final String CHANGE_PASSWORD = "change/password";
    public static final String DELETE_USER = "delete";
    public static final String ERROR_CODE = "E";
    public static final String SUCCESS_CODE = "Y";
}
