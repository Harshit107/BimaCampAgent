package com.teknesya.jeevanbimacamp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.teknesya.jeevanbimacamp.AgentAnimationView;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.UserProfileData;
import com.teknesya.jeevanbimacamp.presentationG;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PresentationRecyclerAdapter extends RecyclerView.Adapter<PresentationRecyclerAdapter.ViewHolder> {


    private List<presentationG> listItems;
    private Context context;
    Activity activity;

    public PresentationRecyclerAdapter(List<presentationG> listItems, Context context,Activity activity) {
        this.listItems = listItems;
        this.context = context;
        this.activity=activity;
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
    public List<presentationG> getListItems() {
        return listItems;
    }

    public void setListItems(List<presentationG> listItems) {
        this.listItems = listItems;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.presentation_listing, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final presentationG listItem = listItems.get(position);
         final String id;
        holder.name.setText(listItem.getName());
        holder.date.setText(listItem.getDate());
        holder.presentation.setText("Count "+listItem.getRemaning());
        id=listItem.getNodeID();
        //Log.d("presentation", listItem.getString());

        //Onclick action to the RecyclearView

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
               AlertDialog.Builder builder= new AlertDialog.Builder(activity);
                       builder .setPositiveButton("Delete Presentation", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toasty.info(context,"Deleting Presentation..").show();
                                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                                DatabaseReference mRoot= FirebaseDatabase.getInstance().getReference();
                                final DatabaseReference getPresentationDB = mRoot.child("users").child("agent")
                                        .child(mAuth.getUid()).child("presentation").child(id);
                                getPresentationDB.removeValue();
                                DatabaseReference removePresentation=mRoot.child("unique").child("presentation");
                                removePresentation.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toasty.success(context,"Success").show();
                                        activity.finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toasty.error(context,e.getMessage()).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                            }
                        })
                        .setMessage("Once You Delete Presentation," +
                                " You cannot get it back")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.app_name);
                try {
                    builder.create().show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s= listItem.getString();
                Log.i("stringview",s);
                String [] split=s.split("--");

                Intent it=new Intent(context, AgentAnimationView.class);
                it.putExtra("string",split);
                it.putExtra("id",id);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(it);

            }
        });
    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date,presentation;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            presentation = (TextView) itemView.findViewById(R.id.presentation);
        }

        public void deletePresentation(String id)
        {





        }
    }
}
