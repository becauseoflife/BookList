package com.casper.testdrivendevelopment.data;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.casper.testdrivendevelopment.data.model.Shop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShopLoader {
    public ArrayList<Shop> getShops() {
        return shops;
    }

    private ArrayList<Shop> shops = new ArrayList<>();

    public String download(String urlStr) {
        HttpURLConnection connection = null;
        try {
            // 调用URL对象的openConnection方法获取HttpURLConnection的实例
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            // 设置请求方式，GET或POST
            connection.setRequestMethod("GET");
            // 设置连接超时、读取超时的时间，单位为毫秒（ms）
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            // 设置是否使用缓存  默认是true
            connection.setUseCaches(true);
            //设置请求头里面的属性
            //connection.setRequestProperty();
            // 开始连接
            Log.i("HttpURLConnection.GET", "开始连接");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                Log.i("HttpURLConnection.GET", "请求成功");

                InputStream in = connection.getInputStream();
                // 使用BufferedReader对象读取返回的数据流
                // 按行读取，存储在StringBuider对象response中
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                Log.v("out-->", response.toString());

                return response.toString();
            } else {
                Log.i("HttpURLConnection.GET", "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                // 结束后，关闭连接
                connection.disconnect();
            }
        }

        return  "";
    }

    public void parseJson(String content)
    {
        // 解析获得的string数据
        try {
            JSONObject jsonObject = new JSONObject(content);
            JSONArray jsonArrayData = jsonObject.getJSONArray("shops");
            int length = jsonArrayData.length();
            for (int i = 0; i < length; i++)
            {
                JSONObject shopJson = jsonArrayData.getJSONObject(i);
                Shop shop = new Shop();
                shop.setName(shopJson.getString("name"));
                shop.setMemo(shopJson.getString("memo"));
                shop.setLatitude(shopJson.getDouble("latitude"));
                shop.setLongitude(shopJson.getDouble("longitude"));
                shops.add(shop);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void load(final Handler handler, final String url)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String content = download(url);
                parseJson(content);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

}
