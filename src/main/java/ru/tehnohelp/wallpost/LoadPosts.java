package ru.tehnohelp.wallpost;

import com.vk.api.sdk.objects.audio.Audio;
import com.vk.api.sdk.objects.audio.AudioFull;
import com.vk.api.sdk.objects.docs.Doc;
import com.vk.api.sdk.objects.market.MarketAlbum;
import com.vk.api.sdk.objects.market.MarketItem;
import com.vk.api.sdk.objects.notes.Note;
import com.vk.api.sdk.objects.pages.WikipageFull;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoAlbum;
import com.vk.api.sdk.objects.polls.Poll;
import com.vk.api.sdk.objects.video.Video;
import com.vk.api.sdk.objects.wall.*;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import ru.tehnohelp.wallpost.addfriends.WasAdd;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static ru.tehnohelp.wallpost.VkWallPosting.loadPostFromWall;

public class LoadPosts {

    public static final String PATH = WasAdd.class.getResource("/properties/password.properties")
            .getPath().replace("/properties/password.properties", "");

    protected static Post loadPost(Command command) {
//                InputStream resourceAsStream = LoadPosts.class.getClassLoader().getResourceAsStream("/posts/posts.xml");
        Post post = null;
//        if (resourceAsStream != null) {
        try (InputStream resourceAsStream = new FileInputStream(PATH + "/posts/posts.xml")) {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(resourceAsStream);
            NodeList posts = document.getElementsByTagName("post");
            Element element = null;
            for (int i = 0; i < posts.getLength(); i++) {
                Node item = posts.item(i);
                NamedNodeMap attributes = item.getAttributes();
                String value = attributes.getNamedItem("id").getNodeValue();
                if (value.equals(command.value)) {
                    element = (Element) item;
                    break;
                }
            }
            if (element != null) {
                String message = element.getElementsByTagName("message").item(0).getTextContent().trim();
                String attach = element.getElementsByTagName("attachments").item(0).getTextContent().trim();
                post = new Post(message, attach + "," + getSite(command));
            } else {
                VkWallPosting.errorInLoad++;
            }
        } catch (SAXException | IOException | ParserConfigurationException | NullPointerException e) {
            e.printStackTrace();
            VkWallPosting.errorInLoad++;
        }
//        } else {
//            VkWallPosting.errorInLoad++;
//        }
//        closeResource(resourceAsStream);
        return post;
    }

    protected static Post loadPostFromGroup(Command command) {
//        InputStream resourceAsStream = LoadPosts.class.getClassLoader().getResourceAsStream("/posts/groupPosts.properties");
        Post post = null;
        Properties prop = new Properties();
//        if (resourceAsStream != null) {
        try (InputStream resourceAsStream = new FileInputStream(PATH + "/posts/groupPosts.properties")) {
            prop.load(resourceAsStream);
            String postId = prop.getProperty(command.value);
            if (postId.contains("null")) {
                return null;
            }
            WallpostFull wallPostFull = loadPostFromWall(postId);
            if (wallPostFull != null) {
                String attachment = getAttachments(wallPostFull);
                post = new Post(wallPostFull.getText(), attachment);
            } else {
                VkWallPosting.errorInLoad++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            VkWallPosting.errorInLoad++;
        }
//        } else {
//            VkWallPosting.errorInLoad++;
//        }
//        closeResource(resourceAsStream);
        return post;
    }

    protected static List<Integer> loadIds() {
        List<Integer> ids = new ArrayList<>();
//        InputStream inputStream = LoadPosts.class.getClassLoader().getResourceAsStream("/posts/groupIds.txt");
        try (InputStream inputStream = new FileInputStream(PATH + "/posts/groupIds.txt")) {
//            if (inputStream != null) {
            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8).trim();
            Set<String> strings = Arrays.stream(result.split("[\r\n,]"))
                    .map(String::trim)
                    .filter(i -> i.length() > 2)
                    .collect(Collectors.toSet());
            for (String s : strings) {
                try {
                    int i = Integer.parseInt(s);
                    ids.add(i * -1);
                } catch (Exception e) {
                    VkWallPosting.errorInLoad++;
                }
            }
//            } else {
//                VkWallPosting.errorInLoad++;
//            }
        } catch (Exception e) {
            e.printStackTrace();
            VkWallPosting.errorInLoad++;
        }
//        closeResource(inputStream);
        return ids;
    }

    public static Time getTimeProperty() {
//        InputStream resourceAsStream = LoadPosts.class.getClassLoader().getResourceAsStream("/posts/time.properties");
        Properties prop = new Properties();
        Time time = null;
        try (InputStream resourceAsStream = new FileInputStream(PATH + "/posts/time.properties")) {
            prop.load(resourceAsStream);
            time = new Time(Integer.parseInt(prop.getProperty("day_add")),
                    Integer.parseInt(prop.getProperty("day_random")),
                    Integer.parseInt(prop.getProperty("hour_add")),
                    Integer.parseInt(prop.getProperty("hour_random")),
                    Integer.parseInt(prop.getProperty("min_add")),
                    Integer.parseInt(prop.getProperty("min_random")),
                    Integer.parseInt(prop.getProperty("sec_add")),
                    Integer.parseInt(prop.getProperty("sec_random")));
        } catch (IOException e) {
            e.printStackTrace();
            VkWallPosting.errorInLoad++;
        }
//        closeResource(resourceAsStream);
        return time;
    }

    public static void saveTimeMap(Map<Command, Long> map) {
//        URL resource = LoadPosts.class.getClassLoader().getResource("/posts/resume.txt");
        File f = new File(PATH + "/posts/resume.txt");
//        try  {
//            assert resource != null;
//            f = new File(resource.toURI());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        assert f != null;
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(f))) {
            objectOutputStream.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Command, Long> loadTimeMap() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(PATH + "/posts/resume.txt"))) {
            return (Map<Command, Long>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getSite(Command comand) {
        switch (comand) {
            case DOM:
                return "https://tehnoluga.ru/door-phone";
            case VIDEO:
                return "https://tehnoluga.ru/video-surveillance";
            case TV:
            case INTERNET:
                return "https://tehnoluga.ru/tv_and_internet";
            default:
                return "https://tehnoluga.ru";
        }
    }

    private static String getAttachments(WallpostFull wallPostFull) {
        List<String> attachList = new ArrayList<>();
        List<WallpostAttachment> attachments = wallPostFull.getAttachments();
        for (WallpostAttachment wA : attachments) {
            WallpostAttachmentType type = wA.getType();
            String attach = "";
            switch (type) {
                case PHOTO:
                    Photo photo = wA.getPhoto();
                    attach = "photo" + photo.getOwnerId() + "_" + photo.getId();
                    break;
                case POSTED_PHOTO:
                    PostedPhoto photo1 = wA.getPostedPhoto();
                    attach = "photo" + photo1.getOwnerId() + "_" + photo1.getId();
                    break;
                case AUDIO:
                    Audio audio = wA.getAudio();
                    attach = "audio" + audio.getOwnerId() + "_" + audio.getId();
                    break;
                case VIDEO:
                    Video video = wA.getVideo();
                    attach = "video" + video.getOwnerId() + "_" + video.getId();
                    break;
                case DOC:
                    Doc doc = wA.getDoc();
                    attach = "doc" + doc.getOwnerId() + "_" + doc.getId();
                    break;
                case LINK:
                    attach = wA.getLink().getUrl();
                    break;
                case GRAFFITI:
                    Graffiti graffiti = wA.getGraffiti();
                    attach = "graffiti" + graffiti.getOwnerId() + "_" + graffiti.getId();
                    break;
                case NOTE:
                    Note note = wA.getNote();
                    attach = "note" + note.getOwnerId() + "_" + note.getId();
                    break;
                case APP:
                    break;
                case POLL:
                    Poll poll = wA.getPoll();
                    attach = "poll" + poll.getOwnerId() + "_" + poll.getId();
                    break;
                case PAGE:
                    WikipageFull page = wA.getPage();
                    attach = "page" + page.getCreatorId() + "_" + page.getId();
                    break;
                case ALBUM:
                    PhotoAlbum album = wA.getAlbum();
                    attach = "album" + album.getOwnerId() + "_" + album.getId();
                    break;
                case PHOTOS_LIST:
//                    List<String> photosList = wA.getPhotosList();
//                    StringBuilder builder = new StringBuilder();
//                    photosList.forEach(i->builder.append("photo").append(i).append(","));
//                    attach = builder.toString();
                    break;
                case MARKET_MARKET_ALBUM:
                    MarketAlbum marketMarketAlbum = wA.getMarketAlbum();
                    attach = "market_album" + marketMarketAlbum.getOwnerId() + "_" + marketMarketAlbum.getId();
                    break;
                case MARKET:
                    MarketItem market = wA.getMarket();
                    attach = "market" + market.getOwnerId() + "_" + market.getId();
                    break;
            }
            attachList.add(attach);
        }
        StringBuilder builder = new StringBuilder();
        attachList.forEach(i -> builder.append(i).append(","));
        return builder.toString();
    }
}
