/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.daos;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.zafritech.zidingorms.domain.User;

/**
 *
 * @author LukeS
 */
public class CommentDao {

    private Long id;

    private String uuId;

    private Long itemId;

    private String comment;

    private User author;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Override
    public String toString() {
        return "CommentDao{" + "id=" + id + ", uuId=" + uuId + ", itemId=" + itemId + ", comment=" + comment + ", author=" + author + ", creationDate=" + creationDate + '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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
}
