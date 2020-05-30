package com.va181.ariawan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION =1;
    private final static String DATABASE_NAME = "db_bukuku";
    private final static String TABLE_BUKU = "t_buku";
    private final static String KEY_ID_BUKU = "ID_Buku";
    private final static String KEY_JUDUL = "Judul";
    private final static String KEY_TGL = "Tanggal";
    private final static String KEY_GAMBAR = "Gambar";
    private final static String KEY_PENERBIT = "Penerbit";
    private final static String KEY_PENULIS = "Penulis";
    private final static String KEY_ISI_BUKU = "Isi_Buku";
    private final static String KEY_LINK = "Link";
    private SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yy ",Locale.getDefault());
    private Context context;

    public DatabaseHandler(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BUKU ="CREATE TABLE " + TABLE_BUKU
                + "(" + KEY_ID_BUKU + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUL + " TEXT, " + KEY_TGL + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_PENERBIT + " TEXT, "
                + KEY_PENULIS + " TEXT, " + KEY_ISI_BUKU + " TEXT, "
                + KEY_LINK + " TEXT);";

        db.execSQL(CREATE_TABLE_BUKU);
        inisiasiBukuAwal(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_BUKU;
        onCreate(db);
    }

    public void tambahBuku (Buku dataBuku) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv =new ContentValues();

        cv.put(KEY_JUDUL, dataBuku.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBuku.getTanggal()));
        cv.put(KEY_GAMBAR, dataBuku.getGambar());
        cv.put(KEY_PENERBIT, dataBuku.getPenerbit());
        cv.put(KEY_PENULIS, dataBuku.getPenulis());
        cv.put(KEY_ISI_BUKU, dataBuku.getIsiBuku());
        cv.put(KEY_LINK, dataBuku.getLink());

        db.insert(TABLE_BUKU, null, cv);
        db.close();

    }
    public void tambahBuku (Buku dataBuku, SQLiteDatabase db) {
        ContentValues cv =new ContentValues();

        cv.put(KEY_JUDUL, dataBuku.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBuku.getTanggal()));
        cv.put(KEY_GAMBAR, dataBuku.getGambar());
        cv.put(KEY_PENERBIT, dataBuku.getPenerbit());
        cv.put(KEY_PENULIS, dataBuku.getPenulis());
        cv.put(KEY_ISI_BUKU, dataBuku.getIsiBuku());
        cv.put(KEY_LINK, dataBuku.getLink());

        db.insert(TABLE_BUKU, null, cv);

    }

    public void editBuku (Buku dataBuku) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv =new ContentValues();

        cv.put(KEY_JUDUL, dataBuku.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBuku.getTanggal()));
        cv.put(KEY_GAMBAR, dataBuku.getGambar());
        cv.put(KEY_PENERBIT, dataBuku.getPenerbit());
        cv.put(KEY_PENULIS, dataBuku.getPenulis());
        cv.put(KEY_ISI_BUKU, dataBuku.getIsiBuku());
        cv.put(KEY_LINK, dataBuku.getLink());

        db.update(TABLE_BUKU, cv, KEY_ID_BUKU + "=?" , new String[]{String.valueOf(dataBuku.getIdBuku())});
        db.close();
}

public void hapusBuku (int idBuku){
    SQLiteDatabase db = getWritableDatabase();
    db.delete(TABLE_BUKU, KEY_ID_BUKU + "=?" , new String[]{String.valueOf(idBuku)});
    db.close();
    }

    public ArrayList<Buku> getAllBuku() {
        ArrayList<Buku> dataBuku = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_BUKU;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if (csr.moveToFirst()){
            do{
                Date tempDate= new Date();
                try {
                    tempDate = sdFormat.parse(csr.getString(2));
                } catch (ParseException er) {
                    er.printStackTrace();
                }

                Buku tempBuku = new Buku(
                    csr.getInt(0),
                    csr.getString(1),
                    tempDate,
                    csr.getString(3),
                    csr.getString(4),
                    csr.getString(5),
                    csr.getString(6),
                    csr.getString(7)
                );

                dataBuku.add(tempBuku);
            } while ((csr.moveToNext()));
        }
        return dataBuku;
    }

    private String storeImageFile(int id) {
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location = InputActivity.saveImageToInternalStorage(image, context);
        return location;
    }

    private void inisiasiBukuAwal(SQLiteDatabase db){
        int idBuku = 0;
        Date tempDate = new Date();


        try {
            tempDate = sdFormat.parse("13/03/2020 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }

        Buku buku1 = new Buku(
                idBuku, "Dilan 1 (Dia adalah Dilanku tahun 1990)",
                tempDate,
                storeImageFile(R.drawable.buku1),
                "Pastel Books",
                "Pidi Baiq",
                "Milea bertemu dengan Dilan di sebuah SMA di Bandung. Itu adalah tahun 1990, saat Milea pindah dari Jakarta ke Bandung. Perkenalan yang tidak biasa kemudian membawa Milea mulai mengenal keunikan Dilan lebih jauh. Dilan yang pintar, baik hati dan romantis... semua dengan caranya sendiri. Cara Dilan mendekati Milea tidak sama dengan teman-teman lelakinya yang lain, bahkan Beni, pacar Milea di Jakarta. Bahkan cara berbicara Dilan yang terdengar kaku, lambat laun justru membuat Milea kerap merindukannya jika sehari saja ia tak mendengar suara itu. Perjalanan hubungan mereka tak selalu mulus. Beni, gank motor, tawuran, Anhar, Kang Adi, semua mewarnai perjalanan itu. Dan Dilan... dengan caranya sendiri...selalu bisa membuat Milea percaya ia bisa tiba di tujuan dengan selamat. Tujuan dari perjalanan ini. Perjalanan mereka berdua. Katanya, dunia SMA adalah dunia paling indah. Dunia Milea dan Dilan satu tingkat lebih indah daripada itu.",
                "https://drive.google.com/open?id=11R3Jj2_cVSLWvdtkPNb_r4H6QhuXkyMp"
        );

        tambahBuku(buku1, db);
        idBuku++;


        try {
            tempDate = sdFormat.parse("13/03/2020 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }

        Buku buku2 = new Buku(
                idBuku, "Fix You",
                tempDate,
                storeImageFile(R.drawable.buku2),
                "Bukuné",
                "Nouraicha Afta & ACI",
                "Kencangnya angin di sore itu membuat daun-daun gugur beterbangan. Terdengar gemuruh petir di balik awan. Orang-orang bergegas, menghindari akan datangnya hujan.\n" +
                        "\n" +
                        "Namun, mereka tetap tenang menatap satu sama lain. Perasaan Aliando tenggelam, seakan terhipnotis dan hanyut di kedalaman sepasang mata cokelat itu. Sedang Prilly, tidak paham dengan apa yang bergemuruh di dadanya. Pipinya kemudian memerah ketika Ali menggenggam jemarinya.\n" +
                        "\n" +
                        "“Sekarang aku menggenggam tanganmu, akankah kamu menggenggam tanganku juga? Mataku hanya melihatmu, maukah kamu hanya melihatku? Hatiku sudah menjadi milikmu, bisakah aku memiliki hatimu?” Mendengar itu, sang gadis tertunduk. Ali mengusapkan ibu jarinya di pipi Prilly.\n" +
                        "\n" +
                        "Ali…, kalau saja ada kalimat yang mampu terucap dari hati ini, tentu dia sudah berkata, aku sayang kamu.\n" +
                        "____________________________\n" +
                        "\n" +
                        "Trauma masa lalu membuat Prilly Rivera enggan menggunakan suaranya. Sementara Aliando Ozora, seorang aktor terkenal, merasa sunyi dalam kesibukannya. Keduanya dipertemukan takdir di sebuah kafe. Takdir yang mengubah jalan hidup keduanya.",
                "https://drive.google.com/open?id=1vd0YjAfCo5STpYuF7KD3yX0xp-bGnt-4"
        );

        tambahBuku(buku2, db);
        idBuku++;


        try {
            tempDate = sdFormat.parse("12/03/2020 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }

        Buku buku3 = new Buku(
                idBuku, "Dear Nathan",
                tempDate,
                storeImageFile(R.drawable.buku3),
                "Best Media ",
                "Erisca Febriani",
                "Nathan (Jefri Nichol) masih tetap menjadi dirinya sendiri. Ia adalah siswa yang “liar” namun, punya sisi yang baik dalam dirinya. Begitu juga dengan Salma (Amanda Rawles), seorang siswi yang sabar, namun menyayangi Nathan dengan segalam kelebihan dan kekuranganya.\n" +
                        "\n" +
                        "Perpaduan Nathan dan Salma seperti dua kisah romansa anak muda. Namun, romansa ini penuh konflik. Nathan kembali berulah karena memukul seorang siswa. Hal yang membuat Salma menjadi salah paham dan sedikit kebingungannya dengan sikap Nathan. Kejadian ini membuat Nathan harus pindah sekolah dan mulai berjarak dengan Salma.\n" +
                        "\n" +
                        "Begitu juga dengan Salma. Kehidupan cintnaya dengan Nathan tidak jauh berbeda dengan kehidupan sehari-harinya. Salma gagal dalam sebuah ujian masuk perguruan tinggi bergensi. Ditambah lagi, ia memiliki pandangan yang berbeda dengan sang Ayah ketika dijodohkan dengan Ridho.\n" +
                        "\n" +
                        "Namun, di tengah permasalahan Salma yang kian berat, sosok Rebecca mencoba membantu. Rebecca mencoba memahami dan memberikan oslusi untuk Salma.\n" +
                        "\n" +
                        "Sayangnya, kedekatan mereka menjadi runyam ketika, Salma kembali melihat kehadiran Nathan. Kisah Nathan, Rebecca dan Salma akan menjadi bumbu yang berbeda dari film ini. Bukan tentang orang ketiga, namun kisah-kisah kecil yang membuat cinta itu adalah bahasa yang sederhana.\n" +
                        "\n" +
                        "Siapa Rebecca sebenarnya? Bagaimana kisah Nathan dan Salma?",
                "https://drive.google.com/open?id=1YQfBSToNdRZd4m93L_TlNiRhcJpQZiTC"
        );

        tambahBuku(buku3, db);
        idBuku++;


        try {
            tempDate = sdFormat.parse("13/03/2020 05:58");
        } catch (ParseException er) {
            er.printStackTrace();
        }

        Buku buku4 = new Buku(
                idBuku, "Si Juki Cari Kerja",
                tempDate,
                storeImageFile(R.drawable.buku4),
                "Bukuné",
                "Faza Meonk",
                "Setelah lulus SMA, Juki adalah bocah nyentrik yang ngakunya nggak menyukai hal mainstream,memutuskan untuk langsung bekerja. Dengan keterampilan seadanya, kelakuan nyeleneh, dan teman-teman yang ajaib, Juki memulai petualangannya.\n" +
                        "\n" +
                        "Sayang…, ada hal yang Juki tidak tahu, yaitu susahnya mencari pekerjaan tanpa bekal yang cukup. Berbagai macam hal dicobanya. Jadi buruh tempel iklan sedot WC, petugas delivery service sebuah warteg, figuran acara televisi, sampai menjadi asisten dukun Mbah Gendeng, semuanya gagal total.\n" +
                        "Apa lagi akal Juki untuk dapatkan pekerjaan dan meredakan Emak yang terus-terusan cemberut karena bocah ajaib ini lama menganggur? Ikuti ceritanya dalam Si Juki Cari Kerja!",
                "https://drive.google.com/open?id=1P_Ny2PG7xq1F_j80CJpvFCCYkGAP8w1n"
        );

        tambahBuku(buku4, db);
    }

}