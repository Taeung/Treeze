package com.example.androidmind.adapter;

import java.util.ArrayList;

import com.example.adroidmind.data.Class;
import com.example.adroidmind.data.Lecture;
import com.example.androidmind.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ClassAdapter extends ArrayAdapter<Class>{

	private LayoutInflater mInflater;

	public ClassAdapter(Context context, int layoutResource,
			ArrayList<Class> object) {

		// ���� Ŭ������ �ʱ�ȭ ����
		// context, 0, �ڷᱸ��
		super(context, layoutResource, object);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	// �������� ��Ÿ���� �ڽ��� ���� xml�� ���̱� ���� ����
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		View view = null;

		// ���� ����Ʈ�� �ϳ��� �׸� ���� ��Ʈ�� ���

		if (v == null) {

			// XML ���̾ƿ��� ���� �о ����Ʈ�信 ����
			view = mInflater.inflate(R.layout.classitem, null);
		} else {

			view = v;
		}

		// �ڷḦ �޴´�.
		final Class classObject = this.getItem(position);

		if (classObject != null) {
			TextView classTV = (TextView) view.findViewById(R.id.classitemTV);
			classTV.setText(classObject.getClassName());
//			// ȭ�� ���
//			ImageView onlineImgView = (ImageView) view.findViewById(R.id.lectureonline);
//			TextView subjectTV = (TextView) view.findViewById(R.id.subjectTV);
//			TextView professorTV = (TextView) view
//					.findViewById(R.id.professorTV);
//
//			subjectTV.setText(lecture.getLectureName());
//
//			professorTV.setText(lecture.getProfessorName());
//			if(lecture.getStateOfLecture()){
//				onlineImgView.setBackgroundResource(R.drawable.online);
//			}
//			else{
//				onlineImgView.setBackgroundResource(R.drawable.offline);
//			}

		}

		return view;

	}



}
