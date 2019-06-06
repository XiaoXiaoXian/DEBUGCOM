package sample;

import gnu.io.SerialPortEventListener;

/**
 * Project:
 * Author : zhaozhen
 * Email  : 2399144252@qq.com
 * Date   : 2019/6/6
 */
public interface ReceiveListener {

    /**
     * 自定义接收处理体
     * @return
     */
    public SerialPortEventListener getReceiveListener();
}
