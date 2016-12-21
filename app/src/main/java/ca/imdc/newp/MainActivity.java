package ca.imdc.newp;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    String cfileName;
    String encfileName;
    TextView instr;
    ListView listView;
    Thread t = null;
    private String[] Test1 = {"Halo"};
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] myDataset;
    private String[] myDate;
    private String[] myTime;




    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){
            case 200:
                boolean audioAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1]== PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};
        int permsRequestCode = 200;
        requestPermissions(perms, permsRequestCode);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.logo);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        //------------------------------------------------ Uncomment this
        myDataset = populateList("names");
        myDate = populateList("date");
       mAdapter = new MyAdapter(myDataset, myDate,this);
        mRecyclerView.setAdapter(mAdapter);
       // ------------------------------------------
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //  mRecyclerView.setHasFixedSize(false);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu_black_24dp, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);
                        // TODO: handle navigation
                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        //Floationg action button to create a dialogue
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setBackgroundColor(Color.RED);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog myAlert = new dialog();
                myAlert.show(getFragmentManager(), "dialog1");
            }
        });
        // use a linear layout manager
    }

    String[] populateList(String g) {
        MediaMetadataRetriever infoVideo = new MediaMetadataRetriever();
        File folder = new File(getExternalFilesDir(null).getAbsolutePath() + "/Encrypted/");
      //  File folder = new File("/storage/emulated/0/Pictures/Encrypted/"); //converted for PixelC
        File[] listOfFiles = folder.listFiles();
        String items[] = new String[listOfFiles.length];
        if (g == "date") {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    // infoVideo.setDataSource(listOfFiles[i].getAbsolutePath());
                    Date lastModDate = new Date(listOfFiles[i].lastModified());
                    System.out.println("Last modified date:" + lastModDate.toString());
                    items[i] = lastModDate.toString();
                }
            }
            return items;
        } else if (g == "names") {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
              //        infoVideo.setDataSource(listOfFiles[i].getAbsolutePath());
                    Date lastModDate = new Date(listOfFiles[i].lastModified());
                    System.out.println("Last modified date:" + lastModDate.toString());
                    items[i] = listOfFiles[i].getName();
                }
            }
            return items;
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    public void dispatchTakeVideoIntent() {
        int a;
        Random random = new Random();
        a = random.nextInt(70) + 1;
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
       // cfileName = "/storage/emulated/0/Pictures" + "/Video/" + "Untitled-" + a + ".mp4";

cfileName = getExternalFilesDir(null).getAbsolutePath() + "/Video/" + "Untitled-" + a + ".mp4";
        //converted for PixelC
       // encfileName = "/storage/emulated/0/Pictures" + "/Encrypted/" + "Untitled-" + a + ".mp4";
encfileName = getExternalFilesDir(null).getAbsolutePath() + "/Encrypted/" + "Untitled-" + a + ".mp4";
        //converted for PixelC
        System.out.println(cfileName);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", (new File(cfileName))));
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_VIDEO_CAPTURE) {
                cry();
                deleteAllVids();
                myDataset = populateList("names");
                myDate = populateList("date");
                mAdapter = new MyAdapter(myDataset, myDate,this);
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }

    private void cry()
    {
        final String key = "1111111111111111";
        final cryptoHash halo = new cryptoHash();
        try {
            String name = createfile("encrypt");
            final File encryptedFile = new File(name);
            final File inputFile = new File(cfileName);
            t = new Thread(new Runnable() {
                public void run() {
                    try {
                        halo.encrypt(key, inputFile, encryptedFile);
                    } catch (CryptoException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            t.join();
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAllVids()
    {
       // File folder = new File("/storage/emulated/0/Pictures"+"/Video/");
        File folder = new File ("/storage/emulated/0/Android/data/ca.imdc.newp/files/Video/");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                listOfFiles[i].delete();
            }
        }
    }

    public void deleteIt(String path)
    {
        File folder = new File(path);
        if(folder.exists())
        folder.delete();
    }


    public String decrypt(String name) {
        final String key = "1111111111111111";
        final cryptoHash halo = new cryptoHash();
        String a = name.replaceAll(".encrypt", "");
        final File decr = new File("/storage/emulated/0/Android/data/ca.imdc.newp/files/Video/" + a);
      //  final File decr = new File("/storage/emulated/0/Pictures/Video/" + a);
        System.out.println("File Path decr: " + decr.getAbsolutePath());
        try {
            decr.createNewFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        final File encryptedFile = new File("/storage/emulated/0/Android/data/ca.imdc.newp/files/Encrypted/" + name);
       // final File encryptedFile = new File("/storage/emulated/0/Pictures/Encrypted/" + name);
        System.out.println("File Path decr: " + encryptedFile.getAbsolutePath());
        try {
            t = new Thread(new Runnable() {
                public void run() {
                    try {
                        halo.decrypt(key, encryptedFile, decr);
                    } catch (CryptoException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            t.join();
        } catch (Exception c) {
            System.out.println(c.getMessage());
        }
        return decr.getAbsolutePath();
    }

    public void halo(String ubc )
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ubc));
        intent.setDataAndType(Uri.parse(ubc), "video/mp4");
        startActivity(intent);
    }


    private String createfile(String type)
    {
        String fName = null;
        if(type == "decrypt")
        {
            fName = cfileName ;
        }
        else if(type == "encrypt")
        {
            fName =  encfileName + ".encrypt";
        }
        else
            return fName;
        File file = new File(fName);
        try
        {
            file.createNewFile();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return fName;
    }


    public class dialog  extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstance) {

            // Use the Builder class for convenient dialog construction
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setTitle("Who are you recording?")
                    .setPositiveButton("SELF", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           dispatchTakeVideoIntent();
                        }
                    })
                    .setNeutralButton("OTHER", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog2 myAlert2 = new dialog2();
                            myAlert2.show(getFragmentManager(), "dialog2");
                            // User cancelled the dialog
                        }
                    });
            return builder.create();
        }
    }

    public class dialog2 extends DialogFragment
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstance) {

            // Use the Builder class for convenient dialog construction
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_text,null))
                    .setTitle("Consent")
                    .setPositiveButton("Disagree", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNeutralButton("Agree", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dispatchTakeVideoIntent();
                            mAdapter.notifyDataSetChanged();
                            // User cancelled the dialog
                        }
                    });
            return builder.create();

        }
    }


}