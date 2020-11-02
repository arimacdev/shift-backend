package com.arimac.backend.pmtool.projectmanagementtool.utils;

public class ENVConfig {

    public static final String BASE_URL = System.getenv("BASE_URL");
    public static final String KEYCLOAK_HOST = System.getenv("KEYCLOAK_HOST");
    public static final String KEYCLOAK_REALM = System.getenv("KEYCLOAK_REALM");
    public static final String KEYCLOAK_ROLE_CLIENT_ID = System.getenv("KEYCLOAK_ROLE_CLIENT_ID");
    public static final String KEYCLOAK_ROLE_CLIENT_NAME = System.getenv("KEYCLOAK_ROLE_CLIENT_NAME");
    public static final String KEYCLOAK_ROLE_CLIENT_SECRET = System.getenv("KEYCLOAK_ROLE_CLIENT_SECRET");
    public static final String DB_URL = System.getenv("DB_URL");
    public static final String DB_USERNAME = System.getenv("DB_USERNAME");
    public static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    public static final String AWS_BUCKET_NAME = System.getenv("AWS_BUCKET_NAME");
    public static final String AWS_REGION = System.getenv("AWS_REGION");
    public static final String AWS_ACCESS_KEY = System.getenv("AWS_ACCESS_KEY");
    public static final String AWS_SECRET_KEY = System.getenv("AWS_SECRET_KEY");
    public static final String SLACK_BASE_URL = System.getenv("SLACK_BASE_URL");
    public static final String SLACK_BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN");

    public static final String ONE_SIGNAL_URL = System.getenv("ONE_SIGNAL_URL");
    public static final String ONE_SIGNAL_TOKEN = System.getenv("ONE_SIGNAL_TOKEN");
    public static final String ONE_SIGNAL_APP_ID = System.getenv("ONE_SIGNAL_APP_ID");

    public static final String SUPPORT_CLIENT_NAME = System.getenv("SUPPORT_CLIENT_NAME");
    public static final String SUPPORT_CLIENT_SECRET = System.getenv("SUPPORT_CLIENT_SECRET");
    public static final String SUPPORT_REALM = System.getenv("SUPPORT_REALM");

    public static final String ENABLE_SCHEDULER = System.getenv("ENABLE_SCHEDULER");
    public static final String MAX_FILE_SIZE = System.getenv("MAX_FILE_SIZE");



}
