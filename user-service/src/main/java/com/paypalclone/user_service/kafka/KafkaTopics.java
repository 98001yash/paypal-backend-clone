package com.paypalclone.user_service.kafka;

public final class KafkaTopics {

    private KafkaTopics() {}

    // USER LIFECYCLE
    public static final String USER_CREATED = "user.user.created";

    //COMPLIANCE
    public static final String USER_KYC_UPDATED = "user.user.kyc-updated";
    public static final String USER_RISK_UPDATED = "user.user.risk-updated";
}
