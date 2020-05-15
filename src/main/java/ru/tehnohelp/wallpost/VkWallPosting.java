package ru.tehnohelp.wallpost;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiCaptchaException;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.WallPostFull;
import net.marketer.RuCaptcha;
import org.apache.commons.io.FileUtils;
import ru.tehnohelp.message.MessageUtils;

import java.io.File;
import java.net.URL;

import static ru.tehnohelp.message.MessageUtils.CAPTCHA;
import static ru.tehnohelp.message.MessageUtils.VK;
import static ru.tehnohelp.wallpost.LoadPosts.loadPostFromGroup;

public class VkWallPosting {

    private static final int ID = 565153037;
    private static final String TOKEN = MessageUtils.loadPassword(VK);

    private static final Object object = new Object();
    private static final TransportClient transportClient = HttpTransportClient.getInstance();
    private static final VkApiClient vk = new VkApiClient(transportClient);
    private static UserActor actor = null;

    protected static int errorInLoad = 0;
    private static int error = 0;
    private static int loop = 0;

    static {
        RuCaptcha.API_KEY = MessageUtils.loadPassword(CAPTCHA);
    }

    protected static String sendPost(Command key) {
        errorInLoad = 0;
        error = 0;
        if (actor == null) {
            actor = new UserActor(ID, TOKEN);
        }
        Post post = loadPostFromGroup(key);
        if (post != null) {
            if (!post.getMessage().startsWith("not public")) {
                for (Integer groupId : LoadPosts.loadIds()) {
                    synchronizePost(groupId, post);
                }
            }
        } else {
            return "post error - " + key;
        }
        return error == 0 && errorInLoad == 0 ? "Команда выполнена" : "COMMAND error " + error + " error load " + errorInLoad;
    }

    static long test = 0;

    private static synchronized void synchronizePost(Integer groupId, Post post) {
        synchronized (object) {
            loop = 0;
            test++;
            post(groupId, post, null, null);
        }
    }

    private static void post(Integer groupId, Post post, String captchaSid, String captchaKey) {
        if (loop > 6) {
            return;
        }
        try {
            loop++;
            vk.wall().post(actor).ownerId(groupId)
                    .message(post.getMessage())
                    .attachments(post.getAttachments())
                    .captchaSid(captchaSid)
                    .captchaKey(captchaKey)
                    .execute();
            Thread.sleep(400);
        } catch (ApiCaptchaException e) {
            String decryption = captcha(e.getImage());
            post(groupId, post, e.getSid(), decryption);
        } catch (ApiException | ClientException | InterruptedException e) {
            error++;
            e.printStackTrace();
        }
    }

    private static String captcha(String captchaImg) {
        String captchaKey = captchaDecryption(captchaImg);
        if (captchaKey.contains("error")) {
            error += 1000;
            captchaKey = null;
        }
        return captchaKey;
    }

    private static String captchaDecryption(String captchaImg) {
        String decryption = "";
        String captchaId = "";
        try {
            URL url = new URL(captchaImg);
            String tDir = "/resources/";
            String path = tDir + "tmp" + ".jpg";
            File file = new File(path);
            file.deleteOnExit();
            FileUtils.copyURLToFile(url, file);

            String response = RuCaptcha.postCaptcha(file);
            if (response.startsWith("OK")) {
                captchaId = response.substring(3);
                int loop = 0;
                while (true) {
                    if (loop > 40) {
                        break;
                    }
                    response = RuCaptcha.getDecryption(captchaId);
                    if (response.equals(RuCaptcha.Responses.CAPCHA_NOT_READY.toString())) {
                        Thread.sleep(5000);
                        loop++;
                    } else {
                        if (response.startsWith("OK")) {
                            decryption = response.substring(3);
                            break;
                        } else {
                            decryption = "error";
                        }
                    }
                }
            } else {
                decryption = "error";
            }
        } catch (Exception e) {
            decryption = "error";
            e.printStackTrace();
        }
        return decryption;
    }

    protected static WallPostFull loadPostFromWall(String postId) {
        try {
            Thread.sleep(400);
            return vk.wall().getById(actor, postId).execute().get(0);
        } catch (ApiException | ClientException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
