package com.social.gamerpoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Pattern;

import Data.databased.Datasource_Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Edit_User_Profile extends Activity {

	Context context;
	Button upload_image, submit;
	TextView textView_signup;
	ImageView user_imageView;
	EditText user_name, first_name, last_name, email, current_password,
			password, confirm_password;
	String User_Name, First_Name, Last_Name, Email, Current_Password, Password,
			old_Password, Confirm_Password, old_Confirm_Password;
	private static int RESULT_LOAD_IMAGE = 1;
	Uri capturedImageUri;
	protected static final int CAMERA_RESULT = 0;
	private static final int CAMERA_REQUEST = 0;
	final int CAMERA_PICTURE = 1;
	private final int GALLERY_PICTURE = 2;
	public static String picturePath;
	Bitmap bitmap, targetBitmap;
	private Intent pictureActionIntent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_user_profile);
		Log.d("on_create_call", "on_create_call");
		Intent it = getIntent();
		Email = it.getStringExtra("Email");
		Password = it.getStringExtra("Password");
		textView_signup = (TextView) findViewById(R.id.textView_signup);
		user_name = (EditText) findViewById(R.id.edit_user_name);
		first_name = (EditText) findViewById(R.id.edit_first_name);
		last_name = (EditText) findViewById(R.id.edit_last_name);
		email = (EditText) findViewById(R.id.edit_email);
		current_password = (EditText) findViewById(R.id.edit_current_password);
		password = (EditText) findViewById(R.id.edit_password);
		confirm_password = (EditText) findViewById(R.id.edit_Confirm_password);
		user_imageView = (ImageView) findViewById(R.id.user_imageView);
		email.setEnabled(false);

		Datasource_Handler mdatabase = new Datasource_Handler(
				getApplicationContext());
		mdatabase.open();
		final Cursor mCursor = mdatabase.check_all_Data(Email, Password);

		mCursor.moveToFirst();
		Log.d("mCursor", "" + mCursor.getCount());
		if (mCursor.getCount() > 0) {
			User_Name = mCursor.getString(0);
			First_Name = mCursor.getString(1);
			Last_Name = mCursor.getString(2);
			Email = mCursor.getString(3);
			old_Password = mCursor.getString(4);
			old_Confirm_Password = mCursor.getString(5);
			picturePath = mCursor.getString(6);
			Log.d("picturePath", "" + picturePath);
		}
		mCursor.moveToNext();
		mCursor.close();

		mdatabase.close();
		user_name.setText(User_Name);
		first_name.setText(First_Name);
		last_name.setText(Last_Name);
		email.setText(Email);
		BitmapFactory.Options optsDownSample = new BitmapFactory.Options();
		optsDownSample.inSampleSize = 3;
		bitmap = BitmapFactory.decodeFile(picturePath, optsDownSample);
		if (bitmap != null) {
			getRoundedShape(bitmap);
			user_imageView.setImageBitmap(targetBitmap);
		}
		upload_image = (Button) findViewById(R.id.button_upload_image);
		submit = (Button) findViewById(R.id.button_Submit);
		upload_image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						Activity_Edit_User_Profile.this);

				// Setting Dialog Title
				alertDialog.setTitle("Upload Image");

				// Setting Dialog Message
				alertDialog.setMessage("How do you want to upload image ?");

				// Setting Icon to Dialog
				alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("Using Camera",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								// Write your code here to invoke YES event
								Calendar cal = Calendar.getInstance();
								File file = new File(Environment
										.getExternalStorageDirectory(), (cal
										.getTimeInMillis() + ".jpg"));
								if (!file.exists()) {
									try {
										file.createNewFile();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} else {
									file.delete();
									try {
										file.createNewFile();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

								capturedImageUri = Uri.fromFile(file);
								pictureActionIntent = new Intent(
										android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
								pictureActionIntent.putExtra(
										MediaStore.EXTRA_OUTPUT,
										capturedImageUri);
								startActivityForResult(pictureActionIntent,
										CAMERA_RESULT);

							}
						});

				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("From Gallary",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to invoke NO event
								pictureActionIntent = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(pictureActionIntent,
										RESULT_LOAD_IMAGE);
								dialog.cancel();

							}
						});

				// Showing Alert Message
				alertDialog.show();
			}
		});

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// get input from user
				First_Name = first_name.getText().toString();
				Last_Name = last_name.getText().toString();
				Email = email.getText().toString();
				Password = password.getText().toString();
				Confirm_Password = confirm_password.getText().toString();
				Current_Password = current_password.getText().toString();

				if (Email.length() > 0 && !checkEmail(Email)) {
					Toast.makeText(getApplicationContext(), "Invalid Email",
							Toast.LENGTH_SHORT).show();
				} else if (First_Name.length() == 0 || Last_Name.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"All fields mandatory", Toast.LENGTH_SHORT).show();
				} else if (!Confirm_Password.equals(Password)) {
					Toast.makeText(getApplicationContext(),
							"Password does not matched", Toast.LENGTH_SHORT)
							.show();
				} else {
					if (Current_Password.length() > 0) {
						if (!Current_Password.equals(old_Password)) {
							Toast.makeText(getApplicationContext(),
									"Entered wrong password",
									Toast.LENGTH_SHORT).show();
						} else {
							Password = password.getText().toString();
							Confirm_Password = confirm_password.getText()
									.toString();
							Datasource_Handler mdatabase = new Datasource_Handler(
									getApplicationContext());
							mdatabase.open();
							mdatabase.update_details_user(First_Name,
									Last_Name, Email, Password,
									Confirm_Password, picturePath);
							mdatabase.close();
						}
					} else {
						Password = old_Password;
						Confirm_Password = old_Confirm_Password;
						Datasource_Handler mdatabase = new Datasource_Handler(
								getApplicationContext());
						mdatabase.open();
						mdatabase.update_details_user(First_Name, Last_Name,
								Email, Password, Confirm_Password, picturePath);
						mdatabase.close();
						Intent it = new Intent(Activity_Edit_User_Profile.this,
								Activity_MainHome_Activity.class);
						it.putExtra("Email", "" + Email);
						it.putExtra("Password", "" + Password);
						startActivity(it);
						finish();
					}

				}
			}
		});

		textView_signup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(Activity_Edit_User_Profile.this,
						Activity_MainHome_Activity.class);
				it.putExtra("Email", "" + Email);
				it.putExtra("Password", "" + Password);
				startActivity(it);
				finish();
			}
		});
	}

	public boolean email() {
		Datasource_Handler mdatabase = new Datasource_Handler(
				getApplicationContext());
		mdatabase.open();
		Cursor mCursor = mdatabase.check_all_email(Email);

		mCursor.moveToFirst();
		Log.d("mCursor", "" + mCursor.getCount());
		if (mCursor.getCount() > 0) {
			Toast.makeText(getApplicationContext(), "Email already exists",
					Toast.LENGTH_SHORT).show();
			return true;
		}
		mCursor.moveToNext();
		mCursor.close();

		mdatabase.close();
		return false;
	}

	private final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(

	"^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
			+ "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
			+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
			+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
			+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
			+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$");

	private boolean checkEmail(String Email) {
		// TODO Auto-generated method stub
		return EMAIL_ADDRESS_PATTERN.matcher(Email).matches();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAMERA_REQUEST) {
			if (resultCode == RESULT_OK && null != data) {
				try {
					bitmap = MediaStore.Images.Media.getBitmap(
							getApplicationContext().getContentResolver(),
							capturedImageUri);
					getRoundedShape(bitmap);
					Log.e("", "enter bitmap");
					user_imageView.setImageBitmap(targetBitmap);
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
				picturePath = capturedImageUri.toString();

				picturePath = picturePath.replace("file://", "");
				Log.e("", "picture path");

			}

		}

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			Log.e("picturePath", "" + picturePath);
			bitmap = BitmapFactory.decodeFile(picturePath);
			getRoundedShape(bitmap);
			user_imageView.setImageBitmap(targetBitmap);

		}
		Log.d("picturePath", "" + picturePath);
	}

	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		// TODO Auto-generated method stub
		int targetWidth = 200;
		int targetHeight = 200;
		targetBitmap = Bitmap.createBitmap(targetWidth,

		targetHeight, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Log.e("path===========", "" + path);
		Log.e("scaleBitmapImage===========", "" + scaleBitmapImage);
		Bitmap sourceBitmap = scaleBitmapImage;
		Log.e("sourceBitmap===========", "" + sourceBitmap);
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}

}
