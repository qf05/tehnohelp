package ru.tehnohelp.wallpost;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class LoadPosts {

    protected static Post loadPost(Command command) {
        InputStream resourceAsStream = LoadPosts.class.getClassLoader().getResourceAsStream("/posts/posts.xml");
        Post post = null;
        if (resourceAsStream != null) {
            try {
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
                    post = new Post(message, attach + ",https://tehnoluga.ru");
                } else {
                    VkWallPosting.errorInLoad++;
                }
            } catch (SAXException | IOException | ParserConfigurationException | NullPointerException e) {
                e.printStackTrace();
                VkWallPosting.errorInLoad++;
            }
        } else {
            VkWallPosting.errorInLoad++;
        }
        closeResource(resourceAsStream);
        return post;
    }

    protected static String loadToken() {
        InputStream resourceAsStream = LoadPosts.class.getClassLoader().getResourceAsStream("/properties/password.properties");
        Properties prop = new Properties();
        String token = "";
        try {
            prop.load(resourceAsStream);
            token = prop.getProperty("token_garsey");
        } catch (IOException e) {
            e.printStackTrace();
            VkWallPosting.errorInLoad++;
        }
        closeResource(resourceAsStream);
        return token;
    }

    protected static List<Integer> loadIds() {
        List<Integer> ids = new ArrayList<>();
        InputStream inputStream = LoadPosts.class.getClassLoader().getResourceAsStream("/posts/groupIds.txt");
        try {
            if (inputStream != null) {
                String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                String[] stringIds = result.split(",");
                for (String s : stringIds) {
                    try {
                        int i = Integer.parseInt(s);
                        ids.add(i * -1);
                    } catch (Exception e) {
                        VkWallPosting.errorInLoad++;
                    }
                }
            } else {
                VkWallPosting.errorInLoad++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            VkWallPosting.errorInLoad++;
        }
        closeResource(inputStream);
        return ids;
    }

    public static Time getTimeProperty() {
        InputStream resourceAsStream = LoadPosts.class.getClassLoader().getResourceAsStream("/posts/time.properties");
        Properties prop = new Properties();
        Time time = null;
        try {
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
        closeResource(resourceAsStream);
        return time;
    }

    public static void saveTimeMap(Map<Command, Long> map) {
        URL resource = LoadPosts.class.getClassLoader().getResource("/posts/resume.txt");
        File f = null;
        try {
            assert resource != null;
            f = new File(resource.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assert f != null;
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(f))) {
            objectOutputStream.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Command, Long> loadTimeMap() {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(
                LoadPosts.class.getClassLoader().getResourceAsStream("/posts/resume.txt"))){
            return (Map<Command, Long>) objectInputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void closeResource(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                VkWallPosting.errorInLoad++;
            }
        }
    }
}
