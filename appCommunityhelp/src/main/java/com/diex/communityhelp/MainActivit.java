package com.diex.communityhelp;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.res.Resources;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.drawable.BitmapDrawable;
        import android.os.Bundle;
        import android.support.v7.app.ActionBarActivity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.Toast;


public class MainActivit extends ActionBarActivity {

    MaterialDialog mMaterialDialog;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit);
        mMaterialDialog = new MaterialDialog(this);
    }




    public void show(View v) {
        final MaterialDialog materialDialog = new MaterialDialog(this);

            materialDialog.setTitle("Administración")
                    .setContentView( R.layout.ingreso_admin)
                    .setMessage(
                            "Clave de Adminstrador")
                    //mMaterialDialog.setBackgroundResource(R.drawable.background);
                    .setPositiveButton("INGRESA", new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            materialDialog.dismiss();
                            Toast.makeText(MainActivit.this, "Ok",
                                    Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("CANCELAR",
                            new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    materialDialog.dismiss();
                                    Toast.makeText(MainActivit.this,
                                            "Cancel", Toast.LENGTH_LONG)
                                            .show();
                                }
                            })
                    .setCanceledOnTouchOutside(true)
                    // You can change the message anytime.
                    // mMaterialDialog.setTitle("提示");
                    .setOnDismissListener(
                            new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    Toast.makeText(MainActivit.this,
                                            "onDismiss",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .show();
            // You can change the message anytime.
            // mMaterialDialog.setMessage("嗨！这是一个 MaterialDialog. 它非常方便使用，你只需将它实例化，这个美观的对话框便会自动地显示出来。它简洁小巧，完全遵照 Google 2014 年发布的 Material Design 风格，希望你能喜欢它！^ ^");

    }


    static int i = 0;


    public void setView(View v) {
        switch (v.getId()) {
            case R.id.button_set_view: {
                mMaterialDialog = new MaterialDialog(this);
                View view = LayoutInflater.from(this)
                        .inflate(R.layout.progressbar_item,
                                null);
                mMaterialDialog.setCanceledOnTouchOutside(true);
                mMaterialDialog.setView(view).show();
            }
            break;
            case R.id.button_set_background: {
                mMaterialDialog = new MaterialDialog(this);
                if (mMaterialDialog != null) {
                    if (i % 2 != 0) {
                        mMaterialDialog.setBackgroundResource(
                                R.drawable.background);
                    } else {
                        Resources res = getResources();
                        Bitmap bmp = BitmapFactory.decodeResource(res,
                                R.drawable.background2);
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(
                                getResources(), bmp);
                        mMaterialDialog.setBackground(bitmapDrawable);
                    }
                    mMaterialDialog.setCanceledOnTouchOutside(true).show();
                    i++;
                    Toast.makeText(getApplicationContext(),
                            "Try to click again~", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "You should init firstly!", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            }
            case R.id.button_set_contentView: {
                final ArrayAdapter<String> arrayAdapter
                        = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1);
                for (int j = 0; j < 38; j++) {
                    arrayAdapter.add("This is item " + j);
                }

                ListView listView = new ListView(this);
                listView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (8 * scale + 0.5f);
                listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
                listView.setDividerHeight(0);
                listView.setAdapter(arrayAdapter);

                final MaterialDialog alert = new MaterialDialog(this).setTitle(
                        "MaterialDialog").setContentView(listView);

                alert.setPositiveButton("OK", new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                alert.show();
                break;
            }
            case R.id.button_set_contentViewById: {
                final MaterialDialog alert = new MaterialDialog(this).setTitle(
                        "MaterialDialog")
                        .setContentView(
                                R.layout.input_view);

                alert.setPositiveButton("OK", new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        alert.dismiss();
                    }
                });
                alert.show();
                break;
            }
            case R.id.button_set_notitile: {
                final MaterialDialog materialDialog = new MaterialDialog(this);
                //materialDialog.setMessage("This is a dialog without title. This is a dialog without title. This is a dialog without title. This is a dialog without title. This is a dialog without title. ")
                materialDialog.setMessage(
                        "This is a dialog without title. This is a dialog without title. This is a dialog without title. " +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                "This is a dialog without title. This is a dialog without title." +
                                " ")
                        .setPositiveButton(android.R.string.yes,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        materialDialog.dismiss();
                                    }
                                });
                materialDialog.show();
            }
        }
    }


    public void buttonPress(View view) {
        // show imm
        InputMethodManager imm = (InputMethodManager) this.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
