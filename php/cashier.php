<?php

class PaymentProcessorTest {
    private static $HOST = "https://test-payment.glocashier.com";

    public static function main() {
        self::pproPay();
    }

    private static function getBillNo() {
        $nowTime = date('YmdHis');
        $randomNum = mt_rand(1000, 9999);
        return $nowTime . $randomNum . "_NEW";
    }

    private static function pproPay() {
        $url = self::$HOST . "/payment/page/v5/prepay";

        $bodyData = [
            "integration" => "CHECK_OUT",
            "merchantNo" => "1660",
            "merOrderNo" => self::getBillNo(),
            "payCurrency" => "USD",
            "payAmount" => "2.09",
            "returnUrl" => self::$HOST . "/payment/test/get/return_url",
            "cancelUrl" => self::$HOST . "/payment/page/v5/prepay",
            "notifyUrl" => self::$HOST . "/payment/test/notify",
            "firstName" => "MASON",
            "ip" => "192.168.0.1",
            "lastName" => "tang",
            "goods" => [
                [
                    "name" => "1 name attention please, this is test order 简中",
                    "price" => "2.09",
                    "num" => 1,
                    "description" => "2 des attention please, this is test order",
                    "img" => "https://payment.glocashier.com/payment/img/productDefaultImg.png"
                ]
            ],
            "street" => "Room 309 , lei muk shue estate , kwai fong , hk",
            "cityOrTown" => "HONGKONG",
            "countryOrRegion" => "USA",
            "stateOrProvince" => "CA",
            "postCodeOrZip" => "00852",
            "email" => "young.yau@7777.com",
            "telephone" => "85265827114",
            "sourceUrl" => "test-payment.1loprocessor.com",
            "remark" => "MASON test order",
            "tranCode" => "TA002"
        ];

        // replace this with correct key.
        $key = "123456";
        $sign = self::generateSha512Hash(
            $bodyData["merchantNo"] .
            $bodyData["merOrderNo"] .
            $bodyData["payCurrency"] .
            $bodyData["payAmount"] .
            $bodyData["returnUrl"] .
            $key
        );
        $bodyData["sign"] = $sign;

        $jsonInputString = json_encode($bodyData);

        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, [
            'Content-Type: application/json'
        ]);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $jsonInputString);

        $response = curl_exec($ch);
        $responseCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);

        curl_close($ch);

        echo "Response Code: " . $responseCode . "\n";
        echo json_encode(json_decode($response), JSON_PRETTY_PRINT) . "\n";
    }

    private static function generateSha512Hash($input) {
        return hash('sha512', $input);
    }
}

PaymentProcessorTest::main();

?>
