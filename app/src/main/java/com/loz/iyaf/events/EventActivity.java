package com.loz.iyaf.events;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loz.R;
import com.loz.iyaf.feed.EventData;
import com.loz.iyaf.imagehelpers.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class EventActivity extends ActivityManagePermission implements OnMapReadyCallback {

    private EventData event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Bundle bundle = this.getIntent().getExtras();
        event = (EventData) bundle.get("event");

        setTitle("Back");

        //setFavouriteButton(event.isFavourite());
        ImageView eventImage = (ImageView) findViewById(R.id.eventImage);
        if (event.getCoverUrl() != null) {
            Utils.loadImage(event.getCoverUrl(), eventImage, null);
        }

        TextView eventDate = (TextView) findViewById(R.id.eventDate);
        SimpleDateFormat sdfDate = new SimpleDateFormat("EEE d MMM yyyy");
        eventDate.setText(sdfDate.format(event.getStartTime()));

        TextView eventTime = (TextView) findViewById(R.id.eventTime);
        SimpleDateFormat sdfTime = new SimpleDateFormat("H:mm");
        eventTime.setText(sdfTime.format(event.getStartTime()));

        TextView eventLocation = (TextView) findViewById(R.id.eventLocation);
        eventLocation.setText(event.getLocation());

        TextView eventName = (TextView) findViewById(R.id.eventName);
        eventName.setText(event.getName());

        TextView eventAbout = (TextView) findViewById(R.id.eventAbout);
        eventAbout.setText(event.getDescription());

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        boolean mapVisible = (event.getVenue() != null && event.getVenue().getLatitude() != null && event.getVenue().getLongitude() != null);
        if (mapVisible) {
            mapFragment.getView().setVisibility(View.VISIBLE);
            mapFragment.getMapAsync(this);
        } else {
            mapFragment.getView().setVisibility(View.INVISIBLE);
        }

        Button calendarButton = (Button) findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                Calendar cal = new GregorianCalendar();
                cal.setTime(event.getStartTime());
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
                if (event.getEndTime() != null) {
                    cal.setTime(event.getEndTime());
                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis());
                }
                intent.putExtra(CalendarContract.Events.TITLE, event.getName());
                intent.putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription());
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.getLocation());
                startActivity(intent);
            }
        });

        Button ticketButton = (Button) findViewById(R.id.ticketButton);
        if (event.getTicketUrl() != null) {
            ticketButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getTicketUrl()));
                    startActivity(browserIntent);
                }
            });
        } else {
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            p.addRule(RelativeLayout.BELOW, R.id.eventAbout);
            p.addRule(RelativeLayout.CENTER_HORIZONTAL);
            p.setMargins(0, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getApplicationContext().getResources().getDisplayMetrics())), 0, 0);
            calendarButton.setLayoutParams(p);
            ticketButton.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setFavouriteButton(event.isFavourite(), menu);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favourite:
                tappedFavouriteButton();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    private void tappedFavouriteButton() {
        event.setFavourite(!event.isFavourite());
        supportInvalidateOptionsMenu();
    }

    private void setFavouriteButton(boolean isFavourite, Menu menu) {
        MenuItem favouriteMenuItem = menu.findItem(R.id.action_favourite);
        if (isFavourite) {
            favouriteMenuItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite_set));
        } else {
            favouriteMenuItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite_unset));
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = getIntent();
        Bundle b = new Bundle();
        b.putSerializable("event", event);
        returnIntent.putExtras(b);
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(event.getVenue().getLatitude(), event.getVenue().getLongitude()))
                .title(event.getLocation()));
        askPermission(PermissionUtils.Manifest_ACCESS_FINE_LOCATION)
                .setPermissionResult(new PermissionResult() {
                    @Override
                    public void permissionGranted() {
                        //permission granted
                        googleMap.setMyLocationEnabled(true);

                    }

                    @Override
                    public void permissionNotGranted() {
                        //permission denied
                        Toast.makeText(getApplicationContext(),
                                "Ok, but you'll need to find yourself on the map",
                                Toast.LENGTH_SHORT).show();                    }
                })
                .requestPermission(PermissionUtils.KEY_CAMERA);
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(event.getVenue().getLatitude(), event.getVenue().getLongitude()));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

}
