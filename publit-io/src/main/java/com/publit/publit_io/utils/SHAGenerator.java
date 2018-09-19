package com.publit.publit_io.utils;

import com.publit.publit_io.constant.Constant;
import com.publit.publit_io.exception.PublitioExceptions;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

public class SHAGenerator {

    //API timestamp is the current UNIX timestamp (32 bits signed integer).
    private String apiTimeStamp;

    //API nonce is an 8 digits random number.
    private String apiNonce;

    //SHA-1 digest of the api_secret, api_timestamp and api_nonce.
    private String apiSignature;

    //To check valid api-key and api-secret.
    private boolean isValidated = false;

    //Public Constructor
    public SHAGenerator() {
        try {
            isValidated = validateAPI(APIConfiguration.apiKey, APIConfiguration.apiSecret);
        } catch (PublitioExceptions publitioExceptions) {
            publitioExceptions.printStackTrace();
        }
        generateSHA();
    }

    /**
     * Generate SHA-1 digest of the apiTimeStamp, apiNonce and apiSecret.
     */
    private void generateSHA() {

        //Generate current api time stamp.
        apiTimeStamp = String.valueOf(new Date().getTime() / 1000);

        //Generate 8 digit random number.
        Random rnd = new Random();
        apiNonce = String.valueOf(10000000L + ((long) rnd.nextInt(900000) * 100) + rnd.nextInt(100));

        if (isValidated) {
            String concatinatedString = apiTimeStamp.concat(apiNonce).concat(APIConfiguration.apiSecret);
            apiSignature = hashString(concatinatedString, Constant.ALGORITHM);
        }
    }

    /**
     * @param concatinatedString It is used to generate SHA-1 digest.
     * @param algorithm          The name of the algorithm requested.
     * @return Hex String which is used as api signature.
     */
    private String hashString(String concatinatedString, String algorithm) {

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(concatinatedString.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {

            return "";
        }
    }

    /**
     * @param hashedBytes Byte Array from which the api signature will generated.
     * @return api signature.
     */
    private String convertByteArrayToHexString(byte[] hashedBytes) {

        StringBuilder stringBuffer = new StringBuilder();
        for (byte hashedByte : hashedBytes) {
            stringBuffer.append(Integer.toString((hashedByte & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    /**
     * To check the null or empty value for api key and api secret.
     *
     * @param apiKey    API key identifies the user to the API.
     * @param apiSecret API secret is used to generate SHA-1 digest.
     * @return Boolean(True / False) value which indicates validations.
     */
    private boolean validateAPI(String apiKey, String apiSecret) throws PublitioExceptions {

        if (apiKey == null || apiKey.isEmpty()) {
            throw new PublitioExceptions("Please add publitio_api_key to your AndroidManifest.");
        }

        if (apiSecret == null || apiSecret.isEmpty()) {
            throw new PublitioExceptions("Please add publitio_api_secret to your AndroidManifest.");
        }

        return true;
    }

    /**
     * Gives the current time stamp.
     *
     * @return The current time stamp.
     */
    public String getApiTimeStamp() {
        return apiTimeStamp;
    }

    /**
     * Gives the 8 digit api nonce.
     *
     * @return The 8 digit api nonce.
     */
    public String getApiNonce() {
        return apiNonce;
    }

    /**
     * Gives the api signature.
     *
     * @return The api signature.
     */
    public String getApiSignature() {
        return apiSignature;
    }
}
