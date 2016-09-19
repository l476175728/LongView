package com.example.liuhailong.longviewsample.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

/**
 * Created by liuhailong on 16/6/30.
 */
public class CustomShowHeaderListView extends ListView {


        /** 继承Apapter来判断是不是固定的item */
        public static interface PinnedSectionListAdapter extends ListAdapter {
            /** 如果返回true则listView上面的type则保持不动 */
            boolean isItemViewTypePinned(int viewType);
        }

        /**记录布局和他所在的位置. */
        static class PinnedSection {
            public View view;
            public int position;
            public long id;
        }

        // 处理触摸事件
        private final Rect mTouchRect = new Rect();
        private final PointF mTouchPoint = new PointF();
        private int mTouchSlop;
        private View mTouchTarget;
        private MotionEvent mDownEvent;

        // 画出来上面不动的type的阴影部分
        private GradientDrawable mShadowDrawable;
        private int mSectionsDistanceY;
        private int mShadowHeight;

        /** listView滚动时的监听 可以为空 */
        OnScrollListener mDelegateOnScrollListener;

        /** 阴影部分,可以为空. */
        PinnedSection mRecycleSection;

        /** shadow instance with a pinned view, can be null. */
        PinnedSection mPinnedSection;

        /** Y-translation固定视图. 用来固定下一个section */
        int mTranslateY;

        /** listView的滑动监听,当item切换的时候调用此方法*/
        private final OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (mDelegateOnScrollListener != null) { // delegate
                    mDelegateOnScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {



                if (mDelegateOnScrollListener != null) { // delegate
                    mDelegateOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }

                // 获取适配器
                ListAdapter adapter = getAdapter();


                if (adapter == null || visibleItemCount == 0) return; //不做任何操作
                Log.d("返回的结果",firstVisibleItem+"..."+visibleItemCount+"..."+totalItemCount+"..."+adapter.getItemViewType(firstVisibleItem));
                final boolean isFirstVisibleItemSection =
                        isItemViewTypePinned(adapter, adapter.getItemViewType(firstVisibleItem));

                if (isFirstVisibleItemSection) {
                    View sectionView = getChildAt(0);
                    if (sectionView.getTop() == getPaddingTop()) { // 布局固定在顶部时 去除阴影部分
                        destroyPinnedShadow();
                    } else { // 当不在顶部时,要确定阴影部分是否显示
                        ensureShadowForPosition(firstVisibleItem, firstVisibleItem, visibleItemCount);
                    }

                } else { // item不在第一个
                    int sectionPosition = findCurrentSectionPosition(firstVisibleItem);
                    if (sectionPosition > -1) { //第一个item可见时
                        ensureShadowForPosition(sectionPosition, firstVisibleItem, visibleItemCount);
                    } else { // 第一个可见的item不存在时,取消shadow
                        destroyPinnedShadow();
                    }
                }
            };

        };

    //设置listview的最大高度
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//
//                MeasureSpec.AT_MOST);
//
//        super.onMeasure(widthMeasureSpec, expandSpec);
//
//    }

        /** 改变阴影 */
        private final DataSetObserver mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                recreatePinnedShadow();
            };
            @Override
            public void onInvalidated() {
                recreatePinnedShadow();
            }
        };

        //-- constructors

        public CustomShowHeaderListView(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView();
        }

//        public CustomShowHeaderListView(Context context, AttributeSet attrs, int defStyle) {
//            super(context, attrs, defStyle);
//            initView();
//        }

        private void initView() {
            setOnScrollListener(mOnScrollListener);
            mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            initShadow(true);
        }

        //设置是否显示阴影部分,false为不显示

        public void setShadowVisible(boolean visible) {
            initShadow(visible);
            if (mPinnedSection != null) {
                View v = mPinnedSection.view;
                invalidate(v.getLeft(), v.getTop(), v.getRight(), v.getBottom() + mShadowHeight);
            }
        }

        //画出第一个固定的item的阴影部分

        public void initShadow(boolean visible) {
            if (visible) {
                if (mShadowDrawable == null) {
                    mShadowDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                            new int[] { Color.parseColor("#ffa0a0a0"), Color.parseColor("#50a0a0a0"), Color.parseColor("#00a0a0a0")});
                    mShadowHeight = (int) (8 * getResources().getDisplayMetrics().density);
                }
            } else {
                if (mShadowDrawable != null) {
                    mShadowDrawable = null;
                    mShadowHeight = 0;
                }
            }
        }

        /** 创建阴影显示在固定item 的指定位置 */
        void createPinnedShadow(int position) {

            // 回收阴影
            PinnedSection pinnedShadow = mRecycleSection;
            mRecycleSection = null;

            // 创建一个新的阴影部分
            if (pinnedShadow == null) pinnedShadow = new PinnedSection();
            // 请求一个新的阴影部分如果需要的话
            View pinnedView = getAdapter().getView(position, pinnedShadow.view, CustomShowHeaderListView.this);

            //获取布局
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) pinnedView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = (ViewGroup.LayoutParams) generateDefaultLayoutParams();
                pinnedView.setLayoutParams(layoutParams);
            }

            int heightMode = MeasureSpec.getMode(layoutParams.height);
            int heightSize = MeasureSpec.getSize(layoutParams.height);

            if (heightMode == MeasureSpec.UNSPECIFIED) heightMode = MeasureSpec.EXACTLY;

            int maxHeight = getHeight() -getListPaddingTop() - getListPaddingBottom();
            if (heightSize > maxHeight) heightSize = maxHeight;

            // measure & layout
            int ws = MeasureSpec.makeMeasureSpec(getWidth() - getListPaddingLeft() - getListPaddingRight(), MeasureSpec.EXACTLY);
            int hs = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
            pinnedView.measure(ws, hs);
            pinnedView.layout(0, 0, pinnedView.getMeasuredWidth(), pinnedView.getMeasuredHeight());
            mTranslateY = 0;

            //初始化阴影部分
            pinnedShadow.view = pinnedView;
            pinnedShadow.position = position;
            pinnedShadow.id = getAdapter().getItemId(position);

            // 存储阴影部分
            mPinnedSection = pinnedShadow;
        }

        /** 销毁阴影在当前固定的item */
        void destroyPinnedShadow() {
            if (mPinnedSection != null) {
                //当固定的item被替换后,准备回收阴影
                mRecycleSection = mPinnedSection;
                mPinnedSection = null;
            }
        }

        /** 确定固定的item存在阴影部分 */
        void ensureShadowForPosition(int sectionPosition, int firstVisibleItem, int visibleItemCount) {

            if (mPinnedSection != null
                    && mPinnedSection.position != sectionPosition) { //初始化
                destroyPinnedShadow();
            }

            if (mPinnedSection == null) { // 阴影部分如果为空的话重新创建
                createPinnedShadow(sectionPosition);
            }

            int nextPosition = sectionPosition + 1;
            if (nextPosition < getCount()) {
                int nextSectionPosition = findFirstVisibleSectionPosition(nextPosition,
                        visibleItemCount - (nextPosition - firstVisibleItem));
                if (nextSectionPosition > -1) {
                    View nextSectionView = getChildAt(nextSectionPosition - firstVisibleItem);
                    final int bottom = mPinnedSection.view.getBottom() + getPaddingTop();
                    mSectionsDistanceY = nextSectionView.getTop() - bottom;
                    if (mSectionsDistanceY < 0) {
                        //下一个item的影子重叠,移除
                        mTranslateY = mSectionsDistanceY;
                    } else {
                        //不重叠就把他放在最上面
                        mTranslateY = 0;
                    }
                } else {
                    // 没有其他的item的阴影可见的话,就放在最上面
                    mTranslateY = 0;
                    mSectionsDistanceY = Integer.MAX_VALUE;
                }
            }

        }

        int findFirstVisibleSectionPosition(int firstVisibleItem, int visibleItemCount) {
            ListAdapter adapter = getAdapter();

            int adapterDataCount = adapter.getCount();
            if (getLastVisiblePosition() >= adapterDataCount) return -1; //没有其他的item的阴影可见的话,就放在最上面

            if (firstVisibleItem+visibleItemCount >= adapterDataCount){
                visibleItemCount = adapterDataCount-firstVisibleItem;
            }

            for (int childIndex = 0; childIndex < visibleItemCount; childIndex++) {
                int position = firstVisibleItem + childIndex;
                int viewType = adapter.getItemViewType(position);
                if (isItemViewTypePinned(adapter, viewType)) return position;
            }
            return -1;
        }

        int findCurrentSectionPosition(int fromPosition) {
            ListAdapter adapter =getAdapter();

            if (fromPosition >= adapter.getCount()) return -1; // 数据改变

            if (adapter instanceof SectionIndexer) {
                // try fast way by asking section indexer
                SectionIndexer indexer = (SectionIndexer) adapter;
                int sectionPosition = indexer.getSectionForPosition(fromPosition);
                int itemPosition = indexer.getPositionForSection(sectionPosition);
                int typeView = adapter.getItemViewType(itemPosition);
                if (isItemViewTypePinned(adapter, typeView)) {
                    return itemPosition;
                } // else, no luck
            }

            // try slow way by looking through to the next section item above
            for (int position=fromPosition; position>=0; position--) {
                int viewType = adapter.getItemViewType(position);
                if (isItemViewTypePinned(adapter, viewType)) return position;
            }
            return -1; // no candidate found
        }

        void recreatePinnedShadow() {
            destroyPinnedShadow();
            ListAdapter adapter = getAdapter();
            if (adapter != null && adapter.getCount() > 0) {
                int firstVisiblePosition = getFirstVisiblePosition();
                int sectionPosition = findCurrentSectionPosition(firstVisiblePosition);
                if (sectionPosition == -1) return; // 没有可现实的view,返回
                ensureShadowForPosition(sectionPosition,
                        firstVisiblePosition, getLastVisiblePosition() - firstVisiblePosition);
            }
        }

        @Override
        public void setOnScrollListener(AbsListView.OnScrollListener listener) {
            if (listener == mOnScrollListener) {
                super.setOnScrollListener(listener);
            } else {
                mDelegateOnScrollListener = listener;
            }
        }

        @Override
        public void onRestoreInstanceState(Parcelable state) {
            super.onRestoreInstanceState(state);
            post(new Runnable() {
                @Override
                public void run() { //当配置改变的时候,存储位置
                    recreatePinnedShadow();
                }
            });
        }

        @Override
        public void setAdapter(ListAdapter adapter) {

            //debug模式下
//            // assert adapter in debug mode
//            if (BuildConfig.DEBUG && adapter != null) {
//                if (!(adapter instanceof PinnedSectionListAdapter))
//                    throw new IllegalArgumentException("Does your adapter implement PinnedSectionListAdapter?");
//                if (adapter.getViewTypeCount() < 2)
//                    throw new IllegalArgumentException("Does your adapter handle at least two types" +
//                            " of views in getViewTypeCount() method: items and sections?");
//            }

            //取消就到adapter,变成新的adapter
            ListAdapter oldAdapter = getAdapter();
            if (oldAdapter != null) oldAdapter.unregisterDataSetObserver(mDataSetObserver);
            if (adapter != null) adapter.registerDataSetObserver(mDataSetObserver);

            // 如果新的adapter和旧的不一样 销毁阴影部分
            if (oldAdapter != adapter) destroyPinnedShadow();

            super.setAdapter(adapter);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            if (mPinnedSection != null) {
                int parentWidth = r - l - getPaddingLeft() - getPaddingRight();
                int shadowWidth = mPinnedSection.view.getWidth();
                if (parentWidth != shadowWidth) {
                    recreatePinnedShadow();
                }
            }
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);

            if (mPinnedSection != null) {

                // prepare variables
                int pLeft =getListPaddingLeft();
                int pTop = getListPaddingTop();
                View view = mPinnedSection.view;

                // draw child
                canvas.save();

                int clipHeight = view.getHeight() +
                        (mShadowDrawable == null ? 0 : Math.min(mShadowHeight, mSectionsDistanceY));
                canvas.clipRect(pLeft, pTop, pLeft + view.getWidth(), pTop + clipHeight);

                canvas.translate(pLeft, pTop + mTranslateY);
                drawChild(canvas, mPinnedSection.view, getDrawingTime());

                if (mShadowDrawable != null && mSectionsDistanceY > 0) {
                    mShadowDrawable.setBounds(mPinnedSection.view.getLeft(),
                            mPinnedSection.view.getBottom(),
                            mPinnedSection.view.getRight(),
                            mPinnedSection.view.getBottom() + mShadowHeight);
                    mShadowDrawable.draw(canvas);
                }

                canvas.restore();
            }
        }

        //重新事件的

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {

            final float x = ev.getX();
            final float y = ev.getY();
            final int action = ev.getAction();

            if (action == MotionEvent.ACTION_DOWN
                    && mTouchTarget == null
                    && mPinnedSection != null
                    && isPinnedViewTouched(mPinnedSection.view, x, y)) { // create touch target

                // user touched pinned view
                mTouchTarget = mPinnedSection.view;
                mTouchPoint.x = x;
                mTouchPoint.y = y;

                // copy down event for eventually be used later
                mDownEvent = MotionEvent.obtain(ev);
            }

            if (mTouchTarget != null) {
                if (isPinnedViewTouched(mTouchTarget, x, y)) { // forward event to pinned view
                    mTouchTarget.dispatchTouchEvent(ev);
                }

                if (action == MotionEvent.ACTION_UP) { // perform onClick on pinned view
                    super.dispatchTouchEvent(ev);
                    performPinnedItemClick();
                    clearTouchTarget();

                } else if (action == MotionEvent.ACTION_CANCEL) { // cancel
                    clearTouchTarget();

                } else if (action == MotionEvent.ACTION_MOVE) {
                    if (Math.abs(y - mTouchPoint.y) > mTouchSlop) {

                        // cancel sequence on touch target
                        MotionEvent event = MotionEvent.obtain(ev);
                        event.setAction(MotionEvent.ACTION_CANCEL);
                        mTouchTarget.dispatchTouchEvent(event);
                        event.recycle();

                        // provide correct sequence to super class for further handling
                        super.dispatchTouchEvent(mDownEvent);
                        super.dispatchTouchEvent(ev);
                        clearTouchTarget();

                    }
                }

                return true;
            }

            //如果不是重写的view,则调用父类的方法
            return super.dispatchTouchEvent(ev);
        }

        private boolean isPinnedViewTouched(View view, float x, float y) {
            view.getHitRect(mTouchRect);

            // by taping top or bottom padding, the list performs on click on a border item.
            // we don't add top padding here to keep behavior consistent.
            mTouchRect.top += mTranslateY;

            mTouchRect.bottom += mTranslateY + getPaddingTop();
            mTouchRect.left += getPaddingLeft();
            mTouchRect.right -= getPaddingRight();
            return mTouchRect.contains((int)x, (int)y);
        }

        private void clearTouchTarget() {
            mTouchTarget = null;
            if (mDownEvent != null) {
                mDownEvent.recycle();
                mDownEvent = null;
            }
        }

        private boolean performPinnedItemClick() {
            if (mPinnedSection == null) return false;

            AdapterView.OnItemClickListener listener =getOnItemClickListener();
            if (listener != null && getAdapter().isEnabled(mPinnedSection.position)) {
                View view =  mPinnedSection.view;
                playSoundEffect(SoundEffectConstants.CLICK);
                if (view != null) {
                    view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
                }
                listener.onItemClick(this, view, mPinnedSection.position, mPinnedSection.id);
                return true;
            }
            return false;
        }

        public static boolean isItemViewTypePinned(ListAdapter adapter, int viewType) {

            Log.d("绑定了吗",adapter+""+((PinnedSectionListAdapter) adapter).isItemViewTypePinned(viewType));
            return ((PinnedSectionListAdapter) adapter).isItemViewTypePinned(viewType);
        }


}
