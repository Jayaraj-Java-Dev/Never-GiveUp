package com.jayaraj.CrazyChat7.J;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileUtil {

	private static void createNewFile(final String path) {
		final int lastSep = path.lastIndexOf(File.separator);
		if (lastSep > 0) {
			final String dirPath = path.substring(0, lastSep);
			FileUtil.makeDir(dirPath);
		}

		final File file = new File(path);

		try {
			if (!file.exists())
				file.createNewFile();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static String readFile(final String path) {
		FileUtil.createNewFile(path);

		final StringBuilder sb = new StringBuilder();
		FileReader fr = null;
		try {
			fr = new FileReader(new File(path));

			final char[] buff = new char[1024];
			int length = 0;

			while ((length = fr.read(buff)) > 0) {
				sb.append(new String(buff, 0, length));
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();
	}

	public static void writeFile(final String path, final String str) {
		FileUtil.createNewFile(path);
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(new File(path), false);
			fileWriter.write(str);
			fileWriter.flush();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileWriter != null)
					fileWriter.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void copyFile(final String sourcePath, final String destPath) {
		if (!FileUtil.isExistFile(sourcePath)) return;
		FileUtil.createNewFile(destPath);

		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(sourcePath);
			fos = new FileOutputStream(destPath, false);

			final byte[] buff = new byte[1024];
			int length = 0;

			while ((length = fis.read(buff)) > 0) {
				fos.write(buff, 0, length);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void moveFile(final String sourcePath, final String destPath) {
		FileUtil.copyFile(sourcePath, destPath);
		FileUtil.deleteFile(sourcePath);
	}

	public static void deleteFile(final String path) {
		final File file = new File(path);

		if (!file.exists()) return;

		if (file.isFile()) {
			file.delete();
			return;
		}

		final File[] fileArr = file.listFiles();

		if (fileArr != null) {
			for (final File subFile : fileArr) {
				if (subFile.isDirectory()) {
					FileUtil.deleteFile(subFile.getAbsolutePath());
				}

				if (subFile.isFile()) {
					subFile.delete();
				}
			}
		}

		file.delete();
	}

	public static boolean isExistFile(final String path) {
		final File file = new File(path);
		return file.exists();
	}

	public static void makeDir(final String path) {
		if (!FileUtil.isExistFile(path)) {
			final File file = new File(path);
			file.mkdirs();
		}
	}

	public static void listDir(final String path, final ArrayList<String> list) {
		final File dir = new File(path);
		if (!dir.exists() || dir.isFile()) return;

		final File[] listFiles = dir.listFiles();
		if (listFiles == null || listFiles.length <= 0) return;

		if (list == null) return;
		list.clear();
		for (final File file : listFiles) {
			list.add(file.getAbsolutePath());
		}
	}

	public static boolean isDirectory(final String path) {
		if (!FileUtil.isExistFile(path)) return false;
		return new File(path).isDirectory();
	}

	public static boolean isFile(final String path) {
		if (!FileUtil.isExistFile(path)) return false;
		return new File(path).isFile();
	}

	public static long getFileLength(final String path) {
		if (!FileUtil.isExistFile(path)) return 0;
		return new File(path).length();
	}

	public static String getExternalStorageDir() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public static String getPackageDataDir(final Context context) {
		return context.getExternalFilesDir(null).getAbsolutePath();
	}

	public static String getPublicDir(final String type) {
		return Environment.getExternalStoragePublicDirectory(type).getAbsolutePath();
	}

	public static String convertUriToFilePath(Context context, Uri uri) {
		String path = null;
		if (DocumentsContract.isDocumentUri(context, uri)) {
			if (FileUtil.isExternalStorageDocument(uri)) {
				String docId = DocumentsContract.getDocumentId(uri);
				String[] split = docId.split(":");
				String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					path = Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			} else if (FileUtil.isDownloadsDocument(uri)) {
				String id = DocumentsContract.getDocumentId(uri);

				if (!TextUtils.isEmpty(id)) {
					if (id.startsWith("raw:")) {
						return id.replaceFirst("raw:", "");
					}
				}

				Uri contentUri = ContentUris
						.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				path = FileUtil.getDataColumn(context, contentUri, null, null);
			} else if (FileUtil.isMediaDocument(uri)) {
				String docId = DocumentsContract.getDocumentId(uri);
				String[] split = docId.split(":");
				String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				String[] selectionArgs = {
						split[1]
				};

				path = FileUtil.getDataColumn(context, contentUri, selection, selectionArgs);
			}
		} else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
			path = FileUtil.getDataColumn(context, uri, null, null);
		} else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
			path = uri.getPath();
		}

		if (path != null) {
			try {
				return URLDecoder.decode(path, "UTF-8");
			}catch(final Exception e){
				return null;
			}
		}
		return null;
	}

	private static String getDataColumn(final Context context, final Uri uri, final String selection, final String[] selectionArgs) {
		Cursor cursor = null;

		final String column = MediaStore.MediaColumns.DATA;
		String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} catch (final Exception e) {

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}


	private static boolean isExternalStorageDocument(final Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	private static boolean isDownloadsDocument(final Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	private static boolean isMediaDocument(final Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	private static void saveBitmap(final Bitmap bitmap, final String destPath) {
		FileOutputStream out = null;
		createNewFile(destPath);
		try {
			out = new FileOutputStream(new File(destPath));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Bitmap getScaledBitmap(final String path, final int max) {
		final Bitmap src = BitmapFactory.decodeFile(path);

		int width = src.getWidth();
		int height = src.getHeight();
		float rate = 0.0f;

		if (width > height) {
			rate = max / (float) width;
			height = (int) (height * rate);
			width = max;
		} else {
			rate = max / (float) height;
			width = (int) (width * rate);
			height = max;
		}

		return Bitmap.createScaledBitmap(src, width, height, true);
	}

	public static int calculateInSampleSize(final BitmapFactory.Options options, final int reqWidth, final int reqHeight) {
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			int halfHeight = height / 2;
			int halfWidth = width / 2;

			while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampleBitmapFromPath(final String path, final int reqWidth, final int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = FileUtil.calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static void resizeBitmapFileRetainRatio(final String fromPath, final String destPath, final int max) {
		if (!FileUtil.isExistFile(fromPath)) return;
		final Bitmap bitmap = FileUtil.getScaledBitmap(fromPath, max);
		FileUtil.saveBitmap(bitmap, destPath);
	}

	public static void resizeBitmapFileToSquare(final String fromPath, final String destPath, final int max) {
		if (!FileUtil.isExistFile(fromPath)) return;
		final Bitmap src = BitmapFactory.decodeFile(fromPath);
		final Bitmap bitmap = Bitmap.createScaledBitmap(src, max, max, true);
		FileUtil.saveBitmap(bitmap, destPath);
	}

	public static void resizeBitmapFileToCircle(final String fromPath, final String destPath) {
		if (!FileUtil.isExistFile(fromPath)) return;
		final Bitmap src = BitmapFactory.decodeFile(fromPath);
		final Bitmap bitmap = Bitmap.createBitmap(src.getWidth(),
				src.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);

		final int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(src.getWidth() / 2, src.getHeight() / 2,
				src.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(src, rect, rect, paint);

		FileUtil.saveBitmap(bitmap, destPath);
	}

	public static void resizeBitmapFileWithRoundedBorder(final String fromPath, final String destPath, final int pixels) {
		if (!FileUtil.isExistFile(fromPath)) return;
		final Bitmap src = BitmapFactory.decodeFile(fromPath);
		final Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src
				.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);

		final int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());
		RectF rectF = new RectF(rect);
		float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(src, rect, rect, paint);

		FileUtil.saveBitmap(bitmap, destPath);
	}

	public static void cropBitmapFileFromCenter(final String fromPath, final String destPath, final int w, final int h) {
		if (!FileUtil.isExistFile(fromPath)) return;
		final Bitmap src = BitmapFactory.decodeFile(fromPath);

		final int width = src.getWidth();
		final int height = src.getHeight();

		if (width < w && height < h)
			return;

		int x = 0;
		int y = 0;

		if (width > w)
			x = (width - w) / 2;

		if (height > h)
			y = (height - h) / 2;

		int cw = w;
		int ch = h;

		if (w > width)
			cw = width;

		if (h > height)
			ch = height;

		final Bitmap bitmap = Bitmap.createBitmap(src, x, y, cw, ch);
		FileUtil.saveBitmap(bitmap, destPath);
	}

	public static void rotateBitmapFile(final String fromPath, final String destPath, final float angle) {
		if (!FileUtil.isExistFile(fromPath)) return;
		final Bitmap src = BitmapFactory.decodeFile(fromPath);
		final Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		final Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
		FileUtil.saveBitmap(bitmap, destPath);
	}

	public static void scaleBitmapFile(final String fromPath, final String destPath, final float x, final float y) {
		if (!FileUtil.isExistFile(fromPath)) return;
		final Bitmap src = BitmapFactory.decodeFile(fromPath);
		final Matrix matrix = new Matrix();
		matrix.postScale(x, y);

		final int w = src.getWidth();
		final int h = src.getHeight();

		final Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, w, h, matrix, true);
		FileUtil.saveBitmap(bitmap, destPath);
	}

	public static void skewBitmapFile(final String fromPath, final String destPath, final float x, final float y) {
		if (!FileUtil.isExistFile(fromPath)) return;
		final Bitmap src = BitmapFactory.decodeFile(fromPath);
		final Matrix matrix = new Matrix();
		matrix.postSkew(x, y);

		final int w = src.getWidth();
		final int h = src.getHeight();

		final Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, w, h, matrix, true);
		FileUtil.saveBitmap(bitmap, destPath);
	}

	public static void setBitmapFileColorFilter(final String fromPath, final String destPath, final int color) {
		if (!FileUtil.isExistFile(fromPath)) return;
		final Bitmap src = BitmapFactory.decodeFile(fromPath);
		final Bitmap bitmap = Bitmap.createBitmap(src, 0, 0,
				src.getWidth() - 1, src.getHeight() - 1);
		final Paint p = new Paint();
		final ColorFilter filter = new LightingColorFilter(color, 1);
		p.setColorFilter(filter);
		final Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(bitmap, 0, 0, p);
		FileUtil.saveBitmap(bitmap, destPath);
	}

	public static void setBitmapFileBrightness(final String fromPath, final String destPath, final float brightness) {
		if (!FileUtil.isExistFile(fromPath)) return;
		final Bitmap src = BitmapFactory.decodeFile(fromPath);
		final ColorMatrix cm = new ColorMatrix(new float[]
				{
						1, 0, 0, 0, brightness,
						0, 1, 0, 0, brightness,
						0, 0, 1, 0, brightness,
						0, 0, 0, 1, 0
				});

		final Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		final Canvas canvas = new Canvas(bitmap);
		final Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(src, 0, 0, paint);
		FileUtil.saveBitmap(bitmap, destPath);
	}

	public static void setBitmapFileContrast(final String fromPath, final String destPath, final float contrast) {
		if (!FileUtil.isExistFile(fromPath)) return;
		final Bitmap src = BitmapFactory.decodeFile(fromPath);
		final ColorMatrix cm = new ColorMatrix(new float[]
				{
						contrast, 0, 0, 0, 0,
						0, contrast, 0, 0, 0,
						0, 0, contrast, 0, 0,
						0, 0, 0, 1, 0
				});

		final Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		final Canvas canvas = new Canvas(bitmap);
		final Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(src, 0, 0, paint);

		FileUtil.saveBitmap(bitmap, destPath);
	}

	public static int getJpegRotate(final String filePath) {
		int rotate = 0;
		try {
			final ExifInterface exif = new ExifInterface(filePath);
			final int iOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

			switch (iOrientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotate = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotate = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotate = 270;
					break;
			}
		}
		catch (final IOException e) {
			return 0;
		}

		return rotate;
	}
	public static File createNewPictureFile(final Context context) {
		final SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_HHmmss");
		final String fileName = date.format(new Date()) + ".jpg";
		return new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + fileName);
	}
}
