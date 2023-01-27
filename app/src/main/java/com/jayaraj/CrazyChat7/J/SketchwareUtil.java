package com.jayaraj.CrazyChat7.J;

import android.app.*;
import android.content.*;
import android.graphics.drawable.*;
import android.net.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;

import java.io.*;
import java.util.*;

public class SketchwareUtil {

    public static int TOP = 1;
    public static int CENTER = 2;
    public static int BOTTOM = 3;

    public static void CustomToast(final Context _context, final String _message, final int _textColor, final int _textSize, final int _bgColor, final int _radius, final int _gravity) {
        final Toast _toast = Toast.makeText(_context, _message, Toast.LENGTH_SHORT);
        final View _view = _toast.getView();
        final TextView _textView = _view.findViewById(android.R.id.message);
        _textView.setTextSize(_textSize);
        _textView.setTextColor(_textColor);
        _textView.setGravity(Gravity.CENTER);

        final GradientDrawable _gradientDrawable = new GradientDrawable();
        _gradientDrawable.setColor(_bgColor);
        _gradientDrawable.setCornerRadius(_radius);
        _view.setBackgroundDrawable(_gradientDrawable);
        _view.setPadding(15, 10, 15, 10);
        _view.setElevation(10);

        switch (_gravity) {
            case 1:
                _toast.setGravity(Gravity.TOP, 0, 150);
                break;

            case 2:
                _toast.setGravity(Gravity.CENTER, 0, 0);
                break;

            case 3:
                _toast.setGravity(Gravity.BOTTOM, 0, 150);
                break;
        }
        _toast.show();
    }

    public static void CustomToastWithIcon(final Context _context, final String _message, final int _textColor, final int _textSize, final int _bgColor, final int _radius, final int _gravity, final int _icon) {
        final Toast _toast = Toast.makeText(_context, _message, Toast.LENGTH_SHORT);
        final View _view = _toast.getView();
        final TextView _textView = _view.findViewById(android.R.id.message);
        _textView.setTextSize(_textSize);
        _textView.setTextColor(_textColor);
        _textView.setCompoundDrawablesWithIntrinsicBounds(_icon, 0, 0, 0);
        _textView.setGravity(Gravity.CENTER);
        _textView.setCompoundDrawablePadding(10);

        final GradientDrawable _gradientDrawable = new GradientDrawable();
        _gradientDrawable.setColor(_bgColor);
        _gradientDrawable.setCornerRadius(_radius);
        _view.setBackgroundDrawable(_gradientDrawable);
        _view.setPadding(10, 10, 10, 10);
        _view.setElevation(10);

        switch (_gravity) {
            case 1:
                _toast.setGravity(Gravity.TOP, 0, 150);
                break;

            case 2:
                _toast.setGravity(Gravity.CENTER, 0, 0);
                break;

            case 3:
                _toast.setGravity(Gravity.BOTTOM, 0, 150);
                break;
        }
        _toast.show();
    }

    public static void sortListMap(ArrayList<HashMap<String, Object>> listMap, String key, boolean isNumber, boolean ascending) {
        Collections.sort(listMap, new Comparator<HashMap<String, Object>>() {
            public int compare(final HashMap<String, Object> _compareMap1, final HashMap<String, Object> _compareMap2) {
                if (isNumber) {
                    final int _count1 = Integer.parseInt(_compareMap1.get(key).toString());
                    final int _count2 = Integer.parseInt(_compareMap2.get(key).toString());
                    if (ascending) {
                        return _count1 < _count2 ? -1 : _count1 < _count2 ? 1 : 0;
                    } else {
                        return _count1 > _count2 ? -1 : _count1 > _count2 ? 1 : 0;
                    }
                } else {
                    if (ascending) {
                        return (Objects.requireNonNull(_compareMap1.get(key)).toString()).compareTo(Objects.requireNonNull(_compareMap2.get(key)).toString());
                    } else {
                        return (Objects.requireNonNull(_compareMap2.get(key)).toString()).compareTo(Objects.requireNonNull(_compareMap1.get(key)).toString());
                    }
                }
            }
        });
    }

    public static void CropImage(final Activity _activity, final String _path, final int _requestCode) {
        try {
            final Intent _intent = new Intent("com.android.camera.action.CROP");
            final File _file = new File(_path);
            final Uri _contentUri = Uri.fromFile(_file);
            _intent.setDataAndType(_contentUri, "image/*");
            _intent.putExtra("crop", "true");
            _intent.putExtra("aspectX", 1);
            _intent.putExtra("aspectY", 1);
            _intent.putExtra("outputX", 280);
            _intent.putExtra("outputY", 280);
            _intent.putExtra("return-data", false);
            _activity.startActivityForResult(_intent, _requestCode);
        } catch (final ActivityNotFoundException _e) {
            Toast.makeText(_activity, "Your device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnected(final Context _context) {
        final ConnectivityManager _connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo _activeNetworkInfo = _connectivityManager.getActiveNetworkInfo();
        return _activeNetworkInfo != null && _activeNetworkInfo.isConnected();
    }

    public static String copyFromInputStream(final InputStream _inputStream) {
        final ByteArrayOutputStream _outputStream = new ByteArrayOutputStream();
        final byte[] _buf = new byte[1024];
        int _i;
        try {
            while ((_i = _inputStream.read(_buf)) != -1) {
                _outputStream.write(_buf, 0, _i);
            }
            _outputStream.close();
            _inputStream.close();
        } catch (final IOException ignored) {
        }

        return _outputStream.toString();
    }

    public static void hideKeyboard(final Context _context) {
        final InputMethodManager _inputMethodManager = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
        _inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static void showKeyboard(final Context _context) {
        final InputMethodManager _inputMethodManager = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
        _inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void showMessage(final Context _context, final String _s) {
        Toast.makeText(_context, _s, Toast.LENGTH_SHORT).show();
    }

    public static int getLocationX(final View _view) {
        final int[] _location = new int[2];
        _view.getLocationInWindow(_location);
        return _location[0];
    }

    public static int getLocationY(final View _view) {
        final int[] _location = new int[2];
        _view.getLocationInWindow(_location);
        return _location[1];
    }

    public static int getRandom(final int _min, final int _max) {
        final Random random = new Random();
        return random.nextInt(_max - _min + 1) + _min;
    }

    public static ArrayList<Double> getCheckedItemPositionsToArray(final ListView _list) {
        final ArrayList<Double> _result = new ArrayList<Double>();
        final SparseBooleanArray _arr = _list.getCheckedItemPositions();
        for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
            if (_arr.valueAt(_iIdx))
                _result.add((double) _arr.keyAt(_iIdx));
        }
        return _result;
    }

    public static float getDip(final Context _context, final int _input) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, _context.getResources().getDisplayMetrics());
    }

    public static int getDisplayWidthPixels(final Context _context) {
        return _context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDisplayHeightPixels(final Context _context) {
        return _context.getResources().getDisplayMetrics().heightPixels;
    }

    public static void getAllKeysFromMap(final Map<String, Object> _map, final ArrayList<String> _output) {
        if (_output == null) return;
        _output.clear();
        if (_map == null || _map.size() < 1) return;
        final Iterator _itr = _map.entrySet().iterator();
        while (_itr.hasNext()) {
            final Map.Entry<String, String> _entry = (Map.Entry) _itr.next();
            _output.add(_entry.getKey());
        }
    }
}
