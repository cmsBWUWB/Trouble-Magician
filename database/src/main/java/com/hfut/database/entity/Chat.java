package com.hfut.database.entity;

import org.greenrobot.greendao.annotation.*;

import java.util.List;
import org.greenrobot.greendao.DaoException;

@Entity
public class Chat {
    public static final int GROUP = 0, FRIEND = 1;
    @Id
    private Long id;
    @NotNull
    private Long ownerId;//这个会话属于谁的？
    @NotNull
    private Integer chatType;
    @NotNull
    private Long targetId;//如果是群消息，则为群id，如果是好友消息，则为好友id
    @ToMany(referencedJoinProperty = "chatId")
    private List<Message> messageList;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1596497024)
    private transient ChatDao myDao;
    @Generated(hash = 708822468)
    public Chat(Long id, @NotNull Long ownerId, @NotNull Integer chatType,
            @NotNull Long targetId) {
        this.id = id;
        this.ownerId = ownerId;
        this.chatType = chatType;
        this.targetId = targetId;
    }
    @Generated(hash = 519536279)
    public Chat() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getOwnerId() {
        return this.ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Integer getChatType() {
        return this.chatType;
    }
    public void setChatType(Integer chatType) {
        this.chatType = chatType;
    }
    public Long getTargetId() {
        return this.targetId;
    }
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 806173331)
    public List<Message> getMessageList() {
        if (messageList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageDao targetDao = daoSession.getMessageDao();
            List<Message> messageListNew = targetDao._queryChat_MessageList(id);
            synchronized (this) {
                if (messageList == null) {
                    messageList = messageListNew;
                }
            }
        }
        return messageList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1946287196)
    public synchronized void resetMessageList() {
        messageList = null;
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
    @Generated(hash = 1004576325)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getChatDao() : null;
    }
}
