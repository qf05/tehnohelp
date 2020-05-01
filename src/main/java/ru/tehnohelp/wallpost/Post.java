package ru.tehnohelp.wallpost;

import java.util.Objects;

public class Post {

    private String message;
    private String attachments;

    public Post(String message, String attachments) {
        this.message = message;
        this.attachments = attachments;
    }

    public String getMessage() {
        return message;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(message, post.message) &&
                Objects.equals(attachments, post.attachments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, attachments);
    }

    @Override
    public String toString() {
        return "Post{" +
                "message='" + message + '\'' +
                ", attachments='" + attachments + '\'' +
                '}';
    }
}
