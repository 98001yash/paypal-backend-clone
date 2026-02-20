package com.paypalclone.account;

import com.paypalclone.base.BaseAccountEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountClosedEvent extends BaseAccountEvent {
}

