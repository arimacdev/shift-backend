package com.arimac.backend.pmtool.projectmanagementtool.dtos.Slack;

public class SlackBlock {
    private String type;
    private SlackText text = new SlackText();
    private SlackAccessory accessory = new SlackAccessory();


    public SlackBlock() {
    }

    public SlackBlock(String type) {
        this.type = type;
    }

    public SlackBlock(String type, SlackText text) {
        this.type = type;
        this.text = text;
    }

    public SlackBlock(String type, SlackText text, SlackAccessory accessory) {
        this.type = type;
        this.text = text;
        this.accessory = accessory;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SlackText getText() {
        return text;
    }

    public void setText(SlackText text) {
        this.text = text;
    }

    public SlackAccessory getAccessory() {
        return accessory;
    }

    public void setAccessory(SlackAccessory accessory) {
        this.accessory = accessory;
    }
}
