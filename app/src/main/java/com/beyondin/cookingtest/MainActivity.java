package com.beyondin.cookingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvStatusText;//当前状态
    private TextView tvStatusTips;//状态提示
    private String stateName="断电状态";
    private String tips;
    CookingContext cookingContext=new CookingContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        tvStatusText = findViewById(R.id.tv_current_status);
        tvStatusTips = findViewById(R.id.tv_current_tips);
        //操作按钮
        Button btnPowerOn = findViewById(R.id.btn_power_on);
        Button btnPowerOff = findViewById(R.id.btn_power_off);
        Button btnCooking = findViewById(R.id.btn_cooking);
        Button btnOpen = findViewById(R.id.btn_open);
        Button btnClose = findViewById(R.id.btn_close);

        btnPowerOn.setOnClickListener(this);
        btnPowerOff.setOnClickListener(this);
        btnCooking.setOnClickListener(this);
        btnOpen.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        tvStatusText.setText("断电状态");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_power_on:
                cookingContext.getState().powerOn();
                tvStatusText.setText(cookingContext.getState().stateName);
                tvStatusTips.setText(cookingContext.getState().tips);
                break;
            case R.id.btn_power_off:
                cookingContext.getState().powerOff();
                tvStatusText.setText(cookingContext.getState().stateName);
                tvStatusTips.setText(cookingContext.getState().tips);
                break;
            case R.id.btn_cooking:
                cookingContext.getState().cooking();
                tvStatusText.setText(cookingContext.getState().stateName);
                tvStatusTips.setText(cookingContext.getState().tips);
                break;
            case R.id.btn_open:
                cookingContext.getState().open();
                tvStatusText.setText(cookingContext.getState().stateName);
                tvStatusTips.setText(cookingContext.getState().tips);
                break;
            case R.id.btn_close:
                cookingContext.getState().close();
                tvStatusText.setText(cookingContext.getState().stateName);
                tvStatusTips.setText(cookingContext.getState().tips);
                break;
        }
    }
}

class CookingContext{
    private BaseState baseState;
    CookingContext(){
        baseState=new PowerOffState(this);
    }
    public void setState(BaseState state) {
        this.baseState = state;
    }
    public BaseState getState() {
        return baseState;
    }
    public void setInfo(String info){
        baseState.setTips(info);
    }
}

abstract class BaseState{
    public String stateName;
    public  String tips;
    public CookingContext cookingContext;

    public abstract void open();
    public abstract void close();
    public abstract void cooking();
    public abstract void powerOn();
    public abstract void powerOff();

    public void setTips(String info){
        tips=info;
    }
}

class OpenState extends BaseState{
    public OpenState(BaseState baseState){
        cookingContext=baseState.cookingContext;
        stateName="开盖状态";
        tips=baseState.tips;
    }

    public void open(){
        cookingContext.setInfo("当前已是开盖状态");

    }

    public void close(){
        cookingContext.setState(new CloseState(this));
    }

    public void cooking(){
        cookingContext.setInfo("当前状态为开盖状态，不能煮饭");
    }

    public void powerOn(){
        cookingContext.setInfo("当前已是接电状态");
    }

    public void powerOff(){
        cookingContext.setState(new PowerOffState(this));
    }

}
class CloseState extends BaseState{
    public CloseState(BaseState baseState){
        stateName="关盖状态";
        cookingContext=baseState.cookingContext;
    }
    public void open(){
        cookingContext.setState(new OpenState(this));
    }
    public void close(){
        cookingContext.setInfo("当前已是关盖状态");
    }
    public void cooking(){
        cookingContext.setState(new CookingState(this));
    }
    public void powerOn(){
        cookingContext.setState(new PowerOnState(this));
    }
    public void powerOff(){
        cookingContext.setState(new PowerOffState(this));
    }
}
class CookingState extends BaseState{
    public CookingState(BaseState baseState){
        stateName="煮饭状态";
        cookingContext=baseState.cookingContext;
    }
    public void open(){
        cookingContext.setInfo("煮饭状态，不能开盖");
    }

    public void close(){
        tips="当前已是关盖状态";
    }

    public void cooking(){
        tips="当前已是煮饭状态";
    }

    public void powerOn(){
        tips="当前已是接电状态";
    }

    public void powerOff(){
        cookingContext.setState(new PowerOffState(this));
    }

}
class PowerOnState extends BaseState{
    public PowerOnState(BaseState baseState){
        stateName="接电状态";
        cookingContext=baseState.cookingContext;
    }

    public void open(){
        cookingContext.setState(new OpenState(this));
    }

    public void close(){
        cookingContext.setState(new CookingState(this));
    }

    public void cooking(){
        cookingContext.setState(new CookingState(this));
    }

    public void powerOn(){
        tips="当前已是接电状态";
    }

    public void powerOff(){
        cookingContext.setState(new PowerOffState(this));
    }
}
class PowerOffState extends BaseState{
    public PowerOffState(CookingContext cc){
        cookingContext=cc;
        stateName="断电状态";
        tips="";
    }
    public PowerOffState(BaseState baseState){
        stateName="断电状态";
        cookingContext=baseState.cookingContext;
    }

    public void open(){
        tips="断电状态，不能开盖";
    }

    public void close(){
        tips="当前已是关盖状态";
    }

    public void cooking(){
        tips="断电状态，不能煮饭";
    }

    public void powerOn(){
        cookingContext.setState(new PowerOnState(this));
    }

    public void powerOff(){
        tips="当前已是断电状态";
    }
}