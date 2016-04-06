package com.udacity.stage2.popularmoviess2.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Rashida on 3/8/2016.
 */
public interface MovieColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull
    String MOVIE_ID = "id";

    @DataType(DataType.Type.TEXT) @NotNull
    String BACKDROP_PATH = "backdrop_path";

    @DataType(DataType.Type.TEXT) @NotNull
    String TITLE = "title";

    @DataType(DataType.Type.TEXT) @NotNull
    String OVERVIEW = "overview";

    @DataType(DataType.Type.TEXT) @NotNull
    String VOTE_AVERAGE = "vote_average";

    @DataType(DataType.Type.TEXT) @NotNull
    String VOTE_COUNT = "vote_count";

    @DataType(DataType.Type.TEXT) @NotNull
    String POSTER_PATH = "poster_path";

    @DataType(DataType.Type.TEXT) @NotNull
    String RELEASE_DATE= "release_date";

    String[] PROJECTION_ALL={_ID,MOVIE_ID,BACKDROP_PATH,TITLE,OVERVIEW,VOTE_AVERAGE,VOTE_COUNT,POSTER_PATH,RELEASE_DATE};

}
