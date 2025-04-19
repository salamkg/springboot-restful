package com.example.springboot.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramService {
    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.chat.id}")
    private String chatId;

    private OkHttpClient client = new OkHttpClient();
    private ObjectMapper mapper = new ObjectMapper();


    public void send(String message) throws IOException {
        String url = "https://api.telegram.org/bot" + token + "/sendMessage";

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("chat_id", chatId);
        jsonMap.put("text", message);

        String json = mapper.writeValueAsString(jsonMap);

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try(Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
        }
    }
}

