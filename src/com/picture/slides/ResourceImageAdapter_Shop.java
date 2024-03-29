package com.picture.slides;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.social.gamerpoint.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

/**
 * This class is an adapter that provides images from a fixed set of resource
 * ids. Bitmaps and ImageViews are kept as weak references so that they can be
 * cleared by garbage collection when not needed.
 * 
 */
public class ResourceImageAdapter_Shop extends AbstractCoverFlowImageAdapter {

	/** The Constant TAG. */
	private static final String TAG = ResourceImageAdapter_Shop.class
			.getSimpleName();

	/** The Constant DEFAULT_LIST_SIZE. */
	private static final int DEFAULT_LIST_SIZE = 20;

	/** The Constant IMAGE_RESOURCE_IDS. */
	private static final List<Integer> IMAGE_RESOURCE_IDS = new ArrayList<Integer>(
			DEFAULT_LIST_SIZE);

	/** The Constant DEFAULT_RESOURCE_LIST. */
	private static final int[] DEFAULT_RESOURCE_LIST = { R.drawable.icon_shop,
			R.drawable.icon_shop1, R.drawable.icon_shop, R.drawable.icon_shop2,
			R.drawable.icon_shop };

	/** The bitmap map. */
	private final Map<Integer, WeakReference<Bitmap>> bitmapMap = new HashMap<Integer, WeakReference<Bitmap>>();

	private final Context context;

	/**
	 * Creates the adapter with default set of resource images.
	 * 
	 * @param context
	 *            context
	 */
	public ResourceImageAdapter_Shop(final Context context) {
		super();
		this.context = context;
		setResources(DEFAULT_RESOURCE_LIST);
	}

	/**
	 * Replaces resources with those specified.
	 * 
	 * @param resourceIds
	 *            array of ids of resources.
	 */
	public final synchronized void setResources(final int[] resourceIds) {
		IMAGE_RESOURCE_IDS.clear();
		for (final int resourceId : resourceIds) {
			IMAGE_RESOURCE_IDS.add(resourceId);
		}
		notifyDataSetChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public synchronized int getCount() {
		return IMAGE_RESOURCE_IDS.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.polidea.coverflow.AbstractCoverFlowImageAdapter#createBitmap(int)
	 */
	@Override
	protected Bitmap createBitmap(final int position) {
		Log.v(TAG, "creating item " + position);
		final Bitmap bitmap = ((BitmapDrawable) context.getResources()
				.getDrawable(IMAGE_RESOURCE_IDS.get(position))).getBitmap();
		bitmapMap.put(position, new WeakReference<Bitmap>(bitmap));
		return bitmap;
	}
}