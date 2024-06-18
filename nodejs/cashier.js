const axios = require('axios');
const crypto = require('crypto');
const moment = require('moment');

const HOST = "https://test-payment.gloprocessor.com";

function getBillNo() {
    const nowTime = moment().format('YYYYMMDDHHmmss');
    const randomNum = Math.floor(1000 + Math.random() * 9000);
    return `${nowTime}${randomNum}_NEW`;
}

function generateSha512Hash(input) {
    return crypto.createHash('sha512').update(input, 'utf-8').digest('hex');
}

async function pproPay() {
    const url = `${HOST}/payment/page/v5/prepay`;

    const bodyData = {
        integration: "CHECK_OUT",
        merchantNo: "1660",
        merOrderNo: getBillNo(),
        payCurrency: "USD",
        payAmount: "2.09",
        returnUrl: `${HOST}/payment/test/get/return_url`,
        cancelUrl: `${HOST}/payment/page/v5/prepay`,
        notifyUrl: `${HOST}/payment/test/notify`,
        firstName: "MASON",
        ip: "192.168.0.1",
        lastName: "tang",
        goods: [
            {
                name: "1 name attention please, this is test order 简中",
                price: "2.09",
                num: 1,
                description: "2 des attention please, this is test order",
                img: "https://payment.gloprocessor.com/payment/img/productDefaultImg.png"
            }
        ],
        street: "Room 309 , lei muk shue estate , kwai fong , hk",
        cityOrTown: "HONGKONG",
        countryOrRegion: "USA",
        stateOrProvince: "CA",
        postCodeOrZip: "00852",
        email: "young.yau@7777.com",
        telephone: "85265827114",
        sourceUrl: "test-payment.1loprocessor.com",
        remark: "MASON test order",
        tranCode: "TA002"
    };

    const key = "123456";  // replace this with the correct key
    const sign = generateSha512Hash(
        bodyData.merchantNo +
        bodyData.merOrderNo +
        bodyData.payCurrency +
        bodyData.payAmount +
        bodyData.returnUrl +
        key
    );
    bodyData.sign = sign;

    try {
        const response = await axios.post(url, bodyData, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        console.log("Response Code:", response.status);
        console.log(JSON.stringify(response.data, null, 2));
    } catch (error) {
        console.error("Error:", error.response ? error.response.data : error.message);
    }
}

pproPay();
