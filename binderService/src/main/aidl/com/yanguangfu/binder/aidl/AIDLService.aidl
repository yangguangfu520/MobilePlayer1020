package com.yanguangfu.binder.aidl;  
import com.yanguangfu.binder.aidl.AIDLActivity;
interface AIDLService {   
    void registerTestCall(AIDLActivity cb);   
    void invokCallBack();
    
    String getName();
    int getAge();
}  
