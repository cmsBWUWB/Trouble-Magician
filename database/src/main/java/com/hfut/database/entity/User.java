package com.hfut.database.entity;

import org.greenrobot.greendao.annotation.*;

import java.util.List;
import org.greenrobot.greendao.DaoException;

@Entity
public class User {
    @Id
    private Long id;
    @NotNull
    @Unique
    private String username;
    @ToMany(referencedJoinProperty = "ownerId")
    private List<Chat> chatList;
    @ToMany
    @JoinEntity(entity = GroupJoinUser.class, sourceProperty = "userId", targetProperty = "groupId")
    private List<Group> groupList;
    @ToMany
    @JoinEntity(entity = UserJoinUser.class, sourceProperty = "userId1", targetProperty = "userId2")
    private List<User> contactList;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;
    @Generated(hash = 1028783970)
    public User(Long id, @NotNull String username) {
        this.id = id;
        this.username = username;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1401314586)
    public List<Chat> getChatList() {
        if (chatList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChatDao targetDao = daoSession.getChatDao();
            List<Chat> chatListNew = targetDao._queryUser_ChatList(id);
            synchronized (this) {
                if (chatList == null) {
                    chatList = chatListNew;
                }
            }
        }
        return chatList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 70989346)
    public synchronized void resetChatList() {
        chatList = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 897975955)
    public List<Group> getGroupList() {
        if (groupList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GroupDao targetDao = daoSession.getGroupDao();
            List<Group> groupListNew = targetDao._queryUser_GroupList(id);
            synchronized (this) {
                if (groupList == null) {
                    groupList = groupListNew;
                }
            }
        }
        return groupList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 114754500)
    public synchronized void resetGroupList() {
        groupList = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1369241256)
    public List<User> getContactList() {
        if (contactList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            List<User> contactListNew = targetDao._queryUser_ContactList(id);
            synchronized (this) {
                if (contactList == null) {
                    contactList = contactListNew;
                }
            }
        }
        return contactList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1466168391)
    public synchronized void resetContactList() {
        contactList = null;
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
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }
}
