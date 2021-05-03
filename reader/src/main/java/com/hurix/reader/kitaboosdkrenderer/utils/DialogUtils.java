package com.hurix.reader.kitaboosdkrenderer.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.constants.Constants;
import com.hurix.reader.kitaboosdkrenderer.listeners.OnDialogOkActionListner;
import com.hurix.reader.kitaboosdkrenderer.listeners.OnDialogYesNoActionListner;


public class DialogUtils {

    public static Dialog alertDialog = null;

    public static Dialog getCustomDialog() {
        return alertDialog;
    }



	public static void showCancelDeleteAlert(final Object refObj,
											 Context context,
											 String title,
											 String message,
											 String acceptTxt, String declineTxt, final OnDialogYesNoActionListner listner) {
		if (!((Activity) context).isFinishing()) {
			if (alertDialog != null && alertDialog.isShowing()) {
				try {
					alertDialog.dismiss();
					alertDialog = null;
				} catch (Exception e) {
					// To avoid App getting crash when simultaneously click on back to bookshelf and Download or Archive.
					e.printStackTrace();
				}
			}

			if (context != null && !((Activity) context).isFinishing()) {


				androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, android.R.style.Theme_Holo_Light_Dialog);
				//builder.setTitle(title).setMessage(message);
				builder.setTitle(Html.fromHtml("<font color='#000000'>" + title + "</font>")).setMessage(message);

				//	builder.setIcon(context.getResources().getDrawable(R.drawable.questionmark));

				int accept, decline;
                accept = R.string.dialog_yes_button;
                decline = R.string.dialog_no_button;
				// Add the buttons
				builder.setPositiveButton(acceptTxt, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (listner != null) {
							alertDialog.dismiss();
							listner.onPostiveClick(refObj);

						}
					}
				});
				builder.setNegativeButton(declineTxt, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (listner != null) {
							alertDialog.dismiss();
							listner.onNegativeClick(refObj);

						}
					}
				});

				// Create the AlertDialog
				int width = (int) context.getResources().getDimension(R.dimen.alertdialog_delete_book_width);
				int height = (int) context.getResources().getDimension(R.dimen.alertdialog_delete_book_height);
				alertDialog = builder.create();
				//alertDialog.getWindow().setLayout(600, 400);
				alertDialog.setCancelable(false);
				alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alertDialog.show();
				((androidx.appcompat.app.AlertDialog)alertDialog).getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#000000"));
				((androidx.appcompat.app.AlertDialog)alertDialog).getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#000000"));
				alertDialog.getWindow().setLayout(width, height);

			}
		}

	}


	public static void showYesNoAlert(final Object refObj, Context context, String title, String message, final OnDialogYesNoActionListner listner) {
        if (alertDialog != null && alertDialog.isShowing()) {
            try {
                alertDialog.dismiss();
                alertDialog = null;
            } catch (Exception e) {
                // To avoid App getting crash when simultaneously click on back to bookshelf and Download or Archive.
                e.printStackTrace();
            }
        }

        if (context != null && !((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.CustomActionBarThemeWithActionBar_White));

            builder.setTitle(title).setMessage(message);

            int accept, decline;
            accept = R.string.dialog_yes_button;
            decline = R.string.dialog_no_button;

            // Add the buttons
            builder.setPositiveButton(accept, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (listner != null) {
                        alertDialog.dismiss();
                        listner.onPostiveClick(refObj);

                    }
                }
            });
            builder.setNegativeButton(decline, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (listner != null) {
                        alertDialog.dismiss();
                        listner.onNegativeClick(refObj);

                    }
                }
            });

            // Create the AlertDialog
            alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            int dividerId = builder.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = alertDialog.findViewById(dividerId);
            divider.setBackgroundColor(context.getResources().getColor(R.color.kitaboo_main_color));
        }

    }


    /***********************************************************************
     * @ Purpose : This methods are used to show Alert with Ok button
     ***********************************************************************/
    public static void showOKAlert(final View view, int Tag, Context context, String title, String message, final OnDialogOkActionListner listner) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                Activity activity = alertDialog.getOwnerActivity();
                if (activity != null && !activity.isFinishing()) {
                    alertDialog.dismiss();
                }
                alertDialog.dismiss();
            }
            alertDialog = null;
        }
        if (context != null && !((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.CustomActionBarThemeWithActionBar_White));

            builder.setTitle(title).setMessage(message);

            builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    alertDialog.dismiss();
                    if (listner != null) {
                        listner.onOKClick(view);

                    }
                }
            });
            alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            int dividerId = builder.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = alertDialog.findViewById(dividerId);
            divider.setBackgroundColor(context.getResources().getColor(R.color.kitaboo_main_color));
        }
    }

    /***********************************************************************
     * @ Purpose : This methods are used to show Alert with Ok button for custom error Handling
     ***********************************************************************/
    public static void showAlert(final View view, int Tag, Context context, String title, String message, final OnDialogOkActionListner listner) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
            alertDialog = null;
        }
        if (context != null && !((Activity) context).isFinishing()) {
            String link = "<a href=\"https:\\support.kitaboo.com\">here</a>";
            Spanned myMessage = Html.fromHtml(message + " " + link + " " );
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.CustomActionBarThemeWithActionBar_White));

            builder.setTitle(title).setMessage(myMessage);

            builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    alertDialog.dismiss();
                    if (listner != null) {
                        listner.onOKClick(view);

                    }
                }
            });
            alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            TextView msgTxt = (TextView) alertDialog.findViewById(android.R.id.message);
            msgTxt.setMovementMethod(LinkMovementMethod.getInstance());
            int dividerId = builder.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = alertDialog.findViewById(dividerId);
            divider.setBackgroundColor(context.getResources().getColor(R.color.kitaboo_main_color));
        }
    }


    /***********************************************************************
     * @ Purpose : This methods are used to Show Custom Toasts.
     ***********************************************************************/
    public static void displayToast(Context context, String msg, int toastType, int gravity) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast, null);

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);

        Toast toast = new Toast(context);
        toast.setDuration(toastType);
        toast.setGravity(gravity, 0, 0);
        toast.setView(layout);
        toast.show();
    }

    public static void displayToastAtCustomizePosition(Context context, String msg, int toastType, int gravity, int leftPos, int bottomPos) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast, null);

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);

        Toast toast = new Toast(context);
        toast.setDuration(toastType);
        toast.setGravity(gravity, leftPos, bottomPos);
        toast.setView(layout);
        toast.show();
    }

    public static void showOKCancelAlert(final Object refObj,
                                         Context context,
                                         String title,
                                         String message,
                                         String acceptTxt, String declineTxt, final OnDialogYesNoActionListner listner) {
        if (!((Activity) context).isFinishing()) {
            if (alertDialog != null && alertDialog.isShowing()) {
                try {
                    alertDialog.dismiss();
                    alertDialog = null;
                } catch (Exception e) {
                    // To avoid App getting crash when simultaneously click on back to bookshelf and Download or Archive.
                    e.printStackTrace();
                }
            }

            if (context != null && !((Activity) context).isFinishing()) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, android.R.style.Theme_Holo_Light_Dialog);
                //builder.setTitle(title).setMessage(message);
                builder.setTitle(Html.fromHtml("<font color='#000000'>" + title + "</font>")).setMessage(message);
                //	builder.setIcon(context.getResources().getDrawable(R.drawable.questionmark));

                int accept, decline;
                accept = R.string.dialog_yes_button;
                decline = R.string.dialog_no_button;

                // Add the buttons
                builder.setPositiveButton(acceptTxt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (listner != null) {
                            alertDialog.dismiss();
                            listner.onPostiveClick(refObj);

                        }
                    }
                });
                builder.setNegativeButton(declineTxt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (listner != null) {
                            alertDialog.dismiss();
                            listner.onNegativeClick(refObj);

                        }
                    }
                });

                // Create the AlertDialog
                alertDialog = builder.create();
                //alertDialog.getWindow().setLayout(600, 400);
                alertDialog.setCancelable(false);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.show();
                ((androidx.appcompat.app.AlertDialog) alertDialog).getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.kitaboo_main_color));
                ((androidx.appcompat.app.AlertDialog) alertDialog).getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#000000"));
            }
        }

    }

}
