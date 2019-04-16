package com.rdm.android.learningwithnationalparks.utils;

import android.util.Log;
import android.widget.PopupMenu;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Rebecca McBath
 * on 4/15/19.
 */
public class Utils {
	private static final String LOG_TAG = Utils.class.getSimpleName();

	// enable icon for popup menu by using Java reflection to call a hidden method
	public static void forceIconsShown(PopupMenu popupMenu) {
		try {
			Field[] fields = popupMenu.getClass().getDeclaredFields();
			for (Field field : fields) {
				if ("mPopup".equals(field.getName())) {
					field.setAccessible(true);
					Object menuPopupHelper = field.get(popupMenu);
					Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
					Method setForceIcons = classPopupHelper.getMethod(
							"setForceShowIcon", boolean.class);
					setForceIcons.invoke(menuPopupHelper, true);
					break;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
