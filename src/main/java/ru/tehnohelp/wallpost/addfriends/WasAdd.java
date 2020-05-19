package ru.tehnohelp.wallpost.addfriends;

import ru.tehnohelp.wallpost.LoadPosts;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.tehnohelp.wallpost.VkWallPosting.ID_GARSAY;
import static ru.tehnohelp.wallpost.addfriends.AddFriends.*;

public class WasAdd {

    public static void saveWas(List<Integer> was, int userId, int groupId) {
        StringBuilder builder = new StringBuilder();
        was.forEach(i -> builder.append(i).append(","));
        try (FileWriter writer = new FileWriter(getFile(userId, groupId), true)) {
            writer.write(builder.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> loadWasAdd(int userId, int groupId) {
        try (FileReader reader = new FileReader(getFile(userId, groupId))) {
            char[] buf = new char[256];
            int c;
            StringBuilder builder = new StringBuilder();
            while ((c = reader.read(buf)) > 0) {
                if (c < 256) {
                    buf = Arrays.copyOf(buf, c);
                }
                builder.append(buf);
            }
            String s = builder.toString();
            return Arrays.stream(s.split(","))
                    .filter(i -> i.length() > 2)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static String getUserNameFile(int userId) {
        String name = "";
        if (userId == ID_GARSAY) {
            name = "garsay";
        } else {
            name = "ivanov";
        }
        return name;
    }

    public static String getGroupNameFile(int groupId) {
        String groupName = "";
        if (groupId == TEHNO_ID) {
            groupName = "Tehno";
        } else if (groupId == PHOBOS_ID) {
            groupName = "Phobos";
        } else if (groupId == MTS_ID) {
            groupName = "Mts";
        }
        return groupName;
    }

    private static String getFile(int userId, int groupId) {
//        VkMessage.sendMessage(Paths.get(".").normalize().toAbsolutePath().toFile().getAbsolutePath());
        String name = getUserNameFile(userId);
        String groupName = getGroupNameFile(groupId);
        System.out.println(name + groupName);
        String fileName = "/addid/" + name + groupName + ".txt";
        return LoadPosts.PATH + fileName;
//        return "D:\\Project\\src\\main\\resources" + fileName;
    }


}
