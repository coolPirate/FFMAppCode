package ffm.geok.com.ui.fragment.data;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ffm.geok.com.R;
import ffm.geok.com.base.BaseBackFragment;


public class CycleFragment extends BaseBackFragment {
    private static final String ARG_NUMBER = "arg_number";

    public static CycleFragment newInstance(int number) {
        CycleFragment fragment = new CycleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, number);
        fragment.setArguments(args);
        return fragment;
    }


}
