package com.example.androidmind.adapter;

import java.util.ArrayList;


import com.example.adroidmind.data.MindNode;
import com.example.adroidmind.data.Ticket;
import com.example.androidmind.R;



import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

public class TicketAdapter extends ArrayAdapter<Ticket> {
	private LayoutInflater mInflater;

	public TicketAdapter(Context context, int layoutResource,
			ArrayList<Ticket> object) {
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
			view = mInflater.inflate(R.layout.ticketitem, null);
		} else {

			view = v;
		}

		// �ڷḦ �޴´�.
		final Ticket ticket = this.getItem(position);

		if (ticket != null) {
			// ȭ�� ���
			TextView ticketNo = (TextView) view.findViewById(R.id.ticketNo);
			TextView ticketTitle = (TextView) view
					.findViewById(R.id.ticketTitle);
			TextView ticketWriter = (TextView)view.findViewById(R.id.ticketWriter);
////			ImageView onlineBTN = (ImageView) view
////					.findViewById(R.id.onlineImg);
////			ImageView starBTN = (ImageView) view.findViewById(R.id.starImg);
//			// �ؽ�Ʈ��1�� getLabel()�� ��� �� ù��° �μ���
			if(ticket.getTicketNumber()>0){
           ticketNo.setText(""+ticket.getTicketNumber());
           ticketTitle.setText(ticket.getNodeStr());
           
			}
			else{
				String depthSpace = new String("");
				MindNode getdepthTemp = ticket.getParentNode();
				while(getdepthTemp instanceof Ticket){
					depthSpace = depthSpace+"    ";
					getdepthTemp = getdepthTemp.getParentNode();
				}
				ticketTitle.setText(depthSpace+ticket.getNodeStr());
			}
			ticketWriter.setText(ticket.getuserName());
           ticketWriter.setText(ticket.getuserName());
//			if (courses.isOnline()) {
//				onlineBTN.setBackgroundResource(R.drawable.nodebg);
//				starBTN.setBackgroundResource(R.drawable.nodebg);
//			}
//
//			else {
//				onlineBTN.setBackgroundResource(R.drawable.nodebg);
//				starBTN.setBackgroundResource(R.drawable.nodebg);
//			}

			// �̹����信 �ѷ��� �ش� �̹������� ���� �� ����° �μ���

		}

		return view;

	}

}
