package com.arlen.photo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlen.photo.ImageCachceUitl_package.ImageCachceUitl;
import com.arlen.photo.upload.Data_up;
import com.arlen.photo.xianlu.xianlu_oracle;
import com.bumptech.glide.Glide;
import com.example.imagedemo.ItemEntity;
import com.example.imagedemo.ListItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by God on 2016/8/18.
 */
public class xianlu_main_activity extends Activity {

    private ExecutorService executorService;
    private TextView txt_xianlu_home;
    private TextView txt_xianlu_back;
    private String xianluname_str;
    private String xianlunum_str;
    private String chehao;
    private String chexiang;
    private AlertDialog selfdialog;
    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 2012) {
                //只要在主线程就可以处理ui
                ((ImageView) xianlu_main_activity.this.findViewById(msg.arg1)).setImageBitmap((Bitmap) msg.obj);
            }
        }
    };
    private int MIN_MARK = 1;
    private int MAX_MARK = 6;
    private ListView listview;
    static ArrayList<ItemEntity> itemEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xianlu_main);

        listview = (ListView) findViewById(R.id.xianlu_main_xianlu_listview);

        executorService = Executors.newFixedThreadPool(5);
        listview_download();
        txt_xianlu_home = (TextView) findViewById(R.id.txt_xianlu_home);
        txt_xianlu_back = (TextView) findViewById(R.id.txt_xianlu_back);
        txt_xianlu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_xianlu_home.setSelected(false);
                txt_xianlu_home.setSelected(true);
                finish();
            }
        });
        txt_xianlu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_xianlu_back.setSelected(false);
                txt_xianlu_back.setSelected(true);
                finish();
            }
        });
    }

    private void listview_download() {
        executorService.submit(new Runnable() {

            @Override
            public void run() {
                xianlu_oracle.getImageromSdk();
                try {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            itemEntities = new ArrayList<ItemEntity>();

                            final String [] xianlu_name = new String[xianlu_oracle.getList_result().size()];

                            final String [] xianlu_num = new String[xianlu_oracle.getList_result().size()];

                            final ItemEntity [] entity_oracle = new ItemEntity[xianlu_oracle.getList_result().size()];

                            for (int i=0;i<xianlu_name.length;i++)
                            {
                                xianlu_name[i] = xianlu_oracle.getList_result().get(i);

                                xianlu_num[i] = xianlu_oracle.getList_result().get(i);

                                entity_oracle[i] = new ItemEntity(Data_up.getSERVICE_URL_IP_PORT_webnnn()+xianlu_name[i]+".jpg","名称："+xianlu_name[i],"数量："+xianlu_num[i],null);

                                itemEntities.add(entity_oracle[i]);
                            }
                            listview.setAdapter(new ListItemAdapter(xianlu_main_activity.this, itemEntities));
                            // 1.无图片
//                ItemEntity entity1 = new ItemEntity(//
//                        "http://img.my.csdn.net/uploads/201410/19/1413698871_3655.jpg", "张三", "今天天气不错...", null);
//                itemEntities.add(entity1);
//
//                ItemEntity entity2 = new ItemEntity(//
//                        "http://img.my.csdn.net/uploads/201410/19/1413698865_3560.jpg", "李四", "今天雾霾呢...", null);
//                itemEntities.add(entity2);
//
//                ItemEntity entity3 = new ItemEntity(//
//                        "http://img.my.csdn.net/uploads/201410/19/1413698837_5654.jpg", "王五", "今天好大的太阳...", null);
//                itemEntities.add(entity3);
//
//                ItemEntity entity4 = new ItemEntity(//
//                        "http://img.my.csdn.net/uploads/201410/19/1413698883_5877.jpg", "赵六", "今天下雨了...", null);
//                itemEntities.add(entity4);
                            //                            listView.setAdapter(new myListAdapt());
//                         listview 点击事件
                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                                    view = inflater.inflate(R.layout.xianlupopup, null);

                                    final TextView pop_chehao = (EditText) view.findViewById(R.id.pop_chehao);
                                    final TextView pop_chexiang = (EditText) view.findViewById(R.id.pop_chexiang);

                                    AlertDialog.Builder ad = new AlertDialog.Builder(xianlu_main_activity.this);
                                    ad.setView(view);

                                    pop_chexiang.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
                                            System.out.println("-1-beforeTextChanged-->"
                                                    + pop_chexiang.getText().toString() + "<--");
                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
                                            System.out.println("-1-onTextChanged-->"
                                                    + pop_chexiang.getText().toString() + "<--");

                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            if (s != null && !s.equals("")) {
                                                if (MIN_MARK != -1 && MAX_MARK != -1) {
                                                    int markVal = 0;
                                                    try {
                                                        markVal = Integer.parseInt(s.toString());
                                                    } catch (NumberFormatException e) {
                                                        markVal = 0;
                                                    }
                                                    if (markVal > MAX_MARK) {
                                                        Toast.makeText(getBaseContext(), "最大值为6", Toast.LENGTH_SHORT).show();
                                                        pop_chexiang.setText(String.valueOf(MAX_MARK));
                                                    }
                                                    if (s.length() > 0) {
                                                        if (MIN_MARK != -1 && MAX_MARK != -1) {
                                                            int num = Integer.parseInt(s.toString());
                                                            if (num > MAX_MARK) {

                                                                pop_chexiang.setText(String.valueOf(MAX_MARK));
                                                            } else if (num < MIN_MARK)
                                                                pop_chexiang.setText(String.valueOf(MIN_MARK));
                                                            return;
                                                        }
                                                    }
                                                    return;
                                                }
                                            }
                                        }
                                    });

                                    xianluname_str = xianlu_name[position];
                                    ad.setTitle("待检查线路："+xianluname_str);
                                    selfdialog = ad.create();
                                    selfdialog.setButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            chehao = pop_chehao.getText().toString();
                                            chexiang = pop_chexiang.getText().toString();
                                            if (chehao.equals("") || chexiang.equals("")) {
                                                showDialog();
                                            } else {
                                                Intent intent = new Intent(xianlu_main_activity.this, crm_main_activity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("zaizhuangxianlu", xianluname_str);
                                                bundle.putString("zaizhuangxianlu_num", xianlunum_str);
                                                bundle.putString("chehao", chehao + "0" + chexiang);
                                                bundle.putString("chexiang", chexiang);
                                                if (chexiang.equals("1") || chexiang.equals("6")) {
                                                    bundle.putString("chexing", "tc");
                                                } else if (chexiang.equals("2") || chexiang.equals("5")) {
                                                    bundle.putString("chexing", "mp");
                                                } else if (chexiang.equals("3") || chexiang.equals("4")) {
                                                    bundle.putString("chexing", "m");
                                                }
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                dialog.cancel();
                                            }
                                        }
                                    });
                                    selfdialog.setButton2("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            selfdialog.cancel();
                                        }
                                    });
                                    selfdialog.show();
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void showDialog() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(xianlu_main_activity.this);
        builder.setTitle("错误").setIcon(android.R.drawable.stat_notify_error);
        builder.setMessage("请输入车号及车厢！！！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });
        dialog = builder.create();
        dialog.show();
    }
}
