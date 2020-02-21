package com.example.inkler;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//Clase que gestiona el listener del recyclerview

public class RecyclerViewListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;


    //Creacion interface para el click listener
    public interface OnItemClickListener{
         void onItemClick(View view, int position);
         void onLongItemClick(View view, int position);
    }
    private GestureDetector mGestureDetector;

    //El lisener con las opciones
    RecyclerViewListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener){
    mListener = listener;
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent event){
                return true;
            }
            @Override
            public void onLongPress (MotionEvent event){
                View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
                if (child != null && mListener != null) {
                    mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent event) {
        View childView = view.findChildViewUnder(event.getX(), event.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(event)){
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
