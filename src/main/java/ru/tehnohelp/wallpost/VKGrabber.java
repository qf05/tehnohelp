package ru.tehnohelp.wallpost;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.audio.Audio;
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
import ru.tehnohelp.message.util.MessageUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static ru.tehnohelp.message.util.MessageUtils.TOKEN_IVANOV;

public class VKGrabber {

    public static final int PHOBOS_GROUP_ID = -135479896;
    public static final int ID_IVANOV = 104618701;
    public static final String IVANOV_TOKEN = MessageUtils.loadPassword(TOKEN_IVANOV);

    private static final TransportClient transportClient = HttpTransportClient.getInstance();
    public static final VkApiClient vk = new VkApiClient(transportClient);
    private static UserActor actor = null;


    public String sendPost(String link, String date) {
        if (actor == null) {
            actor = new UserActor(ID_IVANOV, IVANOV_TOKEN);
        }
        String postId = parseLink(link);
        try {
            WallpostFull wallpostFull = vk.wall().getByIdExtended(actor, postId).execute().getItems().get(0);
            List<String> attachments = getAttachments(wallpostFull);

            String text = wallpostFull.getText();
            int time = parseDate(date);

            vk.wall().post(actor).message(text).attachments(attachments)
                    .publishDate(time).ownerId(PHOBOS_GROUP_ID).execute();

        } catch (ApiException | ClientException e) {
            e.printStackTrace();
            return "ERROR";
        }
        return "OK";
    }

    private static List<String> getAttachments(WallpostFull wallPostFull) {
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
        return attachList;
    }

    private String parseLink(String link) {
        int i = link.indexOf("wall");
        if (i < 0) {
            return null;
        }
        link = link.substring(i + 4);
        if (link.contains("%") && link.indexOf("%") > 0) {
            link = link.substring(0, link.indexOf("%"));
        }
        if (link.contains("?") && link.indexOf("?") > 0) {
            link = link.substring(0, link.indexOf("?"));
        }
        System.out.println(link);
        return link;
    }

    private int parseDate(String date) {
        date = date + ":00";
        LocalDateTime dateTime = LocalDateTime.parse(date);
        return (int) (dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000);
    }

    public String addDayToDate(String date) {
        date = date + ":00";
        LocalDateTime dateTime = LocalDateTime.parse(date).plusDays(1);
        return dateTime.toString();
    }
}
