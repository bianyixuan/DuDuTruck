package com.passenger.util;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3D extends Animation {
	private float fromDegree;	// ��ת��ʼ�Ƕ�
	private float toDegree;		// ��ת��ֹ�Ƕ�
	private float mCenterX;		// ��ת����x
	private float mCenterY;		// ��ת����y
	private Camera mCamera;

	public Rotate3D(float fromDegree, float toDegree, float depthZ, float centerX, float centerY) {
		this.fromDegree = fromDegree;
		this.toDegree = toDegree;
		this.mCenterX = centerX;
		this.mCenterY = centerY;
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}


	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final float FromDegree = fromDegree;
		float degrees = FromDegree + (toDegree - fromDegree) * interpolatedTime;	// ��ת�Ƕȣ�angle��
		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Matrix matrix = t.getMatrix();
		

		if (degrees <= -76.0f) {
			degrees = -90.0f;
			mCamera.save();
			mCamera.rotateY(degrees);		// ��ת
			mCamera.getMatrix(matrix);
			mCamera.restore();
		} else if (degrees >= 76.0f) {
			degrees = 90.0f;
			mCamera.save();
			mCamera.rotateY(degrees);
			mCamera.getMatrix(matrix);
			mCamera.restore();
		} else {
			mCamera.save();
			mCamera.translate(0, 0, centerX);		// λ��x
			mCamera.rotateY(degrees);
			mCamera.translate(0, 0, -centerX);
			mCamera.getMatrix(matrix);
			mCamera.restore();
		}

		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}
}



