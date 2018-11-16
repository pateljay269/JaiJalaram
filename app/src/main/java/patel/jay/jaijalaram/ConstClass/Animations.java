package patel.jay.jaijalaram.ConstClass;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Jay on 14-Feb-18.
 */

public class Animations implements Animator.AnimatorListener {

    private static float fX = 1.0f, fY = 0.0f;

    public Animations() {
    }

    //region Obj Methods

    @NonNull
    public static ObjectAnimator setObjectAnim(View view, boolean action) {
        if (action) {
            fX = 0.0f;
            fY = 1.0f;
        } else {
            fX = 1.0f;
            fY = 0.0f;
        }
        return ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", fX, fY),
                PropertyValuesHolder.ofFloat("scaleY", fX, fY),
                PropertyValuesHolder.ofFloat("Alpha", fX, fY));
    }

    public static void AlphaObj(View view) {

        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0, 1);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, alpha);
        animator.setDuration(1000);
        animator.start();
    }

    public static void ScaleObj(View view) {

        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0, 1);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0, 1);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleY, scaleX);
        animator.setDuration(1000);
        animator.start();
    }

    public static void TransObj(View view) {

        PropertyValuesHolder translateX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0, 100);
        PropertyValuesHolder translateY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0, 100);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, translateX, translateY);
        animator.setDuration(1000);
        animator.start();
    }
    //endregion

    public static void Alpha(View view, long time) {
        fX = 0.0f;
        fY = 1.0f;
        AlphaAnimation alpha = new AlphaAnimation(fX, fY);
        alpha.setDuration(time);
        view.startAnimation(alpha);
    }

    public static void Rotate(View view, long time) {
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setFillAfter(true);
        rotate.setDuration(time);
        view.startAnimation(rotate);
    }

    public static void Scale(View view, long time) {
        fX = 0.0f;
        fY = 1.0f;
        ScaleAnimation scale = new ScaleAnimation(fX, fY, fX, fY);
        scale.setDuration(time);
        view.startAnimation(scale);
    }

    public static void Translate(View view, long time) {
        fX = 0.0f;
        fY = 0.0f;
        float tX = 100.0f;
        float tY = 100.0f;
        TranslateAnimation translate = new TranslateAnimation(fX, tX, fY, tY);
        translate.setDuration(time);
        view.startAnimation(translate);
    }

    public static void MultipleAnim(View view, long time) {
        fX = 0.0f;
        fY = 1.0f;
        AlphaAnimation alpha = new AlphaAnimation(fX, fY);
        alpha.setDuration(time);
        view.startAnimation(alpha);

        ScaleAnimation scale = new ScaleAnimation(fX, fY, fX, fY);
        scale.setDuration(time);

        RotateAnimation rotate = new RotateAnimation(0, 360);
        rotate.setDuration(time);

        float tX = -300.0f;
        TranslateAnimation trans = new TranslateAnimation(-tX, fY, -tX, fY);
        trans.setDuration(time);

        AnimationSet merge = new AnimationSet(true);
        merge.addAnimation(alpha);
        merge.addAnimation(scale);
        merge.addAnimation(rotate);
        merge.addAnimation(trans);

        view.startAnimation(merge);
    }

    //region Listner Methods
    @Override
    public void onAnimationStart(Animator animation, boolean isReverse) {

    }

    @Override
    public void onAnimationEnd(Animator animation, boolean isReverse) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    //endregion

}