package sample;

import gnu.io.SerialPort;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import sample.utils.Log;
import sample.utils.MySerialPort;
import sample.utils.NumberConversion;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Project: 主界面
 * Author : zhaozhen
 * Email  : 2399144252@qq.com
 * Date   : 2019/6/2
 */
public class ContentController {

    private String currentCom = "COM1";
    private String currentBaudRate = "9600";
    private String currentDataBits = "8";
    private String currentCheckBits = "None";
    private String currentStopBits = "1";
    private String currentStreamController = "none";

    private String currentReceiveType = "ASCII";
    private Boolean autoEnter = false;
    private Boolean showSendData = false;
    private Boolean showTime = false;

    private String currentSendType = "ASCII";
    private Boolean foreachSend = false;
    private String foreachTime = null;

    private String sendInfo = null;
    private String receiveInfo = null;

    //串口工具类实例
    private MySerialPort mySerialPort;
    //串口对象
    private SerialPort serialPort;


    //当前串口状态
    private Boolean currentComStatus = false;



    @FXML private BorderPane rootLayout;

    @FXML private Button btn_starting;

    @FXML private Button btn_running;

    @FXML private Button btn_stopping;

    @FXML private Button btn_clear;

    @FXML private ComboBox attr_com;

    @FXML private ComboBox attr_data_bit;

    @FXML private ComboBox attr_baud_rate;

    @FXML private ComboBox attr_check_bit;

    @FXML private ComboBox attr_stop_bit;

    @FXML private ComboBox attr_stream_control;

    @FXML private ToggleGroup receive_type;

    @FXML private ToggleGroup send_type;

    @FXML private CheckBox set_receive_auto_enter;

    @FXML private CheckBox set_receive_show_send_info;

    @FXML private CheckBox set_receive_show_time;

    @FXML private CheckBox set_foreach_send;

    @FXML private TextField set_foreach_send_time;

    @FXML private Text debug_info;

    @FXML private Text debug_receive_bytes;

    @FXML private Text debug_send_bytes;

    @FXML private TextArea debug_receive_data;

    @FXML private TextArea debug_send_data;

    @FXML private Button btn_debug_send;



    public void init(){
        mySerialPort = new MySerialPort();

        //初始化串口列表
        List<String> comList = mySerialPort.getSystemPort();
        if(!comList.isEmpty()){
            attr_com.getItems().clear();
            attr_com.getItems().addAll(comList);
            attr_com.setValue(comList.get(0));
            attr_com.setVisibleRowCount(5);
        }

        //监听串口
        attr_com.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                currentCom = newValue.toString();
                Log.cout(currentCom);

            }
        });
        //监听数据位
        attr_data_bit.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
               currentDataBits = newValue.toString();
               Log.cout(currentDataBits);
            }
        });
        //监听波特率
        attr_baud_rate.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                currentBaudRate = newValue.toString();
                Log.cout(currentBaudRate);
            }
        });
        //监听检查位
        attr_check_bit.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                currentCheckBits = newValue.toString();
                Log.cout(currentCheckBits);
            }
        });
        //停止位
        attr_stop_bit.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                currentStopBits = newValue.toString();
                Log.cout(currentStopBits);
            }
        });
        //流控
        attr_stream_control.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                currentStreamController = newValue.toString();
                Log.cout(currentStreamController);
            }
        });

        //接受数据类型
        receive_type.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                currentReceiveType = newValue.getUserData().toString();
                Log.cout(currentReceiveType);
            }
        });

        //发送数据类型
        send_type.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                currentSendType = newValue.getUserData().toString();
                Log.cout(currentSendType);
            }
        });

        //监听自动换行
        set_receive_auto_enter.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                autoEnter = newValue;
                Log.cout(autoEnter);
            }
        });

        //监听显示发送
        set_receive_show_send_info.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                showSendData = newValue;
                Log.cout(showSendData);
            }
        });
        //监听显示时间
        set_receive_show_time.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                showTime = newValue;
                Log.cout(showSendData);
            }
        });
        //监听重复发送
        set_foreach_send.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                foreachSend = newValue;
                Log.cout(foreachSend);
            }
        });
        //监听重复时间
        set_foreach_send_time.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                foreachTime = newValue;
                if(!isInteger(foreachTime) || foreachTime.length() > 6 || foreachTime.equals("0")){
                    Platform.runLater(() -> { //延迟修改
                        set_foreach_send_time.setText("1000");
                        foreachTime = "1000";
                    });
                }
            }
        });
        //显示时间
        set_receive_show_time.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                showTime = newValue;
                Log.cout(showTime);
            }
        });

        //监听串口开关
        btn_starting.pressedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(currentComStatus == false){
                    currentComStatus = true;
                    btn_debug_send.setText("发送");
                    btn_running.setStyle("-fx-background-color: #19be6b");
                    openSerialPort();
                }
            }
        });
        btn_stopping.pressedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(currentComStatus == true){
                    currentComStatus = false;
                    btn_debug_send.setText("打开");
                    btn_running.setStyle("");
                    closeSerialPort();
                }
            }
        });

        btn_clear.pressedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                debug_receive_data.setText("");
            }
        });


        //调试信息显示
        debug_info.setText("显示调试信息");

        //接受数据
        debug_receive_bytes.setText("接收: 19528Bytes");
        //显示数据
        debug_send_bytes.setText("发送: 12354Bytes");
        //接受面板
        //debug_receive_data.setText("EF 00 01 65 E8");
    }

    @FXML protected void start(){

    }

    /**
     * 发送数据
     */
    @FXML protected void send(){
        //判断串口是否打开
        if(currentComStatus == false){
            currentComStatus = true;
            btn_debug_send.setText("发送");
            btn_running.setStyle("-fx-background-color: #19be6b");
            openSerialPort();
        }else{
            sendData();
        }
    }

    /**
     * 打开串口
     */
    protected void openSerialPort(){

        try {
            serialPort = mySerialPort.openSerialPort(currentCom,Integer.valueOf(currentBaudRate),currentDataBits,currentStopBits,currentCheckBits);
            /**
             * 设置数据监听
             */
            mySerialPort.setListenerToSerialPort(serialPort , debug_receive_data , currentReceiveType);
            debug_info.setText(currentCom  + " " + "is opened");
        } catch (Exception e) {
            debug_info.setText(currentCom  + " " + e.getMessage());

            //按钮状态关闭
            currentComStatus = false;
            btn_debug_send.setText("打开");
            btn_running.setStyle("");
        }
    }
    /**
     * 关闭串口
     */
    protected void closeSerialPort(){
        mySerialPort.closeSerialPort(serialPort);
        debug_info.setText(currentCom  + " " + "is closed");
    }

    /**
     * 发送数据
     */
    protected void sendData(){
        if(!debug_send_data.equals(null)){
            sendInfo = debug_send_data.getText();
            if(currentSendType.equals("Hex")){
                mySerialPort.sendData(serialPort , NumberConversion.hex2byte(sendInfo));
            }else{
                mySerialPort.sendData(serialPort , sendInfo.getBytes());
            }
        }
    }

    /**
     * 接收数据
     */
//    protected void receiveData(){
//
//    }

    /**
     * 正则数字的判断
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[1-9][-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
