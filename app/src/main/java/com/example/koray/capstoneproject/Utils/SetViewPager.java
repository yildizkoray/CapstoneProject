package com.example.koray.capstoneproject.Utils;

        import android.support.v4.view.ViewPager;

        import com.example.koray.capstoneproject.Adapters.ViewPagerAdapter;
        import com.example.koray.capstoneproject.tabfragment.ImdbGenreSelection;
        import com.example.koray.capstoneproject.tabfragment.ProfileFragment;
        import com.example.koray.capstoneproject.tabfragment.SelectGenreForAdvice;

/**
 * Created by Koray on 12.12.2017.
 */

public class SetViewPager {

    public SetViewPager() {

    }

    public void setPager(ViewPager viewPager, ViewPagerAdapter adapter) {
        adapter.addFragment(new ProfileFragment(), "Profil");
        adapter.addFragment(new SelectGenreForAdvice(), "Tavsiye");
        adapter.addFragment(new ImdbGenreSelection(),"Puanla");
        viewPager.setAdapter(adapter);
    }
}
