package com.huangyezhaobiao.view;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyezhaobiao.R;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class WubaDialog extends Dialog {
	
	
	
	public static interface OnBackListener{
		/**
		 * 濡傛灉杩斿洖涓簍rue锛岃〃绀哄鐞嗕簡杩欎釜杩斿洖浜嬩欢
		 * @return
		 */
		boolean onBack();
	}
	
	private OnBackListener mBackListener;
	
    public void setBackListener(OnBackListener backListener) {
		this.mBackListener = backListener;
	}

	public WubaDialog(Context context, int theme) {
        super(context, theme);
        // 璁剧疆鐐瑰嚮灞忓箷Dialog娑堝け  
        setCanceledOnTouchOutside(true);
        //缁檇ialog璁剧疆椋炲叆椋炲嚭鍔ㄧ敾
        Window window = getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        
    }

    public WubaDialog(Context context) {
        this(context,0);
    }
    
    @Override
    public void onBackPressed() {
    	if(mBackListener != null && mBackListener.onBack()) {
    		return ;
    	}
    	super.onBackPressed();
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;
        private String title;
        private int titleGravity;
        private boolean hasTitleIcon;
        private int iconId;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View customView;
        private View mListViewPanel;
        private OnBackListener mBackListener;
        private LayoutInflater mInflater;

        private OnClickListener
                        positiveButtonClickListener,
                        negativeButtonClickListener;
		private View layout;
		private View rl;

        public Builder(Context context) {
        	mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            
        }

        /**
         * Set the Dialog message from String
         * @param title
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         * @param title
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            return setTitle(title, true);
        }
        // 鎶奃ialog鐨勯粯璁や负鍚戝乏瀵�
        public Builder setTitle(String title, boolean hasIcon) {
        	return setTitle(title, Gravity.LEFT, hasIcon, 0);
        }
        public Builder setTitle(String title, int gravity, boolean hasIcon, int iconId) {
            this.title = title;
            this.titleGravity = gravity;
            this.hasTitleIcon = hasIcon;
            this.iconId = iconId;
            return this;
        }
        
        /**
         * Set a custom content view for the Dialog.
         * If a message is set, the contentView is not
         * added to the Dialog...
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.customView = v;
            return this;
        }
        public Builder setContentView(int resId) {
        	this.customView = mInflater.inflate(resId, null);
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the positive button text and it's listener
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText,
                OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText,
                OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button text and it's listener
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText,
                OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }
        
        
        
        public Builder setOnBackListener(OnBackListener backListener){
        	this.mBackListener = backListener;
        	return this;
        }

        /**
         * Create the custom dialog
         */
        public WubaDialog create() {
            // instantiate the dialog with the custom Theme
            final WubaDialog dialog = new WubaDialog(context, 
            		R.style.RequestDialog);
            
            layout = mInflater.inflate(R.layout.request_dialog, null);
            
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            
            // 鏍囬
            setupTitle(layout);
            
            // 鎸夐挳
            boolean hasButtons = setupButtons(dialog, layout);
            
            // 鍐呭
            setupContent(layout);
            
            // 璁剧疆鑳屾櫙
            setBackground(layout, hasButtons);
            
            if(mBackListener != null) {
            	dialog.setBackListener(mBackListener);
            }
            
            dialog.setContentView(layout);
            
           
            return dialog;
        }

		private void showAnimator(View rl2) {
			AnimatorSet set = new AnimatorSet();
			ObjectAnimator alpha = ObjectAnimator.ofFloat(rl,"alpha", 0,1);
			ObjectAnimator translate = ObjectAnimator.ofFloat(rl, "translationY", -300,0);
			alpha.setDuration(100);
			translate.setInterpolator(new OvershootInterpolator(1.4f));
			translate.setDuration(600);
			set.setDuration(600);
			set.playTogether(alpha,translate);
			set.start();
		}

		private void setupTitle(View layout) {
			LinearLayout topPanel = (LinearLayout) layout.findViewById(R.id.topPanel);
			
			TextView titleText = ((TextView)topPanel.findViewById(R.id.title));
//			ImageView iconImage = (ImageView)topPanel.findViewById(R.id.icon);
			
//			if(!hasTitleIcon) {
//				iconImage.setVisibility(View.GONE);
//			} else if(iconId != 0) {
//				iconImage.setImageResource(iconId);
//			}
			if(titleGravity == Gravity.CENTER_HORIZONTAL) {
				RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				titleText.setLayoutParams(titleParams);
			}
			titleText.setText(title);
		}
		
		private boolean setupButtons(final Dialog dialog, View layout) {
			if(positiveButtonText == null && negativeButtonText == null) {
				layout.findViewById(R.id.buttonPanel).setVisibility(View.GONE);
            	return false;
            }
			
			// 纭畾鎸夐挳
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // 璁剧疆鍙栨秷鎸夐挳
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            
            return true;
		}
		
		private void setupContent(View layout) {
			LinearLayout contentPanel = (LinearLayout) layout
					.findViewById(R.id.contentPanel);

			if (message != null) {
				final TextView messageView = ((TextView) layout.findViewById(R.id.message));
				messageView.setText(message);
//				messageView.post(new Runnable() {
//					@Override
//					public void run() {
//						Layout layout = messageView.getLayout();
//						android.view.ViewGroup.LayoutParams params = messageView.getLayoutParams();
//						if(layout != null && params != null) {
//							params.height = layout.getLineTop(layout.getLineCount());
//							messageView.invalidate();
//						}
//					}
//				});
			} 
			else if (mListViewPanel != null) {
				contentPanel.removeAllViews();

				contentPanel.addView(mListViewPanel, new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			} else if(customView != null){
				contentPanel.removeAllViews();

				contentPanel.addView(customView, new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}
		
		private void setBackground(View layout, boolean hasButtons) {
//	        int bottomDark = R.drawable.z_dialog_footer;
//	        
//	        if(!hasButtons) {
//	        	layout.findViewById(R.id.content_wrap).setBackgroundResource(bottomDark);
//	        }
		}
		
    }

    
    
}