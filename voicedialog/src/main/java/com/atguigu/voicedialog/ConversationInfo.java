package com.atguigu.voicedialog;

/**
 * 谈话信息
 *
 */
public class ConversationInfo {
    private String askerText; // 提问者数据
    private String answerText; // 回答者数据
    private int imageID; // 回答者的数据, 可选
    private boolean isAsker; // 当前是否是提问者


    public String getAskerText() {
        return askerText;
    }

    public void setAskerText(String askerText) {
        this.askerText = askerText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public boolean isAsker() {
        return isAsker;
    }

    public void setAsker(boolean isAsker) {
        this.isAsker = isAsker;
    }

    public ConversationInfo() {
        super();
    }

    public ConversationInfo(String askerText, String answerText, int imageID,
                            boolean isAsker) {
        super();
        this.askerText = askerText;
        this.answerText = answerText;
        this.imageID = imageID;
        this.isAsker = isAsker;
    }

    @Override
    public String toString() {
        return "ConversationInfo [askerText=" + askerText + ", answerText="
                + answerText + ", imageID=" + imageID + ", isAsker=" + isAsker
                + "]";
    }
}
