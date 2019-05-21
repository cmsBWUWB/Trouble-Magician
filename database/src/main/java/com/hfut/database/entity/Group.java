package com.hfut.database.entity;

import org.greenrobot.greendao.annotation.*;

import java.util.List;
import org.greenrobot.greendao.DaoException;
@Entity
public class Group {
    @Id
    private Long id;
    @NotNull
    @Unique
    private String groupId;
    @NotNull
    private String groupName;
    @NotNull
    private Long ownerId;
    @ToMany
    @JoinEntity(entity = GroupJoinUser.class, sourceProperty = "groupId", targetProperty = "userId")
    private List<User> members;//群成员
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1591306109)
    private transient GroupDao myDao;
    @Generated(hash = 949434967)
    public Group(Long id, @NotNull String groupId, @NotNull String groupName, @NotNull Long ownerId) {
        this.id = id;
        this.groupId = groupId;
        this.groupName = groupName;
        this.ownerId = ownerId;
    }
    @Generated(hash = 117982048)
    public Group() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getGroupId() {
        return this.groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public Long getOwnerId() {
        return this.ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1336169103)
    public List<User> getMembers() {
        if (members == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            List<User> membersNew = targetDao._queryGroup_Members(id);
            synchronized (this) {
                if (members == null) {
                    members = membersNew;
                }
            }
        }
        return members;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1358688666)
    public synchronized void resetMembers() {
        members = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1333602095)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGroupDao() : null;
    }
}
