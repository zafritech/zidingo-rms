package org.zafritech.zidingorms.database.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Indexed
@Entity(name = "REF_ITEM_COMMENTS")
public class ItemComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private String uuId;

    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item item;

    @Field(store = Store.NO)
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "authorId")
    private User author;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public ItemComment() {
        
    }

    public ItemComment(Item item, String comment) {

        this.uuId = UUID.randomUUID().toString();
        this.item = item;
        this.comment = comment;
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    public ItemComment(Item item, String comment, User author) {

        this.uuId = UUID.randomUUID().toString();
        this.item = item;
        this.comment = comment;
        this.author = author;
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        
        return "ItemComment{" + "id=" + id + ", uuId=" + uuId + ", item=" + 
                item + ", comment=" + comment + ", author=" + author + 
                ", creationDate=" + creationDate + '}';
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }
}
