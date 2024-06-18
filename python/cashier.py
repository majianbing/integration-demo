import json
import requests

host = "https://test-payment.gloprocessor.com"


def get_billNo():
    now_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S')
    random_str = random.sample(string.digits, k=4)
    billNo = now_time + ("".join(random_str)) + "_NEW"
    return billNo


def do_pay():
    url = host + "/payment/page/v5/prepay"
    body_data = {
        "integration": "CHECK_OUT",
        "merchantNo": "1660",
        "merOrderNo": str(get_billNo()),
        "payCurrency": 'USD',
        "payAmount": "2.09",
        "returnUrl": host + "/payment/test/get/return_url",
        "cancelUrl": host + "/payment/page/v5/prepay",
        "notifyUrl": host + "/payment/test/notify",
        "firstName": "MASON",
        "ip": "192.168.0.1",
        "lastName": "tang",
        "goods": [
            {
                "name": "1 name attention please, this is test order 简中",
                "price": "2.09",
                "num": 1,
                "description": "2 des attention please, this is test order",
                "img": "https://payment.gloprocessor.com/payment/img/productDefaultImg.png"
            }
        ],
        "street": "Room 309 , lei muk shue estate , kwai fong , hk",
        "cityOrTown": "HONGKONG",
        "countryOrRegion": "USA",
        "stateOrProvince": "CA",
        "postCodeOrZip": "00852",
        "email": "young.yau@7777.com",
        "telephone": "85265827114",
        "sourceUrl": "test-payment.1loprocessor.com",
        "remark": "MASON test order",
        "tranCode": "TA002"
    }
    # replace this with correct key.
    key = "123456"
    sign = hashlib.sha512((body_data["merchantNo"] + body_data["merOrderNo"] + body_data["payCurrency"] + body_data["payAmount"] + body_data["returnUrl"] + key).encode('utf-8')).hexdigest()
    body_data["sign"] = sign
    res = requests.post(url, data=json.dumps(body_data))
    print(res.status_code)
    print(json.dumps(res.json(), indent=2))


if __name__ == "__main__":
    do_pay()
