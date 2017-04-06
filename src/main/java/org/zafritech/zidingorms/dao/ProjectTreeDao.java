package org.zafritech.zidingorms.dao;

public class ProjectTreeDao {

    private Long id;
    private Long pId;
    private String name;
    private boolean open;
    private boolean isParent;
    private boolean click;
    private Long linkId;

    public ProjectTreeDao() {
    }

    // For folder with click event = FALSE and no url linkId set. 
    public ProjectTreeDao(Long id,
            Long pId,
            String name,
            boolean open,
            boolean isParent,
            boolean click) {

        this.id = id;
        this.pId = pId;
        this.name = name;
        this.open = open;
        this.isParent = isParent;
        this.click = click;
    }

    // For documents with click event = TRUE and url linkId set.
    public ProjectTreeDao(Long id,
            Long pId,
            String name,
            boolean open,
            boolean isParent,
            boolean click,
            Long linkId) {

        this.id = id;
        this.pId = pId;
        this.name = name;
        this.open = open;
        this.isParent = isParent;
        this.click = click;
        this.linkId = linkId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }
}
