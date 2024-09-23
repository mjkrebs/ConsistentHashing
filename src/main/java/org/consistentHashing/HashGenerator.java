package org.consistentHashing;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

public class HashGenerator {


    HashGenerator(){}

    public int createHash(String s) {
        return createHashMurmur(s);
    }

    private int createHashFNV(String s) {
        final int FNV_32_PRIME = 0x01000193;
        int hash = 0x811c9dc5; // FNV-1a 32-bit offset basis

        for (char c : s.toCharArray()) {
            hash ^= c;
            hash *= FNV_32_PRIME;
        }
        return hash & 0x7fffffff;
    }

    private int createHashMurmur(String s) {
        // Use MurmurHash3 for better distribution
        return Hashing.murmur3_32().hashString(s, StandardCharsets.UTF_8).asInt() & 0x7fffffff;
    }
}
