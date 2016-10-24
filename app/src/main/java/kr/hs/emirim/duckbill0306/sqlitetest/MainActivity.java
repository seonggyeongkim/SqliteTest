package kr.hs.emirim.duckbill0306.sqlitetest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editName, editCount,editNameResult,editCountResult;
    Button butInit, butInput,butSelect;
    MyDBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper=new MyDBHelper(getApplicationContext()); //호출 되자마자 db생성
        setContentView(R.layout.activity_main);
        editName=(EditText)findViewById(R.id.edit_groupname);
        editCount=(EditText)findViewById(R.id.edit_groupcount);
        editNameResult=(EditText)findViewById(R.id.edit_name_result);
        editCountResult=(EditText)findViewById(R.id.edit_count_result);
        butInit=(Button)findViewById(R.id.but_init);
        butInput=(Button)findViewById(R.id.but_input);
        butSelect=(Button)findViewById(R.id.but_select);

        butInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=dbHelper.getReadableDatabase(); //데이블 초기화 (insert!)
                dbHelper.onUpgrade(db,1,2);//초기화를 하면 버전이 바뀜!
                db.close();//테이블 삭제
            }
        });

        butInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=dbHelper.getWritableDatabase(); //내용 변경!
                db.execSQL("insert into idoltable values('"+editName.getText().toString()+"' , "
                        +editCount.getText().toString()+");");//내용 변경 될 때 사용! (이름,카운트 변경)
                db.close();
                Toast.makeText(getApplicationContext(),"정상적으로 입력 완료!",Toast.LENGTH_SHORT).show();//정상적으로 테이블이 생성됨을 알려줌!
            }
        });

        butSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=dbHelper.getReadableDatabase();
                Cursor rs=db.rawQuery("select * from idoltable",null);//resultset과 같은 역할!
                String gname="그룹 이름"+"\n"+"=============="+"\n";
                String gcount="안원수"+"\n"+"=============="+"\n";
                while (rs.moveToNext()){
                    gname+=rs.getString(0)+"\n";//첫번째 컬럼 '0'
                    gcount+=rs.getInt(1)+"\n";
                }
                editNameResult.setText(gname);
                editCountResult.setText(gcount);

                rs.close();
                db.close();
            }
        });
    }

    public class MyDBHelper extends SQLiteOpenHelper{ //테이블 생성!
        public MyDBHelper(Context context){
            super(context,"idoldb",null,1);//데이터 베이스 이름 , null , 버전
        }

        //DB 생성을 위해 onCreate 와 onUpgrage 필요!
        @Override
        public void onCreate(SQLiteDatabase db) { //super로 선언된 것을 매개 변수로 받는다!
            db.execSQL("create table idoltable(gname char(40) primary key, gcount integer);");//idoltable 생성

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//기존의 테이블이 존재하면, 기존 삭제! 새로운 테이블 생성
            db.execSQL("drop table if exist idoltable");//idoltable이 존재하면 삭제 하겠다!
            onCreate(db);//삭제 후 다시 생성

        }
    }
}
