package com.loz.iyaf.events;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
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

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class EventActivity extends ActivityManagePermission implements OnMapReadyCallback {

    private EventData event;
    private ShareActionProvider mShareActionProvider;

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
            Utils.loadImage(event.getCoverUrl(), eventImage, null, 0);
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
            ticketButton.setVisibility(View.INVISIBLE);
        }
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(event.getName())
                .putContentType("Events")
                .putContentId(String.valueOf(event.getId())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        MenuItem item = menu.findItem(R.id.action_share);
//        item.setIcon(R.drawable.ic_share);
//
//        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        //MenuItem item = menu.add(Menu.NONE, R.id.action_share, Menu.NONE, "Share");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mShareActionProvider = new ShareActionProvider(this) {
            @Override
            public View onCreateActionView() {
                return null;
            }
        };
        item.setIcon(R.drawable.ic_share);
        MenuItemCompat.setActionProvider(item, mShareActionProvider);

        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_TEXT, event.getName()+" https://www.facebook.com/events/"+event.getEventId());
        mShareActionProvider.setShareIntent(myShareIntent);

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
            case R.id.action_share:
                Log.d("LOZ", "Share event tapped");
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
        Log.d("LOZ", "Setting marker on map");
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(event.getVenue().getLatitude(), event.getVenue().getLongitude()))
                .title(event.getLocation()));
        askPermission(PermissionUtils.Manifest_ACCESS_FINE_LOCATION)
                .setPermissionResult(new PermissionResult() {
                    @Override
                    public void permissionGranted() {
                        //permission granted
                        Log.d("LOZ", "Permission for user location granted");

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
