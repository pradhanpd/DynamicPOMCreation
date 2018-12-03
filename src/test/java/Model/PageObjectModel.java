package Model;

public class PageObjectModel {
    private String tagName;

    public TagType getTagType() {
        return tagType;
    }

    public void setTagType(TagType tagType) {
        this.tagType = tagType;
    }

    private TagType tagType;
    private TagAttribute tagAttribute;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public TagAttribute getTagAttribute() {
        return tagAttribute;
    }

    public void setTagAttribute(TagAttribute tagAttribute) {
        this.tagAttribute = tagAttribute;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    private String tagValue;
}


