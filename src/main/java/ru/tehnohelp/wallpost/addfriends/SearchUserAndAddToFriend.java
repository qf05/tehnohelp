//package ru.tehnohelp.wallpost.addfriends;
//
//import com.vk.api.sdk.client.actors.UserActor;
//import com.vk.api.sdk.exceptions.ApiCaptchaException;
//import com.vk.api.sdk.exceptions.ApiException;
//import com.vk.api.sdk.exceptions.ClientException;
//import com.vk.api.sdk.objects.friends.responses.AddResponse;
//import com.vk.api.sdk.objects.groups.UserXtrRole;
//import com.vk.api.sdk.objects.users.UserFull;
//import com.vk.api.sdk.objects.users.UserMin;
//import ru.tehnohelp.message.VkMessage;
//
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.RequestScoped;
//import javax.faces.bean.ViewScoped;
//import java.io.Serializable;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static ru.tehnohelp.wallpost.VkWallPosting.captcha;
//import static ru.tehnohelp.wallpost.VkWallPosting.vk;
//import static ru.tehnohelp.wallpost.addfriends.AddFriends.ID_IVANOV;
//import static ru.tehnohelp.wallpost.addfriends.AddFriends.IVANOV_TOKEN;
//
//@ManagedBean(name = "findUser")
//@ViewScoped
//@RequestScoped
//public class SearchUserAndAddToFriend implements Serializable {
//
//    private static UserActor actor = new UserActor(ID_IVANOV, IVANOV_TOKEN);
//
//    private static int countSearch = 0;
//    private static int resultCount = 0;
//
//    private static int loop = 0;
//    private static int ok = 0;
//    private static int notOk = 0;
//
//    public static String addToFriends() {
//        loop = 0;
//        ok = 0;
//        notOk = 0;
//        List<UserFull> items = findFriends();
//        for (int i = 0; i < items.size(); i++) {
//            loop = 0;
//            captchaError = false;
//            add(actor, items.get(i).getId(), null, null);
//            System.out.println("i = " + i +"; ok - " + ok + "; not ok - " + notOk);
//        }
//        return "add " + items.size() + " to friend" +
//                 " and ok = " + ok + " notOk = " + notOk;
//    }
//
//    private static boolean captchaError = false;
//
//    private static void add(UserActor actor, Integer UserId, String captchaSid, String captchaKey) {
//        loop++;
//        if (loop > 5) {
//            notOk++;
//            return;
//        }
//        try {
//            if (captchaSid == null) {
//                Thread.sleep(3176 + (int) (Math.random() * 42028));
//            } else {
//                Thread.sleep(4238 + (int) (Math.random() * 5294));
//            }
//            Integer value = vk.friends().add(actor, UserId)
//                    .captchaSid(captchaSid)
//                    .captchaKey(captchaKey)
//                    .execute().getValue();
//            if (value == 1) {
//                ok++;
//            } else {
//                notOk++;
//            }
//        } catch (ApiCaptchaException e) {
//            String decryption = captcha(e.getImage());
//            add(actor, UserId, e.getSid(), decryption);
//            if (captchaError) {
//                VkMessage.sendMessage("Captcha Error");
//                captchaError = false;
//            }
//            captchaError = true;
//        } catch (ApiException | ClientException | InterruptedException e) {
//            notOk++;
//            System.out.println(e.getMessage());
//        }
//    }
//
//
//    public static List<UserFull> findFriends() {
//
//        List<UserFull> items = findAll();
//        System.out.println("result " + items.size());
//        List<UserFull> suggestions = findSuggestions();
//        List<UserFull> finalItems = items;
//        List<Integer> banned = new ArrayList<>();
//        try {
//            banned.addAll(vk.account().getBanned(actor).count(200).execute().getItems().stream().map(UserMin::getId).collect(Collectors.toList()));
//        } catch (ApiException | ClientException e) {
//            e.printStackTrace();
//        }
//
//
//        countSearch = items.size();
//        suggestions = suggestions.stream().filter(i -> !finalItems.contains(i)).collect(Collectors.toList());
//        items.addAll(suggestions);
//        System.out.println("result " + items.size());
//
////        List<UserXtrRole> tehnoList = getUsers();
////        List<Integer> tehnoId = tehnoList.stream().map(UserXtrRole::getId).collect(Collectors.toList());
//
//        System.out.println("START SORT");
//        Comparator<UserFull> comparing = Comparator.comparing(UserFull::getCommonCount).reversed();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, -2);
//        items = items.parallelStream()
//                .filter(i -> i.getFriendStatus() != null
//                        && i.getFriendStatus() == 0
//                        && i.hasPhoto()
//                        && !i.isFriend()
//                        && !banned.contains(i.getId())
//                        && i.getLastSeen() != null
////                        && !tehnoId.contains(i.getId())
//                        && !i.isOnline()
//                        && i.getLastSeen().getTime() > calendar.getTimeInMillis() / 1000 - (60 * 60 * 24 * 3)
//                        && i.canSendFriendRequest()
//                        && i.getCommonCount() != null)
//                .sorted(comparing)
//                .collect(Collectors.toList());
//        System.out.println("FINISH SORT  " + items.size());
//        resultCount = items.size();
//        if (items.size() > 601) {
//            items = items.subList(0, 600);
//        }
//        System.out.println("FINISH FIND friends");
//        Collections.shuffle(items);
//        Collections.shuffle(items);
//        int itemsCount = (int) (Math.random() * 17) + 1;
//        if (items.size() > itemsCount) {
//            items = items.subList(0, itemsCount);
//        }
//        System.out.println("FINISH FIND friends - " + items.size());
//        return items;
//    }
//
//    private static List<UserFull> findAll() {
//        int age = 14;
//        countSearch = 0;
//        List<UserFull> items = new ArrayList<>();
//        System.out.println("START FIND friends");
//
//        try {
//            while (age < 80) {
//                Thread.sleep(250);
//                List<UserFull> items1 = findUserFromCity(age);
//                Thread.sleep(250);
////                List<UserFull> items2 = findUserFromTown(age);
//
//
////                if (items1 != null && items2 != null) {
//                if (items1 != null) {
////                    items.addAll(items2);
//                    items.addAll(items1);
//
//                    System.out.println(items1.size());
////                    System.out.println(items2.size());
//                }
//                age = age > 50 ? age + 3 : age + 1;
//            }
//            items.addAll(findUserMinAndMax());
//            items = items.stream().distinct().collect(Collectors.toList());
//            System.out.println("above day - " + items.size());
//            List<UserFull> items3 = findUserFromDay();
////            List<UserFull> items4 = findUserFromDay2();
//            items.addAll(items3);
////            items.addAll(items4);
//            items = items.stream().distinct().collect(Collectors.toList());
//            System.out.println("after day - " + items.size());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Total: " + items.size());
//        items = items.stream().distinct().collect(Collectors.toList());
//
//        return items;
//    }
//
//    private static List<UserFull> findUserFromDay() throws ClientException, ApiException, InterruptedException {
//        List<UserFull> items = new ArrayList<>();
//        for (int i = 1; i <= 31; i++) {
//            Thread.sleep(250);
//            List<UserFull> items1 = vk.users().search(actor)
//                    .birthDay(i)
//
////                .hometown("Луга")
//                    .city(79)
//
//                    .fields(UserField.PHOTO_50,
//                            UserField.CITY,
//                            UserField.BDATE,
//                            UserField.HOME_TOWN,
//                            UserField.ONLINE,
//                            UserField.FRIEND_STATUS,
//                            UserField.IS_FRIEND,
//                            UserField.CAN_WRITE_PRIVATE_MESSAGE,
//                            UserField.HOME_TOWN,
//                            UserField.COMMON_COUNT,
//                            UserField.CAN_SEND_FRIEND_REQUEST,
//                            UserField.LAST_SEEN,
//                            UserField.HAS_PHOTO)
//                    .count(1000)
//                    .execute().getItems();
//            System.out.println("From day " + i + "  ---  " + items1.size());
//            items.addAll(items1);
//        }
//        return items;
//    }
//
//    private static List<UserFull> findUserFromDay2() throws ClientException, ApiException, InterruptedException {
//        List<UserFull> items = new ArrayList<>();
//        for (int i = 1; i <= 31; i++) {
//            Thread.sleep(250);
//            List<UserFull> items1 = vk.users().search(actor)
//                    .birthDay(i)
//
//                    .hometown("Луга")
////                    .city(79)
//
//                    .fields(UserField.PHOTO_50,
//                            UserField.CITY,
//                            UserField.BDATE,
//                            UserField.HOME_TOWN,
//                            UserField.ONLINE,
//                            UserField.FRIEND_STATUS,
//                            UserField.IS_FRIEND,
//                            UserField.CAN_WRITE_PRIVATE_MESSAGE,
//                            UserField.HOME_TOWN,
//                            UserField.COMMON_COUNT,
//                            UserField.CAN_SEND_FRIEND_REQUEST,
//                            UserField.LAST_SEEN,
//                            UserField.HAS_PHOTO)
//                    .count(1000)
//                    .execute().getItems();
//            System.out.println("From day " + i + "  ---  " + items1.size());
//            items.addAll(items1);
//        }
//        return items;
//    }
//
//
//    private static List<UserFull> findUserFromCity(int age) throws ClientException, ApiException {
//        return vk.users().search(actor)
//                .ageFrom(age).ageTo(age > 50 ? age + 2 : age)
//
////                .hometown("Луга")
//                .city(79)
//
//                .fields(UserField.PHOTO_50,
//                        UserField.CITY,
//                        UserField.BDATE,
//                        UserField.HOME_TOWN,
//                        UserField.ONLINE,
//                        UserField.FRIEND_STATUS,
//                        UserField.IS_FRIEND,
//                        UserField.CAN_WRITE_PRIVATE_MESSAGE,
//                        UserField.HOME_TOWN,
//                        UserField.COMMON_COUNT,
//                        UserField.CAN_SEND_FRIEND_REQUEST,
//                        UserField.LAST_SEEN,
//                        UserField.HAS_PHOTO)
//                .count(1000)
//                .execute().getItems();
//    }
//
//    private static List<UserFull> findUserFromTown(int age) throws ClientException, ApiException {
//        return vk.users().search(actor)
//                .ageFrom(age).ageTo(age > 50 ? age + 2 : age)
//
//                .hometown("Луга")
////                .city(79)
//                .fields(UserField.PHOTO_50,
//                        UserField.CITY,
//                        UserField.BDATE,
//                        UserField.HOME_TOWN,
//                        UserField.ONLINE,
//                        UserField.FRIEND_STATUS,
//                        UserField.IS_FRIEND,
//                        UserField.CAN_WRITE_PRIVATE_MESSAGE,
//                        UserField.HOME_TOWN,
//                        UserField.COMMON_COUNT,
//                        UserField.CAN_SEND_FRIEND_REQUEST,
//                        UserField.LAST_SEEN,
//                        UserField.HAS_PHOTO)
//                .count(1000)
//                .execute().getItems();
//    }
//
//    private static List<UserFull> findUserMinAndMax() throws ClientException, ApiException, InterruptedException {
//        List<UserFull> list = new ArrayList<>();
//        list.addAll(vk.users().search(actor)
//                .ageTo(14)
//
//                .hometown("Луга")
////                .city(79)
//                .fields(UserField.PHOTO_50,
//                        UserField.CITY,
//                        UserField.BDATE,
//                        UserField.HOME_TOWN,
//                        UserField.ONLINE,
//                        UserField.FRIEND_STATUS,
//                        UserField.IS_FRIEND,
//                        UserField.CAN_WRITE_PRIVATE_MESSAGE,
//                        UserField.HOME_TOWN,
//                        UserField.COMMON_COUNT,
//                        UserField.CAN_SEND_FRIEND_REQUEST,
//                        UserField.LAST_SEEN,
//                        UserField.HAS_PHOTO)
//                .count(1000)
//                .execute().getItems());
//        Thread.sleep(250);
//        list.addAll(vk.users().search(actor)
//                .ageFrom(80)
//
//                .hometown("Луга")
////                .city(79)
//                .fields(UserField.PHOTO_50,
//                        UserField.CITY,
//                        UserField.BDATE,
//                        UserField.HOME_TOWN,
//                        UserField.ONLINE,
//                        UserField.FRIEND_STATUS,
//                        UserField.IS_FRIEND,
//                        UserField.CAN_WRITE_PRIVATE_MESSAGE,
//                        UserField.HOME_TOWN,
//                        UserField.COMMON_COUNT,
//                        UserField.CAN_SEND_FRIEND_REQUEST,
//                        UserField.LAST_SEEN,
//                        UserField.HAS_PHOTO)
//                .count(1000)
//                .execute().getItems());
//        Thread.sleep(250);
//        list.addAll(vk.users().search(actor)
//                .ageTo(14)
//                .city(79)
//                .fields(UserField.PHOTO_50,
//                        UserField.CITY,
//                        UserField.BDATE,
//                        UserField.HOME_TOWN,
//                        UserField.ONLINE,
//                        UserField.FRIEND_STATUS,
//                        UserField.IS_FRIEND,
//                        UserField.CAN_WRITE_PRIVATE_MESSAGE,
//                        UserField.HOME_TOWN,
//                        UserField.COMMON_COUNT,
//                        UserField.CAN_SEND_FRIEND_REQUEST,
//                        UserField.LAST_SEEN,
//                        UserField.HAS_PHOTO)
//                .count(1000)
//                .execute().getItems());
//        Thread.sleep(250);
//        list.addAll(vk.users().search(actor)
//                .ageFrom(80)
//                .city(79)
//                .fields(UserField.PHOTO_50,
//                        UserField.CITY,
//                        UserField.BDATE,
//                        UserField.HOME_TOWN,
//                        UserField.ONLINE,
//                        UserField.FRIEND_STATUS,
//                        UserField.IS_FRIEND,
//                        UserField.CAN_WRITE_PRIVATE_MESSAGE,
//                        UserField.HOME_TOWN,
//                        UserField.COMMON_COUNT,
//                        UserField.CAN_SEND_FRIEND_REQUEST,
//                        UserField.LAST_SEEN,
//                        UserField.HAS_PHOTO)
//                .count(1000)
//                .execute().getItems());
//        return list;
//    }
//
//    private static List<UserFull> findSuggestions() {
//        int counterLoop = 0;
//        countSearch = 0;
//        List<UserFull> items = new ArrayList<>();
//        System.out.println("START FIND friends");
//        while (counterLoop < 3) {
//            try {
//                Thread.sleep(200);
//                List<UserFull> items1 = vk.friends().getSuggestions(actor)
//                        .filter(FriendsGetSuggestionsFilter.MUTUAL)
//                        .offset(counterLoop * 500)
//                        .count(500)
//                        .fields(UserField.PHOTO_50,
//                                UserField.CITY,
//                                UserField.BDATE,
//                                UserField.HOME_TOWN,
//                                UserField.ONLINE,
//                                UserField.FRIEND_STATUS,
//                                UserField.IS_FRIEND,
//                                UserField.CAN_WRITE_PRIVATE_MESSAGE,
//                                UserField.HOME_TOWN,
//                                UserField.COMMON_COUNT,
//                                UserField.CAN_SEND_FRIEND_REQUEST,
//                                UserField.LAST_SEEN,
//                                UserField.HAS_PHOTO)
//                        .execute().getItems();
//                if (items1 != null && items1.size() > 0) {
//                    System.out.println(items1.size());
//                    items1 = items1.stream().filter(i -> (i.getCity() != null && i.getCity().getId() == 79)
//                            || (i.getHomeTown() != null && "Луга".equalsIgnoreCase(i.getHomeTown().trim())))
//                            .collect(Collectors.toList());
//                    items.addAll(items1);
//                    countSearch = items.size();
//                    System.out.println(items1.size());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            counterLoop++;
//        }
//        System.out.println("Total: " + items.size());
//        return items;
//    }
//
//    private static String captchaImg = "";
//    private static String captchaSid = "";
//    private static String captchaKey = "";
//    //    private BufferedImage captchaBuf;
//    private static int idUserForCaptcha = -1;
//    private static int counterAdd = 0;
//
//    public int addFriend(int id) {
//        try {
//            if (id > 0) {
//                AddResponse response = vk.friends().add(actor, id).execute();
//                System.out.println(++counterAdd + " " + response.name() + " " + response.getValue());
//            } else {
//                AddResponse response = vk.friends().add(actor, idUserForCaptcha).captchaKey(captchaKey).captchaSid(captchaSid).execute();
//                System.out.println(++counterAdd + " " + response.name() + " " + response.getValue());
//            }
//        } catch (ApiCaptchaException e) {
//            System.out.println(e.getImage());
//            System.out.println(e.getSid());
//            if (id > 0) {
//                idUserForCaptcha = id;
//            }
//            captchaImg = e.getImage();
//            captchaSid = e.getSid();
//            return -1;
//        } catch (ApiException | ClientException e) {
////            e.printStackTrace();
//            System.out.println(e.getMessage());
//            return -2;
//        }
//        return 1;
//    }
//
//
//    private static List<UserXtrRole> getUsers() {
//        int offset = 0;
//        int count = 1;
//        int increment = 1;
//        List<UserXtrRole> resultList = new ArrayList<>();
//        System.out.println("START USER SEARCH");
//        try {
//            while (count > 0) {
//                List<UserXtrRole> items = vk.groups().getMembers(actor, UserField.LAST_SEEN, UserField.CITY)
//                        .groupId("tehnoluga")
//                        .offset(offset)
//                        .count(1000)
//                        .sort(GroupsGetMembersSort.ID_ASC)
//                        .execute()
//                        .getItems();
//                offset = increment * 1000;
//                count = items.size();
//                resultList.addAll(items);
//                Thread.sleep(200);
//                System.out.println(increment++ + "    find" + count);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("FIND: " + resultList.size());
//        return resultList;
//    }
//
//
//    public void setFriends(List<UserFull> friends) {
//    }
//
//    public int getCountSearch() {
//        return countSearch;
//    }
//
//    public void setCountSearch(int countSearch) {
//        this.countSearch = countSearch;
//    }
//
//    public String getCaptchaImg() {
//        return captchaImg;
//    }
//
//    public void setCaptchaImg(String captchaImg) {
//        this.captchaImg = captchaImg;
//    }
//
//    public String getCaptchaSid() {
//        return captchaSid;
//    }
//
//    public void setCaptchaSid(String captchaSid) {
//        this.captchaSid = captchaSid;
//    }
//
//    public String getCaptchaKey() {
//        return captchaKey;
//    }
//
//    public void setCaptchaKey(String captchaKey) {
//        this.captchaKey = captchaKey;
//    }
//
//    public int getIdUserForCaptcha() {
//        return idUserForCaptcha;
//    }
//
//    public void setIdUserForCaptcha(int idUserForCaptcha) {
//        this.idUserForCaptcha = idUserForCaptcha;
//    }
//
//    public int getResultCount() {
//        return resultCount;
//    }
//
//    public void setResultCount(int resultCount) {
//        this.resultCount = resultCount;
//    }
//
//    private int progress;
//
//    public int getProgress() {
//        int i = (countSearch + 1) / 200;
//        i++;
//        if (i > 100) {
//            i = 100;
//        }
//        return i;
//    }
//}
