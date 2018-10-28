package tw.dora.dbtest;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private Resources res;
    private MyHelper myHelper;
    private SQLiteDatabase db;
    private TextView data;

    //SQLite可用免費軟體修改
    //https://sqlitebrowser.org/
    //相關語法可參照SQLite官方網站
    //https://www.sqlite.org/lang.html

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = findViewById(R.id.data);

        //放在Resources(res資料夾)底下的檔案是可讀不可寫(唯讀)
        res = getResources();

        myHelper = new MyHelper(this,"brad",null,1);
        db = myHelper.getReadableDatabase();


    }



    public void test1(View view) {

        try {
            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(res.openRawResource(R.raw.brad))
                    );
            String line;
            while((line = reader.readLine())!=null) {
                Log.v("brad",line);
            }
            reader.close();
        } catch (IOException e) {
            Log.v("brad",e.toString());
        }
    }

    public void test2(View view) {
        Cursor cursor = db.query("cust",
                new String[]{"id","tel","cname"}
                ,"id > ?",new String[]{"4"}
                ,null,null,"cname");
        int count = cursor.getCount();
        Log.v("brad","Count: "+count);

        data.setText("");

        //SQLite儲存資料皆為String類型,且index從0開始起算
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("cname"));
            String tel = cursor.getString(cursor.getColumnIndex("tel"));
            //String birthday = cursor.getString(cursor.getColumnIndex("birthday"));

            data.append(id+":"+name+":"+tel+":"+"\n");
            Log.v("brad",id+":"+name+":"+tel+":");

        }
    }

    public void test3(View view) {
        ContentValues values = new ContentValues();

        //id為prime key必須為一對一,不然無法新增資料
        //values.put("id","1");

        values.put("cname","brad");
        values.put("tel","123");
        values.put("birthday","1999-01-02");

        db.insert("cust",null,values);
        test2(null);
    }

    public void test4(View view) {
        //delete from cust where id = 3
        db.delete("cust","id = ? and cname = ?",new String[]{"6","brad"});
        test2(null);
    }

    public void test5(View view) {
        //update cust set cname ='tony,birthday='1999-01-04' where id =7
        ContentValues values = new ContentValues();
        values.put("cname","tony");
        values.put("birthday","1999-01-04");

        db.update("cust",values,
                "id = ?",new String[]{"7"});
        test2(null);
    }
}
