package ch.virustracker.app.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.virustracker.app.R;
import ch.virustracker.app.controller.VtApp;
import ch.virustracker.app.model.proximityevent.ProximityEvent;
import ch.virustracker.app.model.proximityevent.ProximityEventSummary;

import static ch.virustracker.app.controller.VtApp.getController;

public class ProximityEventListAdapter extends RecyclerView.Adapter<ProximityEventListAdapter.ProximityEventViewHolder> {
    private final DateFormat dateFormat;
    private List<ProximityEventSummary> proximityEvents;

    public static class ProximityEventViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout layout;

        public ProximityEventViewHolder(RelativeLayout v) {
            super(v);
            layout = v;
        }
    }

    public ProximityEventListAdapter(List<ProximityEventSummary> myDataset) {
        proximityEvents = myDataset;
        dateFormat = android.text.format.DateFormat.getDateFormat(VtApp.getContext());
    }

    @Override
    public ProximityEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);
        ProximityEventViewHolder vh = new ProximityEventViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProximityEventViewHolder holder, int position) {
        Date date = new Date();
        ProximityEventSummary proximityEvent = proximityEvents.get(position);
        date.setTime(proximityEvent.getTimeAtBeginningOfDay());
        String dateStr = dateFormat.format(date);
        ((TextView)holder.layout.findViewById(R.id.firstLine)).setText(VtApp.getContext().getString(R.string.event_list_first_line, dateStr, proximityEvent.getNumHighRiskEvents()));
        ((TextView)holder.layout.findViewById(R.id.secondLine)).setText(VtApp.getContext().getString(R.string.event_list_second_line, proximityEvent.getNumLowRiskEvents()));
        if (proximityEvent.getNumHighRiskEvents() > 0) {
            ((ImageView) holder.layout.findViewById(R.id.icon)).setImageResource(R.drawable.img_bad);
        } else if (proximityEvent.getNumLowRiskEvents() > 0) {
            ((ImageView) holder.layout.findViewById(R.id.icon)).setImageResource(R.drawable.img_warn);
        } else {
            ((ImageView) holder.layout.findViewById(R.id.icon)).setImageResource(R.drawable.img_good);
        }
    }

    @Override
    public int getItemCount() {
        return proximityEvents.size();
    }
}
