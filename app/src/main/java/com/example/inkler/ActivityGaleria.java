package com.example.inkler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class ActivityGaleria extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private int shortAnimationDuration;
    private Animator currentAnimator;
    private ImageView imageviewTatuaje;
    private static final int PICK_IMAGE = 100;
    private int idTatuador;
    private DBlocal db;
    Bitmap bmp;
    ArrayList<Bitmap> fotos;
    AdaptadorGaleria adaptador;
    RecyclerView recyclerView;

    //private static final int DSQLITE_DEFAULT_CACHE_SIZE=2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBlocal(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        solicitarPermisos();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        idTatuador = getIntent().getIntExtra("idTatuador", -1);
        //Toast.makeText(getApplicationContext(), "Tatuador: " + idTatuador, Toast.LENGTH_SHORT ).show();

        recyclerView = findViewById(R.id.recyclerGaleria);

        rellenarAdaptador();

        ConstraintLayout cl = findViewById(R.id.activity_galeria);
        if (cl == null) {
            layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        } else {
            layoutManager = new GridLayoutManager(getApplicationContext(), 5);
        }
        recyclerView.setLayoutManager(layoutManager);
        //onclick para ver la foto en grande
        recyclerView.addOnItemTouchListener(new RecyclerViewListener(ActivityGaleria.this, recyclerView, new RecyclerViewListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                imageviewTatuaje = view.findViewById(R.id.tatuaje);
                zoomImageFromThumb(imageviewTatuaje, (Integer) imageviewTatuaje.getTag());

            }
/*
            @Override
            public void onLongItemClick(View view, int position) {
                //Nicths
            }

 */
        }));

        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

    }

    private void rellenarAdaptador() {

        //AAA
        fotos = new ArrayList<>();
        try {
            fotos = db.recogerFotosTatuador(idTatuador);
            Toast.makeText(getApplicationContext(), "Tatuador: " + idTatuador + " Fotos recogidas de la BD: " + fotos.size(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "OMG!", Toast.LENGTH_SHORT).show();
        }
        //AAA
        adaptador = new AdaptadorGaleria(ActivityGaleria.this, fotos);
        recyclerView.setAdapter(adaptador);
    }

    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = findViewById(R.id.imagenGrande);
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.selectorGaleria)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actions, menu);
        if (App.isAdmin()) {
            menu.setGroupVisible(R.id.añadir, false);
            menu.setGroupVisible(R.id.modificar, false);
            menu.setGroupVisible(R.id.logout, false);
            menu.setGroupVisible(R.id.foto, true);
        } else {
            menu.setGroupVisible(R.id.login, true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.admin){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.contraseñatitle));

            final EditText input = new EditText(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            alertDialog.setView(input);

            alertDialog.setPositiveButton(getString(R.string.contraseñabtn), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String password = input.getText().toString();
                    //TODO usuarios reales
                    if (getString(R.string.contraseña).equals(password)){
                        App.setAdmin(true);
                        invalidateOptionsMenu();
                        Toast.makeText(getApplicationContext(), R.string.log_successful, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), R.string.log_unsuccessful, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alertDialog.show();
        } else if (id == R.id.noadmin) {
            App.setAdmin(false);
            invalidateOptionsMenu();

        }else if (id == R.id.añadir_foto) {
            //openGallery();
            sacarFoto();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /******************************
     *  FOTOS
     ******************************/
    //Este metodo lo usaremos para sacar la foto
    public void sacarFoto(){
        //Mediante un intente llamaremos a la camara para sacar una foto
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Una vez sacada esa foto vamos a cojerla del intent y la guardaremos en forma de bitmap
            Bundle ext = data.getExtras();
            bmp = (Bitmap) ext.get("data");
            System.out.println("exito");
            //saveTempBitmap(bmp);
            saveImage(bmp);
        }
    }


    private void saveImage(Bitmap finalBitmap) {
        //Guardamos la foto en la base de datos
        long rowid = db.insertarFoto(App.getBytes(finalBitmap), idTatuador);
        //Toast.makeText(getApplicationContext(), "Foto añadida. ID: " + rowid + " Tatuador: " + idTatuador, Toast.LENGTH_SHORT).show();
        //Volvemos a rellenar el adaptador para que se vean todas las fotos
        rellenarAdaptador();
    }

    //Comprobamos si tenemos permisos de escritura, y si no los pediremos
    private void solicitarPermisos(){
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permisocamara = ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED || permisocamara != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para la camara!.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "Tienes permiso para usar la camara.");
        }
    }

}
