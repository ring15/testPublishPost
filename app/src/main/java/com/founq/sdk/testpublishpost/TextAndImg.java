package com.founq.sdk.testpublishpost;

/**
 * Created by ring on 2020/9/22.
 */
public class TextAndImg {
    String content;
    Boolean isPhoto;
    int photoIndex;

    public TextAndImg() {
    }

    public TextAndImg(String content, Boolean isPhoto, int photoIndex) {
        this.content = content;
        this.isPhoto = isPhoto;
        this.photoIndex = photoIndex;
    }

    public String getContent() {
        return content;
    }

    public Boolean getPhoto() {
        return isPhoto;
    }

    public int getPhotoIndex() {
        return photoIndex;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPhoto(Boolean photo) {
        isPhoto = photo;
    }

    public void setPhotoIndex(int photoIndex) {
        this.photoIndex = photoIndex;
    }
}
