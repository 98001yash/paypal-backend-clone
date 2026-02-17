package com.paypalclone.auth_service.kafka;

public final class KafkaTopics {

    private KafkaTopics() {}

    public static final String USER_REGISTERED = "auth.user.registered";
    public static final String USER_LOGGED_IN  = "auth.user.logged-in";
}
