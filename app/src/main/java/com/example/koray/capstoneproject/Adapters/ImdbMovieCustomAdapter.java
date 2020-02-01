package com.example.koray.capstoneproject.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koray.capstoneproject.Models.Imdb;
import com.example.koray.capstoneproject.R;
import com.example.koray.capstoneproject.Utils.HttpHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Koray on 17.12.2017.
 */

public class ImdbMovieCustomAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context mContext;
    private List<Imdb> ımdbList;

    public ImdbMovieCustomAdapter(Context context, List<Imdb> list) {
        mContext = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ımdbList = list;
    }

    @Override
    public int getCount() {
        return ımdbList.size();
    }

    @Override
    public Object getItem(int i) {
        return ımdbList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.cell_imb_movie, null);
            viewHolder = new ViewHolder();
            viewHolder.imdbPoster = (ImageView) view.findViewById(R.id.imgImdbMoviePoster);
            viewHolder.txtImdbMovieName = (TextView) view.findViewById(R.id.txtImdbMovieName);
            viewHolder.txtGenre = (TextView) view.findViewById(R.id.txtImdbMovieGenre);
            viewHolder.txtYear = (TextView) view.findViewById(R.id.txtImdbMovieYear);
            viewHolder.txtCellCounter = (TextView) view.findViewById(R.id.txtListCounter);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Imdb imImdbMovie = ımdbList.get(i);
        viewHolder.txtCellCounter.setText(String.valueOf(i + 1));
        viewHolder.txtImdbMovieName.setText(imImdbMovie.getOriginalTitle());
        viewHolder.txtGenre.setText(imImdbMovie.getGenres());
        viewHolder.txtYear.setText(String.valueOf(imImdbMovie.getStartYear()));
        new GetUrl(viewHolder.imdbPoster).execute(imImdbMovie.getTconst());

        return view;

    }

    private static class ViewHolder {
        private ImageView imdbPoster;
        private TextView txtImdbMovieName, txtGenre, txtYear, txtCellCounter;
    }

    private class GetUrl extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        ImageView ımageView;

        public GetUrl(ImageView ımage) {
            ımageView = ımage;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... url) {
            String id = url[0];
            String urlPoster = null;
            HttpHandler handler = new HttpHandler();
            String apiUrl = "http://www.omdbapi.com/?i=" + id + "&apikey=4074da0e";
            String jsonStr = handler.makeServiceCall(apiUrl);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if (jsonObject.has("Poster")) {
                        urlPoster = jsonObject.getString("Poster");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return urlPoster;
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            Picasso.with(mContext).load(s).fetch(new Callback() {
                @Override
                public void onSuccess() {
                    Picasso.with(mContext)
                            .load(s)
                            .into(ımageView);
                    progressDialog.dismiss();
                }

                @Override
                public void onError() {

                }
            });
        }
    }
}
