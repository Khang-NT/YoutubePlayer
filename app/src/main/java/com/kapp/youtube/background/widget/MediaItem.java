package com.kapp.youtube.background.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.kapp.youtube.background.BuildConfig;
import com.kapp.youtube.background.R;
import com.kapp.youtube.background.model.IMedia;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by khang on 19/05/2016.
 * Email: khang.neon.1997@gmail.com
 */
public class MediaItem extends FrameLayout implements RippleView.OnRippleCompleteListener {
    private static final String TAG = "MediaItem";

    OnClickListener onClickListener;
    @BindView(R.id.rounded_image_view_thumbnail)
    RoundedImageView ivThumbnail;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.card_view)
    CardView cardView;
    @BindView(R.id.ripple_view)
    RippleView rippleView;

    public MediaItem(Context context) {
        this(context, null);
    }

    public MediaItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.media_item, this, true);
        ButterKnife.bind(this);
        rippleView.setOnRippleCompleteListener(this);

        if (isInEditMode() || BuildConfig.DEBUG) {
            setTitle("Kygo - Firestone ft. Conrad Sewell ");
            setDescription("902.271 views - KygoOfficialVEVO");
            setDuration("03:42");
        }
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onComplete(RippleView rippleView) {
        if (onClickListener != null)
            onClickListener.onClick(this);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setDescription(String description) {
        tvDescription.setText(description);
    }

    public void setDuration(String duration) {
        if (duration == null)
            tvDuration.setVisibility(GONE);
        else {
            tvDuration.setVisibility(VISIBLE);
            tvDuration.setText(duration);
        }
    }

    public void setCardBackgroundColor(int color) {
        cardView.setCardBackgroundColor(color);
        if (color == Color.WHITE) {
            tvTitle.setTextColor(Color.BLACK);
            tvDescription.setTextColor(Color.BLACK);
        } else {
            tvTitle.setTextColor(Color.WHITE);
            tvDescription.setTextColor(Color.WHITE);
        }
    }

    public void bindData(IMedia media) {
        setTitle(media.getTitle());
        setDescription(media.getDescription());
        setDuration(media.getDuration());
        setCardBackgroundColor(media.getCardColor());
        media.bindThumbnail(ivThumbnail, Picasso.with(getContext()));
    }
}
