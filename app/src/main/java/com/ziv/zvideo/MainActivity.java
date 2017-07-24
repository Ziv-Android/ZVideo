package com.ziv.zvideo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView sdcardInfo;
    private EditText fileName;
    private RadioGroup playRadio;
    private Button playBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        getSDCardInfo();
    }

    private void initView() {
        sdcardInfo = (TextView) findViewById(R.id.sdcard_info_text);
        fileName = (EditText) findViewById(R.id.file_name_edit);
        playRadio = (RadioGroup) findViewById(R.id.player_radio_group);
        playBtn = (Button) findViewById(R.id.player_button);
        playBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_button:
                onPlayButtonClick();
                break;
        }
    }

    private void onPlayButtonClick() {
        Intent intent = null;
        // 获取单选按钮ID
        int radioButtonId = playRadio.getCheckedRadioButtonId();
        switch (radioButtonId) {
            case R.id.bitmap_player_radio:
                intent = new Intent(this, BitmapPlayerActivity.class);
                break;
            default:
                throw new UnsupportedOperationException("radioID=" + radioButtonId);
        }
        // 获取存储在外部的文件
        String fileNameText = fileName.getText().toString().trim();
        if (!"".equals(fileNameText)) {
            File file = new File(Environment.getExternalStorageDirectory(), fileNameText);
            intent.putExtra(AbstractPlayerActivity.EXTRA_FILE_NAME, file.getAbsolutePath());
            // 启动PlayerActivity
            startActivity(intent);
        }
    }

    private void getSDCardInfo(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = 0;
            long totalBlock = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSizeLong();
                totalBlock = stat.getBlockCountLong();
            }else {
                blockSize = stat.getBlockSize();
                totalBlock = stat.getBlockCount();
            }
            sdcardInfo.setText("SDCard size = " + formant(totalBlock * blockSize));
        }
    }

    private String formant(long size){
        return Formatter.formatFileSize(this, size);
    }
}
