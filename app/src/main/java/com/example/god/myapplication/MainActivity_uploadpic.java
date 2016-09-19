package com.example.god.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.imagedemo.ItemEntity;
import com.example.imagedemo.ListItemAdapter;
import com.example.oracle.pic_info;
import com.example.oracle.pic_info_name;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.arlen.photo.R;
;


/**
 * 照片墙主活动，使用GridView展示照片墙。
 *
 * @author guolin
 */
public class MainActivity_uploadpic extends Activity {

    private ExecutorService executorService;
    /**
     * 用于展示照片墙的GridView
     */
    private GridView mPhotoWall;

    /**
     * GridView的适配器
     */
    private PhotoWallAdapter mAdapter;

    private int mImageThumbSize;
    private int mImageThumbSpacing;


    /** Item数据实体集合 */
    private ArrayList<ItemEntity> itemEntities;
    private ArrayList<ItemEntity> pic_itemEntities;
    /** ListView对象 */
    private ListView listview;

    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 3333) {
                //只要在主线程就可以处理ui
                ((ImageView) MainActivity_uploadpic.this.findViewById(msg.arg1)).setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    private void test()
    {

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                pic_info_name.getImageromSdk("3");

                final String [] test_name = new String[pic_info_name.getList_result().size()];

                for (int i = 0 ;i < pic_info_name.getList_result().size(); i++)
                {
                    test_name[i] = pic_info_name.getList_result().get(i);
                }

                final String [][] test_name_picinfo = new String[pic_info_name.getList_result().size()][];

                for (int i = 0 ;i < pic_info_name.getList_result().size(); i++)
                {
                    pic_info.getImageromSdk("2",test_name[i]);

                    List<String> picinfo = new ArrayList<String>();

                    test_name_picinfo[i] = new String[pic_info.getList_result().size()];

                    for (int j=0;j<pic_info.getList_result().size();j++)
                    {
                        test_name_picinfo[i][j] = pic_info.getList_result().get(j);
                    }
                }

                final int ww = pic_info_name.getList_result().size();

                final int www = pic_info_name.getList_result().size();

                try {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listview = (ListView) findViewById(R.id.listview);

                            itemEntities = new ArrayList<ItemEntity>();

                            ItemEntity [] entity_oracle = new ItemEntity[ww];

                            for (int i =0;i<ww;i++)
                            {
                                ArrayList<String> urls_oracle = new ArrayList<String>();
                                for (int j=0;j<test_name_picinfo[i].length;j++)
                                {
                                    urls_oracle.add(test_name_picinfo[i][j]);
                                }
                                entity_oracle[i] = new ItemEntity(test_name_picinfo[i][0],test_name[i],null,urls_oracle);

                                itemEntities.add(entity_oracle[i]);

                            }
                            listview.setAdapter(new ListItemAdapter(MainActivity_uploadpic.this, itemEntities));
                        }
                    });
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }




            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_uploadpic);

        executorService = Executors.newFixedThreadPool(5);

        test();

        mImageThumbSize = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);
        mPhotoWall = (GridView) findViewById(R.id.photo_wall);
        mAdapter = new PhotoWallAdapter(this, 0, Images.imageThumbUrls,
                mPhotoWall);
        mPhotoWall.setAdapter(mAdapter);
        mPhotoWall.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        final int numColumns = (int) Math.floor(mPhotoWall
                                .getWidth()
                                / (mImageThumbSize + mImageThumbSpacing));
                        if (numColumns > 0) {
                            int columnWidth = (mPhotoWall.getWidth() / numColumns)
                                    - mImageThumbSpacing;
                            mAdapter.setItemHeight(columnWidth);
                            mPhotoWall.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                    }
                });
    }



    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.fluchCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时结束所有的下载任务
        mAdapter.cancelAllTasks();
    }

}