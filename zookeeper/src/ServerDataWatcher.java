import org.I0Itec.zkclient.IZkDataListener;
public class ServerDataWatcher implements IZkDataListener {

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {
        if (null != data) {
            String[] strArr = dataPath.split("/");
            int serverId = Integer.parseInt(strArr[2]);
            System.out.println("serverId: " + serverId + " data " + data);
        }
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {

    }
}
