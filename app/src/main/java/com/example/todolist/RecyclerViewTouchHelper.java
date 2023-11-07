package com.example.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapter.ToDoAdapter;

public class RecyclerViewTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ToDoAdapter adapter;
    public RecyclerViewTouchHelper(ToDoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter=adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.RIGHT){

            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure that you want to delete it ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteTask(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(position);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            adapter.editItem(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;

        int padding = 8;

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            int bgColor;
            Drawable icon = null;

            if (dX > 0) {
                bgColor = adapter.getContext().getResources().getColor(R.color.swipe_right_color);
                icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_delete_24);
            } else {
                bgColor = adapter.getContext().getResources().getColor(R.color.swipe_left_color);
                icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_edit_24);
            }


            // Setting the background color and icons when the user swipe the task to the left or right.
            // Draw the background color with padding
            c.drawRect(
                    (float) itemView.getLeft() + padding,
                    (float) itemView.getTop(),
                    (float) itemView.getRight() - padding,
                    (float) itemView.getBottom(),
                    new Paint(Paint.ANTI_ALIAS_FLAG) {{
                        setColor(bgColor);
                    }}
            );

            if (icon != null) {
                // Calculate the position for drawing the icon with padding
                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconLeft, iconRight;

                if (dX > 0) {
                    iconLeft = (int) (itemView.getLeft() + iconMargin + padding);
                    iconRight = (int) (itemView.getLeft() + iconMargin + icon.getIntrinsicWidth() + padding);
                } else {
                    iconRight = (int) (itemView.getRight() - iconMargin - padding);
                    iconLeft = (int) (itemView.getRight() - iconMargin - icon.getIntrinsicWidth() - padding);
                }

                // Set the bounds for the icon and draw it
                icon.setBounds(iconLeft, itemView.getTop() + iconMargin, iconRight, itemView.getBottom() - iconMargin);
                icon.draw(c);
            }
        }

    }


}
