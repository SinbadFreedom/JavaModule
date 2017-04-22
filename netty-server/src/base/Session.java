package base;

import io.netty.channel.Channel;

public class Session {
    private Channel channel = null;
    private int accountId;

    public Session(Channel channel, int accountId) {
        this.channel = channel;
        this.accountId = accountId;
    }

    public Channel getChannel() {
        return channel;
    }

    public int getAccountId() {
        return accountId;
    }
}
