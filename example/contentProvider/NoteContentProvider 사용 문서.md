## Authority
- "com.example.content-provider"

## Table 정보
### 1. java_notes
- content uri: content://com.example.content-provider/java_notes
- 칼럼: '_id' (INTEGER, Primary Key), title(TEXT, NOT NULL), content(TEXT, NOT NULL)
### 2. kotlin_notes
- content uri: content://com.example.content-provider/kotlin_notes
- 칼럼: '_id' (INTEGER, Primary Key), title(TEXT, NOT NULL), content(TEXT, NOT NULL)

## 사용 예제
- URI 생성
```
public static final String AUTHORITY="com.example.content-provider";
public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+AUTHORITY);

public static final String TABLE_JAVA="java_notes";
public static final String TABLE_KOTLIN="kotlin_notes";

public static final Uri JAVA_CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,TABLE_JAVA);
public static final Uri KOTLIN_CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,TABLE_KOTLIN);
```

- query(읽기만 허용)
```
//editText가 j이면 java_notes,
//k이면 kotlin_notes의 uri
Uri uri=Uri.EMPTY;
if(table.equals("j")){
    uri=JAVA_CONTENT_URI;
}else if(table.equals("k")){
    uri=KOTLIN_CONTENT_URI;
}

String selection = BaseColumns._ID+"=?";
String [] selectionArgs=new String[]{idx};
Cursor cursor;

//인덱스 값을 0으로 하면 전체 테이블 내용
//인덱스 값이 0이 아니면 특정 행의 값만 가져옴
if(idx.equals("0")){
    cursor=getContentResolver().query(uri,null,null, null,null);
}else{
    cursor=getContentResolver().query(uri,null,selection, selectionArgs,null);
}
```
