package util;

import com.google.flatbuffers.FlatBufferBuilder;
import protocol.OutLogin;

public class ArrayUtil {

    public static byte[] int2Byte(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((value & 0xFF000000) >> 24);
        bytes[1] = (byte) ((value >> 16) & 0xFF);
        bytes[2] = (byte) ((value >> 8) & 0xFF);
        bytes[3] = (byte) (value & 0xFF);
        return bytes;
    }

    public static byte[] outLogin() throws Exception {
        FlatBufferBuilder builder = new FlatBufferBuilder();
        OutLogin.startOutLogin(builder);
        OutLogin.addHasRole(builder, true);
        int endIndex = OutLogin.endOutLogin(builder);
        builder.finish(endIndex);
        return builder.sizedByteArray();
    }
}
