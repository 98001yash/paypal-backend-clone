package com.paypalclone.payout_service.client;

import com.paypalclone.payout_service.entity.Payout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class BankTransferClient {

    public void sendBatch(
            String batchKey,
            List<Payout> payouts
    ) {
        log.info(
                "Sending batch {} to bank with {} payouts",
                batchKey,
                payouts.size()
        );

        // Real world examples:
        // - Generate NEFT / ACH file
        // - Call bank API
        // - Upload SFTP file
    }
}