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

public class Activity_Signup_User extends Activity {

	Context context;
	Button upload_image, submit;
	ImageView user_imageView;
	TextView textView_signup;
	EditText user_name, first_name, last_name, email, password,
			confirm_password;
	String User_Name, First_Name, Last_Name, Email, Password, Confirm_Password;
	private static int RESULT_LOAD_IMAGE = 1;
	Uri capturedImageUri;
	protected static final int CAMERA_RESULT = 0;
	private static final int CAMERA_REQUEST = 0;
	public static String picturePath;
	Bitmap targetBitmap, bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_user);
		textView_signup = (TextView) findViewById(R.id.textView_signup);
		user_imageView = (ImageView) findViewById(R.id.user_imageView);
		upload_image = (Button) findViewById(R.id.button_upload_image);
		submit = (Button) findViewById(R.id.button_Submit);
		upload_image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						Activity_Signup_User.this);

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
								Intent image = new Intent(
										android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
								image.putExtra(MediaStore.EXTRA_OUTPUT,
										capturedImageUri);
								startActivityForResult(image, CAMERA_RESULT);

							}
						});

				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("From Gallary",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to invoke NO event
								Intent image = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(image, RESULT_LOAD_IMAGE);
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

				user_name = (EditText) findViewById(R.id.edit_user_name);
				first_name = (EditText) findViewById(R.id.edit_first_name);
				last_name = (EditText) findViewById(R.id.edit_last_name);
				email = (EditText) findViewById(R.id.edit_email);
				password = (EditText) findViewById(R.id.edit_password);
				confirm_password = (EditText) findViewById(R.id.edit_Confirm_password);

				// get input from user
				User_Name = user_name.getText().toString().trim();
				First_Name = first_name.getText().toString().trim();
				Last_Name = last_name.getText().toString().trim();
				Email = email.getText().toString().trim();
				Password = password.getText().toString().trim();
				Confirm_Password = confirm_password.getText().toString().trim();
				Log.d("User_Name", "" + User_Name);
				if (User_Name.length() == 0 || First_Name.length() == 0
						|| Last_Name.length() == 0 || Email.length() == 0
						|| Password.length() == 0
						|| Confirm_Password.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"All fields are mandatory", Toast.LENGTH_SHORT)
							.show();
				} else if (Email.length() > 0 && !checkEmail(Email)) {
					Toast.makeText(getApplicationContext(), "Invalid Email",
							Toast.LENGTH_SHORT).show();
				} else if (User_Name.length() < 4) {
					Toast.makeText(getApplicationContext(),
							"Username to short", Toast.LENGTH_SHORT).show();
				} else if (!Confirm_Password.equals(Password)) {
					Toast.makeText(getApplicationContext(),
							"Password does not matched", Toast.LENGTH_SHORT)
							.show();
				} else if (username()) {

				} else if (email()) {

				} else {
					Datasource_Handler mdatabase = new Datasource_Handler(
							getApplicationContext());
					mdatabase.open();
					mdatabase.insert_details_user(User_Name, First_Name,
							Last_Name, Email, Password, Confirm_Password,
							picturePath);
					mdatabase.close();
					Intent it = new Intent(Activity_Signup_User.this,
							Activity_Login_Class.class);
					startActivity(it);
				}
			}
		});

		textView_signup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(Activity_Signup_User.this,
						Activity_Login_Class.class);
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
		// Log.d("mCursor", "" + mCursor.getCount());
		if (mCursor.getCount() > 0) {
			Toast.makeText(getApplicationContext(), "Email already exists",
					Toast.LENGTH_SHORT).show();
			mCursor.moveToNext();

			return true;
		}
		mCursor.close();

		mdatabase.close();
		return false;
	}

	public boolean username() {
		Datasource_Handler mdatabase = new Datasource_Handler(
				getApplicationContext());
		mdatabase.open();
		Cursor mCursor = mdatabase.check_all_user_name(User_Name);
		Log.d("User_Name", "" + User_Name);

		mCursor.moveToFirst();
		Log.d("mCursor", "" + mCursor.getCount());
		if (mCursor.getCount() > 0) {
			Toast.makeText(getApplicationContext(), "Username already exists",
					Toast.LENGTH_SHORT).show();
			mCursor.moveToNext();

			return true;
		}
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
			// Bitmap photo = (Bitmap) data.getExtras().get("data");
			// imageView.setImageBitmap(photo);
			try {
				bitmap = MediaStore.Images.Media.getBitmap(
						getApplicationContext().getContentResolver(),
						capturedImageUri);
				getRoundedShape(bitmap);
				user_imageView.setImageBitmap(targetBitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			picturePath = capturedImageUri.toString();
			picturePath = picturePath.replace("file://", "");
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
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}

}
