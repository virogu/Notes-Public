## SerialUtil

```java
import android.text.TextUtils;

public class SerialUtil {
    private static final String TAG = "SerialUtil";

    public static byte[] encodeSerialData(byte[] data, int len) {
        if ((data == null) || (len <= 0) || (data.length < len)) {
            return null;
        }

        byte[] buf = new byte[len * 2 + 2];
        int pos = 0;
        buf[pos++] = (byte) 0x7E;
        for (int i = 0; i < len; i++) {
            if ((data[i] == (byte) 0x7E) || (data[i] == (byte) 0x7D)) {
                buf[pos++] = (byte) 0x7D;
                buf[pos++] = (byte) (data[i] & (byte) (~0x40));
            } else {
                buf[pos++] = data[i];
            }
        }
        buf[pos++] = (byte) 0x7E;

        byte[] output = new byte[pos];
        System.arraycopy(buf, 0, output, 0, pos);
        return output;
    }

    public static byte[] decodeSerialData(byte[] data, int len) {
        if ((data == null) || (len <= 0) || (data.length < len) || (data[0] != (byte) 0x7E)) {
            return null;
        }

        int pos = 0;
        byte[] buf = new byte[len];
        boolean convert = false;
        for (int i = 1; i < len; i++) {
            if (data[i] == (byte) 0x7E) {
                break;
            }
            if (data[i] == (byte) 0x7D) {
                convert = true;
                continue;
            }

            if (convert) {
                buf[pos++] = (byte) (data[i] | (byte) (0x40));
                convert = false;
            } else {
                buf[pos++] = data[i];
            }
        }

        if (pos > 0) {
            byte[] output = new byte[pos];
            System.arraycopy(buf, 0, output, 0, pos);
            return output;
        }

        return null;
    }

    public static String bytesToHexString(byte[] buffer, int length) {
        return bytesToHexString(buffer, 0, length);
    }

    public static String bytesToHexString(byte[] buffer, int start, int length) {
        if ((buffer == null) || (buffer.length == 0)) {
            return null;
        }

        if ((start < 0) || (length <= 0) || ((start + length) > buffer.length)) {
            return null;
        }

        StringBuilder sb = new StringBuilder(length);
        String temp;

        for (int i = start; i < start + length; i++) {
            temp = Integer.toHexString(0xFF & buffer[i]);
            if (temp.length() < 2) {
                sb.append(0);
            }
            sb.append(temp.toUpperCase());
        }

        return sb.toString();
    }

    public static byte[] hexStringToBytes(String string) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        final int len = string.length();
        if ((len == 0) || (string.length() % 2 != 0)) {
            return null;
        }
        final char[] chars = string.toCharArray();
        final byte[] data = new byte[len / 2];
        int index = 0;
        for (int i = 0; i < len; i += 2) {
            int highBit = hexCharToByte(chars[i]);
            int lowBit = hexCharToByte(chars[i + 1]);

            if (highBit != -1 && lowBit != -1) {
                data[index] = (byte) (highBit << 4 | lowBit);
            } else {
                return null;
            }
            index++;
        }
        return data;
    }

    public static int hexCharToByte(char hexChar) {
        if ('0' <= hexChar && hexChar <= '9') {
            return hexChar - '0';
        }
        if ('A' <= hexChar && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        }
        if ('a' <= hexChar && hexChar <= 'f') {
            return hexChar - 'a' + 10;
        }
        return -1;
    }

    public static Integer bytesToInt(byte[] src, int offset) {
        if ((src == null) || (offset < 0) || ((offset + 4) > src.length)) {
            return null;
        }
        return (int) ((src[offset + 3] & 0xFF)
                | ((src[offset + 2] & 0xFF) << 8)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset] & 0xFF) << 24));
    }
}

```
