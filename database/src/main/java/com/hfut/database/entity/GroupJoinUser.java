package com.hfut.database.entity;

import org.greenrobot.greendao.annotation.*;

@Entity
public class GroupJoinUser {
    @Id
    private Long id;
    @NotNull
    private Long groupId;
    @NotNull
    private Long userId;
    @Generated(hash = 1936824087)
    public GroupJoinUser(Long id, @NotNull Long groupId, @NotNull Long userId) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
    }
    @Generated(hash = 1961885949)
    public GroupJoinUser() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getGroupId() {
        return this.groupId;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
