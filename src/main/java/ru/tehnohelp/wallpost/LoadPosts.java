package ru.tehnohelp.wallpost;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LoadPosts {

    protected static Post loadPost(Command command) {
        InputStream resourceAsStream = LoadPosts.class.getClassLoader().getResourceAsStream("/posts/posts.xml");
        if (resourceAsStream!=null) {
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
                    return new Post(message, attach + ",https://tehnoluga.ru");
                } else {
                    VkWallPosting.errorInLoad++;
                }
            } catch (SAXException | IOException | ParserConfigurationException | NullPointerException e) {
                e.printStackTrace();
                VkWallPosting.errorInLoad++;
            }
        }else {
            VkWallPosting.errorInLoad++;
        }
        return null;
    }

    protected static String loadToken() {
        InputStream resourceAsStream = LoadPosts.class.getClassLoader().getResourceAsStream("/properties/password.properties");
        Properties prop = new Properties();
        try {
            prop.load(resourceAsStream);
            return prop.getProperty("token_garsey");
        } catch (IOException e) {
            e.printStackTrace();
            VkWallPosting.errorInLoad++;
        }
        return "";
    }

    protected static List<Integer> loadIds() {
        List<Integer> ids = new ArrayList<>();
        try {
            InputStream inputStream = LoadPosts.class.getClassLoader().getResourceAsStream("/posts/groupIds.txt");
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
        return ids;
    }

    public static Time getTimeProperty(){
        InputStream resourceAsStream = LoadPosts.class.getClassLoader().getResourceAsStream("/posts/time.properties");
        Properties prop = new Properties();
        try {
            prop.load(resourceAsStream);
            return new Time(Integer.parseInt(prop.getProperty("day_add")),
                    Integer.parseInt(prop.getProperty("day_random")),
                    Integer.parseInt(prop.getProperty("hour_add")),
                    Integer.parseInt(prop.getProperty("hour_random")),
                    Integer.parseInt(prop.getProperty("min_add")),
                    Integer.parseInt(prop.getProperty("min_random")),
                    Integer.parseInt(prop.getProperty("sec_add")),
                    Integer.parseInt(prop.getProperty("sec_random")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
