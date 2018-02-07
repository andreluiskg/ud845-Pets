/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PET_LOADER = 0;

    PetCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView petListView = (ListView) findViewById(R.id.list);

        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        mCursorAdapter = new PetCursorAdapter(this, null);
        petListView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(PET_LOADER, null, this);

    }

    /**
     protected void onStart() {
     super.onStart();
     displayDatabaseInfo();
     }
     */

    /**
     * private void displayDatabaseInfo() {
     * <p>
     * String[] projection = new String[]{
     * PetEntry._ID,
     * PetEntry.COLUMN_PET_NAME,
     * PetEntry.COLUMN_PET_BREED,
     * PetEntry.COLUMN_PET_GENDER,
     * PetEntry.COLUMN_PET_WEIGHT
     * };
     * <p>
     * Cursor cursor = getContentResolver().query(
     * PetEntry.CONTENT_URI,
     * projection,
     * null,
     * null,
     * null);
     * <p>
     * ListView petListView = (ListView) findViewById(R.id.list);
     * <p>
     * PetCursorAdapter adapter = new PetCursorAdapter(this, cursor);
     * <p>
     * petListView.setAdapter(adapter);
     * <p>
     * /**
     * // To access our database, we instantiate our subclass of SQLiteOpenHelper
     * // and pass the context, which is the current activity.
     * //PetDbHelper mDbHelper = new PetDbHelper(this);
     * <p>
     * // Create and/or open a database to read from it
     * //SQLiteDatabase db = mDbHelper.getReadableDatabase();
     * <p>
     * String[] projection = new String[]{
     * PetEntry._ID,
     * PetEntry.COLUMN_PET_NAME,
     * PetEntry.COLUMN_PET_BREED,
     * PetEntry.COLUMN_PET_GENDER,
     * PetEntry.COLUMN_PET_WEIGHT
     * };
     * <p>
     * //Cursor cursor = db.query(PetEntry.TABLE_NAME, project, null, null, null, null, null);
     * Cursor cursor = getContentResolver().query(
     * PetEntry.CONTENT_URI,
     * projection,
     * null,
     * null,
     * null);
     * <p>
     * try {
     * // Display the number of rows in the Cursor (which reflects the number of rows in the
     * // pets table in the database).
     * TextView displayView = (TextView) findViewById(R.id.text_view_pet);
     * displayView.setText("Number of rows in pets database table: " + cursor.getCount() + "\n");
     * <p>
     * displayView.append("\n" + PetEntry._ID
     * + " - " + PetEntry.COLUMN_PET_NAME
     * + " - " + PetEntry.COLUMN_PET_BREED
     * + " - " + PetEntry.COLUMN_PET_GENDER
     * + " - " + PetEntry.COLUMN_PET_WEIGHT
     * + "\n"
     * );
     * <p>
     * int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);
     * int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
     * int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
     * int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
     * int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);
     * <p>
     * while (cursor.moveToNext()) {
     * displayView.append("\n" + cursor.getLong(idColumnIndex)
     * + " - " + cursor.getString(nameColumnIndex)
     * + " - " + cursor.getString(breedColumnIndex)
     * + " - " + cursor.getInt(genderColumnIndex)
     * + " - " + cursor.getInt(weightColumnIndex)
     * );
     * }
     * <p>
     * } finally {
     * // Always close the cursor when you're done reading from it. This releases all its
     * // resources and makes it invalid.
     * cursor.close();
     * }
     * <p>
     * }
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertPet() {

        /**PetDbHelper mPetDbHelper = new PetDbHelper(this);

         SQLiteDatabase db = mPetDbHelper.getWritableDatabase();
         */

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        //long id = db.insert(PetEntry.TABLE_NAME, null, values);

        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);

        Toast.makeText(this, getString(R.string.editor_insert_pet_successful) + " " +
                        ContentUris.parseId(newUri),
                Toast.LENGTH_LONG).show();

    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED};

        return new CursorLoader(
                this,
                PetEntry.CONTENT_URI,
                projection,
                null, null, null);

    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link //FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#//CursorAdapter(Context, * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
