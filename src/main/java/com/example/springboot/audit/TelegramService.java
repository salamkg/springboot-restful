package com.example.springboot.audit;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TelegramService {
    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.chat.id}")
    private String chatId;

    private OkHttpClient client = new OkHttpClient();

    public void send(String message) throws IOException {
        String url = "https://api.telegram.org/bot" + token + "/sendMessage " + "?chat_id=" + chatId + "&text=" + message;
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"), message))
                .build();
        client.newCall(request).execute();
    }
}

