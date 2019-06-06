package sample.utils;

import gnu.io.*;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Project:
 * Author : zhaozhen
 * Email  : 2399144252@qq.com
 * Date   : 2019/6/5
 */
public class MySerialPort {

    private  String lastErrorMessage = null;

    private  byte[] lastSendData = null;

    private  String lastReadData = null;

    private byte[] lastReadBytes = null;

    public static void main(String[] args) {
//
//        Scanner scanner = new Scanner(System.in);
//        SerialPort serialPort = null;
//        while(true){
//            String in = scanner.nextLine();
//            switch (in){
//                case "list":
//                    getSystemPort();
//                    break;
//                case "open":
//                    serialPort = openSerialPort("COM1",9600, "" ,"","");
//                    setListenerToSerialPort(serialPort);
//                    break;
//                case "send":
//                    String sendInfo = scanner.nextLine();
//                    byte[] bytes = sendInfo.getBytes();
//                    sendData(serialPort, bytes);
//                    break;
//                case "read":
//
//                    break;
//                case "close":
//                    serialPort.close();
//                    break;
//            }
//        }
       // SerialPort serialPort = new MySerialPort().openSerialPort("COM1",9600, "" ,"","");

       // int a = 0;

    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public byte[] getLastSendData() {
        return lastSendData;
    }

    public String getLastReadData() {
        return lastReadData;
    }

    public byte[] getLastReadBytes() {
        return lastReadBytes;
    }

    private void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }

    private void setLastSendData(byte[] lastSendData) {
        this.lastSendData = lastSendData;
    }

    private void setLastReadData(String lastReadData) {
        this.lastReadData = lastReadData;
    }

    private void setLastReadBytes(byte[] lastReadBytes) {
        this.lastReadBytes = lastReadBytes;
    }

    /**
     * 格式化数据位
     * @param dataBits
     * @return
     */
    private int formatDataBits(String dataBits){
        switch (dataBits){
            case "7":return SerialPort.DATABITS_7;
            case "6":return SerialPort.DATABITS_6;
            case "5":return SerialPort.DATABITS_5;
            default: return SerialPort.DATABITS_8;
        }
    }

    /**
     * 格式化停止位
     */
    private int formatStopBits(String stopBits){
        switch (stopBits){
            case "1.5":return SerialPort.STOPBITS_1_5;
            case "2":return SerialPort.STOPBITS_2;
            default:return SerialPort.STOPBITS_1;
        }
    }

    /**
     * 格式化检查位
     * @param checkBits
     * @return
     */
    private int formatCheckBits(String checkBits){
        switch (checkBits){
            case "Even":return SerialPort.PARITY_EVEN;
            case "Odd":return SerialPort.PARITY_ODD;
            case "Mark":return SerialPort.PARITY_MARK;
            case "Space":return SerialPort.PARITY_SPACE;
            default:return SerialPort.PARITY_NONE;
        }
    }




    /**
     * 获取系统串口列表
     * @return
     */
    public List<String> getSystemPort() {
        List<String> systemPorts = new ArrayList<>();
        //获得系统可用的端口
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();//获得端口的名字
            systemPorts.add(portName);
        }
        Log.cout("系统可用端口列表：" + systemPorts);
        return systemPorts;
    }

    /**
     * 打开串口
     */
    public SerialPort openSerialPort(String serialPortName, int baudRate , String dataBits , String stopBits , String checkBits) throws Exception {
        try {
            //通过端口名称得到端口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(serialPortName);
            //打开端口，（自定义名字，打开超时时间）
            CommPort commPort = portIdentifier.open(serialPortName, 2000);
            //判断是不是串口
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                //设置串口参数（波特率，数据位8，停止位1，校验位无）
                serialPort.setSerialPortParams(baudRate, formatDataBits(dataBits), formatStopBits(stopBits), formatCheckBits(checkBits));
                Log.cout("开启串口成功，串口名称："+serialPortName);
                return serialPort;
            }
            else {
                //是其他类型的端口
                throw new NoSuchPortException();
            }
        } catch (NoSuchPortException e) {
            //e.printStackTrace();
            setLastErrorMessage("no such port");
            throw new Exception("no such port");
        } catch (PortInUseException e) {
            //e.printStackTrace();
            setLastErrorMessage("port in used");
            throw new Exception("port in used");
        } catch (UnsupportedCommOperationException e) {
            //e.printStackTrace();
            setLastErrorMessage("unsupported comm operation");
            throw new Exception("unsupported comm operation");
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort(SerialPort serialPort) {
        if(serialPort != null) {
            serialPort.close();
            Log.cout("关闭了串口："+serialPort.getName());
            serialPort = null;
        }
    }

    /**
     * 发送数据
     */
    public void sendData(SerialPort serialPort, byte[] data) {
        setLastSendData(data);
        OutputStream os = null;
        try {
            os = serialPort.getOutputStream();//获得串口的输出流
            os.write(data);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                    os = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接受数据
     */
    public byte[] readData(SerialPort serialPort) {
        InputStream is = null;
        byte[] bytes = null;
        try {
            is = serialPort.getInputStream();//获得串口的输入流
            int bufflenth = is.available();//获得数据长度
            while (bufflenth != 0) {
                bytes = new byte[bufflenth];//初始化byte数组
                is.read(bytes);
                bufflenth = is.available();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    /**
     * 设置串口监听 此处使用回调最为合理
     */
    public void setListenerToSerialPort(SerialPort serialPort , TextArea textArea , String receiveType , Text debug_receive_bytes) {
        try {
            /**
             * 设置监听
             */
            SerialPortEventListener listener = new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    if(serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {//数据通知
                        byte[] bytes = readData(serialPort);
                        debug_receive_bytes.setText((Integer.valueOf(debug_receive_bytes.getText()) + bytes.length) + "");
                        setLastReadBytes(bytes);
                        if(receiveType.equals("Hex")){
                            setLastReadData(NumberConversion.bytesToHexString(bytes));
                        }else if(receiveType.equals("ASCII")){
                            setLastReadData(new String(bytes));
                        }
                        textArea.setText(textArea.getText() + lastReadData);
                        Log.cout(lastReadData);
                    }
                }
            };
            //给串口添加事件监听
            serialPort.addEventListener(listener);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
        serialPort.notifyOnDataAvailable(true);//串口有数据监听
        serialPort.notifyOnBreakInterrupt(true);//中断事件监听
    }
}
