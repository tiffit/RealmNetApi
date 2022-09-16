package net.tiffit.realmnetapi.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Getter
public class AccessToken {

    private final String token;
    private final String email;
    private final long expire;
    private final long accountId;
    private final String ign;
}
