package com.hfut.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserJoinUser {
    @Id
    private Long id;
    @NotNull
    private Long userId1;
    @NotNull
    private Long userId2;
    @Generated(hash = 906108495)
    public UserJoinUser(Long id, @NotNull Long userId1, @NotNull Long userId2) {
        this.id = id;
        this.userId1 = userId1;
        this.userId2 = userId2;
    }
    @Generated(hash = 1701433221)
    public UserJoinUser() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId1() {
        return this.userId1;
    }
    public void setUserId1(Long userId1) {
        this.userId1 = userId1;
    }
    public Long getUserId2() {
        return this.userId2;
    }
    public void setUserId2(Long userId2) {
        this.userId2 = userId2;
    }
}
