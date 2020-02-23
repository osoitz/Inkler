package com.example.inkler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;


public class GaleriaActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private int shortAnimationDuration;
    private Animator currentAnimator;
    private ImageView imageviewTatuaje;
    private static final int PICK_IMAGE = 100;
    private int idTatuador;
    private DBlocal db;

    //private static final int DSQLITE_DEFAULT_CACHE_SIZE=2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBlocal(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        idTatuador = getIntent().getIntExtra("idTatuador", -1);
        //Toast.makeText(getApplicationContext(), "Tatuador: " + idTatuador, Toast.LENGTH_SHORT ).show();
        ArrayList<Bitmap> fotos = new ArrayList<>();
        try {
            fotos = db.recogerFotosTatuador(idTatuador);
            Toast.makeText(getApplicationContext(), "Tatuador: " + idTatuador + " Fotos recogidas de la BD: " + fotos.size(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "OMG!", Toast.LENGTH_SHORT).show();
        }

        //Log.d("tag", "onCreate: "+ db.recogerFotos(idTatuador));

        RecyclerView recyclerView = findViewById(R.id.recyclerGaleria);
        AdaptadorGaleria adaptador = new AdaptadorGaleria(GaleriaActivity.this, fotos);
        recyclerView.setAdapter(adaptador);
        ConstraintLayout cl = findViewById(R.id.recycler_galeria);
        if (cl == null) {
            layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        } else {
            layoutManager = new GridLayoutManager(getApplicationContext(), 5);
        }
        recyclerView.setLayoutManager(layoutManager);
        //onclick para ver los datos del alumno selccionado en la activity DatosAlumno
        recyclerView.addOnItemTouchListener(new RecyclerViewListener(GaleriaActivity.this, recyclerView, new RecyclerViewListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                imageviewTatuaje = view.findViewById(R.id.tatuaje);
                zoomImageFromThumb(imageviewTatuaje, (Integer) imageviewTatuaje.getTag());

            }

            @Override
            public void onLongItemClick(View view, int position) {
                //Nicths
            }
        }));

        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

    }
    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.imagenGrande);
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

        //noinspection SimplifiableIfStatement
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
            openGallery();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    //Este es el metodo que coge la foto de la galeria y la guarda en la BD
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {

            Toast.makeText(getApplicationContext(), "Añadiendo foto a la BD", Toast.LENGTH_SHORT).show();
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);
                long rowid = db.insertarFoto(App.getBytes(bitmap), idTatuador);
                Toast.makeText(getApplicationContext(), "Foto añadida. ID: " + rowid + " Tatuador: " + idTatuador, Toast.LENGTH_SHORT).show();
                db.insertarFoto(App.getBytes(bitmap), idTatuador);
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.e("Error","exceptions"+e);
            }

            //guardarImagen(imageInByte);

        }
    }



}
