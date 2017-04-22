import base.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class SessionImpl {

    private static ConcurrentHashMap<Channel, Session> sessions = new ConcurrentHashMap<>();

    public static SessionImpl getInstance() {
        return SingletonHolder.instance;
    }

    public void logInSession(Channel channel, int accountId) {
        for (Channel key : sessions.keySet()) {
            Session session = sessions.get(key);
            if (null == session) {
                sessions.remove(key);
            } else if (session.getAccountId() == accountId) {
                logoutSession(session.getChannel());
            }
        }
        sessions.put(channel, new Session(channel, accountId));
    }

    public void logoutSession(Channel channel) {
        if (sessions.containsKey(channel)) {
            Session session = sessions.get(channel);
            /** 清理session数据*/
            sessions.remove(channel);
            if (null != channel) {
                channel.close();
                channel.disconnect();
            }
        }
    }

    public ChannelFuture sendByChannel(Channel channel, byte[] mesg) {
        if (null != channel && null != mesg) {
            return channel.writeAndFlush(mesg);
        }
        return null;
    }

    public ChannelFuture sendByAccount(int account, byte[] mesg) {
        Enumeration<Session> enumeration = sessions.elements();
        while (enumeration.hasMoreElements()) {
            Session session = enumeration.nextElement();
            if (session.getAccountId() == account) {
                return this.sendByChannel(session.getChannel(), mesg);
            }
        }
        return null;
    }

    public Session getSessionByChannel(Channel channel) {
        return sessions.get(channel);
    }

    private static class SingletonHolder {
        public final static SessionImpl instance = new SessionImpl();
    }
}
