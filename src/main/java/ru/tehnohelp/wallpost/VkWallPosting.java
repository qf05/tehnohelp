package ru.tehnohelp.wallpost;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiCaptchaException;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import net.marketer.RuCaptcha;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

public class VkWallPosting {

    private static final int ID = 565153037;
    private static final String TOKEN = LoadPosts.loadToken();

    private static final Object object = new Object();
    private static final TransportClient transportClient = HttpTransportClient.getInstance();
    private static final VkApiClient vk = new VkApiClient(transportClient);
    private static UserActor actor = null;

    protected static int errorInLoad = 0;
    private static int error = 0;
    private static int loop = 0;

    protected static String sendPost(Command key) {
        errorInLoad = 0;
        error = 0;
        if (actor == null) {
            actor = new UserActor(ID, TOKEN);
        }
        Post post = LoadPosts.loadPost(key);
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

    private static synchronized void synchronizePost(Integer groupId, Post post) {
        synchronized (object) {
            loop = 0;
            post(groupId, post);
            System.out.println(post.getMessage());
        }
    }

    private static void post(Integer groupId, Post post) {
        if (loop > 6) {
            return;
        }
        try {
            vk.wall().post(actor).ownerId(groupId).message(post.getMessage()).attachments(post.getAttachments()).execute();
            Thread.sleep(400);
        } catch (ApiCaptchaException e) {
            error += captcha(e.getSid(), e.getImage(), post, groupId);
        } catch (ApiException | ClientException | InterruptedException e) {
            error++;
            e.printStackTrace();
        }
    }

    private static Integer captcha(String captchaSid, String captchaImg, Post post, Integer groupId) {
        if (loop > 6) {
            return 0;
        }
        String captchaKey = captchaDecryption(captchaImg);
        if (captchaKey.contains("error")) {
            error += 1000;
            post(groupId, post);
            return 0;
        }
        try {
            vk.wall().post(actor)
                    .ownerId(groupId)
                    .message(post.getMessage())
                    .attachments(post.getAttachments())
                    .captchaSid(captchaSid)
                    .captchaKey(captchaKey)
                    .execute();
            Thread.sleep(400);
        } catch (ApiCaptchaException e) {
            error += captcha(e.getSid(), e.getImage(), post, groupId);
            loop++;
        } catch (ApiException | ClientException | InterruptedException e) {
            e.printStackTrace();
            error++;
        }
        return error;
    }

    private static String captchaDecryption(String captchaImg) {
        RuCaptcha.API_KEY = "132f6d37ff945d8b9719f9808a83ee6b";
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

                while (true) {
                    response = RuCaptcha.getDecryption(captchaId);
                    if (response.equals(RuCaptcha.Responses.CAPCHA_NOT_READY.toString())) {
                        Thread.sleep(5000);
                        continue;
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
}
