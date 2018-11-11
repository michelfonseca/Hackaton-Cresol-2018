package com.hackaton.fonseca.hackaton;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.R.array;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.hackaton.fonseca.hackaton.Fragments.CalculatorFragment;
import com.hackaton.fonseca.hackaton.Fragments.MainFragment;
import com.hackaton.fonseca.hackaton.Fragments.NewsFragment;
import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.crossfader.util.UIUtils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.SystemUtils;
import com.mikepenz.octicons_typeface_library.Octicons;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;


public class HomeActivity extends AppCompatActivity {

    private ImageView mDisplayImageView;
    private TextView mNameTextView;
    public Drawer result = null;
    private MiniDrawer miniResult = null;
    private AccountHeader headerResult = null;
    private MaterialMenuDrawable materialMenu;
    private boolean isDrawerOpened;
    private MaterialMenuDrawable.IconState iconState;
    private static int RESULT_LOAD_IMAGE = 1;

    private Crossfader crossFader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbarTask);
        setSupportActionBar(mToolbar);

        MainFragment mainFragment = new MainFragment();
        android.support.v4.app.FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
        fragment.replace(R.id.crossfade_content,mainFragment);
        fragment.commit();


        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }

        });

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.mipmap.background_nav)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Aparício Padilha")
                                .withEmail("aparacio.padilha@unisep.com")
                                .withIcon(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withTranslucentStatusBar(false)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Guardião").withIcon(FontAwesome.Icon.faw_tachometer_alt).withBadge("4").withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Simulador").withIcon(FontAwesome.Icon.faw_calculator).withIdentifier(2),
                        new PrimaryDrawerItem().withDescription("Seu perfil de gasto").withName("Perfil").withIcon(FontAwesome.Icon.faw_chart_line).withIdentifier(3),
                        new SectionDrawerItem().withName("Social"),
                        new PrimaryDrawerItem().withName("Notícias").withIcon(FontAwesome.Icon.faw_newspaper).withIdentifier(4),
                        new PrimaryDrawerItem().withName("Compartilhe").withIcon(FontAwesome.Icon.faw_share).withIdentifier(5),
                        new PrimaryDrawerItem().withName("Sair").withIcon(FontAwesome.Icon.faw_sign_out_alt).withIdentifier(7).withTag(1)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == 1) {
                                MainFragment mainFragment = new MainFragment();
                                android.support.v4.app.FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
                                fragment.setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
                                fragment.add(R.id.crossfade_content, mainFragment);
                                fragment.commit();
                                drawerItem.withSetSelected(false);
                                return true;
                            } else if (drawerItem.getIdentifier() == 2) {
                                CalculatorFragment calculatorFragment = new CalculatorFragment();
                                android.support.v4.app.FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
                                fragment.setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
                                fragment.add(R.id.crossfade_content, calculatorFragment);
                                fragment.commit();
                                drawerItem.withSetSelected(false);
                                return true;
                            } else if (drawerItem.getIdentifier() == 4) {
                                NewsFragment newsFragment = new NewsFragment();
                                android.support.v4.app.FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
                                fragment.setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
                                fragment.add(R.id.crossfade_content, newsFragment);
                                fragment.commit();
                                drawerItem.withSetSelected(false);
                                return true;
                            } else if (drawerItem.getIdentifier() == 7) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                HomeActivity.this.startActivity(intent);
                                return true;
                            }
                        }
                        return true;
                    }
                })
                .withGenerateMiniDrawer(true)
                .withSavedInstance(savedInstanceState)
                .buildView();

        miniResult = result.getMiniDrawer();

        int firstWidth = (int) UIUtils.convertDpToPixel(300, this);
        int secondWidth = (int) UIUtils.convertDpToPixel(72, this);

        crossFader = new Crossfader()
                .withContent(findViewById(R.id.crossfade_content))
                .withFirst(result.getSlider(), firstWidth)
                .withSecond(miniResult.build(this), secondWidth)
                .withPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
                    @Override
                    public void onPanelSlide(@NonNull View panel, float slideOffset) {
                        materialMenu.setTransformationOffset(
                                MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                                isDrawerOpened ? 2 - slideOffset : slideOffset
                        );
                        InputMethodManager inputMethodManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }

                    @Override
                    public void onPanelOpened(@NonNull View panel) {
                        isDrawerOpened = true;
                    }

                    @Override
                    public void onPanelClosed(@NonNull View panel) {
                        isDrawerOpened = false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        miniResult.withCrossFader(new CrossfadeWrapper(crossFader));

        crossFader.getCrossFadeSlidingPaneLayout().setShadowResourceLeft(R.drawable.material_drawer_shadow_left);

        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        mToolbar.setNavigationIcon(materialMenu);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (crossFader != null) {
                    crossFader.crossFade();
                }
            }
        });

/*


        if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) {
            Glide.with(TaskActivity.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(ImageHolder);
        } else {
            result;
        }
 */;

        isNetworkConnectionAvailable();
    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (crossFader != null && crossFader.isCrossFaded()) {
            crossFader.crossFade();
            if (crossFader.isCrossFaded()) {
                //materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
            }
        } else {
            if (doubleBackToExitPressedOnce) {
                //materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
                moveTaskToBack(true);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.twice_back, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task, menu);
        MenuInflater inflater = getMenuInflater();
        if (SystemUtils.getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
            inflater.inflate(R.menu.embedded, menu);
            menu.findItem(R.id.menu_1).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_sort).color(Color.WHITE).actionBar());
        }
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void setActionBarSubtitle(String subtitle) {
        getSupportActionBar().setSubtitle(subtitle);
    }

    public void setLogoTitle(Integer id) {
        getSupportActionBar().setIcon(id);
    }

    /*
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_principal) {
            TiskFragment tiskFragment = new TiskFragment();
            android.support.v4.app.FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
            fragment.setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
            fragment.replace(R.id.frameContainer, tiskFragment);
            fragment.commit();

        } else if (id == R.id.nav_note) {
            NoteListFragment noteListFragment = new NoteListFragment();
            android.support.v4.app.FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
            fragment.setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
            fragment.replace(R.id.frameContainer, noteListFragment);
            fragment.commit();

        } else if (id == R.id.nav_site) {
            if (isNetworkConnectionAvailable() == false) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sem conexão")
                        .setContentText("Por favor, conecte-se a internet para ver as notícias")
                        .setCancelText("CONFIGURAÇÕES")
                        .setConfirmText("OK")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                startActivity(new Intent(Settings.ACTION_SETTINGS));
                            }
                        })
                        .show();
            } else {
                SiteFragment siteFragment = new SiteFragment();
                android.support.v4.app.FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
                fragment.setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
                fragment.replace(R.id.frameContainer, siteFragment);
                fragment.commit();
            }

        } else if (id == R.id.nav_dicionario) {
            DictionaryFragment dictionaryFragment = new DictionaryFragment();
            android.support.v4.app.FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
            fragment.setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
            fragment.replace(R.id.frameContainer, dictionaryFragment);
            fragment.commit();

        } else if (id == R.id.nav_avalie) {
            if (isNetworkConnectionAvailable() == false) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sem conexão")
                        .setContentText("Por favor, conecte-se a internet para avaliar o Tisk")
                        .setCancelText("CONFIGURAÇÕES")
                        .setConfirmText("OK")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                startActivity(new Intent(Settings.ACTION_SETTINGS));
                            }
                        })
                        .show();
            } else {
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
            }
        } else if (id == R.id.nav_logout) {
            if (isNetworkConnectionAvailable() == false) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Sem conexão")
                        .setContentText("Por favor, conecte-se a internet para sair")
                        .setCancelText("CONFIGURAÇÕES")
                        .setConfirmText("OK")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                startActivity(new Intent(Settings.ACTION_SETTINGS));
                            }
                        })
                        .show();
            } else {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Fazer logoff")
                        .setContentText("Você tem certeza que deseja fazer logoff?")
                        .setCancelText("NÃO")
                        .setConfirmText("SIM")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(TaskActivity.this, MainActivity.class);
                                TaskActivity.this.startActivity(intent);
                            }
                        })
                        .show();
            }

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    */

    private Integer umaVez = 0;

    private void checkNetworkConnection() {

        umaVez = 1;

        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Sem conexão")
                .setContentText("Por favor, conecte-se a internet para continuar a criar novas tarefas e notas")
                .setCancelText("CONFIGURAÇÕES")
                .setConfirmText("OK")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                })
                .show();
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            if (umaVez == 1) {
                return false;
            } else {
                checkNetworkConnection();
                Log.d("Network", "Not Connected");
                return false;
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        //add the values which need to be saved from the crossFader to the bundle
        outState = crossFader.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case R.id.menu_1:
                crossFader.crossFade();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.nav_principal:
                MainFragment mainFragment = new MainFragment();
                android.support.v4.app.FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
                fragment.setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
                fragment.replace(R.id.frameContainer, mainFragment);
                fragment.commit();
                return true;
            case 7:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Fazer logoff")
                        .setContentText("Você tem certeza que deseja fazer logoff?")
                        .setCancelText("NÃO")
                        .setConfirmText("SIM")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                HomeActivity.this.startActivity(intent);
                            }
                        })
                        .show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}

class CrossfadeWrapper implements ICrossfader {
    private Crossfader mCrossfader;

    public CrossfadeWrapper(Crossfader crossfader) {
        this.mCrossfader = crossfader;
    }

    @Override
    public void crossfade() {
        mCrossfader.crossFade();
    }

    @Override
    public boolean isCrossfaded() {
        return mCrossfader.isCrossFaded();
    }
}

