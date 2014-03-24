/*
 * Copyright 2014 Johan Olsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.krg.ParallaxScrollView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


public class ParallaxScrollView extends FrameLayout implements ObservableScrollView.Callbacks {

    /**
     * The values of BACKGROUND_VIEW_ID and CONTENT_VIEW_ID will become
     * the effective resource ids of the background and content views
     * when building the ParallaxScrollView in code. It is necessary for
     * these views to have ids when arranging the views in a RelativeLayout
     * in {@link ParallaxScrollView#createParallaxScrollView()}.These
     * numbers are totally arbitrary (I like the number 24) and only need
     * to be unique within this view.
     */
    private static final int    BACKGROUND_VIEW_ID = 24;
    private static final int    CONTENT_VIEW_ID = 0x24;

    /**
     * The default scroll factor; if not changed, the background view will scroll
     * in parallax at half the speed of the content view.
     */
    private static final float  DEFAULT_SCROLL_FACTOR = 0.5f;

    private Context                 mContext;
    private float                   mScrollFactor;
    private View                    mBackgroundView;
    private View                    mContentView;
    private int                     mShadowDrawableResId;
    private int                     mShadowHeight;
    private boolean                 mShowShadow;
    private View                    mPlaceholderView;
    private int                     mPlaceholderTop;
    private View                    mHeaderView;
    private boolean                 mHasHeaderView;
    private boolean                 mIsHeaderStuck;
    private OnHeaderStuckListener   mOnHeaderStuckListener;


    public ParallaxScrollView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public interface OnHeaderStuckListener {
        public void onHeaderStuck(boolean isStuck);
    }

    public void init() {
        mScrollFactor = DEFAULT_SCROLL_FACTOR;
        mShadowDrawableResId = -1;
        mShadowHeight = 4;
        mShowShadow = true;
        mPlaceholderTop = Integer.MIN_VALUE;
        mHeaderView = null;
        mHasHeaderView = false;
        mIsHeaderStuck = false;
    }

    public void setOnHeaderStuckListener(OnHeaderStuckListener listener) {
        mOnHeaderStuckListener = listener;
    }

    /**
     * Sets the View that will scroll in parallax (the background view behind the main
     * content). Typically this is an {@link android.widget.ImageView}, but it can be any View.
     * @param backgroundView    The View that will scroll in parallax to the main content.
     */
    public void setBackgroundView(View backgroundView) {
        mBackgroundView = backgroundView;
        if (mBackgroundView.getId() == NO_ID)
            mBackgroundView.setId(BACKGROUND_VIEW_ID);
    }

    /**
     * Sets the View that holds the main content. This view will scroll normally on the
     * screen, with the background View scrolling in parallax behind it. This View ought to
     * be a ViewGroup, or some View that extends lower than the screen dimensions, otherwise
     * the parallax effect will be unobservable. This should <i>not</i> be a {@link android.widget.ListView}
     * or {@link android.widget.GridView}, as those are contained within ScrollViews of their own.
     * Nesting {@link android.widget.ScrollView}s is ill-advised by the Android documentation,
     * and will not achieve the desired parallax effect anyway.
     * @param contentView   The View (recommended to be a ViewGroup) that will hold the main
     *                      content.
     */
    public void setContentView(View contentView) {
        mContentView = contentView;
        if (mContentView.getId() == NO_ID)
            mContentView.setId(CONTENT_VIEW_ID);
    }

    /**
     * Sets the pace that the background view scrolls in parallax with respect to the scrolling
     * of the main content above it. Setting a scroll factor of 0.0 will cause the background
     * view to not scroll independently, and will move with the content as if in the same ScrollView.
     * Setting a scroll factor of 1.0 will.
     * @param scrollFactor  A factor [0.0,1.0] defining the scroll pace of the background view.
     */
    public void setScrollFactor(float scrollFactor) {
        mScrollFactor = scrollFactor;
    }

    /**
     * Sets whether or not a shadow should be displayed over the background. This creates the
     * illusion that the main content is on top of the background, which is why the background
     * scrolls slower. The shadow drawable itself can be changed in
     * {@link com.krg.ParallaxScrollView.ParallaxScrollView#setShadowResId(int)}.
     * @param showShadow    Set to true if a shadow should be shown, false otherwise.
     */
    public void showShadow(boolean showShadow) {
        mShowShadow = showShadow;
    }

    /**
     * Sets the resource id for the drawable that should appear as a shadow overlaying the
     * background. The shadow creates the illusion that the content is above the background,
     * and it is recommended to use a to-transparent gradient of some kind.
     * @param shadowResId   The resource id of the Drawable to be used as the shadow.
     */
    public void setShadowResId(int shadowResId) {
        mShadowDrawableResId = shadowResId;
    }

    /**
     * Sets the height of the shadow overlaid on top of the background view.
     * @param shadowHeight  The desired height for the shadow to be (in pixels).
     */
    public void setShadowHeight(int shadowHeight) {
        mShadowHeight = shadowHeight;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        mHasHeaderView = true;
    }

    /**
     * Obtains the first and second children of the ParallaxScrollView in XML, which become
     * the background and content views, respectively. This is only called when this view is
     * created in an XML layout, and not when made in code. (If there are less than two children, an error will
     * be thrown. If there are more, the extra views will be omitted from the final layout.) The first child will
     * be treated as the background, and will scroll in parallax to the main content. The second child will be the
     * main content; it ought to be contained within some ViewGroup.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() != 2) throw new IllegalStateException("ParallaxScrollView must have 2 children.");

        // Retrieve the background view from the layout.
        mBackgroundView = getChildAt(0);

        // Retrieve the content view from the layout.
        mContentView = getChildAt(1);

        int headerViewCount = 0;
        if (mContentView instanceof ViewGroup) {
            int contentCount = ((ViewGroup) mContentView).getChildCount();
            for (int i = 0; i < contentCount; i++) {
                if (((ViewGroup) mContentView).getChildAt(i) instanceof ScrollHeaderView) {
                    headerViewCount++;
                    mHeaderView = ((ViewGroup) mContentView).getChildAt(i);
                }
            }
        }

        if (headerViewCount > 1) throw new IllegalStateException("ParallaxScrollView can only have 1 header view.");

        mHasHeaderView = (mHeaderView != null);
    }

    /**
     * Gets called when the layout changes. If a layout change has occurred,
     * rectify any unintended shifts of the background view which may have occurred.
     * @see android.widget.FrameLayout#onLayout(boolean, int, int, int, int)
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed)
            parallaxScrollBackgroundView(getScrollY());
    }

    /**
     * Gets called when the ParallaxScrollView becomes initialized in a layout. Build the layout
     * from the current children.
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // With saved instances of all necessary views, remove them all from the layout, so all that is
        // present is an empty parent ParallaxScrollView.
        removeAllViews();
        createParallaxScrollView();
    }

    /**
     * Restructures the views and forms them into the proper setup for parallax scrolling. When defined in XML,
     * the ParallaxScrollView should have only two subviews. Upon completion of this method, what remains in
     * the View hierarchy is the following:
     *      ParallaxScrollView
     *          | ExtendedScrollView
     *              | RelativeLayout
     *                  | Background View
     *                  | Main content View
     *                  | Shadow View (if visible)
     * The nature of this hierarchy is necessitated by a ScrollView's only holding a single child. This is abstracted
     * away from the programmer utilizing this View by requiring him/her to supply the parent ParallaxScrollView with
     * two children: one for the background and another for the main content.
     */
    private void createParallaxScrollView() {
        int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
        int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;

        RelativeLayout.LayoutParams contentsLp = new RelativeLayout.LayoutParams(matchParent, wrapContent);
        contentsLp.addRule(RelativeLayout.BELOW, mBackgroundView.getId());
        mContentView.setLayoutParams(contentsLp);

        // Create the shadow view, depending on member variables set by methods, and set it to be placed
        // above the main content. This provides the illusion that the main content is above the background.
        View shadowView = new View(mContext);
        RelativeLayout.LayoutParams shadowLp = new RelativeLayout.LayoutParams(matchParent, mShadowHeight);
        shadowLp.addRule(RelativeLayout.ABOVE, mContentView.getId());
        shadowView.setLayoutParams(shadowLp);
        if (mShowShadow && mShadowDrawableResId != -1) shadowView.setBackgroundResource(mShadowDrawableResId);

        // Create a RelativeLayout and add the background, main content, and shadow views. This layout serves
        // as a container since a ScrollView can take only a single child View.
        RelativeLayout newContainer = new RelativeLayout(mContext);
        newContainer.setLayoutParams(new RelativeLayout.LayoutParams(matchParent, wrapContent));
        newContainer.addView(mBackgroundView, 0);
        newContainer.addView(mContentView, 1);
        newContainer.addView(shadowView, 2);

        // Create an ObservableScrollView to contain the RelativeLayout from above.
        final ObservableScrollView scrollView = new ObservableScrollView(getContext());
        scrollView.setCallbacks(this);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScroll(0, scrollView.getScrollY());
            }
        });
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(matchParent, matchParent));
        scrollView.setFillViewport(true);

        // If the ParallaxScrollView has a header, find the placeholder and create an additional FrameLayout.
        if (mHasHeaderView) {
            mPlaceholderView = replaceViewInLayoutWithPlaceholder(mHeaderView, (ViewGroup)mContentView);

            FrameLayout frame = new FrameLayout(getContext());
            frame.setLayoutParams(new RelativeLayout.LayoutParams(matchParent, matchParent));
            frame.addView(newContainer);
            frame.addView(mHeaderView);
            scrollView.addView(frame);

        // If there is no header, simply add the RelativeLayout that was created above to the ObservableScrollView.
        } else {
            scrollView.addView(newContainer);
        }

        // Add the ScrollView to the previously-empty ParallaxScrollView, and the layout is complete.
        addView(scrollView);
    }

    private View replaceViewInLayoutWithPlaceholder(final View viewToReplace, ViewGroup parent) {
        int index = parent.indexOfChild(viewToReplace);
        if (index != -1) {
            mHeaderView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int height = mHeaderView.getMeasuredHeight();
            int width = mHeaderView.getMeasuredWidth();

            View placeholder = new View(getContext());
            placeholder.setLayoutParams(new ViewGroup.LayoutParams(width, height));
            placeholder.setBackgroundColor(0x00000000);
            parent.removeViewAt(index);
            parent.addView(placeholder, index);
            return placeholder;
        } else {
            return null;
        }
    }

    /**
     * Scrolls the background view in parallax to the main content. This is accomplished by translating
     * the y-value of the background view at a rate proportional to the scrolling of the main view (that
     * rate is determined by the scroll factor). Note that this action is unsupported by versions of
     * Android lower than API level 11. If the device is not higher than Honeycomb, no parallax scrolling
     * will occur, and the background will scroll inline with the content.
     * @param scrollPosition    The vertical position at which the parent ScrollView of the background
     *                          view has scrolled.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void parallaxScrollBackgroundView(int scrollPosition) {
        if (mBackgroundView != null) {
            int translationY = (int) (scrollPosition * mScrollFactor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                mBackgroundView.setTranslationY(translationY);
        }
    }

    @Override
    public void onScroll(int scrollX, int scrollY) {
        if (mHasHeaderView) {
            // Calculate distance from top of header to top of screen (minus ActionBar and status bar).
            int[] d = new int[2];
            mPlaceholderView.getLocationOnScreen(d);
            if (mPlaceholderTop == Integer.MIN_VALUE) mPlaceholderTop = d[1] - windowTopDisplacement();

            // The header is not stuck if scrollY < mPlaceholderTop. If it was before the scroll,
            // invoke the callback, since the header is no longer stuck. Similarly for the other
            // case.
            if (scrollY < mPlaceholderTop) {
                if (mIsHeaderStuck)
                    if (mOnHeaderStuckListener != null) mOnHeaderStuckListener.onHeaderStuck(false);
                mIsHeaderStuck = false;
            } else if (scrollY >= mPlaceholderTop) {
                if (!mIsHeaderStuck)
                    if (mOnHeaderStuckListener != null) mOnHeaderStuckListener.onHeaderStuck(true);
                mIsHeaderStuck = true;
            }

            mHeaderView.setTranslationY(Math.max(mPlaceholderTop, scrollY));
        }

        parallaxScrollBackgroundView(scrollY);
    }

    /**
     * Calculates the displacement at the top of the window, caused by the ActionBar and status bar.
     * @return  The pixel height of the offset.
     */
    public int windowTopDisplacement() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = getResources().getDimensionPixelSize(resourceId);
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            result += TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        return result;
    }
}