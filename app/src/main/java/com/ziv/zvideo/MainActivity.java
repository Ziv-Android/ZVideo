package com.ziv.zvideo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText fileName;
    private RadioGroup playRadio;
    private Button playBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
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
}
