package ru.tehnohelp.wallpost.addfriends;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.tehnohelp.message.util.MessageUtils;
import ru.tehnohelp.message.VkMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.tehnohelp.message.util.MessageUtils.TOKEN_GARSEY;
import static ru.tehnohelp.wallpost.VkWallPosting.*;
import static ru.tehnohelp.wallpost.addfriends.WasAdd.getGroupNameFile;
import static ru.tehnohelp.wallpost.addfriends.WasAdd.getUserNameFile;

public class AddFriends {

    public static int ID_IVANOV = 104618701; //IVANOV
    public static final String IVANOV_TOKEN = MessageUtils.loadPassword(MessageUtils.TOKEN_IVANOV);

    public static final Integer TEHNO_ID = 184345402;
    public static final Integer MTS_ID = 195269445;
    public static final Integer PHOBOS_ID = 135479896;
    private static int loop = 0;
    private static List<Integer> end = new ArrayList<>();
    private static int ok = 0;
    private static int notOk = 0;

    public static String addFriends() {
        UserActor actor = getActor();
        int groupId = getGroupId();
        initEnd();
        loop = 0;
        ok = 0;
        notOk = 0;
        List<Integer> friends = getFriends(actor, groupId);
        List<Integer> was = new ArrayList<>();
        for (int i = 0; i < (int) (Math.random() * 13) + 26; i++) {
//        for (int i = 0; i < 2; i++) {
            loop = 0;
            captchaError = false;
            add(actor, groupId, friends.get(i), null, null);
            if (loop < 5) {
                was.add(friends.get(i));
            }
        }
        WasAdd.saveWas(was, actor.getId(), groupId);
        return "ok add " + was.size() + " to " + getGroupNameFile(groupId) +
                " from " + getUserNameFile(actor.getId()) + " and ok = " + ok + " notOk = " + notOk;
    }

    private static boolean captchaError = false;
    private static void add(UserActor actor, Integer groupId, Integer friendId, String captchaSid, String captchaKey) {
//        loop++;
//        if (loop > 5) {
//            return;
//        }
//        try {
//            if (captchaSid == null) {
//                Thread.sleep(3176 + (int) (Math.random() * 49028));
//            } else {
//                Thread.sleep(4238 + (int) (Math.random() * 5294));
//            }
////            Integer value = vk.groups().invite(actor, groupId, friendId)
////                    .captchaSid(captchaSid)
////                    .captchaKey(captchaKey)
////                    .execute().getValue();
////            System.out.println("Friend id = " + friendId + " add with result = " + value);
//            if (value == 1) {
//                ok++;
//            } else {
//                notOk++;
//            }
//        } catch (ApiCaptchaException e) {
//            String decryption = captcha(e.getImage());
//            add(actor, groupId, friendId, e.getSid(), decryption);
//            if (captchaError){
//                VkMessage.sendMessage("Captcha Error");
//                captchaError = false;
//            }
//            captchaError = true;
//        } catch (ApiException | ClientException | InterruptedException e) {
////            e.printStackTrace();
//            notOk++;
//            System.out.println(e.getMessage());
//        }
    }

    private static UserActor getActor() {
        if ((int) (Math.random() * 2) == 1) {
            return new UserActor(ID_GARSAY, GARSAY_TOKEN);
        } else {
            return new UserActor(ID_IVANOV, IVANOV_TOKEN);
        }
    }

    private static Integer getGroupId() {
        switch ((int) (Math.random() * 3)) {
            case 1:
                return TEHNO_ID;
            case 2:
                return MTS_ID;
            default:
                return PHOBOS_ID;
        }
    }

    private static List<Integer> getFriends(UserActor actor, int groupId) {
        List<Integer> allFriends = new ArrayList<>();
        try {
            Thread.sleep(400);
            System.out.println("user id = " + actor.getId());
            allFriends = vk.friends()
                    .get(actor)
                    .count(5000)
                    .execute()
                    .getItems();
            allFriends.removeAll(getGroupMembers(actor, groupId));
            allFriends.removeAll(WasAdd.loadWasAdd(actor.getId(), groupId));
        } catch (ApiException | ClientException | InterruptedException e) {
            e.printStackTrace();
        }
        if (allFriends.size() < 41) {
            allFriends = ifFriendsFinish(actor, groupId);
        }
        Collections.shuffle(allFriends);
        Collections.shuffle(allFriends);
        allFriends.subList(0, 40);
        Collections.shuffle(allFriends);
        return allFriends;
    }

    private static List<Integer> ifFriendsFinish(UserActor actor, int groupId) {
        sendMessageNotFriends(actor.getId(), groupId);
        loop++;
        if (loop > 5) {
            VkMessage.sendMessage("ВСЕ ДРУЗЬЯ КОНЧИЛИСЬ!!!!!!");
            return new ArrayList<>();
        }
        end.remove(groupId);
        if (end.size() > 0) {
            Collections.shuffle(end);
            return getFriends(actor, end.get(0));
        } else {
            initEnd();
            if (actor.getId() == ID_GARSAY) {
                VkMessage.sendMessage("КОНЧИЛИСЬ ВСЕ ДРУЗЬЯ у ГАРСЕЯ!!!!!!");
                actor = new UserActor(ID_IVANOV, MessageUtils.loadPassword(IVANOV_TOKEN));
            } else {
                VkMessage.sendMessage("КОНЧИЛИСЬ ВСЕ ДРУЗЬЯ у ИВАНОВА!!!!!!");
                actor = new UserActor(ID_GARSAY, MessageUtils.loadPassword(TOKEN_GARSEY));
            }
            return getFriends(actor, groupId);
        }
    }

    private static List<Integer> getGroupMembers(UserActor actor, int groupId) throws ClientException, ApiException, InterruptedException {
        List<Integer> groupMembers = new ArrayList<>();
        int loop = 0;
        while (true) {
            List<Integer> items = vk.groups().getMembers(actor)
                    .groupId(String.valueOf(groupId))
                    .count(1000)
                    .offset(loop * 1000)
                    .execute()
                    .getItems();
            groupMembers.addAll(items);
            if (items.size() < 1000) {
                break;
            }
            loop++;
            Thread.sleep(400);
        }
        return groupMembers;
    }

    private static void sendMessageNotFriends(int userId, int groupId) {
        String name = getUserNameFile(userId);
        String groupName = getGroupNameFile(groupId);
        VkMessage.sendMessage("Друзья кочались у " + name + " для группы " + groupName);
    }

    private static void initEnd() {
        end = new ArrayList<>();
        end.add(TEHNO_ID);
        end.add(MTS_ID);
        end.add(PHOBOS_ID);
    }
}
