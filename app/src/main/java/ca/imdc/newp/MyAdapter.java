package ca.imdc.newp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.util.RangeValueIterator;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Arrays;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by imdc on 16/08/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mDataset;
    private String[] mDate;


    public Context mContext;
    Thread t = null;
    private static final int LENGTH = 18;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView date;
        public TextView time;
        public ImageView delete;
        public ImageView share;
        public ImageView View;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.my_text_view);
            delete  =  (ImageView) v.findViewById(R.id.delete_image);
            share = (ImageView) v.findViewById(R.id.share_image);
            View = (ImageView) v.findViewById(R.id.view_image);
            time = (TextView) v.findViewById(R.id.time_text);
            date = (TextView) v.findViewById(R.id.date_text);
//// TODO: 17/08/2016 Implement the above variables in the XML
            ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_image);
            shareImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View v) {
                            Snackbar.make(v, "Share Video",
                            Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset, String[] myDate, Context context) {
        mDataset = myDataset;
        mDate = myDate;
        this.mContext = context;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset[position]);
        holder.time.setText(mDate[position % mDate.length]);
     //   holder.date.setText(mDate[position % mDate.length]);
        holder.View.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity mainact = new MainActivity();
                System.out.println(holder.mTextView.getText());
                String name = (String) holder.mTextView.getText();
                System.out.println("Name:"+name);
                String bc = mainact.decrypt(name);
                System.out.println("BC:"+bc);
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(bc));
                intent.setDataAndType(Uri.parse(bc), "video/mp4");
                mContext.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity mainact = new MainActivity();
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(mDataset));
                list.remove(position);
                mDataset = list.toArray(new String[list.size()]);
                notifyItemRemoved(position);
                System.out.println(holder.mTextView.getText());
                String name = (String) holder.mTextView.getText();
                mainact.deleteIt(mContext.getExternalFilesDir(null).getAbsolutePath()+"/Encrypted/"+name);
                mainact.deleteIt(mContext.getExternalFilesDir(null).getAbsolutePath()+"/Video/"+name.replace(".encrypt",""));
            }
        });
//// TODO: 17/08/2016  implement date and time datasets. We need to retrieve it directly from an array
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mDataset==null) return 0;
        else return mDataset.length;
    }
}