package bazi.star.service.impl;

import bazi.star.dto.BrushTicketDto;
import bazi.star.dto.ClientInfoFirst;
import bazi.star.dto.Paramet;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author chenx 2018-12-03 16:40
 */
public class Dictionary {

    private static final Logger logger = LoggerFactory.getLogger(Dictionary.class);

    private static final String V_URL = "https://937707mltg.sjdzp.cn/Miniwx/Index/getOrdersVerify.json";
    private static final String CREATE_ORDER_URL = "https://937707mltg.sjdzp.cn/Miniwx/Index/orders.json";
    private static final String PAY_URL = "https://937707mltg.sjdzp.cn/Miniwx/Index/swiftPay.json?orders_id=";

    public String getEwmUrl(BrushTicketDto brushTicketDto, NameValuePair[] parameter) {
        //设置代理IP
        HttpHost proxy = new HttpHost(brushTicketDto.getHostName(), brushTicketDto.getPort(), "http");
        CloseableHttpClient httpclient = HttpClients
                .custom()
                .setDefaultCookieStore(new BasicCookieStore())
                .setProxy(proxy)
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
            if (StringUtils.equalsIgnoreCase("false", verifyJson.getString("success"))) {
                //判断获取验证码结果
                String errorMessage = verifyJson.getString("message");
                System.out.println(errorMessage);
                return null;
            }
            String verify = JSONObject.parseObject(result).getString("verify");
            String[] cookie1 = http1.getHeaders("Set-Cookie")[4].getValue().split(";")[0].split("=");
            String[] sessid = http1.getHeaders("Set-Cookie")[0].getValue().split(";")[0].split("=");
            logger.info("------cookie------" + Arrays.toString(cookie1));
            logger.info("------sessid------" + Arrays.toString(sessid));
            logger.info("------验证码-------：" + verify);
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
                    .addParameter("orders_verify", verify)
                    .addParameters(parameter)
                    .setConfig(RequestConfig.custom().setRedirectsEnabled(false).setConnectTimeout(5000).build())
                    .build());
            String orderResult = EntityUtils.toString(http2.getEntity());
            JSONObject orderJson = JSONObject.parseObject(orderResult);
            if (StringUtils.equalsIgnoreCase("false", orderJson.getString("success"))) {
                String errorMessage = orderJson.getString("message");
                logger.info(errorMessage);
                return errorMessage;
            }
            String auth_orders_id = JSONObject.parseObject(orderResult).getJSONObject("data").getString("auth_orders_id");
            logger.info("------------------auth_orders_id-------------" + auth_orders_id);
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
                return null;
            }
            String ewmUrl = JSONObject.parseObject(payResult).getJSONObject("data").getJSONObject("params").getString("wxpay_img_url");
            logger.info("-------------------二维码地址-------------" + ewmUrl);
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //封装参数
    public List<Paramet> getParamet(List<ClientInfoFirst> list) {
        List<Paramet> parametList = Lists.newArrayList();

        Paramet paramet = new Paramet();
        paramet.setClientInfoMoreList(null);
        paramet.setGoodsId("");
        paramet.setIdNumber("");
        paramet.setName("");
        paramet.setPlayDate("");
        paramet.setMobile("");
        paramet.setAmount(5);
        parametList.add(paramet);


        return parametList;
    }


    public static void main(String[] args) throws InterruptedException {
//        NameValuePair nameValuePair = new BasicNameValuePair("goods_id", "1731638");
//        NameValuePair nameValuePair = new BasicNameValuePair("play_date", "2019-02-19");
//        NameValuePair nameValuePair = new BasicNameValuePair("amount", "1");
//        NameValuePair nameValuePair = new BasicNameValuePair("name", "方华");
//        NameValuePair nameValuePair = new BasicNameValuePair("mobile", "15517797745");
//        NameValuePair nameValuePair = new BasicNameValuePair("id_number", "431202198811101720");
//        NameValuePair nameValuePair = new BasicNameValuePair("orders_verify", "1");
        Paramet paramet = new Paramet();
        List<NameValuePair> list = Lists.newArrayList();
        list.add(new BasicNameValuePair("goods_id", "1731638"));
        list.add(new BasicNameValuePair("play_date", new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
        list.add(new BasicNameValuePair("amount", paramet.getAmount() + ""));
        list.add(new BasicNameValuePair("goods_id", paramet.getGoodsId()));
        List<ClientInfoFirst> list1 = paramet.getClientInfoMoreList();
        list1.forEach(clientInfoFirst -> {
            NameValuePair nameValuePair = new BasicNameValuePair("id_number_list[]", clientInfoFirst.getIdNumber());
            NameValuePair nameValuePair1 = new BasicNameValuePair("player_name_list[]", clientInfoFirst.getName());
            NameValuePair nameValuePair2 = new BasicNameValuePair("player_mobile_list[]", "");
            list.add(nameValuePair);
            list.add(nameValuePair1);
            list.add(nameValuePair2);
        });
        NameValuePair[] nvps = list.toArray(new NameValuePair[list.size()]);

        BrushTicketDto brushTicketDto = new BrushTicketDto("", 1);
    }
}
