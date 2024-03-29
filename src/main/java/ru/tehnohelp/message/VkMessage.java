package ru.tehnohelp.message;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import ru.tehnohelp.message.util.MessageUtils;

public class VkMessage {

    private static final int GID = 184345402; //194043958;
    private static final String GTOKEN = MessageUtils.loadPassword(MessageUtils.GVK);
//    private static final String GTOKEN = "";

    private static final TransportClient transportClient = HttpTransportClient.getInstance();
    private static final VkApiClient vk = new VkApiClient(transportClient);
    private static GroupActor gActor = null;
    private static final Object object = new Object();

    private VkMessage() {
    }

    public static boolean sendMessage(String message) {
        if (gActor == null) {
            gActor = new GroupActor(GID, GTOKEN);
        }
        try {
            send(message);
        } catch (ApiException | ClientException |InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static synchronized void send(String message) throws ClientException, ApiException, InterruptedException {
            synchronized (object) {
                Thread.sleep(350);
                vk.messages().send(gActor)
                        .randomId((int) (Math.random() * 10000000) + 10)
//                    .userId(104618701)
                        .userId(1350733)
//                    .userId(-184345402)
                        .message(message)
                        .execute();
            }
    }
}
