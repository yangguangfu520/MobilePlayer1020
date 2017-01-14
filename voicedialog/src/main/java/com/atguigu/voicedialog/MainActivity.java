package com.atguigu.voicedialog;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class MainActivity extends Activity implements View.OnClickListener {

    private SpeechUtils mSpeechUtils;
    private List<ConversationInfo> conversationList;
    private MyAdapter mAdapter;
    private ListView mListView;
    private Button btnStartListen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mSpeechUtils = SpeechUtils.getInstance(this);
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.lv_conversation);
        btnStartListen = (Button) findViewById(R.id.btn_start_listen);
        btnStartListen.setOnClickListener(this);
        conversationList = new ArrayList<>();
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {

        mSpeechUtils.showListenVoiceDialog(this, new MyRecognizerDialogListener());
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener{


        StringBuffer sb = new StringBuffer();

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean isLast) {

            //语音识别过来的内容
            String resultString = recognizerResult.getResultString();
            sb.append(getVoice(resultString));//添加成一整句

            System.out.println("正在识别中: " + sb.toString());
            String askerText = "你说什么，我没听见...";
            String answer;
            int imageID = -1;
            if (isLast) { // true, 所有的数据都解析完闭. 赋值给askerText
                askerText = sb.toString();
                answer = sb.toString();
                if (askerText.contains("美女")) {
                    Random random = new Random();
                    int index = random.nextInt(3);
                    answer = ResouesUtils.mmTextArray[index];
                    imageID = ResouesUtils.mmImageArray[index];
                } else if (askerText.contains("精忠报国")) {
                    answer = ResouesUtils.markTextArray[0];
                    imageID = ResouesUtils.markImageArray[0];
                }else if (askerText.contains("奶茶妹")) {
                    answer = ResouesUtils.markTextArray[2];
                    imageID = ResouesUtils.mmImageArray[4];
                }else if (askerText.contains("开会")) {
                    answer = ResouesUtils.markTextArray[1];
                    imageID = ResouesUtils.markImageArray[1];
                } else if (askerText.contains("报警")) {
                    String phoneNumber = "110";
                    Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                    startActivity(intentPhone);
                    return;
                }else if(askerText.contains("120")) {
                    String phoneNumber = "120";
                    Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                    startActivity(intentPhone);
                    return;
                } else if (askerText.contains("打开微信")) {
                    Intent intent = new Intent();
                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);
                    return;
                } else if (askerText.contains("拍照")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
//                   photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    startActivity(intent);
                    return;
                }else if(askerText.contains("名字")) {
                    answer = ResouesUtils.mmTextArray[3];
                }else if(askerText.contains("我在哪儿")) {
                    answer = ResouesUtils.mmTextArray[4];
                }else if(askerText.contains("班长是谁")) {
                    answer = ResouesUtils.mmTextArray[5];
                }else if(askerText.contains("老师是谁")) {
                    answer = ResouesUtils.mmTextArray[6];
                    imageID = ResouesUtils.mmImageArray[3];
                }else if(askerText.contains("你会做什么")) {
                    answer = ResouesUtils.mmTextArray[7];
                }else if(askerText.contains("爱你")) {
                    answer = ResouesUtils.mmTextArray[8];
                }
                sb = new StringBuffer();
            } else { // 还没有把数据解析完毕, return回去, 不去执行后面的代码, 继续下一次拼接.
                return;
            }

            ConversationInfo info = new ConversationInfo(askerText, null, -1, true);
            conversationList.add(info);
            mAdapter.notifyDataSetChanged();

            // 准备回答的数据.
            info = new ConversationInfo(null, answer, imageID, false);
            conversationList.add(info);

            mAdapter.notifyDataSetChanged();
            mListView.setSelection(conversationList.size());
            // 把answer说出来
            mSpeechUtils.speakText(MainActivity.this, answer);
        }

        @Override
        public void onError(SpeechError speechError) {
            Toast.makeText(MainActivity.this, "识别出错了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析json
     * @param resultString
     * @return
     */
    private String getVoice(String resultString) {
        Gson gson = new Gson();
        SpeechBean voice = gson.fromJson(resultString, SpeechBean.class);
        List<SpeechBean.WS> ws = voice.ws;
        StringBuffer sb = new StringBuffer();
        for (SpeechBean.WS wsBean : ws) {
            String str = wsBean.cw.get(0).w;
            sb.append(str);
        }
        return sb.toString();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return conversationList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if(convertView == null) {
                view = View.inflate(MainActivity.this, R.layout.listview_item, null);
            } else {
                view = convertView;
            }

            View answerView = view.findViewById(R.id.ll_answer);
            TextView answerText = (TextView) view.findViewById(R.id.tv_answer_text);
            ImageView answerImage = (ImageView) view.findViewById(R.id.iv_answer_image);
            TextView askerText = (TextView) view.findViewById(R.id.tv_asker_text);

            ConversationInfo info = conversationList.get(position);
            if(info.isAsker()) {
                // 当前是提问者, 隐藏回答者布局
                answerView.setVisibility(View.GONE);
                askerText.setVisibility(View.VISIBLE);
                askerText.setText(info.getAskerText());
            } else {
                // 当前是回答者, 隐藏提问者布局
                answerView.setVisibility(View.VISIBLE);
                askerText.setVisibility(View.GONE);

                answerText.setText(info.getAnswerText());
                if(info.getImageID() == -1) {
                    answerImage.setVisibility(View.GONE);
                } else {
                    answerImage.setVisibility(View.VISIBLE);
                    answerImage.setImageResource(info.getImageID());
                }
            }
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }

}
