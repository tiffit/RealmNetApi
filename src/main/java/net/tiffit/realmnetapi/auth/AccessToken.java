package net.tiffit.realmnetapi.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@RequiredArgsConstructor
@ToString
@Getter
public class AccessToken {

    private final String token;
    private final String email;
    private final long expire;
    private final long accountId;
    private final String ign;
    private final String clientHash;
    @Setter
    private HashMap<Integer, Integer> maxClassLevel;
}
