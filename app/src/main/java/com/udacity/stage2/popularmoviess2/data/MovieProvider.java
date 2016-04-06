package com.udacity.stage2.popularmoviess2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.NotifyBulkInsert;
import net.simonvt.schematic.annotation.NotifyDelete;
import net.simonvt.schematic.annotation.NotifyInsert;
import net.simonvt.schematic.annotation.NotifyUpdate;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Rashida on 3/8/2016.
 */
@ContentProvider(authority = MovieProvider.AUTHORITY,
        database = MovieDatabase.class,
        packageName = "com.udacity.stage2.popularmoviess2.provider")
public final class MovieProvider {

    private MovieProvider() {
    }

    public static final String AUTHORITY = "com.udacity.stage2.popularmoviess2";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String MOVIES = "movies";

    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }



    @TableEndpoint(table = MovieDatabase.MOVIES) public static class Movies {

        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/note")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);

        @InexactContentUri(
                name = "_ID",
                path = Path.MOVIES + "/#",
                type = "vnd.android.cursor.item/note",
                whereColumn = MovieColumns.MOVIE_ID,
                pathSegment = 1)
        public static Uri withId(String id) {
            return buildUri(Path.MOVIES, id);
        }


        @NotifyBulkInsert(paths = Path.MOVIES)
        public static Uri[] onBulkInsert(Context context, Uri uri, ContentValues[] values, long[] ids) {
            return new Uri[] {
                    uri,
            };
        }

    }
}
