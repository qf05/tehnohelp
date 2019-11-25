package ru.tehnohelp.service;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;

public class VkMessage {

    private static final int ID = 565153037; //GARSAY
    private static final String TOKEN = MessageUtils.getProp().getProperty("token_vk");

    private static final UserActor actor = new UserActor(ID, TOKEN);
    private static final TransportClient transportClient = HttpTransportClient.getInstance();
    private static final VkApiClient vk = new VkApiClient(transportClient);


    private VkMessage() {
    }

    public static boolean sendMessage(String message) {
        message = "Новая заявка с сайта: \r\n" + message;
        try {
            vk.messages().send(actor)
//                    .userId(104618701)
                    .userId(1350733)
                    .message(message)
                    .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
