package com.codementor.utils.file;

import java.util.Base64;

public class Base64Utils {

    // 문자열을 Base64로 인코딩
    public static String encodeToString(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    // Base64로 인코딩된 문자열을 디코딩
    public static String decodeFromString(String encodedInput) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedInput);
        return new String(decodedBytes);
    }

    // 바이트 배열을 Base64로 인코딩
    public static String encodeToString(byte[] inputBytes) {
        return Base64.getEncoder().encodeToString(inputBytes);
    }

    // Base64로 인코딩된 바이트 배열을 디코딩
    public static byte[] decodeFromBytes(String encodedInput) {
        return Base64.getDecoder().decode(encodedInput);
    }
}
