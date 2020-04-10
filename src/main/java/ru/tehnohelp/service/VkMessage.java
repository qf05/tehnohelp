package ru.tehnohelp.service;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;

import static ru.tehnohelp.service.MessageUtils.PROPERTIES;

public class VkMessage {

    private static final int GID = 194043958;
    private static final String GTOKEN = PROPERTIES.getProperty("token_vk");

    private static final TransportClient transportClient = HttpTransportClient.getInstance();
    private static final VkApiClient vk = new VkApiClient(transportClient);
    private static GroupActor gActor = null;

    private VkMessage() {
    }

    public static boolean sendMessage(String message) {
        if (gActor == null) {
            gActor = new GroupActor(GID, GTOKEN);
        }
        message = "Новая заявка с сайта: \r\n" + message;
        try {
            vk.messages().send(gActor)
                    .randomId((int) (Math.random() * 10000000) + 10)
//                    .userId(104618701)
                    .userId(1350733)
//                    .userId(-184345402)
                    .message(message)
                    .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
