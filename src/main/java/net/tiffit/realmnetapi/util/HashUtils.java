package net.tiffit.realmnetapi.util;

import com.google.common.hash.Hashing;
import kotlin.text.Charsets;

public class HashUtils {

    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    public static String hash(String input){
        return Hashing.sha1().hashString(input, Charsets.UTF_8).toString();
    }

}
