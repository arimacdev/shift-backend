package com.arimac.backend.pmtool.projectmanagementtool.utils;

public class ENVConfig {

    public static final String KEYCLOAK_HOST = System.getenv("KEYCLOAK_HOST");
    public static final String KEYCLOAK_REALM = System.getenv("KEYCLOAK_REALM");
    public static final String KEYCLOAK_ROLE_CLIENT_ID = System.getenv("KEYCLOAK_ROLE_CLIENT_ID");
    public static final String KEYCLOAK_ROLE_CLIENT_NAME = System.getenv("KEYCLOAK_ROLE_CLIENT_NAME");
    public static final String KEYCLOAK_ROLE_CLIENT_SECRET = System.getenv("KEYCLOAK_ROLE_CLIENT_SECRET");
    public static final String DB_URL = System.getenv("DB_URL");
    public static final String DB_USERNAME = System.getenv("DB_USERNAME");
    public static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

}
