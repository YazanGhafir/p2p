package com.snubbull.app.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.snubbull.app.R;
import com.snubbull.app.model.AndroidID;
import com.snubbull.app.model.Notice;
import java.util.List;


/**
 * An adapter that translates the model representation of Notices to the view representation
 * Notices. This class considers any layout that includes two text views, one with the id 'title'
 * and one with the id 'content' a representation of a notice.
 */
public class NoticeAdapter<T extends Notice> extends android.widget.ArrayAdapter<T> {
  private String ID;
  /**
   * The context this adapter will work in.
   */
  private final Context context;
  /**
   * The ID of the layout resource that will be used.
   */
  private final int layoutResourceId;

  /**
   * Constructor.
   *
   * @param context The context this adapter will work in
   * @param layoutResourceId The id of the layout resource. Must contain title and content TextView
   * @param data The objects to represent in the ListView
   */
  public NoticeAdapter(Context context, int layoutResourceId, List<T> data) {
    super(context, layoutResourceId, data);
    this.context = context;
    this.layoutResourceId = layoutResourceId;
  }

  public NoticeAdapter(Context context, int layoutResourceId, List<T> data,String ID) {
    super(context, layoutResourceId, data);
    this.context = context;
    this.layoutResourceId = layoutResourceId;
    this.ID=ID;
  }

  @NonNull
  @Override

  public View getView(int position, View convertView,
      @NonNull ViewGroup parent) {

      View v = convertView;

    if (v == null) {
        LayoutInflater li;
        li = LayoutInflater.from(context);
        v = li.inflate(layoutResourceId, null);


      }
      Notice notice = getItem(position);

      if (notice != null) {

        TextView date = v.findViewById(R.id.title);
        date.setText(
            notice.getDeliveryDate());

        TextView price = v.findViewById(R.id.content);
        price.setText("" + notice.getPrice());

        TextView from = v.findViewById(R.id.to);
        from.setText(notice.getFrom());

        TextView to = v.findViewById(R.id.inputfieldFrom);
        to.setText(notice.getTo());

      }
      return v;
    }

}