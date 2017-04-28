import com.netflix.curator.x.zkclientbridge.CuratorZKClientBridge;
import org.I0Itec.zkclient.IZkConnection;
import org.I0Itec.zkclient.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

import java.util.ArrayList;
import java.util.List;

public class LoadBalanceImpl implements Runnable {

    public static int serverId = 0;

    public static String ZOOKEEPER_HOST_URL = "127.0.0.1:2181";
    public static String SERVER_NODE = null;
    private static ZkClient zkClient;

    public static final int ZOOKEEPER_TIME_OUT = 1000 * 1000;
    /**
     * 重连次数
     */
    public static final int ZOOKEEPER_RECONNECT_TIME = 10;

    public static String SERVER_PATH = "/server";
    private ServerDataWatcher serverDataWatcher = new ServerDataWatcher();

    public static LoadBalanceImpl getInstance() {
        return SingletonHolder.instance;
    }

    protected void startService() {
        /** host should be 127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002 */
        SERVER_NODE = SERVER_PATH + "/" + serverId;
        zkClient = this.createZkClient(ZOOKEEPER_HOST_URL);
        if (!zkClient.exists(SERVER_NODE)) {
            zkClient.createEphemeral(SERVER_NODE);
        }

        /** 侦听子节点*/
        if (!zkClient.exists(SERVER_PATH)) {
            zkClient.createPersistent(SERVER_PATH);
        }
        zkClient.subscribeChildChanges(SERVER_PATH, new ServerChildrenWatcher(zkClient));
        List<String> currentChilds = zkClient.getChildren(SERVER_PATH);
        this.updateLiveServerList(currentChilds);

        new Thread(LoadBalanceImpl.getInstance(), "LoadBalanceImpl-timer-thread").start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                /**
                 * 更新服务器状态
                 */
                LoadBalanceImpl.getInstance().updatePlayerNum(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private IZkConnection createZkConnection(String connectString) {
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, ZOOKEEPER_TIME_OUT,
                ZOOKEEPER_TIME_OUT, new RetryNTimes(ZOOKEEPER_RECONNECT_TIME, ZOOKEEPER_TIME_OUT));
        client.start();
        try {
            return new CuratorZKClientBridge(client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ZkClient createZkClient(String connectString) {
        try {
            return new ZkClient(createZkConnection(connectString), ZOOKEEPER_TIME_OUT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePlayerNum(int playerNum) {
        if (null == zkClient) {
            return;
        }
        try {
            if (!zkClient.exists(SERVER_NODE)) {
                zkClient.createEphemeral(SERVER_NODE);
            }

            zkClient.writeData(SERVER_NODE, playerNum + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLiveServerList(List<String> currentChilds) {
        ArrayList<Integer> list = new ArrayList<>();
        for (String str : currentChilds) {
            if (null == str || str.equals("null")) {
                continue;
            }
            int serverId = Integer.parseInt(str);
            list.add(serverId);
            System.out.println("updateLiveServerList serverId " + serverId);
            /** 重新添加侦听器*/
            zkClient.subscribeDataChanges("/server/" + serverId, serverDataWatcher);
        }
    }


    private static class SingletonHolder {
        public final static LoadBalanceImpl instance = new LoadBalanceImpl();
    }
}
