package com.proptiger.delphi.model.master;

import java.io.Serializable;

public class ReasonStatusMappingModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8732403775186784830L;

    private Integer           id;
    private Boolean           isPresalesComment;
    private Boolean           isSalesComment;
    private Boolean           isHomeloanComment;
    private Integer           commentId;
    private String            comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsPresalesComment() {
        return isPresalesComment;
    }

    public void setIsPresalesComment(Boolean isPresalesComment) {
        this.isPresalesComment = isPresalesComment;
    }

    public Boolean getIsSalesComment() {
        return isSalesComment;
    }

    public void setIsSalesComment(Boolean isSalesComment) {
        this.isSalesComment = isSalesComment;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getIsHomeloanComment() {
        return isHomeloanComment;
    }

    public void setIsHomeloanComment(Boolean isHomeloanComment) {
        this.isHomeloanComment = isHomeloanComment;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result + ((commentId == null) ? 0 : commentId.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((isHomeloanComment == null) ? 0 : isHomeloanComment.hashCode());
        result = prime * result + ((isPresalesComment == null) ? 0 : isPresalesComment.hashCode());
        result = prime * result + ((isSalesComment == null) ? 0 : isSalesComment.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReasonStatusMappingModel other = (ReasonStatusMappingModel) obj;
        if (comment == null) {
            if (other.comment != null)
                return false;
        }
        else if (!comment.equals(other.comment))
            return false;
        if (commentId == null) {
            if (other.commentId != null)
                return false;
        }
        else if (!commentId.equals(other.commentId))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (isHomeloanComment == null) {
            if (other.isHomeloanComment != null)
                return false;
        }
        else if (!isHomeloanComment.equals(other.isHomeloanComment))
            return false;
        if (isPresalesComment == null) {
            if (other.isPresalesComment != null)
                return false;
        }
        else if (!isPresalesComment.equals(other.isPresalesComment))
            return false;
        if (isSalesComment == null) {
            if (other.isSalesComment != null)
                return false;
        }
        else if (!isSalesComment.equals(other.isSalesComment))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ReasonStatusMappingModel [id=" + id
                + ", isPresalesComment="
                + isPresalesComment
                + ", isSalesComment="
                + isSalesComment
                + ", isHomeloanComment="
                + isHomeloanComment
                + ", commentId="
                + commentId
                + ", comment="
                + comment
                + "]";
    }

}
