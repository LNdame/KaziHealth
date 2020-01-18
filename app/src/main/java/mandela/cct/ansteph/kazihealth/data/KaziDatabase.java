package mandela.cct.ansteph.kazihealth.data;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;

import mandela.cct.ansteph.kazihealth.model.RiskItem;
import mandela.cct.ansteph.kazihealth.model.RiskProfileItem;
import mandela.cct.ansteph.kazihealth.model.User;

@Database(entities = {User.class, RiskItem.class, RiskProfileItem.class}, version = 1, exportSchema = false)
public abstract class KaziDatabase extends RoomDatabase {
    private static String TAG = KaziDatabase.class.getSimpleName();
    private static Object LOCK =  new Object();

    public abstract UserDao userDao();
    public abstract RiskItemDao riskItemDao();
    public abstract RiskProfileDao riskProfileDao();

    private static final String DATABASE_NAME = "kazidatabase";
    private static KaziDatabase instance;

    public static KaziDatabase getInstance(Context context){
        if (instance == null){
            synchronized (LOCK){
                Log.d(TAG, "Creating new database instance");
                instance= Room.databaseBuilder(context.getApplicationContext(),
                        KaziDatabase.class, KaziDatabase.DATABASE_NAME)
                        .addCallback(kaziDatabaseCallback)
                        .build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return instance;
    }
    private static KaziDatabase.Callback kaziDatabaseCallback =
            new KaziDatabase.Callback(){
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateRiskItemTBAsync(instance).execute();
                }
            };



    private static class PopulateRiskItemTBAsync extends AsyncTask<Void, Void, Void>{

        private final RiskItemDao riskItemDao;
        public PopulateRiskItemTBAsync(KaziDatabase dbInstance){
            riskItemDao = dbInstance.riskItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            riskItemDao.deleteAll();
            String sdt_ = DateFormat.format("yyyy-MM-dd  kk:mm", new Date()).toString();
            RiskItem[] riskItems ={
                    new RiskItem("Blood Pressure", "mmHg", sdt_),
                    new RiskItem("Heart Rate", "bpm", sdt_),
                    new RiskItem("Cholesterol", "mmol/L", sdt_),
                    new RiskItem("Blood Glucose Level", "mg/dl", sdt_),
                    new RiskItem("Height", "cm", sdt_),
                    new RiskItem("Weight", "kg", sdt_),
                    new RiskItem("Body Mass Index", "-", sdt_),
                    new RiskItem("Waist", "cm", sdt_),
                    new RiskItem("Hip", "cm", sdt_),
                    new RiskItem("Waist to hip Ration (W2HRatio)", "-", sdt_),
            };
            riskItemDao.insertAll(riskItems);

            return null;
        }
    }


}
