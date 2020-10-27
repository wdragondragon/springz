package org.jdragon.springz.web.core;

import java.io.Closeable;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.26 12:58
 * @Description:
 */
public class CloseUtil {
    public static void closeAll(Closeable... io) {
        for (Closeable closeable : io) {
            try {
                if (closeable != null)
                    closeable.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
}
