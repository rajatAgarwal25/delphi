package com.proptiger.delphi.dto;

public interface ResponseCodes {

    public static String SUCCESS                        = "2XX";
    // Request errors
    public static String UNAUTHORIZED                   = "401";
    public static String FORBIDDEN                      = "403";
    public static String RESOURCE_NOT_FOUND             = "404";
    public static String BAD_REQUEST                    = "4XX";
    public static String NAME_ALREADY_EXISTS            = "499";
    public static String SEARCH_QUERY_ALREADY_EXISTS    = "498";
    public static String BAD_CREDENTIAL                 = "497";
    public static String SESSION_EXPIRED                = "496";
    public static String CAPTCHA_REQUIRED               = "495";
    public static String OTP_REQUIRED                   = "494";
    public static String ACCESS_DENIED                  = "493";
    public static String ACCESS_EXPIRED                 = "492";
    public static String EMPTY_REPORT_GENERATED         = "491";
    public static String CANT_COMPLETE_ACTION           = "490";
    public static String DAILY_DOWNLOAD_LIMIT_EXPIRED   = "489";
    public static String MONTHLY_DOWNLOAD_LIMIT_EXPIRED = "488";
    public static String INVALID_EMAIL                  = "487";
    public static String INVALID_CONTACT_NUMBER         = "486";
    public static String INVALID_COUNTRY                = "485";
    public static String INVALID_USERNAME               = "484";
    public static String INVALID_PASSWORD               = "483";

    public static String COUPONS_SOLD_OUT               = "499";
    public static String MAX_COUPON_BUY_LIMIT           = "498";
    public static String GATEWAY_ERROR                  = "497";

    public static String REQUEST_PARAM_INVALID          = BAD_REQUEST;
    public static String AUTHENTICATION_ERROR           = BAD_REQUEST;

    // server error
    public static String INTERNAL_SERVER_ERROR          = "5XX";
    public static String DATABASE_CONNECTION_ERROR      = INTERNAL_SERVER_ERROR;

    public static String ERROR                          = "ERROR";

}
