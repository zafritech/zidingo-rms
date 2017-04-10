/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.dao;

/**
 *
 * @author LukeS
 */
public class LinkDao {

    private Long id;

    private Long srcItemId;

    private Long srcArtifactId;

    private Long dstItemId;

    private Long dstArtifactId;

    private Long linkTypeId;

    public Long getId() {
        return id;
    }

    public Long getSrcItemId() {
        return srcItemId;
    }

    public void setSrcItemId(Long srcItemId) {
        this.srcItemId = srcItemId;
    }

    public Long getSrcArtifactId() {
        return srcArtifactId;
    }

    public void setSrcArtifactId(Long srcArtifactId) {
        this.srcArtifactId = srcArtifactId;
    }

    public Long getDstItemId() {
        return dstItemId;
    }

    public void setDstItemId(Long dstItemId) {
        this.dstItemId = dstItemId;
    }

    public Long getDstArtifactId() {
        return dstArtifactId;
    }

    public void setDstArtifactId(Long dstArtifactId) {
        this.dstArtifactId = dstArtifactId;
    }

    public Long getLinkTypeId() {
        return linkTypeId;
    }

    public void setLinkTypeId(Long linkTypeId) {
        this.linkTypeId = linkTypeId;
    }
}
