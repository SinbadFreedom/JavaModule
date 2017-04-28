import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

public class ServerChildrenWatcher implements IZkChildListener {
    ZkClient zkClient;

    public ServerChildrenWatcher(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    @Override
    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
        LoadBalanceImpl.getInstance().updateLiveServerList(currentChilds);
    }
}
