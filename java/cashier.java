package com.zmt.payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
/**
 * @ClassName: PaymentProcessorTest
 * @Author Mason.MA
 * @Package com.zmt.payment
 * @Date 6/18/24 16:30
 * @Version 1.0
 */
public class PaymentProcessorTest {
    private static final String HOST = "https://test-payment.glocashier.com";

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        pproPay();
    }

    private static String getBillNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String nowTime = LocalDateTime.now().format(formatter);

        Random random = new Random();
        int randomNum = 1000 + random.nextInt(9000);
        return nowTime + randomNum + "_NEW";
    }

    private static void pproPay() throws IOException, NoSuchAlgorithmException {
        String url = HOST + "/payment/page/v5/prepay";

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode bodyData = objectMapper.createObjectNode();

        bodyData.put("integration", "CHECK_OUT");
        bodyData.put("merchantNo", "1660");
        bodyData.put("merOrderNo", getBillNo());
        bodyData.put("payCurrency", "USD");
        bodyData.put("payAmount", "2.09");
        bodyData.put("returnUrl", HOST + "/payment/test/get/return_url");
        bodyData.put("cancelUrl", HOST + "/payment/page/v5/prepay");
        bodyData.put("notifyUrl", HOST + "/payment/test/notify");
        bodyData.put("firstName", "MASON");
        bodyData.put("ip", "192.168.0.1");
        bodyData.put("lastName", "tang");

        ObjectNode goodsNode = objectMapper.createObjectNode();
        goodsNode.put("name", "1 name attention please, this is test order 简中");
        goodsNode.put("price", "2.09");
        goodsNode.put("num", 1);
        goodsNode.put("description", "2 des attention please, this is test order");
        goodsNode.put("img", "https://payment.glocashier.com/payment/img/productDefaultImg.png");
        bodyData.set("goods", objectMapper.createArrayNode().add(goodsNode));

        bodyData.put("street", "Room 309 , lei muk shue estate , kwai fong , hk");
        bodyData.put("cityOrTown", "HONGKONG");
        bodyData.put("countryOrRegion", "USA");
        bodyData.put("stateOrProvince", "CA");
        bodyData.put("postCodeOrZip", "00852");
        bodyData.put("email", "young.yau@7777.com");
        bodyData.put("telephone", "85265827114");
        bodyData.put("sourceUrl", "test-payment.1loprocessor.com");
        bodyData.put("remark", "MASON test order");
        bodyData.put("tranCode", "TA002");
        // replace this with correct key.
        String key = "123456";
        String sign = generateSha512Hash(bodyData.get("merchantNo").asText() +
                bodyData.get("merOrderNo").asText() +
                bodyData.get("payCurrency").asText() +
                bodyData.get("payAmount").asText() +
                bodyData.get("returnUrl").asText() +
                key);
        bodyData.put("sign", sign);

        String jsonInputString = objectMapper.writeValueAsString(bodyData);

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(jsonInputString);
            wr.flush();
        }

        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readTree(response.toString())));
    }

    private static String generateSha512Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
