package bazi.star.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * @author chenx 2019-02-18 11:54
 */
public class Test {
    private static final String FIRST_URL = "https://937707mltg.sjdzp.cn/Miniwx/Index/buy.html?goods_id=1843845&form=1&cc=1";
    private static final String V_URL = "https://937707mltg.sjdzp.cn/Miniwx/Index/getOrdersVerify.json";
    private static final String CREATE_ORDER_URL = "https://937707mltg.sjdzp.cn/Miniwx/Index/orders.json";
    private static final String PAY_URL = "https://937707mltg.sjdzp.cn/Miniwx/Index/swiftPay.json?orders_id=";


    public static void main(String[] args) throws InterruptedException {
        HttpHost proxy = new HttpHost("117.91.252.107", 9999, "http");
        CloseableHttpClient httpclient = HttpClients
                .custom()
                .setDefaultCookieStore(new BasicCookieStore())
//                    .setProxy(proxy)
                .build();
        try {
            CloseableHttpResponse http1 = httpclient.execute(RequestBuilder.post()
                    .setUri(new URI(V_URL))
                    .setHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                    .setHeader("Accept-Encoding", "gzip, deflate, br")
                    .setHeader("Accept-Language", "zh-CN,zh;q=0.9")
                    .setHeader("Connection", "keep-alive")
                    .setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .setHeader("Host", "937707mltg.sjdzp.cn")
                    .setHeader("Origin", "https://937707mltg.sjdzp.cn")
                    .setHeader("Referer", "https://937707mltg.sjdzp.cn/Miniwx/Index/buy.html?goods_id=1843845&form=1&cc=1")
                    .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                    .setHeader("X-Requested-With", "XMLHttpRequest")
                    .addParameter("verify_type", "1")
                    .build());
            String result = EntityUtils.toString(http1.getEntity());
            JSONObject verifyJson = JSONObject.parseObject(result);
            if (StringUtils.equalsIgnoreCase("false",verifyJson.getString("success"))){
                //判断获取验证码结果
                String errorMessage = verifyJson.getString("message");
                System.out.println(errorMessage);
                return;
            }
            String verify = JSONObject.parseObject(result).getString("verify");
            String[] cookie1 = http1.getHeaders("Set-Cookie")[4].getValue().split(";")[0].split("=");
            String[] sessid = http1.getHeaders("Set-Cookie")[0].getValue().split(";")[0].split("=");
            System.out.println("------cookie------" + Arrays.toString(cookie1));
            System.out.println("------sessid------" + Arrays.toString(sessid));
            System.out.println("------验证码-------：" + verify);
            http1.close();
            Thread.sleep(5000);
            CloseableHttpResponse http2 = httpclient.execute(RequestBuilder.post()
                    .setUri(new URI(CREATE_ORDER_URL))
                    .setHeader("Host", "937707mltg.sjdzp.cn")
                    .setHeader("Connection", "keep-alive")
                    .setHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                    .setHeader("Origin", "https://937707mltg.sjdzp.cn")
                    .setHeader("X-Requested-With", "XMLHttpRequest")
                    .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                    .setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .setHeader("Referer", "https://937707mltg.sjdzp.cn/Miniwx/Index/buy.html?goods_id=1843845&form=1&cc=1")
                    .setHeader("Accept-Encoding", "gzip, deflate, br")
                    .setHeader("Accept-Language", "zh-CN,zh;q=0.9")
                    .setHeader("Cookie", http1.getHeaders("Set-Cookie")[4].getValue() + "; " + http1.getHeaders("Set-Cookie")[0].getValue())
                    .addParameter("goods_id", "1731068")
                    .addParameter("play_date", "2019-02-20")
                    .addParameter("amount", "1")
                    .addParameter("name", "平明奇")
                    .addParameter("mobile", "15517797744")
                    .addParameter("id_number", "820000195008115837")
                    .addParameter("time_slot_damoylxs[]", "100000133720|100000000013|17:30:00-17:59:59|2")
                    .addParameter("orders_verify", verify)
                    .setConfig(RequestConfig.custom().setRedirectsEnabled(false).setConnectTimeout(5000).build())
                    .build());
            String orderResult = EntityUtils.toString(http2.getEntity());
            JSONObject orderJson = JSONObject.parseObject(orderResult);
            if (StringUtils.equalsIgnoreCase("false", orderJson.getString("success"))) {
                //判断下订单结果
                System.out.println(orderJson.getString("message"));
                return;
            }
            String auth_orders_id = JSONObject.parseObject(orderResult).getJSONObject("data").getString("auth_orders_id");
            System.out.println("------------------auth_orders_id-------------" + auth_orders_id);
            http2.close();
            String payUrl = PAY_URL + auth_orders_id;
            CloseableHttpResponse http3 = httpclient.execute(RequestBuilder.get(payUrl)
                    .setHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                    .setHeader("Accept-Encoding", "gzip, deflate, br")
                    .setHeader("Accept-Language", "zh-CN,zh;q=0.9")
                    .setHeader("Connection", "keep-alive")
                    .setHeader("Cookie", http1.getHeaders("Set-Cookie")[4].getValue() + "; " + http1.getHeaders("Set-Cookie")[0].getValue())
                    .setHeader("Host", "937707mltg.sjdzp.cn")
                    .setHeader("Referer", "https://937707mltg.sjdzp.cn/Miniwx/Index/orderInfo.html?orders_id=3f45NCawA6bwJUIHHnuzow")
                    .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                    .setHeader("X-Requested-With", "XMLHttpRequest")
                    .build());
            String payResult = EntityUtils.toString(http3.getEntity());
            JSONObject payJson = JSONObject.parseObject(payResult);
            if (StringUtils.equalsIgnoreCase("false", payJson.getString("success")) || StringUtils.equalsIgnoreCase("false", payJson.getString("wait"))) {
                //判断获取支付二维码结果
                String errorMessage = payJson.getString("message");
                System.out.println(errorMessage);
                return;
            }
            String ewmUrl = JSONObject.parseObject(payResult).getJSONObject("data").getJSONObject("params").getString("wxpay_img_url");
            System.out.println("-------------------二维码地址-------------" + ewmUrl);
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

